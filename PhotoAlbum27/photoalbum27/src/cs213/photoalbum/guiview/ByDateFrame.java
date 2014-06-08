package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * Frame that prompts the user for a start and end date and opens a photoview
 * to display all the photos in the given date range
 * 
 * @author Sangini Shah
 *
 */
public class ByDateFrame extends JFrame {
	JLabel startDate, endDate, errorMsg;
	JTextField start, end;
	JPanel input;
	JButton list, cancel;
	Control control;
	UserView parent;
	
	/**
	 * Constructor
	 * Creates a new ByDateFrame object
	 * 
	 * @param ctrl control object currently holding/manipulating information
	 * @param a frame that called the class
	 */
	public ByDateFrame(Control ctrl, UserView a)
	{
		super("Search by Date");
		setLayout(new FlowLayout());
		this.setSize(280, 150);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		parent = a;
		startDate = new JLabel("Start Date:");
		start = new JTextField("MM/dd/yyyy-HH:mm:ss       ");
		endDate = new JLabel("End Date:");
		end = new JTextField("MM/dd/yyyy-HH:mm:ss       ");
		list = new JButton("List");
		cancel = new JButton("Cancel");
		errorMsg = new JLabel("");
		errorMsg.setFont(new Font("Arial Black", Font.PLAIN, 12));
		errorMsg.setForeground(Color.RED);
		input = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		input.add(startDate, c);
		c.gridx = 1;
		input.add(start, c);
		c.gridy = 1;
		c.gridx = 0;
		input.add(endDate, c);
		c.gridx = 1;
		input.add(end, c);
		add(input);		
		add(list);
		add(cancel);
		add(errorMsg);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		list.addActionListener(new ButtonListener(this));
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowOpened(WindowEvent e) {}
			
		});
		
	}
	
	/**
	 * ButtonListener class for the buttons
	 * 
	 * @author Sangini Shah
	 *
	 */
	class ButtonListener implements ActionListener
	{
		ByDateFrame frame;
		
		/**
		 * Constructor
		 * Creates a new ButtonListener object
		 * 
		 * @param bdf frame that contains the button that fired the action
		 */
		public ButtonListener(ByDateFrame bdf)
		{
			frame = bdf;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = start.getText();
			String e = end.getText();
			if(s == null || e == null || s.equals("MM/dd/yyyy-HH:mm:ss") || e.equals("MM/dd/yyyy-HH:mm:ss"))
			{
				errorMsg.setText("Invalid Date, Try Again");
				return;
			}
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
				sdf.setLenient(false);
				Date startDate = sdf.parse(s);
				Date endDate = sdf.parse(e);
				if(startDate.getTime() > endDate.getTime()){
					errorMsg.setText("Date order is incorrect, Try Again");
					return;
				}
				ArrayList<Photo> photos = control.getPhotosByDate(s, e);
				if(photos.size() == 0){
					errorMsg.setText("No Photos in Given Date Range");
				}
				else
				{
					Album a = new Album(s.trim() + " - " + e.trim());
					for(Photo p: photos)
					{
						a.addPhoto(p);
					}
					JFrame search = new SearchView(control, a);
					frame.setVisible(false);
					search.setVisible(true);
					frame.dispose();
				}
			}catch(Exception e1)
			{
				errorMsg.setText("Invalide Date, Try Again");
			}
		}
		
	}
	
	/**
	 *  Closes the current frame and returns focus to the parent frame
	 */
	private void close(){
		parent.setEnabled(true);
		parent.requestFocus();
		dispose();
	}
}
