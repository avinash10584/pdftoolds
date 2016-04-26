package org.pdftools.pdf.editing;

import com.itextpdf.text.BaseColor;

/**
 * RGB color matrix to keep the color of drawing as received from UI,
 * we extend iText BaseColor to avoid clutter of iText classes in rendering logic
 * and in UI layer
 * @author asingh7!
 *
 */
public class RenderColor extends BaseColor {
    
    public static final RenderColor WHITE = new RenderColor(255, 255, 255);
    public static final RenderColor LIGHT_GRAY = new RenderColor(192, 192, 192);
    public static final RenderColor GRAY = new RenderColor(128, 128, 128);
    public static final RenderColor DARK_GRAY = new RenderColor(64, 64, 64);
    public static final RenderColor BLACK = new RenderColor(0, 0, 0);
    public static final RenderColor RED = new RenderColor(255, 0, 0);
    public static final RenderColor PINK = new RenderColor(255, 175, 175);
    public static final RenderColor ORANGE = new RenderColor(255, 200, 0);
    public static final RenderColor YELLOW = new RenderColor(255, 255, 0);
    public static final RenderColor GREEN = new RenderColor(0, 255, 0);
    public static final BaseColor MAGENTA = new RenderColor(255, 0, 255);
    public static final RenderColor CYAN = new RenderColor(0, 255, 255);
    public static final RenderColor BLUE = new RenderColor(0, 0, 255);
    public static final RenderColor DARK_BLUE = new RenderColor(0, 0, 102);
    public static final RenderColor DARK_RED = new RenderColor(204, 0, 0);
    public static final RenderColor TRANSPARENT = new RenderColor(0, 0, 0, 0);
    
    public RenderColor(final int red, final int green, final int blue) {
        super(red, green, blue, 255);
    }
    public RenderColor(final float red, final float green, final float blue, final float alpha) {
        super(red, green, blue, alpha);
    }
}

