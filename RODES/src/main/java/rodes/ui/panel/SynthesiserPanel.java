package rodes.ui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	JTextField  toleranceTextfield;

	/** Epsilon text field*/
	JTextField  epsilonTextfield;

	/** JVM text field*/
	JTextField  JVMTextField;
	
	/** Processors text field*/
	JTextField  processorsTextField;
	
	/** Various REGEX*/
	private final String INTEGER_REGEX = "^\\d+$";
	private final String DOUBLE_REGEX  = "[0-9]+(.[0-9]+)?";
			

	
	public SynthesiserPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(-1,0, 1);
		if (frame == null)
			throw new NullPointerException();
		
		this.parent		= tab;
		this.properties	= props;

		setLayout(null);
		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		
		//algorithm
		JLabel algorithmLabel 	= new JLabel("Algorithm:");
		algorithmLabel.setBounds(10, 10, 150, 40);

		list = new JComboBox<>();// (new String[]{"NSGAII", "SPEA2", "MOCell"});
		list.addItem("NSGAII");
		list.addItem("SPEA2");
		list.addItem("MOCell");
		
		list.setBounds(170, 10, 220, 40);
		setLayout(null);

		add(algorithmLabel);
		add(list);

		
		//population
		JLabel populationLabel 			= new JLabel("Population:");
		populationLabel.setBounds(10, 50, 150, 40);

		populationTextfield	= new JTextField("50");
		populationTextfield.setBounds(170, 50, 220, 40);
		populationTextfield.setEditable(true);
		
		add(populationLabel);
		add(populationTextfield);

		
		//evaluations
		JLabel evlauationsLabel 			= new JLabel("Evaluations:");
		evlauationsLabel.setBounds(10, 90, 150, 40);
		
		evaluationsTextfield	= new JTextField("1000");
		evaluationsTextfield.setBounds(170, 90, 220, 40);
		evaluationsTextfield.setEditable(true);
		
		add(evlauationsLabel);
		add(evaluationsTextfield);

		
		//tolerance
		JLabel toleranceLabel 			= new JLabel("Tolerance:");
		toleranceLabel.setBounds(10, 130, 150, 40);
		
		toleranceTextfield	= new JTextField("");
		toleranceTextfield.setBounds(170, 130, 220, 40);
		evaluationsTextfield.setEditable(true);
		
		add(toleranceLabel);
		add(toleranceTextfield);

		
		//epsilon
		JLabel epsilonLabel 			= new JLabel("Epsilon:");
		epsilonLabel.setBounds(10, 170, 150, 40);
		
		epsilonTextfield	= new JTextField("");
		epsilonTextfield.setBounds(170, 170, 220, 40);
		epsilonTextfield.setEditable(true);
		
		add(epsilonLabel);
		add(epsilonTextfield);

		
		//JVM
		JLabel JVMLabel 			= new JLabel("JVM:");
		JVMLabel.setBounds(10, 210, 150, 40);
		
		JVMTextField	= new JTextField("");
		JVMTextField.setBounds(170, 210, 220, 40);
		JVMTextField.setEditable(false);
		
		JButton JVMButton	= new JButton("Select JVM");
		JVMButton.setBounds(410, 210, 170, 40);
		JVMButton.setHorizontalAlignment (SwingConstants.LEFT);
		JVMButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String javaDir = File.separator + "usr" + File.separator + "bin";
				createFileChooser(JVMButton, JVMTextField, "JVM", new String[]{"exe,jpg"}, javaDir);
			}
		});				
		
		add(JVMLabel);
		add(JVMTextField);
		add(JVMButton);

		//processors
		JLabel processorsLabel 			= new JLabel("Processors:");
		processorsLabel.setBounds(10, 250, 150, 40);
		
		processorsTextField	= new JTextField("1");
		processorsTextField.setBounds(170, 250, 220, 40);
		processorsTextField.setEditable(true);
		
		add(processorsLabel);
		add(processorsTextField);

		
		setVisible(true);
	}	
	
	
	
	@Override
	protected void checkInputs(){
		StringBuilder errors = new StringBuilder();
		
		//check algorithm
		properties.put("ALGORITHM", 		list.getSelectedItem().toString());
		
		//check population
		String population = populationTextfield.getText();
		if (population.isEmpty() || !population.matches(INTEGER_REGEX)) {
			errors.append("Incorrect population: " + population +"\n");
			properties.put("POPULATION",		null);
		}
		else
			properties.put("POPULATION",		population);

		//check evaluations
		String evaluations = evaluationsTextfield.getText();
		if (evaluations.isEmpty() || !evaluations.matches(INTEGER_REGEX)) {
			errors.append("Incorrect evaluations: " + evaluations +"\n");
			properties.put("EVALUATIONS",		null);
		}
		else
			properties.put("EVALUATIONS", 	evaluations);

		//check tolerance
		String tolerance = toleranceTextfield.getText();
		if (tolerance.isEmpty() || !tolerance.matches(DOUBLE_REGEX)) {
			errors.append("Incorrect tolerance: " + tolerance +"\n");
			properties.put("TOLERANCE", 		null);
		}
		else
			properties.put("TOLERANCE", 		tolerance);

		//check epsilon
		String epsilon = epsilonTextfield.getText();
		if (epsilon.isEmpty() || !epsilon.matches(DOUBLE_REGEX)) {
			errors.append("Incorrect epsilon: " + epsilon +"\n");
			properties.put("EPSILON", 		null);
		}
		else
			properties.put("EPSILON", 		epsilon);

		//check JVM
		String jvm = JVMTextField.getText();
		if (jvm.isEmpty()) {
			errors.append("Incorrect JVM location: " + jvm +"\n");
			properties.put("JVM", 		null);
		}
		else
			properties.put("JVM", 		jvm);
		

		//check evaluations
		String processors = processorsTextField.getText();
		if (processors.isEmpty() || !processors.matches(INTEGER_REGEX)) {
			errors.append("Incorrect processors: " + processors +"\n");
			properties.put("PROCESSORS",		null);
		}
		else
			properties.put("PROCESSORS", 	processors);
		
		properties.put("ERRORS", errors);
	}
}