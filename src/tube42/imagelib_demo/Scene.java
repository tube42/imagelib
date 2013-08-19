/*
 * This file is a part of the TUBE42 imagelib, released under the LGPL license.
 *
 * Development page: https://github.com/tube42/imagelib
 * License:          http://www.gnu.org/copyleft/lesser.html
 */

package tube42.imagelib_demo;


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;

import tube42.lib.imagelib.*;


public abstract class Scene
{
    protected static Image img1, img2, img3, img4, img5, img6, img_icons;
    protected Image img_new;
    
    public boolean handle_key(int key, int gkey, boolean down, boolean repeated) { return false; }
    public void setFocus(boolean focused) 
    {
        if(!focused)
            img_new = null;
    }
    
    public abstract void paint(Graphics g, int w, int h);
    public abstract String getName();
    
    // ------------------------------------
    public void drawTwoImages(Graphics g, int w, int h, Image img1, Image img2)
    {        
        if(img2 == null)
            g.drawImage(img1, w/2, h/2, g.HCENTER | g.VCENTER);            
        else {
            if(img1.getWidth() + img2.getWidth() < w) {
                g.drawImage(img1, (w/2 - img1.getWidth()) / 2, (h-img1.getHeight()) / 2, 0);
                g.drawImage(img2, w / 2 + (w/2 - img2.getWidth()) / 2, (h-img2.getHeight()) / 2, 0);
            } else {
                g.drawImage(img1, 0, 0, 0);
                g.drawImage(img2, w - img2.getWidth(), h/2, 0);                
            }
        }
    }
    public void drawGraph(Graphics g, int [] points, int color, int x0, int y0, int w, int h, int max_x, int max_y)
    {
        g.setColor(color);        
        int old_y = 0, old_x = 0;
        for(int j = 0; j < points.length; j++) {
            int new_y = y0 + h - (points[j] * h) / max_y;
            int new_x = x0 + j * w / max_x;                
            if(j != 0) g.drawLine(old_x, old_y, new_x, new_y);
            old_x = new_x;
            old_y = new_y;
        }        
    }
    
    public void drawHistograms(Graphics g, int [][] hh, int x0, int y0, int w, int h)
    {
        int h4 = h / 4;        
        for(int i = 0; i < 4; i++) {
            int color = (i == 0) ? 0x808080 : (0xFF000000 >>> (i * 8));

            int max = 1;
            for(int j = 0; j < 256; j++) max = Math.max(max, hh[i][j]);
            
            drawGraph(g, hh[i], color, x0, y0 + h4 * i, w, h4, 256, max);
        }                    
    }
    
}


// ----------------------------------------------


/**
 * last scene
 */
/* package */ class SceneLast extends Scene
{    
    public String getName() { return "LAST"; }
    
    public void paint(Graphics g, int w, int h)
    {
        int fh = g.getFont().getHeight();
        
        g.setColor(0xFFFFFF);
        g.drawString("Press FIRE to exit", 10,  h - 2 * fh, 0);                
    }    
}

/**
 * intro scene
 */
/* package */ class SceneIntro extends Scene
{
    
    public SceneIntro() 
    {
        try {
            // load all our images right at the begining
            this.img1 = Image.createImage("/img1.jpg");
            this.img2 = Image.createImage("/img2.jpg");
            this.img3 = Image.createImage("/girl.jpg");
            this.img4 = Image.createImage("/img4.jpg");
            this.img5 = Image.createImage("/img5.jpg");
            this.img_icons = Image.createImage("/icons.png");
        } catch(Exception exx) { exx.printStackTrace(); }
    }
    
    public String getName() { return "INTRO"; }
        
    public void paint(Graphics g, int w, int h)
    {
        int fh = g.getFont().getHeight();        
        g.setColor(0xFFFFFF);
        g.drawString("Press arrows to change", 10, 2 * fh, 0);
        g.drawString("variables.", 10, 3 * fh, 0);        
        g.drawString("Press FIRE to move", 10,  h - 3 * fh, 0);
        g.drawString("to next screen.", 10, h - 2 * fh, 0);                
    }    
}


// ------------------------------------------------------

/**
 * demonstrate blending
 */
/* package */ class SceneBlend extends Scene
{
    private static final int
          BLEND_MIN = 0x00,
          BLEND_MAX = 0xFF,
          BLEND_STEP = (BLEND_MAX - BLEND_MIN) / 16
          ;
    
    // -------------------------------------------
    private int blend = (BLEND_MIN + BLEND_MAX) / 2;
    
    public String getName() { return "BLEND"; }
    
    protected Image update_image()
    {
        return ImageUtils.blend(img1, img2, blend);                        
    }
    
    // -----------------------
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {
        if(down) {
            if(gkey == Canvas.LEFT)       blend = Math.max(BLEND_MIN, blend - BLEND_STEP);
            else if(gkey == Canvas.RIGHT) blend = Math.min(BLEND_MAX, blend + BLEND_STEP);
            else return false; 
            img_new = update_image();
            return true;
        }
        return false;        
    }
    
    public void paint(Graphics g, int w, int h)
    {        
        if(img_new == null) 
            img_new = update_image();        
            
        // draw image if possible
        if(img_new != null) {
            int w2 = img_new.getWidth();
            int h2 = img_new.getHeight();
            g.drawImage(img_new, (w - w2) / 2, (h - h2) / 2, 0);
        }        
    }    
}




/**
 * resize example
 */
/* package */ class SceneResize extends SceneBlend
{
    private static final int
          SIZE_MIN = 0x00,
          SIZE_MAX = 0xFF,
          SIZE_DIV = 64,
          SIZE_STEP = (SIZE_MAX - SIZE_MIN) / 16
          ;
    
    // -------------------------------------------
    private int size = 32;
    private boolean filter = true, mipmap = true;
    
    public String getName() { return "RESIZE"; }
    
    protected Image update_image()
    {
        
        int w = img1.getWidth();
        int h = img1.getHeight();
        
        return ImageUtils.resize(img1,
                  size * w / SIZE_DIV,
                  size * h / SIZE_DIV,
                  filter, mipmap);
    }
    
    // -----------------------
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {
        if(down) {
            if(gkey == Canvas.LEFT)       size = Math.max(SIZE_MIN, size - SIZE_STEP);
            else if(gkey == Canvas.RIGHT) size = Math.min(SIZE_MAX, size + SIZE_STEP);
            else if(key == '*') mipmap = !mipmap;
            else if(key == '#') filter = !filter;
            else return false; 
            img_new = update_image();
            return true;
        }
        return false;        
    }
    
    public void paint(Graphics g, int w, int h)
    {
        super.paint(g, w, h);
        
        int fh = g.getFont().getHeight();
        
        // show the parameters
        g.setColor(0xFF0000);
        g.drawString("size = " + size, 10, h - 4 * fh, 0);
        g.drawString(filter ? "# filtered" : "# unfiltered", 10, h - 3 * fh, 0);
        g.drawString(mipmap ? "* mipmap" : "* scale", 10, h - 2 * fh, 0);        
    }    
}



/**
 * crop 
 */
/* package */ class SceneCrop extends Scene
{
    
    public String getName() { return "CROP"; }
    
    public void setFocus(boolean f)
    {
        if(f) 
            img_new = ImageUtils.crop(img3, -5, -5, 100, 50);
        
        super.setFocus(f);
    }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img3, img_new);
    }    
}

/**
 * demonstrate histogram
 */
/* package */ class SceneHistogram extends Scene
{
    // -------------------------------------------
    protected int [][] histogram;   
    public SceneHistogram()
    {
        this.histogram = ImageAnalysisUtils.getARGBHistogram(img3);
    }
    
    public String getName() { return "HISTOGRAM"; }
    
    // -----------------------
    public void paint(Graphics g, int w, int h)
    {
        int w2 = img3.getWidth();
        int h2 = img3.getHeight();
        g.drawImage(img3, (w - w2) / 2, (h - h2) / 2, 0);        
        
        drawHistograms( g, histogram, 0, 0, w, h / 2);
    }
}


/**
 * demonstrate Histogram Equalization
 */
/* package */ class SceneHistogramEqualization extends Scene
{
    // -------------------------------------------
    protected int [][] histogram, histogram_eq;   
    
    public void setFocus(boolean focused) {
        if(focused) {
            // 1. get original histogram
            this.histogram = ImageAnalysisUtils.getARGBHistogram(img3);
            
            // 2. build the new component transformation tables
            final int total = img3.getWidth() * img3.getHeight();
            byte [][] transform = new byte[4][]; 
            for(int c = 1; c < 4; c++) {
                int sum = 0;
                transform[c] = new byte[256];
                for(int j = 0; j < 255; j++) {
                    sum += histogram[c][j];
                    int l3 = (sum / 2 + sum * 256) / total;
                    transform[c][j] = (byte) l3;
                }
            }
            
            // 3. apply the transformation to the original image            
            this.img_new = ImageFxUtils.transformARGB(img3, null, transform[1], transform[2], transform[3]);            
            this.histogram_eq = ImageAnalysisUtils.getARGBHistogram(img_new); 
            
        }
        super.setFocus(focused);
    }
    
    public String getName() { return "HISTOGRAM EQUALIZATION"; }
    
    // -----------------------
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img3, img_new);
        drawHistograms( g, histogram, 0, 0, w, h / 2);
        drawHistograms( g, histogram_eq, 0, h/2-5, w, h / 2);
        
    }
}


// -----------------------------------------------------

/**
 * demonstrate color curves
 */
/* package */ class SceneColorCurves extends Scene
{
    protected byte [][]curves;
    protected int p0, p1;
    
    public void setFocus(boolean f)
    {
        if(f) {
            this.p0 = 8;
            this.p1 =-5;            
            // allocate curves
            curves = new byte[4][];
            for(int i = 0; i < curves.length; i++)
                curves[i] = new byte[256];
            
            update_image();
        }
        
        super.setFocus(f);
    }
    
    public String getName() { return "COLOR CURVES"; }

    protected void update_image()
    {
        // 1. generate the curves
        for(int i = 0; i < 256; i++) {
            curves[1][i] = (byte)i;
            curves[2][i] = (byte) ((Math.max(0, Math.min(255, i  + (i-128) * p0 / 8)) + i ) / 2);
            curves[3][i] = (byte) ((Math.max(0, Math.min(255, i  + (i-128) * p1 / 8)) + i ) / 2);
            
        }
        // 2. create the new image
        img_new = ImageFxUtils.transformARGB(img1, null, curves[1], curves[2], curves[3]);
    }
    
    // -----------------------
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {        
        if(down) {
            if(gkey == Canvas.LEFT)       p0 = Math.max(-32, p0-1);
            else if(gkey == Canvas.RIGHT) p0 = Math.min(+32, p0+1);
            else if(gkey == Canvas.UP)    p1 = Math.min(+32, p1-1);
            else if(gkey == Canvas.DOWN)  p1 = Math.min(+32, p1+1);
            else return false; 
            update_image();
            return true;
           }
         
        return false;        
    }
    
    public void paint(Graphics g, int w, int h)
    {
        // show the images
        drawTwoImages(g, w, h, img1, img_new);
        
        // show parameters
        int fh = g.getFont().getHeight();
        g.setColor(0x404080);
        g.drawString("p0=" + p0 + ", p1 =" + p1, 5, 2 * fh, 0);
        
        // draw the curves:
        for(int i = 1; i < 4; i++) {
            byte [] c = curves[i];
            g.setColor( 0xFF000000 >>> (i * 8)); // full R, G or B
            
            int old_x = 0, old_y = 0;                        
            int w1 = w * 5 / 6;
            int h1 = h * 5 / 6;
            int x0 = w / 12;
            int y0 = h / 12;
            
            for(int j = 0; j < 256; j++) {
                int n = ((int)c[j]) & 0xFF;
                
                int new_x = x0 + i + j * w1 / 256; // add i so thet dont always overlap
                int new_y = y0 + h1 - n * h1 / 256;
                
                if(j != 0)
                    g.drawLine(old_x, old_y, new_x, new_y);
                
                old_x = new_x;
                old_y = new_y;
            }
        }
        
    }    
}


// -----------------------------------------------------

/**
 * demonstrate thresholds using color curves
 */
/* package */ class SceneColorThreshold extends Scene
{
    protected byte []curve;
    protected int [] curve_i;
    protected int p0;
    
    public void setFocus(boolean f)
    {
        if(f) {
            this.p0 = 64;
            this.curve   = new byte[256];
            this.curve_i = new int[256];
            update_image();
        }
        
        super.setFocus(f);
    }
    
    public String getName() { return "COLOR CURVES"; }

    protected void update_image()
    {
        // 1. generate the curves
        for(int i = 0; i < 256; i++) {
            curve_i[i] = i >= p0 ? i : 0;
            curve[i] = (byte) curve_i[i];
        }
        
        // 2. create the new image
        img_new = ImageFxUtils.transformARGB(img3, null, curve, curve, curve);
    }
    
    // -----------------------
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {        
        if(down) {
            if(gkey == Canvas.LEFT)       p0 = Math.max(0  , p0-5);
            else if(gkey == Canvas.RIGHT) p0 = Math.min(255, p0+5);
            else return false; 
            update_image();
            return true;
           }
         
        return false;        
    }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img3, img_new);
        drawGraph(g, curve_i, 0xFF0000, 10, 10, w-20, h-20, 256, 256);
    }    
}




// -----------------------------------------------------

/**
 * demonstrate color temp using color curves
 */
/* package */ class SceneColorWarmth extends Scene
{
    public void setFocus(boolean f)
    {
        if(f)
            img_new = ImageFxUtils.transformARGB(img5, 0, 30, 30, 0);        
        
        super.setFocus(f);
    }
    
    public String getName() { return "COLOR WARMTH"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img5, img_new);
    }    
}

/* package */ class SceneColorCold extends Scene
{
    public void setFocus(boolean f)
    {
        if(f)
            img_new = ImageFxUtils.transformARGB(img5, 0, 0, 0, 30);
        
        super.setFocus(f);
    }
    
    public String getName() { return "COLOR COLD"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img5, img_new);
    }    
}


// -----------------------------------------------------

/**
 * demonstrate filter
 */
/* package */ class SceneFilter extends Scene
{
        
    public void setFocus(boolean focused) 
    {
        if(focused) {
            
            int [][] matrix  = {                
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 1, 1 } 
            };
            
            this.img_new = ImageFxUtils.applyFilter(img3, matrix, 8);
        }
        
        super.setFocus(focused);
    }
    
    // -----------------------
    public String getName() { return "IMAGE FILTERS"; }
    
    public void paint(Graphics g, int w, int h)
    {   
        drawTwoImages(g, w, h, img3, img_new);
    }    
}



/**
 * demonstrate pixel modifier
 */
/* package */ class SceneGrayscale extends Scene
{
        
    public void setFocus(boolean focused) 
    {
        if(focused) {
            
            // lets do this "in place" directly on the input image
            this.img_new = ImageUtils.moveToCPU(img1);
            ImageFxUtils.applyModifier(img_new, new ImageFxUtils.PixelModifier() {              
                      public int apply(int pixel, int x, int y) {                                    
                      int r = (pixel >> 16) & 0xFF;
                      int g = (pixel >>  8) & 0xFF;
                      int b = (pixel >>  0) & 0xFF;
                      
                      int tmp = (r * 78 + g * 151 + b * 27) >> 8;
                      tmp |= (tmp << 8) | (tmp << 16);
                      return (pixel & 0xFF000000) | tmp;
                  }
                  }, true);
        }
        
        super.setFocus(focused);
    }
    
    // -----------------------
    public String getName() { return "GRAYSCALE MODIFIER"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img1, img_new);
    }    
}

/**
 * demonstrate pixel modifier (2): sepia effect
 */
/* package */ class SceneSepia extends Scene
{
        
    public void setFocus(boolean focused) 
    {
        if(focused) {
            this.img_new = ImageFxUtils.applyModifier(img4, new ImageFxUtils.PixelModifier() {              
                      public int apply(int pixel, int x, int y) {                                    
                      int r = (pixel >> 16) & 0xFF;
                      int g = (pixel >>  8) & 0xFF;
                      int b = (pixel >>  0) & 0xFF;
                      
                      int r2 = Math.min(0xFFFF, r * 101 + g * 197 + b * 48) & 0xFF00;
                      int g2 = Math.min(0xFFFF, r *  89 + g * 176 + b * 43) & 0xFF00;
                      int b2 = Math.min(0xFFFF, r *  67 + g * 137 + b * 43) >> 8;
                      
                      return (pixel & 0xFF000000) | (r2 << 8) | g2 | b2;
                  }
                  }
                      );
        }
        
        super.setFocus(focused);
    }
    
    // -----------------------
    public String getName() { return "SEPIA MODIFIER"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img4, img_new);
    }    
}



/**
 * demonstrate color space conversion: the retro effect:
 * 
 * 1. add noise to luminance 
 * 2. reduce chroma
 * 3. run a 3x1 average filter on the result
 */
/* package */ class SceneRetro extends Scene
{
        
    public void setFocus(boolean focused) 
    {
        if(focused) {
            this.img_new = ImageFxUtils.applyModifier(img1, new ImageFxUtils.PixelModifier() {
                      private int [] buffer = new int[4];
                      private Random rnd = new Random();
                      public int apply(int pixel, int x, int y) {                                    
                      ColorUtils.ARGB2AYCbCr(pixel, buffer);
                      buffer[1] = Math.min(255, buffer[1] + 10 + (rnd.nextInt() & 0x1F));
                      buffer[2] = (buffer[2] * 7) >> 3;
                      buffer[3] = (buffer[3] * 7) >> 3;
                      return ColorUtils.AYCbCr2ARGB(buffer);
                  }
                  }
                      );
            
            // Edge detection matrix
            int [][]matrix = { 
                {  0, -1,  0}, 
                { -1,  5, -1},
                {  0, -1,  0} 
            };
            img_new = ImageFxUtils.applyFilter(img_new, matrix, 1);
        }
        
        super.setFocus(focused);
    }
    
    // -----------------------
    public String getName() { return "RETRO EFFECT (COLORSPACES)"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img1, img_new);
    }    
}


/**
 * demonstrate pixel modifier (3): quantisation
 */
/* package */ class SceneQuantisation extends Scene
{
    protected int [][] histogram1, histogram2;   
    
    public void setFocus(boolean focused) 
    {
        if(focused) {
            this.img_new = ImageFxUtils.applyModifier(img2, new ImageFxUtils.PixelModifier() {              
                      public void apply(int []pixels, int [] output, int size, int y) {
                      for(int i = 0; i < size; i++)
                      output[i] = (pixels[i] & 0x00E0E0E0) | 0xFF0F0F0F; // 8-bit -> 3-bit quantisation + no alpha
                  }
                  }
                      );
            
            this.histogram1 = ImageAnalysisUtils.getARGBHistogram(img2);
            this.histogram2 = ImageAnalysisUtils.getARGBHistogram(img_new);
        }
        
        super.setFocus(focused);
    }
    
    // -----------------------
    public String getName() { return "Quantisation modifier"; }
    
    public void paint(Graphics g, int w, int h)
    {
        drawTwoImages(g, w, h, img2, img_new);
        drawHistograms( g, histogram1, 0, 0, w, h / 2);
        drawHistograms( g, histogram2, 0, h/2-5, w, h / 2);
        
    }    
}




/**
 * performance test: transparent vs opaque
 */
/* package */ class ScenePerfTransparent extends Scene
{
    
    private String [] modes = { "Transparent", "Opque", "Opqeue on GPU" };
    private String [] perfs = { "??", "??", "??" };
    private Image [] images;
    
    /* package */ScenePerfTransparent()
    {
        images = new Image[3];
        images[0] = img_icons;
        images[1] = ImageUtils.setBackground(img_icons, 0xFF101010);
        images[2] = ImageUtils.moveToGPU(images[1]);
    }
    
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {
        return !down;
    }
    
    
    // -----------------------
    public String getName() { return "Transparent / opaque"; }
        
    public void paint(Graphics g, int w, int h)
    {
        for(int j = 0; j < 3; j++) {
            
            long t0 = System.currentTimeMillis();
            for(int i = 0; i < 700; i++)
                g.drawImage(images[j], i & 0x1F, 30 + (j * 64) + (i & 0x1F), 0);
            long t1 = System.currentTimeMillis();
            
            long dt = Math.max(1, t1 - t0);
            perfs[j] = modes[j] + " " + dt + " [ms]";
        }
                
        g.setColor(0xFFFFFF);
        for(int i = 0; i < perfs.length; i++)
            g.drawString(perfs[i], 10,  50 + i * 64, 0);                        
    }        
}


/**
 * performance test: GPU vs CPU memory
 */

/* package */ class ScenePerfGPU extends Scene
{   
    
    private String [] modes = { "Orginal", "CPU", "GPU" };
    private String [] perfs = { "??", "??", "??" };
    
    private Image [] images;
    
    /* package */ScenePerfGPU()
    {
        images = new Image[3];
        images[0] = img4;
        images[1] = ImageUtils.moveToCPU( images[0]);
        images[2] =  ImageUtils.moveToGPU( images[0]);
    }
    
    
    public boolean handle_key(int key, int gkey,  boolean down, boolean repeated)
    {
        return !down;
    }
    
    // -----------------------
    public String getName() { return "GPU vs CPU memory"; }
        
    public void paint(Graphics g, int w, int h)
    {
        for(int j = 0; j < 3; j++) {
            
            long t0 = System.currentTimeMillis();
            for(int i = 0; i < 700; i++)
                g.drawImage(images[j], i & 0x1F, 30 + (j * 64) + (i & 0x1F), 0);
            long t1 = System.currentTimeMillis();
            
            long dt = Math.max(1, t1 - t0);
            perfs[j] = modes[j] + " " + dt + " [ms]";
        }
                
        g.setColor(0xFF00FF);
        for(int i = 0; i < perfs.length; i++)
            g.drawString(perfs[i], 10,  50 + i * 64, 0);                        
    }            
}

