/*
JsPCEmulator

Copyright (C) Kevin O'Dwyer

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

Project homepage: http://sourceforge.net/projects/jspcemulator
*/	
package net.sourceforge.jspcemulator.client;
 	
import java.util.HashMap;

import net.sourceforge.jspcemulator.client.gui.Debugger;
import net.sourceforge.jspcemulator.client.gui.Output;
import net.sourceforge.jspcemulator.client.j2se.KeyEvent;
import net.sourceforge.jspcemulator.client.j2se.PCMonitor;

import gwt.g2d.client.graphics.Color;
import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.TextAlign;
import gwt.g2d.client.graphics.TextBaseline;
//import gwt.g2d.client.graphics.canvas.CanvasPixelArray;
import gwt.g2d.client.graphics.canvas.CanvasPixelArray;
import gwt.g2d.client.graphics.canvas.Context;
import gwt.g2d.client.graphics.canvas.ImageDataAdapter;
//import gwt.g2d.client.graphics.canvas.ImageDataAdapter;
//import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Panel;
/**
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */

public class Screen implements KeyPressHandler, KeyUpHandler, MouseDownHandler
{ 	
	private Surface surface;
	private ImageDataAdapter imageData;
	private CanvasPixelArray pixels;
	private PCMonitor monitor = null;
	private GWTPC pc = null;
	private boolean isDebugMode=false;
	private int rightMouseDownCounter=0;
    private final Color infoColour = KnownColor.LIGHT_GREEN;
    private final int fontHeight=18;
    private final String textStyle=""+fontHeight+"px sans-serif"; 
	private boolean renderLines=true;
    private Panel parent = null;
    private int WIDTH = 720;
    private int HEIGHT = 480;
	public Screen(Panel parent) { 	
		this.parent = parent;
        if(GWT.isProdMode()){
        	renderLines=false;
        }
        resize(WIDTH,HEIGHT);
	} 
	public void resize(int width, int height)
	{
		if(surface!=null){
			surface.removeFromParent();
		}
		surface = new Surface(width, height);
		parent.clear();
		parent.add(surface);
		parent.setVisible(true);
		surface.addKeyPressHandler(this);
		surface.addKeyUpHandler(this);
		surface.addMouseDownHandler(this);
		surface.setFocus(true);
		imageData = surface.createImageData(width,height);
		pixels = imageData.getPixelData();
		clear();
	    WIDTH = width;
	    HEIGHT = height;
	}
	public int getWidth()
	{
		return WIDTH;
	}
	public void renderModePixels()
	{
    	renderLines=false;		
	}
	public void setPC(GWTPC pc)
	{
			this.pc=pc;
	}
	public void setMonitor(PCMonitor monitor)
	{
			this.monitor=monitor;
	}
	/*public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}*/
	@Override
	public void onKeyPress(KeyPressEvent ev)
	{
		//Output.add("src="+ev.getSource());
		if(monitor!=null){
			int  val = ev.getNativeEvent().getKeyCode();//.getNativeKeyCode();
			//Output.add("keypress="+val);

			if(ev.getNativeEvent().getShiftKey()){
				monitor.keyPressed(16);
			}else if(ev.getNativeEvent().getAltKey()){
				monitor.keyPressed(18);
			}else if(ev.getNativeEvent().getCtrlKey()){
				monitor.keyPressed(17);
			}else{
				//remap keypad arrows to arrows
				if(val == KeyEvent.VK_4){//left
					//Output.add("left keypress="+val);
					monitor.keyPressed(KeyEvent.VK_LEFT);				
					monitor.keyPressed(KeyEvent.VK_LEFT);				
					monitor.keyPressed(KeyEvent.VK_LEFT);		
					monitor.keyPressed(KeyEvent.VK_LEFT);							
					//NativeEvent keyDownEvent=Document.get().createKeyDownEvent(false, false, false,false,KeyEvent.VK_LEFT);
					//DomEvent.fireNativeEvent(keyDownEvent, parent);
					//keyDownEvent=Document.get().createKeyDownEvent(false, false, false,false,KeyEvent.VK_LEFT);
					//DomEvent.fireNativeEvent(keyDownEvent, parent);
				}else if(val == KeyEvent.VK_6){//right
					//Output.add("right keypress="+val);
					monitor.keyPressed(KeyEvent.VK_RIGHT);				
					monitor.keyPressed(KeyEvent.VK_RIGHT);				
					monitor.keyPressed(KeyEvent.VK_RIGHT);				
					monitor.keyPressed(KeyEvent.VK_RIGHT);				
					//NativeEvent keyDownEvent=Document.get().createKeyDownEvent(false, false, false,false,KeyEvent.VK_RIGHT);
					//DomEvent.fireNativeEvent(keyDownEvent, surface);
					//keyDownEvent=Document.get().createKeyDownEvent(false, false, false,false,KeyEvent.VK_RIGHT);
					//DomEvent.fireNativeEvent(keyDownEvent, surface);
				}else if(val == KeyEvent.VK_8){//up
					monitor.keyPressed(KeyEvent.VK_UP);				
					monitor.keyPressed(KeyEvent.VK_UP);				
					monitor.keyPressed(KeyEvent.VK_UP);				
					monitor.keyPressed(KeyEvent.VK_UP);				
				}else if(val == KeyEvent.VK_2){//down
					monitor.keyPressed(KeyEvent.VK_DOWN);				
					monitor.keyPressed(KeyEvent.VK_DOWN);				
					monitor.keyPressed(KeyEvent.VK_DOWN);				
					monitor.keyPressed(KeyEvent.VK_DOWN);				
				}else if(val == KeyEvent.VK_7){//mapped to l shift
					monitor.keyPressed(16);		
				}else if(val == KeyEvent.VK_9){//mapped to r shift
					monitor.keyPressed(KeyEvent.VK_RIGHT_SHIFT);		
				}else if(val == KeyEvent.VK_5){//mapped to ctrl
					monitor.keyPressed(17);				
				}else if(val == KeyEvent.VK_0){//mapped to alt
					monitor.keyPressed(18);		
				}else if (val == KeyEvent.VK_ENTER){//enter (captured as a repeating sequence of key press)
					monitor.keyPressed(KeyEvent.VK_ENTER);
				}else if (val == KeyEvent.VK_SPACE){//enter (captured as a repeating sequence of key press)
					monitor.keyPressed(KeyEvent.VK_SPACE);
				//}else{ this causes problems -- double key presses
				//	if(val>0){
						//Output.add("keypress="+val);
				//		monitor.keyPressed(val);
				//	}
				}
			}
		}
		ev.stopPropagation();
	}
	
	private static int OddKeyCodeLeftUp = 100;
	private static int OddKeyCodeRightUp = 102;
	private static int OddKeyCodeUpUp = 104;
	private static int OddKeyCodeDownUp = 98;

	private static int OddKeyCodeZeroUp = 96;
	private static int OddKeyCodeSevenUp = 103;
	private static int OddKeyCodeNineUp = 105;
	private static int OddKeyCodeFiveUp = 101;
	@Override
	public void onKeyUp(KeyUpEvent ev) {
		int  val = ev.getNativeKeyCode();
		//Output.add("keyup="+val);
		if(monitor!=null){
			//remap keypad arrows to arrows
			if(val == OddKeyCodeLeftUp){
				actualKeyPress(KeyEvent.VK_LEFT);
			}else if(val == OddKeyCodeDownUp){
				actualKeyPress(KeyEvent.VK_DOWN);
			}else if(val == OddKeyCodeRightUp){
				actualKeyPress(KeyEvent.VK_RIGHT);
			}else if(val == OddKeyCodeUpUp){
				actualKeyPress(KeyEvent.VK_UP);
			}else if(val == OddKeyCodeFiveUp){
				monitor.keyReleased(17);//ctrl
			}else if(val == OddKeyCodeZeroUp){
				monitor.keyReleased(18); //alt
			}else if(val == OddKeyCodeSevenUp){
				monitor.keyReleased(16);//shift l
			}else if(val == OddKeyCodeNineUp){
				monitor.keyReleased(KeyEvent.VK_RIGHT_SHIFT);//shift r
			}else if (val == KeyEvent.VK_ENTER){
				monitor.keyReleased(KeyEvent.VK_ENTER);
			}else if (val == KeyEvent.VK_SPACE){
				monitor.keyReleased(KeyEvent.VK_SPACE);
			}else{
				actualKeyPress(val);
			}
		}	
		ev.stopPropagation();

	}
	private void actualKeyPress(int val)
	{
		monitor.keyPressed(val);
		monitor.keyReleased(val);		
	}
	public void update(final int[] buffer,int width,int height)
	{
		if(renderLines){
			renderLines(buffer,width,height);
		}else{
			renderSimple(buffer,width,height);
		}
	}
	public void drawImage(ImageElement image,int x,int y)
	{
		surface.drawImage(image, x, y);
	}
	public void clear()
	{
		surface.clear().fillBackground(KnownColor.BLACK);				
	}
	private void renderLines(final int[] buffer,int width,int height)
	{
		surface.clear().fillBackground(KnownColor.BLACK);	
		
		int lines=0;
			int k=0;
			int previousCol;
			int startx;
			final int widthminus1 = width-1;
			for(int y = 0; y< height;y++){
				previousCol=0;
				startx=0;
				int col;
				for(int x=0;x<widthminus1;x++){
					col = buffer[k++];
					if(col!=previousCol){
						if(previousCol!=0){
							drawLine(convertCol(previousCol),y,startx,x-1);
							lines++;
						}
						if(col!=0){
							startx=x;
						}
					}
					previousCol=col;		
				}
				col = buffer[k++];
				if(col!=0){
					drawLine(convertCol(col),y,startx,width);
					lines++;					
				}
			}
	}
	private final String calcColourNoAlphaAllowed(int col)
	{
		StringBuilder stringBuilder = new StringBuilder("#000000");
		String hexString = Integer.toHexString(col);
		String colorCode = stringBuilder.replace(
				stringBuilder.length() - hexString.length(), 
				stringBuilder.length(), 
				hexString).toString();
		
		return colorCode;
	}
	HashMap<String,String> colours=new HashMap<String,String>();
	private final String convertCol(final int c)
	{		
		String sCol=""+c;
		if(!colours.containsKey(""+c)){
			String calc=calcColourNoAlphaAllowed(c);
			colours.put(sCol,calc);
			return calc;
		}else{
			return colours.get(sCol);
		}
	}
	private void drawLine(final String col,final int y,final int x,final int x2)
	{		
		Context context = surface.getContext();
		context.setStrokeStyle(col);
		context.beginPath();
		context.moveTo(x,y);
		context.lineTo(x2,y);
		context.stroke();
		context.closePath();		
	}

    public void debugDraw()
    {
        if(isDebugMode){
            final int size = Debugger.getSize();
            int offset = HEIGHT-(fontHeight*(size+1));
            for(int i=0;i<size;i++){
                String info = Debugger.getMessage(i);
                drawText(surface,infoColour,textStyle,info, 0, offset+(i*fontHeight));
            }
        }
    }
    public void selectScreen(String message)
    {
		surface.clear().fillBackground(KnownColor.BLACK);		
		drawCenteredText(surface,infoColour,textStyle,message, WIDTH/2, 10);    	
    }
	public static void drawCenteredText(Surface surface,Color color,String style,String text,int x,int y)
	{
		surface.setFillStyle(color)
		.setFont(style) 
		.setTextBaseline(TextBaseline.TOP) 
		.setTextAlign(TextAlign.CENTER)
		.fillText(text, x, y); 		

	}

    public void loadScreen(String message)
    {
		surface.clear().fillBackground(KnownColor.BLACK);		
        drawText(surface,infoColour,textStyle,message, WIDTH/5, HEIGHT/2);    	
    }
	private static void drawText(final Surface surface,final Color color,final String style,final String text,final int x,final int y)
	{
		surface.setFillStyle(color)
		.setFont(style) 
		.setTextBaseline(TextBaseline.TOP) 
		.setTextAlign(TextAlign.LEFT)
		.fillText(text, x, y); 		
	}
	@Override
	public void onMouseDown(MouseDownEvent ev) {
		int btn = ev.getNativeButton();
		if(btn == com.google.gwt.dom.client.NativeEvent.BUTTON_RIGHT){
			rightMouseDownCounter++;
			if(rightMouseDownCounter%2==0){
				isDebugMode=true;
				rightMouseDownCounter=0;
			}else{
				isDebugMode=false;
			}
		}else{
			if(btn == com.google.gwt.dom.client.NativeEvent.BUTTON_LEFT){
				int x = ev.getClientX() - surface.getAbsoluteLeft();
				int y = ev.getClientY() - surface.getAbsoluteTop();
				pc.clicked(x,y);
			}
		}
	}
	public void renderSimple(final int[] buffer,int width,int height)
	{
		//Debugger.add("simple");
		final int size= width * height;
		int k=0;
		for (int i = 0; i < size;) {  
			int col = buffer[i++];
			pixels.setData(k++,(0xFF & (col>>16)));
			pixels.setData(k++,(0xFF & (col>>8)));
			pixels.setData(k++,(0xFF & col));		
			pixels.setData(k++,255);
		}
		surface.putImageData(imageData,0,0); 		
		
	}
	public void updateDirect(final int[] buffer,int width,int height)
	{
		if(renderLines){
			renderLines(buffer,width,height);
		}else{
			renderSimple(buffer,width,height);
		}
	}
	
} 	

