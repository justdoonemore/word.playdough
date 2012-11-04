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
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jdom.word.playdough.model.GamePackPlayerModel;
import com.jdom.word.playdough.model.Jumble;
import com.jdom.word.playdough.model.ModelFactory;

public class ContentPanel extends JPanel {
	private static final long serialVersionUID = -5848541969674412462L;

	ContentPanel(final ModelFactory modelFactory) {
		final GamePackPlayerModel model = modelFactory.getGamePackPlayerModel();
		final Jumble jumble = model.getJumble();
		if (jumble == null) {
			return;
		}

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel messagesPanel = new JPanel();
		JLabel label = new JLabel();
		String labelText = "";
		for (String message : jumble.getMessages()) {
			if (labelText.length() != 0) {
				labelText += "<br>";
			}
			labelText += message;
		}
		label.setText("<html>" + labelText + "</html>");
		messagesPanel.add(label);
		this.add(messagesPanel);

		JPanel sourceWordPanel = new JPanel();
		sourceWordPanel.add(new JLabel(jumble.getSourceWord()));
		this.add(sourceWordPanel);

		JPanel runningWordPanel = new JPanel();
		runningWordPanel.add(new JLabel(jumble.getRunningWord()));
		this.add(runningWordPanel);

		JPanel usableLettersPanel = new JPanel();
		List<Character> sourceLetters = jumble.getSourceLetters();
		for (int i = 0; i < sourceLetters.size(); i++) {
			final Character letter = sourceLetters.get(i);
			JButton button = new JButton("" + letter);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jumble.addLetterToRunningWord(letter);
				}
			});
			usableLettersPanel.add(button);
		}
		this.add(usableLettersPanel);

		JPanel playButtonsPanel = new JPanel();
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumble.clearRunningWord();
			}
		});
		playButtonsPanel.add(clearButton);

		JButton shuffleButton = new JButton("Shuffle");
		shuffleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumble.shuffleLetters();
			}
		});
		playButtonsPanel.add(shuffleButton);

		JButton submitWordButton = new JButton("Submit Word");
		submitWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumble.proposeWord();
			}
		});

		playButtonsPanel.add(submitWordButton);
		this.add(playButtonsPanel);

		JPanel controlPanel = new JPanel();
		JButton nextWordButton = new JButton("Next Word");
		nextWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumble.nextWord();
			}
		});
		controlPanel.add(nextWordButton);

		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setShowing(false);
				modelFactory.getGamePackListModel().setShowing(true);
			}
		});
		controlPanel.add(doneButton);
		this.add(controlPanel);
	}
}
