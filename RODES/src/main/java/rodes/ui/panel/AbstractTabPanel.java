package rodes.ui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	
	protected JFileChooser createFileChooser(Component parent, JTextField host, String description, String[] extension, String currentDir){	
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);// "JPG & GIF Images", new String[]{"jpg", "gif"});
	    chooser.setFileFilter(filter);
	    if (currentDir == null)
    			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));//System.getProperty("user.home")));
	    else
	    		chooser.setCurrentDirectory(new File(currentDir));
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    		host.setText(chooser.getSelectedFile().getAbsolutePath());
//	       System.out.println("You chose to open this file: " +
//	            
	    }
	    return chooser;		
	}
	
	protected abstract void checkInputs();
}
