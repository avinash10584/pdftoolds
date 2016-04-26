package org.pdftools.pdf.worker;

import static org.pdftools.pdf.PdfConstants.*;
import static org.pdftools.pdf.PdfConstants.HTMLConstant.*;
import static org.pdftools.pdf.PdfConstants.OCFIELD.*;
import static org.pdftools.pdf.PdfConstants.VelocityConstant.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.allcolor.xml.parser.CShaniDomParser;
import org.allcolor.xml.parser.dom.ADocument;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pdftools.pdf.PdfConstants;
import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageLayout;
import org.pdftools.pdf.PdfPrintOptions;
import org.pdftools.pdf.PdfUtils;
import org.pdftools.pdf.stamper.VerticalTextStamper;
import org.pdftools.utils.BusinessConstants;
import org.pdftools.utils.XYArray;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.util.XRLog;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


/**
 * Added to support PDF related tasks in OC
 * @author asingh7
 *
 */
public class HtmlPdfWorker extends AbstractPdfWorker {
    
    private ConverterType converter;    
    public enum ConverterType {ITEXT, FLYING_SAUCER, PDFTRON}; 
    
    public HtmlPdfWorker(ConverterType converter, String templateURI) {
        super(templateURI, new PdfPageLayout(), new PdfPrintOptions());
        this.converter = converter;
    }
    
    public HtmlPdfWorker(ConverterType converter, String templateURI, PdfPageLayout pageLayout,
            PdfPrintOptions printOptions) {
        super(templateURI, pageLayout, printOptions);
        this.converter = converter;
    }
    
    @Override
    public byte[] processTemplates(String formName, Map<String, String> bindingData) throws PdfConversionException {
        return processTemplates(formName, bindingData, null);
    }

    @Override
    public byte[] processTemplates(String formName, Map<String, String> bindingData, Map<String, String> continuationData)
            throws PdfConversionException {
        String formContent = null;
        try (InputStream in = new URL(getTemplateURI() + HTML_TMPLT_LOCATION + "/" + formName + 
                "/" + formName + HTML_EXTENSION).openStream()) {
            formContent = IOUtils.toString(in, FILE_ENCODING);
        } catch (IOException e) {
            throw new PdfConversionException("IOException in processTemplates", e);
        }
        return generateFormPdf(formName, formContent, bindingData, continuationData);
    }

    public byte[] processFormHtml(String formName, String formHtml, Map<Integer, Map<String, String>> bindData)
            throws PdfConversionException {
        List<byte[]> pagePdfList = new ArrayList<>();
        org.jsoup.nodes.Document doc = Jsoup.parse(formHtml);
        doc.outputSettings().prettyPrint(false);
        Elements pageList = doc.getElementsByClass(HTML_TMPLT_PAGE_CLASS);
        Iterator<org.jsoup.nodes.Element> pageIterator = pageList.iterator();
        int pageNo = 0;
        while (pageIterator.hasNext()) {
            org.jsoup.nodes.Element element = pageIterator.next();
            Map<String, String> pageBindData = element.hasAttr(HTML_TMPLT_PAGE_NUMBER_ATTR) ? bindData.get(pageNo)
                    : bindData.get(0);
            pagePdfList.add(generateFormPdf(formName, element.outerHtml(), pageBindData, pageBindData));
            pageNo++;
        }
        return PdfUtils.merge(pagePdfList);
    }
    
    private byte[] generateFormPdf(String formName, String formContent, Map<String, String> bindingData,
            Map<String, String> continuationData) throws PdfConversionException {
        byte[] processedForm = null;
        if (formContent != null) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                List<String> blackList = Arrays.asList(BLACKLIST_ATTR);
                org.jsoup.nodes.Document doc = Jsoup.parse(formContent);
                if (bindingData != null) {
                    Elements el = doc.getAllElements();
                    if (!Strings.isNullOrEmpty(bindingData.get(HTML_TMPLT_FIELD_HEADERAPPNUM))) {
                        bindingData.put(HTML_TMPLT_FIELD_APPNUM, bindingData.get(HTML_TMPLT_FIELD_HEADERAPPNUM));
                    }
                    for (Element element : el) {
                        Attributes at = element.attributes();
                        for (Attribute a : at) {
                            if (blackList.contains(a.getKey().toLowerCase())) {
                                element.removeAttr(a.getKey());
                            }
                        }
                        if (element.hasAttr(HTML_ATTR_DATA_FIELD) || element.hasClass(HTML_TMPLT_INPUT_CLASS)
                                || element.hasClass(HTML_TMPLT_INPUT_CLASS3)) {
                            processBindings(element, bindingData);
                        } else if (element.hasClass(HTML_TMPLT_HANDLEBAR)) {
                            element.remove();
                        } else if (element.hasClass(HTML_CLASS_ROTATE)) {
                            Font font = new Font(fArial, Float.valueOf(element.attr(PdfConstants.ATTR_VERTICAL_TEXT_FONT_SIZE)),
                                    Integer.valueOf(element.attr(PdfConstants.ATTR_VERTICAL_TEXT_FONT_WEIGHT)));
                            this.getStampers()
                                    .add(new VerticalTextStamper(element.text(),
                                            XYArray.getInstance(Float.valueOf(element.attr(PdfConstants.ATTR_VERTICAL_TEXT_X)),
                                                    Float.valueOf(element.attr(PdfConstants.ATTR_VERTICAL_TEXT_Y))),
                                            font));
                            element.text(PDF_BLANK_SPACE);
                        }
                    }
                }
                doc.outputSettings().prettyPrint(false);
                String updatedHtml = doc.html();
                org.jsoup.nodes.Document cleanedDoc = Jsoup.parse(updatedHtml);
                cleanedDoc.outputSettings().prettyPrint(false);
                Elements pageList = cleanedDoc.getElementsByClass(HTML_TMPLT_PAGE_CLASS);
                Iterator<org.jsoup.nodes.Element> pageIterator = pageList.iterator();
                List<byte[]> finalHTML = new ArrayList<>();
                while (pageIterator.hasNext()) {
                    org.jsoup.nodes.Element element = pageIterator.next();
                    boolean isContinuationPage = element.hasClass(HTML_TMPLT_CONTINUATION_PAGE_CLASS);
                    if (isContinuationPage && continuationData!=null && 
                            continuationData.isEmpty() && !getPrintOptions().getPrintEmptyContinuationPage()) {
                        continue;
                    } else {
                        String templatePageMargin = element.attr(HTML_TMPLT_PAGE_MARGIN_ATTR);
                        Map<String, String> rootTemplateData = new HashMap<>();
                        boolean isBoxLayout = element.hasClass(HTML_TMPLT_BOX_LAYOUT_CLASS);
                        rootTemplateData.put(VELOCITY_TMPLT_PARAM_FORM_NAME, formName);
                        rootTemplateData.put(VELOCITY_TMPLT_PARAM_FORM_CSS, formName + CSS_EXTENSION);
                        rootTemplateData.put(VELOCITY_TMPLT_PARAM_FORM_CONTENT, element.outerHtml());
                        rootTemplateData.put(VELOCITY_TMPLT_PARAM_PAGE_SIZE, getPageLayout().getPageSize()
                                + BusinessConstants.BLANK_SPACE
                                + (getPageLayout().isLandscape() ? PAGE_OREINTATION_LANDSCAPE : BusinessConstants.EMPTY_STRING));
                        rootTemplateData.put(VELOCITY_TMPLT_PARAM_PAGE_MARGIN,
                                getPageMargin(templatePageMargin, isBoxLayout, isContinuationPage));
                        String templateWithCss = processVelocityTemplate(COMMON_FORM_TEMPLATE, rootTemplateData);
                        templateWithCss = templateWithCss.replaceAll(HTML_BLANK_SPACE, PDF_BLANK_SPACE);
    
                        byte[] documentPdf = null;
                        switch (converter) {
                            case FLYING_SAUCER:
                                documentPdf = flyingSaucerConvertHtmlPdf(templateWithCss, getTemplateURI());
                                break;
                            case ITEXT:
                                documentPdf = iTextConvertHtmlPdf(templateWithCss, getTemplateURI());
                                break;
                        }
                        finalHTML.add(documentPdf);
                    }
                }
                processedForm = PdfUtils.merge(finalHTML);
            } catch (Exception e) {
                throw new PdfConversionException("Exception in generateFormPdf", e);
            }
        }
        return processedForm;
    }
    
    private String getPageMargin(String templatePageMargin, boolean isBoxLayout, boolean isContinuationPage) {
        String margin =  templatePageMargin;        
        if(margin == null  || margin.isEmpty()) {
            if(!isBoxLayout) {
                if(isContinuationPage) {
                    margin = VELOCITY_TMPLT_MARGIN_SECOND_PAGE;
                } else {
                    margin = VELOCITY_TMPLT_MARGIN_ZERO;
                }
            } else {
                margin = VELOCITY_TMPLT_MARGIN_ZERO;
            }
        }
        return String.format(VELOCITY_TMPLT_MARGIN_STYLE, margin);
    }
    
    private void processBindings(Element inputElement, Map<String, String> bindingData) {
        String inputElementTag = inputElement.tagName();
        String bindingId = inputElement.attr(HTML_TMPLT_DATA_ATTR);
        if(BusinessConstants.EMPTY_STRING.equals(bindingId)) {
            bindingId = inputElement.id();
        }
        String value = getBindingValue(bindingId, bindingData);
        switch (inputElementTag) {
        case HTML_TAG_INPUT:
            String inputType = inputElement.attr(HTML_ATTR_TYPE);
            switch (inputType) {
            case HTML_INPUT_CHECKBOX:
                processCheckbox(inputElement, value);
                break;
            default:
                processTextField(inputElement, bindingId, value);
                break;
            }
            break;
        case HTML_TAG_DIV:
            processEditableDiv(inputElement, bindingId, value);
            break;
        }
    }
    
    private byte[] flyingSaucerConvertHtmlPdf(String cleanedHtml, String context) throws DocumentException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            // renderer.getSharedContext().setScreenDpi(new Float(96f));            
            if (this.isEnableLogging()) {
                System.getProperties().setProperty(FLYING_SAUCER_LOGGER, Boolean.toString(Boolean.TRUE));
                XRLog.setLoggingEnabled(true);
            }
            final CShaniDomParser parser = new CShaniDomParser(true, false);
            parser.setAutodoctype(false);
            parser.setIgnoreDTD(true);
            ADocument mydoc = (ADocument) parser
                    .parse(new InputSource(new ByteArrayInputStream(cleanedHtml.getBytes(FILE_ENCODING))));
            renderer.setDocumentFromString(mydoc.toString(), context);
            renderer.layout();
            renderer.createPDF(bos);
        } catch (SAXException se) {
            throw new DocumentException(se);
        }
        return bos.toByteArray();
    }
    
    private byte[] iTextConvertHtmlPdf(String cleanedHtml, String context) throws DocumentException, IOException {        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, bos);
        document.open();
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        FileRetrieve retrieve = new FileRetrieveImpl(context);
        cssResolver.setFileRetrieve(retrieve); 
        
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        TagProcessorFactory factory = Tags.getHtmlTagProcessorFactory();
        htmlContext.setTagFactory(factory);
        htmlContext.autoBookmark(false);
 
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
 
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new StringReader(cleanedHtml)); 
        document.close();
        return bos.toByteArray();
    }
    
    private String getBindingValue(String bindingId, Map<String, String> bindingData) {
        String value = bindingData.get(bindingId);
        if(bindingId.equals(SB06_LARGE_ENTITY_SHOW) || bindingId.equals(SB06_SMALL_ENTITY) || 
                bindingId.equals(SB06_MICRO_ENTITY) ) {
            value = bindingData.get(SB06_ENTITY);
            if(value.equals(SB06_LARGE_ENTITY_VAL) && bindingId.equals(SB06_LARGE_ENTITY_SHOW)) {
                value = Boolean.toString(true);
            } else  if(value.equals(SB06_SMALL_ENTITY_VAL) && bindingId.equals(SB06_SMALL_ENTITY)) {
                value = Boolean.toString(true);
            } else  if(value.equals(SB06_MICRO_ENTITY_VAL) && bindingId.equals(SB06_MICRO_ENTITY)) {
                value = Boolean.toString(true);
            }                 
        }
        if(value !=null) {
            value = value.trim().replaceAll(HTML_BLANK_SPACE, PDF_BLANK_SPACE);
            //value = value.trim().replaceAll(BusinessConstants.BLANK_SPACE, PDF_BLANK_SPACE);
            value = value.trim().replaceAll(PdfConstants.LINE_BREAK, PdfConstants.HTMLConstant.HTML_LINE_BREAK);
        }
        return value;
    }
    
    private void processCheckbox(Element inputElement, String value) {
        if (value != null && Boolean.valueOf(value)) {
            inputElement.attr(HTML_ATTR_CHECKED, HTML_ATTR_CHECKED);
            inputElement.before(String.format(HTML_IMAGE_CHECKBOX,
                    inputElement.attr(HTML_ATTR_STYLE),
                    inputElement.attr(HTML_ATTR_CLASS),
                    IMAGE_CHECKED));
        } else {
            inputElement.removeAttr(HTML_ATTR_CHECKED);
            inputElement.before(String.format(HTML_CHECKBOX_EMPTY_HTML,
                    inputElement.attr(HTML_ATTR_STYLE),
                    inputElement.attr(HTML_ATTR_CLASS),
                    IMAGE_UNCHECKED));
        }
        inputElement.remove();
    }
    
    private void processTextField(Element inputElement, String bindingId, String value) {
        inputElement.before(String.format(INPUT_REPLACE, bindingId, 
                inputElement.attr(HTML_ATTR_STYLE), inputElement.attr(HTML_ATTR_CLASS), HTML_TMPLT_INPUT_REPLACE_CLASS,
                value == null || value.isEmpty() ? HTML_TMPLT_EMPTY_DATA_CLASS : HTML_TMPLT_HAS_DATA_CLASS,
                value == null || value.isEmpty() ?  blankSpaces(1) : value));
        inputElement.remove();
    }
    
    private void processEditableDiv(Element inputElement, String bindingId, String value) {
        if(inputElement.hasClass(HTML_TMPLT_CONTINUATION_CLASS) && value == null) {
            inputElement.remove();
        } else {
            if(!inputElement.hasClass(HTML_TMPLT_CONTINUATION_CLASS)) {
                inputElement.before(String.format(DIV_REPLACE, bindingId, 
                        inputElement.attr(HTML_ATTR_STYLE), inputElement.attr(HTML_ATTR_CLASS), HTML_TMPLT_DIV_REPLACE_CLASS,
                        value == null || value.isEmpty() ? HTML_TMPLT_EMPTY_DATA_CLASS : HTML_TMPLT_HAS_DATA_CLASS,
                        value == null || value.isEmpty() ? blankSpaces(1) : value));
                inputElement.remove();
            } else{
                inputElement.html(value);
            }
        }
    }    

    private static String processVelocityTemplate(String templateName, Map<String, String> bindingData) throws PdfConversionException {
        String formContent = BusinessConstants.EMPTY_STRING;
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, VELOCITY_RESOURCE_CLASSPATH);
            ve.setProperty(VELOCITY_CLASS_LOADER, ClasspathResourceLoader.class.getName());
            ve.init();
            Template contentTemplate = ve.getTemplate(templateName, FILE_ENCODING);
            VelocityContext context = new VelocityContext();
            for (String bindingId : bindingData.keySet()) {
                context.put(bindingId,
                        bindingData.get(bindingId) == null ? BusinessConstants.EMPTY_STRING : bindingData.get(bindingId));
            }
            StringWriter formContentWriter = new StringWriter();
            contentTemplate.merge(context, formContentWriter);
            formContent = formContentWriter.toString();
        } catch (Exception e) {
            throw new PdfConversionException(e);
        }
        return formContent;
    }
}
