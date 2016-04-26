package org.pdftools.pdf.editing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.RenderTool;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.tool.xml.Experimental;


/**
 * Experimental class to test the pdf editing using the rendering tools
 * @author asingh7!
 *
 */
@Experimental
public class TestPdfToolSet {

    private static String TEMPLATE_LOCATION = "C:\\Development\\ocforms";
    private static String TEMPLATE_NAME = "TestScript";
    
    public static void main(String[] str) throws Exception {
        byte[] pdf = Files.readAllBytes(Paths.get(TEMPLATE_LOCATION + File.separator + TEMPLATE_NAME 
                + ".pdf"));  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(pdf);
        PdfStamper stamper = new PdfStamper(reader, bos);
        
        MarkupLayer annotationLayer = new MarkupLayer("OCAnnotations", stamper);
        List<RenderTool> annotations = new ArrayList<>();
        LineAnnotationTool pointerAnnotation = new LineAnnotationTool(new LineAnnotationData("pointer annotation data", "pointer annotation title" ,
                LineAnnotationData.Type.ClosedArrow),
                new XYRectangle(XYArray.getInstance(320, 450),  XYArray.getInstance(500, 610)), 1);
        
       /* TextAnn stampAnnotation = new NotesAnnotationTool(new AnnotationData("stamp annotation data", "stamp annotation title" ,
                AnnotationType.STAMP, Files.readAllBytes(Paths.get(TEMPLATE_LOCATION + "\\test.png"))), 
                new XYRectangle(XYArray.getInstance(220, 350), XYArray.getInstance(225, 355)), 1);
        */
        TextAnnotationTool noteAnnotation = new TextAnnotationTool(new TextAnnotationData("note annotation data", "note annotation title" ,
                TextAnnotationData.Type.Note), new XYRectangle(XYArray.getInstance(320, 350), XYArray.getInstance(322, 352)), 1);
        
        TextAnnotationData comment = new TextAnnotationData("comment annotation data", "comment annotation title" ,
                TextAnnotationData.Type.Comment);
        TextAnnotationTool commentAnnotation = new TextAnnotationTool(comment, new XYRectangle(XYArray.getInstance(320, 370), XYArray.getInstance(322, 372)), 1);
        
        TextAnnotationData reply = new TextAnnotationData("comment annotation reply", "comment annotation reply title" ,
                TextAnnotationData.Type.Comment);
        reply.setParentAnnotationId(comment.getAnnotationName());
        TextAnnotationTool replyAnnotation = new TextAnnotationTool(reply, new XYRectangle(XYArray.getInstance(320, 390), XYArray.getInstance(322, 412)), 1);
        
        TextAnnotationTool freeTextAnnotation = new TextAnnotationTool(new TextAnnotationData("free text " + new Date(), "free text" ,
                TextAnnotationData.Type.FreeText), new XYRectangle(XYArray.getInstance(320, 350), XYArray.getInstance(392, 382)), 1);
        
        LineAnnotationTool lineAnnotation = new LineAnnotationTool(new LineAnnotationData("line annotation data", "line annotation title" ,
                LineAnnotationData.Type.None), new XYRectangle(XYArray.getInstance(300, 250), XYArray.getInstance(452, 692)), 1);
        
        SquareCircleAnnotationTool squareAnnotation = new SquareCircleAnnotationTool(new SquareCircleAnnotationData("square annotation data", "square annotation title" ,
                SquareCircleAnnotationData.Type.Square), new XYRectangle(XYArray.getInstance(120, 570), XYArray.getInstance(252, 652)), 1);
        
        SquareCircleAnnotationTool circleAnnotation = new SquareCircleAnnotationTool(new SquareCircleAnnotationData("circle annotation data", "circle annotation title" ,
                SquareCircleAnnotationData.Type.Circle), new XYRectangle(XYArray.getInstance(320, 570), XYArray.getInstance(452, 652)), 1);
        
        annotations.add(pointerAnnotation);
        annotations.add(commentAnnotation);
        //annotations.add(replyAnnotation);
        annotations.add(freeTextAnnotation);
        annotations.add(noteAnnotation);
        annotations.add(lineAnnotation);
        annotations.add(squareAnnotation);
        annotations.add(circleAnnotation);        
        annotationLayer.setRenders(annotations);
        annotationLayer.render();
       /* 
        MarkupLayer drawingLayer = new MarkupLayer("OCdrawBoard", stamper);
        List<RenderTool> drawings = new ArrayList<>();        
        PaintingTool lineDrawing = new PaintingTool(new PaintData(DrawingType.LINE), new XYRectangle(XYArray.getInstance(120, 350),
                XYArray.getInstance(500, 610)), 1);        
        PaintingTool ellipseDrawing = new PaintingTool(new PaintData(DrawingType.ELLIPSE), new XYRectangle(XYArray.getInstance(120, 450),
                XYArray.getInstance(220, 570)), 1);        
        PaintingTool rectangleDrawing = new PaintingTool(new PaintData(DrawingType.RECTANLGLE), new XYRectangle(XYArray.getInstance(320, 450),
                XYArray.getInstance(500, 610)), 1);
        
        drawings.add(lineDrawing);
        drawings.add(ellipseDrawing);
        drawings.add(noteAnnotation);
        drawings.add(rectangleDrawing);
        drawingLayer.setRenders(drawings);
        drawingLayer.render();       
        */
        MarkupLayer textEditLayer = new MarkupLayer("OCTextEdit", stamper);
        HighlightTool highlight = new HighlightTool(new HighlightAnnotationData("user 12"), 
                new XYRectangle(XYArray.getInstance(220, 550),
                        XYArray.getInstance(480, 580)),1);
        TextMarkupTool strikeThrough = new TextMarkupTool(new TextMarkupAnnotationData("user 12", TextMarkupAnnotationData.Type.StrikeOut), 
                new XYRectangle(XYArray.getInstance(220, 440),
                        XYArray.getInstance(480, 460)),1); 
        List<RenderTool> textEdits = new ArrayList<>(); 
        textEdits.add(highlight);
        textEdits.add(strikeThrough);
        textEditLayer.setRenders(textEdits);
        textEditLayer.render();        
        
        stamper.close();
        pdf = bos.toByteArray();
        
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        PdfReader reader1 = new PdfReader(pdf);
        PdfStamper stamper1 = new PdfStamper(reader1, bos1);
        
        MarkupLayer replyLayer = new MarkupLayer("Reply", stamper1);
        List<RenderTool> replies = new ArrayList<>(); 
        replies.add(replyAnnotation);
        replyLayer.setRenders(replies);
        replyLayer.render();        
        stamper1.close();
        pdf = bos1.toByteArray();
        
        PdfReader outputReader = new PdfReader(pdf);
        for (int i = 1; i <= outputReader.getNumberOfPages(); i++) {
            PdfArray array = outputReader.getPageN(i).getAsArray(PdfName.ANNOTS);
            if (array == null) continue;
            for (int j = 0; j < array.size(); j++) {
                PdfDictionary annot = array.getAsDict(j);
                PdfString content = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.CONTENTS));
                PdfString title = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.T)); //this should be username
                PdfBoolean isOpen = (PdfBoolean)PdfReader.getPdfObject(annot.get(PdfName.OPEN));
                
                PdfName type = (PdfName)PdfReader.getPdfObject(annot.get(PdfName.SUBTYPE));
                PdfArray rect = (PdfArray)PdfReader.getPdfObject(annot.get(PdfName.RECT));
                PdfName name = (PdfName)PdfReader.getPdfObject(annot.get(PdfName.NAME));
                
                PdfString createDt = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.M)); //create date D:20160418102052-04'00'
                
                PdfDictionary IRT = (PdfDictionary)PdfReader.getPdfObject(annot.get(PdfName.IRT)); //create date
                
                PdfString subject = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.SUBJECT)); //this should be Sticky Note, Cross-out etc.
                
                PdfString RC = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.RC)); //rich content
                //<?xml version="1.0"?><body xmlns="http://www.w3.org/1999/xhtml" xmlns:xfa="http://www.xfa.org/schema/xfa-data/1.0/" xfa:APIVersion="Acrobat:15.10.0" 
                //xfa:spec="2.0.2" ><p dir="ltr"><span dir="ltr" style="font-size:10.1pt;text-align:left;color:#000000;font-weight:normal;font-style:normal">tyrty</span></p></body>
                
            }
        }
        
        Files.write(Paths.get(TEMPLATE_LOCATION + File.separator + TEMPLATE_NAME + "_scripted" + ".pdf"),
                pdf);  
        
    }
}

