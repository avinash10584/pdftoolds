package org.pdftools.pdf.editing.tools.data;

import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.tools.impl.TextMarkupTool;

/**
 * Class to hold the data for {@link TextMarkupTool}
 * Implementation of PDF 32000-1:2008 12.5.6.10, "Text Markup Annotations"
 * @author asingh7!
 *
 */
public class TextMarkupAnnotationData extends AnnotationData {

    public enum Type {StrikeOut, Squiggly, Underline}; 
    
    private Type type;
    
    public TextMarkupAnnotationData(String annotationTitle, Type type) {
        super(null, annotationTitle, AnnotationCategory.MARKUP, true, RenderColor.DARK_RED);
        this.setType(type);
   }
    
    public TextMarkupAnnotationData(String annotationText, String annotationTitle, Type type) {
         super(annotationText, annotationTitle, AnnotationCategory.MARKUP, true, RenderColor.DARK_RED);
         this.setType(type);
    }
    
    public TextMarkupAnnotationData(String annotationText, String annotationTitle, Type type, RenderColor color) {
        super(annotationText, annotationTitle, AnnotationCategory.MARKUP, true, color);
        this.setType(type);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
