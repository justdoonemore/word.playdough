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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jdom.util.PropertiesUtil;

public class GamePackPlayerSerializerImpl implements GamePackPlayerSerializer {

	public static final String FOUND_WORDS_SUFFIX = ".foundWords";
	private static final String SCORE_SUFFIX = ".score";
	private static final String CURRENT_WORD = "currentWord";

	public Properties serialize(Jumble jumble) {
		Properties properties = new Properties();

		Map<String, Set<String>> wordsFoundMap = jumble.wordsFoundMap;

		for (String key : wordsFoundMap.keySet()) {
			Set<String> found = wordsFoundMap.get(key);
			String propertyLine = StringUtils.join(found,
					PropertiesUtil.SEPARATOR);

			properties.setProperty(key + FOUND_WORDS_SUFFIX, propertyLine);
		}

		for (String key : jumble.scoreMap.keySet()) {
			properties.setProperty(key + SCORE_SUFFIX,
					"" + jumble.scoreMap.get(key));
		}

		properties.setProperty(CURRENT_WORD, jumble.currentWord);

		return properties;
	}

	public Jumble deserialize(Properties properties, Jumble jumble) {
		Map<String, Set<String>> wordsFoundMap = new HashMap<String, Set<String>>();
		Map<String, Integer> scoreMap = new HashMap<String, Integer>();

		for (Object obj : properties.keySet()) {
			String key = (String) obj;
			if (key.endsWith(FOUND_WORDS_SUFFIX)) {
				String word = key.replaceAll(FOUND_WORDS_SUFFIX, "");
				Set<String> values = PropertiesUtil.getPropertyAsSet(
						properties, key);
				wordsFoundMap.put(word, values);
			}

			if (key.endsWith(SCORE_SUFFIX)) {
				String word = key.replaceAll(SCORE_SUFFIX, "");
				int score = PropertiesUtil.getInteger(properties, key);
				scoreMap.put(word, score);
			}
		}

		jumble = new Jumble(jumble.getGamePackName(), jumble.getGamePack(),
				"testUser", jumble.getServerCommunicationManager());
		jumble.wordsFoundMap = wordsFoundMap;
		jumble.scoreMap = scoreMap;

		String currentWord = properties.getProperty(CURRENT_WORD);

		do {
			jumble.setupNextWord();
		} while (!currentWord.equals(jumble.getSourceWord()));
		return jumble;
	}
}
