package guiStaff;

import guiStudent.ViewStudentHome;

public class ControllerStaffHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	**********************************************************************************************/
	
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewStaffHome.theStage, ViewStaffHome.theUser, false);
	}	

	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewStaffHome.theStage);
	}
	
	protected static void performQuit() {
		System.exit(0);
	}
	
	protected static void goToDiscussion() {
		
		guiDiscussion.ViewDiscussion.displayDiscussion(ViewStaffHome.theStage, 
				ViewStaffHome.theUser);
	}
	protected static void goToManageThreads() {
		
		guiManageThreads.ViewManageThreads.displayThreads(ViewStaffHome.theStage, 
				ViewStaffHome.theUser);
	}

}
