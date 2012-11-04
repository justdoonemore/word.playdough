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
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileGamePackResolver extends BaseGamePackResolver {

	private final File unlockedPacksDir;

	public FileGamePackResolver(File gamePacksDirectory,
			File unlockedPacksDirectory) {
		super(gamePacksDirectory);
		this.unlockedPacksDir = unlockedPacksDirectory;
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		return isFilenameAcceptable(gamePackFileName)
				&& !getUserPackUnlockFile(username, gamePackFileName).exists();
	}

	public boolean unlockGamePack(String username, String gamePackFileName) {
		try {
			FileUtils.write(getUserPackUnlockFile(username, gamePackFileName),
					"unlocked");
			return false;
		} catch (IOException e) {
			throw new IllegalStateException(gamePackFileName, e);
		}
	}

	private File getUserPackUnlockFile(String username, String gamePackFileName) {
		return new File(unlockedPacksDir, username + "_" + gamePackFileName);
	}

}