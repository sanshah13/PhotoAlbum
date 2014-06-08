package cs213.photoalbum.guiview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.*;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Photo;

/**
 * Frame that prompts the user to input a username and full name in order
 * to create a new user in the program. 
 * 
 * @author Sangini Shah
 *
 */
public class AddUserFrame extends JFrame {
	JLabel username, fullName, errorMsg;
	JTextField user, name;
	JPanel input;
	JButton create, cancel;
	Control control;
	AdminView parent;
	
	/**
	 * Constructor
	 * Creates a new AddUserFrame object
	 * 
	 * @param ctrl control object currently holding all information
	 * @param a parent frame that called the class
	 */
	public AddUserFrame(Control ctrl, AdminView a)
	{
		super("Create User");
		setLayout(new FlowLayout());
		this.setSize(320, 150);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		parent = a;
		username = new JLabel("Username:");
		user = new JTextField(15);
		fullName = new JLabel("Full name:");
		name = new JTextField(15);
		create = new JButton("Create");
		cancel = new JButton("Cancel");
		errorMsg = new JLabel("");
		errorMsg.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMsg.setForeground(Color.RED);
		input = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		input.add(username, c);
		c.gridx = 1;
		input.add(user, c);
		c.gridy = 1;
		c.gridx = 0;
		input.add(fullName, c);
		c.gridx = 1;
		input.add(name, c);
		add(input);		
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
				if(user.getText().trim().equals("") || name.getText().trim().equals(""))
				{
					errorMsg.setText("Invalid Username/Name, Try Again");
				}
				else{
					try{
						control.addUser(user.getText(), name.getText());
						parent.loadList();
						close();
					} catch (Exception e)
					{
						errorMsg.setText("Username Already Exists");
					}
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
