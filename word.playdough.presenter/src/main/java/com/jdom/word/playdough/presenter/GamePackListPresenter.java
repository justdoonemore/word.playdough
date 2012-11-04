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

import java.util.Iterator;
import java.util.SortedSet;

import com.jdom.util.Observer;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.GamePackListModel;

public class GamePackListPresenter implements Observer {
	private final GamePackListModel model;
	GamePackListView view;

	public GamePackListPresenter(GamePackListModel model, GamePackListView view) {
		this.model = model;
		this.view = view;
	}

	public void saveStateAndExit() {
		this.model.unregisterObserver(this);
		this.view = null;
	}

	public void loadState() {
		this.model.registerObserver(this);
		this.model.init();
		this.model.checkPoints();
	}

	public void update(Subject subject) {
		updateGamePackList(model);
		updateGamePackPoints(model);
	}

	private void updateGamePackPoints(GamePackListModel model2) {
	}

	void updateGamePackList(final GamePackListModel model) {
		SortedSet<String> gamePacks = model.getAvailableGamePackFiles();

		Iterator<String> gamePacksIter = gamePacks.iterator();
		ListItemConfiguration[] listItemConfigurations = new ListItemConfiguration[gamePacks
				.size()];
		for (int i = 0; i < listItemConfigurations.length; i++) {
			final String gamePack = gamePacksIter.next();
			final boolean gamePackLocked = model.isGamePackLocked(gamePack);

			Runnable clickAction = new Runnable() {
				public void run() {
					model.playGamePack(gamePack);
					view.playGamePack(gamePack);
				}
			};

			listItemConfigurations[i] = new ListItemConfiguration(gamePack,
					gamePackLocked, clickAction);
		}

		view.setGamePacks(listItemConfigurations);
	}
}
