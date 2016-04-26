package org.pdftools.pdf.editing.tools.impl;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.RenderTool;
import org.pdftools.pdf.editing.tools.data.PaintData;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * {@link PaintingTool} can be added to a {@link MarkupLayer} to render
 * different kind of drawings on PDF. These drawings are embedded into pdf and
 * cannot be deleted or modified once added 
 * @author asingh7!
 *
 */
public class PaintingTool implements RenderTool {
    
    private static final long serialVersionUID = -7631972657348254675L;
    
    protected int pageNo;
    protected XYRectangle coords;
    protected PaintData data;
    
    
    public PaintingTool(PaintData data, XYRectangle coords, int pageNo) {
        this.data = data;
        this.coords = coords;
        this.pageNo = pageNo;
    }
    
    @Override
    public void render(PdfStamper stamper) {        
        switch (data.getDrawingType()) {
            case LINE:   
                    drawLine(stamper);
                    break;
            case ELLIPSE:   
                    drawEllipse(stamper);
                    break;
            case RECTANLGLE:   
                drawRectangle(stamper);
                break;        
        }       
    }   
    
    private void drawLine(PdfStamper stamper) {    
        PdfContentByte canvas = stamper.getOverContent(pageNo);
        canvas.setColorStroke(data.getColor());
        canvas.moveTo(coords.getLowerLeft().getX(), coords.getLowerLeft().getY());
        canvas.lineTo(coords.getTopRight().getX(), coords.getTopRight().getY());
        canvas.closePathStroke();
    }
    
    private void drawEllipse(PdfStamper stamper) {   
        PdfContentByte canvas = stamper.getOverContent(pageNo);
        canvas.setColorStroke(data.getColor());
        canvas.ellipse(coords.getLowerLeft().getX(), 
                        coords.getLowerLeft().getY(),
                        coords.getTopRight().getX(), 
                        coords.getTopRight().getY());
        canvas.stroke();
    }
    
    private void drawRectangle(PdfStamper stamper) {   
        PdfContentByte canvas = stamper.getOverContent(pageNo);
        canvas.setColorStroke(data.getColor());
        canvas.rectangle(coords.getLowerLeft().getX(), 
                        coords.getLowerLeft().getY(),
                        coords.getTopRight().getX() - coords.getLowerLeft().getX(), 
                        coords.getTopRight().getY() - coords.getLowerLeft().getY());
        canvas.stroke();
    }
}
