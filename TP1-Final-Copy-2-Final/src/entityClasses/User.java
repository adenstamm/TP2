package entityClasses;

/*******
 * <p> Title: User Class </p>
 * 
 * <p> Description: This User class represents a user entity in the system.  It contains the user's
 *  details such as userName, password, and roles being played. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * 
 */ 

public class User {
	
	/*
	 * These are the private attributes for this entity object
	 */
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private String emailAddress;
    private boolean adminRole;
    private boolean studentRole;
    private boolean staffRole;
    private String oneTimePassword;
    private boolean hasOneTimePassword;
    
    /*****
     * <p> Method: User() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
    public User() {
    	
    }

    
    /*****
     * <p> Method: User(String userName, String password, boolean r1, boolean r2,
     * 		boolean r3, boolean r4, boolean r5) </p>
     * 
     * <p> Description: This constructor is used to establish user entity objects. </p>
     * 
     * @param userName specifies the account userName for this user
     * 
     * @param password specifies the account password for this user
     * 
     * @param r1 specifies the the Admin attribute (TRUE or FALSE) for this user
     * 
     * @param r2 specifies the the Student attribute (TRUE or FALSE) for this user
     * 
     * @param r3 specifies the the Reviewer attribute (TRUE or FALSE) for this user
     * 
     */
    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, String fn, String mn, String ln, String pfn, 
    		String ea, boolean r1, boolean r2, boolean r3, String otp, boolean hotp) {
        this.userName = userName;
        this.password = password;
        this.firstName = fn;
        this.middleName = mn;
        this.lastName = ln;
        this.preferredFirstName = pfn;
        this.emailAddress = ea;
        this.adminRole = r1;
        this.studentRole = r2;
        this.staffRole = r3;
        this.oneTimePassword = otp;
        this.hasOneTimePassword = hotp;
    }

    
    /*****
     * <p> Method: void setAdminRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Admin role attribute. </p>
     * 
     * @param role is a boolean that specifies if this user in playing the Admin role.
     * 
     */
    // Sets the role of the Admin user.
    public void setAdminRole(boolean role) {
    	this.adminRole=role;
    }

    
    /*****
     * <p> Method: void setStudentRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the role1 attribute. </p>
     * 
     * @param role is a boolean that specifies if this user in playing role1.
     * 
     */
    // Sets the student user.
    public void setStudentRole(boolean role) {
    	this.studentRole=role;
    }

    
    /*****
     * <p> Method: void setStaffRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the staffRole attribute. </p>
     * 
     * @param role is a boolean that specifies if this user in playing staffRole.
     * 
     */
    // Sets the staffRole user.
    public void setStaffRole(boolean role) {
    	this.staffRole=role;
    }

    
    /*****
     * <p> Method: String getUserName() </p>
     * 
     * <p> Description: This getter returns the UserName. </p>
     * 
     * @return a String of the UserName
     * 
     */
    // Gets the current value of the UserName.
    public String getUserName() { return userName; }

    
    /*****
     * <p> Method: String getPassword() </p>
     * 
     * <p> Description: This getter returns the Password. </p>
     * 
     * @return a String of the password
	 *
     */
    // Gets the current value of the Password.
    public String getPassword() { return password; }

    
    /*****
     * <p> Method: String getFirstName() </p>
     * 
     * <p> Description: This getter returns the FirstName. </p>
     * 
     * @return a String of the FirstName
	 *
     */
    // Gets the current value of the FirstName.
    public String getFirstName() { return firstName; }

    
    /*****
     * <p> Method: String getMiddleName() </p>
     * 
     * <p> Description: This getter returns the MiddleName. </p>
     * 
     * @return a String of the MiddleName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getMiddleName() { return middleName; }

    
    /*****
     * <p> Method: String getLasteName() </p>
     * 
     * <p> Description: This getter returns the LastName. </p>
     * 
     * @return a String of the LastName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getLastName() { return lastName; }

    
    /*****
     * <p> Method: String getPreferredFirstName() </p>
     * 
     * <p> Description: This getter returns the PreferredFirstName. </p>
     * 
     * @return a String of the PreferredFirstName
	 *
     */
    // Gets the current value of the Student role attribute.
    public String getPreferredFirstName() { return preferredFirstName; }

    
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

    public void setUserName(String s) { userName = s; }
    public void setPassword(String s) { password = s; }
    public void setFirstName(String s) { firstName = s; }
    public void setMiddleName(String s) { middleName = s; }
    public void setLastName(String s) { lastName = s; }
    public void setPreferredFirstName(String s) { preferredFirstName = s; }
    public void setEmailAddress(String s) { emailAddress = s; }
    public String getOneTimePassword() { return oneTimePassword; }
    public void setOneTimePassword(String oneTimePassword) { this.oneTimePassword = oneTimePassword; }
    public boolean getHasOneTimePassword() { return hasOneTimePassword; }
    public void setHasOneTimePassword(boolean hasOneTimePassword) { this.hasOneTimePassword = hasOneTimePassword; }

    
    /*****
     * <p> Method: String getAdminRole() </p>
     * 
     * <p> Description: This getter returns the value of the Admin role attribute. </p>
     * 
     * @return a String of "TRUE" or "FALSE" based on state of the attribute
	 *
     */
    // Gets the current value of the Admin role attribute.
    public boolean getAdminRole() { return adminRole; }

    
    /*****
     * <p> Method: String getRole1() </p>
     * 
     * <p> Description: This getter returns the value of the role1 attribute. </p>
     * 
     * @return a String of "TRUE" or "FALSE" based on state of the attribute
	 *
     */
    // Gets the current value of the role1 attribute.
	public boolean getStudentRole() { return studentRole; }

    
    /*****
     * <p> Method: String getStaffRole() </p>
     * 
     * <p> Description: This getter returns the value of the staffRole attribute. </p>
     * 
     * @return a String of "TRUE" or "FALSE" based on state of the attribute
	 *
     */
    // Gets the current value of the staffRole attribute.
    public boolean getStaffRole() { return staffRole; }

        
    /*****
     * <p> Method: int getNumRoles() </p>
     * 
     * <p> Description: This getter returns the number of roles this user plays (0 - 5). </p>
     * 
     * @return a value 0 - 5 of the number of roles this user plays
	 *
     */
    // Gets the current value of the Staff role attribute.
    public int getNumRoles() {
    	int numRoles = 0;
    	if (adminRole) numRoles++;
    	if (studentRole) numRoles++;
    	if (staffRole) numRoles++;
    	return numRoles;
    }
}
