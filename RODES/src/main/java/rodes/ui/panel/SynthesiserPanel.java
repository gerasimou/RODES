package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
	private JComboBox<String> list; 
	
	/** Population text field*/
	private JTextField  populationTextfield;
	
	/** Evaluations text field*/
	private JTextField  evaluationsTextfield;
	
	/** JVM text field*/
	private JTextField  JVMTextField;
	
	/** Processors text field*/
	private JTextField  processorsTextField;
	
	/** Port text field*/
	private JTextField portTextField;			
	
	/** Interval text field*/
	private JTextField intervalTextField;

	
	public SynthesiserPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, -1, 0, 1);
		if (frame == null)
			throw new NullPointerException();
				
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
		
		
		//Intervals		
		JLabel intervalLabel = new JLabel("Intervals:");
		intervalLabel.setToolTipText("Specify the desired interval for displaying/storing intermediate solutions");
		intervalLabel.setIcon(qmIcon);
		intervalLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		intervalLabel.setBounds(10, 130, 150, 40);
		add(intervalLabel);
		
		intervalTextField = new JTextField("");
		intervalTextField.setEditable(true);
		intervalTextField.setBounds(170, 130, 220, 40);
		add(intervalTextField);

		
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
		JVMButton.setBounds(410, 210, 150, 40);
		JVMButton.setHorizontalAlignment (SwingConstants.CENTER);
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
		
		//check interval
		String interval = intervalTextField.getText();
		if (interval.isEmpty() || !interval.matches(Constants.INTEGER_REGEX)) {
			errors.append("Incorrect interval number: " + interval +"\n");
			properties.put(Constants.INTERVAL_KEYWORD,		null);
		}
		else
			properties.put(Constants.INTERVAL_KEYWORD,		interval);

		properties.put("ERRORS", errors);
	}

	
	@Override
	public void reDraw() {}
	
	
	@Override
	public void init() {
		list.setSelectedIndex(0);
		populationTextfield.setText(properties.getProperty(Constants.POPULATION_SIZE_KEYWORD));
		evaluationsTextfield.setText(properties.getProperty(Constants.MAX_EVALUATIONS_KEYWORD));
		JVMTextField.setText(properties.getProperty(Constants.JVM_KEYWORD));
		processorsTextField.setText(properties.getProperty(Constants.PROCESSORS_KEYWORD));
		portTextField.setText(properties.getProperty(Constants.INITIAL_PORT_KEYWORD));
		intervalTextField.setText(properties.getProperty(Constants.INTERVAL_KEYWORD));
	}

}