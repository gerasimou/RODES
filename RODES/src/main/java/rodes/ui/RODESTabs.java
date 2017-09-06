package rodes.ui;

import java.awt.Component;

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

public class RODESTabs extends JTabbedPane{

	private static final long serialVersionUID = 1L;


	protected RODESTabs(JFrame frame, StringProperties properties) {
		
//		tab 										= new JTabbedPane();
		SynthesiserPanel		synthesiserPanel		= new SynthesiserPanel(frame, this, properties);
		ModelPanel 			modelPanel 			= new ModelPanel(frame, this, properties);
		ConfigurationPanel  configurationPanel  	= new ConfigurationPanel(frame, this, properties);
		ExecutionPanel  		executionPanel  		= new ExecutionPanel(frame, this, properties);
		VisualisationPanel  	visualisationPanel  	= new VisualisationPanel(frame, this, properties);
		
		this.addTab("Algorithm"			, synthesiserPanel);
		this.addTab("Model and Properties", modelPanel);
		this.addTab("Configuration"		, configurationPanel);
		this.addTab("Execution"			, executionPanel);
		this.addTab("Visualisation"		, visualisationPanel);
				
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
	            //System.err.println("Tab: " + tab. getSelectedIndex());
	            
	            Component selectedComponent = ((JTabbedPane)e.getSource()).getSelectedComponent(); 
	            if (selectedComponent instanceof AbstractTabPanel)
	            		((AbstractTabPanel)selectedComponent).reDraw();
	            frame.repaint();
	            frame.revalidate();
			}
		});
		
		initTabs();
	}
	
	
	protected void initTabs() {
		int totalTabs = this.getTabCount();
		
		for (int i=0; i<totalTabs; i++) {
			//activate/deactivate tab
			if (i==0)
				this.setEnabledAt(i, true);
			else
				this.setEnabledAt(i, false);
			
			//get the tab
			Component c = this.getComponentAt(i);
			if (c instanceof AbstractTabPanel) {
				((AbstractTabPanel)c).init();
			}
		}		
	}
}
