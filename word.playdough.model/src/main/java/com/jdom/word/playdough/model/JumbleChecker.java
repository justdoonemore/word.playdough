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

import java.util.ArrayList;
import java.util.List;

import com.jdom.word.playdough.model.gamepack.GamePack;

public class JumbleChecker {

	private final String originalWord;

	private final GamePack dictionary;

	public JumbleChecker(GamePack dictionary, String originalWord) {
		this.dictionary = dictionary;
		this.originalWord = originalWord;
	}

	public boolean containsWordLetters(String proposedWord) {
		return containsWordLetters(originalWord, proposedWord);
	}

	public boolean isValidWord(String proposedWord) {
		return dictionary.getValidAnswers(originalWord).contains(proposedWord);
	}

	public static boolean containsWordLetters(String originalWord,
			String proposedWord) {
		List<Character> listOfCharacters = new ArrayList<Character>();
		for (int i = 0; i < originalWord.length(); i++) {
			listOfCharacters.add(originalWord.charAt(i));
		}

		for (int i = 0; i < proposedWord.length(); i++) {
			Character character = proposedWord.charAt(i);
			if (!listOfCharacters.remove(character)) {
				return false;
			}
		}

		return true;
	}

	public List<String> getAvailableAnswers() {
		return dictionary.getValidAnswers(originalWord);
	}

}
