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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.jdom.word.playdough.model.GamePackPlayerSerializer;
import com.jdom.word.playdough.model.GamePackPlayerSerializerImpl;
import com.jdom.word.playdough.model.Jumble;
import com.jdom.word.playdough.model.gamepack.GamePack;

public class JumbleSerializerTest {

	private final GamePackPlayerSerializer serializer = new GamePackPlayerSerializerImpl();

	@Test
	public void testSerializeDeserializeWordsFoundForEachSourceWord() {
		List<String> playableWords = new ArrayList<String>();
		playableWords.add("testWord1");
		playableWords.add("testWord2");

		Map<String, List<String>> validAnswerMap = new HashMap<String, List<String>>();
		GamePack gamePack = new GamePack(playableWords, validAnswerMap);
		Jumble jumble = new Jumble("name", gamePack, "testUser",
				new MockServerCommunicationManager());

		Set<String> foundWords = new HashSet<String>();
		foundWords.add("answer1");
		foundWords.add("answer2");
		Set<String> foundWords2 = new HashSet<String>();
		foundWords2.add("answer3");
		foundWords2.add("answer4");

		jumble.wordsFoundMap.put("testWord1", foundWords);
		jumble.wordsFoundMap.put("testWord2", foundWords2);

		Properties properties = serializer.serialize(jumble);
		Jumble recreated = serializer.deserialize(properties, jumble);
		Set<String> recreatedFoundWords = recreated.wordsFoundMap
				.get("testWord1");
		assertTrue("The found words does not appear to have been restored!",
				recreatedFoundWords.contains("answer1"));
		assertTrue("The found words does not appear to have been restored!",
				recreatedFoundWords.contains("answer2"));

		recreatedFoundWords = recreated.wordsFoundMap.get("testWord2");
		assertTrue("The found words does not appear to have been restored!",
				recreatedFoundWords.contains("answer3"));
		assertTrue("The found words does not appear to have been restored!",
				recreatedFoundWords.contains("answer4"));
	}

	@Test
	public void testSerializeDeserializeScoreForEachSourceWord() {
		List<String> playableWords = new ArrayList<String>();
		playableWords.add("testWord1");
		playableWords.add("testWord2");

		Map<String, List<String>> validAnswerMap = new HashMap<String, List<String>>();
		GamePack gamePack = new GamePack(playableWords, validAnswerMap);
		Jumble jumble = new Jumble("name", gamePack, "testUser",
				new MockServerCommunicationManager());

		jumble.scoreMap.put("testWord1", 10);
		jumble.scoreMap.put("testWord2", 15);

		Properties properties = serializer.serialize(jumble);
		Jumble recreated = serializer.deserialize(properties, jumble);
		int score1 = recreated.scoreMap.get("testWord1");
		int score2 = recreated.scoreMap.get("testWord2");

		assertEquals("The scores do not seem to have been correctly restored!",
				10, score1);
		assertEquals("The scores do not seem to have been correctly restored!",
				15, score2);
	}

	@Test
	public void testSerializeDeserializeStoresCurrentWord() {
		List<String> playableWords = new ArrayList<String>();
		playableWords.add("testWord1");
		playableWords.add("testWord2");

		Map<String, List<String>> validAnswerMap = new HashMap<String, List<String>>();
		GamePack gamePack = new GamePack(playableWords, validAnswerMap);
		Jumble jumble = new Jumble("name", gamePack, "testUser",
				new MockServerCommunicationManager());

		jumble.nextWord();

		String currentWord = jumble.currentWord;

		Properties properties = serializer.serialize(jumble);
		Jumble recreated = serializer.deserialize(properties, jumble);

		assertEquals("The current word doesn't seem to have been restored!",
				currentWord, recreated.currentWord);
	}

	@Test
	public void testSerializeDeserializeRestoresCurrentWordScoreForSingleWordPack() {
		List<String> playableWords = new ArrayList<String>();
		playableWords.add("testWord1");

		Map<String, List<String>> validAnswerMap = new HashMap<String, List<String>>();
		GamePack gamePack = new GamePack(playableWords, validAnswerMap);
		Jumble jumble = new Jumble("name", gamePack, "testUser",
				new MockServerCommunicationManager());

		jumble.scoreMap.put("testWord1", 10);

		Properties properties = serializer.serialize(jumble);
		Jumble recreated = serializer.deserialize(properties, jumble);

		assertEquals(
				"The current word score doesn't seem to have been restored!",
				10, recreated.getScore());
	}
}
