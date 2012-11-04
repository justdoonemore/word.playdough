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

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;


import com.jdom.word.playdough.model.GamePackListModel;
import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.GamePackResolver;
import com.jdom.word.playdough.model.ModelFactory;
import com.jdom.word.playdough.model.ServerCommunicationManager;
import com.jdom.word.playdough.model.ServerCommunicationManagerImpl;
import com.jdom.word.playdough.model.WordPlaydoughConfiguration;
import com.jdom.word.playdough.model.WordPlaydoughConfiguration.WordPlaydoughConfigurationImpl;

class ApplicationFactoryImpl implements ApplicationFactory {
	protected final Context context;

	protected final ModelFactory modelFactory;

	ApplicationFactoryImpl(Context context, ModelFactory modelFactory) {
		this.context = context;
		this.modelFactory = modelFactory;
	}

	public void init() {
		Properties properties = new Properties();
		InputStream rawResource = null;
		try {
			rawResource = context.getResources().openRawResource(
					R.raw.word_playdough);
			properties.load(rawResource);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		WordPlaydoughConfiguration configuration = new WordPlaydoughConfigurationImpl(
				properties);
		modelFactory.setConfiguration(configuration);
		ServerCommunicationManager serverCommunicationManager = new ServerCommunicationManagerImpl(
				new AndroidCacheHandler(context), configuration);
		modelFactory.setServerCommunicationManager(serverCommunicationManager);
	}

	public void onCreate(GamePackResolver gamePackResolver) {
		modelFactory.setGamePackListModel(new GamePackListModel(TEST_USER,
				modelFactory, gamePackResolver));
		modelFactory.setGamePackPlayerModel(new GamePackPlayerModel(TEST_USER));
	}

	public GamePackResolver getGamePackResolver() {
		return new AndroidGamePackResolver(context,
				modelFactory.getServerCommunicationManager());
	}
}
