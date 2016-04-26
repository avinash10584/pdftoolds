package org.pdftools.pdf.editing.tools;

import org.pdftools.pdf.editing.XYRectangle;
import org.pdftools.pdf.editing.tools.data.AnnotationData;
import org.pdftools.utils.XYArray;

/**
 * A Generic class to create toolset for PDF editing
 * @author asingh7!
 *
 * @param <K>
 */
public abstract class LineAnnotationRenderTool<K extends AnnotationData> implements RenderTool {
    
    private static final long serialVersionUID = 4574075899813534810L;
    
    protected int pageNo;
    protected XYArray startingPoint;
    protected float length;
    protected float strokeWidth;
    protected K data;
    
    public LineAnnotationRenderTool(K data, XYRectangle coords, int pageNo) {
        this.data = data;
        this.startingPoint = coords.getLowerLeft();
        this.length = coords.getTopRight().getX() - coords.getLowerLeft().getX();
        this.strokeWidth = coords.getTopRight().getY() - coords.getLowerLeft().getY();
        this.pageNo = pageNo;
    }
}
