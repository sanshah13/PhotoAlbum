package cs213.photoalbum.guiview;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import cs213.photoalbum.model.Photo;

/**
 * Creates/Organizes a JList cell that displays the thumbnails and information of
 * photos in an album
 * 
 * @author Rich Gerdes
 *
 */
public class ListRender implements ListCellRenderer<Photo> {
	
	MessageDisplay frame;
	
	/**
	 * Constructor
	 * Creates new ListRender object
	 * 
	 * @param frame frame that contains the list
	 */
	public ListRender(MessageDisplay frame){
		this.frame = frame;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Photo> list,
			Photo value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.ipadx = 5;
		c.ipady = 5;

		JLabel label = new JLabel("");
		try {
		BufferedImage i = ImageIO.read(new File(value.getFileName()));
	    Image newimg = i.getScaledInstance(100, (int)((((double) 100 / i.getWidth())) * (double) i.getHeight()),  java.awt.Image.SCALE_SMOOTH);
		ImageIcon img = new ImageIcon(newimg);
		label.setIcon(img);
		} catch (IOException e) {
		
		}  
		
		panel.add(label, c);
		
		JPanel p2 = new JPanel(new GridLayout(0,1));
		p2.add(new JLabel(value.getCaption()));
		p2.add(new JLabel(new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH).format(value.getDate())));
		
		panel.add(p2, c);
		
		if (isSelected) {
			panel.setBackground(Color.LIGHT_GRAY);
			p2.setBackground(Color.LIGHT_GRAY);
		} else {
			panel.setBackground(Color.WHITE);
			p2.setBackground(Color.WHITE);
		}

		return panel;
	}

}