package cs213.photoalbum.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Model for saving and restoring the users and their libraries to the hard drive
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public interface Model{

	ArrayList<User> users = new ArrayList<User>();
	
	/**
	 * Reads in the user object with the specified ID
	 * 
	 * @param userID	ID of the user that the program is requesting
	 * @throws ClassNotFoundException thrown if the user object/class is not found
	 * @throws IOException thrown if the file does not exist
	 */
	public User load(String userID) throws IOException, ClassNotFoundException;

	/**
	 * Reads in all saved users into the session. Users saved in ArrayList<User> users
	 * 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public void loadUsers() throws FileNotFoundException, IOException, ClassNotFoundException;
	
	/**
	 * Writes a user's data into the file system
	 * 
	 * @param user	User object to be written to file system
	 * @throws IOException	If file to be written to is not present, exception will be thrown
	 */
	public void save(User user) throws IOException;
	
	/**
	 * Writes all users' data into the file system
	 * 
	 * @throws IOException
	 */
	public void saveUsers() throws IOException;
	
	/**
	 * Deletes the user with the specified id from the program
	 * 
	 * @param id id of user to be deleted
	 * @throws IOException 
	 */
	public void delete(String id) throws IOException;
	
	/**
	 * Returns the users currently part of the program
	 * 
	 * @return	all the users of the program
	 */
	public ArrayList<User> getUsers();
	
	/**
	 * Adds the specified user to the program
	 * 
	 * @param user	user object to be added
	 */
	public void addUser(User user);

}
