package guiRequestAdminAction;

import java.sql.SQLException;
import database.Database;
import entityClasses.AdminRequest;

/**
 * <p> Title: ModelRequestAdminAction Class. </p>
 * * <p> Description: The model handles the business logic for the Admin Request feature.
 * It bridges the gap between the Controller and the Database for submitting requests. </p>
 * * <p> Copyright: Arnav Pushkarna © 2025 </p>
 * * @author Arnav Pushkarna
 * * @version 1.00
 */
public class ModelRequestAdminAction {

    private Database database;
    private String statusMessage;

    /**
     * Constructor for the Model.
     * * @param database The active database instance containing the session and connection.
     */
    public ModelRequestAdminAction(Database database) {
        this.database = database;
        this.statusMessage = "";
    }

    /**
     * Creates a new AdminRequest and registers it in the database.
     * * @param description The detailed text of the request.
     * @param username The username of the staff member making the request.
     * @return True if the request was successfully saved, false otherwise.
     */
    public boolean submitRequest(String description, String username) {
        if (description == null || description.trim().isEmpty()) {
            statusMessage = "Error: Description cannot be empty.";
            return false;
        }

        if (username == null) {
            statusMessage = "Error: No user is currently logged in.";
            return false;
        }

        AdminRequest newRequest = new AdminRequest(username, description);

        try {
            database.register(newRequest);
            statusMessage = "Request submitted successfully!";
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage = "Database Error: Could not save request.";
            return false;
        }
    }

    /**
     * Retrieves the status message of the last operation.
     * * @return A string containing the success or error message.
     */
    public String getStatusMessage() {
        return statusMessage;
    }
}