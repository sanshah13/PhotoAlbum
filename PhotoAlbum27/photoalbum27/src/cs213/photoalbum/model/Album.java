package cs213.photoalbum.model;

import java.io.Serializable; 
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Reference to a users photo album
 * 
 * Stores references to photos in the album
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public class Album implements Serializable{
	
	private static final long serialVersionUID = 4752029772985057368L;
	private String name; //name of the album
	private ArrayList<Photo> photos; //photos in the album
	
	/**
	 * Album Constructor
	 * Creates an album object, gives it the specified name,
	 * and sets up the photo collection
	 * 
	 * @param name	name of the album
	 */
	public Album(String name){
		this.name = name;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Adds the photo object to the album. Throws Exception if photo
	 * is already present in the album.
	 * 
	 * @param p	photo object to be added
	 * @throws IllegalArgumentException	throws exception if photo is already present in album
	 */
	public void addPhoto(Photo p){
		if(!photos.contains(p)){
			photos.add(p);
			p.addAlbum(this);
		}
		else
			throw new IllegalArgumentException("Photo Already Present in Album.");
	}
	
	/**
	 * Removes the specified photo object from the album. Throws Exception
	 * if photo is not present in the album.
	 * 
	 * @param p	photo object to be removed 
	 * @throws IllegalArgumentException throws exception if photo is not present in album
	 */
	public void removePhoto(Photo p){
		if(!photos.remove(p))
		{
			throw new IllegalArgumentException("Photo Not Present in Album.");
		}
		p.removeAlbum(this);
	}
	
	/**
	 * Removes the photo with the specified name from the album.
	 * 
	 * @param fileName	name of the photo to be removed
	 */
	public void removePhoto(String fileName){
		Photo temp = new Photo(fileName, null);
		if(photos.contains(temp))
		{
			removePhoto(temp);
		}
	}
	
	/**
	 * Changes the caption of the photo to the provided caption. Throws an exception
	 * if the photo is not present in the album.
	 * 
	 * @param p	photo object to be recaptioned
	 * @param caption	new caption photo is to be given
	 * @throws IllegalArgumentException	throws exception if photo is not present in album
	 */
	public void recaptionPhoto(Photo p, String caption){
		if(photos.contains(p))
		{
			int index = 0;
			for(Photo temp: photos)
			{
				if(temp.equals(p))
				{
					break;
				}
				index++;
			}
			
			photos.get(index).setCaption(caption);
		}
		else
		{
			throw new IllegalArgumentException("Photo Not Present in Album.");
		}
	}
	
	/**
	 * Changes the caption of the photo to the provided caption
	 * 
	 * @param fileName name of the photo object that is to be recaptioned
	 * @param caption	new caption the photo is to be given
	 */
	public void recaptionPhoto(String fileName, String caption){
		Photo temp = new Photo(fileName, null);
		recaptionPhoto(temp,caption);
	}
	
	/**
	 * Returns the name of the album
	 * 
	 * @return	name of the album
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the album to the provided name
	 * 
	 * @param name the new name to be given to the album
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns a list of all the photos in the album
	 * 
	 * @return	array list of photos in album
	 */
	public ArrayList<Photo> getPhotos()
	{
		return photos;
	}
	
	/**
	 * Checks the album to see if it contains the specified photo.
	 * If it does, it returns the photo object. Otherwise, it returns null.
	 * 
	 * @param fileName	name of the photo object to be searched for
	 * @return	photo object if present in album, otherwise, null
	 */
	public Photo hasPhoto(String fileName)
	{
		Photo photo = null;
		for(Photo p: getPhotos())
		{
			if(p.getFileName().equals(fileName))
			{
				photo = p;
			}
		}
		
		return photo;
	}
	
	/**
	 * Compares the two album objects. If the names match, then the 
	 * albums are equal and true is returned. Otherwise, false if returned.
	 */
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		if(!(o instanceof Album))
		{
			return false;
		}
		
		Album a = (Album)o;
		
		return (this.name).equals(a.getName());
	}
	
	/**
	 * Returns a string with specified credentials of the album
	 * @return s contains important data to be printed of the album
	 */
	public String toString()
	{
		Collections.sort(photos);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
		//TODO start and end times!
		String s = getName() + " number of photos: " + getPhotos().size();
		if(photos.size() > 0)
			s += ", " + sdf.format(photos.get(0).getDate()) + " - " + sdf.format(photos.get(photos.size() - 1).getDate());
		return s;
	}
}
