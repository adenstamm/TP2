package guiManageAdminRequests;

import java.sql.SQLException;
import java.util.List;
import database.Database;
import entityClasses.AdminRequest;

/**
 * <p> Title: ModelManageAdminRequests Class. </p>
 * <p> Description: The model handles the business logic for viewing and managing admin requests.
 * It bridges the gap between the Controller and the Database for retrieving and closing requests. </p>
 * @author Aden stamm
 * @version 1.00
 */
public class ModelManageAdminRequests {

    private Database database;
    private String statusMessage;

    /**
     * Constructor for the Model.
     * @param database The active database instance containing the session and connection.
     */
    public ModelManageAdminRequests(Database database) {
        this.database = database;
        this.statusMessage = "";
    }

    /**
     * Retrieves all open admin requests from the database.
     * @return List of AdminRequest objects with status "Open", or null on error
     */
    public List<AdminRequest> getOpenRequests() {
        try {
            return database.getAllOpenRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error retrieving open requests.";
            return null;
        }
    }

    /**
     * Retrieves all admin requests from the database (both open and closed).
     * @return List of all AdminRequest objects, or null on error
     */
    public List<AdminRequest> getAllRequests() {
        try {
            return database.getAllRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error retrieving requests.";
            return null;
        }
    }

    /**
     * Closes an admin request by setting its status to "Closed" and adding a resolution note.
     * @param requestID The ID of the request to close
     * @param resolutionNote The resolution note explaining how the request was resolved
     * @return True if the request was successfully closed, false otherwise
     */
    public boolean closeRequest(int requestID, String resolutionNote) {
        if (resolutionNote == null) {
            resolutionNote = "";
        }
        try {
            database.closeRequest(requestID, resolutionNote);
            statusMessage = "Request closed successfully!";
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Error closing request.";
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