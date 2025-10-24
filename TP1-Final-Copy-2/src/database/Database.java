package database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import entityClasses.User;
import entityClasses.Invite;
import entityClasses.Post;
import entityClasses.Reply;
import guiAdminHome.ViewAdminHome;


/*******
 * <p> Title: Database Class. </p>
 * 
 * <p> Description: This is an in-memory database built on H2.  Detailed documentation of H2 can
 * be found at https://www.h2database.com/html/main.html (Click on "PDF (2MP) for a PDF of 438 pages
 * on the H2 main page.)  This class leverages H2 and provides numerous special supporting methods.
 * </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.00		2025-04-29 Updated and expanded from the version produce by on a previous
 * 							version by Pravalika Mukkiri and Ishwarya Hidkimath Basavaraj
 */

/*
 * The Database class is responsible for establishing and managing the connection to the database,
 * and performing operations such as user registration, login validation, handling invitation 
 * codes, and numerous other database related functions.
 */
public class Database {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	//  Shared variables used within this class
	private Connection connection = null;		// Singleton to access the database 
	private Statement statement = null;			// The H2 Statement is used to construct queries
	
	// These are the easily accessible attributes of the currently logged-in user
	// This is only useful for single user applications
	private String currentUsername;
	private String currentPassword;
	private String currentFirstName;
	private String currentMiddleName;
	private String currentLastName;
	private String currentPreferredFirstName;
	private String currentEmailAddress;
	private boolean currentAdminRole;
	private boolean currentStudentRole;
	private boolean currentStaffRole;
	private String currentOneTimePassword;
	private boolean currentHasOneTimePassword;
	
	private String currentPostUsername;
    private String currentPostText;
    private boolean currentPostAdminRole;
    private boolean currentPostStudentRole;
    private boolean currentPostStaffRole;
    private int currentPostID;
    private String currentPostLikes;
    private String currentPostViews;
    private String currentPostTime;
    private String currentThread;
    
    private String replyUser;
    private String replyText;
    private boolean adminRole;
    private boolean studentRole;
    private boolean staffRole;
    private int likes;
    private int views;
    private String replyTime;
    private int postId;
    
	/*******
	 * <p> Method: Database </p>
	 * 
	 * <p> Description: The default constructor used to establish this singleton object.</p>
	 * 
	 */
	
	public Database () {
		
	}
	
	
/*******
 * <p> Method: connectToDatabase </p>
 * 
 * <p> Description: Used to establish the in-memory instance of the H2 database from secondary
 *		storage.</p>
 *
 * @throws SQLException when the DriverManager is unable to establish a connection
 * 
 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			connection.setAutoCommit(true);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
/*******
 * <p> Method: createTables </p>
 * 
 * <p> Description: Used to create new instances of the two database tables used by this class.</p>
 * 
 */
	private void createTables() throws SQLException {
		// Create the user database
		String userTable = "CREATE TABLE IF NOT EXISTS userDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "firstName VARCHAR(255), "
				+ "middleName VARCHAR(255), "
				+ "lastName VARCHAR (255), "
				+ "preferredFirstName VARCHAR(255), "
				+ "emailAddress VARCHAR(255), "
				+ "adminRole BOOL DEFAULT FALSE, "
				+ "studentRole BOOL DEFAULT FALSE, "
				+ "staffRole BOOL DEFAULT FALSE, "
				+ "oneTimePassword VARCHAR(255), "
				+ "hasOneTimePassword BOOL DEFAULT FALSE)";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	    		+ "emailAddress VARCHAR(255), "
	            + "role VARCHAR(10), "
	    		+ "deadlineString VARCHAR(24))";
	    statement.execute(invitationCodesTable);
	    
	    // Create the post database
	    String postTable = "CREATE TABLE IF NOT EXISTS postDB ("
	        + "id INT AUTO_INCREMENT PRIMARY KEY, "
	        + "mainUser VARCHAR(255), "
	        + "postText VARCHAR(255), "
	        + "adminRole BOOLEAN DEFAULT FALSE, "
	        + "studentRole BOOLEAN DEFAULT FALSE, "
	        + "staffRole BOOLEAN DEFAULT FALSE, "
	        + "postTime VARCHAR(24), "
	        + "likes VARCHAR(2000), "
	        + "views VARCHAR(2000), "
	        + "postID INT DEFAULT 0,"
	        + "thread VARCHAR(30)"
	        + ")";
	    statement.execute(postTable);

	    // Replies table
	    String replyTable = "CREATE TABLE IF NOT EXISTS replyDB ("
	        + "id INT AUTO_INCREMENT PRIMARY KEY, "
	        + "replyUser VARCHAR(255), "
	        + "replyText VARCHAR(255), "
	        + "replyTime VARCHAR(24), "
	        + "likes INT DEFAULT 0, "
	        + "views INT DEFAULT 0, "
	        + "adminRole BOOLEAN DEFAULT FALSE, "
	        + "studentRole BOOLEAN DEFAULT FALSE, "
	        + "staffRole BOOLEAN DEFAULT FALSE, "
	        + "postId INT"
	        + ")";
	    statement.execute(replyTable);
	    
	}


/*******
 * <p> Method: isDatabaseEmpty </p>
 * 
 * <p> Description: If the user database has no rows, true is returned, else false.</p>
 * 
 * @return true if the database is empty, else it returns false
 * 
 */
	public boolean isDatabaseEmpty() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count") == 0;
			}
		}  catch (SQLException e) {
	        return false;
	    }
		return true;
	}
	
	
/*******
 * <p> Method: getNumberOfUsers </p>
 * 
 * <p> Description: Returns an integer .of the number of users currently in the user database. </p>
 * 
 * @return the number of user records in the database.
 * 
 */
	public int getNumberOfUsers() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch (SQLException e) {
	        return 0;
	    }
		return 0;
	}

/*******
 * <p> Method: register(User user) </p>
 * 
 * <p> Description: Creates a new row in the database using the user parameter. </p>
 * 
 * @throws SQLException when there is an issue creating the SQL command or executing it.
 * 
 * @param user specifies a user object to be added to the database.
 * 
 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO userDB (userName, password, firstName, middleName, "
				+ "lastName, preferredFirstName, emailAddress, adminRole, studentRole, staffRole) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			currentUsername = user.getUserName();
			pstmt.setString(1, currentUsername);
			
			currentPassword = user.getPassword();
			pstmt.setString(2, currentPassword);
			
			currentFirstName = user.getFirstName();
			pstmt.setString(3, currentFirstName);
			
			currentMiddleName = user.getMiddleName();			
			pstmt.setString(4, currentMiddleName);
			
			currentLastName = user.getLastName();
			pstmt.setString(5, currentLastName);
			
			currentPreferredFirstName = user.getPreferredFirstName();
			pstmt.setString(6, currentPreferredFirstName);
			
			currentEmailAddress = user.getEmailAddress();
			pstmt.setString(7, currentEmailAddress);
			
			currentAdminRole = user.getAdminRole();
			pstmt.setBoolean(8, currentAdminRole);
			
			currentStudentRole = user.getStudentRole();
			pstmt.setBoolean(9, currentStudentRole);
			
			currentStaffRole = user.getStaffRole();
			pstmt.setBoolean(10, currentStaffRole);
			
			pstmt.executeUpdate();
		}
		
	}
	
	public void register(Post post) throws SQLException {
		String insertPost = "INSERT INTO postDB (mainUser, postText, adminRole, studentRole, "
				+ "staffRole, likes, views, postTime, postID, thread) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
		try (PreparedStatement pstmt = connection.prepareStatement(insertPost)) {
			currentPostUsername = post.getUserName();
			pstmt.setString(1, currentPostUsername);
			
			currentPostText = post.getPostText();
			pstmt.setString(2, currentPostText);
			
			currentPostAdminRole = post.getAdminRole();
			pstmt.setBoolean(3, currentPostAdminRole);
			
			currentPostStudentRole = post.getStudentRole();			
			pstmt.setBoolean(4, currentPostStudentRole);
			
			currentPostStaffRole = post.getStaffRole();
			pstmt.setBoolean(5, currentPostStaffRole);
			
			currentPostLikes = post.getLikes();
			pstmt.setString(6, currentPostLikes);
			
			currentPostViews = post.getViews();
			pstmt.setString(7, currentPostViews);
			
			currentPostTime = post.getPostTime();
			pstmt.setString(8, currentPostTime);
			
			currentPostID = post.getPostID();
			pstmt.setInt(9, currentPostID);
			
			currentThread = post.getThread();
			pstmt.setString(10, currentThread);

			pstmt.executeUpdate();
		}
		
	}
	
	public void register(Reply reply) throws SQLException {
		
		String insertReply = "INSERT INTO replyDB (postId, replyUser, replyText, adminRole, "
				+ "studentRole, staffRole, likes, views, replyTime) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = connection.prepareStatement(insertReply)) {
			
			postId = reply.getPostId();
			pstmt.setInt(1, postId);

			replyUser = reply.getUserName();
			pstmt.setString(2, replyUser);

			replyText = reply.getReplyText();
			pstmt.setString(3, replyText);
			
			adminRole = reply.getAdminRole();
			pstmt.setBoolean(4, adminRole);
			
			studentRole = reply.getStudentRole();
			pstmt.setBoolean(5, studentRole);

			staffRole = reply.getStaffRole();
			pstmt.setBoolean(6, staffRole);
			
			likes = reply.getLikes();
			pstmt.setInt(7, likes);
			
			views = reply.getViews();
			pstmt.setInt(8, views);
			
			replyTime = reply.getReplyTime();
			pstmt.setString(9, replyTime);
			
			pstmt.executeUpdate();
		}
		
	}
	
/*******
 *  <p> Method: List getUserList() </p>
 *  
 *  <P> Description: Generate an List of Strings, one for each user in the database,
 *  starting with "<Select User>" at the start of the list. </p>
 *  
 *  @return a list of userNames found in the database.
 */
	public List<String> getUserList () {
		List<String> userList = new ArrayList<String>();
		userList.add("<Select a User>");
		String query = "SELECT userName FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userList.add(rs.getString("userName"));
			}
		} catch (SQLException e) {
	        return null;
	    }
//		System.out.println(userList);
		return userList;
	}

	/*******
	 * <p> Method: List getAllUsers() </p>
	 * * <P> Description: Generate a List of User objects for every user in the database. </p>
	 * * @return a list of Users found in the database.
	 */
	    public List<User> getAllUsers() {
	        List<User> userList = new ArrayList<>();
	        String query = "SELECT * FROM userDB";
	        try (Statement stmt = connection.createStatement()) {
	            ResultSet rs = stmt.executeQuery(query);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnCount = rsmd.getColumnCount();

	            // The column count starts from 1
	            for (int i = 1; i <= columnCount; i++ ) {
	              String name = rsmd.getColumnName(i);
	              // Do stuff with name
	              System.out.println(name);
	            }
	            
	            while (rs.next()) {
	                User user = new User(
	                    rs.getString("userName"),
	                    rs.getString("password"),
	                    rs.getString("firstName"),
	                    rs.getString("middleName"),
	                    rs.getString("lastName"),
	                    rs.getString("preferredFirstName"),
	                    rs.getString("emailAddress"),
	                    rs.getBoolean("adminRole"),
	                    rs.getBoolean("studentRole"),
	                    rs.getBoolean("staffRole"),
	                    rs.getString("ONETIMEPASSWORD"),
	                    rs.getBoolean("HASONETIMEPASSWORD")
	                );
	                userList.add(user);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	        return userList;
	    }
	
	    public List<String> getPostList () {
			List<String> postList = new ArrayList<String>();
			postList.add("<Select a Post>");
			String query = "SELECT mainUser FROM postDB";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					postList.add(rs.getString("mainUser"));
				}
			} catch (SQLException e) {
		        return null;
		    }
//			System.out.println(userList);
			return postList;
		}
	    
	    public List<Post> getAllPosts() {
	        List<Post> postList = new ArrayList<>();
	        String query = "SELECT * FROM postDB";
	        try (Statement stmt = connection.createStatement()) {
	            ResultSet rs = stmt.executeQuery(query);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnCount = rsmd.getColumnCount();

	            // The column count starts from 1
	            for (int i = 1; i <= columnCount; i++ ) {
	              String name = rsmd.getColumnName(i);
	              // Do stuff with name
	              System.out.println(name);
	            }
	            
	            while (rs.next()) {
	                Post post = new Post(
	                    rs.getString("mainUser"),
	                    rs.getString("postText"),
	                    rs.getBoolean("adminRole"),
	                    rs.getBoolean("studentRole"),
	                    rs.getBoolean("staffRole"),
	                    rs.getString("likes"),
	                    rs.getString("views"),
	                    rs.getString("postTime"),
	                    rs.getInt("postID"),
	                    rs.getString("thread")
	                    );
	                postList.add(post);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	        return postList;
	    }
	    
	    public List<Reply> getRepliesForPost(int postId) throws SQLException {
	        List<Reply> replies = new ArrayList<>();
	        
	        String selectQuery = "SELECT * FROM replyDB WHERE postId = ? ORDER BY replyTime ASC";
	        
	        try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
	            pstmt.setInt(1, postId);
	            
	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    Reply reply = new Reply(
	                    
	                    rs.getInt("postId"),
	                    rs.getString("replyUser"),
	                    rs.getString("replyText"),
	                    rs.getBoolean("adminRole"),
	                    rs.getBoolean("studentRole"),
	                    rs.getBoolean("staffRole"),
	                    rs.getInt("likes"),
	                    rs.getInt("views"),
	                    rs.getString("replyTime")
	                    );
	                    replies.add(reply);
	                }
	            }
	        }
	        
	        return replies;
	    }
	    
	    public ArrayList<String> getLikesToList(Post post){
	    	ArrayList<String> likeList = new ArrayList<String>();
	    	
	    	String likes = post.getLikes();
	    	
	    	String[] items = likes.split(" ");
	    	Collections.addAll(likeList, items);
	    	
	    	if (items.length == 0) likeList.add("");
	    	
	    	return likeList;
	    }
	    
	    public void registerLikes(ArrayList<String> likedBy, Post post) throws SQLException {
	    	System.out.println("likedby size: " + likedBy.size());
	    	System.out.println(likedBy);
	    	String likes = "";
	        for(String user : likedBy) {
	        	likes += user + " ";
	        }
	        String sql = "UPDATE postDB SET likes = ? WHERE postID = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        
	        pstmt.setString(1, likes);
	        pstmt.setInt(2, post.getPostID());
	        System.out.println("Updating");
	        pstmt.executeUpdate();
	    }
	    
	    public ArrayList<String> getViewsToList(Post post){
	    	ArrayList<String> viewList = new ArrayList<String>();
	    	
	    	String views = post.getViews();
	    	
	    	String[] items = views.split(" ");
	    	Collections.addAll(viewList, items);
	    	
	    	if (items.length == 0) viewList.add("");
	    	
	    	return viewList;
	    }
	    
	    public void registerViews(ArrayList<String> viewedBy, Post post) throws SQLException {
	    	String views = "";
	        for(String user : viewedBy) {
	        	views += user + " ";
	        }
	        String sql = "UPDATE postDB SET views = ? WHERE postID = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        
	        pstmt.setString(1, views);
	        pstmt.setInt(2, post.getPostID());
	        System.out.println("Updating");
	        pstmt.executeUpdate();
	    }
	    
	    public void deletePost(Post post) throws SQLException {
			String newPostText = "";
	        
	        String sql = "UPDATE postDB SET postText = ? WHERE postID = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        
	        pstmt.setString(1, newPostText);
	        pstmt.setInt(2, post.getPostID());
	        System.out.println("Updating");
	        pstmt.executeUpdate();
		}
	    
	    
	    public void deleteReply(Reply reply) throws SQLException {
	    	
	        String sql = "UPDATE replyDB SET replyext = ? WHERE postID = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        
	        pstmt.executeUpdate();
		}
	    
/*******
 * <p> Method: boolean loginAdmin(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Admin role.
 * 
 * @return true if the specified user has been logged in as an Admin else false.
 * 
 */
	public boolean loginAdmin(User user){
		// Validates an admin user's login credentials so the user can login in as an Admin.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "adminRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();	// If a row is returned, rs.next() will return true		
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
/*******
 * <p> Method: boolean loginStudent(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Student role.
 * 
 * @return true if the specified user has been logged in as an Student else false.
 * 
 */
	public boolean loginStudent(User user) {
		// Validates a student user's login credentials.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "studentRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}

	/*******
	 * <p> Method: boolean loginStaff(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username, password, and role
	 * 		is the same as a row in the table for the username, password, and role. </p>
	 * 
	 * @param user specifies the specific user that should be logged in playing the Reviewer role.
	 * 
	 * @return true if the specified user has been logged in as an Student else false.
	 * 
	 */
	// Validates a reviewer user's login credentials.
	public boolean loginStaff(User user) {
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "staffRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}
	
	
	/*******
	 * <p> Method: boolean doesUserExist(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username is  in the table. </p>
	 * 
	 * @param userName specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return true if the specified user is in the table else false.
	 * 
	 */
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM userDB WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

	
	/*******
	 * <p> Method: int getNumberOfRoles(User user) </p>
	 * 
	 * <p> Description: Determine the number of roles a specified user plays. </p>
	 * 
	 * @param user specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return the number of roles this user plays (0 - 5).
	 * 
	 */	
	// Get the number of roles that this user plays
	public int getNumberOfRoles (User user) {
		int numberOfRoles = 0;
		if (user.getAdminRole()) numberOfRoles++;
		if (user.getStudentRole()) numberOfRoles++;
		if (user.getStaffRole()) numberOfRoles++;
		return numberOfRoles;
	}	

	
	/*******
	 * <p> Method: String generateInvitationCode(String emailAddress, String role) </p>
	 * 
	 * <p> Description: Given an email address and a roles, this method establishes and invitation
	 * code and adds a record to the InvitationCodes table.  When the invitation code is used, the
	 * stored email address is used to establish the new user and the record is removed from the
	 * table.</p>
	 * 
	 * @param emailAddress specifies the email address for this new user.
	 * 
	 * @param role specified the role that this new user will play.
	 * 
	 * @return the code of six characters so the new user can use it to securely setup an account.
	 * 
	 */
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String emailAddress, String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 6-character code
	    LocalDateTime deadline = LocalDateTime.now().plusDays(1).plusHours(0).plusMinutes(0);   // Generate a deadline 24 hours later
	    String deadlineString = deadline.format(formatter);
	    System.out.println("The deadline is " + deadlineString);
	    String query = "INSERT INTO InvitationCodes (code, emailaddress, role, deadlineString) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, emailAddress);
	        pstmt.setString(3, role);
	        pstmt.setString(4, deadlineString);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return code;
	}
	
	//This code sets up the format for how dates are recorded
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/*******
	 * <p> Method: String getDeadline(String code) </p>
	 * 
	 * <p> Description: Given an invitation code, it will get the deadline that 
	 * was created for the invite.</p>
	 * 
	 * @param code specifies the 6 digit invitation code
	 * 
	 * @return the string format of the deadline for the code
	 * 
	 */
	public String getDeadline(String code) {
		String query = "SELECT deadlineString FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("deadlineString");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	/*******
	 * <p> Method: boolean deleteExpiredCode(String code) </p>
	 * 
	 * <p> Description: Delete an invitation based on the given code, 
	 * if the current time is past the deadline.</p>
	 * 
	 * @param code specifies the 6 digit invitation code
	 * 
	 * @return a boolean value of whether the code was expired true = expired; false = still valid
	 * 
	 */
	
	public boolean deleteExpiredCode(String code) {
		String codeDeadline = getDeadline(code);
		LocalDateTime deadline = LocalDateTime.parse(codeDeadline, formatter);
		//LocalDateTime now = LocalDateTime.of(2025, 10, 30, 12, 00);
		//System.out.println("Now: " + now);
		LocalDateTime now = LocalDateTime.now();
		if(now.isAfter(deadline)) { //not working
			System.out.println("Past Deadline");
			deleteInvitationCode(code);
			return true; //past deadline
		} else {
			System.out.println("Not Past Deadline");
			return false;  //not past deadline
		}
	}
	
	/*******
	 * <p> Method: void findExpiredCodes(String code) </p>
	 * 
	 * <p> Description: Find what codes are expired and delete them </p>
	 * 
	 * @param code specifies the 6 digit invitation code
	 * 
	 * @return a boolean value of whether the code was expired true = expired; false = still valid
	 * 
	 */
	
	public void findExpiredCodes() {
		LocalDateTime pastDue = LocalDateTime.now();
		String now = pastDue.format(formatter);
		String query  = "SELECT CODE from InvitationCodes Where deadlineString < ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, now);
			ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String expiredCode = rs.getString("code");
	            deleteInvitationCode(expiredCode); // Delete each expired code
	        }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*******
	 * <p> Method: String deleteInvitationCode(String code) </p>
	 * 
	 * <p> Description: Delete an invitation based on the given code.</p>
	 * 
	 * @param code specifies the 6 digit invitation code
	 * 
	 * @return the error message as a string
	 * 
	 */
	
	public String deleteInvitationCode(String code) {
		String query = "DELETE FROM InvitationCodes WHERE code= ? ";
		
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, code);
			System.out.println("Code successfully deleted");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Code failed to deleted");
	        e.printStackTrace();
	    }
		
		return "Code was deleted";
	}

	
	/*******
	 * <p> Method: int getNumberOfInvitations() </p>
	 * 
	 * <p> Description: Determine the number of outstanding invitations in the table.</p>
	 *  
	 * @return the number of invitations in the table.
	 * 
	 */
	// Number of invitations in the database
	public int getNumberOfInvitations() {
		String query = "SELECT COUNT(*) AS count FROM InvitationCodes";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return 0;
	}
	
	
	/*******
	 * <p> Method: boolean emailaddressHasBeenUsed(String emailAddress) </p>
	 * 
	 * <p> Description: Determine if an email address has been user to establish a user.</p>
	 * 
	 * @param emailAddress is a string that identifies a user in the table
	 *  
	 * @return true if the email address is in the table, else return false.
	 * 
	 */
	// Check to see if an email address is already in the database
	public boolean emailaddressHasBeenUsed(String emailAddress) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE emailAddress = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println(rs);
	        if (rs.next()) {
	            // Mark the code as used
	        	return rs.getInt("count")>0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
	/*******
	 * <p> Method: String getRoleGivenAnInvitationCode(String code) </p>
	 * 
	 * <p> Description: Get the role associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the role for the code or an empty string.
	 * 
	 */
	// Obtain the roles associated with an invitation code.
	public String getRoleGivenAnInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	
	/*******
	 * <p> Method: String getEmailAddressUsingCode (String code ) </p>
	 * 
	 * <p> Description: Get the email addressed associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the email address for the code or an empty string.
	 * 
	 */
	// For a given invitation code, return the associated email address of an empty string
	public String getEmailAddressUsingCode (String code ) {
	    String query = "SELECT emailAddress FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("emailAddress");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	/*******
	 *  <p> Method: List getInviteList() </p>
	 *  
	 *  <P> Description: Generate an List of Strings, one for each invite in the database,
	 *  starting with "<Select an Invite>" at the start of the list. </p>
	 *  
	 *  @return a list of invite codes and emails found in the database.
	 */
		public List<String> getInviteList () {
			List<String> inviteList = new ArrayList<String>();
			inviteList.add("<Select an Invite>");
			String query = "SELECT code, emailAddress FROM invitationCodes";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String code = rs.getString("code");
					String email = rs.getString("emailAddress");
					inviteList.add(code + " | " + email);
					System.out.println(code + " | " + email);
				}
			} catch (SQLException e) {
		        e.printStackTrace(); 
		        return new ArrayList<>();
		    }
//			System.out.println(userList);
			return inviteList;
		}
	
	/*******
	 * <p> Method: List getAllInvites() </p>
	 * * <P> Description: Generate a List of User objects for every user in the database. </p>
	 * * @return a list of Users found in the database.
	 */
	    public List<Invite> getAllInvites() {
	        List<Invite> inviteList = new ArrayList<>();
	        String query = "SELECT * FROM invitationCodes";
	        try (Statement stmt = connection.createStatement()) {
	            ResultSet rs = stmt.executeQuery(query);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnCount = rsmd.getColumnCount();

	            // The column count starts from 1
	            for (int i = 1; i <= columnCount; i++ ) {
	              String code = rsmd.getColumnName(i);
	              // Do stuff with name
	              System.out.println(code);
	            }
	            
	            while (rs.next()) {
	                Invite invite = new Invite(
	                    rs.getString("code"),
	                    rs.getString("emailAddress"),
	                    rs.getString("role"),
	                    rs.getString("deadlineString")
	                );
	                inviteList.add(invite);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	        return inviteList;
	    }
	
	
	/*******
	 * <p> Method: void removeInvitationAfterUse(String code) </p>
	 * 
	 * <p> Description: Remove an invitation record once it is used.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 */
	// Remove an invitation using an email address once the user account has been setup
	public void removeInvitationAfterUse(String code) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int counter = rs.getInt(1);
	            // Only do the remove if the code is still in the invitation table
	        	if (counter > 0) {
        			query = "DELETE FROM InvitationCodes WHERE code = ?";
	        		try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
	        			pstmt2.setString(1, code);
	        			pstmt2.executeUpdate();
	        		}catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return;
	}
	
	
	/*******
	 * <p> Method: String getFirstName(String username) </p>
	 * 
	 * <p> Description: Get the first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the first name of a user given that user's username 
	 *  
	 */
	// Get the First Name
	public String getFirstName(String username) {
		String query = "SELECT firstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	

	/*******
	 * <p> Method: void updateFirstName(String username, String firstName) </p>
	 * 
	 * <p> Description: Update the first name of a user given that user's username and the new
	 *		first name.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @param firstName is the new first name for the user
	 *  
	 */
	// update the first name
	public void updateFirstName(String username, String firstName) {
	    String query = "UPDATE userDB SET firstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, firstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentFirstName = firstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/*******
	 * <p> Method: String getMiddleName(String username) </p>
	 * 
	 * <p> Description: Get the middle name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the middle name of a user given that user's username 
	 *  
	 */
	// get the middle name
	public String getMiddleName(String username) {
		String query = "SELECT MiddleName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("middleName"); // Return the middle name if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}

	
	/*******
	 * <p> Method: void updateMiddleName(String username, String middleName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param middleName is the new middle name for the user
	 *  
	 */
	// update the middle name
	public void updateMiddleName(String username, String middleName) {
	    String query = "UPDATE userDB SET middleName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, middleName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentMiddleName = middleName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getLastName(String username) </p>
	 * 
	 * <p> Description: Get the last name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the last name of a user given that user's username 
	 *  
	 */
	// get he last name
	public String getLastName(String username) {
		String query = "SELECT LastName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("lastName"); // Return last name role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateLastName(String username, String lastName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param lastName is the new last name for the user
	 *  
	 */
	// update the last name
	public void updateLastName(String username, String lastName) {
	    String query = "UPDATE userDB SET lastName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, lastName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentLastName = lastName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getPreferredFirstName(String username) </p>
	 * 
	 * <p> Description: Get the preferred first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the preferred first name of a user given that user's username 
	 *  
	 */
	// get the preferred first name
	public String getPreferredFirstName(String username) {
		String query = "SELECT preferredFirstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the preferred first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updatePreferredFirstName(String username, String preferredFirstName) </p>
	 * 
	 * <p> Description: Update the preferred first name of a user given that user's username and
	 * 		the new preferred first name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param preferredFirstName is the new preferred first name for the user
	 *  
	 */
	// update the preferred first name of the user
	public void updatePreferredFirstName(String username, String preferredFirstName) {
	    String query = "UPDATE userDB SET preferredFirstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, preferredFirstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPreferredFirstName = preferredFirstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getEmailAddress(String username) </p>
	 * 
	 * <p> Description: Get the email address of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the email address of a user given that user's username 
	 *  
	 */
	// get the email address
	public String getEmailAddress(String username) {
		String query = "SELECT emailAddress FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("emailAddress"); // Return the email address if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateEmailAddress(String username, String emailAddress) </p>
	 * 
	 * <p> Description: Update the email address name of a user given that user's username and
	 * 		the new email address.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param emailAddress is the new preferred first name for the user
	 *  
	 */
	// update the email address
	public void updateEmailAddress(String username, String emailAddress) {
	    String query = "UPDATE userDB SET emailAddress = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentEmailAddress = emailAddress;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: boolean getUserAccountDetails(String username) </p>
	 * 
	 * <p> Description: Get all the attributes of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return true of the get is successful, else false
	 *  
	 */
	// get the attributes for a specified user
	public boolean getUserAccountDetails(String username) {
		String query = "SELECT * FROM userDB WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();			
			rs.next();
	    	currentUsername = rs.getString(2);
	    	currentPassword = rs.getString(3);
	    	currentFirstName = rs.getString(4);
	    	currentMiddleName = rs.getString(5);
	    	currentLastName = rs.getString(6);
	    	currentPreferredFirstName = rs.getString(7);
	    	currentEmailAddress = rs.getString(8);
	    	currentAdminRole = rs.getBoolean(9);
	    	currentStudentRole = rs.getBoolean(10);
	    	currentStaffRole = rs.getBoolean(11);
			return true;
	    } catch (SQLException e) {
			return false;
	    }
	}
	
	
	/*******
	 * <p> Method: boolean updateUserRole(String username, String role, String value) </p>
	 * 
	 * <p> Description: Update a specified role for a specified user's and set and update all the
	 * 		current user attributes.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param role is string that specifies the role to update
	 * 
	 * @param value is the string that specified TRUE or FALSE for the role
	 * 
	 * @return true if the update was successful, else false
	 *  
	 */
	// Update a users role
	public boolean updateUserRole(String username, String role, String value) {
		if (role.compareTo("Admin") == 0) {
			String query = "UPDATE userDB SET adminRole = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentAdminRole = true;
				else
					currentAdminRole = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Student") == 0) {
			String query = "UPDATE userDB SET studentRole = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentStudentRole = true;
				else
					currentStudentRole = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Staff") == 0) {
			String query = "UPDATE userDB SET staffRole = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, value);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentStaffRole = true;
				else
					currentStaffRole = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}
	
	
	// Attribute getters for the current user
	/*******
	 * <p> Method: String getCurrentUsername() </p>
	 * 
	 * <p> Description: Get the current user's username.</p>
	 * 
	 * @return the username value is returned
	 *  
	 */
	public String getCurrentUsername() { return currentUsername;};

	
	/*******
	 * <p> Method: String getCurrentPassword() </p>
	 * 
	 * <p> Description: Get the current user's password.</p>
	 * 
	 * @return the password value is returned
	 *  
	 */
	public String getCurrentPassword() { return currentPassword;};

	
	/*******
	 * <p> Method: String getCurrentFirstName() </p>
	 * 
	 * <p> Description: Get the current user's first name.</p>
	 * 
	 * @return the first name value is returned
	 *  
	 */
	public String getCurrentFirstName() { return currentFirstName;};

	
	/*******
	 * <p> Method: String getCurrentMiddleName() </p>
	 * 
	 * <p> Description: Get the current user's middle name.</p>
	 * 
	 * @return the middle name value is returned
	 *  
	 */
	public String getCurrentMiddleName() { return currentMiddleName;};

	
	/*******
	 * <p> Method: String getCurrentLastName() </p>
	 * 
	 * <p> Description: Get the current user's last name.</p>
	 * 
	 * @return the last name value is returned
	 *  
	 */
	public String getCurrentLastName() { return currentLastName;};

	
	/*******
	 * <p> Method: String getCurrentPreferredFirstName( </p>
	 * 
	 * <p> Description: Get the current user's preferred first name.</p>
	 * 
	 * @return the preferred first name value is returned
	 *  
	 */
	public String getCurrentPreferredFirstName() { return currentPreferredFirstName;};

	
	/*******
	 * <p> Method: String getCurrentEmailAddress() </p>
	 * 
	 * <p> Description: Get the current user's email address name.</p>
	 * 
	 * @return the email address value is returned
	 *  
	 */
	public String getCurrentEmailAddress() { return currentEmailAddress;};

	
	/*******
	 * <p> Method: boolean getCurrentAdminRole() </p>
	 * 
	 * <p> Description: Get the current user's Admin role attribute.</p>
	 * 
	 * @return true if this user plays an Admin role, else false
	 *  
	 */
	public boolean getCurrentAdminRole() { return currentAdminRole;};

	
	/*******
	 * <p> Method: boolean getCurrentStudentRole() </p>
	 * 
	 * <p> Description: Get the current user's Student role attribute.</p>
	 * 
	 * @return true if this user plays a Student role, else false
	 *  
	 */
	public boolean getCurrentStudentRole() { return currentStudentRole;};

	
	/*******
	 * <p> Method: boolean getCurrentStaffRole() </p>
	 * 
	 * <p> Description: Get the current user's Reviewer role attribute.</p>
	 * 
	 * @return true if this user plays a Reviewer role, else false
	 *  
	 */
	public boolean getCurrentStaffRole() { return currentStaffRole;};

	/*******
	 * <p> Method: boolean getCurrentHasOneTimePassword() </p>
	 * 
	 * <p> Description: Get the current user's has one time password attribute.</p>
	 * 
	 * @return true if this user has a one time password, else false
	 *  
	 */
	public boolean getCurrentHasOneTimePassword() { return currentHasOneTimePassword;};

	/*******
	 * <p> Method: void setonetimepassword() </p>
	 * 
	 * <p> Description: Sets or replaces a one time password for the user that is passed in and marks flag.</p>
	 * 
	 * @return is nothing, its void
	 *  
	 */
	//new code just implemeneted make comments
	public void setOneTimePassword(String username, String oneTimePassword) {
	    String query = "UPDATE userDB SET oneTimePassword = ?, hasOneTimePassword = TRUE WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, oneTimePassword);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentOneTimePassword = oneTimePassword;
	        currentHasOneTimePassword = true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	/*******
	 * <p> Method: String getOneTimePassword() </p>
	 * 
	 * <p> Description: goes to database and asks for one time password only if hasonetimepw is true.</p>
	 * 
	 * @return nothing because it is void
	 *  
	 */
	public String getOneTimePassword(String username) {
	    String query = "SELECT oneTimePassword FROM userDB WHERE username = ? AND hasOneTimePassword = TRUE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("oneTimePassword");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	/*******
	 * <p> Method: void clearOneTimePassword() </p>
	 * 
	 * <p> Description: Clears out one time password.</p>
	 * 
	 * @return nothing because it is void
	 *  
	 */
	public void clearOneTimePassword(String username) {
	    String query = "UPDATE userDB SET oneTimePassword = NULL, hasOneTimePassword = FALSE WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        pstmt.executeUpdate();
	        currentOneTimePassword = null;
	        currentHasOneTimePassword = false;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	/*******
	 * <p> Method: void updatePassword() </p>
	 * 
	 * <p> Description: Updates user password after using one time pw.</p>
	 * 
	 * @return true if this user has a one time password, else false
	 *  
	 */
	public void updatePassword(String username, String newPassword) {
	    String query = "UPDATE userDB SET password = ?, oneTimePassword = NULL, hasOneTimePassword = FALSE WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPassword = newPassword;
	        currentOneTimePassword = null;
	        currentHasOneTimePassword = false;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	//getter methods
	public String getCurrentOneTimePassword() { return currentOneTimePassword; }
	/*******
	 * <p> Debugging method</p>
	 * 
	 * <p> Description: Debugging method that dumps the database of the console.</p>
	 * 
	 * @throws SQLException if there is an issues accessing the database.
	 * 
	 */
	// Dumps the database.
	public void dump() throws SQLException {
		String query = "SELECT * FROM userDB";
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData meta = resultSet.getMetaData();
		while (resultSet.next()) {
		for (int i = 0; i < meta.getColumnCount(); i++) {
		System.out.println(
		meta.getColumnLabel(i + 1) + ": " +
				resultSet.getString(i + 1));
		}
		System.out.println();
		}
		resultSet.close();
	}


	/*******
	 * <p> Method: void closeConnection()</p>
	 * 
	 * <p> Description: Closes the database statement and connection.</p>
	 * 
	 */
	// Closes the database statement and connection.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	/*******
	 * <p> Method: boolean deleteAccount(String user)</p>
	 * 
	 * <p> Description: deletes the account with Username, user, if account exists.</p>
	 * 
	 */
	// Deletes accounts.
	public boolean deleteAccount(String username) {
		
		String query = "DELETE FROM userDB WHERE userName = ?";
		
		try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
			pstmt.setString(1, username);

	        int rowsDeleted = pstmt.executeUpdate();

	        return rowsDeleted > 0;
		} 
		catch  (SQLException e) {
	        e.printStackTrace();
		}
		return false;
	}
}
