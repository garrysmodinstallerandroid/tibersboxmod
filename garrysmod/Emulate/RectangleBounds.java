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

public class RectangleBounds {
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;
        public RectangleBounds(int minX,int minY,int width,int height)
        {
            this.minX=minX;
            this.minY=minY;
            this.maxX=minX+width;
            this.maxY=minY+height;
        }
        public boolean isInside(int x,int y)
        {
            boolean isInside=false;
            if(x>=minX && x<= maxX && y>=minY && y <= maxY){
                isInside=true;
            }
            return isInside;
        }
        public int getMinX()
        {
            return this.minX;
        }
        public int getMinY()
        {
            return this.minY;
        }
}
