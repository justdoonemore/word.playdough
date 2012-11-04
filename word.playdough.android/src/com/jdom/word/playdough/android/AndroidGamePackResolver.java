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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import android.content.Context;


import com.jdom.word.playdough.model.ServerCommunicationManager;
import com.jdom.word.playdough.model.ServerGamePackResolver;

public class AndroidGamePackResolver extends ServerGamePackResolver {

	static int ID_OF_GAME_PACK_LIST = R.raw.game_pack_list;

	private final Context context;

	public AndroidGamePackResolver(Context context,
			ServerCommunicationManager communicationManager) {
		super(null, communicationManager);

		this.context = context;
	}

	@Override
	protected boolean isFilenameAcceptable(String gamePackFileName) {
		return !"1".equals(gamePackFileName);
	}

	@Override
	public String getGamePackContents(String gamePackFileName) {
		String name = getGamePackPrefix() + gamePackFileName;

		InputStream is = null;
		try {
			is = context.getAssets().open(name);
			return IOUtils.toString(is);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	protected String getGamePackPrefix() {
		return "game_pack_";
	}

	@Override
	public SortedSet<String> getGamePackNames() {
		InputStream openRawResource = null;
		String gamePackList = "";
		try {
			openRawResource = context.getResources().openRawResource(
					ID_OF_GAME_PACK_LIST);
			gamePackList = IOUtils.toString(openRawResource);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(openRawResource);
		}
		String[] gamePacks = StringUtils.split(gamePackList, '\n');

		TreeSet<String> treeSet = new TreeSet<String>(new Comparator<String>() {
			public int compare(String arg0, String arg1) {
				return new Integer(arg0).compareTo(new Integer(arg1));
			}
		});
		treeSet.addAll(Arrays.asList(gamePacks));
		return treeSet;
	}
}
