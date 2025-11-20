package guiListUsers;

import guiAdminHome.ViewAdminHome;

public class ControllerListUsers {

	/**********
	 * <p> Method: performReturn </p>
	 * * <p> Description: This method returns the user to the Admin Home page.</p>
	 * */
	protected static void performReturn() {
		ViewAdminHome.displayAdminHome(ViewListUsers.theStage, ViewListUsers.theUser);
	}
}

