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

public class BaseGamePointsTracker implements GamePackPointsTracker {

	protected final ModelFactory modelFactory;

	protected final VendorStrategy strategy;

	public BaseGamePointsTracker(ModelFactory modelFactory,
			VendorStrategy strategy) {
		this.modelFactory = modelFactory;
		this.strategy = strategy;
	}

	public void init() {
		strategy.init();
	}

	public void getNumberOfPoints() {
		strategy.getNumberOfPoints();
	}

	public void spendPoints(int points) {
		strategy.spendPoints(points);
	}

	public void getUpdatePoints(String currencyName, final int pointTotal) {
		setPointsOnModel(pointTotal);
	}

	public void getUpdatePointsFailed(String error) {
		setPointsOnModel(POINTS_ON_ERROR);
	}

	public void getSpendPointsResponse(String currencyName, final int pointTotal) {
		setPointsOnModel(pointTotal);
	}

	public void getSpendPointsResponseFailed(String error) {
		getUpdatePointsFailed(error);
	}

	public void earnedTapPoints(final int amount) {
		setAdditionalPointsOnModel(amount);
	}

	public void earnPoints() {
		strategy.earnPoints();
	}

	private void setAdditionalPointsOnModel(final int delta) {
		final int totalPointsToSet = modelFactory.getGamePackListModel()
				.getPoints() + delta;
		setPointsOnModel(totalPointsToSet);
	}

	private void setPointsOnModel(final int pointsToSet) {
		Runnable updateModel = new Runnable() {
			public void run() {
				modelFactory.getGamePackListModel().setPoints(pointsToSet);
			}
		};
		strategy.invokeRunnable(updateModel);
	}

	public static interface VendorStrategy {
		void init();

		void earnPoints();

		void invokeRunnable(Runnable runnable);

		void spendPoints(int points);

		void getNumberOfPoints();
	}

}
