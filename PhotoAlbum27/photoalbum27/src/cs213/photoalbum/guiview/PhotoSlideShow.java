package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;
import cs213.photoalbum.model.User;

/**
 * Opens a slide show that allows the user to flip through all the
 * photos in the album consecutively
 * 
 * @author Rich Gerdes
 *
 */
public class PhotoSlideShow extends JFrame implements MessageDisplay {

	private JPanel buttonPanel, footerPanel;
	private JButton close, previous, next;
	private JLabel message;

	private PhotoSketch photoPane;

	private Album album;
	private Control control;
	
	private Photo[] photos;
	
	private int photo_index;
	
	private final JFrame parent;

	/**
	 * Constructor
	 * Creates a new PhotoSlideShow object
	 * 
	 * @param control control object that holds/manipulates information
	 * @param album album object that is currently being viewed
	 * @param parent JFrame object that called this class
	 */
	public PhotoSlideShow(Control control, Album album, JFrame parent) {
		super("Photo Album - Slide Show");
		
		this.parent = parent;

		this.album = album;
		this.control = control;
		Object[] o = album.getPhotos().toArray();
		photos = new Photo[o.length];
		for(int i = 0; i < o.length; i++){
			photos[i] = (Photo) o[i];
		}
		
		photo_index = 0;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 300);
		this.setMinimumSize(this.getSize());
		this.setSize(800, 600);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);

		GridBagConstraints c = new GridBagConstraints();

		footerPanel = new JPanel();
		buttonPanel = new JPanel();

		photoPane = new PhotoSketch(this);

		close = new JButton("Close");
		close.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
			
		});
		previous = new JButton("<<");
		previous.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				previous();
			}
			
		});
		next = new JButton(">>");
		next.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
			
		});

		message = new JLabel("Message: Nothing");

		c.fill = GridBagConstraints.BOTH;

		c.gridy = 0;
		c.weighty = 1;
		c.weightx = 1;
		this.add(photoPane, c);
		c.gridy = 1;
		c.weighty = 0;
		this.add(buttonPanel, c);
		c.gridy = 2;
		this.add(footerPanel, c);

		buttonPanel.add(previous);
		buttonPanel.add(close);
		buttonPanel.add(next);

		footerPanel.add(message);

		this.setVisible(true);
		
		update();

	}

	/**
	 * Updates the slideshow to display the next image
	 */
	private void update() {

		BufferedImage image;
		try {
			image = ImageIO.read(new File(photos[photo_index].getFileName()));
		} catch (IOException ex) {
			image = null;
		}
		photoPane.setImage(image);
		photoPane.repaint();
	}

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
	
	/**
	 * Closes the slide show and returns to the PhotoView frame
	 */
	private void exit(){
		parent.setVisible(true);
		parent.setEnabled(true);
		this.setVisible(false);
		this.dispose();
	}
	
	/**
	 * Displays the next consecutive photo in the album
	 */
	private void next(){
		photo_index++;
		if(photo_index >= photos.length){
			photo_index = 0;
		}
		update();
	}
	
	/**
	 * Displays the previous photo in the album
	 */
	private void previous(){
		photo_index--;
		if(photo_index < 0){
			photo_index = photos.length - 1;
		}
		update();
	}

}
