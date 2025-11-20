package entityClasses;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*******
 * <p> Title: ManageReply Class</p>
 * 
 * <p> Description: The ManageReply Page View. This class will be used to store new replies as well
 * 	   as updating some attributes that require the database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Ian Johnson
 * 
 * @version 1.00		2025-10-13 Initial version
 *  
 */


public class ManageReply {
	
	
	/*******
	 * <p> Title: ManageReply Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 */
	
	public ManageReply() {
		// No initialization required at this time
	}
	
	/*****
     * <p> Method: void storeReply(Post mainPost, User user, String replyText) </p>
     * 
     * <p> Description: This method prepares and stores a reply into the database. </p>
     * 
     * @param mainPost the post that the reply is tied to.
     * 
     * @param user the user that wrote the reply
     * 
     * @param replyText the text that will be stored in the reply
     * 
     */
	
	public static void storeReply(Post mainPost, User user, String replyText, boolean feedback) {
		
		if(user.getUserName().isEmpty()) {
			System.out.println("Reply is missing a Username.");
			return;
		}
		if (replyText.isEmpty()) {
			System.out.println("Reply is missing the reply text.");
			return;
		}
		if(mainPost.getPostID() == 0) {
			System.out.println("Reply is not pointing to a valid postID.");
			return;
		}
		int postID = 0;
		String Username = user.getUserName();
		boolean adminRole = user.getAdminRole();
		boolean studentRole = user.getStudentRole();
		boolean staffRole = user.getStaffRole();
		int likes = 0;
		int views = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
		LocalDateTime Time = LocalDateTime.now().plusDays(1).plusHours(0).plusMinutes(0);
		String replyTime = Time.format(formatter);
		
		if(mainPost.getPostID() == 50) {
		postID = 2;
		}else {
		postID = mainPost.getPostID();
		}
		
		Reply reply = new Reply(postID, Username, replyText, adminRole, studentRole, staffRole, likes, views, replyTime, feedback);
		System.out.println(replyText);
		try {
		applicationMain.FoundationsMain.database.register(reply);
		} catch (SQLException e) {
			System.out.println("Failed to register post.");
			System.exit(0);
		}
	}
	
	/*****
     * <p> Method: void deleteReply(Reply reply) </p>
     * 
     * <p> Description: This method deletes a reply from the database. </p>
     * 
     * @param reply the reply that is going to be deleted
     * 
     * 
     */
	
	public static void deleteReply(Reply reply) {
		try {
		applicationMain.FoundationsMain.database.deleteReply(reply);
		} catch (SQLException e) {
			System.out.println("Failed to delete Post");
		}
	}
	
}