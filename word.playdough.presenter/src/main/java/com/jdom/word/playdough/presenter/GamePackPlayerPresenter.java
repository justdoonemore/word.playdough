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
package com.jdom.word.playdough.presenter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jdom.util.Observer;
import com.jdom.util.PropertiesUtil;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.GamePackPlayerSerializer;
import com.jdom.word.playdough.model.GamePackPlayerSerializerImpl;
import com.jdom.word.playdough.model.Jumble;

public class GamePackPlayerPresenter implements Observer {

	static GamePackPlayerSerializer serializer = new GamePackPlayerSerializerImpl();

	private final GamePackPlayerModel model;
	GamePackPlayerView view;
	private boolean[] originalSourceLettersUsedMarkers;

	public GamePackPlayerPresenter(GamePackPlayerModel model,
			GamePackPlayerView view) {
		this.model = model;
		this.view = view;
	}

	public void saveStateAndExit() {
		Properties properties = serializer.serialize(model.getJumble());
		PropertiesUtil.writePropertiesFile(properties,
				new File(view.getFilesDir(), model.getJumble()
						.getGamePackName()));

		this.model.unregisterObserver(this);
		this.view = null;
	}

	public void loadState() {
		Jumble jumble = model.getJumble();
		try {
			File file = new File(view.getFilesDir(), jumble.getGamePackName());
			Properties properties = PropertiesUtil.readPropertiesFile(file);
			jumble = serializer.deserialize(properties, model.getJumble());
			model.setJumble(jumble);
		} catch (IllegalArgumentException iae) {
			// Just means no activity has been performed on this pack
		}

		model.registerObserver(this);
	}

	public void update(Subject subject) {
		Jumble jumble = model.getJumble();
		updateWordsFoundDisplay(jumble);
		updateMessages(jumble);
		updateCurrentWord(jumble);
		updateScore(jumble);
		updateRunningWord(jumble);
		updateSourceLetters(jumble);
	}

	void updateRunningWord(Jumble jumble) {
		view.setRunningWord(jumble.getRunningWord());
	}

	void updateScore(Jumble jumble) {
		view.setScore(jumble.getScore() + "");
	}

	void updateMessages(Jumble jumble) {
		view.setMessages(jumble.getMessages());
	}

	void updateWordsFoundDisplay(Jumble jumble) {
		int wordsFound = jumble.getWordsFoundCount();
		int availableWords = jumble.getAvailableAnswersCount();
		double percentage = ((double) wordsFound / (double) availableWords) * 100D;
		String wordsFoundText = wordsFound + "/" + availableWords + " ("
				+ new DecimalFormat("##.##").format(percentage) + "%)";

		view.setWordsFoundDisplay(wordsFoundText);
	}

	void updateCurrentWord(Jumble jumble) {
		view.setCurrentWord(jumble.getSourceWord().toUpperCase());
	}

	void updateSourceLetters(Jumble jumble) {
		List<Character> sourceLetters = jumble.getSourceLetters();
		String runningWord = jumble.getRunningWord();
		if (runningWord == null || StringUtils.isEmpty(runningWord)) {
			originalSourceLettersUsedMarkers = new boolean[sourceLetters.size()];
		}

		String sourceWord = jumble.getSourceWord();
		int length = sourceWord.length();
		List<ButtonConfiguration> buttons = new ArrayList<ButtonConfiguration>(
				length);
		for (int i = 0; i < length; i++) {
			final int currentIndex = i;
			Character character = new Character(sourceWord.charAt(i));

			final String letter = new String("" + character).toUpperCase();
			boolean shouldBeEnabled = !originalSourceLettersUsedMarkers[i];

			Runnable clickAction = new Runnable() {
				public void run() {
					new Thread() {
						final SoundHandler soundHandler = view
								.getSoundHandler();

						@Override
						public void run() {
							if (!soundHandler.isMuted()) {
								soundHandler.playButtonClick();
							}
						}
					}.start();

					addLetterToRunningWord(currentIndex, letter.charAt(0));
				}
			};

			ButtonConfiguration button = new ButtonConfiguration(letter,
					currentIndex, shouldBeEnabled, clickAction);
			buttons.add(button);
		}

		view.updateSourceLetters(buttons);
	}

	public void addLetterToRunningWord(int indexOfLetterInSourceWord,
			char letter) {
		Jumble jumble = model.getJumble();
		originalSourceLettersUsedMarkers[indexOfLetterInSourceWord] = true;
		jumble.addLetterToRunningWord(letter);
	}

	public void submitWord() {
		model.getJumble().proposeWord();
	}

	public void nextWord() {
		model.getJumble().nextWord();
	}

	public void previousWord() {
		model.getJumble().previousWord();
	}

	public void clearRunningWord() {
		model.getJumble().clearRunningWord();
	}
}
