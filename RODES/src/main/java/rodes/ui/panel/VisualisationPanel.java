package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import evochecker.auxiliary.StringProperties;
import evochecker.auxiliary.Utility;
import javafx.animation.AnimationTimer;
import rodes.testing.TestRODES;

@SuppressWarnings("serial")
public class VisualisationPanel extends AbstractTabPanel{

	/** Scroll pane for textare*/
	private JScrollPane scrollPane;

	/** Textout textarea*/
	private JTextArea feedbackTextArea;
	
	TestRODES testRodes;
	JPanel thisPanel;
	
	public VisualisationPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, 2, 3, -1);
		if (frame == null)
			throw new NullPointerException();
		
		thisPanel = this;
		
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
//				RODES.main(null);
				
				testRodes = new TestRODES();
//				reDraw();
				AnimationTimer timer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						List<Integer> list = new ArrayList<Integer>();
						testRodes.getQueue().drainTo(list);
						System.err.println(list);
						StringBuilder sb = new StringBuilder();
						list.forEach(sb::append);
						feedbackTextArea.append(sb.toString());
						feedbackTextArea.update(feedbackTextArea.getGraphics());
						thisPanel.revalidate();
						thisPanel.repaint();
						frame.repaint();
						frame.revalidate();	
					}
				};
				timer.start();
				
				Thread t = new Thread(testRodes, "TestRODES");
				t.start();

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
	public void reDraw() {
//		String str = testRodes.getList();
//		feedbackTextArea.setText(str);
//		
//		while (true) {
//			String str2=null;
//			do {
//				str2=testRodes.getList();
////				System.err.println(str.equals(str2));
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			while (str.equals(str2));
//			str = str2;
//			feedbackTextArea.append(str);
//			feedbackTextArea.update(feedbackTextArea.getGraphics());
//			this.revalidate();
//			this.repaint();
////			frame.repaint();
////			frame.revalidate();	
//		}
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				List<Integer> list = new ArrayList<Integer>();
				testRodes.getQueue().drainTo(list);
				if (!list.isEmpty()) {
					System.err.println(list);
					StringBuilder sb = new StringBuilder();
					list.forEach(sb::append);
					feedbackTextArea.append(sb.toString());
					feedbackTextArea.update(feedbackTextArea.getGraphics());
					thisPanel.revalidate();
					thisPanel.repaint();
					frame.repaint();
					frame.revalidate();	
				}
			}
		};
		timer.start();
	}
	
}

