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
package com.jdom.word.playdough.presenter;

import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jdom.word.playdough.model.GamePackListModel;
import com.jdom.word.playdough.presenter.GamePackListPresenter;
import com.jdom.word.playdough.presenter.GamePackListView;

@RunWith(PowerMockRunner.class)
public class GamePackListPresenterTest {

	@Mock
	private GamePackListModel mockModel;

	@Mock
	private GamePackListView mockView;

	private GamePackListPresenter presenter;

	@Before
	public void setUp() {
		presenter = new GamePackListPresenter(mockModel, mockView);
	}

	@Test
	public void testSaveStateAndExitWillNullReferenceToView() {
		presenter.saveStateAndExit();

		assertNull(
				"The presenter should have nulled its reference to the view!",
				presenter.view);
	}

	@Test
	public void testSaveStateAndExitWillUnregisterFromModel() {
		presenter.saveStateAndExit();

		Mockito.verify(mockModel).unregisterObserver(presenter);
	}

	@Test
	public void testLoadStateWillRegisterAsObserverOfModel() throws IOException {
		presenter.loadState();

		Mockito.verify(mockModel).registerObserver(presenter);
	}

	@Test
	public void testLoadStateWillLoadCurrentPoints() throws IOException {
		presenter.loadState();

		Mockito.verify(mockModel).checkPoints();
	}

	@Test
	public void testLoadStateWillCallInitOnModel() throws IOException {
		presenter.loadState();

		Mockito.verify(mockModel).init();
	}
}
