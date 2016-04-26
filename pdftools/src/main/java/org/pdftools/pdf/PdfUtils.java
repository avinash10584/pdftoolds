package org.pdftools.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pdftools.pdf.PdfConstants.PDFPROPERTY;
import org.pdftools.utils.NumberEnum;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Utility class for merging PDF
 * 
 * @author asingh7!
 *
 */
public class PdfUtils {

    /**
     * This method can be used to merge multiple pdf byte[] into a single pdf
     * 
     * @param templates
     * @return
     * @throws PdfConversionException
     */
    public static byte[] merge(Collection<byte[]> templates) throws PdfConversionException {
        Document document = new Document();
        try (ByteArrayOutputStream mergedDocStream = new ByteArrayOutputStream()) {
            PdfCopy copy = new PdfCopy(document, mergedDocStream);
            document.open();
            for (byte[] template : templates) {
                if (template.length > 0) {
                    copy.addDocument(new PdfReader(template));
                }
            }
            document.close();
            return mergedDocStream.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in processMerge", e);
        }
    }

    /**
     * This method can be used to merge multiple pdf byte[] into a single pdf
     * 
     * @param templates
     * @param documentProperties
     *            this is used to set some basic properties like Author, Title
     *            etc on document
     * @return
     * @throws PdfConversionException
     */
    public static byte[] merge(Collection<byte[]> templates, Map<String, String> documentProperties)
            throws PdfConversionException {
        return processMerge(templates, null, null, documentProperties);
    }

    public static byte[] merge(Collection<byte[]> templates, PdfPrintOptions printOptions) throws PdfConversionException {
        return processMerge(templates, null, printOptions, null);
    }

    public static byte[] merge(Collection<byte[]> templates, PdfPageLayout pageLayout, PdfPrintOptions printOptions)
            throws PdfConversionException {
        return processMerge(templates, pageLayout, printOptions, null);
    }

    public static byte[] processMerge(Collection<byte[]> templates, PdfPageLayout pageLayout, PdfPrintOptions printOptions,
            Map<String, String> documentProperties) throws PdfConversionException {
        byte[] finalPdf = merge(templates);
        try (ByteArrayOutputStream mergedDocStream = new ByteArrayOutputStream()) {
            Document document = (pageLayout == null) ? new Document() : new Document(pageLayout.getPageSizeWithRotation());
            PdfWriter writer = PdfWriter.getInstance(document, mergedDocStream);
            document.open();
            addToPageWriter(document, writer, finalPdf);
            setPrintOptions(writer, printOptions);
            addDocumentProperties(document, documentProperties);
            document.close();
            finalPdf = mergedDocStream.toByteArray();
            if (printOptions != null) {
                finalPdf = PdfUtils.fitToPage(finalPdf, printOptions.getFitToPage());
                finalPdf = PdfUtils.scale(finalPdf, printOptions.getScaling());
            }
            return finalPdf;
        } catch (Exception e) {
            throw new PdfConversionException("Exception in processMerge", e);
        }
    }

    /**
     * Method to delete specific pages from pdf, this will be used in pdf
     * editing library
     * 
     * @param template
     * @param pageList
     * @return
     * @throws PdfConversionException
     */
    public static byte[] deletePages(byte[] template, List<Integer> pageList) throws PdfConversionException {
        Document document = new Document();
        try (ByteArrayOutputStream mergedDocStream = new ByteArrayOutputStream()) {
            PdfReader reader = new PdfReader(template);
            PdfCopy copy = new PdfCopy(document, mergedDocStream);
            document.open();
            PdfStamper stamper = new PdfStamper(reader, mergedDocStream);
            for (int i = 1; i < reader.getNumberOfPages() && !pageList.contains(i); i++) {
                PdfImportedPage importedPage = stamper.getImportedPage(reader, i);
                copy.addPage(importedPage);
            }
            copy.freeReader(reader);
            mergedDocStream.flush();
            document.close();
            return mergedDocStream.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in processMerge", e);
        }
    }

    /**
     * Fit to page can be used to fit the content of pdf inside the pdf document
     * size, this method does not change the size of pdf , it only shrinks the
     * content to fit it inside existing pdf
     * 
     * @param template
     * @param fitToPage
     * @return
     * @throws PdfConversionException
     */
    public static byte[] fitToPage(byte[] template, Integer fitToPage) throws PdfConversionException {
        if (fitToPage != null) {
            Document document = new Document();
            try (ByteArrayOutputStream mergedDocStream = new ByteArrayOutputStream()) {
                PdfWriter writer = PdfWriter.getInstance(document, mergedDocStream);
                document.open();
                PdfReader reader = new PdfReader(template);
                PdfContentByte cb = writer.getDirectContent();
                Rectangle pagesize = getPageSize(reader, 1);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    pagesize = getPageSize(reader, i);
                    document.setPageSize(pagesize);
                    document.newPage();
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    float offsetX = 0;
                    float offsetY = 0;
                    offsetX = Math
                            .abs((pagesize.getWidth() - (pagesize.getWidth() * fitToPage / NumberEnum.HUNDRED.getFloatValue()))
                                    / NumberEnum.TWO.getFloatValue());
                    offsetY = Math
                            .abs((pagesize.getHeight() - (pagesize.getHeight() * fitToPage / NumberEnum.HUNDRED.getFloatValue()))
                                    / NumberEnum.TWO.getFloatValue());
                    cb.addTemplate(page, fitToPage / NumberEnum.HUNDRED.getFloatValue(), 0, 0,
                            fitToPage / NumberEnum.HUNDRED.getFloatValue(), offsetX, offsetY);
                }
                document.close();
                return mergedDocStream.toByteArray();
            } catch (Exception e) {
                throw new PdfConversionException("Exception in fitToPage", e);
            }
        } else {
            return template;
        }
    }

    /**
     * This method can be used to resize pdf to a differnet scale , actual pdf
     * gets resized
     * 
     * @param template
     * @param scaling
     * @return
     * @throws PdfConversionException
     */
    public static byte[] scale(byte[] template, Integer scaling) throws PdfConversionException {
        if (scaling != null) {
            Document document = new Document();
            try (ByteArrayOutputStream mergedDocStream = new ByteArrayOutputStream()) {
                PdfWriter writer = PdfWriter.getInstance(document, mergedDocStream);
                document.open();
                PdfReader reader = new PdfReader(template);
                PdfContentByte cb = writer.getDirectContent();
                Rectangle pagesize = getPageSize(reader, 1);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    pagesize = getPageSize(reader, i);
                    document.setPageSize(getScaledPageSize(pagesize, scaling));
                    document.newPage();
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    cb.addTemplate(page, scaling / NumberEnum.HUNDRED.getFloatValue(), 0, 0,
                            scaling / NumberEnum.HUNDRED.getFloatValue(), 0, 0);
                }
                document.close();
                return mergedDocStream.toByteArray();
            } catch (Exception e) {
                throw new PdfConversionException("Exception in scale", e);
            }
        } else {
            return template;
        }
    }

    private static void addToPageWriter(Document document, PdfWriter writer, byte[] template)
            throws DocumentException, IOException {
        PdfContentByte cb = writer.getDirectContent();
        PdfReader reader = new PdfReader(template);
        Rectangle pagesize = getPageSize(reader, 1);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            pagesize = getPageSize(reader, i);
            document.setPageSize(pagesize);
            document.newPage();
            PdfImportedPage page = writer.getImportedPage(reader, i);
            cb.addTemplate(page, 0, 0);
        }
    }

    private static void addDocumentProperties(Document document, Map<String, String> documentProperties) {
        if (documentProperties != null) {
            document.addTitle(documentProperties.get(PDFPROPERTY.TITLE));
            document.addCreator(documentProperties.get(PDFPROPERTY.CREATOR));
            document.addKeywords(documentProperties.get(PDFPROPERTY.KEYWORDS));
            document.addAuthor(documentProperties.get(PDFPROPERTY.AUTHOR));
        }
    }

    private static void setPrintOptions(PdfWriter writer, PdfPrintOptions printOptions) {
        if (printOptions != null) {
            writer.addViewerPreference(PdfName.NUMCOPIES, new PdfNumber(printOptions.getCopies()));
        }
        PdfDestination magnify = new PdfDestination(PdfDestination.XYZ, -1, -1,
                PdfConstants.DEFAULT_VIEW_SCALING / NumberEnum.HUNDRED.getValue());
        PdfAction zoom = PdfAction.gotoLocalPage(1, magnify, writer);
        writer.setOpenAction(zoom);
        writer.addViewerPreference(PdfName.PAGEMODE, PdfName.FULLSCREEN);
        writer.addViewerPreference(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
        writer.addViewerPreference(PdfName.PRINTSCALING, PdfName.APPDEFAULT);
        writer.addViewerPreference(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
        writer.addViewerPreference(PdfName.PICKTRAYBYPDFSIZE, PdfBoolean.PDFTRUE);
    }

    private static Rectangle getScaledPageSize(Rectangle orignal, float scaling) {
        return new Rectangle(orignal.getWidth() * scaling / NumberEnum.HUNDRED.getFloatValue(),
                orignal.getHeight() * scaling / NumberEnum.HUNDRED.getFloatValue());
    }

    private static Rectangle getPageSize(PdfReader reader, int pagenumber) {
        Rectangle pagesize = reader.getPageSizeWithRotation(pagenumber);
        return new Rectangle(Math.min(pagesize.getWidth(), pagesize.getHeight()),
                Math.max(pagesize.getWidth(), pagesize.getHeight()));
    }
}
