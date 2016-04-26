package org.pdftools.pdf;

/**
 * PdfPrintOptions can be ussed to pass the options like scaling and fitToPage
 * to be used during print preview, option like copies add the copies option to
 * printer, printEmptyContinuationPage can be used to skip empty continuation
 * pages.
 *
 * @author asingh7!
 */
public class PdfPrintOptions {

    private Integer copies = 1;

    private Integer scaling;

    private Integer fitToPage;

    private Boolean printEmptyContinuationPage = true;

    public PdfPrintOptions() {
    }

    /**
     * Instantiates a new pdf print options.
     *
     * @param copies, scaling, fitToPage
     */
    public PdfPrintOptions(Integer copies, Integer scaling, Integer fitToPage) {
        this(copies, scaling, fitToPage, null);
    }

    /**
     * Instantiates a new pdf print options.
     *
     * @param copies, scaling, fitToPage, printEmptyContinuationPage
     */
    public PdfPrintOptions(Integer copies, Integer scaling, Integer fitToPage, Boolean printEmptyContinuationPage) {
        super();
        if (copies != null) {
            this.setCopies(copies);
        }
        if (scaling != null) {
            this.setScaling(scaling);
        }
        if (fitToPage != null) {
            this.setFitToPage(fitToPage);
        }
        if (printEmptyContinuationPage != null) {
            this.setPrintEmptyContinuationPage(printEmptyContinuationPage);
        }
    }

    /**
     * Gets the copies.
     *
     * @return the copies
     */
    public Integer getCopies() {
        return copies;
    }

    /**
     * Sets the copies.
     *
     * @param copies the new copies
     */
    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    /**
     * Gets the scaling.
     *
     * @return the scaling
     */
    public Integer getScaling() {
        return scaling;
    }

    /**
     * Sets the scaling.
     *
     * @param scaling the new scaling
     */
    public void setScaling(Integer scaling) {
        this.scaling = scaling;
    }

    /**
     * Gets the fit to page.
     *
     * @return the fit to page
     */
    public Integer getFitToPage() {
        return fitToPage;
    }

    /**
     * Sets the fit to page.
     *
     * @param fitToPage the new fit to page
     */
    public void setFitToPage(Integer fitToPage) {
        this.fitToPage = fitToPage;
    }

    /**
     * Gets the prints the empty continuation page.
     *
     * @return the prints the empty continuation page
     */
    public Boolean getPrintEmptyContinuationPage() {
        return printEmptyContinuationPage;
    }

    /**
     * Sets the prints the empty continuation page.
     *
     * @param printEmptyContinuationPage the new prints the empty continuation page
     */
    public void setPrintEmptyContinuationPage(Boolean printEmptyContinuationPage) {
        this.printEmptyContinuationPage = printEmptyContinuationPage;
    }
}
