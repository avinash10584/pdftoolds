package org.pdftools.pdf.editing.tools.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.pdftools.pdf.editing.RenderColor;

import lombok.Data;
import lombok.ToString;

/**
 * Class to hold annotation data
 * {@link AnnotationCategory} is Enum type categories as per PDF 32000-1:2008, we don't support 
 * POLYGON, INK , SCREEN , 3D and PRINTER annotations
 * annotationName is the unique annotation name per page.
 * annotationText specifies item 'Contents' in Pdfdictionary
 * annotationName is 'NM' in Pdfdictionary
 * isPrintable is used to set flags for printing for the annotation
 * createdDt should be added to the Pdf Dictionary in below format 
 * (D:YYYYMMDDHHmmSSOHH'mm )
 * where:
    YYYY shall be the year
    MM shall be the month (01–12)
    DD shall be the day (01–31)
    HH shall be the hour (00–23) 
    O should be a - showing difference from UT
    PDF 32000-1:2008
 * @author asingh7!
 *
 */
@Data
@ToString
public class AnnotationData {

    public static enum AnnotationCategory {TEXT, LINE, LINK, SQUARECIRCLE, MARKUP, STAMP, ATTACHMENT};
    public static enum AnnotationState {Accepted, Rejected, Cancelled, Completed, None};
    
    private String annotationText;
    private String annotationTitle;
    private AnnotationCategory annotationCategory;
    private String annotationName;
    private Date createdDt;
    private boolean isPrintable = false;
    private String parentAnnotationId;
    private AnnotationState state = AnnotationState.None;
    private RenderColor color = RenderColor.YELLOW;       
    
    private SimpleDateFormat isoDateFormat = new SimpleDateFormat("YYYYMMDDHHmmSS");    
    
    public String getUniqueAnnotationName() {
        return String.format("OC_%s_%s", annotationCategory, System.currentTimeMillis());
    }

    public String pdfFormattedDate(Date date) {
        String formattedDate = isoDateFormat.format(date);
        TimeZone tz = TimeZone.getDefault();  
        long offSetMinutes = tz.getOffset(date.getTime())/ 1000 / 60;
        long offSetHours = TimeUnit.MINUTES.toHours(offSetMinutes);
        return String.format("D:%s-HH'mm'", formattedDate, TimeUnit.MINUTES.toHours(offSetMinutes), 
                offSetMinutes - offSetHours*60);
    }
    
    public AnnotationData(String annotationTitle, AnnotationCategory annotationType) {
        this.createdDt = new Date();
        this.annotationTitle = annotationTitle;
        this.annotationCategory = annotationType;
        this.annotationName = getUniqueAnnotationName();
    }
    
    public AnnotationData(String annotationTitle, AnnotationCategory annotationType, RenderColor color) {
        this(annotationTitle, annotationType);
        this.color = color;
    } 
    
    public AnnotationData(String annotationText, String annotationTitle, AnnotationCategory annotationType) {
        this.createdDt = new Date();
        this.annotationText = annotationText;
        this.annotationTitle = annotationTitle;
        this.annotationCategory = annotationType;
        this.annotationName = getUniqueAnnotationName();
    }
    
    public AnnotationData(String annotationText, String annotationTitle, AnnotationCategory annotationType, boolean isPrintable) {
        this(annotationText, annotationTitle, annotationType);
        this.isPrintable = isPrintable;
    }

    public AnnotationData(String annotationText, String annotationTitle, AnnotationCategory annotationType, RenderColor color) {
        this(annotationText, annotationTitle, annotationType);
        this.color = color;
    } 
    
    public AnnotationData(String annotationText, String annotationTitle, AnnotationCategory annotationType, 
            boolean isPrintable, RenderColor color) {
        this(annotationText, annotationTitle, annotationType);
        this.isPrintable = isPrintable;
        this.color = color;
    }       
}
