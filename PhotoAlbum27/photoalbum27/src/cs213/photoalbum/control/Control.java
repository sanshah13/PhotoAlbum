package cs213.photoalbum.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Model;
import cs213.photoalbum.model.Photo;
import cs213.photoalbum.model.User;

/**
 * Controller for the User and album libraries. Handles all updates to the libraries
 * 
 * Updates and modifies users, albums, photos
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public class Control {
	
	Model model;
	User currentUser;
	
	/**
	 *Constructor 
	 *Instantiates Control's variables 
	 *
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public Control() throws FileNotFoundException, ClassNotFoundException, IOException {
		model = new SessionBackend();
		model.loadUsers();
	}
	
	/**
	 * Adds the user to the model. If the username is already present 
	 * in the model, throws an error.
	 * 
	 * @param id	if of the user to be added
	 * @param userName	name of the user to be added
	 */
	public void addUser(String id, String userName) {
		User user = new User(id, userName);
		model.addUser(user);
	}
	
	/**
	 * Calls the delete method in the model class to delete
	 * the user with the specified id.
	 * 
	 * @param id	id of the user to be deleted
	 * @throws IOException 
	 */
	public void deleteUser(String id) throws IOException {
		model.delete(id);
	}
	
	/**
	 * Sets the current user to the user with the specified id
	 *  
	 * @param id	id of the current user
	 * @throws IllegalArgumentException	throws error if requested user is not present in program
	 */
	public void login(String id) {
		ArrayList<User> users = model.getUsers();
		boolean found = false;
		int counter = 0, index = -1;
		
		for(User u: users){
			if(u.getId().equals(id)){
				found = true;
				index = counter;
			}
			counter++;
		}
		
		if(found)
		{
			currentUser = users.get(index);
		}
		else
		{
			throw new IllegalArgumentException("User ID does not exist.");
		}
	}
	
	/**
	 * Creates a new album with the specified name
	 * 
	 * @param name	name of the new album
	 */
	public void createAlbum(String name) {
		currentUser.addAlbum(name);
	}
	
	/**
	 * Deletes the album with the specified name
	 * 
	 * @param name	name of the album to be deleted
	 */
	public void deleteAlbum(String name) {
		currentUser.removeAlbum(name);
	}
	
	/**
	 * Returns a hashmap of all the albums of the current user
	 *   
	 * @return hash map of all the albums the user has
	 */
	public HashMap<String, Album> getAlbums() {
		return currentUser.getAlbums();
	}
	
	/**
	 * Renames  the old album with the provided new name
	 * 
	 * @param oldAlbum name of the old album
	 * @param newAlbum new name of the album
	 */
	public void renameAlbum(String oldAlbum, String newAlbum)
	{
		currentUser.renameAlbum(oldAlbum, newAlbum);
	}
	
	/**
	 * Returns an array list of all the photos in the specified album
	 * 
	 * @param albumName	name of the album which we wish to retrieve photos from
	 * @return array list of photos in the album
	 */
	public ArrayList<Photo> listPhotos(String albumName) {
		return currentUser.getAlbum(albumName).getPhotos();
	}
	
	/**
	 * Adds the photo with the specified caption to the specified album
	 * 
	 * @param fileName	file name of the photo
	 * @param caption	caption that corresponds with photo
	 * @param albumName	album file is to be added to
	 * @return caption of the photo just added to the album
	 */
	public String addPhoto(String fileName, String caption, String albumName) {
		Photo photo = null;
		for(Album a: currentUser.getAlbums().values())
		{
			for(Photo p: a.getPhotos())
			{
				if(p.getFileName().equals(fileName))
				{
					photo = p;
					break;
				}
			}
		}
		if(photo == null)
		{
			photo = new Photo(fileName, caption);
		}
		else
		{
			if(caption != null)
			{
				photo.setCaption(caption);
			}
		}
		currentUser.getAlbum(albumName).addPhoto(photo);
		return photo.getCaption();
	}
	
	/**
	 * Moves the photo from the old album to the new album
	 * 	 
	 * @param fileName	name of the photo file
	 * @param oldAlbum	name of the old album
	 * @param newAlbum	name of the new album
	 * @throws IllegalArgumentException	throws exception if albums are not found in user 
	 * 									or photo is not found in old album 
	 */
	public void movePhoto(String fileName, String oldAlbum, String newAlbum) {
		Album o = currentUser.getAlbum(oldAlbum);
		Album n = currentUser.getAlbum(newAlbum);
		if(o == null)
		{
			throw new IllegalArgumentException("Old Album not present in User library");
		}
		if(n == null)
		{
			throw new IllegalArgumentException("New Album not present in User library");
		}
		
		Photo photo = o.hasPhoto(fileName);
		
		if(photo != null)
		{
			if(n.hasPhoto(fileName) == null)
			{	
				o.removePhoto(photo);
				n.addPhoto(photo);
			}
			else
			{
				throw new IllegalArgumentException("Photo already present in new album");
			}
		}
		else
		{
			throw new IllegalArgumentException("Photo not found");
		}
	}
	
	/**
	 * Removes the photo from the specified album
	 * 
	 * @param fileName	name of the photo file
	 * @param albumName	name of the album from which the photo is to be removed
	 * @throws IllegalArgumentException	throws error if album or photo is not found 
	 */
	public void removePhoto(String fileName, String albumName) {
		Album album = currentUser.getAlbum(albumName);
		if(album == null)
		{
			throw new IllegalArgumentException("Album not present in User");
		}
		
		Photo photo = album.hasPhoto(fileName);
		
		if(photo != null)
		{
			album.removePhoto(photo);
		}
		else
		{
			throw new IllegalArgumentException("Photo not found");
		}
	}
	
	/**
	 * Adds the new tag to the specified photo file
	 * 
	 * @param fileName	name of the photo file
	 * @param tag	tag type
	 * @param value	tag value
	 * @throws IllegalArgumentException throws exception if the photo does not exist
	 */
	public void addTag(String fileName, String tag, String value) {
		boolean added = false;
		for(Album a: currentUser.getAlbums().values())
		{
			for(Photo p: a.getPhotos())
			{
				if(p.getFileName().equals(fileName))
				{
					p.addTag(tag, value);
					added = true;
				}
			}
		}
		if(!added)
		{
			throw new IllegalArgumentException("Photo does not exist");
		}
	}
	
	/**
	 * Deletes the specified tag from the photo
	 * 
	 * @param fileName name of the photo file
	 * @param tag	type of the tag to be deleted
	 * @param value	value of the tag
	 * @throws IllegalArgumentException throws exception if photo does not exist
	 */
	public void deleteTag(String fileName, String tag, String value) {
		boolean deleted = false;
		for(Album a: currentUser.getAlbums().values())
		{
			for(Photo p: a.getPhotos())
			{
				if(p.getFileName().equals(fileName))
				{
					p.deleteTag(tag, value);
					deleted = true;
				}
			}
		}
		if(!deleted){
			throw new IllegalArgumentException("Photo does not exist");
		}
	}
	
	/**
	 * Returns the first instance of the photo or null if photo is not found
	 * 
	 * @param fileName	name of photo file
	 * @return	photo object or null if photo is not found
	 */
	public Photo getPhoto(String fileName){
		for(Album a: currentUser.getAlbums().values())
		{
			for(Photo p: a.getPhotos())
			{
				if(p.getFileName().equals(fileName))
				{
					return p;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of photos that are in the range of the specified date/times
	 * 
	 * @param start	beginning time from which photos should be sifted
	 * @param end	ending time of period
	 * @return list	list of photos that fall into specified time period
	 * @throws ParseException 
	 */
	public ArrayList<Photo> getPhotosByDate(String start, String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
		
		ArrayList<Photo> inRange = new ArrayList<Photo>();
		Date startDate = sdf.parse(start);
		Date endDate = sdf.parse(end);
		
		for(Album a : currentUser.getAlbums().values()){
			for(Photo p : a.getPhotos()){
				if(p.getDate() > startDate.getTime() && p.getDate() < endDate.getTime() && !inRange.contains(p)){
					inRange.add(p);
				}
			}
		}
		Collections.sort(inRange);
		return inRange;
	}
	
	/**
	 * Returns a list of photos that contain the specified tag(s)
	 * 
	 * @param list	hashmap of tags 
	 * @return	list of photos with the specified tags
	 */
	public ArrayList<Photo> getPhotosByTag(ArrayList<String> list) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		for(Album a : currentUser.getAlbums().values()){
			for(Photo p : a.getPhotos()){
				boolean tagged = true;
				for(String s : list){
					String name = null;
					String tag = s;
					if(s.contains(":")){
						name = s.substring(0, s.indexOf(":"));
						tag = s.substring(s.indexOf(":") + 1);
					}
					if(!p.taggedWith(name, tag)){
						tagged = false;
						break;
					}
				}
				if(tagged)
					photos.add(p);
			}
		}
		Collections.sort(photos);
		return photos;
		
	}
	
	/**
	 * Saves data for the current user
	 * @throws IOException 
	 */
	public void logout() throws IOException {
		model.save(currentUser);
		currentUser = null;
	}
	
	/**
	 * Returns a list of all users present in the program
	 * 
	 * @return all users present in the program 
	 */
	public ArrayList<User> getUsers() {		
		return model.getUsers();
	}
	
	/**
	 * Saves all users' information to the file
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		model.saveUsers();
	}

	/**
	 * Returns the current user being manipulated
	 * 
	 * @return currentUser	user currently logged into the program
	 */
	public User getUser() {
		return currentUser;
	}
	
	
}
