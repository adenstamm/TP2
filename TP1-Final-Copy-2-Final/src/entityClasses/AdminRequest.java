package entityClasses;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private String documentation;
    private int originalID = -1;

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
        this.documentation = null;
        this.originalID = -1;
    }

    /**
     * Full constructor for retrieving a request from the database.
     */
    public AdminRequest(int id, String creator, String desc, String status, 
    		String note, String documentation, int originalID) {
        this.requestID = id;
        this.creatorUsername = creator;
        this.description = desc;
        this.status = status;
        this.resolutionNote = note;
        this.documentation = documentation;
        this.originalID = originalID;
    }

    // --- Getters ---
    public int getRequestID() { return requestID; }
    public String getCreatorUsername() { return creatorUsername; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getResolutionNote() { return resolutionNote; }
    public String getDocumentation() { return this.documentation; }
    public int getOriginalID() { return this.originalID; }

    // --- Setters ---
    // (Used by the prototype to set the ID after database insertion)
    public void setRequestID(int id) { this.requestID = id; }
    
    // Maintenance
    public void addDocumentation(String toAdd) {
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        
    	this.documentation += "toAdd" + "-*-*-\n";
    }
    
    public List<String> documentationToList() {
    	if (this.documentation == null) return null;
    	
    	List<String> updates = new ArrayList<>();
    	char[] delimiter = {'-', '*', '-', '*', '-', '\n'};
    	int n = this.documentation.length();
    	int i = 0;
    	int delimitInd = 0;
    	int currChunk = 0;
    	while (i < n) {
    		if (documentation.charAt(i) == delimiter[delimitInd]) ++delimitInd;
    		else delimitInd = 0;
    		
    		if (delimiter.length == delimitInd) {
    			updates.add(documentation.substring(currChunk, currChunk - delimitInd + 1));
    			currChunk = i + 1;
    			delimitInd = 0;
    		}
    		
    		++i;
    	}
    	
    	return updates;
    }
    
}