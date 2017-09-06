package rodes.ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.StringProperties;

public class RODES_UI {

	/** The frame*/
	private JFrame frame;
	
	/** Tabbed pane*/
	private RODESTabs tab;
	
	/** String properties*/
	private StringProperties properties;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RODES_UI window = new RODES_UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the application.
	 */
	public RODES_UI() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
//		OptionDialog.createTabbedPane(frame);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//init properties
		properties = new StringProperties();
		setDefautlProperties();
		
		//Create Tab
		tab = new RODESTabs(frame, properties);
		tab.initTabs();
		
		frame.getContentPane().add(tab);

		//Create the menu bar.
		MenuBar menuBar = new MenuBar(frame, tab);
		frame.setJMenuBar(menuBar);

		
		//Display the window.
        frame.pack();
        frame.setVisible(true);		
	}
	
	
	protected void setDefautlProperties() {
		properties.setProperty(Constants.POPULATION_SIZE_KEYWORD, "4");
		properties.setProperty(Constants.MAX_EVALUATIONS_KEYWORD, "12");
		properties.setProperty(Constants.INTERVAL_KEYWORD, "4");
		properties.setProperty(Constants.JVM_KEYWORD, "/usr/bin/java");
		properties.setProperty(Constants.PROCESSORS_KEYWORD, "1");
		properties.setProperty(Constants.INITIAL_PORT_KEYWORD, "8880");
		properties.setProperty(Constants.POPULATION_SIZE_KEYWORD, "5");
		properties.setProperty(Constants.PROBLEM_KEYWORD, "Google");
		properties.setProperty(Constants.MODEL_FILE_KEYWORD, "models/Google/googleTemplate.sm");
		properties.setProperty(Constants.PROPERTIES_FILE_KEYWORD, "models/Google/google.csl");
		properties.setProperty(Constants.TOLERANCE_KEYWORD, "0.1");
		properties.setProperty(Constants.EPSILON_KEYWORD, "0.05");
		properties.setProperty(Constants.SENSITIVITY_KEYWORD, "true");
	}
}

