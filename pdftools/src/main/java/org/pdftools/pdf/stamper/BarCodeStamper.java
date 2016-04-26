package org.pdftools.pdf.stamper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.pdftools.pdf.PdfConversionException;
import org.pdftools.pdf.PdfPageStamper;
import org.pdftools.utils.XYArray;

import lombok.extern.slf4j.Slf4j;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Slf4j
public class BarCodeStamper implements PdfPageStamper {
    
    private static final String IMAGE_TYPE = "jpeg";
    
    private String barCode;    
    private XYArray coordinates;
    private XYArray imageSize;
    
    public BarCodeStamper(String barCode, XYArray coordinates) {
        this.barCode = barCode;
        this.coordinates = coordinates;
    }
    
    public BarCodeStamper(String barCode, XYArray coordinates, XYArray imageSize) {
        this(barCode, coordinates);
        this.imageSize = imageSize;
    }
    
    @Override
    public byte[] stamp(byte[] pdf, Rectangle pageSize) throws PdfConversionException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
                ByteArrayOutputStream imageout = new ByteArrayOutputStream();) {
            reader = new PdfReader(pdf);
            stamper = new PdfStamper(reader, bos);           
            PdfContentByte contentByte = stamper.getOverContent(1);            
            Barcode39 code39ext = new Barcode39();
            code39ext.setCode(barCode);
            code39ext.setStartStopText(false);
            code39ext.setExtended(true);
            java.awt.Image rawImage = code39ext.createAwtImage(Color.BLACK, Color.WHITE);
            BufferedImage outImage = new BufferedImage
                    (rawImage.getWidth(null), rawImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
            outImage.getGraphics().drawImage(rawImage, 0, 0, null);
            ImageIO.write(outImage, IMAGE_TYPE, imageout);
            if (imageSize == null) {
                contentByte.addImage(Image.getInstance(imageout.toByteArray()), 
                        outImage.getWidth(), 0f, 0f, outImage.getHeight(), this.coordinates.getX(), this.coordinates.getY());  
            } else {
                contentByte.addImage(Image.getInstance(imageout.toByteArray()), 
                    this.imageSize.getX(), 0f, 0f, this.imageSize.getY(), this.coordinates.getX(), this.coordinates.getY());
            }
            stamper.setFormFlattening(true);
            stamper.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("Exception in BarCodeStamper", e);
        }
    }
}
