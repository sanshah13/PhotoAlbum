package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.User;

/**
 * Frame that holds all information that is visible to the admin user. 
 * Allows admin to view current users, add new users, and delete current users. 
 * 
 * @author Sangini Shah
 *
 */
public class AdminView extends JFrame{
	
	Control control;
	JButton logout, add, delete;
	JList<User> users;
	JLabel admin, errorMessage;
	
	/**
	 * Constructor
	 * Creates new AdminView object
	 * 
	 * @param ctrl control object currently holding/manipulating all information
	 */
	public AdminView(Control ctrl)
	{
		super("Photo Album- Admin");
		control = ctrl;
		
		this.setSize(400,310);
		//this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		logout = new JButton("Logout");
		add = new JButton("Add User");
		delete = new JButton("Delete User");
		errorMessage = new JLabel(" ");
		errorMessage.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMessage.setForeground(Color.RED);
		logout.addActionListener(new ButtonListener(this));
		add.addActionListener(new ButtonListener(this));
		delete.addActionListener(new ButtonListener(this));
		
		users = new JList<User>();
		users.addListSelectionListener(new ListListener());
		loadList();
		JScrollPane sP = new JScrollPane();
		sP.setViewportView(users);
		
		admin = new JLabel("Admin");
		admin.setFont(new Font("Calibri", Font.BOLD, 26));
		
		c.fill = GridBagConstraints.BOTH;
		
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = .5;
		c.weighty = 0;
		c.insets = new Insets(10, 10, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		this.add(admin, c);
		
		c.gridx = 3;
		c.insets = new Insets(15, 2, 2, 2);
		c.anchor = GridBagConstraints.EAST;
		this.add(logout, c);
		
		c.insets = new Insets(20, 2, 2, 2);
		c.gridx = 2;
		c.gridy = 1;
		this.add(add, c);
		
		c.gridx = 3;
		this.add(delete, c);
		
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 10;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.WEST;
		this.add(sP, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 12;
		c.gridheight = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(errorMessage, c);
		
		this.addWindowListener(new WindowListener(){
			
			@Override
			public void windowActivated(WindowEvent e) {}
	
			@Override
			public void windowClosed(WindowEvent e) {}
	
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					control.save();
				} catch (IOException e1) {
				}
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
	 * Loads information into the JList in order to update/display users
	 */
	public void loadList()
	{
		try {
			control.save();
		} catch (IOException e) {
		}
		ArrayList<User> u = (ArrayList<User>)control.getUsers().clone();
		u.remove(new User("admin", "admin"));
		User[] use = new User[u.size()];
		users.setListData(u.toArray(use));
	}
	
	/**
	 * List listener for the JList
	 * 
	 * @author Sangini Shah
	 *
	 */
	class ListListener implements ListSelectionListener
	{	
		public void valueChanged(ListSelectionEvent arg0)
		{
			errorMessage.setText(null);
		}
	}
	
	/**
	 * Action listener for the buttons
	 * 
	 * @author Sangini Shah
	 *
	 */
	class ButtonListener implements ActionListener
	{
		AdminView frame;
		
		/**
		 * Constructor
		 * Creates new ButtonListener object
		 * 
		 * @param av frame that contains the button that fired the action
		 */
		public ButtonListener(AdminView av)
		{
			frame = av;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JButton src = (JButton)e.getSource();
			if(src == frame.logout)
			{
				try {
					frame.control.logout();
					JFrame startup = new Login(control);
					frame.setVisible(false);
					startup.setVisible(true);
					frame.dispose();
				} catch (IOException e1) {
				}	
			}
			else if(src == frame.add)
			{
				frame.errorMessage.setText("");
				JFrame addUser = new AddUserFrame(control, frame);
				frame.setEnabled(false);
				addUser.setVisible(true);
			}
			else if(src == frame.delete)
			{
				frame.errorMessage.setText("");
				if(control.getUsers().size() <= 1)
				{
					frame.errorMessage.setText("No users to be deleted");
					return;
				}
				User delete = frame.users.getSelectedValue();
				try {
					control.deleteUser(delete.getId());
					frame.loadList();
				} catch (Exception e1) {
					frame.errorMessage.setText("Error: No user selected");
				}
			}
			
		}
		
	}
}
