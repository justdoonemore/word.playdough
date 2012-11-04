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

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.jdom.word.playdough.presenter.ButtonConfiguration;
import com.jdom.word.playdough.presenter.GamePackPlayerPresenter;
import com.jdom.word.playdough.presenter.GamePackPlayerView;

public class GamePackPlayerActivity extends BaseGamePackActivity implements
		GamePackPlayerView {
	private GamePackPlayerPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.play);
	}

	@Override
	protected void onPause() {
		super.onPause();

		presenter.saveStateAndExit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		presenter = new GamePackPlayerPresenter(getWordPlaydoughApplication()
				.getGamePackPlayerModel(), this);
		presenter.loadState();
	}

	public void setCurrentWord(String currentWord) {
		TextView currentWordView = (TextView) findViewById(R.id.current_word);
		currentWordView.setText(currentWord);
	}

	public void setWordsFoundDisplay(String wordsFoundText) {
		TextView wordsFoundView = (TextView) findViewById(R.id.words_found_count);
		wordsFoundView.setText(wordsFoundText);
	}

	public void setMessages(List<String> messages) {
		String message = StringUtils.join(messages, IOUtils.LINE_SEPARATOR);
		TextView messageView = (TextView) findViewById(R.id.messages);
		messageView.setText(message);
	}

	public void setScore(String score) {
		TextView scoreView = (TextView) findViewById(R.id.score);
		scoreView.setText(score);
	}

	public void setRunningWord(String runningWord) {
		TextView runningWordView = (TextView) findViewById(R.id.running_word);
		runningWordView.setText(runningWord);
	}

	public void submitWord(View view) {
		presenter.submitWord();
	}

	public void nextWord(View view) {
		presenter.nextWord();
	}

	public void previousWord(View view) {
		presenter.previousWord();
	}

	public void clearRunningWord(View view) {
		presenter.clearRunningWord();
	}

	public void done(View view) {
		this.finish();
	}

	public void updateSourceLetters(List<ButtonConfiguration> buttons) {
		TableLayout availableLettersTable = (TableLayout) findViewById(R.id.source_letters_table);
		availableLettersTable.removeAllViews();
		TableRow row = new TableRow(this);

		for (int i = 0; i < buttons.size(); i++) {
			final ButtonConfiguration buttonConfiguration = buttons.get(i);
			final String displayText = buttonConfiguration.getDisplayText();
			final boolean shouldBeEnabled = buttonConfiguration.isEnabled();
			final Runnable clickAction = buttonConfiguration.getClickAction();

			if (i == 9 || i == 18) {
				availableLettersTable.addView(row);
				row = new TableRow(this);
			}

			final Button button = new Button(this);
			button.setText(displayText);
			button.setClickable(shouldBeEnabled);
			button.setEnabled(shouldBeEnabled);
			button.setBackgroundColor(Color.BLACK);
			button.setTextSize(20);
			int color = (shouldBeEnabled) ? Color.WHITE : Color.BLACK;
			button.setTextColor(color);

			if (clickAction != null) {
				button.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						clickAction.run();
					}
				});
			}

			row.addView(button);
		}
		availableLettersTable.addView(row);

	}
}
