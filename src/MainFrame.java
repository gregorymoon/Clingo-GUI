import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ComponentListener 
{
	//declare public variables
	public static OutputPanel oPanel;
	public static ControlPanel controlPanel;
	public static CodePanel codePanel;
	public static File currCodeFile, currOutputFile;
	public static MainFrame currFrame;

	public MainFrame(String ver)
	{		
		initComponents();
		this.setTitle("Clingo GUI - " + ver);
	}	//end MainFrame Constructor

	private void initComponents()
	{	
		//initialize public variables
		controlPanel = new ControlPanel();
		oPanel = new OutputPanel();
		codePanel = new CodePanel();

		currOutputFile = null;
		currCodeFile = null;
		currFrame = this;

		//set up panels
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(new JLabel("Code:"), BorderLayout.NORTH);
		westPanel.add(codePanel, BorderLayout.CENTER);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(2, 0));
		eastPanel.add(oPanel);
		eastPanel.add(controlPanel);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(westPanel);
		mainPanel.add(eastPanel);

		//set up main frame
		this.setSize(1020, 790);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(this);
		this.add(mainPanel);
	}	//end initComponents

	public void componentResized(ComponentEvent e) 
	{

	}	//end componentResized

	public void componentMoved(ComponentEvent e) {}

	public void componentShown(ComponentEvent e) {}

	public void componentHidden(ComponentEvent e) {}
}	//end MainFrame
