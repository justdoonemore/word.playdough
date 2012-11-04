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
package com.jdom.word.playdough.model.gamepack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.jdom.util.PropertiesUtil;
import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.gamepack.GamePack;
import com.jdom.word.playdough.model.gamepack.GamePackFileGenerator;

public class GamePackFileGeneratorTest {

	private static final String PROMPT = "prompt";
	private static final String CURRENT = "current";
	private static final String HYPOTHETICAL = "hypothetical";
	private static final String LONG_WORD_WITH_NO_SOLUTIONS = "zzzzzzzzzzzzzz";
	private static final File SMALL_DICTIONARY_FILE = new File(
			"src/test/resources/example_dic.txt");
	private static final File ELEVEN_WORD_DICTIONARY_FILE = new File(
			"src/test/resources/eleven_word_dictionary_file.txt");
	private final String[] words = new String[] { HYPOTHETICAL, CURRENT, "hot",
			"pot", "heat", "rent", "nut", PROMPT, LONG_WORD_WITH_NO_SOLUTIONS };
	private GamePackFileGenerator dictionary = new GamePackFileGenerator(words);

	@Test
	public void testFindingWordsSevenLettersOrMoreReturnsWordMoreThanSevenLetters() {
		assertTrue(
				"A word more than 7 letters in length should have been included!",
				dictionary.findWordsWithSevenLettersOrMore().contains(
						HYPOTHETICAL));
	}

	@Test
	public void testFindingWordsSevenLettersOrMoreReturnsWordExactlySevenLetters() {
		assertTrue(
				"A word exactly 7 letters in length should have been included!",
				dictionary.findWordsWithSevenLettersOrMore().contains(CURRENT));
	}

	@Test
	public void testFindingWordsSevenLettersOrMoreDoesNotReturnWordLessThanSevenLetters() {
		assertFalse(
				"A word less than 7 letters in length should not have been included!",
				dictionary.findWordsWithSevenLettersOrMore().contains(PROMPT));
	}

	@Test
	public void testFindingWordsSevenLettersOrMoreDoesNotReturnOtherwiseValidWordWithNoWordSolutions() {
		assertFalse(
				"A word with no solutions should not have been included!",
				dictionary.findWordsWithSevenLettersOrMore().contains(
						LONG_WORD_WITH_NO_SOLUTIONS));
	}

	@Test
	public void testPlacesWordsForPlayingInProperties() {
		Properties properties = dictionary.generateProperties();

		String property = properties.getProperty(GamePack.PLAYABLE_WORDS_KEY);
		assertTrue("Should have contained both valid words for playing!",
				property.contains(HYPOTHETICAL));
		assertTrue("Should have contained both valid words for playing!",
				property.contains(CURRENT));
		assertEquals("Should not have contained more words than we expected!",
				HYPOTHETICAL.length() + CURRENT.length() + ",".length(),
				property.length());
	}

	@Test
	public void testPlacesStartsWithWordBonusAnswersInPropertiesFile() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head" });
		Properties properties = dictionary.generateProperties();

		assertEquals(
				"Should have contained the one bonus answer!",
				"hot",
				properties.getProperty("hothead"
						+ Bonus.STARTS_WITH_WORD.getLookupSuffix()));
	}

	@Test
	public void testDoesNotPlacesStartsWithWordBonusAnswersInPropertiesFileForInvalidWords() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head" });
		Properties properties = dictionary.generateProperties();

		assertFalse(
				"Should not have set a bonus property for non-playing words!",
				properties.containsKey("hot"
						+ Bonus.STARTS_WITH_WORD.getLookupSuffix()));
	}

	@Test
	public void testPlacesAtLeastHalfLettersUsedBonusAnswersInPropertiesFile() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head" });
		Properties properties = dictionary.generateProperties();

		assertEquals(
				"Should have contained the one bonus answer!",
				"head",
				properties.getProperty("hothead"
						+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED
								.getLookupSuffix()));
	}

	@Test
	public void testPlacesLongestWordBonusAnswersInPropertiesFile() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head" });
		Properties properties = dictionary.generateProperties();

		assertEquals(
				"Should have contained the one bonus answer!",
				"head",
				properties.getProperty("hothead"
						+ Bonus.LONGEST_WORD.getLookupSuffix()));
	}

	@Test
	public void testDoesNotPlaceAtLeastHalfLettersUsedBonusAnswersInPropertiesFileForInvalidWords() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head" });
		Properties properties = dictionary.generateProperties();

		assertFalse(
				"Should not have set a bonus property for non-playing words!",
				properties.containsKey("hot"
						+ Bonus.STARTS_WITH_WORD.getLookupSuffix()));
	}

	@Test
	public void testDoesNotPlaceInvalidSolutionAsBonusAnswerInPropertiesFile() {
		dictionary = new GamePackFileGenerator(new String[] { "hothead", "hot",
				"head", "half" });
		Properties properties = dictionary.generateProperties();

		assertFalse(
				"Invalid answers should not be included as a bonus answer!",
				properties.getProperty(
						"hothead"
								+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED
										.getLookupSuffix()).contains("half"));
	}

	@Test
	public void testReadsInAllWordsFromDictionaryFile() throws IOException {
		Set<String> words = GamePackFileGenerator
				.readWordsFromDictionaryFile(SMALL_DICTIONARY_FILE);
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("current"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("hot"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("heat"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("pot"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("hypothetical"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("nut"));
		assertTrue(
				"Expected to find the word from the sample dictionary file!",
				words.contains("rent"));
	}

	@Test
	public void testSplitsIntoPacksWithSpecificNumberOfWordsIfSpecified()
			throws IOException {
		final int numberOfWordsInEachGamePack = 5;
		Set<String> words = GamePackFileGenerator
				.readWordsFromDictionaryFile(ELEVEN_WORD_DICTIONARY_FILE);

		GamePackFileGenerator generator = new GamePackFileGenerator(words);
		List<Properties> gamePackPropertiesList = generator
				.generateGamePacks(numberOfWordsInEachGamePack);
		assertEquals("Should have generated 3 game packs!", 3,
				gamePackPropertiesList.size());

		Iterator<Properties> propertiesIter = gamePackPropertiesList.iterator();
		Properties pack = propertiesIter.next();
		assertEquals(
				"There should have been the specified number of playable words in the game pack!",
				numberOfWordsInEachGamePack,
				PropertiesUtil.getPropertyAsList(pack,
						GamePack.PLAYABLE_WORDS_KEY).size());
		pack = propertiesIter.next();
		assertEquals(
				"There should have been the specified number of playable words in the game pack!",
				numberOfWordsInEachGamePack,
				PropertiesUtil.getPropertyAsList(pack,
						GamePack.PLAYABLE_WORDS_KEY).size());
		pack = propertiesIter.next();
		assertEquals(
				"There should have been the specified number of playable words in the game pack!",
				1,
				PropertiesUtil.getPropertyAsList(pack,
						GamePack.PLAYABLE_WORDS_KEY).size());
	}
}