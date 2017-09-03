package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")
public class SynthesiserPanel extends AbstractTabPanel{
		
	/** Algorithms list*/
	JComboBox<String> list; 
	
	/** Population text field*/
	JTextField  populationTextfield;
	
	/** Evaluations text field*/
	JTextField  evaluationsTextfield;
	
	/** Tolerance text field*/
//	JTextField  toleranceTextfield;

	/** Epsilon text field*/
//	JTextField  epsilonTextfield;

	/** JVM text field*/
	JTextField  JVMTextField;
	
	/** Processors text field*/
	JTextField  processorsTextField;
	
	/** Port text field*/
	JTextField portTextField;			

	
	public SynthesiserPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(-1,0, 1);
		if (frame == null)
			throw new NullPointerException();
		
		ImageIcon qmIcon = null;
		try {
			qmIcon = new ImageIcon(ImageIO.read(getClass().getResource("/img/qm24.png")));
		} 
		catch (IOException e2) {
			e2.printStackTrace();
		}
		
		this.parent		= tab;
		this.properties	= props;

		setLayout(null);
		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		
		//algorithm
		JLabel algorithmLabel 	= new JLabel("Algorithm:");
		algorithmLabel.setBounds(10, 10, 100, 40);
		algorithmLabel.setToolTipText("Select the evolutionary algorithm for synthesis");
		algorithmLabel.setIcon(qmIcon);
		algorithmLabel.setHorizontalTextPosition(SwingConstants.LEFT);

		list = new JComboBox<>();// (new String[]{"NSGAII", "SPEA2", "MOCell"});
		list.addItem("NSGAII");
		list.addItem("SPEA2");
		list.addItem("MOCell");
		
		list.setBounds(170, 10, 220, 40);
		setLayout(null);

		add(algorithmLabel);
		add(list);

		
		//population
		JLabel populationLabel 			= new JLabel ("Population:");
		populationLabel.setBounds(10, 50, 150, 40);
		populationLabel.setIcon(qmIcon);
		populationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		populationLabel.setToolTipText("Specify the desired population size");

		populationTextfield	= new JTextField("");
		populationTextfield.setBounds(170, 50, 220, 40);
		populationTextfield.setEditable(true);
		
		add(populationLabel);
		add(populationTextfield);

		
		//evaluations
		JLabel evlauationsLabel 			= new JLabel("Evaluations:");
		evlauationsLabel.setBounds(10, 90, 150, 40);
		evlauationsLabel.setIcon(qmIcon);
		evlauationsLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		evlauationsLabel.setToolTipText("Specify the desired number of total evaluations");
		
		evaluationsTextfield	= new JTextField("");
		evaluationsTextfield.setBounds(170, 90, 220, 40);
		evaluationsTextfield.setEditable(true);
		
		add(evlauationsLabel);
		add(evaluationsTextfield);

		
//		//tolerance
//		JLabel toleranceLabel 			= new JLabel("Tolerance:");
//		toleranceLabel.setBounds(10, 130, 150, 40);
//		toleranceLabel.setIcon(qmIcon);
//		toleranceLabel.setHorizontalTextPosition(SwingConstants.LEFT);
//		toleranceLabel.setToolTipText("Specify the desired tolerance level");
//		
//		toleranceTextfield	= new JTextField("");
//		toleranceTextfield.setBounds(170, 130, 220, 40);
//		toleranceTextfield.setEditable(true);
//		
//		add(toleranceLabel);
//		add(toleranceTextfield);
//
//		
//		//epsilon
//		JLabel epsilonLabel 			= new JLabel("Epsilon:");
//		epsilonLabel.setBounds(10, 170, 150, 40);
//		epsilonLabel.setIcon(qmIcon);
//		epsilonLabel.setHorizontalTextPosition(SwingConstants.LEFT);
//		epsilonLabel.setToolTipText("Specify the desired epsilon (leniency) value");
//		
//		epsilonTextfield	= new JTextField("");
//		epsilonTextfield.setBounds(170, 170, 220, 40);
//		epsilonTextfield.setEditable(true);
//		
//		add(epsilonLabel);
//		add(epsilonTextfield);

		
		//JVM
		JLabel JVMLabel 			= new JLabel("JVM:");
		JVMLabel.setBounds(10, 210, 150, 40);
		JVMLabel.setIcon(qmIcon);
		JVMLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		JVMLabel.setToolTipText("Select the java executable on this machine");
		
		JVMTextField	= new JTextField("");
		JVMTextField.setBounds(170, 210, 220, 40);
		JVMTextField.setEditable(false);
		
		JButton JVMButton	= new JButton("Select JVM");
		JVMButton.setBounds(410, 210, 170, 40);
		JVMButton.setHorizontalAlignment (SwingConstants.LEFT);
		JVMButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String javaDir = File.separator + "usr" + File.separator + "bin";
				showFileChooser(JVMButton, JVMTextField, "JVM", new String[]{"exe,jpg"}, javaDir);
			}
		});				
		
		add(JVMLabel);
		add(JVMTextField);
		add(JVMButton);

		//processors
		JLabel processorsLabel 			= new JLabel("Processors:");
		processorsLabel.setBounds(10, 250, 150, 40);
		processorsLabel.setIcon(qmIcon);
		processorsLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		processorsLabel.setToolTipText("Specify the number of parallel executions");
		
		processorsTextField	= new JTextField("");
		processorsTextField.setBounds(170, 250, 220, 40);
		processorsTextField.setEditable(true);
		
		add(processorsLabel);
		add(processorsTextField);
		
		
		//Port
		JLabel portLabel = new JLabel("Starting port:");
		portLabel.setBounds(10, 290, 150, 40);
		portLabel.setIcon(qmIcon);
		portLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		portLabel.setToolTipText("Specify the starting port number");

		portTextField = new JTextField();
		portTextField.setBounds(170, 290, 220, 40);
		portTextField.setEditable(true);
		
		add(portLabel);
		add(portTextField);

		
		setVisible(true);
		
		list.setSelectedIndex(0);
		populationTextfield.setText("50");
		evaluationsTextfield.setText("1000");
//		toleranceTextfield.setText("0.1");
//		epsilonTextfield.setText("0.2");
		JVMTextField.setText("/usr/bin/java");
		processorsTextField.setText("1");
		portTextField.setText("8880");
		
//		JButton button = new JButton();
//		button.setHorizontalAlignment(SwingConstants.CENTER);
//		button.setBounds(80, 21, 50, 22);
//		try {
//			Image img = ImageIO.read(getClass().getResource("/img/qm24.png"));
//		    button.setIcon(new ImageIcon(img));
//		    button.setBorder(new CompoundBorder());
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String javaDir = File.separator + "usr" + File.separator + "bin";
//				showFileChooser(JVMButton, JVMTextField, "JVM", new String[]{"exe,jpg"}, javaDir);
//			}
//		});	
//		add(button);
		
	}	
	
	
	
	@Override
	protected void checkInputs(){
		StringBuilder errors = new StringBuilder();
		
		//check algorithm
		properties.put(Constants.ALGORITHM_KEYWORD, list.getSelectedItem().toString());
		
		//check population
		String population = populationTextfield.getText();
		if (population.isEmpty() || !population.matches(Constants.INTEGER_REGEX)) {
			errors.append("Incorrect population: " + population +"\n");
			properties.put(Constants.POPULATION_SIZE_KEYWORD, null);
		}
		else
			properties.put(Constants.POPULATION_SIZE_KEYWORD,		population);

		//check evaluations
		String evaluations = evaluationsTextfield.getText();
		if (evaluations.isEmpty() || !evaluations.matches(Constants.INTEGER_REGEX)) {
			errors.append("Incorrect evaluations: " + evaluations +"\n");
			properties.put(Constants.MAX_EVALUATIONS_KEYWORD,		null);
		}
		else
			properties.put(Constants.MAX_EVALUATIONS_KEYWORD, 	evaluations);

//		//check tolerance
//		String tolerance = toleranceTextfield.getText();
//		if (tolerance.isEmpty() || !tolerance.matches(DOUBLE_REGEX)) {
//			errors.append("Incorrect tolerance: " + tolerance +"\n");
//			properties.put(Constants.TOLERANCE_KEYWORD, 		null);
//		}
//		else
//			properties.put(Constants.TOLERANCE_KEYWORD, 		tolerance);
//
//		//check epsilon
//		String epsilon = epsilonTextfield.getText();
//		if (epsilon.isEmpty() || !epsilon.matches(DOUBLE_REGEX)) {
//			errors.append("Incorrect epsilon: " + epsilon +"\n");
//			properties.put(Constants.EPSILON_KEYWORD, 		null);
//		}
//		else
//			properties.put(Constants.EPSILON_KEYWORD, 		epsilon);

		//check JVM
		String jvm = JVMTextField.getText();
		if (jvm.isEmpty()) {
			errors.append("Incorrect JVM location: " + jvm +"\n");
			properties.put(Constants.JVM_KEYWORD, 		null);
		}
		else
			properties.put(Constants.JVM_KEYWORD, 		jvm);
		
		//check evaluations
		String processors = processorsTextField.getText();
		if (processors.isEmpty() || !processors.matches(Constants.INTEGER_REGEX)) {
			errors.append("Incorrect processors: " + processors +"\n");
			properties.put(Constants.PROCESSORS_KEYWORD,		null);
		}
		else
			properties.put(Constants.PROCESSORS_KEYWORD, 	processors);

		//check port
		String port = portTextField.getText();
		if (port.isEmpty() || !port.matches(Constants.INTEGER_REGEX)) {
			errors.append("Incorrect port number: " + port +"\n");
			properties.put(Constants.INITIAL_PORT_KEYWORD,		null);
		}
		else
			properties.put(Constants.INITIAL_PORT_KEYWORD, 	port);

		properties.put("ERRORS", errors);
	}
}