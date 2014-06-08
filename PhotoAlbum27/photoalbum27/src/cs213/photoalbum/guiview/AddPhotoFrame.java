package cs213.photoalbum.guiview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * Frame that prompts the user to input the file name of a photo to add 
 * to the current album
 * 
 * @author Rich Gerdes
 *
 */
public class AddPhotoFrame extends JFrame {
		
	private JLabel file, caption, message;
	private JTextField filename, captionText;
	private JButton cancel, add;
	
	private JFrame parent;
	
	/**
	 * Constructor
	 * Creates a new AddPhotoFrame object
	 * 
	 * @param album album that is currently in use
	 * @param frame parent frame that called the class
	 */
	public AddPhotoFrame(final Album album, JFrame frame){
		super("Add Photo");
		this.parent = frame;
		
		file = new JLabel("File name:");
		caption = new JLabel("Caption:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);


		filename = new JTextField(15);
		captionText = new JTextField(15);
		
		cancel = new JButton("Cancel");
		add = new JButton("Add");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		input.add(file, c);
		c.gridx = 1;
		input.add(filename, c);
		c.gridy = 1;
		c.gridx = 0;
		input.add(caption, c);
		c.gridx = 1;
		input.add(captionText, c);
		add(input);
		add(cancel);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		add(add);
		
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				File f = new File(filename.getText());
				if(f.exists() && !f.isDirectory() && !captionText.getText().equals("")){
					try{
						album.addPhoto(new Photo(filename.getText(), captionText.getText()));
						close();
					}catch(IllegalArgumentException e){
						if(e.getMessage().equals("File does not exist.")){
							message.setText("File does not exist");
						}
						else if(e.getMessage().equals("Photo Already Present in Album.")){
							message.setText("Photo already exists in album");
						}
					}
				}else{
					if(f.exists() && f.isDirectory())
						message.setText("Invalid File");
					if(!f.exists())
						message.setText("File does not exist");
					if(captionText.getText().equals(""))
						message.setText("Photo needs a caption");
				}
			}
			
		});
		
		add(message);
		
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
		this.setSize(260, 150);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setVisible(true);
		
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
