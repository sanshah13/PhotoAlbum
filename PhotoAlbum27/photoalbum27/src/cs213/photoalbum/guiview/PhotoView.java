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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;
import cs213.photoalbum.model.User;

/**
 * Displays all the photos present in the album as well as their
 * expanded views and gives the user to edit photo attributes as well
 * as adding and deleting photos
 * 
 * @author Rich Gerdes
 *
 */
public class PhotoView extends JFrame implements MessageDisplay{

	JPanel buttonPanel, headerPanel, footerPanel,
			container;
	JButton back, add, remove, recaption, addTag, removeTag, move, slideShow;
	JLabel message, userName, albumName;
	
	PhotoList photos;
	PhotoPanel photoPane; 
	
	Album album;
	User user;
	Control control;

	/**
	 * Constructor
	 * Creates new PhotoView object
	 * 
	 * @param control control object that holds/manipulates the information
	 * @param album album object that is currently opened by the user
	 */
	public PhotoView(final Control control, final Album album) {
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

		back = new JButton("Back");
		add = new JButton("Add Photo");
		
		back.addActionListener(new ButtonListener(this));
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openAddWindow();
			}
						
		});
		
		remove = new JButton("Remove Photo");
		remove.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.removePhoto(photoPane.getPhoto().getFileName(), album.getName());
				refresh();
			}
						
		});
		recaption = new JButton("Recaption Photo");
		recaption.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openCaptionWindow();
			}
						
		});
		addTag = new JButton("Add Tag");
		addTag.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openAddTagWindow();
			}
						
		});
		removeTag = new JButton("Remove Tag");
		removeTag.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openRemoveTagFrame();
			}
						
		});
		move = new JButton("Move Photo");
		move.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openMoveFrame();
			}
						
		});
		slideShow = new JButton("Slide Show");
		
		slideShow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(album.getPhotos().size() != 0)
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
		
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(recaption);
		buttonPanel.add(addTag);
		buttonPanel.add(removeTag);
		buttonPanel.add(move);
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
		
		if(album.getPhotos().size() == 0)
		{
			JOptionPane.showMessageDialog(null, "No Photos in Album", "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Closes the program and saves the data
	 */
	protected void exit() {
		try {
			control.save();
		} catch (IOException e) {
		}
		this.dispose();
	}
	
	/**
	 * Button Listener for the buttons
	 * 
	 * @author Rich Gerdes
	 *
	 */
	class ButtonListener implements ActionListener{

		PhotoView pv;
		
		public ButtonListener(PhotoView p)
		{
			pv = p;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame userView = new UserView(control);
			pv.setVisible(false);
			userView.setVisible(true);
			pv.dispose();		
		}
		
	}
	
	/**
	 * Opens a MoveFrame to give the user the option to choose which
	 * album to move the photo to
	 */
	protected void openMoveFrame() {
		if(photoPane.getPhoto() == null)
			return;
		this.setEnabled(false);
		new MoveFrame(control, photoPane.getPhoto(), album, this);
	}

	/**
	 * Opens a RemoveTagFrame to give the user the option to delete
	 * certain tags from the photo
	 */
	protected void openRemoveTagFrame() {
		if(photoPane.getPhoto() == null)
			return;
		this.setEnabled(false);
		new RemoveTagFrame(photoPane.getPhoto(), this);	
	}

	/**
	 * Opens an AddTagFrame to give the user the option to add a new
	 * tag to the photo
	 */
	protected void openAddTagWindow() {
		if(photoPane.getPhoto() == null)
			return;
		this.setEnabled(false);
		new AddTagFrame(photoPane.getPhoto(), this);	
	}

	/**
	 * Opens a PhotoSlideShow frame to allow the user to view a slide
	 * show of the photos in the album
	 */
	public void openSlideShow(){
		this.setVisible(false);
		this.setEnabled(false);
		new PhotoSlideShow(control, album, this);
	}

	/**
	 * Opens an AddPhotoFrame window to allow the user to add a new 
	 * photo to the album
	 */
	protected void openAddWindow() {
		this.setEnabled(false);
		new AddPhotoFrame(album, this);
	}

	/**
	 * Opens a RecaptionFrame window to allow the user to set a new
	 * caption to the photo
	 */
	protected void openCaptionWindow() {
		if(photoPane.getPhoto() == null)
			return;
		this.setEnabled(false);
		new RecaptionFrame(photoPane.getPhoto(), this);		
	}

	/**
	 * Refreshes the photo list and photo pane
	 */
	private void refresh() {
		photos.update();
		photoPane.update();
	}

	/*public static void main(String[] args) {
		//User u = new User("username", "Full name");
		Album a = new Album("name");
		Photo p = new Photo("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg", "Desert");
		p.addTag("Location", "Sahara");
		a.addPhoto(p);
		a.addPhoto(new Photo("C:\\Users\\Public\\Pictures\\Sample Pictures\\Jellyfish.jpg", "Jelly Fish"));
		
		
		Control c;
		try {
			c = new Control();
			c.login(c.getUsers().get(0).getId());
			//Album a = c.getAlbums().get(c.getAlbums().keySet().toArray()[1]);
			new PhotoView(c, a);
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
	}*/

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
