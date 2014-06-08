package cs213.photoalbum.guiview;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import cs213.photoalbum.*;
import cs213.photoalbum.control.*;
import cs213.photoalbum.model.User;

/**
 * Creates a Login frame that allows the user to log in with a 
 * specified user id or as the admin
 * 
 * @author Sangini Shah
 *
 */
public class Login extends JFrame
{
	JLabel username, errorMessage;
	JTextField name;
	JButton login;
	JPanel uName, button, error;
	Control control;
	
	/**
	 * Constructor
	 * Creates a new Login object
	 * 
	 * @param c control object that handles/manipulates information
	 * 
	 */
	public Login(Control c)
	{
		super("Photo Album- Login");
		
		control = c;
		
		this.setSize(275,150);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		setLayout(new BorderLayout());
		name = new JTextField(10);
		username = new JLabel("Username:");
		uName = new JPanel(new FlowLayout());
		uName.add(username);
		uName.add(name);
		
		error = new JPanel(new FlowLayout());
		errorMessage = new JLabel(" ");
		errorMessage.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMessage.setForeground(Color.RED);
		error.add(errorMessage);
		
		button = new JPanel(new FlowLayout());
		login = new JButton("Login");
		button.add(login);
		login.addActionListener(new ButtonListener(this));
		
		add(uName, BorderLayout.NORTH);
		add(button, BorderLayout.CENTER);
		add(error, BorderLayout.SOUTH);

	}
	
	/**
	 * Action Listener for the buttons
	 * 
	 * @author Sangini Shah
	 *
	 */
	class ButtonListener implements ActionListener
	{

		Login frame;
		
		public ButtonListener(Login f)
		{
			frame = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String user = frame.name.getText();
			frame.errorMessage.setText(" ");
			try
			{
				frame.control.login(user);
				if(user.equalsIgnoreCase("admin"))
				{
					JFrame admin = new AdminView(control);
					frame.setVisible(false);
					admin.setVisible(true);
					frame.dispose();
				}
				else
				{
					JFrame userView = new UserView(control);
					frame.setVisible(false);
					userView.setVisible(true);
					frame.dispose();
				}
			}
			catch(IllegalArgumentException e)
			{
				frame.errorMessage.setText("Invalid User");
			}
		}
		
	}
	
	/**
	 * Main method. Runs program and prompts user to log in
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			Control ctrl = new Control();
			if(!ctrl.getUsers().contains(new User("admin", null)))
				ctrl.addUser("admin", "admin");
			JFrame frame = new Login(ctrl);
			frame.setVisible(true);
		} catch (FileNotFoundException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
		
	}
}

