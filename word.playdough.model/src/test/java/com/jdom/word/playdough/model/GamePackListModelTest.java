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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jdom.word.playdough.model.GamePackListModel;
import com.jdom.word.playdough.model.GamePackPointsTracker;
import com.jdom.word.playdough.model.ModelFactory;

@RunWith(PowerMockRunner.class)
public class GamePackListModelTest {

	private static final String UNLOCKABLE_GAME_PACK_NAME = "game_pack_unlockable1.properties";
	static final String UNLOCKED_GAME_PACK_NAME = "game_pack_default.properties";
	private final File testDir = GamePackListModelTest
			.setupTestClassDir(getClass());
	private final GamePackPointsTracker mockPointsTracker = Mockito
			.mock(GamePackPointsTracker.class);

	private final ModelFactory modelFactory = new ModelFactory();

	private final GamePackListModel model = new GamePackListModel("testUser",
			modelFactory, new MockGamePackResolver(testDir));

	@Test
	public void testListGamePacksDelegatesToResolver() {
		SortedSet<String> gamePacks = model.getAvailableGamePackFiles();
		assertEquals(2, gamePacks.size());
		assertTrue("Did not find the default game pack!",
				gamePacks.contains(UNLOCKED_GAME_PACK_NAME));
		assertTrue("Did not find the unlockable game pack!",
				gamePacks.contains(UNLOCKABLE_GAME_PACK_NAME));
	}

	/**
	 * Setup the directory the test class should use.
	 * 
	 * @param testClass
	 *            The test class to create a directory for
	 * @return The directory created for the test class
	 */
	public static File setupTestClassDir(Class<?> testClass) {
		File dir = new File(System.getProperty("java.io.tmpdir"),
				testClass.getSimpleName());

		// Delete any preexisting version
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		// Make the directory
		dir.mkdirs();

		return dir;
	}

	@Test
	public void testCheckWhetherGamePackIsLockedDelegatesToResolver() {
		assertFalse(
				"The model doesn't appear to have correctly delegated to the resolver!",
				model.isGamePackLocked(UNLOCKED_GAME_PACK_NAME));
		assertTrue(
				"The model doesn't appear to have correctly delegated to the resolver!",
				model.isGamePackLocked(UNLOCKABLE_GAME_PACK_NAME));
	}

	@Test
	public void testStartGamePackAlertsGamePackPlayer() {
		MockGamePackPlayerModel mockModel = new MockGamePackPlayerModel();
		modelFactory.setGamePackPlayerModel(mockModel);

		assertFalse(mockModel.setJumbleCalled);
		model.playGamePack(UNLOCKED_GAME_PACK_NAME);
		assertTrue(mockModel.setJumbleCalled);
	}
}
