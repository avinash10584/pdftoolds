package org.pdftools.pdf;
import com.itextpdf.text.Rectangle;

/**
 * This class is used to implement stampers like watermark, page footer and other post-processing work on PDF
 * @author asingh7!
 *
 */
public interface PdfPageStamper {
    /**
     * This method is used to take existing pdf and do stamping work on that like watermark, page footer etc.
     * @param pdf
     * @param pageSize
     * @throws PdfConversionException
     */
    public byte[] stamp(byte[] pdf, Rectangle pageSize) throws PdfConversionException;
}
