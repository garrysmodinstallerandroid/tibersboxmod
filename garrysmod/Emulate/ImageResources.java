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
/** 
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */
package net.sourceforge.jspcemulator.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
/**
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */

public interface ImageResources extends ClientBundle {

	ImageResources INSTANCE = GWT.create(ImageResources.class);
	
	@Source("freedos-logo3.png")
	DataResource FreeDosImage();
	@Source("sanos.gif")
	DataResource SanosImage();
}
