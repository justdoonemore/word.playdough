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
import java.util.SortedSet;

import com.jdom.util.Observer;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.gamepack.GamePack;
import com.jdom.word.playdough.model.gamepack.GamePackFileParser;

public class GamePackListModel implements Subject {

	public static int COST_OF_GAME_PACK = 100;

	private final Collection<Observer> observers = new ArrayList<Observer>();

	private final GamePackResolver gamePackResolver;

	private final ModelFactory modelFactory;

	private boolean showing = true;

	private final String username;

	private int points;

	public GamePackListModel(String username, ModelFactory modelFactory,
			GamePackResolver gamePackResolver) {
		this.username = username;
		this.modelFactory = modelFactory;
		this.gamePackResolver = gamePackResolver;

		this.points = 0;
	}

	public SortedSet<String> getAvailableGamePackFiles() {
		return gamePackResolver.getGamePackNames();
	}

	public boolean isGamePackLocked(String gamePackFileName) {
		return gamePackResolver.isGamePackLocked(this.username,
				gamePackFileName);
	}

	public void playGamePack(String gamePackName) {
		GamePackFileParser parser = new GamePackFileParser();
		GamePack gamePack = parser.parse(gamePackResolver
				.getGamePackContents(gamePackName));
		Jumble jumble = new Jumble(gamePackName, gamePack, "testuser",
				modelFactory.getServerCommunicationManager());

		GamePackPlayerModel gamePackPlayerModel = modelFactory
				.getGamePackPlayerModel();
		gamePackPlayerModel.setJumble(jumble);

		setShowing(false);
	}

	public boolean isShowing() {
		return showing;
	}

	public void setShowing(boolean showing) {
		this.showing = showing;

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

	public String getUsername() {
		return username;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int pointsToNotify) {
		points = pointsToNotify;
		updateObservers();
	}

	public void checkPoints() {
	}

	public void init() {
	}
}
