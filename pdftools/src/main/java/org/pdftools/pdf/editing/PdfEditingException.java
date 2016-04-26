package org.pdftools.pdf.editing;

/**
 * Generic class to wrap the iText Pdf exceptions
 * @author asingh7!
 *
 */
public class PdfEditingException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public PdfEditingException () {}

    public PdfEditingException(String message) {
        super (message);
    }

    public PdfEditingException(Throwable cause) {
        super (cause);
    }
    
    public PdfEditingException(String message, Throwable cause) {
        super (message, cause);
    }
}
