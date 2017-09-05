package rodes.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.StringProperties;
import evochecker.auxiliary.Utility;

@SuppressWarnings("serial")
public class ConfigurationPanel extends AbstractTabPanel{

	/** Scroll pane for table*/
	private JScrollPane scrollPane;
	
	/** Table*/
	private JTable table;
	
	public ConfigurationPanel (JFrame frame, JTabbedPane tab, StringProperties props) {
		super(frame, 1, 2, 3);
		if (frame == null)
			throw new NullPointerException();
		
		
		this.parent		= tab;
		this.properties = props;

		setPreferredSize(new Dimension(600, 400));
		setBorder(null);
		setLayout(null);
		
		//Configuration label
		JLabel configurationLabel = new JLabel("Specified configuration");
		configurationLabel.setBounds(10, 10, 300, 40);
		configurationLabel.setToolTipText("Specified configuration for synthesis");
		configurationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		add(configurationLabel);
				
		setVisible(true);
	}	
	
	
	

	@Override
	protected void checkInputs() {}




	@Override
	public void reDraw() {		
		if (scrollPane != null)
			this.remove(scrollPane);
		
		table = new JTable(new TableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		
		//Create the scroll pane and add the table to it.
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 50, 500, 250);
		
        //Add the scroll pane to this panel.
        add(scrollPane);
	}
	
	class TableModel extends AbstractTableModel{
		String[] columnNames = {"Parameter", "Value"};

		Object[][] data; 
		
		private TableModel() {
			data = new Object[properties.stringPropertyNames().size()][2];
			int i=0;
			for (String key : properties.stringPropertyNames()) {
				if (key.equals(Constants.ERRORS_KEYWORD))
					continue;
				String value = properties.getProperty(key);
				data[i][0] = key;
				data[i][1] = value;
				i++;
			}
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
		
		@Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
		
		@Override
        public String getColumnName(int col) {
            return columnNames[col];
        }	
	}
}

