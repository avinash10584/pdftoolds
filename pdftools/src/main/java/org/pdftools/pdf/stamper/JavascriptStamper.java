package org.pdftools.pdf.stamper;

import java.io.ByteArrayOutputStream;

import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageStamper;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class JavascriptStamper implements PdfPageStamper {
    
    private String script;    
    
    public JavascriptStamper(String script) {
        this.script = script;
    }
    
    @Override
    public byte[] stamp(byte[] pdf, Rectangle pageSize) throws PdfConversionException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
                ByteArrayOutputStream imageout = new ByteArrayOutputStream();) {
            PdfReader reader = new PdfReader(pdf);
            PdfStamper stamper = new PdfStamper(reader, bos);           
            //PdfContentByte contentByte = stamper.getOverContent(1);            
            stamper.addJavaScript(script);
            //stamper.setFormFlattening(true);
            stamper.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in JavascriptStamper", e);
        }        
    }
}
