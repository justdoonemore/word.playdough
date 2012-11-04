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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.GamePackPlayerSerializer;
import com.jdom.word.playdough.model.Jumble;
import com.jdom.word.playdough.presenter.GamePackPlayerPresenter;
import com.jdom.word.playdough.presenter.GamePackPlayerView;

@RunWith(PowerMockRunner.class)
public class GamePackPlayerPresenterTest {

	private final File testDir = setupTestClassDir(getClass());

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

	@Mock
	private GamePackPlayerModel mockModel;

	@Mock
	private GamePackPlayerView mockView;

	@Mock
	private GamePackPlayerSerializer mockSerializer;

	private GamePackPlayerPresenter presenter;

	@Before
	public void setUp() {
		GamePackPlayerPresenter.serializer = mockSerializer;

		Mockito.when(mockView.getFilesDir()).thenReturn(testDir);
		Mockito.when(mockSerializer.serialize(Mockito.<Jumble> anyObject()))
				.thenReturn(new Properties());
		presenter = new GamePackPlayerPresenter(mockModel, mockView);
	}

	@Test
	public void testSaveStateAndExitWillSaveCurrentJumbleStateToAFile() {
		final String gamePackName = "thisisatest";

		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockModel.getJumble()).thenReturn(mockJumble);
		Mockito.when(mockJumble.getGamePackName()).thenReturn(gamePackName);

		presenter.saveStateAndExit();

		assertTrue("The game state should have been written out to disk!",
				new File(testDir, gamePackName).isFile());
	}

	@Test
	public void testSaveStateAndExitWillUnregisterFromModel() {
		final String gamePackName = "thisisatest";

		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockModel.getJumble()).thenReturn(mockJumble);
		Mockito.when(mockJumble.getGamePackName()).thenReturn(gamePackName);

		presenter.saveStateAndExit();

		Mockito.verify(mockModel).unregisterObserver(presenter);
	}

	@Test
	public void testSaveStateAndExitWillNullReferenceToView() {
		final String gamePackName = "thisisatest";

		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockModel.getJumble()).thenReturn(mockJumble);
		Mockito.when(mockJumble.getGamePackName()).thenReturn(gamePackName);

		presenter.saveStateAndExit();

		assertNull(
				"The presenter should have nulled its reference to the view!",
				presenter.view);
	}

	@Test
	public void testLoadStateWillSetLoadedStateOnModel() throws IOException {
		final String gamePackName = "thisisatest";

		FileUtils.write(new File(testDir, gamePackName), "test=test");

		Jumble mockJumble = Mockito.mock(Jumble.class);
		Jumble mockDeserializedJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockModel.getJumble()).thenReturn(mockJumble);
		Mockito.when(mockJumble.getGamePackName()).thenReturn(gamePackName);
		Mockito.when(
				mockSerializer.deserialize(Mockito.<Properties> anyObject(),
						Mockito.<Jumble> anyObject())).thenReturn(
				mockDeserializedJumble);
		Mockito.when(mockJumble.getSourceWord()).thenReturn("someword");
		Mockito.when(mockJumble.getSourceLetters()).thenReturn(
				Arrays.asList('s', 'o', 'm', 'e', 'w', 'o', 'r', 'd'));

		presenter.loadState();

		Mockito.verify(mockModel).setJumble(mockDeserializedJumble);
	}

	@Test
	public void testLoadStateWillRegisterAsObserverOfModel() throws IOException {
		final String gamePackName = "thisisatest";

		FileUtils.write(new File(testDir, gamePackName), "test=test");

		Jumble mockJumble = Mockito.mock(Jumble.class);
		Jumble mockDeserializedJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockModel.getJumble()).thenReturn(mockJumble);
		Mockito.when(mockJumble.getGamePackName()).thenReturn(gamePackName);
		Mockito.when(
				mockSerializer.deserialize(Mockito.<Properties> anyObject(),
						Mockito.<Jumble> anyObject())).thenReturn(
				mockDeserializedJumble);
		Mockito.when(mockJumble.getSourceWord()).thenReturn("someword");
		Mockito.when(mockJumble.getSourceLetters()).thenReturn(
				Arrays.asList('s', 'o', 'm', 'e', 'w', 'o', 'r', 'd'));

		presenter.loadState();

		Mockito.verify(mockModel).registerObserver(presenter);
	}

	@Test
	public void testUpdateWordsFoundDisplayCallsSetWithCorrectTextOnView() {
		final int availableAnswers = 7;
		final int foundAnswers = 3;
		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockJumble.getAvailableAnswersCount()).thenReturn(
				availableAnswers);
		Mockito.when(mockJumble.getWordsFoundCount()).thenReturn(foundAnswers);

		presenter.updateWordsFoundDisplay(mockJumble);

		Mockito.verify(mockView).setWordsFoundDisplay("3/7 (42.86%)");
	}

	@Test
	public void testUpdateMessagesCallsSetWithCorrectMessages() {
		List<String> messages = Arrays.asList("message1", "message2");
		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockJumble.getMessages()).thenReturn(messages);

		presenter.updateMessages(mockJumble);

		Mockito.verify(mockView).setMessages(messages);
	}

	@Test
	public void testUpdateCurrentWordCallsSetWithUpperCasedVersion() {
		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockJumble.getSourceWord()).thenReturn("lowercase");

		presenter.updateCurrentWord(mockJumble);

		Mockito.verify(mockView).setCurrentWord("LOWERCASE");
	}

	@Test
	public void testUpdateScoreCallsSetWithStringVersion() {
		final int score = 10;
		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockJumble.getScore()).thenReturn(score);

		presenter.updateScore(mockJumble);

		Mockito.verify(mockView).setScore("" + 10);
	}

	@Test
	public void testUpdateRunningWordCallsSetWithWord() {
		final String runningWord = "thisisatest";
		Jumble mockJumble = Mockito.mock(Jumble.class);
		Mockito.when(mockJumble.getRunningWord()).thenReturn(runningWord);

		presenter.updateRunningWord(mockJumble);

		Mockito.verify(mockView).setRunningWord(runningWord);
	}

	// @Test
	// public void
	// testUpdateSourceLettersCreatesDisabledButtonConfigurationForUsedButton()
	// {
	// final String runningWord = "thisisatest";
	// Jumble mockJumble = Mockito.mock(Jumble.class);
	// Mockito.when(mockJumble.getRunningWord()).thenReturn(runningWord);
	//
	// presenter.addLetterToRunningWord(1, 'S');
	// presenter.updateSourceLetters(mockJumble);
	//
	// Mockito.verify(mockView).setRunningWord(runningWord);
	// }
}
