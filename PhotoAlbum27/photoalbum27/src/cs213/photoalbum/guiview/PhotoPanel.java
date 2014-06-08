package cs213.photoalbum.guiview;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs213.photoalbum.model.Photo;

/**
 * Panel that displays the full size image and lists its date, caption
 * and any tags that it contains
 * 
 * @author Rich Gerdes
 *
 */
public class PhotoPanel extends JPanel {

	private PhotoSketch sketch;
	private Photo photo;
	private JPanel dataView;
	private JLabel caption, date, tagLabel, tagList;

	/**
	 * Constructor
	 * Creates a new PhotoPanel object
	 * 
	 * @param photoView frame that contains this panel
	 */
	public PhotoPanel(MessageDisplay photoView) {
		sketch = new PhotoSketch(photoView);
		dataView = new JPanel();

		caption = new JLabel("No Photo found");
		date = new JLabel("XX/XX/XXXX-XX:XX");
		tagLabel = new JLabel("Tags:");
		tagList = new JLabel("Tag List");

		dataView.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridwidth = 1;

		c.weightx = 1;
		c.gridy = 0;
		dataView.add(caption, c);
		c.gridy = 1;
		dataView.add(date, c);
		c.gridy = 2;
		dataView.add(tagLabel, c);
		c.gridy = 3;
		dataView.add(tagList, c);

		this.setLayout(new BorderLayout());

		this.add(sketch, BorderLayout.CENTER);
		this.add(dataView, BorderLayout.SOUTH);
	}

	/**
	 * Sets photo to be displayed by the panel
	 * 
	 * @param p photo object to be displayed
	 */
	public void setPhoto(Photo p) {
		photo = p;
	}

	/**
	 * Updates the panel to display the photo selected in the list
	 */
	public void update() {
		BufferedImage image;
		if(photo != null){
			try {
				image = ImageIO.read(new File(photo.getFileName()));
			} catch (IOException ex) {
				image = null;
			}
			sketch.setImage(image);
			sketch.repaint();
	
			caption.setText(photo.getCaption());
			date.setText(new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH)
					.format(photo.getDate()));
			String tagString = "";
			HashMap<String, ArrayList<String>> tags = photo.getTags();
			Set<String> keys = tags.keySet();
			for (String key : keys) {
				tagString += key + ":";
				for (String s : tags.get(key)) {
					tagString += "\"" + s + "\",";
				}
				tagString = tagString.substring(0, tagString.length() - 1);
			}
			if (tagString != "") {
				tagLabel.setText("Tags:");
				tagList.setText(tagString);
			} else {
				tagLabel.setText("");
				tagList.setText("");
			}

		}else{
			image = null;
			sketch.setImage(image);
			sketch.repaint();
			caption.setText("No Photo found");
			date.setText("XX/XX/XXXX-XX:XX");
			tagLabel.setText("Tags:");
			tagList.setText("Tag List");
		}

	}

	/**
	 * Returns the current photo being displayed
	 * 
	 * @return photo being displayed
	 */
	public Photo getPhoto() {
		return this.photo;
	}

}
