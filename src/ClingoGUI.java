import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ClingoGUI 
{
	final private static String downloadLink = "http://sourceforge.net/projects/potassco/files/clingo/";

	public static void main(String[] args) throws IOException
	{
		String ver = getClingoVer();
		if(ver == null)
			initDialog();
		else
		{
			MainFrame frame = new MainFrame(ver);
			frame.setVisible(true);
		}	
	}	//end main
	
	private static void initDialog()
	{
		JDialog dialog = new JDialog();
		JPanel panel = new JPanel();
		
		JLabel label = new JLabel("Please download clingo and restart application.");
		
		JLabel linkLabel = new JLabel("Download Here");
		linkLabel.addMouseListener(new ClickListener());
		linkLabel.setForeground(Color.blue);
		
		panel.add(label);
		panel.add(linkLabel);
		
		dialog.addWindowListener(new DialogListener());
		dialog.setTitle("Download Clingo");
		dialog.setSize(350, 75);
		dialog.setLocationRelativeTo(null);
		dialog.add(panel);
		dialog.setVisible(true);
	}	//end initDialog
	
	private static String getClingoVer()
	{
		ProcessBuilder pb = null;
		Process proc = null;
		
		try 
		{
			String[] commands = {"clingo", "--version"};
			pb = new ProcessBuilder(commands);
			proc = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String ver = reader.readLine();
			return ver;
		} catch (IOException e) 
		{
			return null;
		}	
	}
	
	private static class ClickListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e) 
		{
			try 
			{
				java.awt.Desktop.getDesktop().browse(new URI(downloadLink));
				System.exit(0);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			
		}
		
		public void mouseEntered(MouseEvent e)
		{
			JLabel label = (JLabel)e.getSource();
			
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));;
		}
	}	//end ClickListener
	
	private static class DialogListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e) 
		{
			System.exit(0);
		}
	}	//end DialogListener
}	//end ClingoGUI
