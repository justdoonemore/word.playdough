/** 
 *  Copyright (C) 2012  Just Do One More
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jdom.word.playdough.presenter;

public class ButtonConfiguration {

	private final String displayText;
	private final int index;
	private final boolean enabled;
	private final Runnable clickAction;

	public ButtonConfiguration(String displayText, int index, boolean enabled,
			Runnable clickAction) {
		this.displayText = displayText;
		this.index = index;
		this.enabled = enabled;
		this.clickAction = clickAction;
	}

	public String getDisplayText() {
		return displayText;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getIndex() {
		return index;
	}

	public Runnable getClickAction() {
		return clickAction;
	}

}
