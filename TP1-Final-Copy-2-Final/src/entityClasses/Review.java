package entityClasses;

/*******
 * <p> Title: Review Class </p>
 * 
 * <p> Description: This Review class represents a Reply in the system.  It contains the Reply's
 *  details such as code, user, roles, and time of reply. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * 
 */ 

public class Review {
	
	
	/**Specifies the author of the review*/
	private String reviewStaff;
	/**Specifies the person being reviewed*/
	private String reviewStudent;
	/**Holds the text for the review*/
    private String reviewText;
    /**Specifies the time that a review has been made*/
    private String reviewTime;
    /**Specifies the post that the review is attached to*/
    private int postId;
    
    /*****
     * <p> Method:Review(int postId, String reviewStaff, String reviewStudent, String reviewText, String reviewTime) </p>
     * 
     * <p> Description: This constructor is used to establish reply entity objects. </p>
     * 
     * @param postId specifies the post that the review is tied to
     * 
     * @param reviewStaff specifies the author of the review
     * 
     * @param reviewStudent specifies who is being reviewed
     * 
     * @param reviewText holds the text for the review
     * 
     * @param reviewTime specifies the time that this reply was created
     * 
     */
    // Constructor to initialize a new Reply object with replyUser, replyText, and role.
    
    public Review(int postId, String reviewStaff, String reviewStudent, String reviewText, String reviewTime) {
    	
    	this.postId = postId;
    	this.reviewStaff = reviewStaff;
    	this.reviewStudent = reviewStudent;
    	this.reviewText = reviewText;
    	this.reviewTime = reviewTime;
    }
    
    
    /*****
     * <p> Method: void setPostId(int postId) </p>
     * 
     * <p> Description: This setter defines the PostID attribute. </p>
     * 
     * @param postId indicates which post this is.
     * 
     */
    
    public void setPostId(int postId) {
    	this.postId = postId;
    }
    
    /*****
     * <p> Method: void setReviewStaff(String reviewStaff) </p>
     * 
     * <p> Description: This setter defines the Review Staff attribute. </p>
     * 
     * @param reviewStaff indicates who posted this review.
     * 
     */
    
    public void setReviewStaff(String reviewStaff) {
    	this.reviewStaff = reviewStaff;
    }
    
    /*****
     * <p> Method: void setReviewStudent(String reviewStudent) </p>
     * 
     * <p> Description: This setter defines the Review Student attribute. </p>
     * 
     * @param reviewStudent indicates who this review is for
     * 
     */
    
    public void setReviewStudent(String reviewStudent) {
    	this.reviewStudent = reviewStudent;
    }
    
    /*****
     * <p> Method: void setReviewText(String reviewText) </p>
     * 
     * <p> Description: This setter defines the Review text attribute. </p>
     * 
     * @param reviewText is a String that defines what the substance of the review is.
     * 
     */
    
    public void setReviewText(String reviewText) {
    	this.reviewText = reviewText;
    }
    
   
    /*****
     * <p> Method: void setReviewTime(String reviewTime) </p>
     * 
     * <p> Description: This setter defines the Review Time attribute. </p>
     * 
     * @param reviewTime is a String that defines what time a review was made.
     * 
     */
    
    public void setReviewTime(String reviewTime) {
    	this.reviewTime = reviewTime;
    }
    
    
    
    
    /*****
     * <p> Method: int getPostId() </p>
     * 
     * <p> Description: This getter returns the PostId. </p>
     * 
     * @return an int of the PostId
	 *
     */
    // Gets the current value of the PostID attribute.
    
    public int getPostId() { return postId; }
    
    /*****
     * <p> Method: String getStaffName() </p>
     * 
     * <p> Description: This getter returns the UserName of the staff member who created it. </p>
     * 
     * @return a String of the UserName of the staff member
	 *
     */
    // Gets the current value of the UserName attribute.
    
    public String getStaffName() { return reviewStaff; }
    
    /*****
     * <p> Method: String getStudentName() </p>
     * 
     * <p> Description: This getter returns the UserName of the student 
     * it is for. </p>
     * 
     * @return a String of the UserName of the student member
	 *
     */
    // Gets the current value of the UserName attribute.
    
    public String getStudentName() { return reviewStudent; }
   
    
    /*****
     * <p> Method: String getReviewText() </p>
     * 
     * <p> Description: This getter returns the reviewText. </p>
     * 
     * @return a String of the reviewText
	 *
     */
    // Gets the current value of the ReplyText attribute.
    
    public String getReviewText() { return reviewText; }
    
    
    
    /*****
     * <p> Method: String getReviewTime() </p>
     * 
     * <p> Description: This getter returns the reviewTime. </p>
     * 
     * @return a String of the reviewTime
	 *
     */
    // Gets the current value of the ReplyTime attribute.
    
    public String getReviewTime() { return reviewTime; }
    
}