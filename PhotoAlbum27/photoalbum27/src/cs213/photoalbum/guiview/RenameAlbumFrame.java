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
 * Prompts the user to input a new name for the selected album
 * 
 * @author Sangini Shah
 *
 */
public class RenameAlbumFrame extends JFrame {
	JLabel newName, errorMsg, blank;
	JTextField name;
	JPanel input;
	JButton rename, cancel;
	Control control;
	UserView parent;
	
	/**
	 * Constructor
	 * Creates a new RenameAlbumFrame object
	 * 
	 * @param ctrl control object that holds/manipulates information
	 * @param uv parent frame that calls the class
	 */
	public RenameAlbumFrame(Control ctrl, UserView uv)
	{
		super("Rename Album");
		setLayout(new FlowLayout());
		this.setSize(320, 150);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		parent = uv;
		newName = new JLabel("New name:");
		name = new JTextField(15);
		rename = new JButton("Rename");
		cancel = new JButton("Cancel");
		blank = new JLabel("                                                                                         ");
		errorMsg = new JLabel("");
		errorMsg.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMsg.setForeground(Color.RED);
				
		add(blank);
		add(newName);
		add(name);
		add(rename);
		add(cancel);
		add(errorMsg);
		
		cancel.addActionListener(new ActionListener(){
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		rename.addActionListener(new ActionListener(){
	
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
					int row = parent.albums.getSelectedRow();
					String oldName = (String)((DefaultTableModel)parent.albums.getModel()).getValueAt(row, 0);
					if(oldName.equals(aName))
						close();
					else
					{
						control.renameAlbum(oldName, aName);
						parent.message.setText("Album Renamed Successfully");
						parent.albums.setValueAt(aName, row, 0);
						close();
					}
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
	 *  Closes the current frame and returns focus to the parent frame
	 */
	private void close(){
		parent.setEnabled(true);
		parent.requestFocus();
		dispose();
	}
}
