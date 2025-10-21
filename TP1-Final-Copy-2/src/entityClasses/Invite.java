package entityClasses;

/*******
 * <p> Title: User Class </p>
 * 
 * <p> Description: This Invite class represents an invitation in the system.  It contains the invite's
 *  details such as code, email, roles, and deadline. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * 
 */ 

public class Invite {
	
	/*
	 * These are the private attributes for this entity object
	 */
    private String code;
    private String emailAddress;
    private String role;
    private String deadlineString;
    
    /*****
     * <p> Method: Invite() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
    public Invite() {
    	
    }

    
    /*****
     * <p> Method: User(String code, String emailAddress, String role, String deadlineSring) </p>
     * 
     * <p> Description: This constructor is used to establish invites. </p>
     * 
     * @param code specifies the invite code to create the account
     * 
     * @param emailAddress specifies the emailAddress it was sent to
     * 
     * @param role specifies role code
     * 
     * @param deadlineString specifies the deadline
     * 
     * 
     */
    // Constructor to initialize a new User object with userName, password, and role.
    public Invite(String code, String emailAddress, String role, String deadlineString) {
    	this.code = code;
    	this.emailAddress = emailAddress;
    	this.role = role;
    	this.deadlineString = deadlineString;
    }
    
    /*****
     * <p> Method: String getInviteCode() </p>
     * 
     * <p> Description: This getter returns the code. </p>
     * 
     * @return a String of the code
     */
    // Gets the current value of the UserName.
    public String getInviteCode() { return code; }

    /*****
     * <p> Method: String getRole() </p>
     * 
     * <p> Description: This getter returns the number relative to the code. </p>
     * 
     * @return a String of the role
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getRole() { return role; }
    
    /*****
     * <p> Method: String getDeadlineString() </p>
     * 
     * <p> Description: This getter returns the deadline as a string. </p>
     * 
     * @return a String of the deadline.
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getDeadlineString() { return deadlineString; }
    
    /*****
     * <p> Method: String getEmailAddress() </p>
     * 
     * <p> Description: This getter returns the EmailAddress. </p>
     * 
     * @return a String of the EmailAddress
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getEmailAddress() { return emailAddress; }

}
