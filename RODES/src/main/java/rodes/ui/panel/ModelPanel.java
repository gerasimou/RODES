package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")


public class ModelPanel extends AbstractTabPanel{

	/** Model textfield */
	JTextField  modelTextfield;
	
	/** Properties textfield*/
	JTextField  propertiesTextfield;
	
	private final String MODEL_MESSAGE 		= "CTMC model:";
	private final String PROPERTIES_MESSAGE 	= "CSL properties:";

	
	
	
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

		modelTextfield	= new JTextField("");
		modelTextfield.setBounds(170, 10, 220, 40);
		modelTextfield.setEditable(false);
		
		JButton modelButton	= new JButton("Select model");
		modelButton.setBounds(410, 10, 170, 40);
		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser(modelButton, modelTextfield, "CTMC model", new String[]{"sm"}, null);
			}
		});
		
		add(modelLabel);
		add(modelTextfield);
		add(modelButton);
		
		
		//properties
		JLabel 		propertiesLabel 	= new JLabel(PROPERTIES_MESSAGE);
		propertiesLabel.setBounds(10, 50, 150, 40);
		
		propertiesTextfield	= new JTextField("");
		propertiesTextfield.setBounds(170, 50, 220, 40);
		propertiesTextfield.setEditable(false);
		
		JButton propertiesButton	= new JButton("Select CSL properties");
		propertiesButton.setBounds(410, 50, 170, 40);
		propertiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser(propertiesButton, propertiesTextfield, "CSL properties", new String[]{"csl"}, null);
			}
		});
		
		add(propertiesLabel);
		add(propertiesTextfield);
		add(propertiesButton);
		
		modelTextfield.setText("/Users/sgerasimou/Documents/Git/RODES/RODES/models/Google/google.sm");
		propertiesTextfield.setText("/Users/sgerasimou/Documents/Git/RODES/RODES/models/Google/google.csl");
		setVisible(true);
	}	
	


	@Override
	protected void checkInputs() {
		StringBuilder errors = new StringBuilder();

		//check model
		String model = modelTextArea.getText();
		if (model.isEmpty()) {
			errors.append("Incorrect model: " + model +"\n");
			properties.put(Constants.MODEL_FILE_KEYWORD,		null);
		}
		else
			properties.put(Constants.MODEL_FILE_KEYWORD,		model);
		
		//check properties
		String props = propertiesTextArea.getText();
		if (props.isEmpty()) {
			errors.append("Incorrect properties: " + props +"\n");
			properties.put(Constants.PROPERTIES_FILE_KEYWORD,		null);
		}
		else
			properties.put(Constants.PROPERTIES_FILE_KEYWORD,		props);

		properties.put("ERRORS", errors);
	}
}
