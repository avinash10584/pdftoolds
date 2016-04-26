package org.pdftools.pdf.editing.tools.impl;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.PdfEditingException;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.BlockAnnotationRenderTool;
import org.pdftools.pdf.editing.tools.data.LineAnnotationData;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * Annotation render tool can be added to a {@link MarkupLayer} to render
 * different kind of annotations. These annotations are embedded into pdf and
 * can be seen in Adobe Reader Annotations created using
 * {@link LineAnnotationTool} will be added as annotation on PDF and can be
 * deleted from PDF when opened in Adobe Reader
 * 
 * @author asingh7!
 *
 */
public class LineAnnotationTool extends BlockAnnotationRenderTool<LineAnnotationData> {

    private static final long serialVersionUID = 4212306527114104622L;
            
    final static public int ANNOTATION_LINE_WIDTH = 2;
    final static public int ANNOTATION_SQUARE_CIRCLE_WIDTH = 1;

    public LineAnnotationTool(LineAnnotationData data, XYRectangle coords, int pageNo) {
        super(data, coords, pageNo);
    }

    @Override
    public void render(PdfStamper stamper) throws PdfEditingException {
        PdfAnnotation annotation = null;
            switch (data.getType()) {
                case ClosedArrow :
                case OpenArrow : annotation = drawPointer(stamper); break;
                case Line : 
                case None : annotation = drawLine(stamper); break;
                default :
                    throw new PdfEditingException(String.format("Annotation Type %s is not supported", 
                            data.getType()));
            }
            annotation.put(PdfName.NM, new PdfString(data.getAnnotationName()));
            annotation.setTitle(data.getAnnotationTitle());
            annotation.setColor(data.getColor());
            annotation.setPage(pageNo);
            annotation.put(PdfName.IC, new PdfArray(new int[]{1, 0, 0}));
            if (data.isPrintable()) {
                annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
            }
            stamper.addAnnotation(annotation, pageNo);
    }

    /**
     * A pointer Annotation will draw a LINE annotation to the coordinates
     * 
     * @param stamper
     */
    private PdfAnnotation drawPointer(PdfStamper stamper) {
        PdfAnnotation annotation = PdfAnnotation.createLine(stamper.getWriter(),
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(), coords.getTopRight().getX(),
                        coords.getTopRight().getY()),
                data.getAnnotationText(), coords.getLowerLeft().getX() + ANNOTATION_LINE_WIDTH, 
                coords.getLowerLeft().getY() + ANNOTATION_LINE_WIDTH,
                coords.getTopRight().getX() - ANNOTATION_LINE_WIDTH, coords.getTopRight().getY() - ANNOTATION_LINE_WIDTH);
        PdfArray le = new PdfArray();
        le.add(new PdfName(data.getType().name()));
        le.add(new PdfName("None"));
        annotation.setBorderStyle(new PdfBorderDictionary(ANNOTATION_LINE_WIDTH, PdfBorderDictionary.STYLE_SOLID));
        annotation.put(new PdfName("LE"), le);
        annotation.put(new PdfName("IT"), new PdfName("LineArrow"));
        return annotation;
    }

    /**
     * A line annotation for the PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawLine(PdfStamper stamper) {
        PdfAnnotation lineAnnotation = PdfAnnotation.createLine(stamper.getWriter(),
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(), coords.getTopRight().getX(),
                        coords.getTopRight().getY()),
                data.getAnnotationText(), coords.getLowerLeft().getX() + ANNOTATION_LINE_WIDTH, 
                coords.getLowerLeft().getY() + ANNOTATION_LINE_WIDTH,
                coords.getTopRight().getX() - ANNOTATION_LINE_WIDTH, coords.getTopRight().getY() - ANNOTATION_LINE_WIDTH);
        lineAnnotation.setBorderStyle(new PdfBorderDictionary(ANNOTATION_LINE_WIDTH, PdfBorderDictionary.STYLE_SOLID));
        return lineAnnotation;
    }
}
