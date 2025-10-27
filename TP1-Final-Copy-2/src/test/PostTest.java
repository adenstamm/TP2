package test;

import java.sql.SQLException;
import java.util.List;
import applicationMain.FoundationsMain;
import database.Database;
import entityClasses.Post;
import entityClasses.User;
import entityClasses.ManagePost;

public class PostTest {
    
    private static Database database;
    
    public static void main(String[] args) {
        System.out.println("Post Tests");
        
        // DB connection
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
        
        System.out.println("All tests completed!");
    }
    
    // Test delete post functionality
    public static void testDeletePost() {
        System.out.println("\n--- Testing Delete Post ---");
        
        User testUser = new User("testuser", "password", "Test", "", "User", "Test", "test@email.com", true, true, true, "", false);
        
        ManagePost.storePost(testUser, "This is a test post for deletion", "General", "test,delete", false);
        
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
            
            ManagePost.deletePost(testPost);
            System.out.println("Post deleted successfully");
            
            // Verify deletion
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
    
    // Test keyword search functionality
    public static void testKeywordSearch() {
        System.out.println("Keyword Search");
        
        User testUser = new User("searchuser", "password", "Search", "", "User", "Search", "search@email.com", true, true, true, "", false);
        
        ManagePost.storePost(testUser, "Java programming post", "General", "java,programming", false);
        ManagePost.storePost(testUser, "Database design post", "General", "database,sql", false);
        ManagePost.storePost(testUser, "Web development post", "General", "web,javascript", false);
        
        List<Post> javaPosts = database.getPostsByTag("java");
        System.out.println("Found " + javaPosts.size() + " posts with 'java' tag");
        
        List<Post> dbPosts = database.getPostsByTag("database");
        System.out.println("Found " + dbPosts.size() + " posts with 'database' tag");
        
        List<Post> webPosts = database.getPostsByTag("web");
        System.out.println("Found " + webPosts.size() + " posts with 'web' tag");
        
        List<Post> noPosts = database.getPostsByTag("nonexistent");
        System.out.println("Found " + noPosts.size() + " posts with 'nonexistent' tag");
        
        // Verify search results
        if (javaPosts.size() > 0) {
            System.out.println("Java search working");
        } else {
            System.out.println("Java search failed");
        }
        
        if (dbPosts.size() > 0) {
            System.out.println("Database search working");
        } else {
            System.out.println("Database search failed");
        }
        
        if (webPosts.size() > 0) {
            System.out.println("Web search working");
        } else {
            System.out.println("Web search failed");
        }
        
        if (noPosts.size() == 0) {
            System.out.println("Non-existent tag search working");
        } else {
            System.out.println("Non-existent tag search failed");
        }
    }
}
