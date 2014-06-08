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

import cs213.photoalbum.model.Photo;

/**
 * Allows the user to select which tag(s) to remove from the photo
 * 
 * @author Rich Gerdes
 *
 */
public class RemoveTagFrame extends JFrame {
		
	private JLabel label, label2, message;
	private JComboBox<String> tagTypes, tagValues;
	private JButton cancel, remove;
	
	private JFrame parent;

	/**
	 * Constructor
	 * Creates a new RemoveTagFrame object
	 * 
	 * @param photo photo object being edited
	 * @param frame frame calling the class
	 */
	public RemoveTagFrame(final Photo photo, JFrame frame){
		super("Remove Tag");
		this.parent = frame;

		this.setSize(300, 150);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		
		label = new JLabel("Tag Type:");
		label2 = new JLabel("Tag Value:");
		message = new JLabel(" ");
		message.setForeground(Color.RED);
		
		ArrayList<String> a = new ArrayList<String>();
		for(String key : photo.getTags().keySet()){
			a.add(key);
		}
		Object[] oa = a.toArray();
		String[] tags = new String[oa.length];
		for(int i = 0; i < oa.length; i++){
			tags[i] = (String) oa[i];
		}
		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		tagTypes = new JComboBox<String>(tags);
		tagValues = new JComboBox<String>();
		tagTypes.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tagValues.removeAllItems();
				for(String s : photo.getTags().get(tagTypes.getSelectedItem())){
					tagValues.addItem(s);
				}
				tagValues.setEnabled(true);
			}
		});

		if(tags.length == 0){
			tagTypes.setEnabled(false);
			tagValues.setEnabled(true);
			message.setText("No Tags");
		}else{
			tagValues.removeAllItems();
			for(String s : photo.getTags().get(tagTypes.getSelectedItem())){
				tagValues.addItem(s);
			}
			tagValues.setEnabled(true);
		}
		
		
		cancel = new JButton("Cancel");
		remove = new JButton("Remove");
		
		JPanel input = new JPanel();
		input.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		
		c.gridx = 0;
		input.add(label, c);
		c.gridx = 1;
		input.add(tagTypes, c);
		c.gridx=0;
		c.gridy = 1;
		input.add(label2, c);
		c.gridx = 1;
		input.add(tagValues, c);
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
				if(!tagValues.isEnabled())
					return;
				else{
					photo.deleteTag((String) tagTypes.getSelectedItem(), (String) tagValues.getSelectedItem());
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
