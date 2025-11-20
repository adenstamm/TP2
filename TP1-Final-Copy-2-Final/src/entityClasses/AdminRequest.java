package entityClasses;

/**
 * <p> Title: AdminRequest Class </p>
 * <p> Description: Entity class for the HW3 Prototype (Task 5).
 * This class represents a single request from a staff member to an admin. </p>
 * * @author Arnav Pushkarna
 */
public class AdminRequest {

    private int requestID;
    private String creatorUsername;
    private String description;
    private String status;
    private String resolutionNote;

    /**
     * Constructor for creating a new request.
     * @param creatorUsername The staff member creating the request.
     * @param description     The text of the request.
     */
    public AdminRequest(String creatorUsername, String description) {
        this.creatorUsername = creatorUsername;
        this.description = description;
        this.status = "Open"; // Default status on creation
        this.resolutionNote = ""; // Default empty note
        this.requestID = -1; // -1 means it's not yet in the DB
    }

    /**
     * Full constructor for retrieving a request from the database.
     */
    public AdminRequest(int id, String creator, String desc, String status, String note) {
        this.requestID = id;
        this.creatorUsername = creator;
        this.description = desc;
        this.status = status;
        this.resolutionNote = note;
    }

    // --- Getters ---
    public int getRequestID() { return requestID; }
    public String getCreatorUsername() { return creatorUsername; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getResolutionNote() { return resolutionNote; }

    // --- Setters ---
    // (Used by the prototype to set the ID after database insertion)
    public void setRequestID(int id) { this.requestID = id; }
}