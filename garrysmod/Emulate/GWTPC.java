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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import gwt.g2d.client.graphics.ImageLoader;
import gwt.g2d.client.graphics.ImageLoader.CallBack;

import net.sourceforge.jspcemulator.client.emulator.PC;
import net.sourceforge.jspcemulator.client.gui.CodeSplitter;
import net.sourceforge.jspcemulator.client.gui.Debugger;
import net.sourceforge.jspcemulator.client.gui.Output;
import net.sourceforge.jspcemulator.client.gui.RectangleBounds;
import net.sourceforge.jspcemulator.client.j2se.*;
import net.sourceforge.jspcemulator.client.support.DriveSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
/**
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */

public class GWTPC implements EntryPoint {
	Screen d=null;
	private static Timer loadTimer = null;
	private static Timer runTimer = null;
	private PCMonitor monitor = null;
	private PC pc = null;
	private static boolean locked=false;
	
	public static final String DOS_OS="dos";
	public static final String SANOS_OS="sanos";
	private static String os=DOS_OS;
	private HashMap<String,ImageElement> pictures = new HashMap<String,ImageElement>();

	private RectangleBounds freedosBounds;
	private RectangleBounds sanosBounds;
	
private static Logger rootLogger = Logger.getLogger("");

	
	public void onModuleLoad() {		
	    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
	        public void onUncaughtException(Throwable e) {
    			if(loadTimer!=null){
    				loadTimer.cancel();
    			}
    			if(runTimer!=null){
    				runTimer.cancel();
    			}
    			Throwable f = e.getCause();
    			String extraDetail = "";
    			if(f!=null){
    				extraDetail+=f.getMessage() +"\n"+f.toString();
    			}
	          RootPanel.get().add(new Label("Unexpected error occurred.  Not able to recover.  ErrorMessage: " + e.getMessage()+" details"+extraDetail));
	        }
	      });
	    
	    Output.setPC(this);
	    VerticalPanel vp = new VerticalPanel();
		vp.getElement().getStyle().setProperty("width","100%");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().getStyle().setProperty("width","100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		d = new Screen(hp);
	    d.setPC(this);
				
			
		HTML title = new HTML("<H2>PC emulator in JavaScript</H2>",true);// h1=null;
		title.getElement().getStyle().setProperty("color","#FFFFFF");		
		vp.add(title);

		RootPanel.get().add(vp);
		RootPanel.get().add(hp);

	    VerticalPanel vp2 = new VerticalPanel();
		vp2.getElement().getStyle().setProperty("width","100%");
		vp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HTML info = new HTML("<br /> Some keys mapped to Keypad: Arrow keys & 5=ctrl, 0=alt, 7/9=ctrl. Not compatible with numerous browsers - If it doesn't seem to be working, yours is one of them.<br />");
		info.getElement().getStyle().setProperty("color","#FFFFFF");		
		vp2.add(info);
		HTML links = new HTML("<br /> This <a href='http://sourceforge.net/projects/jspcemulator'>project</a> (GPL) would not of been possible without the brilliance of <a href='http://jpc.sourceforge.net/home_home.html'>JPC</a> (GPL), <a href='http://sourceforge.net/projects/freedos/'>FreeDOS</a> (GPL) and <a href='http://www.jbox.dk/sanos/'>SANOS</a> (<a href='http://www.jbox.dk/sanos/copying.txt'>BSD style</a>).");
		links.getElement().getStyle().setProperty("color","#FFFFFF");		
		vp2.add(links);
		RootPanel.get().add(vp2);

		Element body = RootPanel.getBodyElement();
		body.getStyle().setProperty("backgroundColor","#496E98");//"#34374D");//#444865");//black");

		loadImages();
		loadingLoop();
	}
	boolean drawnIcons=false;
	boolean drawIconsAttempt=false;
	boolean waitingForChoice=true;
	private void chooseOSDisplay()
	{
		if(pictures.size()==2){
			drawIconsAttempt=true;
			ImageElement freedos = pictures.get("FreeDosImage");
			ImageElement sanos = pictures.get("SanosImage");
			int width= d.getWidth();
			freedosBounds = new RectangleBounds(centreCoordinates(width,freedos.getWidth()),50,freedos.getWidth(),freedos.getHeight());
			sanosBounds = new RectangleBounds(centreCoordinates(width,sanos.getWidth()),200,sanos.getWidth(),sanos.getHeight());
			d.selectScreen("Select Operating System");
			d.drawImage(freedos, freedosBounds.getMinX(), freedosBounds.getMinY());
			d.drawImage(sanos, sanosBounds.getMinX(), sanosBounds.getMinY());
			drawnIcons=true;
		}
	}
	public int centreCoordinates(int size,int width)
	{
		int midS = size/2;
		int midW = width/2;
		return midS-midW;
	}
	public void clicked(int x,int y)
	{
		if(drawnIcons && waitingForChoice){
			if(freedosBounds.isInside(x, y)){
				os=DOS_OS;
				waitingForChoice=false;
				d.loadScreen(CodeSplitter.getMessage());
			}else if(sanosBounds.isInside(x, y)){
				os=SANOS_OS;
				waitingForChoice=false;
				d.loadScreen(CodeSplitter.getMessage());
			}
		}
	}

	
	private void loadImages()
	{
		DataResource dosDR = net.sourceforge.jspcemulator.client.gui.ImageResources.INSTANCE.FreeDosImage();        		
		DataResource sanosDR = net.sourceforge.jspcemulator.client.gui.ImageResources.INSTANCE.SanosImage();        		
		loadPicture("FreeDosImage",dosDR);
		loadPicture("SanosImage",sanosDR);
	}
	private void loadPicture(final String filename, DataResource im)
	{
		String url = im.getUrl();
		ImageLoader.loadImages(url, new CallBack() {
			@Override
			public void onImagesLoaded(ImageElement[] imageElements) {
				ImageElement image =imageElements[0];
				pictures.put(filename,image);
			}
		});
	}

	/////
	public void output(String message)
	{
        RootPanel.get().add(new Label(message));		
	}

	private void loadingLoop()
	{
    	final ArrayList<DriveSet> drives = new ArrayList<DriveSet>();

		loadTimer = new Timer(){
			public void run(){
				try{
					if(!drawnIcons){
						if(!drawIconsAttempt){
							chooseOSDisplay();
						}
					}else if(waitingForChoice){
						
					}else{
						d.loadScreen(CodeSplitter.getMessage());
						if(!locked){
							locked=true;
							if(CodeSplitter.getPercentage()<CodeSplitter.LOAD_PC){
								GWT.runAsync(new RunAsyncCallback(){
									public void	onFailure(java.lang.Throwable reason) {
										CodeSplitter.setError(reason.getMessage());
									}
									public void	onSuccess(){
								        CodeSplitter.setPercentage(CodeSplitter.STARTING);
								        pc = new PC(new VirtualClock(),os);
										//Output.add("setPC");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_PC);
								        pc.setMemory();	
										//Output.add("setMemory");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_MEMORY);
								        locked=false;
									}
								});
							}else if(CodeSplitter.getPercentage()<CodeSplitter.LOAD_DRIVE){
								
								GWT.runAsync(new RunAsyncCallback(){
									public void	onFailure(java.lang.Throwable reason) {
										CodeSplitter.setError(reason.getMessage());
									}
									public void	onSuccess(){
										String[] args=null;
										if(os.compareTo(DOS_OS)==0){
											args=new String[] {"-fda","mem:./resources/images/"+os+"floppy.img",
													"-hda","mem:./hdddisk.img","-boot","fda"};
										}else{
											args=new String[] {"-fda","mem:./resources/images/"+os+"floppy.img","-boot","fda"};
										}
								        drives.add(DriveSet.buildFromArgs(args));
										pc.setDrives(drives.get(0));
										//Output.add("setDrives");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_DRIVE);
							        	locked=false;
									}
								});
							}else if(CodeSplitter.getPercentage()<CodeSplitter.CONFIRM_DRIVE){
								DriveSet allDrives = drives.get(0);
					        	if(allDrives!=null && allDrives.areDrivesloaded()){
							        CodeSplitter.setPercentage(CodeSplitter.CONFIRM_DRIVE);
						        }
					        	locked=false;
							}else if(CodeSplitter.getPercentage()<CodeSplitter.LOAD_PERIPHERALS){
								GWT.runAsync(new RunAsyncCallback(){
									public void	onFailure(java.lang.Throwable reason) {
										CodeSplitter.setError(reason.getMessage());
									}
									public void	onSuccess(){
										pc.setPeripherals();							
										//Output.add("setPeripherals");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_PERIPHERALS);
									    pc.setBIOS();
										//Output.add("setBIOS");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_BIOS);
									    pc.configurePC();
										//Output.add("configurePC");
								        CodeSplitter.setPercentage(CodeSplitter.CONFIGURE_PC);
										d.loadScreen(CodeSplitter.getMessage());
								        monitor = new PCMonitor(pc);
								        d.setMonitor(monitor);
										//Output.add("setMonitor");
								        CodeSplitter.setPercentage(CodeSplitter.LOAD_MONITOR);
										d.loadScreen(CodeSplitter.getMessage());
								        pc.start();
										//Output.add("start");
								        CodeSplitter.setPercentage(CodeSplitter.START_PC);
										d.loadScreen(CodeSplitter.getMessage());
					    				loadTimer.cancel();
					    				loadTimer = null;
								        locked=false;
					    				mainLoop();
									}
								});
							}
						}
					}
				}catch(Exception e){
					Debugger.add("exception message="+e.toString());
				}
			}
		};
		loadTimer.scheduleRepeating(1000*4);
	}
	private void loadingLoopOrig()
	{
		try{
		        pc = new PC(new VirtualClock(),os);
		        pc.setMemory();	
		        String[] args=new String[] {"-fda","mem:./resources/images/"+os+"floppy.img","-boot","fda"};
				pc.setDrives(DriveSet.buildFromArgs(args));
				pc.setPeripherals();							
			    pc.setBIOS();
		        CodeSplitter.setPercentage(CodeSplitter.LOAD_BIOS);
			    pc.configurePC();
		        CodeSplitter.setPercentage(CodeSplitter.CONFIGURE_PC);
				//Output.add("PCMONITOR CREATE");
		        monitor = new PCMonitor(pc);
				//Output.add("PCMONITOR CREATED");
		        d.setMonitor(monitor);
		        CodeSplitter.setPercentage(CodeSplitter.LOAD_MONITOR);
				//Output.add("STARTING");
		        pc.start();
		        CodeSplitter.setPercentage(CodeSplitter.START_PC);
				d.loadScreen(CodeSplitter.getMessage());
				mainLoop();
		}catch(Exception e){
	          RootPanel.get().add(new Label("loadingLoop.  ErrorMessage: " + e.getMessage()));
			//Debugger.add("exception message="+e.toString());
		}
	}

	
	private void mainLoop()
	{
		final int[] textOffset = new int[1];
		textOffset[0]=0;
		monitor.setScreen(d);
		runTimer = new Timer(){
			public void run(){
				try{
					if(!monitor.isDirect()){
						int newOffset = monitor.getTextOffset();
						if(newOffset!=textOffset[0] || monitor.isNewGraphicsScreen()){
							long previousTime=System.currentTimeMillis();	
							textOffset[0] = newOffset;
					        int[] pixels=monitor.paint();
							d.update(pixels,monitor.getWidth(),monitor.getHeight());
							long duration =System.currentTimeMillis()-previousTime;
							Debugger.add("render duration="+duration);						
							d.debugDraw();
						}						
					}					 
					pc.execute();
				}catch(Exception e){
					Throwable f = e.getCause();
					String extraDetail = "";
					if(f!=null){
						extraDetail+=f.getMessage() +"\n"+f.toString();
					}
			          RootPanel.get().add(new Label("ErrorMessage: " + e.getMessage()+ " "+ extraDetail));
			          if(runTimer!=null){
			        	  runTimer.cancel();
			          }
			          rootLogger.log(Level.SEVERE, "exception:", e);
				}
			}
		};
		runTimer.scheduleRepeating(1000/25);//30
	}

}
