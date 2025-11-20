package guiManageThreads;

import java.util.Optional;

import entityClasses.Post;
import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/*******
 * <p> Title: ModelDiscussion Class. </p>
 * 
 * <p> Description: The Discussion Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting discussion information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ControllerManageThreads {
	/**
	 * Default constructor for ControllerDiscussion.
	 * Initializes the controller with default values and no special setup.
	 */
	public ControllerManageThreads() {
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewManageThreads.theStage);
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
}