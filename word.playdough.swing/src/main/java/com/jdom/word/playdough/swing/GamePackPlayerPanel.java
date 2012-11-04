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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.jdom.util.Observer;
import com.jdom.util.Subject;
import com.jdom.word.playdough.model.ModelFactory;

public class GamePackPlayerPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1540254454746555153L;
	private final ModelFactory modelFactory;

	public GamePackPlayerPanel(ModelFactory modelFactory) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.modelFactory = modelFactory;
	}

	public void update(Subject subject) {
		this.removeAll();
		this.add(new ContentPanel(this.modelFactory));
		setVisible(this.modelFactory.getGamePackPlayerModel().isShowing());
	}

}
