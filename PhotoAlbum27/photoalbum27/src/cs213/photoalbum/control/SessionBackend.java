package cs213.photoalbum.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cs213.photoalbum.model.Model;
import cs213.photoalbum.model.User;

/**
 * Backend for saving userdata on program close
 * 
 * @author Rich Gerdes and Sangini Shah
 * 
 */
public class SessionBackend implements Model {

	public static final String storeDir = "data";

	/**
	 * Reads in the data of the user from the files
	 * 
	 * @return u	user currently loaded into system
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public User load(String userID) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				storeDir + File.separator + (userID + ".dat")));
		User u = (User) ois.readObject();
		ois.close();
		return u;
	}

	/**
	 * Reads in all saved users into the session. Users saved in ArrayList<User> users
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public void loadUsers() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		String path = "./" + storeDir;
		File folder = new File(path);
		if (!(folder.exists() && folder.isDirectory())) {
			folder.mkdir();
		}
		File[] files = folder.listFiles();
		ObjectInputStream ois;
		for (File f : files) {
			ois = new ObjectInputStream(new FileInputStream(f));
			Object o = ois.readObject();
			if (o instanceof User) {
				users.add((User) o);
			}
			ois.close();
		}
	}

	/**
	 * Writes a users data into the file system
	 * 
	 * @param user	User object to be written to file system
	 */
	public void save(User user) throws IOException {
		if (user == null)
			return;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				storeDir + File.separator + (user.getId() + ".dat")));
		oos.writeObject(user);
		oos.close();
	}

	/**
	 * Saves all users to the file
	 * 
	 * @throws IOException
	 */
	public void saveUsers() throws IOException {
		for (User u : users) {
			save(u);
		}
	}

	/**
	 * Deletes the specified user from the program
	 * 
	 * @param id id of user to be deleted
	 */
	public void delete(String id) throws IOException {
		User u = null;
		for (User user : users) {
			u = user;
			if (u.getId().equals(id)) {
				String path = storeDir + "/" + u.getId() + ".dat";

				File file = new File(path);
				if (!file.delete()) {
					throw new IOException();
				}
				break;
			}
		}
		if (u != null)
			users.remove(u);
	}

	/**
	 * Returns the users currently part of the program
	 * 
	 * @return all the users of the program
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * Adds the specified user to the list of users. Throws an error if the
	 * username is already present in the program.
	 * 
	 * @param user
	 *            user to be added to the program
	 * @throws IllegalArgumentException
	 *             Throws exception if username is already present in the
	 *             program
	 */
	public void addUser(User user) {
		if (!users.contains(user))
			users.add(user);
		else {
			User temp = null;
			for (User u : users) {
				if (u.equals(user)) {
					temp = u;
					break;
				}
			}
			throw new IllegalArgumentException(temp.getName());
		}
	}

}
