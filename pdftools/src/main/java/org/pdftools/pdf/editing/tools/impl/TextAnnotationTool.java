package org.pdftools.pdf.editing.tools.impl;

import java.io.IOException;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.PdfEditingException;
import org.pdftools.pdf.editing.RenderColor;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.BlockAnnotationRenderTool;
import org.pdftools.pdf.editing.tools.data.TextAnnotationData;
import org.pdftools.pdf.editing.tools.data.TextAnnotationData.Type;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * Annotation render tool can be added to a {@link MarkupLayer} to render
 * different kind of annotations. These annotations are embedded into pdf and
 * can be seen in Adobe Reader Annotations created using
 * {@link TextAnnotationTool} will be added as annotation on PDF and can be
 * deleted from PDF when opened in Adobe Reader
 * 
 * @author asingh7!
 *
 */
public class TextAnnotationTool extends BlockAnnotationRenderTool<TextAnnotationData> {

    private static final long serialVersionUID = 4212306527114104622L;

    public TextAnnotationTool(TextAnnotationData data, XYRectangle coords, int pageNo) {
        super(data, coords, pageNo);
    }

    @Override
    public void render(PdfStamper stamper) throws PdfEditingException {
        PdfAnnotation annotation = null;
            switch (data.getType()) {
                case Note:
                    annotation = drawNote(stamper);
                    annotation.setColor(data.getColor());
                    break;
                case Comment:
                    annotation = drawComment(stamper);
                    annotation.setColor(data.getColor());
                    break;  
                case FreeText:
                    annotation = drawFreeText(stamper);
                    break;
                case Text:
                    annotation = drawText(stamper);
                    break;    
                case Popup:
                    annotation = drawPopup(stamper);
                    break;    
                default :
                    throw new PdfEditingException(String.format("Annotation Type %s is not supported", 
                            data.getType()));
            }
            if (!data.getType().equals(Type.Popup)) {
                annotation.put(PdfName.NM, new PdfString(data.getAnnotationName()));
                annotation.setTitle(data.getAnnotationTitle());
            }
            annotation.setPage(pageNo);
            if (data.isPrintable()) {
                annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
            }
            stamper.addAnnotation(annotation, pageNo);
    }
    
    /**
     * This method will add a simple note annotation to PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawNote(PdfStamper stamper) {
        PdfAnnotation note = PdfAnnotation.createText(
                stamper.getWriter(), new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(),
                        coords.getTopRight().getX(), coords.getTopRight().getY()),
                data.getAnnotationTitle(), data.getAnnotationText(), true, null);
        note.put(PdfName.NAME, new PdfName(data.getType().name()));
        return note;
    }
    
    /**
     * This method will add a text annotation to PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawText(PdfStamper stamper) {
        boolean isOpen = false;
        if(data.getIsOpen() != null) {
            isOpen = data.getIsOpen();
        }
        PdfAnnotation textAnnotation = PdfAnnotation.createText(stamper.getWriter(), 
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(),
                coords.getTopRight().getX(), coords.getTopRight().getY()), 
                data.getAnnotationTitle(), data.getAnnotationText(), isOpen, null);
        textAnnotation.put(PdfName.NAME, new PdfName(data.getType().name()));
        return textAnnotation;
    }
    
    /**
     * This method will add a popup annotation to PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawPopup(PdfStamper stamper) {
        boolean isOpen = false;
        if(data.getIsOpen() != null) {
            isOpen = data.getIsOpen();
        }
        PdfAnnotation note = PdfAnnotation.createPopup(stamper.getWriter(), 
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(),
                coords.getTopRight().getX(), coords.getTopRight().getY()), data.getAnnotationText(), isOpen);
        note.put(PdfName.NAME, new PdfName(data.getType().name()));
        return note;
    }
    
    /**
     * This method will add a simple comment annotation to PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawComment(PdfStamper stamper) {
        PdfIndirectReference parent = null;
        if (data.getParentAnnotationId() != null) {
            PdfArray array = stamper.getReader().getPageN(pageNo).getAsArray(PdfName.ANNOTS);
            for (int j = 0; j < array.size(); j++) {
                PdfDictionary annot = array.getAsDict(j);
                if (annot !=null) {
                    PdfString uniqueId = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.NM));
                    if (uniqueId!= null && uniqueId.toString().equals(data.getParentAnnotationId())) {
                        parent = array.getAsIndirectObject(j);
                        break;
                    }                
                }
            }        
        }
        PdfAnnotation comment = PdfAnnotation.createText(
                stamper.getWriter(), new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(),
                        coords.getTopRight().getX(), coords.getTopRight().getY()),
                data.getAnnotationTitle(), data.getAnnotationText(), true, null);
        comment.put(PdfName.NAME, new PdfName(data.getType().name()));
        if (parent != null) {
            comment.put(PdfName.IRT, parent);
        }
        return comment;
    }
    
    
    /**
     * This method will add a free text comment annotation to PDF
     * 
     * @param stamper
     * @throws PdfEditingException 
     */
    private PdfAnnotation drawFreeText(PdfStamper stamper) throws PdfEditingException {
        PdfContentByte pcb = stamper.getOverContent(pageNo);
        BaseFont helv;
        try {
            helv = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (IOException | DocumentException e) {
            throw new PdfEditingException(e);
        }
        pcb.setFontAndSize(helv, 12); 
        pcb.setColorFill(RenderColor.DARK_BLUE);
        pcb.setColorStroke(RenderColor.TRANSPARENT);
        //pcb.setColorFill(RenderColor.DARK_BLUE);
        PdfAnnotation freeText = PdfAnnotation.createFreeText(
                stamper.getWriter(), new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(),
                        coords.getTopRight().getX(), coords.getTopRight().getY()),
                        data.getAnnotationText(), pcb);
        freeText.setBorderStyle(new PdfBorderDictionary(1, PdfBorderDictionary.STYLE_DASHED));
        return freeText;
    }
}
