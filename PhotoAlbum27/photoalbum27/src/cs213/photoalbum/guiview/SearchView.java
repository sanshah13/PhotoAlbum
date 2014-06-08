package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;
import cs213.photoalbum.model.User;

/**
 * Displays the photos sorted by the given dates/tags and allows the user
 * to save the temporary album that is created with them
 * 
 * @author Rich Gerdes
 *
 */
public class SearchView extends JFrame implements MessageDisplay{

	JPanel buttonPanel, headerPanel, footerPanel, container;
	JButton save, back, slideShow;
	JLabel message, userName, albumName;
	
	PhotoList photos;
	PhotoPanel photoPane; 
	
	Album album;
	User user;
	Control control;

	/**
	 * Constructor
	 * Creates new SearchView object
	 * 
	 * @param control control object that holds/manipulates all information
	 * @param album temporary album that was created through a search
	 */
	public SearchView(final Control control, final Album album) {
		super("Photo Album");
		
		this.album = album;
		this.control = control;
		this.user = control.getUser();
		
		this.setSize(800, 500);
		this.setMinimumSize(this.getSize());
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		
		GridBagConstraints c = new GridBagConstraints();
		
		headerPanel = new JPanel();
		footerPanel = new JPanel();
		container = new JPanel();
		buttonPanel = new JPanel();
		
		photoPane = new PhotoPanel(this);
		
		photos = new PhotoList(album, photoPane, this);

		
		back = new JButton("Back to Albums");
		back.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				back();
			}
						
		});
		save = new JButton("Save");
		
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSaveWindow();
			}
						
		});
		slideShow = new JButton("Slide Show");
		
		slideShow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSlideShow();
			}
			
		});
		
		message = new JLabel("Message: Nothing");
		userName = new JLabel(user.getId());
		userName.setFont(new Font("Calibri", Font.BOLD, 26));
		albumName = new JLabel(album.getName());
		albumName.setFont(new Font("Calibri", Font.BOLD, 20));
		
		c.fill = GridBagConstraints.BOTH;
		
		c.ipadx = 5;
		c.ipady = 5;
		
		c.weightx = 1;
		this.add(headerPanel, c);
		c.gridy = 1;
		c.weighty = 1;
		this.add(container, c);
		c.gridy = 2;
		c.weighty = 0;
		this.add(buttonPanel, c);
		c.gridy = 3;
		this.add(footerPanel, c);
		
		buttonPanel.add(save);
		buttonPanel.add(slideShow);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		headerPanel.setLayout(new GridBagLayout());
		c.weightx = 0;
		c.insets = new Insets(5, 5, 10, 0);
		headerPanel.add(userName, c);
		c.weightx = 1;
		c.gridx = 1;
		c.insets = new Insets(20, 0, 0, 0);
		JPanel title = new JPanel();
		title.add(albumName);
		headerPanel.add(title, c);
		c.weightx = 0;
		c.gridx = 2;
		c.insets = new Insets(2, 0, 25, 2);
		headerPanel.add(back, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		container.setLayout(new GridBagLayout());
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 0, 0);
		container.add(photos, c);
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 0, 5, 0);
		container.add(photoPane, c);
		
		footerPanel.add(message);
		
		
		this.setVisible(true);
		
		this.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				refresh();
			}
			
			@Override
			public void focusLost(FocusEvent arg0) {}
			
		});
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				exit();
				}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
			
		});
	}

	/**
	 * Saves all information and closes the program
	 */
	protected void exit() {
		try {
			control.save();
		} catch (IOException e) {
		}
		this.dispose();
	}
	
	/**
	 * Opens a SaveFrame that allows the user to save the temp album
	 */
	protected void openSaveWindow() {
		this.setEnabled(false);
		new SaveFrame(album, control, this);
	}

	/**
	 * Allows the user to return to the UserView that called the search
	 */
	protected void back() {
		JFrame userView = new UserView(control);
		this.setVisible(false);
		userView.setVisible(true);
		this.dispose();
	}

	/**
	 * Creates and displays a PhotoSlideShow object so user can flip
	 * through pictures in the temporary album
	 */
	protected void openSlideShow(){
		this.setVisible(false);
		this.setEnabled(false);
		new PhotoSlideShow(control, album, this);
	}

	/**
	 * Updates the photo list and photo pane
	 */
	protected void refresh() {
		photos.update();
		photoPane.update();
	}
	
	/*

	public static void main(String[] args) {
		//User u = new User("username", "Full name");
		Album a = new Album("name");
		Photo p = new Photo("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg", "Desert");
		p.addTag("Location", "Sahara");
		a.addPhoto(p);
		a.addPhoto(new Photo("C:\\Users\\Public\\Pictures\\Sample Pictures\\Jellyfish.jpg", "Jelly Fish"));
		a.addPhoto(new Photo("C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala.jpg", "Koala"));
		
		
		Control c;
		try {
			c = new Control();
			c.login(c.getUsers().get(0).getId());
			//Album a = c.getAlbums().get(c.getAlbums().keySet().toArray()[1]);
			new SearchView(c, a);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	@Override
	public void sendMessage(String msg) {
		message.setText(msg);		
		message.setForeground(Color.BLACK);
	}

	@Override
	public void sendError(String err) {
		message.setText(err);
		message.setForeground(Color.RED);
	}

}
