package org.pdftools.pdf.editing.tools.data;

import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.tools.impl.TextMarkupTool;
/**
 * Class to hold the data for {@link TextMarkupTool}
 * Implementation of PDF 32000-1:2008 12.5.6.10, "Text nnotations"
 * @author asingh7!
 *
 */
public class TextAnnotationData extends AnnotationData {

    public static enum Type {Note, Comment, Text, Popup, FreeText}; 
    
    private Type type;
    private Boolean isOpen;
    
    public TextAnnotationData(String annotationText, String annotationTitle, Type type) {
         super(annotationText, annotationTitle, AnnotationCategory.TEXT, true, RenderColor.YELLOW);
         this.setType(type);
    }
    
    public TextAnnotationData(String annotationText, String annotationTitle, Type type, RenderColor color) {
        super(annotationText, annotationTitle, AnnotationCategory.TEXT, true, color);
        this.setType(type);
    }
    
    public TextAnnotationData(String annotationText, String annotationTitle, Type type, Boolean isOpen) {
        super(annotationText, annotationTitle, AnnotationCategory.TEXT, true, RenderColor.YELLOW);
        this.setType(type);
        this.setIsOpen(isOpen);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
}
