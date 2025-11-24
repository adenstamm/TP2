package entityClasses;

/*******
 * <p> Title: Post Class </p>
 * 
 * <p> Description: This Post class represents a Post in the system.  It contains the Post's
 *  details such as code, user, roles, and time of post. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Ian Johnson
 * 
 * 
 */ 

public class StudentNote {

	/**specifies the author of the post*/
	private String student;
	
	private String staff;
	/**Holds the text for the post*/
    private String note;
    /**Specifies whether the user has deleted this post, is kept so that the replies are still tied to a post*/
    private boolean softDelete;

    
    /*****
     * <p> Method: Post(String mainUser, String postText, boolean adminRole, boolean studentRole, boolean staffRole, String likes, int views,
     * 	 String postTime, int postID, String thread) </p>
     * 
     * <p> Description: This constructor is used to establish post entity objects. These post entities will be stored in the database and
     *     used in the guiDiscussion package. The purpose of this object is to contain post text that is written by a user and seen by other
     *     users. It contains the person who wrote the post, the text inside, the roles that the user has, the people who have liked the post,
     *     the people who have viewed/read the post, the time the post was made, the ID, the thread the post is associated with, the post's
     *     tags, and whether or not this post has been deleted. </p>
     * 
     */
    // Constructor to initialize a new Reply object with replyUser, replyText, and role.
    
    public StudentNote(String student, String staff, String note, boolean softDelete) {
    	this.student = student;
    	this.staff = staff;
    	this.note = note;
    	this.softDelete = softDelete;
    	
    }
    
   
    /*****
     * <p> Method: void setPostText(String postText) </p>
     * 
     * <p> Description: This setter defines the Post text attribute. </p>
     * 
     * @param postText is a String that defines what the substance of the post is.
     * 
     */
    
    public void setStudent(String student) {
    	this.student = student;
    }
    
    /*****
     * <p> Method: void setAdminRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Admin role attribute. </p>
     * 
     * @param adminRole is a boolean that specifies if this post was made by an admin.
     * 
     */
    
    public void setStaff(String staff) {
    	this.staff = staff;
    }
    
    /*****
     * <p> Method: void setStudentRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Student role attribute. </p>
     * 
     * @param studentRole is a boolean that specifies if this post was made by an student.
     * 
     */
    
    public void setNote(String note) {
    	this.note = note;
    }
    
    /*****
     * <p> Method: void setStaffRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Staff role attribute. </p>
     * 
     * @param staffRole is a boolean that specifies if this post was made by an staff.
     * 
     */
    
    public void setSoftDelete(boolean softDelete) {
    	this.softDelete = softDelete;
    }
    
    
    public String getStudent() { return student; }
    
    /*****
     * <p> Method: String getLikes() </p>
     * 
     * <p> Description: This getter returns the likes. </p>
     * 
     * @return a String of the likes
	 *
     */
    // Gets the current value of the Likes attribute.
    
    public String getStaff() { return staff; }
    
    /*****
     * <p> Method: String getViews() </p>
     * 
     * <p> Description: This getter returns the views. </p>
     * 
     * @return a String of the views
	 *
     */
    // Gets the current value of the Views attribute.
    
    public String getNote() { return note; }
    
    /*****
     * <p> Method: String getPostText() </p>
     * 
     * <p> Description: This getter returns the PostText. </p>
     * 
     * @return a String of the PostText
	 *
     */
    // Gets the current value of the PostText attribute.
    
    public boolean getSoftDelete() { return softDelete; }
    
}