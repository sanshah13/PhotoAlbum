package cs213.photoalbum.model;

import java.io.Serializable; 
import java.util.HashMap;

/**
 * Reference to a user of the program
 * 
 * Stores references to albums owned my a user
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 3305354711672638867L;
	private String id; // username of the user
	private String name; // full name of the user
	private HashMap<String, Album> library; // user's collection of albums. Key = name of album, Value = album
	
	/**
	 * User Constructor
	 * Creates a User, and sets up their library
	 *
	 * @param id	the unique username of the user. Used when the user logs in
	 * @param name	the Full name of the user
	 * 			
	 */
	public User(String id, String name){
		this.id = id;
		this.name = name;
		library = new HashMap<String, Album>();
	}

	/**
	 * Adds the album object to the user's library. Throws an exception if
	 * album is already present in the user's library.
	 *
	 * @param a	album object to be added to the user's library
	 * @throws IllegalArgumentException	throws exception if album was present in user's library
	 */
	public void addAlbum(Album a){
		if(!library.containsKey(a.getName())){
			library.put(a.getName(), a);
		}
		else
			throw new IllegalArgumentException("Album Already Present in Library.");
	}
	
	/**
	 * Creates an Album object and adds it to the user's library.
	 *
	 * @param name name of the new album to be added to the library
	 */
	public void addAlbum(String name){
		Album temp = new Album(name);
		addAlbum(temp);
	}
	
	/**
	 * Removes an album with a specified name from the user's library. If the album
	 * does not exist in the library nothing is removed and an exception is thrown.
	 *
	 * @param name	the name of the album to be removed. 
	 * @throws IllegalArgumentException throws exception if album is not present in user's library
	 */
	public void removeAlbum(String name){
		Album temp = library.remove(name);
		if(temp == null)
		{
			throw new IllegalArgumentException("Album Not Present in Library.");
		}
	}
	
	/**
	 * Removes the specified album from the user's library
	 * 
	 * @param a	album object that is to be removed from the user's library
	 */
	public void removeAlbum(Album a){
		removeAlbum(a.getName());
	}
	
	/**
	 * Renames the specified album to the new name provided. If the user 
	 * already has an album with the new name then nothing is changed
	 * and an exception is thrown. If the album with the old name is not present in the library, then an
	 * exception is thrown.
	 * 
	 * @param oldName	the current name of the album
	 * @param newName	the new name that the album will be given
	 * @throws IllegalArgumentException	  throws exception if old album is not present or new name is being used
	 */
	public void renameAlbum(String oldName, String newName){
		if(!library.containsKey(newName))
		{
			Album temp = library.remove(oldName);
			if(temp != null)
			{
				temp.setName(newName);
				library.put(newName, temp);
			}
			else
			{
				throw new IllegalArgumentException("Album Not Present in Library.");
			}
		}
		else
		{
			throw new IllegalArgumentException("New Album Name Already Present in Library.");
		}
	}

	/**
	 * Renames the specified album to the new name provided. If the user
	 * already has an album with the new name then nothing is changed.
	 * 
	 * @param a	the album object who's name is to be changed
	 * @param newName	the new name that the album object will be given
	 */
	public void renameAlbum(Album a, String newName){
		renameAlbum(a.getName(), newName);
	}
	
	/**
	 * Returns the user's id
	 * 
	 * @return the calling user's id
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Returns the user's name
	 * 
	 * @return the calling user's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the list of albums that the user contains
	 * 
	 * @return	the hashmap that contains all the albums of the user
	 */
	public HashMap<String, Album> getAlbums()
	{
		return library;
	}
	
	/**
	 * Returns the album with the specified name
	 * 
	 * @param name	name of the album 
	 * @return	album with the specified name
	 */
	public Album getAlbum(String name) {
		return library.get(name);
	}
	
	/**
	 * Returns true if the parameter object has the same user id as the calling object
	 * 
	 * @param o	object to be compared
	 */
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof User))
		{
			return false;
		}
		
		User user = (User)o;
		
		return this.getId().equals(user.getId());
	}
	
	/**
	 * Returns string of the user's id
	 * 
	 * @return	user's id
	 */
	public String toString()
	{
		return getId();
	}
}
