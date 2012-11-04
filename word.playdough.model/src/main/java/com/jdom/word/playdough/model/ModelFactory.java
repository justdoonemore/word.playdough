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

public final class ModelFactory {

	private GamePackListModel gamePackListModel;

	private GamePackPlayerModel gamePackPlayerModel;

	private ServerCommunicationManager serverCommunicationManager;

	private WordPlaydoughConfiguration configuration;

	public GamePackListModel getGamePackListModel() {
		return gamePackListModel;
	}

	public GamePackPlayerModel getGamePackPlayerModel() {
		return gamePackPlayerModel;
	}

	public void setGamePackPlayerModel(GamePackPlayerModel gamePackPlayerModel) {
		this.gamePackPlayerModel = gamePackPlayerModel;
	}

	public void setGamePackListModel(GamePackListModel gamePackListModel) {
		this.gamePackListModel = gamePackListModel;
	}

	public ServerCommunicationManager getServerCommunicationManager() {
		return serverCommunicationManager;
	}

	public void setServerCommunicationManager(
			ServerCommunicationManager serverCommunicationManager) {
		this.serverCommunicationManager = serverCommunicationManager;
	}

	public WordPlaydoughConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(WordPlaydoughConfiguration configuration) {
		this.configuration = configuration;
	}
}
