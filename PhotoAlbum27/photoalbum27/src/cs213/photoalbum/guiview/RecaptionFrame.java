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

import cs213.photoalbum.model.Photo;

/**
 * Prompts the user to input a new caption for the photo
 *  
 * @author Rich Gerdes
 *
 */
public class RecaptionFrame extends JFrame {
		
	private JLabel caption, message;
	private JTextField captionText;
	private JButton cancel, add;
	
	private JFrame parent;

	/**
	 * Constructor
	 * Creates a new RecaptionFrame object
	 * 
	 * @param photo photo being recaptioned
	 * @param frame frame calling the class
	 */
	public RecaptionFrame(final Photo photo, JFrame frame){
		super("Recaption");
		this.parent = frame;
		
		caption = new JLabel("New Caption:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);

		captionText = new JTextField(15);
		captionText.setText(photo.getCaption());
		
		cancel = new JButton("Cancel");
		add = new JButton("Add");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		
		c.gridx = 0;
		input.add(caption, c);
		c.gridx = 1;
		input.add(captionText, c);
		add(input);
		JPanel pane = new JPanel();
		pane.add(cancel);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		pane.add(add);
		
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(captionText.getText().equals(photo.getCaption())){
					System.out.print("a");
					message.setText("No Change made");
				}else if(captionText.getText().trim().equals("")){
					message.setText("Caption Required");
				}else{
					photo.setCaption(captionText.getText());
					close();
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
	 *  Closes the current frame and returns focus to the parent frame
	 */
	private void close(){
		parent.setEnabled(true);
		parent.requestFocus();
		dispose();
	}

}
