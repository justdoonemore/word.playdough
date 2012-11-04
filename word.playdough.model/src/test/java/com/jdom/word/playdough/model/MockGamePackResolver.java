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
import java.util.HashSet;
import java.util.Set;

import com.jdom.word.playdough.model.FileGamePackResolver;

public class MockGamePackResolver extends FileGamePackResolver {

	private static final File TEST_RESOURCES_DIR = new File(
			"src/test/resources");

	private final Set<String> unlockedGamePacks = new HashSet<String>();
	{
		unlockedGamePacks.add(GamePackListModelTest.UNLOCKED_GAME_PACK_NAME);
	}

	public MockGamePackResolver(File unlockedPacksDirectory) {
		super(TEST_RESOURCES_DIR, unlockedPacksDirectory);
	}

	@Override
	public boolean isGamePackLocked(String username, String gamePackFileName) {
		return !unlockedGamePacks.contains(gamePackFileName);
	}

	@Override
	public boolean unlockGamePack(String username, String gamePackFileName) {
		unlockedGamePacks.add(gamePackFileName);
		return false;
	}

}
