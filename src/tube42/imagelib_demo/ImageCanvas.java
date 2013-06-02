/*
 * This file is a part of the TUBE42 imagelib, released under the LGPL license.
 *
 * Development page: http://projects.developer.nokia.com/imagelib
 * License:          http://www.gnu.org/copyleft/lesser.html
 */

package tube42.imagelib_demo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import tube42.lib.imagelib.*;

/**
 * ImageUtil example files:
 * This class contain the demo cavnas.
 */

public class ImageCanvas
extends Canvas
{    
    
    private MIDlet parent;
    private Scene [] scenes;
    private int curr;    
    
    public ImageCanvas(MIDlet parent)
    {
        setFullScreenMode(true);
        
        try {
            this.parent = parent;
            
            this.curr = 0;
            this.scenes = new Scene[] {                      
                new SceneIntro(),
                      // library showcase
                      new SceneBlend(),
                      new SceneResize(),                      
                      new SceneCrop(),                      
                      new SceneHistogram(),
                      new SceneHistogramEqualization(),                
                      new SceneColorWarmth(),
                      new SceneColorCold(),                      
                      new SceneColorCurves(),
                      new SceneColorThreshold(),
                      new SceneQuantisation(),
                      new SceneFilter(),
                      new SceneGrayscale(),
                      new SceneSepia(), 
                      new SceneRetro(),
                      
                      // phone performance tests
                      new ScenePerfTransparent(),
                      new ScenePerfGPU(),
                      
                      // -= THE END =-
                      new SceneLast()
                  };
        } catch(Exception exx) {
            exx.printStackTrace();
        }
        
        scenes[curr].setFocus(true);
    }
    
    // ---------------------------------------
    // key input
    public void keyPressed(int k)
    {
        handle_key( k, true, false);
    }
    
    public void keyRepeated(int k)
    {
        handle_key( k, true, true);
    }
        
    public void keyReleased(int k)
    {
        handle_key(k, false, false);      
    }
    
    // ---------------------------------------
    // touch input

    public void pointerDragged(int x, int y)
    {
        handle_touch(x, y, true, true);
    }
    
    public void pointerPressed(int x, int y)
    {
        handle_touch(x, y, true, true);
    }
    
    public void pointerReleased(int x, int y)
    {
        handle_touch(x, y, false, false);
    }
    
    // ---------------------------------------
    private void advance()
    {
        curr ++;
        if(curr >= scenes.length) {
            curr = 0;
            parent.notifyDestroyed();        
        }
        
        if(curr > 0)
            scenes[curr-1].setFocus(false);
        if(curr < scenes.length)
            scenes[curr].setFocus(true);
        
        repaint();        
    }
    
    private void handle_touch(int x, int y, boolean down, boolean draged)
    {
        if(!down) advance();
    }
    private void handle_key(int key, boolean down, boolean repeated)
    {
        int gkey = getGameAction(key);
        
        if(!down && gkey == FIRE) {
            advance();
        } else if( scenes[curr].handle_key(key, gkey, down, repeated)) {
            repaint();    
        }
    }

    
    // ---------------------------------------
    public void paint(Graphics g)
    {
        
        int w = getWidth();
        int h = getHeight();                
        
        // Clear screen
        g.setColor(0x000000);
        g.fillRect(0, 0, w, h);
        
        scenes[curr].paint(g, w, h);         
        
        g.setColor(0xFF0000);
        g.drawString( scenes[curr].getName(), 5, 5, 0);
        
    }
    
}
