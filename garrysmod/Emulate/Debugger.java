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

public class Debugger {

    private static String[] info=new String[] {"","","","","","","","","","","","","","","","","","","",""};
    private static int count=0;
    private static int limit=20;
    private static long start = System.currentTimeMillis();
    private static String duration()
    {
        long end = System.currentTimeMillis();
        long duration = end-start;
        long seconds = duration/1000;
        long minutes = seconds/60;
        long remaining = seconds%60;
        return " T+"+minutes+"m" +remaining+"s";
    }
    public static void add(String message){
        message=message+duration();
        //System.out.println(message);
        if(count<limit){
            info[count++]=message;
        }else{
            cycle(message);
        }
    }
    private static void cycle(String message)
    {
        for(int i=1;i<limit;i++){
            info[i-1]=info[i];
        }
        info[limit-1]=message;
    }
    public static int getSize()
    {
        return count;
    }
    public static String getMessage(int index)
    {
        return info[index];
    }
}
