package guiAdminRequests;

import java.sql.SQLException;
import java.util.List;

import database.Database;
import entityClasses.User;
import entityClasses.AdminRequest;

public class ModelAdminRequests {

	// Static attributes
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static AdminRequest selectedRequest;
	
	/**
     * Public constructor
     */
	public ModelAdminRequests(Database theDatabase) {
		this.theDatabase = theDatabase;
	}
	
	public static List<Integer> getRequestList() {
		return theDatabase.getRequestList();
	}
	
	public static AdminRequest getRequest(int ID) { 
		try {
			return theDatabase.getRequestByID(ID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setSelectedRequest(AdminRequest request) {
		selectedRequest = request;
	}
	
	public static void addDocs(String text) {
		if (text == null) return;
			
		selectedRequest.addDocumentation(text);
	}
	
	public static List<String> getDocsList() {
		return selectedRequest.documentationToList();
	}
	
	
	
}
