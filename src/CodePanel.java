import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class CodePanel extends JPanel implements DocumentListener
{
	//declare instance variables
	public JTextArea codeArea, lineNumberArea;
	private int numLines, currLineNum;
	private Stack<String> stringsToDelete;
	
	
	public CodePanel()
	{
		initComponents();
	}	//end CodePanel constructor
	
	private void initComponents()
	{
		numLines = 0;
		currLineNum = 0;
		
		stringsToDelete = new Stack<String>();
		
		//set up text areas
		codeArea = new JTextArea();
		codeArea.setBorder(BorderFactory.createLineBorder(Color.black));
		codeArea.getDocument().addDocumentListener(this);

		lineNumberArea = new JTextArea();
		lineNumberArea.setBorder(BorderFactory.createLineBorder(Color.black));
		lineNumberArea.setBackground(this.getBackground());
		lineNumberArea.setEditable(false);
		lineNumberArea.setColumns(3);
		lineNumberArea.setAlignmentX(RIGHT_ALIGNMENT);	
		
		//set up scroll panes and synchronize scroll bars
		JScrollPane codePane = new JScrollPane(codeArea);
		
		JScrollPane lineNumberPane = new JScrollPane(lineNumberArea);
		lineNumberPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		lineNumberPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar sBarCodePane = codePane.getVerticalScrollBar();
		JScrollBar sBarLineNumPane = lineNumberPane.getVerticalScrollBar();
		sBarLineNumPane.setModel(sBarCodePane.getModel());
		
		//set up code panel
		this.setLayout(new BorderLayout());
		this.add(codePane, BorderLayout.CENTER);
		this.add(lineNumberPane, BorderLayout.WEST);
	}
	
	private void updateLineNums()
	{
		String stringToAdd = null;
		
		if(codeArea.getLineCount() > numLines)
		{
			int diff = codeArea.getLineCount() - numLines;
			
			while(diff > 0)
			{
	 			currLineNum++;
	 			diff--;
				stringToAdd = currLineNum + "\n";
				lineNumberArea.append(stringToAdd);
				stringsToDelete.push(stringToAdd);
			}
			
		}
		else if(codeArea.getLineCount() < numLines)
		{
			String text = lineNumberArea.getText();
			int diff = numLines - codeArea.getLineCount();
			
			while(diff > 0)
			{
				currLineNum--;
				diff--;
				text = text.replaceAll(stringsToDelete.pop(), "");
			}
			
			lineNumberArea.setText(text);
		}
			
			
		numLines = codeArea.getLineCount();
	}	//end updateLineNums
	
	//
	//Implement DocumentListener methods
	//
	
	public void insertUpdate(DocumentEvent e) 
	{
		if(codeArea.getLineCount() != numLines)
			updateLineNums();
	}	//end insertUpdate

	public void removeUpdate(DocumentEvent e) 
	{	
		if(codeArea.getLineCount() != numLines)
			updateLineNums();
	}	//end removeUpdate

	public void changedUpdate(DocumentEvent e) 
	{
		if(codeArea.getLineCount() != numLines)
			updateLineNums();
	}	//end changedUpdate
}
