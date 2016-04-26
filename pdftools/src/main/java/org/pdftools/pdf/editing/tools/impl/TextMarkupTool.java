package org.pdftools.pdf.editing.tools.impl;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.PdfEditingException;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.LineAnnotationRenderTool;
import org.pdftools.pdf.editing.tools.data.TextMarkupAnnotationData;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * {@link TextMarkupTool} can be added to a {@link MarkupLayer} to modify and edit text in PDF
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
public class TextMarkupTool extends LineAnnotationRenderTool<TextMarkupAnnotationData> {
    
    private static final long serialVersionUID = -3301907767967874690L;

    public TextMarkupTool(TextMarkupAnnotationData data, XYRectangle coords, int pageNo) {
        super(data, coords, pageNo);
    }
    
    @Override
    public void render(PdfStamper stamper) throws PdfEditingException {        
        PdfAnnotation annotation = null;
        switch (data.getType()) {
            case StrikeOut:   
                    annotation = strikeText(stamper);
                    break;   
            case Squiggly:   
                    annotation = squigglyText(stamper);
                    break;
            case Underline:   
                    annotation = underlineText(stamper);
                    break;           
            default :
                    throw new PdfEditingException(String.format("Text Editing Tool Type %s is not supported", 
                        data.getType()));
            }
            annotation.put(PdfName.NM, new PdfString(data.getAnnotationName()));
            annotation.setColor(data.getColor());
            annotation.setTitle(data.getAnnotationTitle());
            annotation.setPage(pageNo);
            if (data.isPrintable()) {
                annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
            }
            stamper.addAnnotation(annotation, pageNo); 
        
    } 
    
    /**
     * Squiggly text based on coordinates, UI can pass multiple set of coordinates 
     * 
     * @param stamper
     */
    private PdfAnnotation squigglyText(PdfStamper stamper) {   
        float[] quad = {startingPoint.getX() + length, 
                startingPoint.getY(), 
                startingPoint.getX(), 
                startingPoint.getY(),
                startingPoint.getX() + length,
                startingPoint.getY() + strokeWidth,
                startingPoint.getX(),
                startingPoint.getY() + strokeWidth}; 
        PdfAnnotation squiggle = PdfAnnotation.createMarkup(stamper.getWriter(), 
            new Rectangle(startingPoint.getX(), 
                    startingPoint.getY(), 
                    startingPoint.getX() + length, 
                    startingPoint.getY() + strokeWidth), null, PdfAnnotation.MARKUP_SQUIGGLY,
                    quad); 
        return squiggle; 
    }
    
    /**
     * Squiggly text based on coordinates, UI can pass multiple set of coordinates 
     * 
     * @param stamper
     */
    private PdfAnnotation strikeText(PdfStamper stamper) {       
        float[] quad = {startingPoint.getX() + length, 
                startingPoint.getY() - strokeWidth/2, 
                startingPoint.getX(), 
                startingPoint.getY() - strokeWidth/2,
                startingPoint.getX() + length,
                startingPoint.getY() + strokeWidth/2,
                startingPoint.getX(),
                startingPoint.getY() + strokeWidth/2}; 
        PdfAnnotation strike = PdfAnnotation.createMarkup(stamper.getWriter(), 
            new Rectangle(startingPoint.getX(), 
                    startingPoint.getY(), 
                    startingPoint.getX() + length, 
                    startingPoint.getY() + strokeWidth/2), null, PdfAnnotation.MARKUP_STRIKEOUT,
                    quad); 
        return strike; 
    }
    
    /**
     * Underline text based on coordinates, UI can pass multiple set of coordinates 
     * 
     * @param stamper
     */
    private PdfAnnotation underlineText(PdfStamper stamper) {   
        float[] quad = {startingPoint.getX() + length, 
                        startingPoint.getY(), 
                        startingPoint.getX(), 
                        startingPoint.getY(),
                        startingPoint.getX() + length,
                        startingPoint.getY() + strokeWidth,
                        startingPoint.getX(),
                        startingPoint.getY() + strokeWidth}; 
        PdfAnnotation underline = PdfAnnotation.createMarkup(stamper.getWriter(), 
                new Rectangle(startingPoint.getX(), 
                        startingPoint.getY(), 
                        startingPoint.getX() + length, 
                        startingPoint.getY() + strokeWidth), null, PdfAnnotation.MARKUP_UNDERLINE,
                        quad); 
        return underline; 
    }
}
