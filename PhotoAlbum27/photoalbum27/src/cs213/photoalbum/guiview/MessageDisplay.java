package cs213.photoalbum.guiview;

/**
 * Prompts frame to send and receive messages/errors
 * @author Rich Gerdes
 *
 */
public interface MessageDisplay {
	
	/**
	 * Prints a message to the frame to indicate a successful update
	 * to the frame/object in question
	 * 
	 * @param msg message to be displayed
	 */
	public void sendMessage(String msg);
	
	/**
	 * Prints an error message to the frame to indicate an unsuccessful
	 * update to the frame/object in question
	 * 
	 * @param err error message to be displayed
	 */
	public void sendError(String err);

}
