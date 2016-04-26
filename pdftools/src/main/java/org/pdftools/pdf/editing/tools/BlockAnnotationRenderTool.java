package org.pdftools.pdf.editing.tools;

import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.data.AnnotationData;

/**
 * A Generic class to create toolset for PDF editing
 * @author asingh7!
 *
 * @param <K>
 */
public abstract class BlockAnnotationRenderTool<K extends AnnotationData> implements RenderTool {
    
    private static final long serialVersionUID = 7428884608263123881L;
    
    protected int pageNo;
    protected XYRectangle coords;
    protected K data;
    
    public BlockAnnotationRenderTool(K data, XYRectangle coords, int pageNo) {
        this.data = data;
        this.coords = coords;
        this.pageNo = pageNo;
    }
}
