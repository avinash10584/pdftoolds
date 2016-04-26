package org.pdftools.pdf.editing.tools.data;

import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.tools.impl.HighlightTool;

/**
 * Class to hold the data for {@link HighlightTool}
 * @author asingh7!
 *
 */
public class HighlightAnnotationData extends AnnotationData {
    
    public enum Type {Highlight}; 
    
    private Type type= Type.Highlight;
    
    public HighlightAnnotationData(String annotationTitle) {
        super(annotationTitle, AnnotationCategory.MARKUP, RenderColor.YELLOW);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
