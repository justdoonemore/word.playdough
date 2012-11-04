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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jdom.util.Observer;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.gamepack.GamePack;

public class Jumble implements Subject {

	static final String BONUS_PREFIX = "* BONUS: ";

	private JumbleChecker jumbleChecker;

	private Set<String> wordsFound = new HashSet<String>();

	static WordScoringStrategy scoreStrategy = WordScoringStrategy.ONE_POINT_PER_WORD_LETTER;

	private final List<String> playableWords;

	private int currentWordIndex;

	GamePack gamePack;

	String currentWord;

	private List<Character> availableCharacters;

	private int score;

	private String runningWord = "";

	private List<String> messages = new ArrayList<String>();

	private Configuration configuration;

	private final Collection<Observer> observers = new ArrayList<Observer>();

	private final String username;

	private final String gamePackName;

	ServerCommunicationManager highScoreManager;

	Map<String, Set<String>> wordsFoundMap = new HashMap<String, Set<String>>();

	Map<String, Integer> scoreMap = new HashMap<String, Integer>();

	public Jumble(String gamePackName, GamePack gamePack, String username,
			ServerCommunicationManager highScoreManager) {
		this.gamePackName = gamePackName;
		this.configuration = new Configuration(false);
		this.gamePack = gamePack;
		this.username = username;
		this.highScoreManager = highScoreManager;
		playableWords = gamePack.getPlayableWords();
		setupNextWord();
	}

	public int getScore() {
		return score;
	}

	public String getSourceWord() {
		return currentWord;
	}

	public void nextWord() {
		calculateScore();
		setupNextWord();
	}

	public void previousWord() {
		calculateScore();
		setupPreviousWord();
	}

	private void calculateScore() {
		int oldHighScore = highScoreManager.getUserHighScoreForWord(username,
				currentWord);

		if (score > oldHighScore) {
			highScoreManager.proposeUserHighScoreForWord(username, currentWord,
					score);

			messages.add("Congratulations!  New high score of " + score
					+ " for word [" + currentWord + "]!");
		} else {
			messages.add("Score of " + score + " for word [" + currentWord
					+ "]!");
		}

		if (this.currentWord != null) {
			scoreMap.put(currentWord, score);
		}
	}

	void setupPreviousWord() {
		if (currentWordIndex == 0) {
			currentWordIndex = playableWords.size();
		}

		currentWordIndex--;

		this.currentWord = playableWords.get(currentWordIndex);

		setupWord();
	}

	void setupNextWord() {
		if (currentWordIndex == playableWords.size() - 1) {
			currentWordIndex = -1;
		}

		currentWordIndex++;

		this.currentWord = playableWords.get(currentWordIndex);

		setupWord();
	}

	private void setupWord() {
		jumbleChecker = new JumbleChecker(gamePack, currentWord);

		if (scoreMap.get(this.currentWord) == null) {
			scoreMap.put(currentWord, 0);
		}
		score = scoreMap.get(this.currentWord);

		this.wordsFound = wordsFoundMap.get(this.currentWord);

		// If no collection has been created for the current word,
		// create it. Otherwise use the existing.
		if (this.wordsFound == null) {
			this.wordsFound = new HashSet<String>();
			wordsFoundMap.put(this.currentWord, this.wordsFound);
		}

		clearRunningWord();
	}

	public List<Character> getSourceLetters() {
		return availableCharacters;
	}

	public void addLetterToRunningWord(char character) {
		character = Character.toLowerCase(character);
		availableCharacters.remove(new Character(character));
		runningWord += Character.toUpperCase(character);

		SortedSet<String> wordsFoundStartingWithRunningWord = new TreeSet<String>();
		for (String foundWord : wordsFound) {
			if (foundWord.toLowerCase().startsWith(runningWord.toLowerCase())) {
				wordsFoundStartingWithRunningWord.add(foundWord);
			}
		}

		if (!wordsFoundStartingWithRunningWord.isEmpty()) {
			messages.add("Already found " + wordsFoundStartingWithRunningWord);
		}

		updateObservers();
	}

	public void proposeWord() {
		proposeWord(runningWord);
	}

	void proposeWord(String proposedWord) {
		proposedWord = proposedWord.toLowerCase();
		int scoreForAnswer = scoreStrategy.getScoreForAnswer(currentWord,
				proposedWord);

		if (jumbleChecker.isValidWord(proposedWord)) {
			if (wordsFound.add(proposedWord)) {
				messages.add("+" + scoreForAnswer + " Valid word ["
						+ proposedWord + "]!");
				score += scoreForAnswer;
				score += checkForBonuses(currentWord, proposedWord);
				scoreMap.put(currentWord, score);
			} else {
				messages.add("You already found [" + proposedWord + "]!");
			}
		} else if (configuration.isHardcoreMode()) {
			score -= scoreForAnswer;
			messages.add("-" + scoreForAnswer + " Invalid word ["
					+ proposedWord + "]!");
		} else {
			messages.add("Invalid word [" + proposedWord + "]!");
		}

		scoreMap.put(currentWord, score);

		clearRunningWord();
	}

	private int checkForBonuses(String currentWord2, String proposedWord) {
		int totalBonusScore = 0;

		// We know all bonuses in the list returned below apply, so just
		// get the points, no need to reverify
		for (Bonus bonus : gamePack.getApplicableBonuses(currentWord2,
				proposedWord)) {

			int bonusScore = bonus.getPoints();

			messages.add("+" + bonusScore + " " + bonus.getDescription()
					+ " [bonus]");

			totalBonusScore += bonusScore;
		}
		return totalBonusScore;
	}

	public void clearRunningWord() {
		availableCharacters = WordUtil.getListOfCharactersInWord(currentWord);
		runningWord = "";

		updateObservers();
	}

	public List<String> getMessages() {
		List<String> currentMessages = messages;
		messages = new ArrayList<String>();
		return currentMessages;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getRunningWord() {
		return runningWord;
	}

	public void removeLetterFromRunningWordViaIndex(int index) {
		availableCharacters
				.add(Character.toLowerCase(runningWord.charAt(index)));
		String tempRunningWord = runningWord;
		runningWord = tempRunningWord.substring(0, index);
		runningWord += tempRunningWord.substring(index + 1);

		updateObservers();
	}

	public void registerObserver(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}

		observer.update(this);
	}

	public void unregisterObserver(Observer observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	private void updateObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}

	public Set<String> getWordsFound() {
		return wordsFound;
	}

	public void shuffleLetters() {
		Collections.shuffle(availableCharacters);

		updateObservers();
	}

	public List<String> getAvailableAnswers() {
		return jumbleChecker.getAvailableAnswers();
	}

	public String getGamePackName() {
		return gamePackName;
	}

	public GamePack getGamePack() {
		return gamePack;
	}

	public ServerCommunicationManager getServerCommunicationManager() {
		return highScoreManager;
	}

	public int getWordsFoundCount() {
		return getWordsFound().size();
	}

	public int getAvailableAnswersCount() {
		return getAvailableAnswers().size();
	}
}
