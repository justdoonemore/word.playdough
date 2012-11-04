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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.Configuration;
import com.jdom.word.playdough.model.Jumble;
import com.jdom.word.playdough.model.WordScoringStrategy;
import com.jdom.word.playdough.model.WordUtil;
import com.jdom.word.playdough.model.gamepack.GamePack;
import com.jdom.word.playdough.model.gamepack.MockDictionary;

@RunWith(PowerMockRunner.class)
public class JumbleTest {
	private static final String TEST_USER_NAME = "testUser";

	private final List<String> playableWords = new ArrayList<String>();
	{
		playableWords.add("hypothetical");
	}

	private final Map<String, List<String>> VALID_ANSWER_MAP = new HashMap<String, List<String>>();
	{
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hoot", "hot"));
	}

	private final Map<String, List<Bonus>> bonusesMap = new HashMap<String, List<Bonus>>();

	private final GamePack DICTIONARY = new MockDictionary(playableWords,
			VALID_ANSWER_MAP, bonusesMap);

	private final MockServerCommunicationManager mockHighScoreManager = new MockServerCommunicationManager();

	private final Jumble jumble = getJumble(DICTIONARY);

	@BeforeClass
	public static void staticSetUp() {
		// To simplify things always use one point per word
		Jumble.scoreStrategy = WordScoringStrategy.ONE_POINT_PER_WORD;
	}

	@Before
	public void setUp() {
		jumble.highScoreManager = mockHighScoreManager;
	}

	@Test
	public void testScoreStartsAtZero() {
		assertEquals("Invalid starting score!", 0, jumble.getScore());
	}

	@Test
	public void testJumbleIncreasesScoreWhenValidWordIsSpecified() {
		int scoreBeforeWordProposal = jumble.getScore();

		jumble.proposeWord("hot");

		assertTrue(
				"The score should have been increased when a Valid word was specified!",
				jumble.getScore() > scoreBeforeWordProposal);
	}

	@Test
	public void testJumbleDoesNotIncreaseScoreWhenInvalidWordIsSpecified() {
		int scoreBeforeWordProposal = jumble.getScore();

		jumble.proposeWord("none");

		assertFalse(
				"The score should not have been increased when an Invalid word was specified!",
				jumble.getScore() > scoreBeforeWordProposal);
	}

	@Test
	public void testJumbleDoesNotIncreaseScoreWhenSameWordIsSpecifiedMoreThanOnce() {
		jumble.proposeWord("hot");
		int scoreBeforeSecondWordProposal = jumble.getScore();
		jumble.proposeWord("hot");
		assertFalse(
				"The score should not have been increased when the same word was specified more than once!",
				jumble.getScore() > scoreBeforeSecondWordProposal);
	}

	@Test
	public void testJumbleGoesThroughAllSourceWordsBeforeRestarting() {
		// Add 3 more words to create 4 total
		playableWords.add("thisisoneword");
		playableWords.add("thisistwoword");
		playableWords.add("thisisthreeword");

		Set<String> sourceWordsUsed = new HashSet<String>();

		// Add each word as going through it
		for (int i = 0; i < playableWords.size(); i++) {
			// this will add the following words: hypothetical, thisisoneword,
			// thisistwoword, thisisthreeword
			assertTrue("Should not have used a previously used word yet!",
					sourceWordsUsed.add(jumble.getSourceWord()));
			jumble.nextWord();
		}
		assertFalse("Should have used a previously used word now!",
				sourceWordsUsed.add(jumble.getSourceWord()));
	}

	@Test
	public void testGetSourceLettersIncludesExactlyThoseFromOriginalWord() {
		List<Character> sourceLetters = jumble.getSourceLetters();

		for (int i = 0; i < "hypothetical".length(); i++) {
			assertTrue(
					"Did not find the source letter in the collection!",
					sourceLetters.remove(new Character("hypothetical".charAt(i))));
		}

		assertTrue(
				"There should be no more source letters than the original word contained!",
				sourceLetters.isEmpty());
	}

	@Test
	public void testGetSourceLettersRemovesSourceLetterWhenAddingToRunningWord() {
		List<Character> sourceLetters = jumble.getSourceLetters();
		assertTrue(
				"The letter already used should  be available when not added to the running word!",
				sourceLetters.contains(new Character('y')));

		jumble.addLetterToRunningWord('y');

		assertFalse(
				"The letter already used should not be available when added to the running word!",
				sourceLetters.contains(new Character('y')));
	}

	@Test
	public void testGetSourceLettersRemovesSourceLetterOfDifferentCaseWhenAddingToRunningWord() {
		List<Character> sourceLetters = jumble.getSourceLetters();
		assertTrue(
				"The letter already used should  be available when not added to the running word!",
				sourceLetters.contains(new Character('y')));

		jumble.addLetterToRunningWord('Y');

		assertFalse(
				"The letter already used should not be available when added to the running word!",
				sourceLetters.contains(new Character('y')));
	}

	@Test
	public void testProposingWordResetsAvailableCharacters() {
		jumble.addLetterToRunningWord('y');
		jumble.proposeWord();

		List<Character> sourceLetters = jumble.getSourceLetters();
		assertTrue(
				"All letters should be available again after proposing running word!",
				sourceLetters.contains(new Character('y')));
	}

	@Test
	public void testProposingWordResetsRunningWord() {
		jumble.addLetterToRunningWord('y');
		assertEquals("Y", jumble.getRunningWord());
		jumble.proposeWord();
		assertEquals("", jumble.getRunningWord());
	}

	@Test
	public void testClearingRunningWordResetsAvailableCharacter() {
		jumble.addLetterToRunningWord('y');
		jumble.clearRunningWord();

		List<Character> sourceLetters = jumble.getSourceLetters();
		assertTrue(
				"All letters should be available again after proposing running word!",
				sourceLetters.contains(new Character('y')));
	}

	@Test
	public void testClearingRunningWordResetsRunningWord() {
		jumble.addLetterToRunningWord('y');
		assertEquals("Y", jumble.getRunningWord());
		jumble.clearRunningWord();
		assertEquals("", jumble.getRunningWord());
	}

	@Test
	public void testRemovingLetterFromRunningWordAddsItBackToAvailableLetters() {
		jumble.addLetterToRunningWord('y');
		jumble.removeLetterFromRunningWordViaIndex(0);
		assertTrue("The letter should have been available for use again!",
				jumble.getSourceLetters().contains(new Character('y')));
	}

	@Test
	public void testRemovingFirstLetterFromRunningWordRemovesItFromRunningWord() {
		jumble.addLetterToRunningWord('h');
		jumble.addLetterToRunningWord('o');
		jumble.addLetterToRunningWord('t');
		jumble.removeLetterFromRunningWordViaIndex(0);

		assertEquals("OT", jumble.getRunningWord());
	}

	@Test
	public void testRemovingMiddleLetterFromRunningWordRemovesItFromRunningWord() {
		jumble.addLetterToRunningWord('h');
		jumble.addLetterToRunningWord('o');
		jumble.addLetterToRunningWord('t');
		jumble.removeLetterFromRunningWordViaIndex(1);

		assertEquals("HT", jumble.getRunningWord());
	}

	@Test
	public void testRemovingLastLetterFromRunningWordRemovesItFromRunningWord() {
		jumble.addLetterToRunningWord('h');
		jumble.addLetterToRunningWord('o');
		jumble.addLetterToRunningWord('t');
		jumble.removeLetterFromRunningWordViaIndex(2);

		assertEquals("HO", jumble.getRunningWord());
	}

	@Test
	public void testProposingValidWordSetsMessageWithWordAndPoints() {
		jumble.proposeWord("hot");

		String expectedMessage = "+1 Valid word [hot]!";

		assertEquals(expectedMessage, jumble.getMessages().iterator().next());
	}

	@Test
	public void testProposingValidWordWithDifferentCaseSetsMessageWithWordAndPoints() {
		jumble.proposeWord("HOT");

		String expectedMessage = "+1 Valid word [hot]!";

		assertEquals(expectedMessage, jumble.getMessages().iterator().next());
	}

	@Test
	public void testProposingAlreadyFoundWordReturnsSpecificMessageWithWord() {
		jumble.proposeWord("HOT");
		jumble.getMessages();
		jumble.proposeWord("HOT");

		String expectedMessage = "You already found [hot]!";

		assertEquals(expectedMessage, jumble.getMessages().iterator().next());
	}

	@Test
	public void testAddingAvailableLetterWillAddWordsThatHaveBeenFoundToList() {
		playableWords.clear();
		playableWords.add("stopsign");
		VALID_ANSWER_MAP.put("stopsign", Arrays.asList("stop", "gin"));

		jumble.nextWord();
		jumble.proposeWord("stop");

		jumble.getMessages();

		jumble.addLetterToRunningWord('S');
		assertEquals(
				"Did not find the message listing the already found words as expected!",
				"Already found [stop]", jumble.getMessages().iterator().next());

	}

	@Test
	public void testAddingAvailableLetterWillNotAddWordsThatHaveBeenFoundToListIfTheyDontStartWithTheLetter() {
		playableWords.clear();
		playableWords.add("stopsign");

		VALID_ANSWER_MAP.put("stopsign", Arrays.asList("stop", "gin"));
		jumble.proposeWord("stop");

		jumble.getMessages();

		jumble.addLetterToRunningWord('G');
		assertTrue("There should have been no messages to display!", jumble
				.getMessages().isEmpty());
	}

	@Test
	public void testProposingInvalidWordSetsMessageWithWordAndPointsInHardcoreMode() {
		Configuration configuration = new Configuration(true);
		jumble.setConfiguration(configuration);
		jumble.proposeWord("toh");

		String expectedMessage = "-1 Invalid word [toh]!";

		assertEquals(expectedMessage, jumble.getMessages().iterator().next());
	}

	@Test
	public void testProposingInvalidWordSetsMessageWithWordNotInHardcoreMode() {
		Configuration configuration = new Configuration(false);
		jumble.setConfiguration(configuration);
		jumble.proposeWord("toh");

		String expectedMessage = "Invalid word [toh]!";

		assertEquals(expectedMessage, jumble.getMessages().iterator().next());
	}

	@Test
	public void testProposingValidWordUpdatesScoreInMap() {
		jumble.proposeWord("hot");

		assertTrue(
				"The score should be greater than 0 after submitting a Valid word!",
				jumble.scoreMap.get("hypothetical") > 0);
	}

	@Test
	public void testScoreIsDecreasedWhenRunningInHardcoreMode() {
		Configuration configuration = new Configuration(true);
		jumble.setConfiguration(configuration);

		int score = jumble.getScore();

		jumble.proposeWord("toh");

		assertTrue("Expected the score to be decreased in hardcore mode!",
				jumble.getScore() < score);
	}

	@Test
	public void testScoreIsNotDecreasedWhenNotRunningInHardcoreMode() {
		Configuration configuration = new Configuration(false);
		jumble.setConfiguration(configuration);

		int score = jumble.getScore();

		jumble.proposeWord("toh");

		assertFalse("Expected the score to NOT be decreased in hardcore mode!",
				jumble.getScore() < score);
	}

	@Test
	public void testBonusScoreIsAppliedWhenApplicable() {
		// Create a fake word that will get the
		// "more than half letters used bonus"
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hypothela"));
		bonusesMap.put(
				GamePack.getBonusesLookupKey("hypothetical", "hypothela"),
				Arrays.asList(Bonus.AT_LEAST_HALF_THE_LETTERS_USED));

		jumble.proposeWord("hypothela");

		int newScore = jumble.getScore();

		int expectedBonusAmount = Bonus.AT_LEAST_HALF_THE_LETTERS_USED
				.getBonusPoints("hypothetical", "hypothela",
						new HashSet<String>());
		assertTrue(
				"The expected bonus amount must be greater than zero for this test!",
				expectedBonusAmount > 0);

		assertTrue("The bonus score does not seem to have been applied!",
				(newScore - expectedBonusAmount) > 0);
	}

	@Test
	public void testApplicableBonusesIsRetrievedFromGamePack() {
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hypothela"));

		GamePack mockGamePack = PowerMockito.mock(GamePack.class);
		jumble.gamePack = mockGamePack;
		jumble.proposeWord("hypothela");

		Mockito.verify(mockGamePack).getApplicableBonuses("hypothetical",
				"hypothela");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testApplicableBonusesAreNotRecalculated() {
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hypothela"));

		GamePack mockGamePack = PowerMockito.mock(GamePack.class);
		Bonus mockBonus = PowerMockito.mock(Bonus.class);
		jumble.gamePack = mockGamePack;
		PowerMockito.when(
				mockGamePack.getApplicableBonuses("hypothetical", "hypothela"))
				.thenReturn(Arrays.asList(mockBonus));

		jumble.proposeWord("hypothela");

		Mockito.verify(mockGamePack).getApplicableBonuses("hypothetical",
				"hypothela");
		Mockito.verify(mockBonus, Mockito.never()).getBonusPoints(
				Mockito.anyString(), Mockito.anyString(), Mockito.anySet());
	}

	@Test
	public void testBonusScoreMessageIsAddedToMessageWhenApplicable() {
		// Create a fake word that will get the
		// "at least half letters used bonus"
		bonusesMap.put(
				GamePack.getBonusesLookupKey("hypothetical", "hypothela"),
				Arrays.asList(Bonus.AT_LEAST_HALF_THE_LETTERS_USED));
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hypothela"));

		jumble.proposeWord("hypothela");

		List<String> messages = jumble.getMessages();

		String expectedMessage1 = "+1 Valid word [hypothela]!";
		String expectedMessage2 = "+1 "
				+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED.getDescription()
				+ " [bonus]";

		Iterator<String> iterator = messages.iterator();
		assertEquals(expectedMessage1, iterator.next());
		assertEquals(expectedMessage2, iterator.next());
	}

	@Test
	public void testBonusScoreMessagesIsAddedToMessageWhenMoreThanOneBonusesAreApplicable() {
		playableWords.clear();
		playableWords.add("stopsign");
		VALID_ANSWER_MAP.put("stopsign", Arrays.asList("stop", "sign"));
		bonusesMap.put(GamePack.getBonusesLookupKey("stopsign", "stop"), Arrays
				.asList(Bonus.AT_LEAST_HALF_THE_LETTERS_USED,
						Bonus.STARTS_WITH_WORD));

		jumble.nextWord();
		// Clear old messages
		jumble.getMessages();
		jumble.proposeWord("stop");

		List<String> messages = jumble.getMessages();
		Iterator<String> iter = messages.iterator();

		String expectedMessage1 = "+1 Valid word [stop]!";
		String expectedMessage2 = "+1 "
				+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED.getDescription()
				+ " [bonus]";
		String expectedMessage3 = "+1 "
				+ Bonus.STARTS_WITH_WORD.getDescription() + " [bonus]";

		assertEquals(expectedMessage1, iter.next());
		assertEquals(expectedMessage2, iter.next());
		assertEquals(expectedMessage3, iter.next());
	}

	@Test
	public void testNotifiesObserverOnRegister() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);

		assertEquals(1, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testNotifiesObserverOnNextWord() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);
		jumble.nextWord();

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testNotifiesObserverOnAddLetterToRunningWord() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);
		jumble.addLetterToRunningWord('y');

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testNotifiesObserverOnProposeWord() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);
		jumble.proposeWord();

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testNotifiesObserverOnClearRunningWord() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);
		jumble.clearRunningWord();

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testNotifiesObserverOnRemoveLetterFromRunningWord() {
		MockObserver mockObserver = new MockObserver();
		jumble.addLetterToRunningWord('y');
		jumble.registerObserver(mockObserver);
		jumble.removeLetterFromRunningWordViaIndex(0);

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

	@Test
	public void testGettingWordsFoundReturnsAllValidWords() {
		playableWords.clear();
		playableWords.add("stopsign");

		VALID_ANSWER_MAP.put("stopsign", Arrays.asList("stop", "sign"));

		jumble.nextWord();
		jumble.proposeWord("stop");
		jumble.proposeWord("fsdafsd");
		jumble.proposeWord("sign");

		Set<String> wordsFound = jumble.getWordsFound();
		assertEquals(2, wordsFound.size());
		assertTrue(wordsFound.contains("stop"));
		assertTrue(wordsFound.contains("sign"));
	}

	@Test
	public void testScoreGoesBackToZeroOnNextWordCall() {
		playableWords.add("stopsign");

		jumble.proposeWord("hot");
		assertTrue(
				"The score should be greater than zero when specifying a valid word!",
				jumble.getScore() > 0);

		jumble.nextWord();

		assertFalse("The score should be back to zero after changing words!!",
				jumble.getScore() > 0);
	}

	@Test
	public void testScoreGoesBackToZeroOnPreviousWordCall() {
		playableWords.add("stopsign");

		jumble.proposeWord("hot");
		assertTrue(
				"The score should be greater than zero when specifying a valid word!",
				jumble.getScore() > 0);

		jumble.previousWord();

		assertFalse("The score should be back to zero after changing words!!",
				jumble.getScore() > 0);
	}

	@Test
	public void testRunningWordGoesBackToZeroOnNextWordCall() {
		jumble.addLetterToRunningWord('y');
		assertEquals("Y", jumble.getRunningWord());
		jumble.nextWord();
		assertEquals("", jumble.getRunningWord());
	}

	@Test
	public void testAvailableLettersIsDifferentOnNextWordCall() {
		playableWords.add("stopsign");

		assertTrue(
				"The first word should have contained the specified letter!",
				jumble.getSourceLetters().contains('y'));
		jumble.nextWord();
		assertFalse(
				"The second word should not have contained the specified letter!",
				jumble.getSourceLetters().contains('y'));
	}

	@Test
	public void testWordsFoundIsClearedOnNextWordCallIfSourceWordIsDifferent() {
		playableWords.add("stopsign");

		jumble.proposeWord("hot");

		assertTrue(jumble.getWordsFound().contains("hot"));

		jumble.nextWord();

		assertFalse(jumble.getWordsFound().contains("hot"));
	}

	@Test
	public void testCurrentScoreIsSubmittedOnNextWordCall() {
		jumble.proposeWord("hot");
		jumble.nextWord();

		assertEquals(
				"Did not find the correct score submitted to the high score manager!",
				1, mockHighScoreManager.submittedScore);
		assertEquals(
				"Did not find the correct user submitted to the high score manager!",
				TEST_USER_NAME, mockHighScoreManager.submittedUser);
	}

	@Test
	public void testFindingWordIsAddedToListForSourceWord() {
		String sourceWord = jumble.getSourceWord();
		jumble.proposeWord("hot");
		jumble.nextWord();

		Set<String> wordsFound = jumble.wordsFoundMap.get(sourceWord);
		assertTrue(
				"Should have contained the found word for the previously played word!",
				wordsFound.contains("hot"));
	}

	@Test
	public void testScoreIsAddedToMapForSourceWord() {
		String sourceWord = jumble.getSourceWord();
		jumble.proposeWord("hot");
		assertTrue(jumble.getScore() > 0);
		jumble.nextWord();

		Integer scoreForPreviousWord = jumble.scoreMap.get(sourceWord);
		assertTrue("The score should be greater than 0!",
				scoreForPreviousWord > 0);
	}

	@Test
	public void testBeatingHighScoreForUserAddsCongratulationsMessage() {
		jumble.proposeWord("hot");
		mockHighScoreManager.submittedScore = 0;
		jumble.nextWord();

		Iterator<String> iterator = jumble.getMessages().iterator();
		// Skip the score for the proposed word
		iterator.next();
		// Now check the high score message
		assertEquals(
				"Congratulations!  New high score of 1 for word [hypothetical]!",
				iterator.next());
	}

	@Test
	public void testNotBeatingHighScoreForUserAddsScoreMessage() {
		jumble.proposeWord("hot");
		mockHighScoreManager.submittedScore = 2;
		jumble.nextWord();

		Iterator<String> iterator = jumble.getMessages().iterator();
		// Skip the score for the proposed word
		iterator.next();
		// Now check the high score message
		assertEquals("Score of 1 for word [hypothetical]!", iterator.next());
	}

	private Jumble getJumble(GamePack customDictionary) {
		return new Jumble("name", customDictionary, TEST_USER_NAME,
				mockHighScoreManager);
	}

	@Test
	public void testGettingMessagesClearsThem() {
		jumble.proposeWord("hot");
		List<String> messages = jumble.getMessages();
		assertFalse("There should have been a message after proposing a word!",
				messages.isEmpty());
		assertTrue(
				"There should not have been any messages after retrieving them!",
				jumble.getMessages().isEmpty());
	}

	@Test
	public void testShufflingLettersWillReturnThemInADifferentOrder() {
		List<Character> originalArrangement = WordUtil
				.getListOfCharactersInWord(jumble.getSourceWord());

		jumble.shuffleLetters();

		List<Character> newArrangement = jumble.getSourceLetters();

		assertEquals("The same number of letters should have been returned!",
				originalArrangement.size(), newArrangement.size());

		boolean changeFound = false;
		for (int i = 0; i < originalArrangement.size(); i++) {
			if (!newArrangement.get(i).equals(originalArrangement.get(i))) {
				changeFound = true;
				break;
			}
		}

		assertTrue("The letters should not have all been in the same order!",
				changeFound);

		// Make sure all letters were present
		for (Character original : originalArrangement) {
			assertTrue(
					"Did not find the original character in the shuffled version!",
					newArrangement.remove(original));
		}
	}

	@Test
	public void testNotifiesObserverOnShuffleLetters() {
		MockObserver mockObserver = new MockObserver();
		jumble.registerObserver(mockObserver);
		jumble.shuffleLetters();

		assertEquals(2, mockObserver.timesUpdateCalled);
	}

}