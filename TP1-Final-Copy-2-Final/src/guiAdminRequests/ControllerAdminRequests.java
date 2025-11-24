package guiAdminRequests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;

import entityClasses.AdminRequest;
import guiAddRemoveRoles.ViewAddRemoveRoles;
import javafx.scene.control.TextArea;

public class ControllerAdminRequests {

	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static ModelAdminRequests model;
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	public static List<String> loadRequests() {
		List<Integer> temp = model.getRequestList();
		List<String> requestList = new ArrayList<>();
		requestList.add("<Select a Request>");
		for (int id : temp) {
			requestList.add(Integer.toString(id));
		}
		
		return requestList;
	}
	
	protected static void doSelectRequest(String requestID) {
		if (requestID.compareTo("<Select a Request>") == 0) {
			ModelAdminRequests.setSelectedRequest(null);
			repaintTheWindow();
			return;
		}
		
		
		AdminRequest selectedRequest = ModelAdminRequests.getRequest(Integer.valueOf(requestID));
		ModelAdminRequests.setSelectedRequest(selectedRequest);
		repaintTheWindow();
	}
	
	protected static void performAddDocumentation() {
		if (ModelAdminRequests.selectedRequest == null) {
			repaintTheWindow();
			return;
		}
		
		 ViewAdminRequests.theRootPane.getChildren().removeAll(
				 ViewAdminRequests.button_addDocs, ViewAdminRequests.button_viewDocs,
				 ViewAdminRequests.button_originalRequest);
		 
		 ViewAdminRequests.theRootPane.getChildren().addAll(
				 ViewAdminRequests.text_addDocs, ViewAdminRequests.button_back, ViewAdminRequests.button_submit);
	}
		
		
	
	protected static void performViewDocumentation() {
		if (ModelAdminRequests.selectedRequest == null) return;
		
		 ViewAdminRequests.theRootPane.getChildren().removeAll(
				 ViewAdminRequests.button_addDocs, ViewAdminRequests.button_viewDocs);
		 
		 
		 ViewAdminRequests.vbox_docs.getChildren().clear();
		 
		 List<String> docs = ModelAdminRequests.getDocsList();
		 if (docs == null) {
			 TextArea ta = new TextArea("");
			 ta.setWrapText(true);
			 //ta.setPrefHeight(100);  // optional
			 ta.setEditable(false);
			 ViewAdminRequests.vbox_docs.getChildren().add(ta);
		 }
		 else {
			 for (String doc : docs) {
				 TextArea ta = new TextArea(doc);
				 ta.setWrapText(true);
				 //ta.setPrefHeight(100);  // optional
				 ta.setEditable(false);
				 ViewAdminRequests.vbox_docs.getChildren().add(ta);
			 }
		 }
		 
		 
		 ViewAdminRequests.theRootPane.getChildren().addAll(
				 ViewAdminRequests.scroll_docs, ViewAdminRequests.button_back);
		 
		 int originalID = ModelAdminRequests.selectedRequest.getOriginalID();
		 if (originalID != -1) {
			 
		 }
		 
		 ViewAdminRequests.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		 ViewAdminRequests.theStage.setScene(ViewAdminRequests.theScene);
		 ViewAdminRequests.theStage.show();
	}
	
	protected static void performOriginalRequest() {
		String ogID = Integer.toString(ModelAdminRequests.selectedRequest.getOriginalID());
		ViewAdminRequests.combobox_requestSelect.setValue(ogID);
		doSelectRequest(ogID);
	}

	
	protected static void performBack() {
		ViewAdminRequests.text_addDocs.setText("");
		repaintTheWindow();
	}
	
	protected static void performSubmit() {
		System.out.println("in");
		ModelAdminRequests.addDocs(ViewAdminRequests.text_addDocs.getText());
		ViewAdminRequests.text_addDocs.setText("");
	}
	
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewAdminRequests.theRootPane.getChildren().clear();
		
		// Defermine which of the two views to show to the user
		if (ModelAdminRequests.selectedRequest == null) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewAdminRequests.theRootPane.getChildren().addAll(
					ViewAdminRequests.titleLabel, ViewAdminRequests.label_UserDetails,
					ViewAdminRequests.instructionLabel, ViewAdminRequests.line_Separator1,
					ViewAdminRequests.combobox_requestSelect,
					ViewAdminRequests.line_Separator4, ViewAdminRequests.button_Return,
					ViewAdminRequests.button_Logout, ViewAdminRequests.button_Quit);
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewAdminRequests.theRootPane.getChildren().addAll(
					ViewAdminRequests.titleLabel, ViewAdminRequests.label_UserDetails,
					ViewAdminRequests.instructionLabel, ViewAdminRequests.line_Separator1,
					ViewAdminRequests.combobox_requestSelect,
					ViewAdminRequests.button_addDocs, ViewAdminRequests.button_viewDocs,
					ViewAdminRequests.line_Separator4, ViewAdminRequests.button_Return,
					ViewAdminRequests.button_Logout, ViewAdminRequests.button_Quit);
			
			if (ModelAdminRequests.selectedRequest.getOriginalID() != -1) {
				ViewAdminRequests.theRootPane.getChildren().add(
						ViewAdminRequests.button_originalRequest);
			}
			
		}
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewAdminRequests.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewAdminRequests.theStage.setScene(ViewAdminRequests.theScene);
		ViewAdminRequests.theStage.show();
	}
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		ModelAdminRequests.selectedRequest = null;
		ViewAdminRequests.combobox_requestSelect.setValue("<Select a Request>");
		doSelectRequest("<Select a Request>");
		
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewAdminRequests.theStage,
				ViewAdminRequests.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminRequests.theStage);
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
