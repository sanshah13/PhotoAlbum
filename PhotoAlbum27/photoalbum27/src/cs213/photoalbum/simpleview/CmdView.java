package cs213.photoalbum.simpleview;


import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cs213.photoalbum.control.Control;
import cs213.photoalbum.model.Album;
import cs213.photoalbum.model.Photo;
import cs213.photoalbum.model.User;

/**
 * A command line interface for the user to interface with the program
 * 
 * @author Rich Gerdes and Sangini Shah
 */
public class CmdView{

	static Control control; 
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
	
	/**
	 * Driver for command line interface of photo album project
	 * 
	 * @param args arguments from command line
	 */
	public static void main(String[] args){
		try {
			control = new Control();
			
			if(args.length < 1){
				System.out.println("Invalid run of the program. Exiting...");
			}else if(args[0].equals("listusers")){
					listUsers();
			}else if(args[0].equals("adduser") && args.length >= 3){
				createUser(args[1], args[2]);
			}else if(args[0].equals("deleteuser") && args.length >= 2){
				deleteUser(args[1]);
			}else if(args[0].equals("login") && args.length >= 2){
				login(args[1]);
			}else{
				System.out.println("Invalid run of the program. Check your arguments. Exiting...");
			}
			
			try {
				control.save();
			} catch (IOException e) {
			}
		} catch (FileNotFoundException e1) {
		} catch (ClassNotFoundException e1) {
		} catch (IOException e1) {
		}
	}
	

	/**
	 * Have control create a user with a given username. 
	 * 
	 * @param username	username to be assigned to the new user
	 */
	public static void createUser(String id, String username){
		try{
			control.addUser(id, username);
			System.out.println("created user " + id + " with name " + username);
		}
		catch(Exception e)
		{
			System.out.println("user " + id + " already exists with name " + e.getMessage());
		}
	}
	
	/**
	 * Have control delete a given user and all data associated
	 * 
	 * @param username
	 */
	public static void deleteUser(String username){
		try{
			control.deleteUser(username);
			System.out.println("deleted user " + username);
		}
		catch(Exception e){
			System.out.println("user " + username + " does not exist");
		}
	}
	
	/**
	 * List all users
	 */
	public static void listUsers(){
		ArrayList<User> users = control.getUsers();
		if(users.size() == 0){
			System.out.println("no users exist");
		}
		else{
			for(User u : users){
				System.out.println(u.toString());
			}
		}
	}
	
	/**
	 * Interactive Mode
	 * Set a user as active for the current session
	 * 
	 * @param username username of the user logging in
	 */
	public static void login(String username){
		try{
			control.login(username);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			while(true){
			
				try {
					line = br.readLine();
					line += " ";
					String[] values = line.split("([ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$))");
					if(values.length == 0)
						continue;
					if(values.length >= 2 && values[0].equals("createAlbum")){
						createAlbum(values[1].replace("\"", ""));
					}else if(values.length >= 2 && values[0].equals("deleteAlbum")){
						deleteAlbum(values[1].replace("\"", ""));
					}else if(values.length >= 1 && values[0].equals("listAlbums")){
						listAlbums();
					}else if(values.length >= 2 && values[0].equals("listPhotos")){
						listPhotos(values[1].replace("\"", ""));
					}else if(values.length >= 4 && values[0].equals("addPhoto")){
						addPhotoToAlbum(values[1].replace("\"", ""), values[2].replace("\"", ""), values[3].replace("\"", ""));
					}else if(values.length >= 4 && values[0].equals("movePhoto")){
						movePhoto(values[1].replace("\"", ""), values[2].replace("\"", ""), values[3].replace("\"", ""));
					}else if(values.length >= 3 && values[0].equals("removePhoto")){
						removePhotoFromAlbum(values[1].replace("\"", ""), values[2].replace("\"", ""));
					}else if(values.length >= 3 && values[0].equals("addTag")){
						addTag(values[1].replace("\"", ""), values[2]);
					}else if(values.length >= 3 && values[0].equals("deleteTag")){
						removeTag(values[1].replace("\"", ""), values[2]);
					}else if(values.length >= 2 && values[0].equals("listPhotoInfo")){
						listPhotoInfo(values[1].replace("\"", ""));
					}else if(values.length >= 3 && values[0].equals("getPhotosByDate")){
						getPhotosBetweenDates(values[1], values[2]);
					}else if(values.length >= 2 && values[0].equals("getPhotosByTag")){
						getPhotosWithTags(line.substring(values[0].length() + 1));
					}else if(values[0].equals("logout")){
						control.logout();
						break;
					}else{
						System.out.println("Error: Invalid input");
					}
					System.out.println();
				} catch (IOException e) {
				}
				
				try {
					control.save();
				} catch (IOException e) {
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("user " + username + " does not exist");
		}
	}
	
	/**
	 * disconnect user and exit session
	 * @throws IOException 
	 */
	public static void logout() throws IOException{
		control.logout();
	}
	
	/**
	 * Create an album with given name
	 * 
	 * @param album name of new album
	 */
	public static void createAlbum(String album){
		try{
			control.createAlbum(album);
			System.out.println("created album for user " + control.getUser().getId() + ":\n" + album);
		}
		catch(Exception e)
		{
			System.out.println("album exists for user " + control.getUser().getId() + ":\n" + album);
		}
	}
	
	/**
	 * Delete an album with a given name
	 * 
	 * @param album name of album to be deleted
	 */
	public static void deleteAlbum(String album){
		try{
			control.deleteAlbum(album);
			System.out.println("deleted album from user " + control.getUser().getId() + ":\n" + album);
		}
		catch(Exception e){
			System.out.println("album does not exist for user " + control.getUser().getId() + ":\n" + album);
		}
	}
	
	/**
	 * List all albums for active user{\n\n}
	 */
	public static void listAlbums(){
		Collection<Album> c = control.getAlbums().values();
		if(c.size() != 0){
			for(Album a : control.getAlbums().values()){
				System.out.println(a.toString());
			}
		}
		else
		{
			System.out.println("No albums exist for user " + control.getUser().getId());
		}
	}
	
	/**
	 * List photos in a given album
	 * 
	 * @param album
	 */
	public static void listPhotos(String album){
		Album a = control.getUser().getAlbum(album);
		if(a == null){
			System.out.println("There is no album with the name " + album);
			return;
		}
		ArrayList<Photo> photos = a.getPhotos(); 
		if(photos.size() != 0){
			System.out.println("Photos for album " + album + ":");
			for(Photo p : photos){
				System.out.println(p.toString());
			}
		}
		else{
			System.out.println("No photos in album");
		}
	}
	
	/**
	 * Make a new photo and add it to a given album
	 * 
	 * @param photo name of new photo
	 * @param caption caption for photo
	 * @param album name of album
	 */
	public static void addPhotoToAlbum(String photo, String caption, String album){
		try{
			String s = control.addPhoto(photo, caption, album);
			System.out.println("Added photo " + photo + ":\n" + s + " - Album: " + album);
		}
		catch(IllegalArgumentException e)
		{
			if(e.getMessage().equals("File does not exist.")){
				System.out.println("File " + photo + " does not exist");
			}
			else if(e.getMessage().equals("Photo Already Present in Album.")){
				System.out.println("Photo " + photo + " already exists in album " + album);
			}
			else{
				System.out.println("Album " + album + " does not exist");
			}
		}
	}
	
	/**
	 * Remove a photo from a given album
	 * 
	 * @param photo name of photo
	 * @param album name of album
	 */
	public static void removePhotoFromAlbum(String photo, String album){
		try{
			control.removePhoto(photo, album);
			System.out.println("Removed photo: \n" + photo + " - From album " + album);
		}
		catch(Exception e){
			System.out.println("Photo " + photo + " is not in album " + album);
		}
	}
	
	/**
	 * move a photo with a given name from one album to another
	 * 
	 * @param photo name of photo to be moved
	 * @param oldAlbum name of current album
	 * @param newAlbum name of target album
	 */
	public static void movePhoto(String photo, String oldAlbum, String newAlbum){
		try{
			control.movePhoto(photo, oldAlbum, newAlbum);
			System.out.println("Moved photo " + photo +":\n" + photo + " - From album " + oldAlbum + " to album " + newAlbum);
		}
		catch(Exception e){
			if(e.getMessage().equals("Photo not found")){
				System.out.println("Photo " + photo + " does not exist in " + oldAlbum);
			}
			else if(e.getMessage().equals("Old Album not present in User library")){
				System.out.println("Album " + oldAlbum + " does not exist");
			}
			else if(e.getMessage().equals("New Album not present in User library")){
				System.out.println("Album " + newAlbum + " does not exist");
			}
			else if(e.getMessage().equals("Photo already present in new album")){
				System.out.println("Photo " + photo + " already exists in " + newAlbum);
			}
		}
	}
	
	/**
	 * List info for a given photo
	 * 
	 * @param photo name of photo to print
	 */
	public static void listPhotoInfo(String photo){
		Photo p = control.getPhoto(photo);
		if(p == null){
			System.out.println("Photo " + photo + " does not exist");
		}
		else{
			System.out.println("Photo file name: " + photo);
			System.out.print("Album: ");
			for(Album a: p.getAlbums()){
				if(a.equals(p.getAlbums().get(p.getAlbums().size()-1)))
					System.out.print(a.getName());
				else
					System.out.print(a.getName() + ", ");
			}
			System.out.println();
			System.out.println("Date: " + sdf.format(p.getDate()));
			System.out.println("Caption: " + p.getCaption());
			System.out.println("Tags: ");
			HashMap<String, ArrayList<String>> map = p.getTags();
			try{
				if(map.get("location").size() != 0)
					System.out.println("location:" + map.get("location").get(0));
			}
			catch(Exception e){}
			try{
				ArrayList<String> people = map.get("person");
				if(people != null)
				{	
					for(String s: people){
						System.out.println("person:" + s);
					}
				}
			}
			catch(Exception e){}
			
			for(String key: map.keySet())
			{
				if(!key.contains("location") && !key.contains("person")){
					for(String value: map.get(key)){
						System.out.println(key + ":" + value);
					}
				}
			}
		}
	}
	
	/**
	 * Add a new tag to a photo
	 * 
	 * @param photo name of photo
	 * @param tag tag info
	 */
	public static void addTag(String photo, String tag){
		String[] data = tag.replace("\"", "").split(":");
		try{
			control.getPhoto(photo).addTag(data[0], data[1]);
			System.out.println("Added tag:\n" + photo + " " + data[0] + ":\"" + data[1] + "\"");
		}
		catch(Exception e){
			try{
				if(e.getMessage().equals("Already has a location.")){
					System.out.println("Photo already contains location tag. Delete location and then add tag.");
				}
				else if(e.getMessage().equals("Photo does not exist")){
					System.out.println("Photo " + photo + " does not exist. Check Spelling");
				}
				else{
					System.out.println("Tag already exists for " + photo + " " + data[0] + ":\"" + data[1] + "\"");
				}
			}
			catch(Exception e1){
				System.out.println("Photo " + photo + " does not exist. Check spelling of file name");
			}
		}
	}
	
	/**
	 * Remove a tag from given photo
	 * 
	 * @param photo name of the photo
	 * @param tag name of tag to be removed
	 */
	public static void removeTag(String photo, String tag){
		String[] data = tag.replace("\"", "").split(":");
		try{
			control.getPhoto(photo).deleteTag(data[0], data[1]);
			System.out.println("Deleted tag:\n" + photo + " " + data[0] + ":" + data[1]);
		}
		catch(Exception e){
			try{
				if(e.getMessage().equals("Tag not present in photo."))
					System.out.println("Tag does not exist for " + photo + " " + data[0] + ":" + data[1]);
				else if(e.getMessage().equals("Photo does not exist"))
					System.out.println("Photo " + photo + " does not exist. Check spelling of file name");
			}
			catch(Exception e1){
				System.out.println("Photo " + photo + " does not exist. Check spelling of file name");
			}
		}
	}
	
	/**
	 * Get photos taken between two dates.
	 * 
	 * Date format MM/dd/yyyy-HH:mm:ss
	 * 
	 * @param begin being date
	 * @param end end date
	 */
	public static void getPhotosBetweenDates(String begin, String end){
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ENGLISH);
			sdf.setLenient(false);
			Date startDate = sdf.parse(begin);
			Date endDate = sdf.parse(end);
			if(startDate.getTime() > endDate.getTime()){
				System.out.println("Error: Date order is incorrect. The second date comes before the first.");
				return;
			}
			ArrayList<Photo> photos = control.getPhotosByDate(begin, end);
			if(photos.size() == 0){
				System.out.println("No Photos in that date range");
			}
			else{
				System.out.println("Photos for user " + control.getUser() + " in range " + begin + " to " + end + ":");
				for(Photo p : photos){
					System.out.print(p.getCaption());
					System.out.print(" - Album: ");
					boolean first = true;
					for(Album a: p.getAlbums()){
						if(!first)
							System.out.print(", ");
						System.out.print(a.getName());
						first = false;
					}
					System.out.println(" - Date: " + sdf.format(p.getDate()));
				}
			}
		} catch (ParseException e) {
			System.out.println("Error: Invalid Date Format");
		}
	}
	
	/**
	 * Get all photos with a given set of tags. Tags are converted to a HashMap<String, String>
	 * 
	 * @param tags String of tags to be parsed
	 */
	public static void getPhotosWithTags(String tags){
		ArrayList<String> list = new ArrayList<String>();
		String[] data = tags.split("([ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$))");
		for(String t : data){
			list.add(t.trim().replace("\"", ""));
		}
		ArrayList<Photo> photos = control.getPhotosByTag(list);
		if(photos.size() == 0){
			System.out.println("No Photos with those tags");
		}
		else{
			System.out.println("Photos for user " + control.getUser() + " with tags " + tags.replace("\"", "") + ":");
			for(Photo p : photos){
				System.out.print(p.getCaption());
				System.out.print(" - Album: ");
				boolean first = true;
				for(Album a: p.getAlbums()){
					if(!first)
						System.out.print(", ");
					System.out.print(a.getName());
					first = false;
				}
				System.out.println(" - Date: " + sdf.format(p.getDate()));
			}
		}
	}

}
