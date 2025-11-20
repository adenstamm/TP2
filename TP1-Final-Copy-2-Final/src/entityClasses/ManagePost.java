package entityClasses;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ManagePost Class</p>
 * 
 * <p> Description: The ManagePost Page View. This class will be used to store new posts as well
 * 	   as updating some attributes that require the database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Ian Johnson
 * 
 * @version 1.00		2025-10-13 Initial version
 *  
 */

public class ManagePost {
	
	
	/*******
	 * <p> Title: ManagePost() Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 */
	
	public ManagePost() {
		// No initialization required at this time
	}
	
	/*****
     * <p> Method: void storePost(User mainUser, String postText, String thread) </p>
     * 
     * <p> Description: This method prepares and stores a post into the database. </p>
     * 
     * @param mainUser the user that wrote the post.
     * 
     * @param postText the text that will be stored in the post
     * 
     * @param thread the specific thread that this post belongs to
     * 
     * @param tags the tags that are associated with the post
     * 
     * @param softDelete determines whether the post is deleted or not
     * 
     */
	
	public static void storePost(User mainUser, String postText, String thread, String tags, boolean softDelete) {
		if(mainUser.getUserName().isEmpty()) {
			System.out.println("Error: The post needs to have a username attached to it.");
			return;
		} else if(postText.isEmpty()) {
			System.out.println("Error: There is no text for the post.");
			return;
			
		} else {
			
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
			posts = applicationMain.FoundationsMain.database.getAllPosts();
			int postID = posts.size() + 1;
			
			if(thread.isEmpty()) {
				thread = "General";
			}
			
			Post post = new Post(Username, postText, adminRole, studentRole, staffRole, likes, views, postTime, postID, thread, softDelete, tags, false);
			System.out.println(postText);
			try {
			applicationMain.FoundationsMain.database.register(post);
			} catch (SQLException e) {
				System.out.println("Failed to register post.");
				System.exit(0);
			}
		}
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
	
	/*****
     * <p> Method: void registerLike(Post post, User user) </p>
     * 
     * <p> Description: This method adds or removes a like to a post given the users that have liked this post before </p>
     * 
     * @param post the post that the user is liking
     * 
     * @param user the user that is liking the post
     * 
     * 
     */
	
	public static void registerLike(Post post, User user) {
		ArrayList<String> likesList = new ArrayList<String>();
		likesList = applicationMain.FoundationsMain.database.getLikesToList(post);
		System.out.println("Registering " + user.getUserName());
		if(likesList.contains(user.getUserName())) {
			likesList.remove(user.getUserName());
			try {
				applicationMain.FoundationsMain.database.registerLikes(likesList, post);
			} catch(SQLException e) {
					System.out.println("Failed to register like.");
			}	
		} 
		else {
			likesList.add(user.getUserName());
			try {
			applicationMain.FoundationsMain.database.registerLikes(likesList, post);
			} catch(SQLException e) {
				System.out.println("Failed to register like.");
			}
		}
	}
	
	/*****
     * <p> Method: void registerView(Post post, User user) </p>
     * 
     * <p> Description: This method adds or removes a view to a post given the users that have liked this post before </p>
     * 
     * @param post the post that the user is viewing
     * 
     * @param user the user that is viewing the post
     * 
     * 
     */
	
	public static void registerView(Post post, User user) {
		ArrayList<String> viewsList = applicationMain.FoundationsMain.database.getViewsToList(post);
		System.out.println("Registering " + user.getUserName());
		if(viewsList.contains(user.getUserName())) {
			viewsList.remove(user.getUserName());

			try {
				applicationMain.FoundationsMain.database.registerViews(viewsList, post);
			} catch(SQLException e) {
					System.out.println("Failed to register view.");
			}	
		} 
		else {
			viewsList.add(user.getUserName());
			try {
			applicationMain.FoundationsMain.database.registerViews(viewsList, post);
			} catch(SQLException e) {
				System.out.println("Failed to register views.");
			}
		}
	}
	
	/*****
     * <p> Method: void deletePost(Post post) </p>
     * 
     * <p> Description: This method deletes a post </p>
     * 
     * @param post the post that is going to be deleted
     * 
     * 
     */
	
	public static void deletePost(Post post) {
		try {
		applicationMain.FoundationsMain.database.deletePost(post);
		} catch (SQLException e) {
			System.out.println("Failed to delete Post");
		}
	}
	/**
	// This is a temporary method for registering the test cases
		public static void registerTestCases() {
			
			User user1 = new User("IanJohnson", "123456aA.", "", "", "", "", "", true, false, false, "", false);
			User user2 = new User("", "123456aA.", "", "", "", "", "", true, false, false, "", false);
			//(String mainUser, String postText, boolean adminRole, boolean studentRole, boolean staffRole, String likes, String views, String postTime, int postID, String thread, boolean softDelete, String tags)
			Post post0 = new Post("", "", true, false, false, "", "", "2025-10-10 1:00", 0, "General", false, "");
			Post post1 = new Post("", "", true, false, false, "", "", "2025-10-10 1:00", 1, "General",  false, "");
			Post post2 = new Post("", "", true, false, false, "", "", "2025-10-10 1:00", 2, "General",  false, "");
			Post post3 = new Post("", "", true, false, false, "", "", "2025-10-10 1:00", 50, "General", false, "");
			
			System.out.println("Registering the first post test case.");
			storePost(user2, "This is the first test case.", "General", "", false);
			System.out.println("Registering the second post test case.");
			storePost(user1, "This is the second test case.", "General", "", false);
			System.out.println("Registering the third post test case.");
			storePost(user1, "", "General", "", false);
			System.out.println("Registering the fourth post test case.");
			storePost(user2, "", "General", "", false);
			System.out.println("Registering the fifth post test case.");
			storePost(user1, "This is the fifth test case", "General", "", false);
			
			System.out.println("Registering the first reply test case.");
			ManageReply.storeReply(post1, user2, "This is the first test case.");
			System.out.println("Registering the second reply test case.");
			ManageReply.storeReply(post0, user1, "This is the second test case.");
			System.out.println("Registering the third reply test case.");
			ManageReply.storeReply(post1, user1, "This is the third test case");
			System.out.println("Registering the fourth reply test case.");
			ManageReply.storeReply(post2, user2, "");
			System.out.println("Registering the fifth reply test case.");
			ManageReply.storeReply(post3, user1, "This is the fifth test case.");
				
		}
	**/
}