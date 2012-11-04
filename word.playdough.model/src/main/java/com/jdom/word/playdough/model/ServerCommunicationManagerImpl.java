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

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class ServerCommunicationManagerImpl implements
		ServerCommunicationManager {

	private final CacheStatusMarker cacheStatusMarker;

	private final WordPlaydoughConfiguration configuration;

	public ServerCommunicationManagerImpl(CacheStatusMarker cacheStatusMarker,
			WordPlaydoughConfiguration configuration) {
		this.cacheStatusMarker = cacheStatusMarker;
		this.configuration = configuration;
	}

	public int getUserHighScoreForWord(String user, String word) {
		if (configuration.isCommunicateWithServer()) {
			HttpClient client = getHttpClient();
			HttpMethod method = getHttpMethod(Constants.GET_HIGH_SCORE_URL
					+ "?" + Constants.USER_KEY + "=" + user + "&"
					+ Constants.WORD_KEY + "=" + word);

			try {
				client.executeMethod(method);
				return Integer.parseInt(method.getResponseBodyAsString());
			} catch (Exception e) {
				return cacheStatusMarker.getCachedUserHighScore(word);
			} finally {
				method.releaseConnection();
			}
		} else {
			return cacheStatusMarker.getCachedUserHighScore(word);
		}
	}

	public boolean proposeUserHighScoreForWord(String user, String word,
			int score) {
		if (configuration.isCommunicateWithServer()) {
			HttpClient client = getHttpClient();
			HttpMethod method = getHttpMethod(Constants.PROPOSE_HIGH_SCORE_URL
					+ "?" + Constants.USER_KEY + "=" + user + "&"
					+ Constants.WORD_KEY + "=" + word + "&"
					+ Constants.VALUE_TO_SET_KEY + "=" + score);

			try {
				boolean dirtyCache = cacheStatusMarker.getCacheStatus();
				client.executeMethod(method);
				cacheStatusMarker.setCacheStatus(false);
				if (dirtyCache) {
					List<String> cachedUserScores = cacheStatusMarker
							.getCachedUserHighScores();
					for (String string : cachedUserScores) {
						String[] scoreSplit = string
								.split(CacheStatusMarker.SPLIT);
						proposeUserHighScoreForWord(user, scoreSplit[0],
								Integer.parseInt(scoreSplit[1]));
					}
					cacheStatusMarker.clearUserScores();
				}
				return dirtyCache;
			} catch (Exception e) {
				e.printStackTrace();
				cacheStatusMarker.setCacheStatus(true);
				cacheStatusMarker.addCachedUserHighScore(user, word, score);
				return false;
			} finally {
				method.releaseConnection();
			}
		} else {
			cacheStatusMarker.addCachedUserHighScore(user, word, score);
			return false;
		}
	}

	public boolean isGamePackLocked(String username, String gamePackFileName) {
		if (configuration.isCommunicateWithServer()) {
			HttpClient client = getHttpClient();
			HttpMethod method = getHttpMethod(Constants.GET_LOCK_STATUS_URL
					+ "?" + Constants.USER_KEY + "=" + username + "&"
					+ Constants.PACK_KEY + "=" + gamePackFileName);

			try {
				client.executeMethod(method);
				return Boolean.parseBoolean(method.getResponseBodyAsString());
			} catch (Exception e) {
				return cacheStatusMarker.isGamePackLocked(username,
						gamePackFileName);
			} finally {
				method.releaseConnection();
			}
		} else {
			return cacheStatusMarker.isGamePackLocked(username,
					gamePackFileName);
		}
	}

	public boolean sendLockStatus(String user, String pack, boolean lockStatus) {
		if (configuration.isCommunicateWithServer()) {
			HttpClient client = getHttpClient();
			HttpMethod method = getHttpMethod(Constants.SET_LOCK_STATUS_URL
					+ "?" + Constants.USER_KEY + "=" + user + "&"
					+ Constants.PACK_KEY + "=" + pack + "&"
					+ Constants.VALUE_TO_SET_KEY + "=" + lockStatus);

			try {
				boolean cacheDirty = cacheStatusMarker.getCacheStatus();
				client.executeMethod(method);
				cacheStatusMarker.setCacheStatus(false);
				if (cacheDirty) {
					for (String gamePack : cacheStatusMarker
							.getCachedLockStatuses()) {
						sendLockStatus(user, gamePack, lockStatus);
					}
					cacheStatusMarker.clearCachedLockStatuses();
				}
				return cacheDirty;
			} catch (Exception e) {
				e.printStackTrace();
				cacheStatusMarker.setCacheStatus(true);
				cacheStatusMarker.addCachedLockStatus(user, pack, lockStatus);
				return false;
			} finally {
				method.releaseConnection();
			}
		} else {
			cacheStatusMarker.addCachedLockStatus(user, pack, lockStatus);
			return false;
		}
	}

	protected HttpClient getHttpClient() {
		return new HttpClient();
	}

	protected HttpMethod getHttpMethod(String url) {
		return new GetMethod(url);
	}

	public static interface CacheStatusMarker {
		String SPLIT = "<>";
		String CACHE_DIRTY_MARKER = "cacheDirtyMarker";
		String PREFERENCES = "preferences";
		String CACHED_LOCK_STATUS = "cachedLockStatus";
		String CACHED_HIGH_SCORES = "cachedHighScores";

		void setCacheStatus(boolean status);

		void clearCachedLockStatuses();

		void clearUserScores();

		int getCachedUserHighScore(String word);

		boolean isGamePackLocked(String username, String gamePackFileName);

		boolean getCacheStatus();

		void addCachedLockStatus(String user, String pack, boolean lockStatus);

		List<String> getCachedLockStatuses();

		void addCachedUserHighScore(String user, String word, int score);

		List<String> getCachedUserHighScores();
	}
}
