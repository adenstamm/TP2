package database;

import java.sql.SQLException;
import java.util.Optional;

import applicationMain.FoundationsMain;
import entityClasses.ManagePost;
import entityClasses.Post;
import entityClasses.StorePrivatePost;
import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import guiDiscussion.ViewDiscussion;

/*******
 * <p> Title: NewPostTests Class. </p>
 * 
 * <p> Description: This class is used to test the ability to store posts without loss or corruption
 * 					of important information. This tests the roles, hidden modifier and the post text
 * 					feature.
 * </p>
 * 
 * <p> Copyright: Ian Johnson © 2025 </p>
 * 
 * @author Ian Johnson
 * 
 * @version 1.00
 */


public class NewPostTests {
	
	/*-*******************************************************************************************
     * Attributes
     *-------------------------------------------------------------------------------------------*/
    /** Shared handle to the app database for these tests. */
	private static Database database;
	
	/**
	 * Default constructor for PostTests.
	 * Initializes the controller with default values and no special setup.
	 */
	public NewPostTests() {
		// No initialization required at this time
	}
	
	/*****
     * <p> Method: void storePosts() </p>
     * 
     * <p> Description: This method stores posts into the database that will be accessed by each test.
     * 				    It creates posts that are written by Students, Staff, and Admins. It also creates
     *  				posts that are hidden and others that aren't</p>
     * 
     */
	public void StorePosts() {
		try {
            database = new Database();
            database.connectToDatabase();
            FoundationsMain.database = database;
        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
            return;
        }
		
		User Admin = new User(
	            "Admin", "password", "Test", "", "User", "Test",
	            "test1@email.com", true, false, false, "", false
	        );
		User Student = new User(
	            "Student", "password", "Test", "", "User", "Test",
	            "test2@email.com", false, true, false, "", false
	        );
		User Staff = new User(
	            "Staff", "password", "Test", "", "User", "Test",
	            "test3@email.com", false, false, true, "", false
	        );
		
		
		StorePrivatePost.storePrivatePost(Student, "This is a post", "General", "test", false);
		ManagePost.storePost(Student, "This is another student Post", "", "", false);
		StorePrivatePost.storePrivatePost(Student, "This is the third student Post", "", "", false);
		
		StorePrivatePost.storePrivatePost(Staff, "This is a post", "", "", false);
		ManagePost.storePost(Staff, "This is another staff Post", "", "", false);
		StorePrivatePost.storePrivatePost(Staff, "This is the third staff Post", "", "", false);
		
		StorePrivatePost.storePrivatePost(Admin, "This is a post", "", "", false);
		ManagePost.storePost(Admin, "This is another admin Post", "", "", false);
		StorePrivatePost.storePrivatePost(Admin, "This is the third admin Post", "", "", false);
	}
	/*****
     * <p> Method: void TestRoles() </p>
     * 
     * <p> Description: This method gets all the posts stored in the database and stores them in a list.
     *  				The method goes through each one testing all the roles that are stored and checks
     *   				to see if they are the correct ones.</p>
     * 
     */
	//@Test
	void TestRoles() {}
	
	/*****
     * <p> Method: void TestVisibility() </p>
     * 
     * <p> Description: This method gets all the posts stored in the database and stores them in a list.
     *  				The method goes through each one testing the visibility of the post. This is done
     *  				by checking the value of the "hidden" attribute..</p>
     * 
     */
	//@Test
	void TestVisibility() {}
	
	/*****
     * <p> Method: void TestTextInfo() </p>
     * 
     * <p> Description: This method gets all the posts stored in the database and stores them in a list.
     *  				The method goes through each comparing the text to the original to ensure that the
     *  				contents are not changed.</p>
     * 
     */
	//@Test
	void TestTextInfo() {}
	
}
