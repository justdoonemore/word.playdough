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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.jdom.util.PropertiesUtil;
import com.jdom.word.playdough.model.Bonus;
import com.jdom.word.playdough.model.JumbleChecker;

public class GamePackFileGenerator {
	
	private static final int MINIMUM_WORD_LENGTH = 7;
	private final Set<String> words;

	/**
	 * Usage: java GamePackFileGenerator <input file> <output directory>
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File dictionaryFile = new File(args[0]);

		// Validate arguments first
		if (!dictionaryFile.isFile()) {
			throw new IllegalArgumentException(
					"The specified file does not seem to be a valid file ["
							+ dictionaryFile.getAbsolutePath() + "]!");
		}

		File outputDir = new File(args[1]);
		if (!outputDir.isDirectory()) {
			throw new IllegalArgumentException(
					"The specified directory does not seem to be a valid directory ["
							+ outputDir.getAbsolutePath() + "]!");
		}

		Set<String> words = GamePackFileGenerator
				.readWordsFromDictionaryFile(dictionaryFile);

		GamePackFileGenerator generator = new GamePackFileGenerator(words);
		List<Properties> properties = generator.generateGamePacks(20);

		for (int i = 0; i < properties.size(); i++) {
			Properties current = properties.get(i);
			File outputFile = new File(outputDir, i + ".properties");
			PropertiesUtil.writePropertiesFile(current, outputFile);
		}
	}

	public GamePackFileGenerator(Set<String> words) {
		this.words = words;
	}

	GamePackFileGenerator(String[] wordsInArray) {
		this.words = new HashSet<String>();
		for (String string : wordsInArray) {
			words.add(string);
		}
	}

	public Set<String> findWordsWithSevenLettersOrMore() {
		Set<String> longEnoughWords = new HashSet<String>();

		for (String word : words) {
			if (word.length() > MINIMUM_WORD_LENGTH - 1) {
				for (String word2 : words) {
					if (!word.equals(word2)
							&& JumbleChecker.containsWordLetters(word, word2)) {
						longEnoughWords.add(word);
						break;
					}
				}
			}
		}

		return longEnoughWords;
	}

	Properties generateProperties() {
		System.out.println("Finding words with seven letters or more...");
		Set<String> playableWords = findWordsWithSevenLettersOrMore();
		System.out.println("Done finding words with seven letters or more...");
		String propertiesString = StringUtils.join(playableWords,
				PropertiesUtil.SEPARATOR);
		Properties properties = new Properties();
		properties.setProperty(GamePack.PLAYABLE_WORDS_KEY, propertiesString);

		for (String word : playableWords) {
			System.out.println("Finding valid answers for word [" + word
					+ "]...");
			// First find all valid answers for the playable word
			Set<String> validAnswersForWord = findValidAnswersForWord(word,
					words);
			System.out.println("Done finding valid answers for word [" + word
					+ "]...");

			properties.setProperty(word + GamePack.ANSWERS_SUFFIX, StringUtils
					.join(validAnswersForWord, PropertiesUtil.SEPARATOR));

			for (Bonus bonus : Bonus.AVAILABLE_BONUSES) {
				System.out.println("Checking for bonus of types ["
						+ bonus.getClass().getName() + "]");
				// Now find bonus answers
				Set<String> bonusAnswers = new HashSet<String>();
				for (String wordToCheck : validAnswersForWord) {
					int bonusScore = bonus.getBonusPoints(word, wordToCheck,
							validAnswersForWord);

					if (bonusScore > 0) {
						bonusAnswers.add(wordToCheck);
					}
				}
				properties.setProperty(word + bonus.getLookupSuffix(),
						StringUtils
								.join(bonusAnswers, PropertiesUtil.SEPARATOR));
			}
		}

		return properties;
	}

	private Set<String> findValidAnswersForWord(String playableWord,
			Set<String> allWords) {
		Set<String> validAnswersForWord = new HashSet<String>();
		for (String word : allWords) {
			if (word.equals(playableWord))
				continue;

			if (JumbleChecker.containsWordLetters(playableWord, word)) {
				validAnswersForWord.add(word);
			}
		}

		return validAnswersForWord;
	}

	static Set<String> readWordsFromDictionaryFile(File file)
			throws IOException {
		BufferedReader reader = null;

		Set<String> words = new HashSet<String>();
		try {
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				words.add(line.trim());
			}
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return words;
	}

	public List<Properties> generateGamePacks(
			int numberOfPlayableWordsInEachPack) {
		List<Properties> finishedPacks = new ArrayList<Properties>();

		Properties allProperties = generateProperties();

		List<String> allPlayableWords = PropertiesUtil.getPropertyAsList(
				allProperties, GamePack.PLAYABLE_WORDS_KEY);

		Iterator<String> iter = allPlayableWords.iterator();
		Properties currentPack = new Properties();
		List<String> currentPackPlayableWords = new ArrayList<String>();
		for (int i = 0; iter.hasNext(); i++) {
			String playableWord = iter.next();
			System.out.println("Finding a pack for word #" + i + " ["
					+ playableWord + "]");
			String playableWordPrefix = playableWord + ".";

			if (i % numberOfPlayableWordsInEachPack == 0 && i > 0) {
				currentPack.setProperty(GamePack.PLAYABLE_WORDS_KEY,
						StringUtils.join(currentPackPlayableWords,
								PropertiesUtil.SEPARATOR));
				finishedPacks.add(currentPack);
				currentPackPlayableWords.clear();
				currentPack = new Properties();
			}

			currentPackPlayableWords.add(playableWord);
			Iterator<Entry<Object, Object>> propertiesIter = allProperties
					.entrySet().iterator();

			while (propertiesIter.hasNext()) {
				Entry<Object, Object> entry = propertiesIter.next();
				String key = (String) entry.getKey();
				if (key.startsWith(playableWordPrefix)) {
					currentPack.setProperty(key, (String) entry.getValue());
					propertiesIter.remove();
				}
			}
		}

		if (!currentPack.isEmpty()) {
			currentPack.setProperty(GamePack.PLAYABLE_WORDS_KEY, StringUtils
					.join(currentPackPlayableWords, PropertiesUtil.SEPARATOR));
			finishedPacks.add(currentPack);
		}

		return finishedPacks;
	}
}