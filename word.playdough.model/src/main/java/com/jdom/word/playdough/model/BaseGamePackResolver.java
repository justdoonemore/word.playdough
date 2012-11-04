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
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

public abstract class BaseGamePackResolver implements GamePackResolver {
	private final File gamePacksDirectory;

	public BaseGamePackResolver(File gamePacksDirectory) {
		this.gamePacksDirectory = gamePacksDirectory;
	}

	public String getGamePackContents(String gamePackFileName) {
		try {
			return FileUtils.readFileToString(new File(gamePacksDirectory,
					gamePackFileName));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public SortedSet<String> getGamePackNames() {
		SortedSet<String> names = new TreeSet<String>();
		for (File file : gamePacksDirectory.listFiles()) {
			if (file.getName().startsWith(GAME_PACK_PREFIX)) {
				names.add(file.getName());
			}
		}

		return names;
	}

	protected boolean isFilenameAcceptable(String gamePackFileName) {
		return gamePackFileName.startsWith(LOCKED_GAME_PACK_PREFIX);
	}
}
