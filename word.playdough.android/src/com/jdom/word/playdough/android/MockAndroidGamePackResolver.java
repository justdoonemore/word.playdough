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

import com.jdom.word.playdough.model.ServerCommunicationManager;

public class MockAndroidGamePackResolver extends AndroidGamePackResolver {

	public MockAndroidGamePackResolver(Context context,
			ServerCommunicationManager serverCommunicationManager) {
		super(context, serverCommunicationManager);
	}

	@Override
	protected String getGamePackPrefix() {
		return "test_" + super.getGamePackPrefix();
	}
}
