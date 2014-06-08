package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * Frame that takes multiple tags as input and searches for 
 * photos with the given tags
 * 
 * @author Rich Gerdes
 *
 */
public class ByTagFrame extends JFrame {
	JLabel tagType, tagValue, errorMsg;
	JTextField type, value;
	JPanel input;
	JButton list, cancel, add, delete;
	Control control;
	UserView parent;
	JList tags;

	/**
	 * Constructor
	 * Creates a new ByTagFrame object
	 * 
	 * @param ctrl control object currently holding/manipulating all information
	 * @param a frame that calls the class
	 */
	public ByTagFrame(Control ctrl, UserView a)
	{
		super("Search By Tag");
		setLayout(new FlowLayout());
		this.setSize(400, 240);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		parent = a;
		input = new JPanel(new GridBagLayout());
		

		tagType = new JLabel("Tag Type (Optional):");
		type = new JTextField(15);
		tagValue = new JLabel("Tag Value:");
		value = new JTextField(15);
		add = new JButton("add");
		delete = new JButton("delete");
		
		final DefaultListModel<String> model = new DefaultListModel<String>();
		tags = new JList<String>(model);
		tags.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tags.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		tags.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(tags);
		listScroller.setPreferredSize(new Dimension(250, 80));	
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.ipadx = 5;
		c.anchor = GridBagConstraints.EAST;
		input.add(tagType, c);
		c.gridx = 1;
		input.add(type, c);
		c.gridy = 1;
		c.gridx = 0;
		input.add(tagValue, c);
		c.gridx = 1;
		input.add(value, c);
		c.gridx = 2;
		c.gridy--;
		c.insets = new Insets(0,5,0,5);
		c.anchor = GridBagConstraints.WEST;
		input.add(add, c);
		c.gridy++;
		c.insets = new Insets(5,5,0,0);
		input.add(delete, c);
		
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				errorMsg.setText("");
				if(!value.getText().trim().equals("")){
					String s = "";
					if(!type.getText().trim().equals("")){
						s = type.getText() + ":";
					}
					s += "\"" + value.getText() + "\"";
					if(!model.contains(s))
						model.addElement(s);
				}
			}
			
		});
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				errorMsg.setText("");
				int index = tags.getSelectedIndex();
				if(index < 0)
				{
					errorMsg.setText("No tag selected");
					return;
				}
				else
				{
					model.remove(index);
				}
				
			}
			
		});
		
		JPanel bottom = new JPanel(new GridBagLayout());
		list = new JButton("List");
		cancel = new JButton("Cancel");
		errorMsg = new JLabel("");
		errorMsg.setFont(new Font("Arial Black", Font.PLAIN, 14));
		errorMsg.setForeground(Color.RED);
		errorMsg.setHorizontalAlignment(SwingConstants.CENTER);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0,0,0,0);
		bottom.add(list, c);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.insets = new Insets(0,5,0,0);
		bottom.add(cancel, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5,0,0,0);
		bottom.add(errorMsg, c);
		
		add(listScroller);
		add(input);		
		add(bottom);
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
			
		});
		
		list.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				errorMsg.setText("");
				if(model.getSize() > 0){
					String name = "";
					ArrayList<String> ar = new ArrayList<String>();
					for(Object o : model.toArray()){
						ar.add(((String) o).replace("\"", ""));
						name += (String) o.toString() + " ";
					}
					ArrayList<Photo> photos = control.getPhotosByTag(ar);
					if(photos.size() == 0)
					{
						errorMsg.setText("No photos with specified tag(s)");
						return;
					}
					Album a = new Album(name);
					for(Photo p : photos){
						a.addPhoto(p);
					}
					
					setVisible(false);
					new SearchView(control, a);
					dispose();
					parent.dispose();
				}else{
					errorMsg.setText("No Tags Entered");
				}
			}
		});
		
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
