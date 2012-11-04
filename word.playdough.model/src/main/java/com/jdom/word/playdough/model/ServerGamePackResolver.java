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
package com.jdom.word.playdough.model;

import java.io.File;

public class ServerGamePackResolver extends BaseGamePackResolver {

	private final ServerCommunicationManager communicationManager;

	public ServerGamePackResolver(File gamePacksDirectory,
			ServerCommunicationManager communicationManager) {
		super(gamePacksDirectory);
		this.communicationManager = communicationManager;
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		return isFilenameAcceptable(gamePackFileName)
				&& getServerCommunicationManager().isGamePackLocked(username,
						gamePackFileName);
	}

	public boolean unlockGamePack(String username, String gamePackFileName) {
		return getServerCommunicationManager().sendLockStatus(username,
				gamePackFileName, false);
	}

	protected ServerCommunicationManager getServerCommunicationManager() {
		return communicationManager;
	}

	@Override
	protected boolean isFilenameAcceptable(String gamePackFileName) {
		return gamePackFileName.startsWith(LOCKED_GAME_PACK_PREFIX);
	}

}
