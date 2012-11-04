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

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.gamepack.GamePack;
import com.jdom.word.playdough.model.gamepack.GamePackFileParser;

public class GamePackFileParserTest {

	@Test
	public void testSetsUpCorrectDictionaryFileWithPlayableWords()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another,\nhothead\n" + GamePack.ALL_WORDS
				+ "=head,hot\n" + Bonus.STARTS_WITH_WORD.getLookupSuffix()
				+ "=hot" + "\n" + "hothead" + GamePack.ANSWERS_SUFFIX
				+ "=hot,head" + "\n" + "another" + GamePack.ANSWERS_SUFFIX
				+ "=not";
		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		assertEquals("Incorrect number of playable words!", 2, dictionary
				.getPlayableWords().size());
	}

	@Test
	public void testSetsUpCorrectDictionaryFileWithCorrectPlayableWords()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another,\nhothead\n" + "hothead"
				+ GamePack.ANSWERS_SUFFIX + "=hot,head" + "\n" + "another"
				+ GamePack.ANSWERS_SUFFIX + "=not";
		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		assertEquals("Incorrect number of playable words!", 2, dictionary
				.getPlayableWords().size());
	}

	@Test
	public void testSetsUpCorrectDictionaryFileWithCorrectPlayableWordAnswers()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another,\n" + "hothead" + GamePack.ANSWERS_SUFFIX
				+ "=hot,head" + "\n" + "another" + GamePack.ANSWERS_SUFFIX
				+ "=not";
		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		List<String> validAnswers = dictionary.getValidAnswers("hothead");
		assertTrue(validAnswers.contains("hot"));
		assertTrue(validAnswers.contains("head"));
	}

	@Test
	public void testSetsUpCorrectDictionaryWithStartsWithBonus()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another,\nhothead\n" + "\n" + "hothead"
				+ GamePack.ANSWERS_SUFFIX + "=hot,head\n" + "hothead"
				+ Bonus.STARTS_WITH_WORD.getLookupSuffix() + "=hot" + "\n"
				+ "another" + GamePack.ANSWERS_SUFFIX + "=not";
		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		List<Bonus> bonuses = dictionary.getApplicableBonuses("hothead", "hot");

		assertTrue(bonuses.contains(Bonus.STARTS_WITH_WORD));
		assertFalse(bonuses.contains(Bonus.AT_LEAST_HALF_THE_LETTERS_USED));
	}

	@Test
	public void testSetsUpCorrectDictionaryWithAtLeastHalfTheLettersUsedBonus()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another,hothead\n" + "hothead"
				+ GamePack.ANSWERS_SUFFIX + "=head,hot\n" + "hothead"
				+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED.getLookupSuffix()
				+ "=head" + "\n" + "another" + GamePack.ANSWERS_SUFFIX
				+ "=not";
		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		List<Bonus> bonuses = dictionary
				.getApplicableBonuses("hothead", "head");

		assertFalse(bonuses.contains(Bonus.STARTS_WITH_WORD));
		assertTrue(bonuses.contains(Bonus.AT_LEAST_HALF_THE_LETTERS_USED));
	}

	@Test
	public void testSetsUpCorrectDictionaryWithMultipleBonusesUsed()
			throws IOException {
		String fileContents = GamePack.PLAYABLE_WORDS_KEY + "="
				+ "hothead,another\n" + "hothead" + GamePack.ANSWERS_SUFFIX
				+ "=head,hot\n" + "hothead"
				+ Bonus.AT_LEAST_HALF_THE_LETTERS_USED.getLookupSuffix()
				+ "=head\nhothead" + Bonus.STARTS_WITH_WORD.getLookupSuffix()
				+ "=head\n" + "\n" + "another" + GamePack.ANSWERS_SUFFIX
				+ "=not\n";

		GamePack dictionary = new GamePackFileParser().parse(fileContents);
		List<Bonus> bonuses = dictionary
				.getApplicableBonuses("hothead", "head");

		assertTrue(bonuses.contains(Bonus.STARTS_WITH_WORD));
		assertTrue(bonuses.contains(Bonus.AT_LEAST_HALF_THE_LETTERS_USED));
	}
}
