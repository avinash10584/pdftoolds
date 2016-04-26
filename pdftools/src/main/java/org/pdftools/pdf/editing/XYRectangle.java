package org.pdftools.pdf.editing;

import org.pdftools.utils.XYArray;

import lombok.Data;

/**
 * XY coordinates based rectangle to paint the annotation and drawings on pdf
 * @author asingh7!
 *
 */
@Data
public class XYRectangle {
    
    XYArray lowerLeft;
    XYArray topRight;
    
    public XYRectangle(XYArray lowerLeft, XYArray topRight) {
        this.lowerLeft = lowerLeft;
        this.topRight = topRight;        
    }
}
