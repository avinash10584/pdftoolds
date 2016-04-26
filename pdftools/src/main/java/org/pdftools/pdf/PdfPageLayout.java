package org.pdftools.pdf;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;

/**
 * Page Layout to render pdf for different size and orientation.
 *
 * @author asingh7!
 */
public class PdfPageLayout {

    private float pageHeight;
    
    private float pageWidth;
    
    private String pageSize = PdfConstants.DEFAULT_PAGE_SIZE;
    
    private Boolean landscape = false;

    public PdfPageLayout() {}
    
    /**
     * Instantiates a new pdf page layout.
     *
     * @param pageSize, landscape 
     */
    public PdfPageLayout(String pageSize, Boolean landscape) {
        super();
        if(pageSize != null) {
            this.pageSize = pageSize;
        }
        if(landscape != null) {
            this.landscape = landscape;
        }
        Rectangle rect = PageSize.getRectangle(pageSize);
        this.pageHeight = rect.getHeight();
        this.pageWidth = rect.getWidth();
    }

    /**
     * Gets the size in inches.
     *
     * @param size
     * @return the size in inches
     */
    public static float getSizeInInches(float size) {
        return Utilities.pointsToInches(size);
    }

    /**
     * Gets the page size with rotation.
     *
     * @return the page size with rotation
     */
    public Rectangle getPageSizeWithRotation() {
        return landscape ? PageSize.getRectangle(pageSize).rotate() : PageSize
            .getRectangle(pageSize);
    }

    /**
     * Gets the page height.
     *
     * @return the page height
     */
    public float getPageHeight() {
        return pageHeight;
    }

    /**
     * Sets the page height.
     *
     * @param pageHeight the new page height
     */
    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
    }

    /**
     * Gets the page width.
     *
     * @return the page width
     */
    public float getPageWidth() {
        return pageWidth;
    }

    /**
     * Sets the page width.
     *
     * @param pageWidth the new page width
     */
    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the new page size
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Checks if is landscape.
     *
     * @return true, if is landscape
     */
    public boolean isLandscape() {
        return landscape;
    }

    /**
     * Sets the landscape.
     *
     * @param landscape the new landscape
     */
    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
}
