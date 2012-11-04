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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jdom.word.playdough.model.ServerCommunicationManagerImpl;
import com.jdom.word.playdough.model.WordPlaydoughConfiguration;
import com.jdom.word.playdough.model.ServerCommunicationManagerImpl.CacheStatusMarker;

@RunWith(PowerMockRunner.class)
public class ServerCommunicationManagerImplTest {

	final HttpClient mockClient = PowerMockito.mock(HttpClient.class);
	final HttpMethod mockMethod = PowerMockito.mock(HttpMethod.class);
	final CacheStatusMarker mockCacheStatusMarker = PowerMockito
			.mock(CacheStatusMarker.class);
	final WordPlaydoughConfiguration mockConfiguration = PowerMockito
			.mock(WordPlaydoughConfiguration.class);

	@Test
	public void testRetrievesScoreFromServerAndGetsResponse()
			throws HttpException, IOException {
		final int score = 30;
		final String user = "testUser";
		final String word = "testWord";

		// Required return values
		PowerMockito.when(mockMethod.getResponseBodyAsString()).thenReturn(
				"" + score);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		int returnValue = impl.getUserHighScoreForWord(user, word);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).getResponseBodyAsString();
		verify(mockMethod).releaseConnection();

		assertEquals("Incorrect score was retrieved!", score, returnValue);
	}

	@Test
	public void testDoesNotRetrieveScoreFromServerIfConfiguredAsSuch()
			throws HttpException, IOException {
		final String user = "testUser";
		final String word = "testWord";

		// Required return values
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.getUserHighScoreForWord(user, word);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient, never()).executeMethod(mockMethod);
		verify(mockMethod, never()).getResponseBodyAsString();
		verify(mockMethod, never()).releaseConnection();
	}

	@Test
	public void testSendsScoreToServer() throws HttpException, IOException {
		final int score = 30;
		final String user = "testUser";
		final String word = "testWord";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.proposeUserHighScoreForWord(user, word, score);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
	}

	@Test
	public void testDoesNotSendScoreToServerIfConfiguredAsSuch()
			throws HttpException, IOException {
		final int score = 30;
		final String user = "testUser";
		final String word = "testWord";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.proposeUserHighScoreForWord(user, word, score);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient, never()).executeMethod(mockMethod);
		verify(mockMethod, never()).releaseConnection();
	}

	@Test
	public void testRetrievesLockStatusFromServerAndGetsResponse()
			throws IOException {
		final boolean lockStatus = true;
		final String user = "testUser";
		final String pack = "testPack";

		// Required return values
		PowerMockito.when(mockMethod.getResponseBodyAsString()).thenReturn(
				"" + lockStatus);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();
		boolean returnValue = impl.isGamePackLocked(user, pack);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).getResponseBodyAsString();
		verify(mockMethod).releaseConnection();

		assertEquals("Incorrect lock status was retrieved!", lockStatus,
				returnValue);
	}

	@Test
	public void testDoesNotRetrieveLockStatusFromServerIfConfiguredAsSuch()
			throws IOException {
		final boolean lockStatus = true;
		final String user = "testUser";
		final String pack = "testPack";

		// Required return values
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();
		impl.isGamePackLocked(user, pack);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient, never()).executeMethod(mockMethod);
		verify(mockMethod, never()).getResponseBodyAsString();
		verify(mockMethod, never()).releaseConnection();
	}

	@Test
	public void testSendsLockStatusToServer() throws HttpException, IOException {
		final boolean lockStatus = false;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.sendLockStatus(user, pack, lockStatus);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
	}

	@Test
	public void testDoesNotSendsLockStatusToServerIfConfiguredAsSuch()
			throws HttpException, IOException {
		final boolean lockStatus = false;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.sendLockStatus(user, pack, lockStatus);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient, never()).executeMethod(mockMethod);
		verify(mockMethod, never()).releaseConnection();
	}

	@Test
	public void testIfConfiguredToNotUseServerLockStatusIsSentToCache()
			throws HttpException, IOException {

		final boolean lockStatus = false;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.sendLockStatus(user, pack, lockStatus);

		// Verifications
		verify(mockCacheStatusMarker).addCachedLockStatus(user, pack,
				lockStatus);
	}

	@Test
	public void testIfConfiguredToNotUseServerHighScoreIsSentToCache()
			throws HttpException, IOException {
		final int score = 30;
		final String user = "testUser";
		final String word = "testWord";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.proposeUserHighScoreForWord(user, word, score);

		// Verifications
		verify(mockCacheStatusMarker).addCachedUserHighScore(user, word, score);
	}

	@Test
	public void testIfConfiguredToNotUseServerGetHighScoreIsFromCache()
			throws HttpException, IOException {
		final int score = 30;
		final String user = "testUser";
		final String word = "testWord";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);
		PowerMockito.when(mockCacheStatusMarker.getCachedUserHighScore(word))
				.thenReturn(score);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertEquals("Didn't get the correct score from the cache!", score,
				impl.getUserHighScoreForWord(user, word));

		// Verifications
		verify(mockCacheStatusMarker, times(1)).getCachedUserHighScore(word);
	}

	@Test
	public void testIfConfiguredToNotUseServerGetLockStatusIsFromCache()
			throws IOException {
		final boolean lockStatus = true;
		final String user = "testUser";
		final String pack = "testPack";

		// Required return values
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(false);
		PowerMockito.when(mockCacheStatusMarker.isGamePackLocked(user, pack))
				.thenReturn(lockStatus);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();
		assertTrue(impl.isGamePackLocked(user, pack));

		// Verifications
		verify(mockCacheStatusMarker, times(1)).isGamePackLocked(user, pack);
	}

	@Test
	public void testIfServerIsUnreachableDuringSetLockStatusDirtyMarkerSetToTrue()
			throws HttpException, IOException {

		final boolean lockStatus = false;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.doThrow(new RuntimeException("blah")).when(mockClient)
				.executeMethod(mockMethod);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.sendLockStatus(user, pack, lockStatus);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(true);
	}

	@Test
	public void testIfServerIsReachableDuringSetLockStatusDirtyMarkerSetToFalse()
			throws HttpException, IOException {

		final boolean lockStatus = false;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.sendLockStatus(user, pack, lockStatus);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
	}

	@Test
	public void testIfServerIsUnreachableDuringSetHighScoreDirtyMarkerSetToTrue()
			throws HttpException, IOException {

		final int score = 10;
		final String user = "testUser";
		final String word = "testPack";

		PowerMockito.doThrow(new RuntimeException("blah")).when(mockClient)
				.executeMethod(mockMethod);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.proposeUserHighScoreForWord(user, word, score);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(true);
	}

	@Test
	public void testIfServerIsReachableDuringSetHighScoreDirtyMarkerSetToFalse()
			throws HttpException, IOException {

		final int score = 10;
		final String user = "testUser";
		final String word = "testPack";

		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		impl.proposeUserHighScoreForWord(user, word, score);

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
	}

	@Test
	public void testIfCacheWasDirtyAndSetHighScoreSucceedsReturnsTrue()
			throws HttpException, IOException {
		final int score = 10;
		final String user = "testUser";
		final String word = "testPack";

		PowerMockito.when(mockCacheStatusMarker.getCacheStatus()).thenReturn(
				true);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertTrue(
				"Should have received true since the cache was dirty and now it's not!",
				impl.proposeUserHighScoreForWord(user, word, score));

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
	}

	@Test
	public void testIfCacheWasNotDirtyAndProposeHighScoreSucceedsReturnsFalse()
			throws HttpException, IOException {
		final int score = 10;
		final String user = "testUser";
		final String word = "testPack";

		PowerMockito.when(mockCacheStatusMarker.getCacheStatus()).thenReturn(
				false);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertFalse(
				"Should have received false since the cache was not dirty!",
				impl.proposeUserHighScoreForWord(user, word, score));

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
	}

	@Test
	public void testIfCacheWasDirtyAndSetLockStatusSucceedsReturnsTrue()
			throws HttpException, IOException {
		final boolean lockStatus;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockCacheStatusMarker.getCacheStatus()).thenReturn(
				true);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertTrue(
				"Should have received true since the cache was dirty and now it's not!",
				impl.sendLockStatus(user, pack, true));

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
		// Now verify no puts and only one get from the cache
		verify(mockCacheStatusMarker, Mockito.never()).addCachedLockStatus(
				user, pack, true);
		verify(mockCacheStatusMarker, Mockito.times(1)).getCachedLockStatuses();
	}

	@Test
	public void testIfCacheWasNotDirtyAndSetLockStatusSucceedsDoesNotPutOrGetDataInCache()
			throws HttpException, IOException {
		final boolean lockStatus = true;
		final String user = "testUser";
		final String pack = "testPack";

		PowerMockito.when(mockCacheStatusMarker.getCacheStatus()).thenReturn(
				false);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertFalse(
				"Should have received false since the cache was not dirty!",
				impl.sendLockStatus(user, pack, lockStatus));

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
		// Now verify no gets/puts from the cache
		verify(mockCacheStatusMarker, Mockito.never()).addCachedLockStatus(
				user, pack, lockStatus);
		verify(mockCacheStatusMarker, Mockito.never()).getCachedLockStatuses();
	}

	@Test
	public void testIfCacheWasDirtyAndProposeUserScoreSucceedsReturnsTrueCacheDataIsSent()
			throws HttpException, IOException {
		final int score = 10;
		final String user = "testUser";
		final String word = "word";

		PowerMockito.when(mockCacheStatusMarker.getCacheStatus()).thenReturn(
				true);
		PowerMockito.when(mockConfiguration.isCommunicateWithServer())
				.thenReturn(true);

		TestMockServerCommunicationManager impl = new TestMockServerCommunicationManager();

		assertTrue(
				"Should have received true since the cache was dirty and now it's not!",
				impl.proposeUserHighScoreForWord(user, word, score));

		// Verifications of stuff we expect to ALWAYS happen
		verify(mockClient).executeMethod(mockMethod);
		verify(mockMethod).releaseConnection();
		verify(mockCacheStatusMarker).setCacheStatus(false);
		// Now verify no puts and only one get from the cache
		verify(mockCacheStatusMarker, Mockito.never()).addCachedUserHighScore(
				user, word, score);
		verify(mockCacheStatusMarker, Mockito.times(1))
				.getCachedUserHighScores();
	}

	class TestMockServerCommunicationManager extends
			ServerCommunicationManagerImpl {
		public TestMockServerCommunicationManager() {
			super(mockCacheStatusMarker, mockConfiguration);
		}

		@Override
		protected HttpMethod getHttpMethod(String url) {
			return mockMethod;
		}

		@Override
		protected HttpClient getHttpClient() {
			return mockClient;
		}
	}
}
