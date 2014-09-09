import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MainFrame extends JFrame 
{
	private JFrame currFrame;
	private File currFile;
	private JButton executeButton, saveFileButton, clearCodeAreaButton;
	private JTextArea codeArea, outputArea;
	private JPanel mainPanel, eastPanel, westPanel, southeastPanel, 
	southeastCenterPanel, northeastPanel;

	public MainFrame(String ver)
	{		
		initComponents();

		this.setTitle("Clingo GUI - " + ver);
		this.setSize(750, 750);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initComponents()
	{
		ButtonListener bListener = new ButtonListener();

		currFile = null;
		currFrame = this;
		
		clearCodeAreaButton = new JButton("Clear Code");
		clearCodeAreaButton.addActionListener(bListener);

		saveFileButton = new JButton("Save File");
		saveFileButton.addActionListener(bListener);

		executeButton = new JButton("Execute");
		executeButton.addActionListener(bListener);

		outputArea = new JTextArea();
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);

		codeArea = new JTextArea();

		westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(new JLabel("Code:"), BorderLayout.NORTH);
		westPanel.add(codeArea, BorderLayout.CENTER);

		southeastCenterPanel = new JPanel();
		southeastCenterPanel.add(saveFileButton);
		southeastCenterPanel.add(executeButton);
		southeastCenterPanel.add(clearCodeAreaButton);

		southeastPanel = new JPanel();
		southeastPanel.setLayout(new BorderLayout());
		southeastPanel.add(new JLabel("Controls:"), BorderLayout.NORTH);
		southeastPanel.add(southeastCenterPanel, BorderLayout.CENTER);

		northeastPanel = new JPanel();
		northeastPanel.setLayout(new BorderLayout());
		northeastPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
		northeastPanel.add(outputArea, BorderLayout.CENTER);

		eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(2, 0));
		eastPanel.add(northeastPanel);
		eastPanel.add(southeastPanel);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(westPanel);
		mainPanel.add(eastPanel);


		this.add(mainPanel);
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton)e.getSource();

			if(source.equals(executeButton))
			{
				BufferedReader br = null;
				ProcessBuilder pb = null;
				Process proc = null;
				BufferedWriter writer = null;
				File tempFile = null;

				try 
				{
					tempFile = File.createTempFile("temp", ".lp");
					writer = new BufferedWriter(new FileWriter(tempFile));
					writer.write(codeArea.getText());
					writer.close();

					String[] command = {"clingo", tempFile.getPath()};

					pb = new ProcessBuilder(command);
					proc = pb.start();
					br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

					String line = null;
					String outputString = "";
					int lineNum = 0;
					
					while((line = br.readLine()) != null)
					{
						outputString += line + "\n";
						
						outputArea.setText(outputString);
						
						lineNum += 1;
					}
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
			else if(source.equals(saveFileButton))
			{
				JFileChooser fc = new JFileChooser();

				fc.showSaveDialog(currFrame);
			}
			else if(source.equals(clearCodeAreaButton))
			{
				currFile = null;
				codeArea.setText("");
			}
		}	//end actionPerformed
	}	//end ButtonListener
}	//end MainFrame
