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

import java.util.ArrayList;
import java.util.Collection;

import com.jdom.util.Observer;
import com.jdom.util.Subject;

public class GamePackPlayerModel implements Subject, Observer {

	private final Collection<Observer> observers = new ArrayList<Observer>();

	private Jumble jumble;

	private boolean showing;

	private final String username;

	public GamePackPlayerModel(String username) {
		this.username = username;
	}

	public void update(Subject subject) {
		// So if this model is notified via its Jumble then it notifies anyone
		// listening
		updateObservers();
	}

	public void registerObserver(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}

		observer.update(this);
	}

	public void unregisterObserver(Observer observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	private void updateObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}

	public Jumble getJumble() {
		return jumble;
	}

	public void setJumble(Jumble jumble) {
		this.jumble = jumble;
		this.jumble.registerObserver(this);

		this.setShowing(true);
	}

	public boolean isShowing() {
		return showing;
	}

	public void setShowing(boolean showing) {
		this.showing = showing;

		updateObservers();
	}

}
