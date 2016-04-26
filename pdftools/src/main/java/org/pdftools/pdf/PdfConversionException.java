package org.pdftools.pdf;

/**
 * Generic class to wrap the iText Pdf exceptions
 * @author asingh7!
 *
 */
public class PdfConversionException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public PdfConversionException () {}

    public PdfConversionException(String message) {
        super (message);
    }

    public PdfConversionException(Throwable cause) {
        super (cause);
    }
    
    public PdfConversionException(String message, Throwable cause) {
        super (message, cause);
    }
}
