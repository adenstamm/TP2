package guiViewClosedRequests;

import java.sql.SQLException;
import java.util.List;
import database.Database;
import entityClasses.AdminRequest;

/**
 * <p> Title: ModelViewClosedRequests Class. </p>
 * <p> Description: The model handles the business logic for viewing and managing closed admin requests.
 * It bridges the gap between the Controller and the Database for retrieving and updating requests. </p>
 * @author Auto-generated
 * @version 1.00
 */
public class ModelViewClosedRequests {

    private Database database;
    private String statusMessage;

    /**
     * Constructor for the Model.
     * @param database The active database instance containing the session and connection.
     */
    public ModelViewClosedRequests(Database database) {
        this.database = database;
        this.statusMessage = "";
    }

    /**
     * Retrieves all closed admin requests from the database.
     * @return List of AdminRequest objects with status "Closed", or null on error
     */
    public List<AdminRequest> getClosedRequests() {
        try {
            return database.getAllClosedRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error retrieving closed requests.";
            return null;
        }
    }

    /**
     * Reopens a closed request by changing its status to "Open".
     * @param requestID The ID of the request to reopen
     * @return True if the request was successfully reopened, false otherwise
     */
    public boolean reopenRequest(int requestID) {
        try {
            database.updateRequestStatus(requestID, "Open");
            statusMessage = "Request reopened successfully!";
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error reopening request.";
            return false;
        }
    }

    /**
     * Updates the description of an admin request.
     * @param requestID The ID of the request to update
     * @param newDescription The new description text
     * @return True if the description was successfully updated, false otherwise
     */
    public boolean updateRequestDescription(int requestID, String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            statusMessage = "Error: Description cannot be empty.";
            return false;
        }
        try {
            database.updateRequestDescription(requestID, newDescription);
            statusMessage = "Description updated successfully!";
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error updating description.";
            return false;
        }
    }

    /**
     * Retrieves the status message of the last operation.
     * @return A string containing the success or error message.
     */
    public String getStatusMessage() {
        return statusMessage;
    }
}

