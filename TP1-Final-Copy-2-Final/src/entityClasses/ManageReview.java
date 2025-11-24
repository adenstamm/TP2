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
 * @author Xandra Cornelius
 * 
 * @version 1.00		2025-10-13 Initial version
 *  
 */


public class ManageReview {
	
	
	/*******
	 * <p> Title: ManageReview Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 */
	
	public ManageReview() {
		// No initialization required at this time
	}
	
	/*****
     * <p> Method: void storeReview(Post mainPost, User user, String replyText) </p>
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
	
	public static void storeReview(Post mainPost, User user, String reviewText) {
		
		if(user.getUserName().isEmpty()) {
			System.out.println("Reply is missing a Username.");
			return;
		}
		if (reviewText.isEmpty()) {
			System.out.println("Reply is missing the reply text.");
			return;
		}
		if(mainPost.getPostID() == 0) {
			System.out.println("Reply is not pointing to a valid postID.");
			return;
		}
		int postID = 0;
		String staffUsername = user.getUserName();
		String studentUsername = mainPost.getUserName();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
		LocalDateTime Time = LocalDateTime.now().plusDays(0).plusHours(0).plusMinutes(0);
		String reviewTime = Time.format(formatter);
		
		if(mainPost.getPostID() == 50) {
		postID = 2;
		}else {
		postID = mainPost.getPostID();
		}
		
		Review review = new Review(postID, staffUsername, studentUsername, reviewText, reviewTime);
		System.out.println(reviewText);
		try {
		applicationMain.FoundationsMain.database.registerReview(review);
		} catch (SQLException e) {
			System.out.println("Failed to register review.");
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
	
//	public static void deleteReview(Review review) {
//		try {
//		applicationMain.FoundationsMain.database.deleteReview(review);
//		} catch (SQLException e) {
//			System.out.println("Failed to delete Review");
//		}
//	}
	
}