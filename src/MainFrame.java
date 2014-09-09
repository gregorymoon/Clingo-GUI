import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class MainFrame extends JFrame 
{
	private JLabel notificationLabel;
	private JTextField numSolutionsField;
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
		this.setSize(690, 580);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	//end MainFrame Constructor

	private void initComponents()
	{
		ButtonListener bListener = new ButtonListener();

		currFile = null;
		currFrame = this;

		notificationLabel = new JLabel();
		notificationLabel.setForeground(Color.red);
		notificationLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		numSolutionsField = new JTextField();
		numSolutionsField.setText("0");
		numSolutionsField.setColumns(5);

		clearCodeAreaButton = new JButton("Clear Code");
		clearCodeAreaButton.addActionListener(bListener);

		saveFileButton = new JButton("Save File");
		saveFileButton.addActionListener(bListener);

		executeButton = new JButton("Execute");
		executeButton.addActionListener(bListener);

		outputArea = new JTextArea();
		outputArea.setBorder(BorderFactory.createLineBorder(Color.black));
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);

		codeArea = new JTextArea();
		codeArea.setBorder(BorderFactory.createLineBorder(Color.black));

		westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(new JLabel("Code:"), BorderLayout.NORTH);
		westPanel.add(codeArea, BorderLayout.CENTER);

		southeastCenterPanel = new JPanel();
		southeastCenterPanel.add(new JLabel("Number of Solutions:"));
		southeastCenterPanel.add(numSolutionsField);
		southeastCenterPanel.add(executeButton);
		southeastCenterPanel.add(saveFileButton);
		southeastCenterPanel.add(clearCodeAreaButton);
		southeastCenterPanel.add(notificationLabel);

		southeastPanel = new JPanel();
		southeastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
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

		this.setResizable(false);
		this.addComponentListener(new SizeListener());
		this.add(mainPanel);
	}	//end initComponents

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton)e.getSource();

			if(source.equals(executeButton))
				executeCode();
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

		private void executeCode()
		{
			int numAnswers;

			try
			{
				numAnswers = Integer.parseInt(numSolutionsField.getText());
			}
			catch(NumberFormatException e)
			{
				notificationLabel.setText("<html>Please enter an integer for number of solutions.<br>"
						+ "Defaulting to 0 (All solutions).</html>");
				numAnswers = 0;
			}

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

				String[] command = {"clingo", tempFile.getPath(), Integer.toString(numAnswers)};

				pb = new ProcessBuilder(command);
				proc = pb.start();
				br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				String line = null;
				String outputString = "";
				String[] answers;
				boolean answerSetFound = false;
				int numFoundAnswers = 0;

				while((line = br.readLine()) != null)
				{
					String lowerLine = line.toLowerCase();

					if(answerSetFound)
					{							
						answers = line.split(" ");

						for(int i = 0; i < answers.length; i++)
						{
							if(i + 2 < answers.length)
							{
								outputString += answers[i];
								i += 1;
								outputString += "\t" + answers[i] + "\n";
							}
							else
								outputString += answers[i] + "\n";			
						}

						answerSetFound = false;
					}
					else if(!lowerLine.contains("version") && !lowerLine.contains("solving") && !lowerLine.contains("reading"))
					{
						if(lowerLine.contains("answer"))
						{
							numFoundAnswers += 1;
							
							if(numFoundAnswers > 1)
								outputString += "\n" + line + "\n";
							else
								outputString += line + "\n";

							answerSetFound = true;	
						}
						else
							outputString += line + "\n";
					}
				}

				outputArea.setText(outputString);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			finally
			{
				try 
				{
					Files.delete(tempFile.toPath());
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}	//end executeCode
	}	//end ButtonListener
	
	private class SizeListener extends ComponentAdapter
	{
		public void componentResized(ComponentEvent e) 
		{
			System.out.println("Width: " + currFrame.getWidth());
			System.out.println("Height: " + currFrame.getHeight());
		}	//end componentResized	
	}	//end SizeListener
}	//end MainFrame
