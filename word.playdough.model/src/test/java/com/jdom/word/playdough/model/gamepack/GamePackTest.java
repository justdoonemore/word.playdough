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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.gamepack.GamePack;

public class GamePackTest {
	private static final String PROMPT = "prompt";
	private static final String CURRENT = "current";
	private static final String HYPOTHETICAL = "hypothetical";
	private static final String LONG_WORD_WITH_NO_SOLUTIONS = "zzzzzzzzzzzzzz";
	private final String[] words = new String[] { HYPOTHETICAL, CURRENT, "hot",
			"pot", "heat", "rent", "nut", PROMPT, LONG_WORD_WITH_NO_SOLUTIONS };
	private final List<String> wordsForPlaying = new ArrayList<String>(
			Arrays.asList(HYPOTHETICAL, CURRENT));
	private final Map<String, List<String>> validAnswers = new HashMap<String, List<String>>();
	private final GamePack dictionary = new GamePack(wordsForPlaying,
			validAnswers);

	@Before
	public void setUp() {
		validAnswers.put(CURRENT, Arrays.asList("rent", "nut"));
		validAnswers.put(HYPOTHETICAL, Arrays.asList("hot", "heat"));
		validAnswers.put(PROMPT, Arrays.asList("pot"));
	}

	@Test
	public void testReturnsAssociatedListOfValidAnswers() {
		assertTrue(
				"The word located in the dictionary should have returned true for being there!",
				dictionary.getValidAnswers(PROMPT).contains("pot"));
	}

	@Test
	public void testReturnsNullForInvalidPlayableWord() {
		assertNull(dictionary.getValidAnswers("notpresent"));
	}

	@Test
	public void testReturnsEmptyListWhenNoBonusesApply() {
		List<Bonus> bonuses = dictionary.getApplicableBonuses("blah", "bl");
		assertTrue("Expected an empty list of bonuses!", bonuses.isEmpty());
	}
}