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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jdom.word.playdough.model.ServerCommunicationManagerImpl.CacheStatusMarker;

public class DefaultCacheHandler implements CacheStatusMarker {
	private final File directory;

	public DefaultCacheHandler(File directory) {
		this.directory = directory;
	}

	public void setCacheStatus(boolean status) {
		if (status) {
			try {
				FileUtils
						.write(new File(directory, "cacheStatus"), "" + status);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		} else {
			FileUtils.deleteQuietly(new File(directory, "cacheStatus"));
		}
	}

	public boolean getCacheStatus() {
		return new File(directory, "cacheStatus").isFile();
	}

	public void addCachedLockStatus(String user, String pack, boolean lockStatus) {
		String existingLine = null;
		try {
			existingLine = FileUtils.readFileToString(new File(directory,
					"cachedLockStatus"));
		} catch (IOException e) {

		}

		existingLine = (existingLine == null) ? pack : existingLine + SPLIT
				+ pack;

		try {
			FileUtils.write(new File(directory, "cachedLockStatus"),
					existingLine);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void addCachedUserHighScore(String user, String word, int score) {

		String existingLine = null;
		try {
			existingLine = FileUtils.readFileToString(new File(directory,
					CACHED_HIGH_SCORES));

			existingLine = existingLine + "_" + word + SPLIT + score;

		} catch (IOException e) {
			existingLine = word + SPLIT + score;
		}

		try {
			FileUtils.write(new File(directory, CACHED_HIGH_SCORES),
					existingLine);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public List<String> getCachedLockStatuses() {
		try {
			File file = new File(directory, "cachedLockStatus");
			String existingLine = FileUtils.readFileToString(file);
			return Arrays.asList(existingLine.split(SPLIT));

		} catch (IOException e) {
			return Collections.<String> emptyList();
		}
	}

	public List<String> getCachedUserHighScores() {
		try {
			File file = new File(directory, CACHED_HIGH_SCORES);
			String existingLine = FileUtils.readFileToString(file);
			FileUtils.deleteQuietly(file);
			return Arrays.asList(existingLine.split("_"));

		} catch (IOException e) {
			return Collections.<String> emptyList();
		}
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		try {
			File file = new File(directory, "cachedLockStatus");
			String existingLine = FileUtils.readFileToString(file);
			return !Arrays.asList(existingLine.split(SPLIT)).contains(
					gamePackFileName);

		} catch (IOException e) {
			return true;
		}
	}

	public int getCachedUserHighScore(String word) {
		try {
			File file = new File(directory, CACHED_HIGH_SCORES);
			String existingLine = FileUtils.readFileToString(file);

			for (String score : Arrays.asList(existingLine.split("_"))) {
				String[] split = score.split(SPLIT);
				if (word.equals(split[0])) {
					return Integer.parseInt(split[1]);
				}
			}

			return 0;

		} catch (IOException e) {
			return 0;
		}
	}

	public void clearUserScores() {
		File file = new File(directory, CACHED_HIGH_SCORES);
		FileUtils.deleteQuietly(file);
	}

	public void clearCachedLockStatuses() {
		File file = new File(directory, "cachedLockStatus");
		FileUtils.deleteQuietly(file);
	}
}
