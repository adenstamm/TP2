package guiDiscussion;

import entityClasses.User;
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
	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDiscussion.theStage);
	}
	
	protected static void performQuit() {
		System.exit(0);
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
