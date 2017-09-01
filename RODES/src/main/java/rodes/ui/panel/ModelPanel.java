package rodes.ui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")


public class ModelPanel extends AbstractTabPanel{

	private final String MODEL_MESSAGE 		= "Parametric CTMC model:";
	private final String PROPERTIES_MESSAGE = "CSL properties:";

	
	public ModelPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(0, 1, 2);
		if (frame == null)
			throw new NullPointerException();
		
		this.parent		= tab;
		this.properties = props;
		
		setLayout(null);
		setPreferredSize(new Dimension(600, 200));
		setBorder(null);
		
		
		//model
		JLabel 		modelLabel 	  	= new JLabel(MODEL_MESSAGE);
		modelLabel.setBounds(10, 10, 150, 40);

		JTextField  modelTextfield	= new JTextField("model");
		modelTextfield.setBounds(170, 10, 220, 40);
		modelTextfield.setEditable(false);
		
		JButton modelButton	= new JButton("Select model");
		modelButton.setBounds(410, 10, 170, 40);
		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFileChooser(modelButton, modelTextfield, "CTMC model", new String[]{"sm"}, null);
			}
		});
		
		add(modelLabel);
		add(modelTextfield);
		add(modelButton);
		
		
		//properties
		JLabel 		propertiesLabel 	= new JLabel(PROPERTIES_MESSAGE);
		propertiesLabel.setBounds(10, 50, 150, 40);
		
		JTextField  propertiesTextfield	= new JTextField("properties");
		propertiesTextfield.setBounds(170, 50, 220, 40);
		propertiesTextfield.setEditable(false);
		
		JButton propertiesButton	= new JButton("Select CSL properties");
		propertiesButton.setBounds(410, 50, 170, 40);
		propertiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFileChooser(propertiesButton, propertiesTextfield, "CSL properties", new String[]{"csl"}, null);
			}
		});
		
		
		add(propertiesLabel);
		add(propertiesTextfield);
		add(propertiesButton);
		
		

		setVisible(true);
	}	
	


	@Override
	protected void checkInputs() {
		
	}
}
