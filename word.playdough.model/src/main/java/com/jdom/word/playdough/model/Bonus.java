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

import java.util.Set;

public interface Bonus {

	int getBonusPoints(String originalWord, String proposedWord,
			Set<String> validAnswersForWord);

	String getDescription();

	String getLookupSuffix();

	int getPoints();

	Bonus AT_LEAST_HALF_THE_LETTERS_USED = new Bonus() {
		public int getBonusPoints(String originalWord, String proposedWord,
				Set<String> allAnswers) {
			// Multiply by 10 here so we don't have to deal with decimals
			if ((proposedWord.length() * 10) > ((originalWord.length() * 10 / 2) - 1)) {
				return getPoints();
			}

			return 0;
		}

		public String getDescription() {
			return "At least half the letters used!";
		}

		public String getLookupSuffix() {
			return ".BonusAtLeastHalfLettersUsed";
		}

		public int getPoints() {
			return 1;
		}
	};
	Bonus STARTS_WITH_WORD = new Bonus() {
		public int getBonusPoints(String originalWord, String proposedWord,
				Set<String> allAnswers) {
			if (originalWord.startsWith(proposedWord)) {
				return getPoints();
			}

			return 0;
		}

		public String getDescription() {
			return "Found the smaller word the original word starts with!";
		}

		public String getLookupSuffix() {
			return ".BonusStartsWithWord";
		}

		public int getPoints() {
			return 1;
		}
	};

	Bonus LONGEST_WORD = new Bonus() {
		public int getBonusPoints(String originalWord, String proposedWord,
				Set<String> allAnswers) {
			int proposedWordLength = proposedWord.length();
			for (String answer : allAnswers) {
				// If there is a single word longer than this word, then it
				// doesn't count for
				// the bonus
				if (!answer.equals(proposedWord)
						&& answer.length() > proposedWordLength) {
					return 0;
				}
			}

			return getPoints();
		}

		public int getPoints() {
			return 1;
		}

		public String getDescription() {
			return "Found one of the longest word that can be formed from the original word!";
		}

		public String getLookupSuffix() {
			return ".BonusLongestWord";
		}

	};

	Bonus[] AVAILABLE_BONUSES = new Bonus[] { AT_LEAST_HALF_THE_LETTERS_USED,
			STARTS_WITH_WORD, LONGEST_WORD };
}