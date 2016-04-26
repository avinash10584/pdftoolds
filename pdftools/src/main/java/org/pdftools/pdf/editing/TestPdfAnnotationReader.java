package org.pdftools.pdf.editing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.pdftools.pdf.editing.MarkupLayer;
import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.AnnotationReader;
import org.pdftools.pdf.editing.tools.RenderTool;
import org.pdftools.pdf.editing.tools.data.AnnotationData;
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
public class TestPdfAnnotationReader {

    private static String TEMPLATE_LOCATION = "C:\\Development\\ocforms";
    private static String TEMPLATE_NAME = "PTO-Supplemental Examination Certificate";
    
    public static void main(String[] str) throws Exception {
        byte[] pdfWithAnnotations = Files.readAllBytes(Paths.get(TEMPLATE_LOCATION + File.separator + "TestScript_scripted"
                + ".pdf"));  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(pdfWithAnnotations);
        PdfStamper stamper = new PdfStamper(reader, bos);
        List<RenderTool> tools = AnnotationReader.readAnnotations(stamper);
        stamper.close();
        
        byte[] pdf = Files.readAllBytes(Paths.get(TEMPLATE_LOCATION + File.separator + TEMPLATE_NAME
                + ".pdf"));
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        PdfReader reader1 = new PdfReader(pdf);
        PdfStamper stamper1 = new PdfStamper(reader1, bos1);
        MarkupLayer annotationLayer = new MarkupLayer("OCAnnotations", stamper1);         
        annotationLayer.setRenders(tools);
        annotationLayer.render();
        stamper1.close();
        
        Files.write(Paths.get(TEMPLATE_LOCATION + File.separator + TEMPLATE_NAME + "_updated"
                + ".pdf"), bos1.toByteArray());
    }
}

