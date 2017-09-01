package rodes.ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import evochecker.auxiliary.StringProperties;
import rodes.ui.panel.ModelPanel;
import rodes.ui.panel.SynthesiserPanel;
import rodes.ui.panel.VisualisationPanel;

public class RODES_UI {

	/** The frame*/
	private JFrame frame;

	/** String properties*/
	StringProperties properties;


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
		
//		OptionDialog.createTabbedPane(frame);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//init properties
		properties = new StringProperties();
		
		JTabbedPane tab 							= new JTabbedPane();
		SynthesiserPanel	synthesiserPanel			= new SynthesiserPanel(frame, tab, properties);
		ModelPanel 			modelPanel 			= new ModelPanel(frame, tab, properties);
		VisualisationPanel  visualisationPanel  	= new VisualisationPanel(frame, tab, properties); 
		
		tab.addTab("Synthesiser"	     ,  synthesiserPanel);
		tab.addTab("Model and Properties", modelPanel);
		tab.addTab("Visualisation"		 , visualisationPanel);

		tab.setEnabledAt(0, true);
		tab.setEnabledAt(1, false);
		tab.setEnabledAt(2, false);
		
		frame.getContentPane().add(tab);
		
//		JPanel panel = new JPanel();
//		panel.setBackground(Color.ORANGE);
//		panel.setPreferredSize(new Dimension (5,100));
//		tab.addTab("New tab", panel);
//		panel.setLayout(null);		
	}
}

