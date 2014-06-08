package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;

/**
 * Prompts the user to rename and save the temporary album created 
 * by the search options in AlbumView
 * 
 * @author Rich Gerdes
 *
 */
public class SaveFrame extends JFrame {
		
	private JLabel caption, message;
	private JTextField albumName;
	private JButton cancel, save;
	
	private JFrame parent;

	/**
	 * Constructor
	 * Creates new SaveFrame object
	 * 
	 * @param album temporary album object
	 * @param control control object that holds/manipualtes all information
	 * @param frame frame that calls the class
	 */
	public SaveFrame(final Album album, final Control control, JFrame frame){
		super("Rename");
		this.parent = frame;
		
		caption = new JLabel("Rename:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);

		albumName = new JTextField(15);
		albumName.setText(album.getName());
		
		cancel = new JButton("Cancel");
		save = new JButton("Add");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		
		c.gridx = 0;
		input.add(caption, c);
		c.gridx = 1;
		input.add(albumName, c);
		add(input);
		JPanel pane = new JPanel();
		pane.add(cancel);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		pane.add(save);
		
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				album.setName(albumName.getText());
				try{
					control.getUser().addAlbum(album);
					openPhotoView(control, album);
				}
				catch(Exception e){
					message.setText("Album Name in Use");
				}
				
			}
			
		});
		
		add(pane);
		JPanel pane2 = new JPanel();
		pane2.add(message);
		add(pane2);
		
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
		this.setSize(300, 150);
		this.setLayout(new GridLayout(0,1));
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
		
	}
	
	/**
	 * Opens the PhotoView to display all photos in the album and 
	 * edit them like a normal album
	 * 
	 * @param c control object that holds/manipulates all information
	 * @param a album object that has just been created and added to the user
	 */
	protected void openPhotoView(Control c, Album a) {
		parent.dispose();
		this.dispose();
		new PhotoView(c, a);
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
