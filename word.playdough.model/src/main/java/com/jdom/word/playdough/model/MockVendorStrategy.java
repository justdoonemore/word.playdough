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

import com.jdom.word.playdough.model.BaseGamePointsTracker.VendorStrategy;

public class MockVendorStrategy implements VendorStrategy {
	private int numberOfPoints = 50;
	private final ModelFactory modelFactory;

	protected MockVendorStrategy(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	public void init() {
		// Nothing required
	}

	public void invokeRunnable(Runnable runnable) {
		runnable.run();
	}

	public void spendPoints(int points) {
		numberOfPoints -= points;
	}

	public void getNumberOfPoints() {
		invokeRunnable(new Runnable() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// Nothing to do
				}
				modelFactory.getGamePackListModel().setPoints(numberOfPoints);
			}
		});
	}

	public void earnPoints() {
		try {
			Thread.sleep(50);
			numberOfPoints += 100;
		} catch (InterruptedException e) {
			// Nothing to do
		}
		modelFactory.getGamePackListModel().setPoints(numberOfPoints);
	}
}
