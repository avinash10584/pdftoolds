package org.pdftools.pdf.worker;

import static org.pdftools.pdf.PdfConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageLayout;
import org.pdftools.pdf.PdfPageStamper;
import org.pdftools.pdf.PdfPrintOptions;
import org.pdftools.pdf.PdfWorker;

import com.itextpdf.text.pdf.BaseFont;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract Implementation of PdfWorker class to create Form PDF
 * @author asingh7!
 *
 */
@Slf4j
public abstract class AbstractPdfWorker implements PdfWorker {
    
    private Map<String,String> documentProperties;
    private PdfPageLayout pageLayout;
    private PdfPrintOptions printOptions;
    private String templateURI;    
    private List<PdfPageStamper> stampers = new ArrayList<>();    
    private boolean enableLogging = false;

    public AbstractPdfWorker(String templateURI, PdfPageLayout pageLayout, PdfPrintOptions printOptions) {
        super();
        this.pageLayout = pageLayout;
        this.templateURI = templateURI;
        this.setPrintOptions(printOptions);
    }
    
    @Override
    public byte[] processStampers(byte[] template) throws PdfConversionException {
        byte[] processedTemplate = template;
        for (PdfPageStamper stamper : getStampers()) {
            processedTemplate = stamper.stamp(processedTemplate, pageLayout.getPageSizeWithRotation());
        }
        return processedTemplate;
    }
    
    protected static String blankSpaces(int number) {
        StringBuilder blankSpace = new StringBuilder();
        for (int i = 0; i < number; i++) {
            blankSpace.append(PDF_BLANK_SPACE);
        }
        return blankSpace.toString();
    }

    protected static int normalizeSpace(float length, BaseFont font, float fontSize) {
        return (int) (Math.floor(length / font.getWidthPoint(PDF_BLANK_SPACE, fontSize)));
    }

    public PdfPageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PdfPageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    public String getTemplateURI() {
        return templateURI;
    }

    public void setTemplateURI(String templateURI) {
        this.templateURI = templateURI;
    }

    public Map<String,String> getDocumentProperties() {
        return documentProperties;
    }

    public void setDocumentProperties(Map<String,String> documentProperties) {
        this.documentProperties = documentProperties;
    }

    public PdfPrintOptions getPrintOptions() {
        return printOptions;
    }

    public void setPrintOptions(PdfPrintOptions printOptions) {
        this.printOptions = printOptions;
    }

    public List<PdfPageStamper> getStampers() {
        return stampers;
    }

    public void setStampers(List<PdfPageStamper> stampers) {
        this.stampers = stampers;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }    
}
