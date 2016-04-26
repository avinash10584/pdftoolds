package org.pdftools.pdf.editing.tools.data;

import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.tools.impl.PaintingTool;

import lombok.Data;

/**
 * Class to hold the data for {@link PaintingTool}
 * @author asingh7!
 *
 */
@Data
public class PaintData {

    public enum DrawingType {LINE, RECTANLGLE, ELLIPSE}; 
    
    private DrawingType drawingType;
    private RenderColor color;
    
    public PaintData(DrawingType drawingType) {
     this.drawingType = drawingType;
     this.color = RenderColor.BLUE;
    }
    
    public PaintData(DrawingType drawingType, 
            RenderColor color) {
        this.drawingType = drawingType;
        this.color = color;
    }
}
