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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jdom.word.playdough.model.Bonus;

public class GamePack {
	public static final String PLAYABLE_WORDS_KEY = "playableWords";
	public static final String STARTS_WITH_WORD_BONUS_SUFFIX = "."
			+ Bonus.STARTS_WITH_WORD.getClass().getName();
	public static final String ALL_WORDS = "allWords";
	public static final String ANSWERS_SUFFIX = ".answers";
	private final List<String> playableWords;
	private final Map<String, List<Bonus>> bonusMap;
	private final Map<String, List<String>> validAnswerMap;

	public GamePack(List<String> playableWords,
			Map<String, List<String>> validAnswerMap) {
		this(playableWords, validAnswerMap, new HashMap<String, List<Bonus>>());
	}

	GamePack(List<String> playableWords,
			Map<String, List<String>> validAnswerMap,
			Map<String, List<Bonus>> bonusMap) {
		this.playableWords = playableWords;
		this.validAnswerMap = validAnswerMap;
		this.bonusMap = bonusMap;
	}

	public List<String> getPlayableWords() {
		return playableWords;
	}

	public List<String> getValidAnswers(String originalWord) {
		return validAnswerMap.get(originalWord);
	}

	public List<Bonus> getApplicableBonuses(String originalWord,
			String proposedWord) {
		List<Bonus> bonuses = bonusMap.get(GamePack.getBonusesLookupKey(
				originalWord, proposedWord));

		return (bonuses == null) ? Collections.<Bonus> emptyList() : bonuses;
	}

	public static String getBonusesLookupKey(String originalWord, String answer) {
		return originalWord + "." + answer;
	}
}