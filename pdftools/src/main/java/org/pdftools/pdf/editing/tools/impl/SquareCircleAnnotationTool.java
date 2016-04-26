package org.pdftools.pdf.editing.tools.impl;

import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.PdfEditingException;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.BlockAnnotationRenderTool;
import org.pdftools.pdf.editing.tools.data.SquareCircleAnnotationData;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * Annotation render tool can be added to a {@link MarkupLayer} to render
 * different kind of annotations. These annotations are embedded into pdf and
 * can be seen in Adobe Reader Annotations created using
 * {@link SquareCircleAnnotationTool} will be added as annotation on PDF and can be
 * deleted from PDF when opened in Adobe Reader
 * 
 * @author asingh7!
 *
 */
public class SquareCircleAnnotationTool extends BlockAnnotationRenderTool<SquareCircleAnnotationData> {

    private static final long serialVersionUID = 4212306527114104622L;
            
    public static final int ANNOTATION_LINE_WIDTH = 2;
    public static final int ANNOTATION_BORDER_WIDTH = 1;

    public SquareCircleAnnotationTool(SquareCircleAnnotationData data, XYRectangle coords, int pageNo) {
        super(data, coords, pageNo);
    }

    @Override
    public void render(PdfStamper stamper) throws PdfEditingException {
        PdfAnnotation annotation = null;
            switch (data.getType()) {
                case Square:
                    annotation = drawSquare(stamper);
                    break;
                case Circle:
                    annotation = drawCircle(stamper);
                    break;
                default :
                    throw new PdfEditingException(String.format("Annotation Type %s is not supported", 
                            data.getType()));
            }
            annotation.put(PdfName.NM, new PdfString(data.getAnnotationName()));
            annotation.setTitle(data.getAnnotationTitle());
            annotation.setColor(data.getColor());
            annotation.setPage(pageNo);
            if (data.isPrintable()) {
                annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
            }
            stamper.addAnnotation(annotation, pageNo);
    }
    
    /**
     * This method will draw a Circle annotation on PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawCircle(PdfStamper stamper) {
        PdfAnnotation circleAnnotation = PdfAnnotation.createSquareCircle(stamper.getWriter(),
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(), coords.getTopRight().getX(),
                        coords.getTopRight().getY()),
                data.getAnnotationText(), false);
        circleAnnotation.setBorderStyle(new PdfBorderDictionary(ANNOTATION_BORDER_WIDTH, PdfBorderDictionary.STYLE_SOLID));
        return circleAnnotation;
    }

    /**
     * his method will draw a Square annotation on PDF
     * 
     * @param stamper
     */
    private PdfAnnotation drawSquare(PdfStamper stamper) {
        PdfAnnotation squareAnnotation = PdfAnnotation.createSquareCircle(stamper.getWriter(),
                new Rectangle(coords.getLowerLeft().getX(), coords.getLowerLeft().getY(), coords.getTopRight().getX(),
                        coords.getTopRight().getY()),
                data.getAnnotationText(), true);
        squareAnnotation.setBorderStyle(new PdfBorderDictionary(ANNOTATION_BORDER_WIDTH, PdfBorderDictionary.STYLE_SOLID));
        return squareAnnotation;
    }
}
