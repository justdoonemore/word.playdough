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

import java.util.HashSet;
import java.util.Set;

import com.jdom.word.playdough.model.ServerCommunicationManager;

public class MockServerCommunicationManager implements
		ServerCommunicationManager {

	String submittedUser;

	int submittedScore;

	String submittedWord;

	Set<String> unlocked = new HashSet<String>();

	public int getUserHighScoreForWord(String user, String word) {
		return submittedScore;
	}

	public boolean proposeUserHighScoreForWord(String user, String word,
			int score) {
		this.submittedUser = user;
		this.submittedWord = word;
		this.submittedScore = score;
		return false;
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		return !unlocked.contains(gamePackFileName);
	}

	public boolean sendLockStatus(String username, String gamePackFileName,
			boolean locked) {
		if (locked) {
			unlocked.remove(gamePackFileName);
		} else {
			unlocked.add(gamePackFileName);
		}
		return false;
	}
}
