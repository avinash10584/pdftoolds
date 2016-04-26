package org.pdftools.pdf.stamper;

import static org.pdftools.pdf.PdfConstants.*;

import java.io.ByteArrayOutputStream;

import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageStamper;
import org.pdftools.utils.NumberEnum;

import lombok.extern.slf4j.Slf4j;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Slf4j
public class FooterStamper implements PdfPageStamper {

    private static final float FOOTER_BOTTOM_MARGIN = 8f;
    private static final float FOOTER_HEIGHT = 4f;
    private static final float FOOTER_OPACITY = 0.8f;
    private static final float FOOTER_FONT_SIZE = 7f;
    private static final String FOOTER_STRING = "Page %s of %s";

    /** {@inheritDoc} */
    @Override
    public byte[] stamp(byte[] pdf, Rectangle pageSize) throws PdfConversionException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            reader = new PdfReader(pdf);
            stamper = new PdfStamper(reader, bos);      
            for(int pageNo = 1; pageNo <= reader.getNumberOfPages(); pageNo++) {
                PdfContentByte contentByte = stamper.getOverContent(pageNo);
                float posX = pageSize.getWidth() / NumberEnum.TWO.getValue();
                float posY = 0;
                contentByte.setColorFill(BaseColor.WHITE);
                PdfGState opacity = new PdfGState();
                opacity.setFillOpacity(FOOTER_OPACITY);
                contentByte.setGState(opacity);
                contentByte.rectangle(0, 0, pageSize.getWidth(), FOOTER_HEIGHT + posY);
                contentByte.fill();        
                contentByte.setColorFill(BaseColor.BLACK);
                contentByte.setFontAndSize(fArial, FOOTER_FONT_SIZE);
                contentByte.beginText();        
                contentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, 
                        String.format(FOOTER_STRING, pageNo , reader.getNumberOfPages()), 
                        posX, FOOTER_BOTTOM_MARGIN + posY, 0f);
                contentByte.endText();
            }
            stamper.setFormFlattening(true);  
            stamper.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in FooterStamper", e);
        }
    }
}
