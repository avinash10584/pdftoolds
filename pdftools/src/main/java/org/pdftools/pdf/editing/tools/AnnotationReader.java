package org.pdftools.pdf.editing.tools;

import java.util.ArrayList;
import java.util.List;

import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.data.HighlightAnnotationData;
import org.pdftools.pdf.editing.tools.data.LineAnnotationData;
import org.pdftools.pdf.editing.tools.data.SquareCircleAnnotationData;
import org.pdftools.pdf.editing.tools.data.TextAnnotationData;
import org.pdftools.pdf.editing.tools.data.TextMarkupAnnotationData;
import org.pdftools.pdf.editing.tools.impl.HighlightTool;
import org.pdftools.pdf.editing.tools.impl.LineAnnotationTool;
import org.pdftools.pdf.editing.tools.impl.SquareCircleAnnotationTool;
import org.pdftools.pdf.editing.tools.impl.TextAnnotationTool;
import org.pdftools.pdf.editing.tools.impl.TextMarkupTool;
import org.pdftools.utils.XYArray;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class AnnotationReader {

    public static List<RenderTool> readAnnotations(PdfStamper stamper) {
        PdfReader outputReader = stamper.getReader();
        List<RenderTool> pdfAnnotations = new ArrayList<>();
        for (int pageNo = 1; pageNo <= outputReader.getNumberOfPages(); pageNo++) {
            PdfArray array = outputReader.getPageN(pageNo).getAsArray(PdfName.ANNOTS);
            if (array == null)
                continue;
            for (int j = 0; j < array.size(); j++) {
                PdfDictionary annot = array.getAsDict(j);
                RenderTool annotation = null;
                PdfName type = (PdfName) PdfReader.getPdfObject(annot.get(PdfName.SUBTYPE));
                String annotationType = type.toString().substring(1);
                if (annotationType.equals(TextAnnotationData.Type.Comment.name())
                        || annotationType.equals(TextAnnotationData.Type.Note.name())
                        || annotationType.equals(TextAnnotationData.Type.FreeText.name())
                        || annotationType.equals(TextAnnotationData.Type.Text.name())
                        || annotationType.equals(TextAnnotationData.Type.Popup.name())) {
                    annotation = buildTextAnnotation(annot, annotationType, pageNo);
                } else if (annotationType.equals(HighlightAnnotationData.Type.Highlight.name())) {
                    annotation = buildHighlightAnnotation(annot, annotationType, pageNo);
                } else if (annotationType.equals(LineAnnotationData.Type.OpenArrow.name())
                        || annotationType.equals(LineAnnotationData.Type.ClosedArrow.name())
                        || annotationType.equals(LineAnnotationData.Type.None.name())
                        || annotationType.equals(LineAnnotationData.Type.Line.name())) {
                    annotation = buildLineAnnotation(annot, annotationType, pageNo);
                } else if (annotationType.equals(SquareCircleAnnotationData.Type.Square.name())
                        || annotationType.equals(SquareCircleAnnotationData.Type.Circle.name())) {
                    annotation = buildSquareCircleAnnotation(annot, annotationType, pageNo);
                } else if (annotationType.equals(TextMarkupAnnotationData.Type.StrikeOut.name())
                        || annotationType.equals(TextMarkupAnnotationData.Type.Squiggly.name())
                        || annotationType.equals(TextMarkupAnnotationData.Type.Underline.name())) {
                    annotation = buildTextMarkupAnnotation(annot, annotationType, pageNo);
                } else {
                    // skip annnotation , we don't support these
                }
                if (annotation != null) {
                    pdfAnnotations.add(annotation);
                }
            }
        }      
        return pdfAnnotations;
    }

    private static RenderTool buildTextAnnotation(PdfDictionary annot, String type, int pageNo) {
        String annotationText = null;
        String annotationTitle = null;
        Boolean isOpen = null;
        PdfString content = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
        PdfString title = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.T));
        PdfBoolean open = (PdfBoolean) PdfReader.getPdfObject(annot.get(PdfName.OPEN));
        PdfString rc = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.RC));
        if (content != null) {
            annotationText = content.toString();
        }
        if (rc != null) {
            annotationText = rc.toString();
        }
        if (title != null) {
            annotationTitle = title.toString();
        }
        if (open != null) {
            isOpen = open.booleanValue();
        }
        TextAnnotationData textAnnotation = new TextAnnotationData(annotationText, annotationTitle,
                TextAnnotationData.Type.valueOf(type), isOpen);
        XYRectangle coords = getCoordinates(annot);
        return new TextAnnotationTool(textAnnotation, coords, pageNo);
    }

    private static RenderTool buildHighlightAnnotation(PdfDictionary annot, String type, int pageNo) {
        String annotationText = null;
        String annotationTitle = null;
        PdfString content = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
        PdfString title = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.T));
        PdfString rc = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.RC));
        if (content != null) {
            annotationText = content.toString();
        }
        if (rc != null) {
            annotationText = rc.toString();
        }
        if (title != null) {
            annotationTitle = title.toString();
        }
        HighlightAnnotationData highlightAnnotation = new HighlightAnnotationData(annotationTitle);
        highlightAnnotation.setAnnotationText(annotationText);
        highlightAnnotation.setType(HighlightAnnotationData.Type.Highlight);
        XYRectangle coords = getCoordinates(annot);
        return new HighlightTool(highlightAnnotation, coords, pageNo);
    }
    
    private static RenderTool buildLineAnnotation(PdfDictionary annot, String type, int pageNo) {
        String annotationText = null;
        String annotationTitle = null;
        PdfString content = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
        PdfString title = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.T));
        PdfString rc = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.RC));
        if (content != null) {
            annotationText = content.toString();
        }
        if (rc != null) {
            annotationText = rc.toString();
        }
        if (title != null) {
            annotationTitle = title.toString();
        }
        LineAnnotationData lineAnnotation = new LineAnnotationData(annotationText, annotationTitle,
                LineAnnotationData.Type.valueOf(type));
        XYRectangle coords = getCoordinates(annot);
        return new LineAnnotationTool(lineAnnotation, coords, pageNo);
    }

    private static RenderTool buildSquareCircleAnnotation(PdfDictionary annot, String type, int pageNo) {
        String annotationText = null;
        String annotationTitle = null;
        PdfString content = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
        PdfString title = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.T));
        PdfString rc = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.RC));
        if (content != null) {
            annotationText = content.toString();
        }
        if (rc != null) {
            annotationText = rc.toString();
        }
        if (title != null) {
            annotationTitle = title.toString();
        }
        SquareCircleAnnotationData squareCircleAnnotation = new SquareCircleAnnotationData(annotationText, annotationTitle,
                SquareCircleAnnotationData.Type.valueOf(type));
        XYRectangle coords = getCoordinates(annot);
        return new SquareCircleAnnotationTool(squareCircleAnnotation, coords, pageNo);
    }

    private static RenderTool buildTextMarkupAnnotation(PdfDictionary annot, String type, int pageNo) {
        String annotationText = null;
        String annotationTitle = null;
        PdfString content = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
        PdfString title = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.T));
        PdfString rc = (PdfString) PdfReader.getPdfObject(annot.get(PdfName.RC));
        if (content != null) {
            annotationText = content.toString();
        }
        if (rc != null) {
            annotationText = rc.toString();
        }
        if (title != null) {
            annotationTitle = title.toString();
        }
        TextMarkupAnnotationData textMarkupAnnotationData = new TextMarkupAnnotationData(annotationText, annotationTitle,
                TextMarkupAnnotationData.Type.valueOf(type));
        XYRectangle coords = getCoordinates(annot);
        return new TextMarkupTool(textMarkupAnnotationData, coords, pageNo);
    }

    private static XYRectangle getCoordinates(PdfDictionary annot) {
        PdfArray rc = (PdfArray) PdfReader.getPdfObject(annot.get(PdfName.RECT));
        PdfNumber lowerX = rc.getAsNumber(0);
        PdfNumber lowerY = rc.getAsNumber(1);
        PdfNumber topX = rc.getAsNumber(2);
        PdfNumber topY = rc.getAsNumber(3);
        return new XYRectangle(XYArray.getInstance(lowerX.floatValue(), lowerY.floatValue()), 
                XYArray.getInstance(topX.floatValue(), topY.floatValue()));
    }
}
