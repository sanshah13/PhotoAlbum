package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * Displays all the albums contained in the User and allows the user 
 * to rename, add, delete, and open albums or to search for photos
 * by tags or by date
 * 
 * @author Sangini Shah
 *
 */
public class UserView extends JFrame
{
	JLabel username, search, message;
	JButton logout, create, delete, rename, open, tag, date;
	JTable albums;
	JPanel buttons, searchBy, logOut;
	Control control;
	
	/**
	 * Constructor
	 * Creates a new UserView object
	 * 
	 * @param ctrl control object that holds/manipulates information
	 */
	public UserView(Control ctrl)
	{
		super("Photo Album- UserView");
		
		this.setSize(550, 420);
		//this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		control = ctrl;
		username = new JLabel(control.getUser().getId());
		username.setFont(new Font("Calibri", Font.BOLD, 26));
		search = new JLabel("Search by:");
		message = new JLabel(" ");
		message.setFont(new Font("Arial Black", Font.PLAIN, 14));
		logout = new JButton("Logout");
		create = new JButton("Create");
		delete = new JButton("Delete");
		rename = new JButton("Rename");
		open = new JButton("Open");
		tag = new JButton("Tag");
		date = new JButton("Date");
		buttons = new JPanel(new FlowLayout());
		searchBy = new JPanel(new FlowLayout());
		logOut = new JPanel(new FlowLayout());
		
		buttons.add(create);
		buttons.add(delete);
		buttons.add(rename);
		buttons.add(open);
		
		searchBy.add(search);
		searchBy.add(tag);
		searchBy.add(date);
		
		logOut.add(logout);
		
		String[] colNames = {"Album Name", "Number of Photos", "Start Date", "End Date"};
		DefaultTableModel tbModel = new DefaultTableModel(colNames, 0);
		albums = new JTable(tbModel){
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		albums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sP = new JScrollPane();
		sP.setViewportView(albums);
		Collection<Album> list = control.getAlbums().values();
		loadTable(list);
		albums.setEnabled(true);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = .5;
		c.weighty = 0;
		c.insets = new Insets(10, 10, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		this.add(username, c);
		
		c.weightx = 0;
		c.gridx = 7;
		c.insets = new Insets(15, 100, 2, 2);
		c.anchor = GridBagConstraints.EAST;
		this.add(logOut, c);
		
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(10,5,10,0);
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 6;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(sP, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridy = 8;
		c.gridheight = 1;
		c.gridwidth = 4;
		this.add(buttons, c);
		
		c.gridx = 5;
		c.gridwidth = 3;
		this.add(searchBy, c);
		
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = GridBagConstraints.REMAINDER;
		message.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(message, c);
		
		logout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					control.logout();
					JFrame startup = new Login(control);
					setVisible(false);
					startup.setVisible(true);
					dispose();
				} catch (IOException e1) {
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
			
				try {
					control.save();
					} catch (IOException e1) {
					}
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
		
		create.addActionListener(new ButtonListener(this));
		delete.addActionListener(new ButtonListener(this));
		rename.addActionListener(new ButtonListener(this));
		open.addActionListener(new ButtonListener(this));
		tag.addActionListener(new ButtonListener(this));
		date.addActionListener(new ButtonListener(this));
	}
	
	
	/**
	 * Button Listener for the buttons
	 * 
	 * @author Sangini Shah
	 *
	 */
	class ButtonListener implements ActionListener
	{
		UserView frame;
		
		public ButtonListener(UserView f)
		{
			frame = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			frame.message.setForeground(Color.BLACK);
			if(e.getSource() == frame.create)
			{
				frame.message.setText("");
				JFrame addAlbum = new AddAlbumFrame(control, frame);
				frame.setEnabled(false);
				addAlbum.setVisible(true);
			}
			else if(e.getSource() == frame.delete)
			{
				frame.message.setText("");
				try{
					int row = albums.getSelectedRow();
					String a = (String) ((DefaultTableModel) albums.getModel()).getValueAt(row, 0);
					control.deleteAlbum(a);
					((DefaultTableModel)albums.getModel()).removeRow(row);
					frame.message.setText("Album Deleted Sucessfully");
				}
				catch(Exception e1)
				{
					frame.message.setForeground(Color.RED);
					frame.message.setText("No Album Selected");
				}
			}
			else if(e.getSource() == frame.rename)
			{
				frame.message.setText("");
				int row = albums.getSelectedRow();
				if(row < 0)
				{
					frame.message.setText("No Album Selected");
					return;
				}
				JFrame renameAlbum = new RenameAlbumFrame(control, frame);
				frame.setEnabled(false);
				renameAlbum.setVisible(true);
			}
			else if(e.getSource() == frame.open)
			{
				int row = albums.getSelectedRow();
				if(row < 0)
				{
					frame.message.setText("No Album Selected");
					return;
				}
				String aname = (String)((DefaultTableModel)albums.getModel()).getValueAt(row, 0);
				Album a = control.getAlbums().get(aname);
				JFrame photoView = new PhotoView(control, a);
				frame.setVisible(false);
				photoView.setVisible(true);
				frame.dispose();
			}
			else if(e.getSource() == frame.tag)
			{
				if(control.getUser().getAlbums().values().size() != 0)
				{
						JFrame byTag = new ByTagFrame(control, frame);
						frame.setEnabled(false);
						byTag.setVisible(true);
				}
			}
			else if(e.getSource() == frame.date)
			{
				if(control.getUser().getAlbums().values().size() != 0)
				{
					JFrame byDate = new ByDateFrame(control, frame);
					frame.setEnabled(false);
					byDate.setVisible(true);
				}
			}
		}
	}
	
	/**
	 * Loads and updates information in the table
	 * 
	 * @param toAdd albums to be added to the list
	 */
	public void loadTable(Collection<Album> toAdd)
	{
		for(Album a: toAdd)
		{
			String[] values = new String[4];
			values[0] = a.getName();
			ArrayList<Photo> photos = a.getPhotos();
			values[1] = Integer.toString(photos.size());
			if(photos.size() != 0)
			{
				Collections.sort(photos);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
				values[2] = sdf.format(photos.get(0).getDate());
				values[3] = sdf.format(photos.get(photos.size()-1).getDate());
			}
			else
			{
				values[2] = "-";
				values[3] = "-";
			}
			((DefaultTableModel)albums.getModel()).addRow(values);			
		}
	}
}
