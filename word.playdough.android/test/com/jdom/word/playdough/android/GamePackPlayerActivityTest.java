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
package com.jdom.word.playdough.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jdom.android.utilities.ViewUtils;
import com.jdom.word.playdough.android.AndroidGamePackResolver;
import com.jdom.word.playdough.android.GamePackPlayerActivity;
import com.jdom.word.playdough.android.WordPlaydoughApplication;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GamePackPlayerActivityTest {

	private static final String BOOKWORM = "BOOKWORM";

	private GamePackPlayerActivity activity;

	private WordPlaydoughApplication application;

	@BeforeClass
	public static void staticSetUp() {
		System.setProperty("test.mode", Boolean.TRUE.toString());
	}

	@Test
	public void testCurrentWordIsDisplayed() {
		setupActivity();

		assertEquals("Did not find the current word as expected!", BOOKWORM,
				((TextView) activity.findViewById(R.id.current_word)).getText());
	}

	@Test
	public void testEachSourceLetterHasARepresentativeButton() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		int numberOfChildren = sourceLetters.size();
		assertEquals(
				"There should have been exactly one button for each letter in the source word!",
				BOOKWORM.length(), numberOfChildren);

		String recreatedWord = "";

		for (int i = 0; i < numberOfChildren; i++) {
			recreatedWord += sourceLetters.get(i).getText();
		}

		assertEquals(
				"Did not find the letters in the correct order from the source word!",
				BOOKWORM, recreatedWord);
	}

	@Test
	public void testClickingSourceLetterAddsItToRunningWord() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		sourceLetters.get(0).performClick();

		TextView runningWord = (TextView) activity
				.findViewById(R.id.running_word);
		assertEquals("Did not find the letter added to the running word!", ""
				+ BOOKWORM.charAt(0), runningWord.getText());
	}

	@Test
	public void testClickingSourceLetterDisablesIt() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		int numberOfSourceLettersBeforeClick = sourceLetters.size();

		sourceLetters.get(0).performClick();

		sourceLetters = ViewUtils.getChildrenOfType(table, Button.class);
		assertEquals("There should still be the same amount of letters!",
				numberOfSourceLettersBeforeClick, sourceLetters.size());

		assertFalse("The first letter should now be disabled!", sourceLetters
				.get(0).isEnabled());
		assertFalse("The first letter should no longer be clickable!",
				sourceLetters.get(0).isClickable());
	}

	@Test
	public void testClearingRunningWordClearsIt() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		sourceLetters.get(0).performClick();

		TextView runningWordTextView = (TextView) activity
				.findViewById(R.id.running_word);
		assertFalse("The running word should not be empty at this point!",
				"".equals(runningWordTextView.getText()));

		Button clearButton = (Button) activity.findViewById(R.id.clear_button);
		clearButton.performClick();

		assertTrue("The running word should be empty at this point!",
				"".equals(runningWordTextView.getText()));

	}

	@Test
	public void testClickingSubmitWordClearsRunningWord() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		sourceLetters.get(0).performClick();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.submit_word);
		submitWordButton.performClick();

		TextView runningWord = (TextView) activity
				.findViewById(R.id.running_word);
		assertEquals(
				"The running word should be clear after submitting a word!",
				"", runningWord.getText());
	}

	@Test
	public void testClickingSubmitWordDisplaysMessages() {
		setupActivity();

		TextView messageView = (TextView) activity.findViewById(R.id.messages);
		CharSequence currentMessages = messageView.getText();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		sourceLetters.get(0).performClick();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.submit_word);
		submitWordButton.performClick();

		assertFalse("The messages should have changed on submitting a word!",
				messageView.getText().equals(currentMessages));
	}

	@Test
	public void testClickingSubmitWordChangesTheScoreDisplay() {
		setupActivity();

		TextView scoreView = (TextView) activity.findViewById(R.id.score);
		CharSequence scoreText = scoreView.getText();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		// Spell book
		for (int i = 0; i < 4; i++) {
			ViewUtils.getChildrenOfType(table, Button.class).get(i)
					.performClick();
		}

		Button submitWordButton = (Button) activity
				.findViewById(R.id.submit_word);
		submitWordButton.performClick();

		assertFalse(
				"The score should have changed on submitting a valid word!",
				scoreView.getText().equals(scoreText));
	}

	@Test
	public void testClickingSubmitWordChangesTheWordsFoundDisplay() {
		setupActivity();

		TextView wordsFoundView = (TextView) activity
				.findViewById(R.id.words_found_count);
		CharSequence wordsFound = wordsFoundView.getText();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		// Spell book
		for (int i = 0; i < 4; i++) {
			ViewUtils.getChildrenOfType(table, Button.class).get(i)
					.performClick();
		}

		Button submitWordButton = (Button) activity
				.findViewById(R.id.submit_word);
		submitWordButton.performClick();

		assertFalse(
				"The words found text should have changed on submitting a valid word!",
				wordsFoundView.getText().equals(wordsFound));
	}

	@Test
	public void testClickingNextWordClearsRunningWord() {
		setupActivity();

		TableLayout table = (TableLayout) activity
				.findViewById(R.id.source_letters_table);

		List<Button> sourceLetters = ViewUtils.getChildrenOfType(table,
				Button.class);
		sourceLetters.get(0).performClick();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.next_word);
		submitWordButton.performClick();

		TextView runningWord = (TextView) activity
				.findViewById(R.id.running_word);
		assertEquals(
				"The running word should be clear after submitting a word!",
				"", runningWord.getText());
	}

	@Test
	public void testClickingNextWordChangesCurrentWord() {
		setupActivity();
		application.getGamePackListModel().playGamePack("2");

		TextView currentWordView = (TextView) activity
				.findViewById(R.id.current_word);

		CharSequence currentWord = currentWordView.getText();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.next_word);
		submitWordButton.performClick();

		assertFalse(
				"The current word should have changed when clicking next word!",
				currentWord.equals(currentWordView.getText()));
	}

	@Test
	public void testClickingPreviousWordChangesCurrentWord() {
		setupActivity();
		application.getGamePackListModel().playGamePack("2");

		TextView currentWordView = (TextView) activity
				.findViewById(R.id.current_word);

		CharSequence currentWord = currentWordView.getText();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.previous_word);
		submitWordButton.performClick();

		assertFalse(
				"The current word should have changed when clicking next word!",
				currentWord.equals(currentWordView.getText()));
	}

	@Test
	public void testClickingPreviousWordThenNextWordChangesBackToOriginalWord() {
		setupActivity();
		application.getGamePackListModel().playGamePack("2");

		TextView currentWordView = (TextView) activity
				.findViewById(R.id.current_word);

		CharSequence currentWord = currentWordView.getText();

		Button nextWordButton = (Button) activity
				.findViewById(R.id.previous_word);
		nextWordButton.performClick();

		assertFalse(
				"The current word should have changed when clicking previous word!",
				currentWord.equals(currentWordView.getText()));

		currentWord = currentWordView.getText();

		Button submitWordButton = (Button) activity
				.findViewById(R.id.next_word);
		submitWordButton.performClick();

		assertFalse(
				"The current word should have changed when clicking next word!",
				currentWord.equals(currentWordView.getText()));
	}

	@Test
	public void testClickingDoneFinishesTheActivity() {
		setupActivity();

		assertFalse(
				"Expected the activity to not be finishing at this moment!",
				activity.isFinishing());

		ImageButton doneButton = (ImageButton) activity.findViewById(R.id.done);
		doneButton.performClick();

		assertTrue("Expected the activity to be finishin at this moment!",
				activity.isFinishing());
	}

	private void setupActivity() {
		System.setProperty("test.mode", Boolean.TRUE.toString());
		activity = new GamePackPlayerActivity();
		AndroidGamePackResolver.ID_OF_GAME_PACK_LIST = R.raw.test_game_pack_list;
		application = activity.getWordPlaydoughApplication();
		application.onCreate();
		application.getGamePackListModel().playGamePack("1");

		activity.onCreate(null);
		activity.onResume();
	}
}
