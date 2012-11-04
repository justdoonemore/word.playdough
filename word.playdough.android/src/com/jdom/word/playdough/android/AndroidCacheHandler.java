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
package com.jdom.word.playdough.android;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jdom.word.playdough.model.ServerCommunicationManagerImpl.CacheStatusMarker;

public class AndroidCacheHandler implements CacheStatusMarker {
	private final Context context;

	AndroidCacheHandler(Context context) {
		this.context = context;
	}

	public void setCacheStatus(boolean status) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(CACHE_DIRTY_MARKER, status);
		editor.commit();
	}

	public boolean getCacheStatus() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(CACHE_DIRTY_MARKER, Boolean.FALSE);
	}

	public void addCachedLockStatus(String user, String pack, boolean lockStatus) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		String existingLine = sharedPreferences.getString(CACHED_LOCK_STATUS,
				null);

		existingLine = (existingLine == null) ? pack : existingLine + SPLIT
				+ pack;

		Editor editor = sharedPreferences.edit();
		editor.putString(CACHED_LOCK_STATUS, existingLine);
		editor.commit();
	}

	public List<String> getCachedLockStatuses() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		String existingLine = sharedPreferences.getString(CACHED_LOCK_STATUS,
				null);

		return (existingLine == null) ? Collections.<String> emptyList()
				: Arrays.asList(existingLine.split(SPLIT));

	}

	public void addCachedUserHighScore(String user, String word, int score) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		String existingLine = sharedPreferences.getString(CACHED_HIGH_SCORES,
				null);

		existingLine = (existingLine == null) ? word + SPLIT + score
				: existingLine + "_" + word + SPLIT + score;

		Editor editor = sharedPreferences.edit();
		editor.putString(CACHED_HIGH_SCORES, existingLine);
		editor.commit();
	}

	public List<String> getCachedUserHighScores() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		String existingLine = sharedPreferences.getString(CACHED_HIGH_SCORES,
				null);

		return (existingLine == null) ? Collections.<String> emptyList()
				: Arrays.asList(existingLine.split("_"));
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		String existingLine = sharedPreferences.getString(CACHED_LOCK_STATUS,
				null);

		return (existingLine == null) ? true : !Arrays.asList(
				existingLine.split(SPLIT)).contains(gamePackFileName);
	}

	public void clearCachedLockStatuses() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(CACHED_LOCK_STATUS, null);
		editor.commit();
	}

	public void clearUserScores() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(CACHED_LOCK_STATUS, null);
		editor.commit();
	}

	public int getCachedUserHighScore(String word) {
		for (String score : getCachedUserHighScores()) {
			String[] split = score.split(SPLIT);
			if (word.equals(split[0])) {
				return Integer.parseInt(split[1]);
			}
		}

		return 0;
	}

}
