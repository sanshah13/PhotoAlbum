package cs213.photoalbum.model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * Reference to a photo
 * 
 * Stores file name, caption, date and tags
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public class Photo implements Serializable, Comparable<Photo>{
	
	private static final long serialVersionUID = -2069780867775208396L;
	private String fileName;
	private String caption;
	private Calendar cal;
	private HashMap<String, ArrayList<String>> tags;
	private File file;
	private ArrayList<Album> albums;
	
	/**
	 * Photo Constructor
	 * Creates a record for a photo
	 * Stores the filename and photo tags
	 * 
	 * @param fileName	url location of the file
	 * @param caption	caption for the photo
	 * @throws IllegalArgumentException if file is not found, exception is thrown
	 */
	public Photo(String fileName, String caption) {
		this.fileName = fileName;
		this.caption = caption;
		this.cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		tags = new HashMap<String, ArrayList<String>>();
		file = new File(fileName);
		albums = new ArrayList<Album>();
		if(!file.exists())
		{
			throw new IllegalArgumentException("File does not exist.");
		}
	}
	
	/**
	 * Returns the filename of the photo
	 * 
	 * @return Filename of the photo
	 */
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * Returns the caption for the photo
	 * 
	 * @return Caption for the photo.
	 */
	public String getCaption(){
		return caption;
	}

	/**
	 * Changes the caption of the photo to the provided string
	 * 
	 * @param caption	the new caption to be given to the photo
	 */
	public void setCaption(String caption){
		this.caption = caption;
	}

	/**
	 * Add a new tag to the photo with a specified name and value
	 * 
	 * @param tag	name of tag to add
	 * @param value	value of new tag
	 * @throws IllegalArgumentException	throws exception if tag is already present in photo
	 */
	public void addTag(String tag, String value){
		if(tag.equalsIgnoreCase("location"))
		{
			if(tags.containsKey(tag))
			{
				throw new IllegalArgumentException("Already has a location.");
			}
			else
			{
				ArrayList<String> list = new ArrayList<String>();
				list.add(value);
				Collections.sort(list);
				tags.put(tag, list);
			}
		}
		else
		{
			if(tags.containsKey(tag))
			{
				ArrayList<String> temp = tags.get(tag);
				if(temp.contains(value))
				{
					throw new IllegalArgumentException("Tag already given to photo.");
				}
				else
				{
					temp.add(value);
					Collections.sort(temp);
					tags.put(tag, temp);
				}
			}
			else
			{
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(value);
				tags.put(tag, temp);
			}
		}
	}

	/**
	 * Remove a tag with specified name from photo
	 * 
	 * @param tag	type of tag to remove
	 * @param value	corresponding tag value to be removed
	 * @throws IllegalArgumentException	throws exception if tag is not present in photo
	 */
	public void deleteTag(String tag, String value){
		if(tags.containsKey(tag))
		{
			ArrayList<String> temp = tags.get(tag);
			if(!temp.remove(value))
			{
				throw new IllegalArgumentException("Tag not present in photo.");
			}
			if(temp.size() == 0)
			{
				tags.remove(tag);
			}
			else
			{
				Collections.sort(temp);
				tags.put(tag, temp);
			}
		}
		else
		{
			throw new IllegalArgumentException("Tag not present in photo.");
		}
		
	}
	
	/**
	 * Return list of tag for a photo
	 * 
	 * @return tags - HashMap<String, String> which is the list of all tags
	 */
	public HashMap<String, ArrayList<String>> getTags(){
		return tags;
	}
	
	/**
	 * Compares the two photo objects and returns true if they have the same 
	 * file names. If not, the method returns false. 
	 */
	public boolean equals(Object o){
		if(o == null)
		{
			return false;
		}
		if(!(o instanceof Photo))
		{
			return false;
		}
		
		Photo p = (Photo)o;
		return (this.getFileName()).equals(p.getFileName());
	}
	
	/**
	 * Returns string of important information of the photo
	 * 
	 * @return s	string with photo data
	 */
	public String toString(){
		String s = getFileName() + " - " + new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH).format(getDate());
		return s;
	}

	/**
	 * Returns last modified date of the photo file
	 * 
	 * @return	last modified date of the photo
	 */
	public long getDate(){
		return file.lastModified();
	}
	
	/**
	 * Returns a boolean that checks if the photo was tagged with the specified values
	 * 
	 * @param name	tag type
	 * @param value	tag value
	 * @return true if photo was tagged with specific type/value, false if it was not
	 */
	public boolean taggedWith(String name, String value){
		if(name == null){
			Set<String> keys = tags.keySet();
			for(String k : keys){
				for(String s : tags.get(k)){
					if(s.equals(value)){
						return true;
					}
				}
			}	
		}else{
			if(tags.get(name) == null)
				return false;
			for(String s : tags.get(name)){
				if(s.equals(value)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns 1, -1, or 0 depending on whether the photo has an earlier, later, 
	 * or same modification time as the parameter 
	 */
	public int compareTo(Photo o) {
		return (int) (this.getDate() - o.getDate());
	}
	
	/**
	 * Adds album to photo indicating photo is present in that album
	 * 
	 * @param a album photo is present in
	 */
	public void addAlbum(Album a)
	{
		albums.add(a);
	}
	
	/**
	 * Removes album from the list of albums the photo is in
	 * 
	 * @param a	album photo was removed from
	 */
	public void removeAlbum(Album a)
	{
		albums.remove(a);
	}
	
	/**
	 * Returns a list of all the albums the photo was present in 
	 * 
	 * @return	list of all albums photo is present in 
	 */
	public ArrayList<Album> getAlbums()
	{
		return albums;
	}
}

