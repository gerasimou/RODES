package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import evochecker.EvoChecker;
import evochecker.auxiliary.StringProperties;
import evochecker.auxiliary.Utility;

@SuppressWarnings("serial")
public class VisualisationPanel extends AbstractTabPanel{
	
	
	public VisualisationPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(1, 2, -1);
		if (frame == null)
			throw new NullPointerException();
		
		this.parent		= tab;
		this.properties = props;

		setLayout(null);
		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		setLayout(null);
		
		JButton JVMButton	= new JButton("Start execution");
		JVMButton.setBounds(6, 202, 170, 40);
		JVMButton.setHorizontalAlignment (SwingConstants.LEFT);
		JVMButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start Execution
				Utility.loadPropertiesInstance(props);
				EvoChecker.main(null);
			}
		});
		add(JVMButton);

		setVisible(true);
	}	
	

	@Override
	protected void checkInputs() {
		
	}
}