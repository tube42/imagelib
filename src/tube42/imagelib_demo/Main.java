/*
 * This file is a part of the TUBE42 imagelib, released under the LGPL license.
 *
 * Development page: https://github.com/tube42/imagelib
 * License:          http://www.gnu.org/copyleft/lesser.html
 */

package tube42.imagelib_demo;


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * ImageUtil example files:
 * This class loads the demo MIDlet and creates the demo Canvas
 */
public class Main extends MIDlet 
{        
    private ImageCanvas canvas;
    
    public Main() 
    {
        this.canvas = new ImageCanvas(this);   
    }    
    
    public void startApp() 
    {
        Display.getDisplay(this).setCurrent(canvas);
    }
    
    public void pauseApp() 
    {
        // EMPTY
    }
    
    public void destroyApp(boolean unc) 
    {
        // EMPTY
    }              
}
