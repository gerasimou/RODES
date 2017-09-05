package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
import rodes.testing.TestRODES;

@SuppressWarnings("serial")
public class VisualisationPanel2 extends AbstractTabPanel{

	/** Scroll pane for textare*/
	private JScrollPane scrollPane;

	/** Textout textarea*/
	private JTextArea feedbackTextArea;
	
	TestRODES testRodes;
    final BlockingQueue<Integer> messageQueue = new ArrayBlockingQueue<>(1);
    
    KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();

	
	public VisualisationPanel2 (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, 2, 3, -1);
		if (frame == null)
			throw new NullPointerException();
			
		this.parent		= tab;
		this.properties = props;

		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		setLayout(null);

		
		//Start button
		JButton startButton	= new JButton("Start synthesis");
		startButton.setBounds(410, 340, 170, 40);
		startButton.setHorizontalAlignment (SwingConstants.LEFT);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start Execution
				Utility.loadPropertiesInstance(props);
				System.err.println("Starting synthesis");

//				TestRODES producer = new TestRODES(messageQueue);
//				Thread thread = new Thread(producer, "TestRODES");
				RODES rodes = new RODES();
				Thread thread = new Thread(rodes, "RODES");
				thread.setDaemon(true);
				thread.start();

				
				MyTimer timer = new MyTimer(100);
				timer.start();
												
			}
		});		
		add(startButton);
		
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
		
		setVisible(true);
	}	
	
	
	

	@Override
	protected void checkInputs() {}



	@Override
	public void reDraw() {}

	
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
	                		String message = knowledge.get(Constants.EVALUATIONS_KEYWORD);
	                		knowledge.set(Constants.EVALUATIONS_KEYWORD, "");
	                    if (message != null && !message.isEmpty()) {
							feedbackTextArea.append(message + "\n");
	//                        console.appendText("\n" + message);
	                    }
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

