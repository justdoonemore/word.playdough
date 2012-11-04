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

import android.app.Application;

import com.jdom.word.playdough.model.GamePackListModel;
import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.GamePackResolver;
import com.jdom.word.playdough.model.ModelFactory;

public class WordPlaydoughApplication extends Application {

	static final boolean TEST_MODE = Boolean.getBoolean("test.mode");

	private final ModelFactory modelFactory = new ModelFactory();

	private ApplicationFactory applicationFactory;

	@Override
	public void onCreate() {
		super.onCreate();

		applicationFactory = TEST_MODE ? new TestApplicationFactoryImpl(this,
				modelFactory) : new ApplicationFactoryImpl(this, modelFactory);
		applicationFactory.init();

		onCreate(applicationFactory.getGamePackResolver());
	}

	void onCreate(GamePackResolver gamePackResolver) {
		applicationFactory.onCreate(gamePackResolver);
	}

	public GamePackListModel getGamePackListModel() {
		return modelFactory.getGamePackListModel();
	}

	public GamePackPlayerModel getGamePackPlayerModel() {
		return modelFactory.getGamePackPlayerModel();
	}

	public ModelFactory getModelFactory() {
		return modelFactory;
	}
}
