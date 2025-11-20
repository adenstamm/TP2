package guiDeleteUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import database.Database;
import entityClasses.User;
import guiDeleteUser.ViewDeleteUser;
import guiDeleteUser.ViewDeleteUser;
import guiDeleteUser.ViewDeleteUser;
import guiAdminHome.ViewAdminHome;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ControllerDeleteUser {

	/*-********************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.

	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		


	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectUser() {
		ViewDeleteUser.theSelectedUser = 
				(String) ViewDeleteUser.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewDeleteUser.theSelectedUser);
		setupSelectedUser();

	}


	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewDeleteUser.theRootPane.getChildren().clear();


		// Only show the request to select a user to be updated and the ComboBox
		ViewDeleteUser.theRootPane.getChildren().addAll(
				ViewDeleteUser.label_PageTitle, ViewDeleteUser.label_UserDetails, 
				ViewDeleteUser.button_UpdateThisUser, ViewDeleteUser.line_Separator1,
				ViewDeleteUser.label_SelectUser, ViewDeleteUser.combobox_SelectUser, 
				ViewDeleteUser.button_DeleteUser, ViewDeleteUser.line_Separator4, 
				ViewDeleteUser.button_Return, ViewDeleteUser.button_Logout, 
				ViewDeleteUser.button_Quit);


		// Add the list of widgets to the stage and show it

		// Set the title for the window
		ViewDeleteUser.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewDeleteUser.theStage.setScene(ViewDeleteUser.theDeleteUserScene);
		ViewDeleteUser.theStage.show();
	}


	/**********
	 * <p> Method: setupSelectedUser() </p>
	 * 
	 * <p> Description: This method fetches the current values for the widgets whose values change
	 * based on which user has been selected and any actions that the admin takes. </p>
	 * 
	 */
	private static void setupSelectedUser() {
		System.out.println("*** Entering setupSelectedUser");

		// Disable button if no user is selected
		if (ViewDeleteUser.theSelectedUser != null &&
				ViewDeleteUser.theSelectedUser.compareTo("<Select a User>") != 0) {
			ViewDeleteUser.button_DeleteUser.setDisable(false);
		}
		else
			ViewDeleteUser.button_DeleteUser.setDisable(true);

		repaintTheWindow();
	}


	/**********
	 * <p> Method: performDeleteUser() </p>
	 * 
	 * <p> Description: This method adds a new role to the list of role in the ComboBox select
	 * list. </p>
	 * 
	 */
	protected static void performDeleteUser() {

		// Create alert window to confirm user choice
		Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDialog.setTitle("Confirm Deletion");
		confirmDialog.setHeaderText("Are you sure you want to delete this user?");
		confirmDialog.setContentText("This action cannot be undone.");

		// Show the dialog and capture the result
		Optional<ButtonType> result = confirmDialog.showAndWait();

		// If confirmed, delete account
		if (result.isPresent() && result.get() == ButtonType.OK) {
			boolean res = theDatabase.deleteAccount(ViewDeleteUser.theSelectedUser);
			ViewDeleteUser.refreshUsers();

			if (res)
				System.out.println("Account successfully deleted!");
			else
				System.out.println("Issue deleting account.");
		}

		ViewDeleteUser.theSelectedUser = "<Select a User>";

	}


	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewDeleteUser.theStage,
				ViewDeleteUser.theUser);
	}


	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDeleteUser.theStage);
	}


	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}