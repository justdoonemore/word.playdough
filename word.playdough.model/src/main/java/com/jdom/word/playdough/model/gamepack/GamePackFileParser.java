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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jdom.util.CollectionUtil;
import com.jdom.util.PropertiesUtil;
import com.jdom.word.playdough.model.Bonus;

public class GamePackFileParser {
	public GamePack parse(File gamePack) {
		return parse(PropertiesUtil.readPropertiesFile(gamePack));
	}

	public GamePack parse(String fileContents) {
		return parse(PropertiesUtil.readPropertiesFile(fileContents));
	}

	private GamePack parse(Properties properties) {
		List<String> wordsForPlaying = PropertiesUtil.getPropertyAsList(
				properties, GamePack.PLAYABLE_WORDS_KEY);

		Map<String, List<String>> validAnswerMap = new HashMap<String, List<String>>();

		for (String playableWord : wordsForPlaying) {
			List<String> answers = PropertiesUtil.getPropertyAsList(properties,
					playableWord + GamePack.ANSWERS_SUFFIX);

			validAnswerMap.put(playableWord, answers);
		}

		Map<String, List<Bonus>> bonusesMap = setupBonusesMap(properties);

		GamePack dictionary = new GamePack(wordsForPlaying, validAnswerMap,
				bonusesMap);
		return dictionary;
	}

	private Map<String, List<Bonus>> setupBonusesMap(Properties properties) {
		Map<String, List<Bonus>> bonusesMap = new HashMap<String, List<Bonus>>();

		for (Bonus bonus : Bonus.AVAILABLE_BONUSES) {
			for (Object propertyKey : properties.keySet()) {
				String key = (String) propertyKey;

				if (key.endsWith(bonus.getLookupSuffix())) {
					String originalWord = key.substring(0,
							key.indexOf(bonus.getLookupSuffix()));
					String answersApplicableToBonusString = properties
							.getProperty(key);
					if (answersApplicableToBonusString != null) {
						String[] answersApplicableToBonus = StringUtils.split(
								answersApplicableToBonusString,
								PropertiesUtil.SEPARATOR);

						for (String answer : answersApplicableToBonus) {
							String bonusesMapKey = GamePack
									.getBonusesLookupKey(originalWord, answer);
							List<Bonus> bonusesForAnswer = CollectionUtil
									.getOrPutAndGetList(bonusesMap,
											bonusesMapKey);
							bonusesForAnswer.add(bonus);
						}
					}
				}
			}
		}

		return bonusesMap;
	}
}
