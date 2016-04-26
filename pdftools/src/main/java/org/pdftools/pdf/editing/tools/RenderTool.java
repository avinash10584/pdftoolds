package org.pdftools.pdf.editing.tools;

import java.io.Serializable;

import org.pdftools.pdf.editing.PdfEditingException;

import com.itextpdf.text.pdf.PdfStamper;

/**
 * Common methods that exist in the PDF rendering tools, all rendering tools
 * should have a method to render on the pdf
 * 
 * @author asingh7!
 *
 */
public interface RenderTool extends Serializable {
    public abstract void render(PdfStamper stamper) throws PdfEditingException;
}
