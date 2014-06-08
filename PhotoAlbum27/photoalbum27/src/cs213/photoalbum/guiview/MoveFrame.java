package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * Frame that prompts the user to input the album to which they
 * want to move the specified photo 
 * 
 * @author Rich Gerdes
 *
 */
public class MoveFrame extends JFrame {
		
	private JLabel label, message;
	private JComboBox<String> album;
	private JButton cancel, remove;
	
	private PhotoView parent;

	/**
	 * Constructor
	 * Creates a new MoveFrame object
	 * 
	 * @param control control object that is holding/manipulating information
	 * @param photo photo object to be moved
	 * @param current current album object photo is in
	 * @param frame PhotoView object that called this class
	 */
	public MoveFrame(final Control control, final Photo photo, final Album current, PhotoView frame){
		super("Move Photo");
		this.parent = frame;

		this.setSize(300, 150);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		
		label = new JLabel("Album:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);
		
		ArrayList<String> a = new ArrayList<String>();
		for(String key : control.getAlbums().keySet()){
			if(!key.equals(current.getName()))
			a.add(key);
		}
		Object[] oa = a.toArray();
		String[] list = new String[oa.length];
		for(int i = 0; i < oa.length; i++){
			list[i] = (String) oa[i];
		}
		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		album = new JComboBox<String>(list);

		if(list.length == 0){
			album.setEnabled(false);
			message.setText("No Other Albums");
		}
		
		cancel = new JButton("Cancel");
		remove = new JButton("Move");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		
		c.gridx = 0;
		input.add(label, c);
		c.gridx = 1;
		input.add(album, c);
		
		c = new GridBagConstraints();
		add(input, c);
		JPanel pane = new JPanel();
		pane.add(cancel);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		pane.add(remove);
		
		remove.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!album.isEnabled())
					return;
				else{
					try{
						control.movePhoto(photo.getFileName(), current.getName(), (String) album.getSelectedItem());
					}
					catch(Exception e)
					{
						message.setText("Photo Already Present in Album");
						return;
					}
					parent.photos.update();
					parent.photoPane.setPhoto(null);
					parent.photoPane.update();
					
				}
				close();
			}
			
		});
		
		c.gridy = 1;
		add(pane, c);
		JPanel pane2 = new JPanel();
		pane2.add(message);
		c.gridy = 2;
		add(pane2, c);
		
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
		
		this.setVisible(true);
		
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
