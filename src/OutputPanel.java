import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class OutputPanel extends JPanel implements ItemListener
{
	//declare instance variables
	public JTextArea outputArea;
	public JComboBox<String> answerBox;
	private ArrayList<AnswerSet> answerSets;
	
	public OutputPanel()
	{
		initComponents();
	}	//end OutputPanel constructor
	
	private void initComponents()
	{
		//set up combo boxes
		answerBox = new JComboBox<String>();
		answerBox.addItemListener(this);
		
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
		this.add(answerBox, BorderLayout.NORTH);
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		this.answerSets = MainFrame.controlPanel.getAnswerSets();

		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			String selected = (String)e.getItem(), outputString = "";
			
			if(selected.equals("All"))
			{
				for(AnswerSet set : answerSets)
					outputString += set.toString();
			}
			else
			{
				int idx = Integer.parseInt(selected.split(" ")[1]) - 1;
				outputString = answerSets.get(idx).toString();
			}
			
			MainFrame.oPanel.outputArea.setText(outputString);
		}
	}	//end itemStateChangedEvent
}	//en d OutputPanel
