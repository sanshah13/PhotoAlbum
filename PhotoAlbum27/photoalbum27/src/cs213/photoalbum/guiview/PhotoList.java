package cs213.photoalbum.guiview;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;

/**
 * List of photos that contains multiple JPanels with thumbnails of 
 * pictures and their corresponding information
 * 
 * @author Rich Gerdes
 *
 */
public class PhotoList extends JPanel implements ListSelectionListener {

	Album album;
	PhotoPanel photoPanel;
	private JList<Photo> list;
	private DefaultListModel<Photo> listModel;
	
	boolean stopper = false;
	private MessageDisplay parent;

	/**
	 * Constructor
	 * Creates a new PhotoList object
	 * 
	 * @param album album object with all photos that need to be listed
	 * @param photoPanel photoPanel object that would display the first photo of the album
	 * @param parent MessageDisplay object that contains this list
	 */
	public PhotoList(Album album, PhotoPanel photoPanel, MessageDisplay parent) {
		this.album = album;
		this.photoPanel = photoPanel;
		this.parent = parent;
		this.setMinimumSize(new Dimension(250, 500));
		
		setLayout(new BorderLayout());

		listModel = new DefaultListModel<Photo>();
		list = new JList<Photo>(listModel);
		list.setCellRenderer(new ListRender(parent));
		
		update();

		// Create the list and put it in a scroll pane.
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		JScrollPane listScrollPane = new JScrollPane(list);

		add(listScrollPane, BorderLayout.CENTER);
		
		valueChanged(null);
	}

	/**
	 * Updates the list based on any photos added/removed
	 */
	public void update() {
		for(Photo p : album.getPhotos()){
			if(!listModel.contains(p))
				listModel.addElement(p);
		}
		
		for(Object p : listModel.toArray()){
			if(!album.getPhotos().contains(p)){
				listModel.removeElement(p);
			}
		}
		
		list.setSelectedIndex(0);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		photoPanel.setPhoto(list.getSelectedValue());
		photoPanel.update();
	}
}
