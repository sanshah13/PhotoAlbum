package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
 /**
  * Frame that prompts the user to input the name for a new album 
  * that will be added to the user
  * 
  * @author Sangini Shah
  * 
  */
public class AddAlbumFrame extends JFrame
{
	JLabel albumName, errorMsg, blank;
	JTextField name;
	JPanel input;
	JButton create, cancel;
	Control control;
	UserView parent;
	
	/**
	 * Constructor
	 * Creates a new AlbumFrame object
	 * 
	 * @param ctrl control object currently holding/manipulating all information
	 * @param uv parent frame that called the class
	 */
	public AddAlbumFrame(Control ctrl, UserView uv)
	{
		super("Create Album");
		setLayout(new FlowLayout());
		this.setSize(320, 150);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		parent = uv;
		albumName = new JLabel("Album name:");
		name = new JTextField(15);
		create = new JButton("Create");
		cancel = new JButton("Cancel");
		blank = new JLabel("                                                                                         ");
		errorMsg = new JLabel("");
		errorMsg.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMsg.setForeground(Color.RED);
				
		add(blank);
		add(albumName);
		add(name);
		add(create);
		add(cancel);
		add(errorMsg);
		
		cancel.addActionListener(new ActionListener(){
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		create.addActionListener(new ActionListener(){
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				errorMsg.setText("");
				String aName = name.getText();
				if(aName.trim().equals("") || aName == null)
				{
					errorMsg.setText("Invalid Name, Try Again");
					return;
				}
				try
				{
					control.createAlbum(aName);
					ArrayList<Album> temp = new ArrayList<Album>();
					temp.add(control.getAlbums().get(aName));
					parent.loadTable(temp);
					close();
					parent.message.setText("Album Added Successfully");
				}
				catch(Exception e)
				{
					errorMsg.setText("Album Name Already Exists");
				}
			}	
		});
		
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
	 * Closes the current frame and returns focus to the parent frame
	 */
	private void close(){
		parent.setEnabled(true);
		parent.requestFocus();
		dispose();
	}
}
