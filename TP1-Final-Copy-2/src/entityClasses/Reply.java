package entityClasses;

/*******
 * <p> Title: Reply Class </p>
 * 
 * <p> Description: This Reply class represents a Reply in the system.  It contains the Reply's
 *  details such as code, user, roles, and time of reply. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * 
 */ 

public class Reply {
	
	/*
	 * These are the private attributes for this entity object
	 */
	
	private String replyUser;
    private String replyText;
    private boolean adminRole;
    private boolean studentRole;
    private boolean staffRole;
    private int likes;
    private int views;
    private String replyTime;
    private int postId;
    
    /*****
     * <p> Method: Reply(int postId, String replyUser, String replyText, boolean adminRole, boolean studentRole,
     *  	boolean staffRole, int likes, int views, String replyTime) </p>
     * 
     * <p> Description: This constructor is used to establish reply entity objects. </p>
     * 
     * @param replyUser specifies the author of the reply
     * 
     * @param replyText holds the text for the reply
     * 
     * @param adminRole specifies the the Admin attribute (TRUE or FALSE) for this author
     * 
     * @param studentRole specifies the the Student attribute (TRUE or FALSE) for this author
     * 
     * @param staffRole specifies the the Staff attribute (TRUE or FALSE) for this author
     * 
     */
    // Constructor to initialize a new Reply object with replyUser, replyText, and role.
    
    public Reply(int postId, String replyUser, String replyText, boolean adminRole, boolean studentRole, boolean staffRole, int likes, int views, String replyTime) {
    	
    	this.postId = postId;
    	this.replyUser = replyUser;
    	this.replyText = replyText;
    	this.adminRole = adminRole;
    	this.studentRole = studentRole;
    	this.staffRole = staffRole;
    	this.likes = likes;
    	this.views = views;
    	this.replyTime = replyTime;
    }
    
    
    /*****
     * <p> Method: void setPostID(int postID) </p>
     * 
     * <p> Description: This setter defines the PostID attribute. </p>
     * 
     * @param postID indicates which post this is.
     * 
     */
    
    public void setPostId(int postId) {
    	this.postId = postId;
    }
    
    /*****
     * <p> Method: void setReplyUser(String replyUser) </p>
     * 
     * <p> Description: This setter defines the Reply User attribute. </p>
     * 
     * @param replyUser indicates who posted this reply.
     * 
     */
    
    public void setReplyUser(String replyUser) {
    	this.replyUser = replyUser;
    }
    
    /*****
     * <p> Method: void setReplyText(String replyText) </p>
     * 
     * <p> Description: This setter defines the Reply text attribute. </p>
     * 
     * @param postText is a String that defines what the substance of the reply is.
     * 
     */
    
    public void setReplyText(String replyText) {
    	this.replyText = replyText;
    }
    
    /*****
     * <p> Method: void setAdminRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Admin role attribute. </p>
     * 
     * @param role is a boolean that specifies if this post was made by an admin.
     * 
     */
    
    public void setAdminRole(boolean adminRole) {
    	this.adminRole = adminRole;
    }
    
    /*****
     * <p> Method: void setStudentRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Student role attribute. </p>
     * 
     * @param role is a boolean that specifies if this post was made by an student.
     * 
     */
    
    public void setStudentRole(boolean studentRole) {
    	this.studentRole = studentRole;
    }
    
    /*****
     * <p> Method: void setStaffRole(boolean role) </p>
     * 
     * <p> Description: This setter defines the Staff role attribute. </p>
     * 
     * @param role is a boolean that specifies if this post was made by an staff.
     * 
     */
    
    public void setStaffRole(boolean staffRole) {
    	this.staffRole = staffRole;
    }
    
    /*****
     * <p> Method: void setLikes(String likes) </p>
     * 
     * <p> Description: This setter defines the Likes attribute. </p>
     * 
     * @param likes is a String that defines who has liked this post.
     * 
     */
    
    public void setLikes(int likes) {
    	this.likes = likes;
    }
    
    /*****
     * <p> Method: void setViews(String views) </p>
     * 
     * <p> Description: This setter defines the Views attribute. </p>
     * 
     * @param views is a String that defines who has viewed/read a given reply.
     * 
     */
    
    public void setViews(int views) {
    	this.views = views;
    }
    
    /*****
     * <p> Method: void setPostTime(String postTime) </p>
     * 
     * <p> Description: This setter defines the Post Time attribute. </p>
     * 
     * @param postTime is a String that defines what time a post was made.
     * 
     */
    
    public void setReplyTime(String replyTime) {
    	this.replyTime = replyTime;
    }
    
    
    
    
    /*****
     * <p> Method: int getPostID() </p>
     * 
     * <p> Description: This getter returns the PostID. </p>
     * 
     * @return an int of the PostID
	 *
     */
    // Gets the current value of the PostID attribute.
    
    public int getPostId() { return postId; }
    
    /*****
     * <p> Method: String getUserName() </p>
     * 
     * <p> Description: This getter returns the UserName. </p>
     * 
     * @return a String of the UserName
	 *
     */
    // Gets the current value of the UserName attribute.
    
    public String getUserName() { return replyUser; }
    
    /*****
     * <p> Method: String getLikes() </p>
     * 
     * <p> Description: This getter returns the likes. </p>
     * 
     * @return a String of the likes
	 *
     */
    // Gets the current value of the Likes attribute.
    
    public int getLikes() { return likes; }
    
    /*****
     * <p> Method: String getPostText() </p>
     * 
     * <p> Description: This getter returns the PostText. </p>
     * 
     * @return a String of the PostText
	 *
     */
    // Gets the current value of the PostText attribute.
    
    public int getViews() { return views; }
    
    /*****
     * <p> Method: String getReplyext() </p>
     * 
     * <p> Description: This getter returns the ReplyText. </p>
     * 
     * @return a String of the ReplyText
	 *
     */
    // Gets the current value of the ReplyText attribute.
    
    public String getReplyText() { return replyText; }
    
    /*****
     * <p> Method: boolean getAdminRole() </p>
     * 
     * <p> Description: This getter returns the Admin role. </p>
     * 
     * @return a Boolean of the Admin
	 *
     */
    // Gets the current value of the Admin role attribute.
    
    public boolean getAdminRole() { return adminRole; }
    
    /*****
     * <p> Method: boolean getStaffRole() </p>
     * 
     * <p> Description: This getter returns the Staff role. </p>
     * 
     * @return a Boolean of the Staff
	 *
     */
    // Gets the current value of the Staff role attribute.
    
    public boolean getStaffRole() { return staffRole; }
    
    /*****
     * <p> Method: boolean getStudentRole() </p>
     * 
     * <p> Description: This getter returns the Student role. </p>
     * 
     * @return a Boolean of the Student
	 *
     */
    // Gets the current value of the Student role attribute.
    
    public boolean getStudentRole() { return studentRole; }
    
    /*****
     * <p> Method: String getReplyTime() </p>
     * 
     * <p> Description: This getter returns the ReplyTime. </p>
     * 
     * @return a String of the ReplyTime
	 *
     */
    // Gets the current value of the ReplyTime attribute.
    
    public String getReplyTime() { return replyTime; }
    
}



