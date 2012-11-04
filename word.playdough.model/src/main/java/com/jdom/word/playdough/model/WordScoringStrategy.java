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

public interface WordScoringStrategy {
	int getScoreForAnswer(String originalWord, String answer);

	WordScoringStrategy ONE_POINT_PER_WORD = new WordScoringStrategy() {
		public int getScoreForAnswer(String originalWord, String answer) {
			return 1;
		}
	};

	WordScoringStrategy ONE_POINT_PER_WORD_LETTER = new WordScoringStrategy() {
		public int getScoreForAnswer(String originalWord, String answer) {
			return answer.length();
		}
	};
}
