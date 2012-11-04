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

public class ListItemConfiguration {

	private final String gamePack;
	private final boolean gamePackLocked;
	private final Runnable clickAction;

	public ListItemConfiguration(String gamePack, boolean gamePackLocked,
			Runnable clickAction) {
		this.gamePack = gamePack;
		this.gamePackLocked = gamePackLocked;
		this.clickAction = clickAction;
	}

	public String getGamePack() {
		return gamePack;
	}

	public boolean isGamePackLocked() {
		return gamePackLocked;
	}

	public Runnable getClickAction() {
		return clickAction;
	}
}
