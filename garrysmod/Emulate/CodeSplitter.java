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
package net.sourceforge.jspcemulator.client.gui;
/** 
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */

public class CodeSplitter {

    private static int percentage=0;
    public static int STARTING=5;
    public static int LOAD_PC=10;
    public static int LOAD_MEMORY=20;
    public static int LOAD_DRIVE=30;
    public static int CONFIRM_DRIVE=35;
    public static int LOAD_PERIPHERALS=40;
    public static int LOAD_BIOS=50;
    public static int CONFIGURE_PC=60;
    public static int LOAD_MONITOR=70;
    public static int START_PC=80;
        
    private static int counter=0;
    private static String errorMessage = null;
    public static void setError(String error)
    {
    	errorMessage=error;
    }
    public static String getMessage()
    {
    	String message =null;
    	if(errorMessage!=null){
    		message = errorMessage;
    	}else{
    		String dots="";
    		for(int i=0;i<counter && i<60;i++){
    			dots=dots+".";
    		}
    		//message = " Loading: "+percentage+"%"+dots;
    		message = " Loading  "+dots;
    	}
    	counter = counter+1;
    	return message;
    }
    public static void setPercentage(int nextLevel){
    	percentage=nextLevel;
    	//counter=0;
    }
    public static int getPercentage()
    {
    	return percentage;
    }
}
