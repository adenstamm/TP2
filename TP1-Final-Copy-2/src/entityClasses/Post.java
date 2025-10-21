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

public class Post {
	/*
	 * These are the private attributes for this entity object
	 */
	
	private String mainUser;
    private String postText;
    private boolean adminRole;
    private boolean studentRole;
    private boolean staffRole;
    private String likes;
    private int views;
    private String postTime;
    private int postID;
    private String thread;
    
    /*****
     * <p> Method: Post(String mainUser, String postText, boolean adminRole, boolean studentRole, boolean staffRole, String likes, int views,
     * 	 String postTime, int postID, String thread) </p>
     * 
     * <p> Description: This constructor is used to establish post entity objects. </p>
     * 
     * @param mainUser specifies the author of the post
     * 
     * @param postText holds the text for the post
     * 
     * @param adminRole specifies the the Admin attribute (TRUE or FALSE) for this author
     * 
     * @param studentRole specifies the the Student attribute (TRUE or FALSE) for this author
     * 
     * @param staffRole specifies the the Staff attribute (TRUE or FALSE) for this author
     * 
     */
    // Constructor to initialize a new Reply object with replyUser, replyText, and role.
    
    public Post(String mainUser, String postText, boolean adminRole, boolean studentRole, boolean staffRole, String likes, int views, String postTime, int postID, String thread) {
    	this.mainUser = mainUser;
    	this.postText = postText;
    	this.adminRole = adminRole;
    	this.studentRole = studentRole;
    	this.staffRole = staffRole;
    	this.likes = likes;
    	this.views = views;
    	this.postTime = postTime;
    	this.postID = postID;
    	this.thread = thread;
    }
    
   
    /*****
     * <p> Method: void setPostText(String postText) </p>
     * 
     * <p> Description: This setter defines the Post text attribute. </p>
     * 
     * @param postText is a String that defines what the substance of the post is.
     * 
     */
    
    public void setPostText(String postText) {
    	this.postText = postText;
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
    
    public void setLikes(String likes) {
    	this.likes = likes;
    }
    
    /*****
     * <p> Method: void setViews(String views) </p>
     * 
     * <p> Description: This setter defines the Views attribute. </p>
     * 
     * @param views is a String that defines who has viewed/read a given post.
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
    
    public void setPostTime(String postTime) {
    	this.postTime = postTime;
    }
    
    /*****
     * <p> Method: void setPostID(int postID) </p>
     * 
     * <p> Description: This setter defines the PostID attribute. </p>
     * 
     * @param postID indicates which post this is.
     * 
     */
    
    public void setPostID(int postID) {
    	this.postID = postID;
    }
    
    /*****
     * <p> Method: void setThread(String thread) </p>
     * 
     * <p> Description: This setter defines the Thread attribute. </p>
     * 
     * @param thread is a String that defines what thread the post is a part of.
     * 
     */
    
    public void setThread(String thread) {
    	this.thread = thread;
    }
    
    /*****
     * <p> Method: String getUserName() </p>
     * 
     * <p> Description: This getter returns the UserName. </p>
     * 
     * @return a String of the UserName
	 *
     */
    // Gets the current value of the UserName attribute.
    
    public String getUserName() { return mainUser; }
    
    /*****
     * <p> Method: String getLikes() </p>
     * 
     * <p> Description: This getter returns the likes. </p>
     * 
     * @return a String of the likes
	 *
     */
    // Gets the current value of the Likes attribute.
    
    public String getLikes() { return likes; }
    
    /*****
     * <p> Method: String getViews() </p>
     * 
     * <p> Description: This getter returns the views. </p>
     * 
     * @return a String of the views
	 *
     */
    // Gets the current value of the Views attribute.
    
    public int getViews() { return views; }
    
    /*****
     * <p> Method: String getPostText() </p>
     * 
     * <p> Description: This getter returns the PostText. </p>
     * 
     * @return a String of the PostText
	 *
     */
    // Gets the current value of the PostText attribute.
    
    public String getPostText() { return postText; }
    
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
     * <p> Method: String getPostTime() </p>
     * 
     * <p> Description: This getter returns the PostTime. </p>
     * 
     * @return a String of the PostTime
	 *
     */
    // Gets the current value of the PostTime attribute.
    
    public String getPostTime() { return postTime; }
    
    /*****
     * <p> Method: int getPostID() </p>
     * 
     * <p> Description: This getter returns the PostID. </p>
     * 
     * @return an int of the PostID
	 *
     */
    // Gets the current value of the PostID attribute.
    
    public int getPostID() { return postID; }
    
    /*****
     * <p> Method: String getThread() </p>
     * 
     * <p> Description: This getter returns the Thread. </p>
     * 
     * @return a String of the Thread
	 *
     */
    // Gets the current value of the Thread attribute.
    
    public String getThread() { return thread; }
    
}



