package org.pdftools.pdf;

import java.util.Map;
/**
 * Common interface for handling pdf generation and bindings, it has two implementations for now , HTML and AcroForm
 * @author asingh7!
 *
 */
public interface PdfWorker {
    /**
     * Process the html/pdf template with the binding data and return byte[] of pdf
     * @throws PdfConversionException
     */  
    public byte[] processTemplates(String formName, Map<String, String> bindingData) throws PdfConversionException;
    
    public byte[] processTemplates(String templateName, Map<String, String> bindingData,
            Map<String, String> continuationData) throws PdfConversionException;
    /**
     * This method is used to apply stampers to existing PDF     
     */
    public byte[] processStampers(byte[] template) throws PdfConversionException;
}
