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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.jdom.word.playdough.presenter.GamePackListPresenter;
import com.jdom.word.playdough.presenter.GamePackListView;
import com.jdom.word.playdough.presenter.ListItemConfiguration;
import com.jdom.word.playdough.presenter.ViewHandler;

public class GamePackListActivity extends BaseGamePackActivity implements
		GamePackListView {

	private GamePackListPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public void onPause() {
		super.onPause();

		presenter.saveStateAndExit();
	}

	@Override
	public void onResume() {
		super.onResume();

		presenter = new GamePackListPresenter(getWordPlaydoughApplication()
				.getGamePackListModel(), this);
		presenter.loadState();
	}

	public void setGamePacks(
			final ListItemConfiguration[] listItemConfigurations) {
		final int length = listItemConfigurations.length;
		String[] items = new String[length];
		for (int i = 0; i < length; i++) {
			items[i] = listItemConfigurations[i].getGamePack();
		}

		ListView gamePackList = (ListView) this.findViewById(R.id.gamePackList);
		gamePackList.setAdapter(new GamePackListArrayAdapter(this,
				R.layout.list_item, items, listItemConfigurations));

		gamePackList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listItemConfigurations[position].getClickAction().run();
			}
		});
	}

	public ViewHandler getViewHandler() {
		return new AndroidViewHandler(this);
	}

	public void playGamePack(String gamePack) {
		Intent intent = new Intent(getApplicationContext(),
				GamePackPlayerActivity.class);
		startActivity(intent);
	}

	private class GamePackListArrayAdapter extends ArrayAdapter<String> {
		private final ListItemConfiguration[] listItemConfigurations;

		public GamePackListArrayAdapter(
				GamePackListActivity gamePackListActivity, int listItem,
				String[] items, ListItemConfiguration[] listItemConfigurations) {
			super(gamePackListActivity, listItem, items);

			this.listItemConfigurations = listItemConfigurations;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.list_item, parent, false);
			}

			// Get item
			String packName = getItem(position);

			// Set item text
			TextView textView = (TextView) row
					.findViewById(R.id.list_item_text);
			textView.setText(packName);

			final ImageView imageView = (ImageView) row
					.findViewById(R.id.list_item_icon);
			return row;
		}
	}
}