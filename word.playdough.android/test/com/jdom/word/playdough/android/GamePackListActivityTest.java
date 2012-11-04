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

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.jdom.android.utilities.ViewUtils;
import com.jdom.word.playdough.android.AndroidGamePackResolver;
import com.jdom.word.playdough.android.GamePackListActivity;
import com.jdom.word.playdough.android.GamePackPlayerActivity;
import com.jdom.word.playdough.android.MockAndroidGamePackResolver;
import com.jdom.word.playdough.android.WordPlaydoughApplication;
import com.jdom.word.playdough.model.MockServerCommunicationManager;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(RobolectricTestRunner.class)
public class GamePackListActivityTest {

	private final GamePackListActivity activity = new GamePackListActivity();

	@BeforeClass
	public static void staticSetUp() {
		System.setProperty("test.mode", Boolean.TRUE.toString());
	}

	@Test
	public void testActivityHasCustomApplication() {
		setupActivity();

		assertEquals("The custom application was not found for the activity!",
				WordPlaydoughApplication.class, activity.getApplication()
						.getClass());
	}

	@Test
	public void testTheGamePackListModelIsAvailableOnTheApplication() {
		setupActivity();

		WordPlaydoughApplication application = (WordPlaydoughApplication) activity
				.getApplication();
		assertNotNull("Unable to find the game pack list model!",
				application.getGamePackListModel());
	}

	@Test
	public void testTheGamePackPlayerModelIsAvailableOnTheApplication() {
		setupActivity();

		WordPlaydoughApplication application = (WordPlaydoughApplication) activity
				.getApplication();
		assertNotNull("Unable to find the game pack player model!",
				application.getGamePackPlayerModel());
	}

	@Test
	public void testAButtonIsCreatedForEachGamePack() {
		setupActivity();

		ListView tableLayout = (ListView) activity
				.findViewById(R.id.gamePackList);

		List<String> expectedGamePackNames = new ArrayList<String>();
		expectedGamePackNames.add("1");
		expectedGamePackNames.add("2");
		expectedGamePackNames.add("3");

		List<TextView> textViews = ViewUtils.getChildrenOfType(tableLayout,
				TextView.class);
		for (TextView textView : textViews) {
			expectedGamePackNames.remove(textView.getText());
		}

		assertTrue(
				"Did not find all game packs as available buttons!  Still remaining: "
						+ expectedGamePackNames,
				expectedGamePackNames.isEmpty());
	}

	@Test
	public void testClickingOnUnlockedGamePackStartsActivity() {
		setupActivity();

		ListView gamePackList = (ListView) activity
				.findViewById(R.id.gamePackList);
		Robolectric.shadowOf(gamePackList).clickFirstItemContainingText("1");

		ShadowActivity shadowActivity = shadowOf(activity);
		Intent startedIntent = shadowActivity.getNextStartedActivity();
		ShadowIntent shadowIntent = shadowOf(startedIntent);
		assertEquals(
				"The unlocked game pack should have started the activity directly!",
				shadowIntent.getComponent().getClassName(),
				GamePackPlayerActivity.class.getName());
	}

	@Test
	public void testClickingOnUnlockedGamePackSetGamePackOnTheGamePackPlayerModel() {
		setupActivity();

		ListView gamePackList = (ListView) activity
				.findViewById(R.id.gamePackList);
		Robolectric.shadowOf(gamePackList).clickFirstItemContainingText("1");

		WordPlaydoughApplication application = activity
				.getWordPlaydoughApplication();
		assertEquals("Did not find the playable word set on the model!",
				"bookworm", application.getGamePackPlayerModel().getJumble()
						.getSourceWord());
	}

	@Test
	public void testClickingOnUnlockedGamePackDoesNotDisplayADialog() {
		setupActivity();

		ListView gamePackList = (ListView) activity
				.findViewById(R.id.gamePackList);
		Robolectric.shadowOf(gamePackList).clickFirstItemContainingText("1");

		AlertDialog lastDialog = ShadowAlertDialog.getLatestAlertDialog();
		assertNull(
				"A confirmation dialog should not have been displayed for an already unlocked game pack!",
				lastDialog);
	}

	private void setupActivity() {
		AndroidGamePackResolver.ID_OF_GAME_PACK_LIST = R.raw.test_game_pack_list;
		WordPlaydoughApplication wordPlaydoughApplication = activity
				.getWordPlaydoughApplication();
		wordPlaydoughApplication.onCreate();
		wordPlaydoughApplication
				.onCreate(new MockAndroidGamePackResolver(
						wordPlaydoughApplication,
						new MockServerCommunicationManager()));
		activity.onCreate(null);
		activity.onResume();
	}
}
