package rodes.ui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import evochecker.auxiliary.StringProperties;

public abstract class AbstractTabPanel extends JPanel {
	/** Parent tab pane*/
	Component parent;

	/** String properties*/
	StringProperties properties;

	/** Next and previous buttons*/
	JButton nextButton;
	JButton previousButton;
	
	
	public AbstractTabPanel(int previous, int current, int next) {
		initButtons(previous, current, next);
	}
	
	
	private void initButtons(int previous, int current, int next) {
		if (previous != -1) {
			previousButton = new JButton("Previous");
			previousButton.setBounds(10, 340, 170, 40);
			previousButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JTabbedPane)parent).setEnabledAt(previous, true);
					((JTabbedPane)parent).setEnabledAt(current, false);
					((JTabbedPane)parent).setSelectedIndex(previous);
				}
			});
			add(previousButton);
		}
		
		if (next != -1) {
			nextButton = new JButton("Next");
			nextButton.setBounds(410, 340, 170, 40);
			nextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					checkInputs();
					String errors = properties.get("ERRORS").toString(); 
					if (!errors.isEmpty())
						JOptionPane.showMessageDialog(parent, errors, "Configuration errors", JOptionPane.ERROR_MESSAGE);
					else {
						((JTabbedPane)parent).setEnabledAt(current, false);
						((JTabbedPane)parent).setEnabledAt(next, true);
						((JTabbedPane)parent).setSelectedIndex(next);
					}
					System.out.println(properties.toString());
				}
			});
			add(nextButton);
		}
	}
	
	
	protected abstract void checkInputs();
}
