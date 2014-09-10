import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class MainFrame extends JFrame 
{
	private JScrollPane outputPane, codePane, lineNumberPane;
	private JTextField numSolutionsField, timeLimitField;
	private JFrame currFrame;
	private File currCodeFile, currOutputFile;
	private JButton executeButton, saveCodeAsButton, clearCodeAreaButton,
		saveOutputButton, openExistingFileButton, saveCodeButton, saveOutputAsButton,
		clearOutputAreaButton;
	private JTextArea codeArea, outputArea, notificationArea, lineNumberArea;
	private JPanel mainPanel, eastPanel, westPanel, southeastPanel, 
	southeastCenterPanel, northeastPanel, codeDisplayPanel;

	public MainFrame(String ver)
	{		
		initComponents();

		this.setTitle("Clingo GUI - " + ver);
		this.setSize(1020, 790);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	//end MainFrame Constructor

	private void initComponents()
	{	
		ButtonListener bListener = new ButtonListener();

		currOutputFile = null;
		currCodeFile = null;
		currFrame = this;
		
		saveCodeButton = new JButton("Save Code");
		saveCodeButton.addActionListener(bListener);

		openExistingFileButton = new JButton("Open Existing File");
		openExistingFileButton.addActionListener(bListener);

		saveOutputButton = new JButton("Save Output");
		saveOutputButton.addActionListener(bListener);

		notificationArea = new JTextArea();
		notificationArea.setLineWrap(true);
		notificationArea.setBackground(this.getBackground());
		notificationArea.setForeground(Color.red);
		notificationArea.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		timeLimitField = new JTextField();
		timeLimitField.setText("5");
		timeLimitField.setColumns(5);
		
		numSolutionsField = new JTextField();
		numSolutionsField.setText("1");
		numSolutionsField.setColumns(5);

		clearCodeAreaButton = new JButton("Clear Code");
		clearCodeAreaButton.addActionListener(bListener);

		saveOutputAsButton = new JButton("Save Output As...");
		saveOutputAsButton.addActionListener(bListener);

		saveCodeAsButton = new JButton("Save Code As...");
		saveCodeAsButton.addActionListener(bListener);

		executeButton = new JButton("Execute");
		executeButton.addActionListener(bListener);

		outputArea = new JTextArea();
		outputArea.setBorder(BorderFactory.createLineBorder(Color.black));
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);

		codeArea = new JTextArea();
		codeArea.setBorder(BorderFactory.createLineBorder(Color.black));
		codeArea.getDocument().addDocumentListener(new AreaListener());

		lineNumberArea = new JTextArea();
		lineNumberArea.setBorder(BorderFactory.createLineBorder(Color.black));
		lineNumberArea.setBackground(currFrame.getBackground());
		lineNumberArea.setEditable(false);
		lineNumberArea.setColumns(3);
		lineNumberArea.setAlignmentX(RIGHT_ALIGNMENT);	
		
		outputPane = new JScrollPane(outputArea);
		
		codePane = new JScrollPane(codeArea);
		
		lineNumberPane = new JScrollPane(lineNumberArea);
		lineNumberPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		lineNumberPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar sBarCodePane = codePane.getVerticalScrollBar();
		JScrollBar sBarLineNumPane = lineNumberPane.getVerticalScrollBar();
		sBarLineNumPane.setModel(sBarCodePane.getModel());
		
		codeDisplayPanel = new JPanel();
		codeDisplayPanel.setLayout(new BorderLayout());
		codeDisplayPanel.add(codePane, BorderLayout.CENTER);
		codeDisplayPanel.add(lineNumberPane, BorderLayout.WEST);
		
		westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(new JLabel("Code:"), BorderLayout.NORTH);
		westPanel.add(codeDisplayPanel, BorderLayout.CENTER);
		
		clearOutputAreaButton = new JButton("Clear Output");
		clearOutputAreaButton.addActionListener(bListener);
		
		southeastCenterPanel = new JPanel();
		southeastCenterPanel.add(new JLabel("Number of Solutions:"));
		southeastCenterPanel.add(numSolutionsField);
		southeastCenterPanel.add(new JLabel("Time Limit (seconds): "));
		southeastCenterPanel.add(timeLimitField);
		southeastCenterPanel.add(executeButton);
		southeastCenterPanel.add(saveCodeAsButton);
		southeastCenterPanel.add(saveCodeButton);
		southeastCenterPanel.add(saveOutputAsButton);
		southeastCenterPanel.add(saveOutputButton);
		southeastCenterPanel.add(openExistingFileButton);
		southeastCenterPanel.add(clearCodeAreaButton);
		southeastCenterPanel.add(clearOutputAreaButton);

		southeastPanel = new JPanel();
		southeastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		southeastPanel.setLayout(new BorderLayout());
		southeastPanel.add(new JLabel("Controls:"), BorderLayout.NORTH);
		southeastPanel.add(southeastCenterPanel, BorderLayout.CENTER);
		southeastPanel.add(notificationArea, BorderLayout.SOUTH);

		northeastPanel = new JPanel();
		northeastPanel.setLayout(new BorderLayout());
		northeastPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
		northeastPanel.add(outputPane, BorderLayout.CENTER);

		eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(2, 0));
		eastPanel.add(northeastPanel);
		eastPanel.add(southeastPanel);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(westPanel);
		mainPanel.add(eastPanel);

		this.addComponentListener(new SizeListener());
		this.setResizable(true);
		this.add(mainPanel);
	}	//end initComponents
	
	private class AreaListener implements DocumentListener
	{	
		public void insertUpdate(DocumentEvent e) 
		{
			updateLineNums();
		}

		public void removeUpdate(DocumentEvent e) 
		{	
			updateLineNums();
		}

		public void changedUpdate(DocumentEvent e) 
		{
			updateLineNums();
		}
		
		private void updateLineNums()
		{
			lineNumberArea.setText("");
			for(int i = 0; i < codeArea.getLineCount(); i++)
				lineNumberArea.append(i+1 + "\n");
		}
	}	//end AreaListener
	
	private class SizeListener extends ComponentAdapter
	{
		public void componentResized(ComponentEvent e) 
		{
			System.out.println("Height: " + e.getComponent().getHeight());
			System.out.println("Width: " + e.getComponent().getWidth());
		}
	}
	
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton source = (JButton)e.getSource();

			if(source.equals(executeButton))
				executeCode();
			else if(source.equals(saveCodeAsButton))
				saveCodeAs();
			else if(source.equals(saveCodeButton))
				saveCode();
			else if(source.equals(openExistingFileButton))
				openExistingFile();
			else if(source.equals(saveOutputButton))
				saveOutput();
			else if (source.equals(saveOutputAsButton))
				saveOutputAs();
			else if(source.equals(clearCodeAreaButton))
			{
				currCodeFile = null;
				codeArea.setText("");
				notificationArea.setText("Cleared code area.");
			}
			else if(source.equals(clearOutputAreaButton))
			{
				currOutputFile = null;
				outputArea.setText("");
				notificationArea.setText("Cleared output area.");
			}
		}	//end actionPerformed

		private void openExistingFile()
		{
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);

			if(fc.showOpenDialog(currFrame) == JFileChooser.APPROVE_OPTION)
			{
				currCodeFile = fc.getSelectedFile();

				try 
				{
					String textAreaString = "";
					String line = null;
					BufferedReader br = new BufferedReader(new FileReader(currCodeFile));
					while((line = br.readLine()) != null)
					{
						textAreaString += line + "\n";
					}

					br.close();
					codeArea.setText(textAreaString);
					notificationArea.setText("Opened:\n" + currCodeFile.getPath() + "\nsuccessfully.");

				} catch (IOException e) {
					e.printStackTrace();
					notificationArea.setText("File at\n" + currCodeFile.getPath() + "\nnot found.");
				}
			}
		}	//end openExistingFile

		private void saveOutput()
		{
			FileWriter fw;
			try 
			{
				if(currOutputFile == null)
				{
					currOutputFile = new File(System.getProperty("user.home") + File.separator
							+ "Desktop" + File.separator + "temp_output_filename.txt");
				}

				if(currOutputFile.getName().contains(".txt"))
				{
					fw = new FileWriter(currOutputFile);

					fw.write(outputArea.getText());
					fw.close();

					notificationArea.setText("Saved code to:\n" + currOutputFile.getPath() + "");
				}
				else
					notificationArea.setText("Did not save to:\n" + currOutputFile.getPath() 
							+ "\nOnly \".txt\" files can be saved.");

			} catch (IOException e1) {
				e1.printStackTrace();
				notificationArea.setText("Unable to save code to\n" + currOutputFile.getPath() + "");
			}
		}	//end saveOutput

		private void saveOutputAs()
		{
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);

			if(currOutputFile == null)
				fc.setSelectedFile(new File("temp_output_filename.txt"));
			else
				fc.setSelectedFile(currOutputFile);

			if(fc.showSaveDialog(currFrame) == JFileChooser.APPROVE_OPTION)
			{
				currOutputFile = fc.getSelectedFile();	
				saveCode();
			}
		}	//end saveOutputAs

		private void saveCode()
		{
			FileWriter fw;
			try 
			{
				if(currCodeFile == null)
				{
					currCodeFile = new File(System.getProperty("user.home") + File.separator
							+ "Desktop" + File.separator + "temp_code_filename.txt");
				}

				if(currCodeFile.getName().contains(".txt"))
				{
					fw = new FileWriter(currCodeFile);

					fw.write(codeArea.getText());
					fw.close();

					notificationArea.setText("Saved code to:\n" + currCodeFile.getPath() + "");
				}
				else
					notificationArea.setText("Did not save code to:\n" + currCodeFile.getPath() 
							+ "\nOnly \".txt\" files can be saved.");

			} catch (IOException e1) {
				e1.printStackTrace();
				notificationArea.setText("Unable to save code to\n" + currCodeFile.getPath() + "");
			}
		}	//end saveFile

		private void saveCodeAs()
		{
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);

			if(currCodeFile == null)
				fc.setSelectedFile(new File("temp_code_filename.txt"));
			else
				fc.setSelectedFile(currCodeFile);

			if(fc.showSaveDialog(currFrame) == JFileChooser.APPROVE_OPTION)
			{
				currCodeFile = fc.getSelectedFile();	
				saveCode();
			}
		}	//end saveFileAs

		private void executeCode()
		{			
			long startTime = System.currentTimeMillis();
			
			String fiveSpaces = "     ";
			String errMsg = "";
			int numAnswers, timeLimit;
			boolean errCaught = false;

			try
			{
				numAnswers = Integer.parseInt(numSolutionsField.getText());
			}
			catch(NumberFormatException e)
			{
				errMsg = "\n\nPlease enter an integer for number of solutions.\n"
						+ "Defaulting to 1 solution.";
				numAnswers = 1;
				errCaught = true;
			}
			
			try
			{
				timeLimit = Integer.parseInt(timeLimitField.getText());
			}
			catch(NumberFormatException e)
			{
				if(errCaught)
					errMsg = "\n\nPlease enter an integer for number of solutions and time limit.\n"
						+ "Defaulting to 1 solution and 5 second time limit.";
				else
					errMsg = "\n\nPlease enter an integer for time limit.\n"
							+ "Defaulting to 5 seconds.";
				timeLimit = 5;
			}

			File tempFile = null;
			BufferedReader br = null;
			ProcessBuilder pb = null;
			Process proc = null;
			BufferedWriter writer = null;

			try 
			{
				tempFile = File.createTempFile("temp", ".txt");
				writer = new BufferedWriter(new FileWriter(tempFile));
				writer.write(codeArea.getText());
				writer.close();

				String[] command = {"clingo", tempFile.getPath(), "-n " + Integer.toString(numAnswers),
						"--time-limit=" + timeLimit};

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
						boolean tab = false;
						answers = line.split(" ");
						Collections.sort(Arrays.asList(answers));
						String answerKey = null;

						for(int i = 0; i < answers.length; i++)
						{
							String answer = answers[i];

							if(answer.contains("("))
							{
								if(answerKey == null)
								{
									if(i > 0)
										outputString += "\n\n";
									
									tab = false;
									answerKey = answer.split("\\(")[0];
								}
								else
								{
									if(!answer.split("\\(")[0].equals(answerKey))
									{
										outputString += "\n\n";
										answerKey = answer.split("\\(")[0];

										tab = false;
									}
								}
							}

							if(tab)
							{
								outputString += fiveSpaces + answers[i] + "\n";
								tab = false;
							}
							else
							{
								outputString += answers[i];	
								tab = true;
							}
							
							if(i == answers.length - 1)
								outputString += "\n\n";
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

				long elapsedTime = System.currentTimeMillis() - startTime;
				outputArea.setText(outputString);
				notificationArea.setText("Completed executing code in " + convertTime(elapsedTime)
						+ "\n" + numFoundAnswers + " Answers found" + errMsg);
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
		
		private String convertTime(long time)
		{
			String retVal = null;
			
			retVal = String.format("%ds.", 
				    TimeUnit.MILLISECONDS.toSeconds(time) - 
				    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
				);
			
			if(retVal.equals("0s."))
				retVal = time + "ms.";
			
			return retVal;
		}	//end convertTime
	}	//end ButtonListener
}	//end MainFrame
