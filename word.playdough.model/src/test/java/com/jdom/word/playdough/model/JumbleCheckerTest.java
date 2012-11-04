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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.JumbleChecker;
import com.jdom.word.playdough.model.gamepack.GamePack;
import com.jdom.word.playdough.model.gamepack.MockDictionary;

public class JumbleCheckerTest {

	private static final Map<String, List<String>> VALID_ANSWER_MAP = new HashMap<String, List<String>>();
	static {
		VALID_ANSWER_MAP.put("hypothetical", Arrays.asList("hoot", "hot"));
	}

	private static final GamePack DICTIONARY = new MockDictionary(
			Arrays.asList("hypothetical"), VALID_ANSWER_MAP,
			new HashMap<String, List<Bonus>>());

	private final JumbleChecker checker = new JumbleChecker(DICTIONARY,
			"hypothetical");

	@Test
	public void testProposedWordMadeWithLettersOfOriginalReturnsTrueForContainsWord() {
		assertTrue(
				"Should have returned true when the proposed word is made from letters of the original!",
				checker.containsWordLetters("hot"));
	}

	@Test
	public void testProposedWordMadeWithLettersOfOriginalButWrongQuantityOfLetterReturnsFalseForContainsWord() {
		// Basically there isn't two 'o's in hypothetical but the proposed word
		// does have two
		assertFalse(
				"Should have returned false when the proposed word has too many of one letter of the original!",
				checker.containsWordLetters("hoot"));
	}

	@Test
	public void testProposedWordMadeWithLettersOfOriginalAndDictionaryWordReturnsTrueForValidWord() {
		assertTrue(
				"Should have returned true when the proposed word is contained in the dictionary and is made of valid letters from original word!",
				checker.isValidWord("hot"));
	}

	@Test
	public void testProposedWordMadeWithLettersOfOriginalAndNotDictionaryWordReturnsFalseForValidWord() {
		assertFalse(
				"Should have returned true when the proposed word is contained in the dictionary and is made of valid letters from original word!",
				checker.isValidWord("toh"));
	}
}
