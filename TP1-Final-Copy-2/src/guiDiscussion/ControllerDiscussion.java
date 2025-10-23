package guiDiscussion;

import java.sql.SQLException;
import java.util.Optional;

import database.Database;
import entityClasses.User;
import guiDiscussion.ViewDiscussion;
import guiManageInvites.ViewManageInvites;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import entityClasses.Post;

public class ControllerDiscussion {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	**********************************************************************************************/
	
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	

	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDiscussion.theStage);
	}
	
	protected static void performQuit() {
		System.exit(0);
	}
	
	/**********
	 * <p> Method: doSelectThread() </p>
	 * 
	 * <p> Description: THis method should change which posts are displayed </p>
	 * 
	 * 
	 * 
	 */
	protected static void doSelectThread() {
		return;
	}

	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiRole1.ViewRole1Home.displayRole1Home(theStage, theUser);
			break;
		case 3:
			guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
	
	
	/**********
	 * <p> Method: performDeletePost() </p>
	 * 
	 * <p> Description: This method calls for an alert and nullifies the post. </p>
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
