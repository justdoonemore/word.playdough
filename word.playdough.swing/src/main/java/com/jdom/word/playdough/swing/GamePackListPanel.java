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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.jdom.util.Observer;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.GamePackListModel;

public class GamePackListPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 3849013324172479807L;
	private final GamePackListModel model;

	public GamePackListPanel(GamePackListModel model) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.model = model;
	}

	public void update(Subject subject) {
		this.removeAll();
		this.add(new ListPanel(this.model));

		setVisible(this.model.isShowing());
	}

	public class ListPanel extends JPanel {
		private static final long serialVersionUID = 8455502255968239517L;

		public ListPanel(final GamePackListModel model) {
			for (final String gamePackFile : model.getAvailableGamePackFiles()) {
				final boolean locked = model.isGamePackLocked(gamePackFile);
				String display = locked ? gamePackFile + "(locked}"
						: gamePackFile;
				JButton button = new JButton(display);
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						model.playGamePack(gamePackFile);
					}
				});
				this.add(button);
			}
		}
	}

}
