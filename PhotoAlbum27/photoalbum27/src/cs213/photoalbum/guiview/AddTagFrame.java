package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs213.photoalbum.model.Photo;

/**
 * Frame that prompts the user to input a tag type-value pair to add to 
 * the specified photo
 * 
 * @author Rich Gerdes
 *
 */
public class AddTagFrame extends JFrame {
		
	private JLabel ttLabel, tvLabel, message;
	private JTextField tagType, tagValue;
	private JButton cancel, add;
	
	private JFrame parent;

	/**
	 * Constructor
	 * Creates a new AddTagFrame object
	 * 
	 * @param photo photo object to which the tag is being added
	 * @param frame parent frame that called the class
	 */
	public AddTagFrame(final Photo photo, JFrame frame){
		super("Add Photo");
		this.parent = frame;
		
		ttLabel = new JLabel("Tag Type");
		tvLabel = new JLabel("Tag Value:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);


		tagType = new JTextField(15);
		tagValue = new JTextField(15);
		
		cancel = new JButton("Cancel");
		add = new JButton("Add");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		input.add(ttLabel, c);
		c.gridx = 1;
		input.add(tagType, c);
		c.gridy = 1;
		c.gridx = 0;
		input.add(tvLabel, c);
		c.gridx = 1;
		input.add(tagValue, c);
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
				if(photo.getTags().containsKey(tagType.getText())){
					 for(String s : photo.getTags().get(tagType.getText())){
						 if(s.equals(tagValue.getText())){
							message.setText("");
							return;
						 }
					 }
				}
				if(tagType.getText().equals("") || tagValue.getText().equals(""))
				{
					message.setText("Need both type and value");
					return;
				}
				photo.addTag(tagType.getText(),tagValue.getText());
				close();
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
		this.setSize(300, 150);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		
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
