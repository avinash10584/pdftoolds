package org.pdftools.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

public class PDFToImage {

    public static final int DPI = 300;
    public static final String IMAGE_FORMAT = "png";
    
    private PDFToImage() {}

    public static List<byte[]> getImages(byte[] pdf) throws Exception {        
        List<byte[]> images = new ArrayList<>();
        PDDocument document = PDDocument.loadNonSeq(new ByteArrayInputStream(pdf), null);
        List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
        for (PDPage pdPage : pdPages) { 
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, DPI);
            ImageIOUtil.writeImage(bim, IMAGE_FORMAT, bout, DPI);
            bout.flush();
            bout.close();            
            images.add(bout.toByteArray());
        }
        return images;
    }
}