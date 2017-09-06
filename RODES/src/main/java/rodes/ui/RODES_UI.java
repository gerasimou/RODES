package rodes.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import evochecker.auxiliary.StringProperties;
import rodes.ui.panel.AbstractTabPanel;
import rodes.ui.panel.ConfigurationPanel;
import rodes.ui.panel.ExecutionPanel;
import rodes.ui.panel.ModelPanel;
import rodes.ui.panel.SynthesiserPanel;
import rodes.ui.panel.VisualisationPanel;

public class RODES_UI {

	/** The frame*/
	private JFrame frame;
	
	/** Tabbed pand*/
	private JTabbedPane tab;
	
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
		
		//Create Tab
		tab 										= new JTabbedPane();
		SynthesiserPanel		synthesiserPanel		= new SynthesiserPanel(frame, tab, properties);
		ModelPanel 			modelPanel 			= new ModelPanel(frame, tab, properties);
		ConfigurationPanel  configurationPanel  	= new ConfigurationPanel(frame, tab, properties);
		ExecutionPanel  		executionPanel  		= new ExecutionPanel(frame, tab, properties);
		VisualisationPanel  	visualisationPanel  	= new VisualisationPanel(frame, tab, properties);
		
		tab.addTab("Algorithm"			, synthesiserPanel);
		tab.addTab("Model and Properties", modelPanel);
		tab.addTab("Configuration"		, configurationPanel);
		tab.addTab("Execution"			, executionPanel);
		tab.addTab("Visualisation"		, visualisationPanel);
		
		init();
		
		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
	            System.err.println("Tab: " + tab. getSelectedIndex());
	            
	            Component selectedComponent = ((JTabbedPane)e.getSource()).getSelectedComponent(); 
	            if (selectedComponent instanceof AbstractTabPanel)
	            		((AbstractTabPanel)selectedComponent).reDraw();
	            frame.repaint();
	            frame.revalidate();
			}
		});
		
		frame.getContentPane().add(tab);

		//Create the menu bar.
		MenuBar menuBar = new MenuBar(frame, tab);
		frame.setJMenuBar(menuBar);

		
		//Display the window.
        frame.pack();
        frame.setVisible(true);		
	}
	
	protected void init() {
		tab.setEnabledAt(0, true);
		tab.setEnabledAt(1, false);
		tab.setEnabledAt(2, false);
		tab.setEnabledAt(3, false);
		tab.setEnabledAt(4, false);
	}
}

