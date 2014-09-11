import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class OutputPanel extends JPanel 
{
	//declare instance variables
	public JTextArea outputArea;
	
	public OutputPanel()
	{
		initComponents();
	}	//end OutputPanel constructor
	
	private void initComponents()
	{
		//set up text areas
		outputArea = new JTextArea();
		outputArea.setBorder(BorderFactory.createLineBorder(Color.black));
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);
		
		//set up scroll panes
		JScrollPane outputPane = new JScrollPane(outputArea);
		
		//set up output panel
		this.setLayout(new BorderLayout());
		this.add(new JLabel("Output:"), BorderLayout.NORTH);
		this.add(outputPane, BorderLayout.CENTER);
	}
}
