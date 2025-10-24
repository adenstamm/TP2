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
     */
	
	public static void storePost(User mainUser, String postText, String thread, String tags) {
		if(mainUser.getUserName().isEmpty()) {
			System.out.println("Error: The post needs to have a username attached to it.");
			return;
		} else if(postText.isEmpty()) {
			System.out.println("Error: There is no text for the post.");
			return;
			
		} else {
			
			String Username = mainUser.getUserName();
			boolean adminRole = mainUser.getAdminRole();
			boolean studentRole = mainUser.getNewRole1();
			boolean staffRole = mainUser.getNewRole2();
			String likes = "";
			int views = 0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
			LocalDateTime Time = LocalDateTime.now().plusDays(1).plusHours(0).plusMinutes(0);
			String postTime = Time.format(formatter);
			List<Post> posts = new ArrayList<>();
			posts = applicationMain.FoundationsMain.database.getAllPosts();
			int postID = posts.size() + 1;
			
			if(thread.isEmpty()) {
				thread = "General";
			}
			
			Post post = new Post(Username, postText, adminRole, studentRole, staffRole, likes, views, postTime, postID, thread, tags);
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
}