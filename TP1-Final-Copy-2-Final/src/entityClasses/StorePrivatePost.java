package entityClasses;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: StorePrivatePost Class</p>
 * 
 * <p> Description: The StorePrivatePost class is being used as a prototype code to help the implementation of the private feedback system described in the 
 * 					staff epics.</p>
 * 
 * <p> Copyright: Ian Johnson © 2025 </p>
 * 
 * @author Ian Johnson
 * 
 * @version 1.00		2025-11-12 Initial version
 *  
 */

public class StorePrivatePost {
	
	/**
	 * Default constructor for StorePrivatePost.
	 * Initializes the controller with default values and no special setup.
	 */
	public StorePrivatePost() {
		// No initialization required at this time
	}
	
/*****
     * <p> Method: void storePrivatePost(User mainUser, String postText, String thread) </p>
     * 
     * <p> Description: This method prepares and stores a private post into the database. This is one of the ways that staff can give feedback to specific students.
     * 	                A post that is stored by this method will only be seen by staff and the student who wrote it.</p>
     * 
     * @param mainUser the user that wrote the post.
     * 
     * @param postText the text that will be stored in the post
     * 
     * @param thread the specific thread that this post belongs to
     * 
     * @param tags the tags that are associated with the post--
     * 
     * @param softDelete determines whether the post is deleted or not
     * 
     */
	public static void storePrivatePost(User mainUser, String postText, String thread, String tags, boolean softDelete) {
		//These checks make sure that there is a User associated with the Post and also makes sure there is a Post Text along with it.
		if(mainUser.getUserName().isEmpty()) {
			System.out.println("Error: The post needs to have a username attached to it.");
			return;
		} else if(postText.isEmpty()) {
			System.out.println("Error: There is no text for the post.");
			return;
			
		} else {
			//This takes information about the user and stores it with the post
			String Username = mainUser.getUserName();
			boolean adminRole = mainUser.getAdminRole();
			boolean studentRole = mainUser.getStudentRole();
			boolean staffRole = mainUser.getStaffRole();
			String likes = "";
			String views = "";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
			LocalDateTime Time = LocalDateTime.now().plusDays(0).plusHours(0).plusMinutes(0);
			String postTime = Time.format(formatter);
			List<Post> posts = new ArrayList<>();
			//See how many posts are in the database and give it a proper postId
			posts = applicationMain.FoundationsMain.database.getAllPosts();
			int postID = posts.size() + 1;
			
			if(thread.isEmpty()) {
				thread = "General";
			}
			//This is where the new post is created, in this case it is a private post and has the parameter "hidden" at the end.
			Post post = new Post(Username, postText, adminRole, studentRole, staffRole, likes, views, postTime, postID, thread, softDelete, tags, true);
			System.out.println(postText);
			try {
			applicationMain.FoundationsMain.database.register(post);
			} catch (SQLException e) {
				System.out.println("Failed to register post.");
				System.exit(0);
			}
		}
	}
}