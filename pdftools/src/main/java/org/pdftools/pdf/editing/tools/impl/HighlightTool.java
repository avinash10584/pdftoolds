package org.pdftools.pdf.editing.tools.impl;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.BlockAnnotationRenderTool;
import org.pdftools.pdf.editing.tools.data.HighlightAnnotationData;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * {@link HighlightTool} can be added to a {@link MarkupLayer} to modify and edit text in PDF
 * It only supports highlighting and strike through
 * quadrilateral in iText is defined in convex order as below
 * Convex order - this is the one working like expected:
 * 
 *  7,8             5, 6
 *  (x,y)___________(x,y)
 *      |           |
 *      |           |
 *      |___________|
 *  (x,y)           (x,y)
 *  3, 4            1, 2 
     
 * @author asingh7!
 *
 */
public class HighlightTool extends BlockAnnotationRenderTool<HighlightAnnotationData> {
    
    private static final long serialVersionUID = 1939386908984253104L;
    
    public HighlightTool(HighlightAnnotationData data, XYRectangle coords, int pageNo) {
        super(data, coords, pageNo);
    }
    /**
     * Highlights the existing content by placing a layer under the content,
     * should we allow color being passed for highlight ?
     * @param stamper
     */
    @Override
    public void render(PdfStamper stamper) {    
        float[] quad = {coords.getTopRight().getX(), 
                coords.getLowerLeft().getY(), 
                coords.getLowerLeft().getX(), 
                coords.getLowerLeft().getY(),
                coords.getTopRight().getX(),
                coords.getTopRight().getY(),
                coords.getLowerLeft().getX(),
                coords.getTopRight().getY()}; 
        PdfAnnotation highlight = PdfAnnotation.createMarkup(stamper.getWriter(), 
                new Rectangle(coords.getLowerLeft().getX(), 
                        coords.getLowerLeft().getY(), 
                        coords.getTopRight().getX(), 
                        coords.getTopRight().getY()), null, PdfAnnotation.MARKUP_HIGHLIGHT,
                        quad); 
        highlight.put(PdfName.NM, new PdfString(data.getAnnotationName()));
        highlight.setColor(data.getColor());
        highlight.setTitle(data.getAnnotationTitle());
        highlight.setPage(pageNo);
        if (data.isPrintable()) {
            highlight.setFlags(PdfAnnotation.FLAGS_PRINT);
        }
        stamper.addAnnotation(highlight, pageNo); 
    }
}
