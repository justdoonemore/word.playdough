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
package com.jdom.word.playdough.swing;

import java.io.File;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jdom.util.Observer;
import com.jdom.util.PropertiesUtil;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.DefaultCacheHandler;
import com.jdom.word.playdough.model.GamePackListModel;
import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.ModelFactory;
import com.jdom.word.playdough.model.ServerCommunicationManager;
import com.jdom.word.playdough.model.ServerCommunicationManagerImpl;
import com.jdom.word.playdough.model.ServerGamePackResolver;
import com.jdom.word.playdough.model.WordPlaydoughConfiguration;
import com.jdom.word.playdough.model.ServerCommunicationManagerImpl.CacheStatusMarker;
import com.jdom.word.playdough.model.WordPlaydoughConfiguration.WordPlaydoughConfigurationImpl;

public class Main extends JFrame implements Observer {
	private static final long serialVersionUID = 2100441180903958393L;
	private static final File GAME_PACK = new File(
			"src/main/resources/game_pack.properties");

	public static void main(String[] args) {
		Properties properties = PropertiesUtil.readPropertiesFile(new File(
				"src/main/resources/word_playdough.properties"));
		WordPlaydoughConfiguration configuration = new WordPlaydoughConfigurationImpl(
				properties);

		ModelFactory modelFactory = new ModelFactory();
		CacheStatusMarker cacheHandler = new DefaultCacheHandler(new File(
				System.getProperty("java.io.tmpdir")));
		ServerCommunicationManager commManager = new ServerCommunicationManagerImpl(
				cacheHandler, configuration);
		modelFactory.setServerCommunicationManager(commManager);

		String username = "testUser";
		GamePackPlayerModel playerModel = new GamePackPlayerModel(username);
		modelFactory.setGamePackPlayerModel(playerModel);

		GamePackListModel listModel = new GamePackListModel(username,
				modelFactory, new ServerGamePackResolver(new File(
						"src/main/resources"), commManager));
		modelFactory.setGamePackListModel(listModel);

		Main main = new Main();
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.add(getGamePackListPanel(listModel));
		mainPanel.add(getGamePanel(modelFactory));

		main.getContentPane().add(mainPanel);
		main.setVisible(true);

		playerModel.registerObserver(main);
		listModel.registerObserver(main);
	}

	private static GamePackListPanel getGamePackListPanel(
			GamePackListModel listModel) {
		GamePackListPanel panel = new GamePackListPanel(listModel);
		listModel.registerObserver(panel);

		return panel;
	}

	private static GamePackPlayerPanel getGamePanel(ModelFactory modelFactory) {
		GamePackPlayerPanel panel = new GamePackPlayerPanel(modelFactory);
		modelFactory.getGamePackPlayerModel().registerObserver(panel);

		return panel;
	}

	public void update(Subject subject) {
		pack();
	}

}
