package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")


public class ModelPanel extends AbstractTabPanel{

	/** Problem textfield */
	private JTextField problemTextField;
	
	/** Model textarea */
	private JTextArea  modelTextArea;
	
	/** Properties textarea*/
	private JTextArea  propertiesTextArea;

	/** Tolerance text field*/
	private JTextField  toleranceTextfield;

	/** Epsilon text field*/
	private JTextField  epsilonTextfield;

	/** Sensitivity checkbox*/
	private JCheckBox sensitivityCheckBox;
	
	private final String MODEL_MESSAGE 		= "CTMC model:";
	private final String PROPERTIES_MESSAGE 	= "CSL properties:";

	
	
	
	public ModelPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, 0, 1, 2);
		if (frame == null)
			throw new NullPointerException();
		
		this.parent		= tab;
		this.properties = props;
		
		setLayout(null);
		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		
		
		//problem
		JLabel label = new JLabel("Problem:");
		label.setBounds(10, 12, 150, 40);
		add(label);
		
		problemTextField = new JTextField();
		problemTextField.setBounds(116, 12, 270, 40);
		problemTextField.setEditable(true);
		add(problemTextField);
		
		
		//model
		JLabel 		modelLabel 	  	= new JLabel(MODEL_MESSAGE);
		modelLabel.setBounds(10, 64, 150, 40);

		modelTextArea	= new JTextArea(2, 20);
		modelTextArea.setLineWrap(true);
		modelTextArea.setBounds(116, 64, 270, 40);
		modelTextArea.setEditable(false);
		
		JButton modelButton	= new JButton("Select model");
		modelButton.setBounds(406, 64, 170, 40);
		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser(modelButton, modelTextArea, "CTMC model", new String[]{"sm"}, null);
			}
		});
		
		add(modelLabel);
		add(modelTextArea);
		add(modelButton);
		
		
		//properties
		JLabel 		propertiesLabel 	= new JLabel(PROPERTIES_MESSAGE);
		propertiesLabel.setBounds(10, 116, 150, 40);
		
		propertiesTextArea	= new JTextArea(2,50);
		propertiesTextArea.setLineWrap(true);
		propertiesTextArea.setBounds(115, 116, 270, 40);
		propertiesTextArea.setEditable(false);
		
		JButton propertiesButton	= new JButton("Select CSL properties");
		propertiesButton.setBounds(406, 116, 170, 40);
		propertiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser(propertiesButton, propertiesTextArea, "CSL properties", new String[]{"csl"}, null);
			}
		});
		
		add(propertiesLabel);
		add(propertiesTextArea);
		add(propertiesButton);
		
		
		//tolerance
		JLabel toleranceLabel 			= new JLabel("Tolerance:");
		toleranceLabel.setBounds(10, 184, 150, 40);
		toleranceLabel.setIcon(qmIcon);
		toleranceLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		toleranceLabel.setToolTipText("Specify the desired tolerance level");
		
		toleranceTextfield	= new JTextField("");
		toleranceTextfield.setBounds(116, 184, 220, 40);
		toleranceTextfield.setEditable(true);
		
		add(toleranceLabel);
		add(toleranceTextfield);

		
		//epsilon
		JLabel epsilonLabel 			= new JLabel("Epsilon:");
		epsilonLabel.setBounds(10, 224, 150, 40);
		epsilonLabel.setIcon(qmIcon);
		epsilonLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		epsilonLabel.setToolTipText("Specify the desired epsilon (leniency) value");
		
		epsilonTextfield	= new JTextField("");
		epsilonTextfield.setBounds(116, 224, 220, 40);
		epsilonTextfield.setEditable(true);
		
		add(epsilonLabel);
		add(epsilonTextfield);
		
		sensitivityCheckBox = new JCheckBox("Use sensitivity?  ", true);
		sensitivityCheckBox.setBounds(10, 264, 220, 40);
		sensitivityCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
		add(sensitivityCheckBox);
		
		problemTextField.setText("Google");
		modelTextArea.setText("/Users/sgerasimou/Documents/Git/RODES/RODES/models/Google/googleTemplate.sm");
		propertiesTextArea.setText("/Users/sgerasimou/Documents/Git/RODES/RODES/models/Google/google.csl");
		toleranceTextfield.setText("0.1");
		epsilonTextfield.setText("0.2");
				
		setVisible(true);
	}	
	


	@Override
	protected void checkInputs() {
		StringBuilder errors = new StringBuilder();

		//check problem
		String problem = problemTextField.getText();
		if (problem.isEmpty()) {
			errors.append("Incorrect problem name: " + problem +"\n");
			properties.put(Constants.PROBLEM_KEYWORD, 		null);
		}
		else
			properties.put(Constants.PROBLEM_KEYWORD, 		problem);

		
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

		//check tolerance
		String tolerance = toleranceTextfield.getText();
		if (tolerance.isEmpty() || !tolerance.matches(Constants.DOUBLE_REGEX)) {
			errors.append("Incorrect tolerance: " + tolerance +"\n");
			properties.put(Constants.TOLERANCE_KEYWORD, 		null);
		}
		else
			properties.put(Constants.TOLERANCE_KEYWORD, 		tolerance);

		//check epsilon
		String epsilon = epsilonTextfield.getText();
		if (epsilon.isEmpty() || !epsilon.matches(Constants.DOUBLE_REGEX)) {
			errors.append("Incorrect epsilon: " + epsilon +"\n");
			properties.put(Constants.EPSILON_KEYWORD, 		null);
		}
		else
			properties.put(Constants.EPSILON_KEYWORD, 		epsilon);

		//check sensitivity
		properties.put(Constants.SENSITIVITY_KEYWORD, sensitivityCheckBox.isSelected());

		
		properties.put("ERRORS", errors);
	}



	@Override
	public void reDraw() {
	}
}
