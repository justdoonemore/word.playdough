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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;


import com.jdom.word.playdough.presenter.SoundHandler;

class AndroidSoundHandler implements SoundHandler {

	private static final String MUTE_SETTING = "mute";
	private static final String AUDIO_PREFS = "audio.prefs";

	private final Context context;

	AndroidSoundHandler(Context context) {
		this.context = context;
	}

	public void playButtonClick() {
		MediaPlayer mp = MediaPlayer.create(context, R.raw.button_click);
		mp.start();
	}

	public boolean isMuted() {
		SharedPreferences prefs = getAudioPreferences();
		final boolean isMuted = prefs.getBoolean(MUTE_SETTING, false);
		return isMuted;
	}

	public void toggleMute() {
		Editor editor = getAudioPreferences().edit();
		editor.putBoolean(MUTE_SETTING, !isMuted());
		editor.commit();
	}

	private SharedPreferences getAudioPreferences() {
		SharedPreferences prefs = context.getSharedPreferences(AUDIO_PREFS,
				Context.MODE_PRIVATE);
		return prefs;
	}
}