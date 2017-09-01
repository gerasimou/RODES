package rodes.ui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import evochecker.auxiliary.StringProperties;

@SuppressWarnings("serial")
public class VisualisationPanel extends AbstractTabPanel{
	
	
	public VisualisationPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(1, 2, -1);
		if (frame == null)
			throw new NullPointerException();
		
		this.parent		= tab;
		this.properties = props;

		setLayout(null);
		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		
		//algorithm

		JLabel algorithmLabel 	= new JLabel("Algorithm:");
		algorithmLabel.setBounds(10, 10, 150, 40);

		JComboBox<String> list = new JComboBox<>();// Box<String> (new String[]{"NSGAII", "SPEA2", "MOCell"});
		list.setBounds(170, 10, 220, 40);
		setLayout(null);

		add(algorithmLabel);
		add(list);

		
		//population
		JLabel populationLabel 			= new JLabel("Population:");
		populationLabel.setBounds(10, 50, 150, 40);

		JTextField  populationTextfield	= new JTextField("50");
		populationTextfield.setBounds(170, 50, 220, 40);
		populationTextfield.setEditable(true);
		
		add(populationLabel);
		add(populationTextfield);

		
		//evaluations
		JLabel evlauationsLabel 			= new JLabel("Evaluations:");
		evlauationsLabel.setBounds(10, 90, 150, 40);
		
		JTextField  evaluationsTextfield	= new JTextField("1000");
		evaluationsTextfield.setBounds(170, 90, 220, 40);
		evaluationsTextfield.setEditable(true);
		
		add(evlauationsLabel);
		add(evaluationsTextfield);

		
		//tolerance
		JLabel toleranceLabel 			= new JLabel("Tolerance:");
		toleranceLabel.setBounds(10, 130, 150, 40);
		
		JTextField  toleranceTextfield	= new JTextField("");
		toleranceTextfield.setBounds(170, 130, 220, 40);
		evaluationsTextfield.setEditable(true);
		
		add(toleranceLabel);
		add(toleranceTextfield);

		
		//epsilon
		JLabel epsilonLabel 			= new JLabel("Epsilon:");
		epsilonLabel.setBounds(10, 170, 150, 40);
		
		JTextField  epsilonTextfield	= new JTextField("");
		epsilonTextfield.setBounds(170, 170, 220, 40);
		epsilonTextfield.setEditable(true);
		
		add(epsilonLabel);
		add(epsilonTextfield);

		
		//JVM
		JLabel JVMLabel 			= new JLabel("JVM:");
		JVMLabel.setBounds(10, 210, 150, 40);
		
		JTextField  JVMTextField	= new JTextField("");
		JVMTextField.setBounds(170, 210, 220, 40);
		JVMTextField.setEditable(false);
		
		JButton JVMButton	= new JButton("Select JVM");
		JVMButton.setBounds(410, 210, 170, 40);
		JVMButton.setHorizontalAlignment (SwingConstants.LEFT);
		JVMButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFileChooser(JVMButton, "JVM", new String[]{"*"});
			}
		});				
		
		add(JVMLabel);
		add(JVMTextField);
		add(JVMButton);

		//processors
		JLabel processorsLabel 			= new JLabel("Processors:");
		processorsLabel.setBounds(10, 250, 150, 40);
		
		JTextField  processorsTextField	= new JTextField("1");
		processorsTextField.setBounds(170, 250, 220, 40);
		processorsTextField.setEditable(true);
		
		add(processorsLabel);
		add(processorsTextField);

		setVisible(true);
	}	
	
	
	private JFileChooser createFileChooser(Component parent, String description, String[] extension){	
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);// "JPG & GIF Images", new String[]{"jpg", "gif"});
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	    
	    }
	    
	    return chooser;		
	}


	@Override
	protected void checkInputs() {
		// TODO Auto-generated method stub
		
	}
}