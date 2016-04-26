package org.pdftools.pdf.worker;


import static org.pdftools.pdf.PdfConstants.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageLayout;
import org.pdftools.pdf.PdfPrintOptions;
import org.pdftools.pdf.PdfUtils;
import org.pdftools.pdf.PdfConstants.OCFORM;
import org.pdftools.utils.BusinessConstants;

import lombok.extern.slf4j.Slf4j;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Added to support AcroForm PDF bindings support and conversion to a single combined PDF
 * @author asingh7
 *
 */
@Slf4j
public class TemplatePdfWorker extends AbstractPdfWorker {

    private static final float FIELD_TOP_PADDING = 10;
    private static final float FIELD_LEFT_PADDING = 1;
    private static final String DEFAULT_FONT_APPEARANCE = "10.0";

    private static final int CONTINUATION_PAGE_NO = 2;
    private static final float CONTINUATION_TOP_PADDING = 80;
    private static final float CONTINUATION_LEFT_PADDING = 30;
    private static final float CONTINUATION_BOTTOM_PADDING = 40;
    private static final float CONTINUATION_LINE_SPACE = 12;
    private static final float CONTINUATION_FONT = 10.02f;

    private static final String BINDING_FLAG = "setfflags";

    public TemplatePdfWorker(String templateURI) {
        super(templateURI, new PdfPageLayout(), new PdfPrintOptions());
    }

    public TemplatePdfWorker(String templateURI, PdfPageLayout pageLayout, PdfPrintOptions printOptions) {
        super(templateURI, pageLayout, printOptions);
    }

    @Override
    public byte[] processTemplates(String formName, Map<String, String> bindingData) throws PdfConversionException {
        byte[] updatedPage = applyBindingsIText(formName, bindingData);
        return updatedPage;
    }

    @Override
    public byte[] processTemplates(String formName, Map<String, String> bindingData, Map<String, String> continuationData)
            throws PdfConversionException {
        byte[] updatedPage = null;
        updatedPage = applyBindingsIText(formName, bindingData);
        if (continuationData !=null && continuationData.size() > 0) {
            updatedPage = processContinuationPage(updatedPage, continuationData);
        } else {
            updatedPage = PdfUtils.deletePages(updatedPage, Arrays.asList(CONTINUATION_PAGE_NO));
        }
        return updatedPage;
    }

    /**
     * This method is used to apply binding data to an existing form template
     * created using Adobe Distiller Pro It uses the data from web forms and
     * apply to PDF template using IText
     *
     * @param formName
     * @param bindingData
     * @throws PdfConversionException
     */
	private byte[] applyBindingsIText(String formName, Map<String, String> bindingData)
		throws PdfConversionException {
		InputStream templateStream = null;
		PdfReader reader = null;
		PdfStamper stamper = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			if(getTemplateURI().contains(BusinessConstants.CLASSPATH_PREFIX)) {
				    templateStream = this.getClass().getClassLoader().getResourceAsStream(getTemplateURI().
					substring(BusinessConstants.CLASSPATH_PREFIX.length()) + formName);
			} else {
				File templateFile = new File(getTemplateURI()+ File.separator + formName);
				templateStream = new FileInputStream(templateFile);
			}
			reader = new PdfReader(templateStream);
			stamper = new PdfStamper(reader, bos);
			if (bindingData != null) {
    			for (String bindingId : bindingData.keySet()) {
    				String fieldId = bindingId;
    				updateBindingData(stamper, formName, fieldId, bindingData.get(bindingId));
    			}
			}
			stamper.setFormFlattening(true);
			stamper.close();
			return bos.toByteArray();
		} catch (Exception e) {
			throw new PdfConversionException("Exception in AcroFormPdfTemplateWorker : applyBindingsIText", e);
		} finally {
		    if (templateStream != null) {
		        try {
                    templateStream.close();
                } catch (IOException e) {
                    log.error("Could not close template input stream.", e);
                }
		    }
		}
	}

    private static void updateBindingData(PdfStamper stamper, String formName, String fieldId, String fieldData) throws
            DocumentException, IOException {
        switch (stamper.getAcroFields().getFieldType(fieldId)) {
            case AcroFields.FIELD_TYPE_CHECKBOX:
                String[] app = stamper.getAcroFields().getAppearanceStates(fieldId);
                if (Boolean.valueOf(fieldData)) {
                   stamper.getAcroFields().setField(fieldId, app[0]);
                }
                break;
            case AcroFields.FIELD_TYPE_TEXT:
                if (isUnderlineField(fieldId)) {
                    stamper.getAcroFields().setFieldProperty(fieldId, BINDING_FLAG, PdfAnnotation.FLAGS_HIDDEN, null);
                    replaceBindFieldWithColumnText(stamper, stamper.getAcroFields(), fieldId, fieldData, true);
                } else {
                    stamper.getAcroFields().setField(fieldId, fieldData);
                }
        }
    }

    @Deprecated
    private static void replaceBindFieldWithColumnText(PdfStamper stamper, AcroFields fields, String bindingId,
            String bindingData, boolean isUnderline) throws DocumentException, IOException {
        List<AcroFields.FieldPosition> pos = fields.getFieldPositions(bindingId);
        PdfContentByte contentBtye = stamper.getOverContent((int) pos.get(0).page);
        final PdfDictionary itemDict = (PdfDictionary) fields.getFieldItem(bindingId).getMerged(0);
        final PdfObject da = itemDict.get(PdfName.DA);
        String appearanceString = da.toString();
        String[] appearances = appearanceString.split(BusinessConstants.BLANK_SPACE);
        appearances[1] = BusinessConstants.EMPTY_STRING.equals(appearances[1]) ? DEFAULT_FONT_APPEARANCE : appearances[1];
        float fontSize = Float.valueOf(appearances[1]);
        float originalLength = pos.get(0).position.getRight() - pos.get(0).position.getLeft();
        String updatedBindingData = bindingData;
        if (isUnderline && bindingData == null) {
            int blankSpacesToAdd = normalizeSpace(originalLength, fArial, fontSize);
            updatedBindingData = blankSpaces(blankSpacesToAdd - 1);
        } else {
            int blankSpacesToAdd = normalizeSpace(originalLength -
                    fArial.getWidthPoint(bindingData, Float.valueOf(appearances[1])), fArial,
                    fontSize);
            updatedBindingData = bindingData + blankSpaces(blankSpacesToAdd - 1);
        }
        float leftPad = FIELD_LEFT_PADDING;
        float topPad = FIELD_TOP_PADDING;
        ColumnText ct = new ColumnText(contentBtye);
        Chunk bindingText = new Chunk(updatedBindingData);
        Font fieldFont = null;
        if (isUnderline) {
            fieldFont = new Font(fArial, Float.valueOf(appearances[1]), Font.UNDERLINE);
        } else {
            fieldFont = new Font(fArial, Float.valueOf(appearances[1]));
        }
        bindingText.setFont(fieldFont);
        Phrase p = new Phrase(bindingText);
        ct.setSimpleColumn(p, pos.get(0).position.getLeft() + leftPad,
                pos.get(0).position.getBottom(), pos.get(0).position.getRight(), pos.get(0).position.getTop() - topPad,
                0, Element.ALIGN_BOTTOM);
        ct.go();
    }

    private byte[] processContinuationPage(byte[] template, Map<String, String> overflowData) throws PdfConversionException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if(template !=null && overflowData.size() > 0) {
                reader = new PdfReader(template);
                stamper = new PdfStamper(reader, bos);
                PdfContentByte contentBtye = stamper.getOverContent(CONTINUATION_PAGE_NO);
                if (contentBtye != null) {
                    ColumnText continuationWrapper = new ColumnText(contentBtye);
                    Paragraph continuationParagraph = new Paragraph();

                    BaseFont fArial = BaseFont.createFont(ARIAL_FONT_FILE_PATH, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                    contentBtye.setFontAndSize(fArial, CONTINUATION_FONT);
                    for (String overflowId : overflowData.keySet()) {
                        stamper.getAcroFields().setFieldProperty(overflowId, BINDING_FLAG, PdfAnnotation.FLAGS_HIDDEN, null);
                        if (overflowData.get(overflowId) != null) {
                            Paragraph continuationField = new Paragraph(overflowData.get(overflowId));
                            addBlankLine(continuationField, 1);
                            continuationField.getFont().setSize(CONTINUATION_FONT);
                            continuationParagraph.add(continuationField);
                        }
                    }
                    PdfImportedPage page = stamper.getImportedPage(reader, CONTINUATION_PAGE_NO);
                    int continuationPage = CONTINUATION_PAGE_NO;
                    if(ColumnText.hasMoreText(continuationWrapper.go())) {
                        while(true) {
                            stamper.insertPage(++continuationPage, PageSize.LETTER);
                            stamper.getOverContent(continuationPage).addTemplate(page, 0, 0);
                            continuationWrapper.setCanvas(stamper.getOverContent(continuationPage));
                            continuationWrapper.setSimpleColumn(CONTINUATION_LEFT_PADDING,
                                    getPageLayout().getPageHeight() - CONTINUATION_TOP_PADDING,
                                    getPageLayout().getPageWidth() - CONTINUATION_LEFT_PADDING,
                                    CONTINUATION_BOTTOM_PADDING, CONTINUATION_LINE_SPACE, Element.ALIGN_LEFT);
                            if (!ColumnText.hasMoreText(continuationWrapper.go()))
                                break;
                        }
                    }
                }
                stamper.setFormFlattening(true);
            }
            stamper.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in AcroFormPdfTemplateWorker: processContinuationPage", e);
        }
    }

    @Deprecated
    private static boolean isUnderlineField(String bindingId) {
        return false;
    }

    private static void addBlankLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(BusinessConstants.BLANK_SPACE));
        }
    }

    @Deprecated
    private static void renameBindFields(PdfStamper stamper, int pageNo) throws DocumentException, IOException {
        AcroFields form = stamper.getAcroFields();
        Set<String> keys = new HashSet<>(form.getFields().keySet());
        for (String key : keys) {
            form.renameField(key, String.format(OCFORM.FIELD_RENAME_PATTERN, key, pageNo));
        }
    }
}
