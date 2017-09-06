package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")
public class VisualisationPanel extends AbstractTabPanel{

	/** Graph panel */
//	private JPanel graphPanel;
    
    KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();

	BufferedImage img;
	
	Image scaledImage;

	
	public VisualisationPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, -1, 4, -1);
		if (frame == null)
			throw new NullPointerException();
			
		this.parent		= tab;
		this.properties = props;

		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		setLayout(null);
		
//		graphPanel = new JPanel();
//		graphPanel.setBackground(Color.WHITE);
//		graphPanel.setBounds(15, 15, 570, 370);		
//		add(graphPanel);
		
		setVisible(true);
	}	
	
	
	

	@Override
	protected void checkInputs() {}


	@Override
	public void reDraw() {
		try {
			String path = knowledge.get(Constants.GRAPH_KEYWORD).toString();
			File file = new File(path);
			img = ImageIO.read(file);
			
			scaledImage = img.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);

//			picLabel = new JLabel(new ImageIcon(img));
//			graphPanel.add(picLabel);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void init() {}
	
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(scaledImage, 0, 0, this); // see javadoc for more info on the parameters            
    }

}

