package org.pdftools.pdf.editing;

import java.io.IOException;
import java.util.List;

import org.pdftools.pdf.editing.tools.RenderTool;

import com.itextpdf.text.pdf.PdfStamper;

/**
 * This class is used to create a layer on top of existing pdf,
 * this will finally extend PdfLayer class from iText
 * @author asingh7!
 *
 */
public class MarkupLayer {
    
    protected PdfStamper stamper;
    private List<RenderTool> renders;
    
    public MarkupLayer(String name, PdfStamper stamper) throws IOException {
        this.stamper = stamper;
    }  
    
    public void render() throws PdfEditingException {
        for(RenderTool pdfObject : getRenders()) {
            pdfObject.render(stamper);
        }
    }

    public List<RenderTool> getRenders() {
        return renders;
    }

    public void setRenders(List<RenderTool> renders) {
        this.renders = renders;
    }
}
