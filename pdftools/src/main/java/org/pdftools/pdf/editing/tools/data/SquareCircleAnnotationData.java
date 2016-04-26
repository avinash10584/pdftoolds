package org.pdftools.pdf.editing.tools.data;

import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.tools.impl.SquareCircleAnnotationTool;

/**
 * Class to hold the data for {@link SquareCircleAnnotationTool}
 * Implementation of PDF 32000-1:2008 12.5.6.8 Square and Circle Annotations
 * @author asingh7!
 *
 */
public class SquareCircleAnnotationData extends AnnotationData {

    public enum Type {Square, Circle}; 
    
    private Type type;
    
    public SquareCircleAnnotationData(String annotationText, String annotationTitle, Type type) {
         super(annotationText, annotationTitle, AnnotationCategory.SQUARECIRCLE, true, RenderColor.DARK_RED);
         this.setType(type);
    }
    
    public SquareCircleAnnotationData(String annotationText, String annotationTitle, Type type, RenderColor color) {
        super(annotationText, annotationTitle, AnnotationCategory.SQUARECIRCLE, true, color);
        this.setType(type);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
