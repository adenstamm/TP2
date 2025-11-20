package test;

import java.sql.SQLException;
import java.util.List;
import applicationMain.FoundationsMain;
import database.Database;
import entityClasses.Post;
import entityClasses.User;
import entityClasses.ManagePost;

/*******
 * <p> Title: PostTest Class. </p>
 *
 * <p> Description: Tiny console test harness I wrote to check post
 * create/delete, tag search, and liking against our in-memory DB. Runs a couple of
 * focused tests and prints what happened. </p>
 *
 * <p> Copyright: Aden Stamm© 2025 </p>
 *
 * @author Aden Stamm
 * @version 1.01   2025-10-27  Initial test scaffold
 */

public class PostTest {

	/**
	 * Default constructor for PostTest.
	 * Initializes the controller with default values and no special setup.
	 */
	public PostTest() {
		// No initialization required at this time
	}
	
    /*-*******************************************************************************************
     * Attributes
     *-------------------------------------------------------------------------------------------*/
    /** Shared handle to the app database for these tests. */
    private static Database database;

    /*-*******************************************************************************************
     * Main
     *-------------------------------------------------------------------------------------------*/

    /**********
     * <p> Method: main(String[] args) </p>
     *
     * <p> Description: Bootstraps a DB connection, runs the delete and keyword
     * search tests, and logs simple pass/fail style messages to stdout. </p>
     *
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println("Post Tests");

        // Bring up the DB and hook FoundationsMain.database so helpers use the same instance.
        try {
            database = new Database();
            database.connectToDatabase();
            FoundationsMain.database = database;
        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
            return;
        }

        testDeletePost();
        testKeywordSearch();
        testLikePost();

        System.out.println("All tests completed!");
    }

    /*-*******************************************************************************************
     * Tests
     *-------------------------------------------------------------------------------------------*/

    /**********
     * <p> Method: testDeletePost() </p>
     *
     * <p> Description: Creates a throwaway post, deletes it (soft delete),
     * then confirms the record shows softDelete=true. </p>
     */
    public static void testDeletePost() {
        System.out.println("\n--- Testing Delete Post ---");

        // Minimal user just for this test.
        User testUser = new User(
            "testuser", "password", "Test", "", "User", "Test",
            "test@email.com", true, true, true, "", false
        );

        // Seed a post to remove.
        ManagePost.storePost(
            testUser,
            "This is a test post for deletion",
            "General",
            "test,delete",
            false
        );

        // Find the seeded post by exact text.
        List<Post> posts = database.getAllPosts();
        Post testPost = null;
        for (Post post : posts) {
            if (post.getPostText().equals("This is a test post for deletion")) {
                testPost = post;
                break;
            }
        }

        if (testPost != null) {
            System.out.println("Found test post with ID: " + testPost.getPostID());

            // Exercise: delete then verify softDelete flips to true.
            ManagePost.deletePost(testPost);
            System.out.println("Post deleted successfully");

            List<Post> postsAfterDelete = database.getAllPosts();
            for (Post post : postsAfterDelete) {
                if (post.getPostID() == testPost.getPostID() && post.getSoftDelete()) {
                    System.out.println("Post soft deleted confirmed");
                    break;
                }
            }
        } else {
            System.out.println("Post not found");
        }
    }

    /**********
     * <p> Method: testKeywordSearch() </p>
     *
     * <p> Description: Seeds three posts with different tag sets, then checks
     * that getPostsByTag(tag) returns sensible counts for java/database/web
     * and zero for a tag that doesn’t exist. </p>
     */
    public static void testKeywordSearch() {
        System.out.println("\n--- Keyword Search ---");

        // Separate user
        User testUser = new User(
            "searchuser", "password", "Search", "", "User", "Search",
            "search@email.com", true, true, true, "", false
        );

        // three posts with distinct tag buckets.
        ManagePost.storePost(testUser, "Java programming post",     "General", "java,programming", false);
        ManagePost.storePost(testUser, "Database design post",      "General", "database,sql",     false);
        ManagePost.storePost(testUser, "Web development post",      "General", "web,javascript",   false);

        // Queries by tag
        List<Post> javaPosts = database.getPostsByTag("java");
        System.out.println("Found " + javaPosts.size() + " posts with 'java' tag");

        List<Post> dbPosts = database.getPostsByTag("database");
        System.out.println("Found " + dbPosts.size() + " posts with 'database' tag");

        List<Post> webPosts = database.getPostsByTag("web");
        System.out.println("Found " + webPosts.size() + " posts with 'web' tag");

        List<Post> noPosts = database.getPostsByTag("nonexistent");
        System.out.println("Found " + noPosts.size() + " posts with 'nonexistent' tag");

        // Assertions for 100 percent certainty
        System.out.println(javaPosts.size() > 0 ? "Java search working"      : "Java search failed");
        System.out.println(dbPosts.size()   > 0 ? "Database search working"  : "Database search failed");
        System.out.println(webPosts.size()  > 0 ? "Web search working"       : "Web search failed");
        System.out.println(noPosts.size()   == 0 ? "Non-existent tag search working" : "Non-existent tag search failed");
    }

    /**********
     * <p> Method: testLikePost() </p>
     *
     * <p> Description: Creates a user and a post, then tests liking the post
     * and verifies that the user has liked the post. </p>
     */
    public static void testLikePost() {
        System.out.println("\n--- Testing Like Post ---");

        // Create a test user for liking posts.
        User testUser = new User(
            "likeuser", "password", "Like", "", "User", "Like",
            "like@email.com", true, true, true, "", false
        );

        // Create a post to like.
        ManagePost.storePost(
            testUser,
            "This is a test post for liking",
            "General",
            "test,like",
            false
        );

        // Find the created post by exact text.
        List<Post> posts = database.getAllPosts();
        Post testPost = null;
        for (Post post : posts) {
            if (post.getPostText().equals("This is a test post for liking")) {
                testPost = post;
                break;
            }
        }

        if (testPost != null) {
            System.out.println("Found test post with ID: " + testPost.getPostID());

            // Test: like the post then verify the user has liked it.
            ManagePost.registerLike(testPost, testUser);
            System.out.println("Post liked successfully");

            // Verify the user has liked the post.
            if (verifyUserLikedPost(testPost, testUser)) {
                System.out.println("User like verification successful");
            } else {
                System.out.println("User like verification failed");
            }

            // Test: unlike the post then verify the user has unliked it.
            ManagePost.registerLike(testPost, testUser);
            System.out.println("Post unliked successfully");

            // Verify the user has unliked the post.
            if (!verifyUserLikedPost(testPost, testUser)) {
                System.out.println("User unlike verification successful");
            } else {
                System.out.println("User unlike verification failed");
            }
        } else {
            System.out.println("Post not found");
        }
    }

    /**********
     * <p> Method: verifyUserLikedPost(Post post, User user) </p>
     *
     * <p> Description: Helper method to verify if a user has liked a specific post. </p>
     *
     * @param post the post to check
     * @param user the user to check for
     * @return true if the user has liked the post, false otherwise
     */
    public static boolean verifyUserLikedPost(Post post, User user) {
        try {
            // Refresh the post from the database to get the latest likes
            List<Post> allPosts = database.getAllPosts();
            Post refreshedPost = null;
            for (Post p : allPosts) {
                if (p.getPostID() == post.getPostID()) {
                    refreshedPost = p;
                    break;
                }
            }
            
            if (refreshedPost == null) {
                System.out.println("Could not find post in database");
                return false;
            }
            
            List<String> likesList = database.getLikesToList(refreshedPost);
            // Filter out empty strings since the database stores likes with spaces
            // and empty strings can be created when splitting
            for (String like : likesList) {
                if (!like.trim().isEmpty() && like.trim().equals(user.getUserName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error verifying user like: " + e.getMessage());
            return false;
        }
    }
}
