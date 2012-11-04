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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.jdom.word.playdough.presenter.SoundHandler;

abstract class BaseGamePackActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		MenuItem item = menu.findItem(R.id.mute);

		if (getSoundHandler().isMuted()) {
			item.setTitle(R.string.unmute);
			item.setIcon(R.drawable.ic_lock_silent_mode_off);
		} else {
			item.setTitle(R.string.mute);
			item.setIcon(R.drawable.ic_lock_silent_mode);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.more:
			about();
			return true;
		case R.id.mute:
			reverseMuted(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected boolean isMuted() {
		return getSoundHandler().isMuted();
	}

	protected void reverseMuted(MenuItem item) {
		SoundHandler soundHandler = getSoundHandler();
		if (soundHandler.isMuted()) {
			item.setTitle(R.string.mute);
			item.setIcon(R.drawable.ic_lock_silent_mode);
		} else {
			item.setTitle(R.string.unmute);
			item.setIcon(R.drawable.ic_lock_silent_mode_off);
		}

		soundHandler.toggleMute();
	}

	void about() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q=pub:Just Do One More"));
		startActivity(intent);
	}

	WordPlaydoughApplication getWordPlaydoughApplication() {
		return (WordPlaydoughApplication) getApplication();
	}

	public SoundHandler getSoundHandler() {
		return new AndroidSoundHandler(this);
	}

	public void getDisplayAdResponse(final View adView) {
		runOnUiThread(new Runnable() {
			public void run() {
				int ad_width = adView.getLayoutParams().width;
				int ad_height = adView.getLayoutParams().height;
				LinearLayout view = (LinearLayout) findViewById(R.id.tapjoy_view);
				view.removeAllViews();
				// Using screen width, but substitute for the any width.
				int desired_width = view.getMeasuredWidth();
				// Resize banner to desired width and keep aspect ratio.
				android.view.ViewGroup.LayoutParams layout = new android.view.ViewGroup.LayoutParams(
						desired_width, (desired_width * ad_height) / ad_width);
				adView.setLayoutParams(layout);
				view.addView(adView);
			}
		});

	}
}
