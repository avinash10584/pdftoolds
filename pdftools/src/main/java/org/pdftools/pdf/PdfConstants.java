package org.pdftools.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;

/**
 * PDF supporting constants
 */
@Slf4j
public class PdfConstants {
    
    public static final String FLYING_SAUCER_LOGGER = "xr.util-logging.loggingEnabled";
    
    public static final String PDF_EXTENSION = ".pdf";
    public static final String PAGE_OREINTATION_LANDSCAPE = "landscape";
    public static final String PDF_BLANK_SPACE = "\u00A0"; 
    public static final String LINE_BREAK = "\n";
    public static final String ARIAL_FONT_FILE_PATH = "font/arial/ARIAL.TTF";
    public static final String ARIALBD_FONT_FILE_PATH = "font/arial/ARIALBD.TTF";
    public static final String ARIALBI_FONT_FILE_PATH = "font/arial/ARIALBI.TTF";
    public static final String ARIALI_FONT_FILE_PATH = "font/arial/ARIALI.TTF";
    public static final String TIMES_FONT_FILE_PATH = "font/times/TIMES.TTF";
    public static final String TIMESBD_FONT_FILE_PATH = "font/times/TIMESBD.TTF";
    public static final String TIMESBI_FONT_FILE_PATH = "font/times/TIMESBI.TTF";
    public static final String TIMESI_FONT_FILE_PATH = "font/times/TIMESI.TTF";
    public static final String DEFAULT_PAGE_SIZE = "LETTER";
    public static final float DEFAULT_SCALING = 100;
    public static final float DEFAULT_VIEW_SCALING = 150;
    public static final float PAGE_SIZE_LETTER_WIDTH = 612;
    public static final float PAGE_SIZE_LETTER_HEIGHT = 792;
    public static final float FONT_SMOOTHING_THRESHOLD = 7f;
    public static final float FONT_10F = 10f;
    public static final float ROTATE_VERTICAL = 90f;
    public static final String ATTR_VERTICAL_TEXT_X = "pdf-x";
    public static final String ATTR_VERTICAL_TEXT_Y = "pdf-y";
    public static final String ATTR_VERTICAL_TEXT_FONT_SIZE = "pdf-font-size";
    public static final String ATTR_VERTICAL_TEXT_FONT_WEIGHT = "pdf-font-weight";
    public static BaseFont fArial = null;     
    
    static {
        try{
            fArial = BaseFont.createFont(ARIAL_FONT_FILE_PATH, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            log.error("Could not initialize Arial Font for PDF generation {}", e);
        }
    }    
    
    /**
   	 * PDF Property Constants 
     */
    public static class PDFPROPERTY {
        public static final String TITLE = "Title";
        public static final String KEYWORDS = "Keywords";
        public static final String CREATOR = "Creator";
        public static final String AUTHOR = "Author";
    }
    
    /**
   	 * OC Form Constants 
     */
    public static class OCFORM {
        
        @SuppressWarnings("serial")
        public static final List<String> WIZARD_FORMS = new ArrayList<String>() {{
            add("PTO-892");
            add("TSS-PTO-SB-07");
            add("PTO-SEC");
            add("PTO-850-IIM");
            add("PTOL-2079-Notice");
        }};
        
        public static final String PTO_SEC = "PTO-Supplemental Examination Certificate";
        public static final String FIELD_RENAME_PATTERN = "%s_%d";   
        public static final String PTO_892 = "PTO-892";
        public static final String SB07 = "TSS-PTO-SB-07";
    }
    
    /**
   	 * OC field Constants 
     */
    public static class OCFIELD {
        public static final String OTHER = "other";
        public static final String APPNUM = "txtHeaderAppNum";
        public static final String SB06_ENTITY = "chkLargeEntity";
        public static final String SB06_LARGE_ENTITY_SHOW = "chkLargeEntityForShow";
        public static final String SB06_MICRO_ENTITY = "chkMicroEntity";
        public static final String SB06_SMALL_ENTITY = "chkSmallEntity";
        
        public static final String SB06_LARGE_ENTITY_VAL = "N";
        public static final String SB06_MICRO_ENTITY_VAL = "M";
        public static final String SB06_SMALL_ENTITY_VAL = "Y";        
    }
    
    /**
	 * HTML Constants 
     */
    public static class HTMLConstant {                
        public static final String HTML_LINE_BREAK = "<br>";
        public static final String HTML_CLASS_ROTATE = "rotate";
        public static final String HTML_TAG_INPUT = "input";
        public static final String HTML_TAG_DIV = "div";
        public static final String HTML_INPUT_CHECKBOX = "checkbox";
        public static final String HTML_IMAGE_CHECKBOX = "<div class='checkbox'><img style='%s' class='%s checkimage' src='%s'/></div>";
        public static final String HTML_CHECKBOX_EMPTY_HTML = "<div class='checkbox'><img style='%s' class='%s uncheckimage' src='%s'/></div>";              
        public static final String HTML_INPUT_TEXT = "text";        
        public static final String HTML_ATTR_DATA_FIELD = "data-field";
        public static final String HTML_ATTR_TYPE = "type";
        public static final String HTML_ATTR_CHECKED = "checked";
        public static final String HTML_ATTR_STYLE = "style";
        public static final String HTML_ATTR_CLASS = "class";
        
        public static final String HTML_TMPLT_LOCATION = "forms";
        public static final String IMAGE_CHECKED = "images/check-mark-inside-a-square-outline-box.png";
        public static final String IMAGE_UNCHECKED = "images/empty-box.png";
        public static final String HTML_EXTENSION = ".html";
        public static final String CSS_EXTENSION = ".css";
        public static final String FILE_ENCODING = "UTF-8";
        public static final String HTML_BLANK_SPACE = "&nbsp;";        
        public static final String HTML_PAGE_OREINTATION = "landscape";        
        public static final String[] BLACKLIST_ATTR =  {"onkeypress","onclick","onkeydown","onkeyup","readonly",
                "data-page-linked", "data-page-multiple", "data-page-count"};
        public static final String[] BLACKLIST_TAGS =  {"head","meta","script"};        
        public static final String HTML_TMPLT_FIELD_APPNUM = "AppNo";
        public static final String HTML_TMPLT_FIELD_HEADERAPPNUM = "txtHeaderAppNum";
        
        public static final String HTML_TMPLT_INPUT_CLASS = "form-element";
        public static final String HTML_TMPLT_DATA_ATTR = "data-field";
        public static final String HTML_TMPLT_PAGE_NUMBER_ATTR = "page-number";
        public static final String HTML_TMPLT_PAGE_MARGIN_ATTR = "page-margin";
        public static final String HTML_TMPLT_CONTINUATION_CLASS = "continuation-field";
        public static final String HTML_TMPLT_HANDLEBAR = "handleBarTemplate";
        public static final String HTML_TMPLT_INPUT_CLASS3 = "AppNo";
        public static final String HTML_TMPLT_PAGE_CLASS = "pagenew";
        public static final String HTML_TMPLT_BOX_LAYOUT_CLASS = "letter-boxed";
        public static final String HTML_TMPLT_CONTINUATION_PAGE_CLASS = "continuation-page";        
        public static final String HTML_TMPLT_INPUT_REPLACE_CLASS = "inputreplacement";
        public static final String HTML_TMPLT_DIV_REPLACE_CLASS = "divreplacement";
        public static final String HTML_TMPLT_HAS_DATA_CLASS = "not-empty";
        public static final String HTML_TMPLT_EMPTY_DATA_CLASS = "empty";
        public static final String INPUT_REPLACE = "<span id='%s' style='%s' class='%s %s %s'>%s</span>";
        public static final String DIV_REPLACE = "<div id='%s' contenteditable='true' style='%s' class='%s %s %s'>%s</div>";
    }
    
    
    /**
   	 * Velocity Constant 
     */
    public static class VelocityConstant {    
        public static final String VELOCITY_RESOURCE_CLASSPATH = "classpath";
        public static final String VELOCITY_CLASS_LOADER = "classpath.resource.loader.class";
        public static final String COMMON_FORM_TEMPLATE = "META-INF/velocity/formVelocityTemplate.vm";
        
        public static final String VELOCITY_TMPLT_PARAM_FORM_NAME = "formName";
        public static final String VELOCITY_TMPLT_PARAM_FORM_CSS = "formCSS";
        public static final String VELOCITY_TMPLT_PARAM_FORM_CONTENT = "formContent";    
        public static final String VELOCITY_TMPLT_PARAM_PAGE_SIZE = "size";
        public static final String VELOCITY_TMPLT_PARAM_PAGE_MARGIN = "pageMargin";
        public static final String VELOCITY_TMPLT_PARAM_PAGE_PADDING = "pagePadding";
        public static final String VELOCITY_TMPLT_MARGIN_ZERO = "0in;";
        public static final String VELOCITY_TMPLT_MARGIN_SECOND_PAGE = "0.6in 0.25in 0.4in 0.25in";
        public static final String VELOCITY_TMPLT_MARGIN_STYLE = "margin:%s";    
    }
}
