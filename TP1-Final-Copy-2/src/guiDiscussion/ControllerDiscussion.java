package guiDiscussion;

import java.util.Optional;

import entityClasses.Post;
import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


/*******
 * <p> Title: ControllerDiscussion Class. </p>
 * 
 * <p> Description: The Discussion Page Controller. Sets up the conditions for ViewDiscussion
 * to properly run. Manages where the user will go when entering and leaving the discussion page</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ControllerDiscussion {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	**********************************************************************************************/
	
	/*******
	 * <p> Title: ControllerDiscussion Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 */
	
	public ControllerDiscussion() {
		// No initialization required at this time
	}
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method should log the user out of their account. </p>
	 * 
	 * 
	 * 
	 */
	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDiscussion.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method should quit the program. </p>
	 * 
	 * 
	 * 
	 */
	
	protected static void performQuit() {
		System.exit(0);
	}
	
	/**********
	 * <p> Method: goToUserHomePage(theStage Stage, theUser User) </p>
	 * 
	 * <p> Description: Directs the user back to their homepage. </p>
	 * 
	 * @param theStage The current stage of the program.
	 * 
	 * @param theUser The current user that is going to the home page.
	 * 
	 */
	
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiStudent.ViewStudentHome.displayStudentHome(theStage, theUser);
			break;
		case 3:
			guiStaff.ViewStaffHome.displayStaffHome(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
	
	/**********
	 * <p> Method: doSelectThread() </p>
	 * 
	 * <p> Description: This method should change which posts are displayed </p>
	 * 
	 * 
	 * 
	 */
	protected static void doSelectThread() {
		return;
	}
	
	/**********
	 * <p> Method: performDeletePost(post Post) </p>
	 * 
	 * <p> Description: This method should delete the specified post. </p>
	 * 
	 * @param post The post that will be deleted.
	 * 
	 */
	protected static void performDeletePost(Post post) {

		// Create alert window to confirm user choice
		Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDialog.setTitle("Confirm Deletion");
		confirmDialog.setHeaderText("Are you sure you want to delete this post?");
		confirmDialog.setContentText("This action cannot be undone.");

		// Show the dialog and capture the result
		Optional<ButtonType> result = confirmDialog.showAndWait();
		
		// If confirmed, delete account
		if (result.isPresent() && result.get() == ButtonType.OK) {
			entityClasses.ManagePost.deletePost(post);
			ViewDiscussion.refreshPosts();
		}
	}
	
}
