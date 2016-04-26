package org.pdftools.pdf.stamper;

import java.io.ByteArrayOutputStream;

import org.pdftools.pdf.PdfConstants;
import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageStamper;
import org.pdftools.utils.XYArray;

import lombok.extern.slf4j.Slf4j;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Slf4j
public class VerticalTextStamper implements PdfPageStamper {
    
    private String verticalTextData;    
    private Font font;
    private XYArray coordinates;    
    
    public VerticalTextStamper(String verticalTextData, XYArray coordinates, Font font) {
        this.coordinates = coordinates;
        this.verticalTextData = verticalTextData;
        this.font = font;
    }
    
    @Override
    public byte[] stamp(byte[] pdf, Rectangle pageSize) throws PdfConversionException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            reader = new PdfReader(pdf);
            stamper = new PdfStamper(reader, bos);           
            PdfContentByte contentByte = stamper.getOverContent(1);
           Chunk bindingText = new Chunk(verticalTextData);
            bindingText.setFont(this.font);
            Phrase p = new Phrase(bindingText);
            ColumnText.showTextAligned(contentByte, PdfContentByte.ALIGN_CENTER, p,
                    coordinates.getX(), coordinates.getY(), PdfConstants.ROTATE_VERTICAL);
            stamper.setFormFlattening(true);
            stamper.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in VerticalTextStamper", e);
        }
    }
}
