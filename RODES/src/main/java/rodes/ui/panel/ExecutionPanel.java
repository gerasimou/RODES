package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.auxiliary.StringProperties;
import evochecker.auxiliary.Utility;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import rodes.RODES;

@SuppressWarnings("serial")
public class ExecutionPanel extends AbstractTabPanel{

	/** Scroll pane for textare*/
	private JScrollPane scrollPane;

	/** Textout textarea*/
	private JTextArea feedbackTextArea;
	
	/** Synthesis buttong*/
	private JButton synthesisButton;
	
	/** Graph button*/
	private JButton graphButton;
	
//	TestRODES testRodes;
//    final BlockingQueue<Integer> messageQueue = new ArrayBlockingQueue<>(1);
    
    KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();

	
	public ExecutionPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, 2, 3, -1);
		if (frame == null)
			throw new NullPointerException();
			
		this.parent		= tab;
		this.properties = props;

		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		setLayout(null);

		
		//Start button
		synthesisButton	= new JButton("Start synthesis");
		synthesisButton.setBounds(410, 340, 170, 40);
		synthesisButton.setHorizontalAlignment (SwingConstants.LEFT);
		synthesisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start Execution
				Utility.loadPropertiesInstance(props);
				System.err.println("Starting synthesis");

				//TestRODES producer = new TestRODES(messageQueue);
				//Thread thread = new Thread(producer, "TestRODES");
				RODES rodes = new RODES();
				Thread thread = new Thread(rodes, "RODES");
				thread.setDaemon(true);
				thread.start();
//				knowledge.put(Constants.DONE_KEYWORD, true);
				
				MyTimer timer = new MyTimer(500);
				timer.start();
				
				Timer doneTimer = new Timer(2000, new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
	                		Boolean done = (Boolean) knowledge.get(Constants.DONE_KEYWORD);
	                		if (done!= null && done) {
	                			graphButton.setEnabled(true);
	                			knowledge.put(Constants.DONE_KEYWORD, false);
	                		}
					}
				});
				doneTimer.start();
									
				synthesisButton.setEnabled(false);
				previousButton.setEnabled(false);
			}
		});		
		add(synthesisButton);
		
		//feedback textarea
		feedbackTextArea = new JTextArea();
		feedbackTextArea.setBounds(50, 50, 500, 250);
		feedbackTextArea.setLineWrap(true);
		feedbackTextArea.setEditable(false);
		
		//Scroll pane
		scrollPane = new JScrollPane (feedbackTextArea);
        scrollPane.setBounds(50, 50, 500, 250);
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		add(scrollPane);
		
		graphButton = new JButton("Generate graph");
		graphButton.setHorizontalAlignment(SwingConstants.LEFT);
		graphButton.setBounds(210, 340, 170, 40);
		graphButton.setEnabled(false);
		graphButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					JTextField mlabTextField = new JTextField();
					String dirPath = knowledge.get("MATLAB") != null ? knowledge.get("MATLAB").toString() : System.getProperty("user.home"); 
					System.out.println(dirPath);
					int returnVal = showFileChooser(scrollPane, mlabTextField, "Matlab", new String[]{"exe"}, dirPath);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String matlab = mlabTextField.getText() + File.separator + "bin" + File.separator + "matlab"; 						
    						knowledge.put("MATLAB", mlabTextField.getText());

						
						String tolerance = Utility.getProperty(Constants.TOLERANCE_KEYWORD).replace(".", "");
						String epsilon  	=  Utility.getProperty(Constants.EPSILON_KEYWORD).replace(".", "");
						String run		=  "1";
								
						//run matlab to generate graph
						String params		= "'../data/',"												//data directory  
								  + "'" + Utility.getProperty(Constants.PROBLEM_KEYWORD) 	+"', "	//Problem name (subdirectory)
								  + "2, 1, 2, " 													//continuous params, discrete params, objectives
								  + "'" + tolerance +"', "										//tolerance
								  + "'" + epsilon +"'";  										//epsilon
						String[] command   	= { "/bin/bash", "-c", "cd mlab;"
								+ matlab +  " -nodisplay -nosplash -nodesktop -r \"try "
	//							+ " /Applications/MATLAB_R2016a.app/bin/matlab -nodisplay -nosplash -nodesktop -r \"try " 
								+ "createPlotsCMD(" + params + "); catch; end; quit\""};
	            			ProcessBuilder pb = new ProcessBuilder(command);
	            			pb.redirectOutput(Redirect.INHERIT);
	            			pb.redirectError(Redirect.INHERIT);
	            			Process p;
						p = pb.start();
	            			p.waitFor();
	            			            			
	            			//show graph
	    					String graph 	 = tolerance +"_"+ epsilon +"_"+ run +"_objs.png";
	    					String graphPath = "mlab" + File.separator + "graphs" + File.separator + Utility.getProperty(Constants.PROBLEM_KEYWORD) + File.separator + graph;
	    					knowledge.put(Constants.GRAPH_KEYWORD, graphPath);
	    					
	    					JOptionPane.showMessageDialog(parent, "Graph saved at " + graphPath, "Graph output", JOptionPane.INFORMATION_MESSAGE);
	
	    					
	    					((JTabbedPane)parent).setEnabledAt(3, false);
	    					((JTabbedPane)parent).setSelectedIndex(4);
					}
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		add(graphButton);
		
		setVisible(true);
	}	
	
	
	

	@Override
	protected void checkInputs() {}


	@Override
	public void reDraw() {}
	
	
	@Override
	public void init() {	
		synthesisButton.setEnabled(true);
		graphButton.setEnabled(false);
	}

	
	
	class MyTimer extends Timer{
		final LongProperty lastUpdate = new SimpleLongProperty();
		final long minUpdateInterval = 100; // nanoseconds. Set to higher number to slow output.
		
		
		ActionListener timerListener;
		
		protected MyTimer (int initialDelay) {
			super (initialDelay, null);
			initTimerListener();
			addActionListener(timerListener);
		}
		
		private void initTimerListener() {
			timerListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					long now = System.currentTimeMillis();
	                if (now - lastUpdate.get() > minUpdateInterval) {
//	                    final Integer message = messageQueue.poll();
//	                		String message = knowledge.get(Constants.EVALUATIONS_KEYWORD);
//	                		knowledge.set(Constants.EVALUATIONS_KEYWORD, "");
	                		String message = knowledge.getMessage();
//	                    if (message != null && !message.isEmpty()) {
						feedbackTextArea.append(message);
	//                        console.appendText("\n" + message);
//	                    }
	//	    				List<Integer> list = new ArrayList<Integer>();
	//	    				messageQueue.drainTo(list);
	//	    				if (!list.isEmpty()) {
	//		                        console.appendText("\n" + list.toString());
	//							feedbackTextArea.append(list.toString());
	//							feedbackTextArea.update(feedbackTextArea.getGraphics());
	//							StringBuilder sb = new StringBuilder();
	//							list.forEach(sb::append);
	//							feedbackTextArea.append(sb.toString());
	//							feedbackTextArea.update(feedbackTextArea.getGraphics());
	//							frame.repaint();
	//							frame.revalidate();	
	//	                      }
	                    lastUpdate.set(now);		                
	                }
				}
			};
		}		
	}
}

