package guiDiscussion;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import database.Database;
//import database.Database;
import entityClasses.User;
import entityClasses.Post;
import entityClasses.Reply;

/*******
 * <p> Title: ViewRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role2 Home Page.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-04-20 Initial version
 *  
 */

public class ViewDiscussion {
	
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Label label_CreatePost = new Label();
	private static TextArea text_PostText = new TextArea();
	protected static Button button_Post = new Button("Post");
	protected static VBox postContainer = new VBox(10);
	
	// UI Elements for search
	private static TextField text_SearchTags = new TextField();
	private static Button button_Search = new Button("Search");
	private static Button button_ClearSearch = new Button("Clear");
	
	private static TextField text_PostTags = new TextField();
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	protected static Button button_YourPosts = new Button("Your Posts");
	protected static Button button_UnreadPosts = new Button("Unread Posts");
	
	// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
	// would contain the widgets needed for the user to play the assigned role.
	
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_BackToHome = new Button("Back To Home");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewDiscussion theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	
	private static Scene theDiscussion;		// The shared Scene each invocation populates
	

	/*-*******************************************************************************************

	Constructors
	
	 */

	/**********
	 * <p> Method: displayDiscussion(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Discussion page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayDiscussion(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewDiscussion();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		
		label_UserDetails.setText("User: " + theUser.getUserName());// Set the username

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Discussion Page");
		theStage.setScene(theDiscussion);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewRole2Home() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayRole2Home method.</p>
	 * 
	 */
	private ViewDiscussion() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theDiscussion = new Scene(theRootPane, width, height);	// Create the scene
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Discussion Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		buildPostContainer(null);
        
        
        ScrollPane scrollPane = new ScrollPane(postContainer);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(90);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(410);
 	    scrollPane.setPannable(true);

		// GUI Area 2
		setupButtonUI(button_YourPosts, "Dialog", 18, 250, Pos.CENTER, 500, 70);
        button_YourPosts.setOnAction((event) -> {enterUserPosts();});
        
        setupButtonUI(button_UnreadPosts, "Dialog", 18, 250, Pos.CENTER, 100, 70);
        button_UnreadPosts.setOnAction((event) -> {enterUnreadPosts();});

		// GUI Area 3
        setupButtonUI(button_BackToHome, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_BackToHome.setOnAction((event) -> {ControllerDiscussion.goToUserHomePage(theStage, theUser);});
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerDiscussion.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
        
        
        
        theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
	        line_Separator4, button_BackToHome, button_Quit, scrollPane);
	}
	
	protected static void buildPostContainer(String searchTag) {


		if (!button_YourPosts.isVisible()) {
			enterUserPosts();
			return;
		}
		else if (!button_UnreadPosts.isVisible()) {
			enterUnreadPosts();
			return;
		}
		
		// --- Create Search Box ---
		HBox searchBox = new HBox(10);
		searchBox.setPadding(new Insets(5));
		searchBox.setAlignment(Pos.CENTER_LEFT);
		text_SearchTags.setPromptText("Search by tag...");
		if (searchTag != null) {
			text_SearchTags.setText(searchTag);
		}
		
		button_Search.setOnAction((event) -> { 
			String tag = text_SearchTags.getText();
			postContainer.getChildren().clear();
			buildPostContainer(tag);
		});
		
		button_ClearSearch.setOnAction((event) -> {
			text_SearchTags.clear();
			postContainer.getChildren().clear();
			buildPostContainer(null);
		});
		
		searchBox.getChildren().addAll(new Label("Search Tags:"), text_SearchTags, button_Search, button_ClearSearch);
		
		// --- Create "Create Post" Box ---
		Label label_CreatePost = new Label("Create a Post:");
		label_CreatePost.setFont(new Font("Arial", 20));
		
		TextArea text_PostText = new TextArea();
		text_PostText.setPromptText("Enter your Post");
		text_PostText.setPrefRowCount(3);
		text_PostText.setWrapText(true);
		text_PostText.setMaxWidth(width);
		
		// Add the new Tags field
		text_PostTags.setPromptText("Enter tags, separated by commas (e.g., java, sql, fxml)");
		text_PostTags.setMaxWidth(width);
		
		postContainer.setSpacing(10);
		
		Button button_Post = new Button("Post");
        button_Post.setOnAction((event) -> {
        	// Pass the tags text to storePost
        	entityClasses.ManagePost.storePost(theUser, text_PostText.getText(), "General", text_PostTags.getText()); 
        	text_PostText.clear();	// Clear fields after posting
        	text_PostTags.clear();
            postContainer.getChildren().clear(); 
            buildPostContainer(null); // Refresh with no search
        });
        
        // Add all elements to the container
        postContainer.getChildren().addAll(searchBox, new Separator(), label_CreatePost, text_PostText, text_PostTags, button_Post);
        displayPosts(searchTag);
	}

	protected static void enterUserPosts() {
		postContainer.getChildren().clear();
		button_YourPosts.setVisible(false);
		Button button_Back = new Button("Back");
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 580, 540);
		boolean flag = false;
		for (var child : theRootPane.getChildren()) {
			if ("Back_User".equals(child.getId()))
				return;
		}
		
		if (!flag) {
			button_Back.setId("Back_User");
			button_Back.setOnAction((event) -> {button_YourPosts.setVisible(true); 
        							postContainer.getChildren().clear();
        							buildPostContainer();	
        							theRootPane.getChildren().remove(button_Back);});
			theRootPane.getChildren().add(button_Back);
		}
		displayUsersPosts();
	}
	
	protected static void enterUnreadPosts() {
		System.out.println("in");
		postContainer.getChildren().clear();
		button_UnreadPosts.setVisible(false);
		Button button_Back = new Button("Back");
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 580, 540);
		boolean flag = false;
		for (var child : theRootPane.getChildren()) {
			if ("Back_Unread".equals(child.getId()))
				return;
		}
		
		if (!flag) {
			button_Back.setId("Back_Unread");
			button_Back.setOnAction((event) -> {button_UnreadPosts.setVisible(true); 
        							postContainer.getChildren().clear();
        							buildPostContainer();	
        							theRootPane.getChildren().remove(button_Back);});
			theRootPane.getChildren().add(button_Back);
		}
		displayUnreadPosts();
	}
	
	protected static void displayPosts(String searchTag) {
		List<Post> posts;
		
		// Decide whether to get all posts or search by tag
		if (searchTag != null && !searchTag.isEmpty()) {
			posts = applicationMain.FoundationsMain.database.getPostsByTag(searchTag);
		} else {
			posts = applicationMain.FoundationsMain.database.getAllPosts();
		}
		System.out.println("Number of posts: " + posts.size());
        
		if(posts.size() == 0) {
			if (searchTag != null && !searchTag.isEmpty()) {
				postContainer.getChildren().add(new Label("No posts found with tag: " + searchTag));
			}
			return;
		} else {
			for(Post post : posts){

				VBox singlePostBox = new VBox(5);
				singlePostBox.setPadding(new Insets(10));
				singlePostBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
		        singlePostBox.setMaxWidth(Double.MAX_VALUE);
		        
		        
		        List<String> likes = new ArrayList<>();
				likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		        int likesNum = likes.size() - 1;
		        
				Label label_User = new Label("User: " + post.getUserName() + "  " + post.getPostTime() + "  Likes: " + likesNum);
				label_User.setFont(new Font("Arial", 15));
				
				
				Label postTextLabel = new Label(post.getPostText());
		        postTextLabel.setFont(new Font("Arial", 14));
		        postTextLabel.setWrapText(true);
		        
		        singlePostBox.getChildren().addAll(label_User, postTextLabel);
		        
		        TextArea text_ReplyText = new TextArea();
		        text_ReplyText.setPrefRowCount(5);
		        text_ReplyText.setWrapText(true);
		        text_ReplyText.setMaxWidth(width);
		        
		        HBox buttons = new HBox(5);
		        
		        Button button_Reply = new Button("Post Reply");
		        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
		                                displayRepliesForPost(post);});
		        
		        Button button_Like = new Button("Like");
		        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser);
		                                updateLikes(post, label_User);});

				// --- ADDED BLOCK to display tags ---
		        String tags = post.getTags();
		        Label tagsLabel = new Label("Tags: " + (tags != null && !tags.isEmpty() ? tags : "None"));
		        tagsLabel.setFont(new Font("Arial", 12));
		        tagsLabel.setStyle("-fx-font-style: italic;");
		        // --- END OF ADDED BLOCK ---
		        
		        Button button_View = new Button("Mark as Read");
		        button_View.setOnAction((event) -> {entityClasses.ManagePost.registerView(post, theUser);
		                                updateViews(post, button_View);});
		        
		        buttons.getChildren().addAll(button_Reply, button_Like, button_View);
		        
		        postContainer.getChildren().add(singlePostBox);
		        postContainer.getChildren().addAll(text_ReplyText, buttons, tagsLabel);
		        displayRepliesForPost(post);
		        postContainer.getChildren().addAll(new Separator());
		        

		    
			}
		}
	}
	
	protected static void displayRepliesForPost(Post post, String searchTag) {
		
		TextArea text_ReplyText = new TextArea();
        text_ReplyText.setPrefRowCount(3);
        text_ReplyText.setWrapText(true);
        text_ReplyText.setMaxWidth(width);
        int postID = post.getPostID();
        
        HBox buttons = new HBox(5);
        
        Button button_Reply = new Button("Reply");
        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
                                postContainer.getChildren().clear(); buildPostContainer(searchTag);});
        
        Button button_Like = new Button("Like");
        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser); 
                                postContainer.getChildren().clear(); buildPostContainer(searchTag);});
        
        buttons.getChildren().addAll(button_Reply, button_Like);
        
        postContainer.getChildren().addAll(text_ReplyText, buttons);
        
        List<Reply> replies = new ArrayList<>();
        try {
		replies = applicationMain.FoundationsMain.database.getRepliesForPost(postID);
        } catch (SQLException e) {
        	System.exit(0);
        }
        
        for(Reply reply : replies) {
        	VBox singleReplyBox = new VBox(5);
        	singleReplyBox.setPadding(new Insets(10));
        	singleReplyBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
        	singleReplyBox.setMaxWidth(Double.MAX_VALUE);
        	
        	Label label_User = new Label("User: " + reply.getUserName() + "  " + reply.getReplyTime());
			label_User.setFont(new Font("Arial", 15));
			label_User.setTranslateX(30);
        	
			Label replyTextLabel = new Label(reply.getReplyText());
	        replyTextLabel.setFont(new Font("Arial", 14));
	        replyTextLabel.setWrapText(true);
	        replyTextLabel.setTranslateX(30);
	        
	        singleReplyBox.getChildren().addAll(label_User, replyTextLabel);
	        
	        postContainer.getChildren().addAll(singleReplyBox);
        }
	}
	
	
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}


	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextArea t, String ff, double f, double w, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}
	
	/*-********************************************************************************************

	Helper methods to reduce code length


	 */
	
	protected static void updateLikes(Post post, Label label_User) {
		List<Post> posts = applicationMain.FoundationsMain.database.getAllPosts();

		for (Post p : posts)
			if (p.getPostID() == post.getPostID()) post.setLikes(p.getLikes());
		
		List<String> likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		int likesNum = likes.size() - 1;
		label_User.setText("User: " + post.getUserName() + "  " + post.getPostTime() + "  Likes: " + likesNum);
	}
	
	protected static void updateViews(Post post, Button button_View) {
		List<Post> posts = applicationMain.FoundationsMain.database.getAllPosts();

		for (Post p : posts)
			if (p.getPostID() == post.getPostID()) post.setViews(p.getViews());
		
		
		List<String> views = applicationMain.FoundationsMain.database.getViewsToList(post);
		System.out.println("views: " + views);
		if (views.contains(theUser.getUserName()))
			button_View.setText("Mark as Unread");
		else
			button_View.setText("Mark as Read");
	}

	protected static void displayUsersPosts() {
		List<Post> all_posts = new ArrayList<>();
		all_posts = applicationMain.FoundationsMain.database.getAllPosts();
		List<Post> posts = new ArrayList<>();
		for (Post post : all_posts) {
			String user = post.getUserName();
			if (user != null && user.compareTo(theUser.getUserName()) == 0)
				posts.add(post);
		}
        
		if(posts.size() == 0) {
			return;
		} else {
			for(Post post : posts){
				VBox singlePostBox = new VBox(5);
				singlePostBox.setPadding(new Insets(10));
				singlePostBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
		        singlePostBox.setMaxWidth(Double.MAX_VALUE);
		        
		        
		        List<String> likes = new ArrayList<>();
				likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		        int likesNum = likes.size() - 1;
		        
				Label label_User = new Label("User: " + post.getUserName() + "  " + post.getPostTime() + "  Likes: " + likesNum);
				label_User.setFont(new Font("Arial", 15));
				
				
				Label postTextLabel = new Label(post.getPostText());
		        postTextLabel.setFont(new Font("Arial", 14));
		        postTextLabel.setWrapText(true);
		        
		        singlePostBox.getChildren().addAll(label_User, postTextLabel);
		        
		        TextArea text_ReplyText = new TextArea();
		        text_ReplyText.setPrefRowCount(5);
		        text_ReplyText.setWrapText(true);
		        text_ReplyText.setMaxWidth(width);
		        
		        HBox buttons = new HBox(5);
		        
		        Button button_Reply = new Button("Post Reply");
		        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
		                                displayRepliesForPost(post);});
		        
		        Button button_Like = new Button("Like");
		        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser);
		                                updateLikes(post, label_User);});
		        
		        Button button_View = new Button("Mark as Read");
		        button_View.setOnAction((event) -> {entityClasses.ManagePost.registerView(post, theUser);
		                                updateViews(post, button_View);});
		        
		        buttons.getChildren().addAll(button_Reply, button_Like, button_View);
		        
		        postContainer.getChildren().add(singlePostBox);
		        postContainer.getChildren().addAll(text_ReplyText, buttons);
		        displayRepliesForPost(post);
		        postContainer.getChildren().addAll(new Separator());
			}
		}
	}
	
	protected static void displayUnreadPosts() {
		List<Post> all_posts = new ArrayList<>();
		all_posts = applicationMain.FoundationsMain.database.getAllPosts();
		List<Post> posts = new ArrayList<>();
		String userName = theUser.getUserName();
		for (Post post : all_posts) {
			if (!applicationMain.FoundationsMain.database.getViewsToList(post).contains(userName))
				posts.add(post);
		}
        
		if(posts.size() == 0) {
			return;
		} else {
			for(Post post : posts){
				VBox singlePostBox = new VBox(5);
				singlePostBox.setPadding(new Insets(10));
				singlePostBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
		        singlePostBox.setMaxWidth(Double.MAX_VALUE);
		        
		        
		        List<String> likes = new ArrayList<>();
				likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		        int likesNum = likes.size() - 1;
		        
				Label label_User = new Label("User: " + post.getUserName() + "  " + post.getPostTime() + "  Likes: " + likesNum);
				label_User.setFont(new Font("Arial", 15));
				
				
				Label postTextLabel = new Label(post.getPostText());
		        postTextLabel.setFont(new Font("Arial", 14));
		        postTextLabel.setWrapText(true);
		        
		        singlePostBox.getChildren().addAll(label_User, postTextLabel);
		        
		        TextArea text_ReplyText = new TextArea();
		        text_ReplyText.setPrefRowCount(5);
		        text_ReplyText.setWrapText(true);
		        text_ReplyText.setMaxWidth(width);
		        
		        HBox buttons = new HBox(5);
		        
		        Button button_Reply = new Button("Post Reply");
		        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
		                                displayRepliesForPost(post);});
		        
		        Button button_Like = new Button("Like");
		        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser);
		                                updateLikes(post, label_User);});
		        
		        Button button_View = new Button("Mark as Read");
		        button_View.setOnAction((event) -> {entityClasses.ManagePost.registerView(post, theUser);
		                                updateViews(post, button_View);});
		        
		        buttons.getChildren().addAll(button_Reply, button_Like, button_View);
		        
		        postContainer.getChildren().add(singlePostBox);
		        postContainer.getChildren().addAll(text_ReplyText, buttons);
		        displayRepliesForPost(post);
		        postContainer.getChildren().addAll(new Separator());
			}
		}
	}
}
