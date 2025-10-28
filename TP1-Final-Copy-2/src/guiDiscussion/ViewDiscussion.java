package guiDiscussion;

import javafx.collections.FXCollections;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
	
	/** These are the application values required by the user interface */
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	/** Indicates the window height.*/
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	private static String page = "Default";


	/** These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	   GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	   and a button to allow this user to update the account settings 
	*/
	
	protected static Label label_PageTitle = new Label();
	/** Will show the current user.*/
	protected static Label label_UserDetails = new Label();
	/** Indicates that this is where you create a new post.*/
	protected static Label label_CreatePost = new Label();
	
	/** The area where the user can input the post text.*/
	private static TextArea text_PostText = new TextArea();
	/** The is the button for users to publish their post.*/
	protected static Button button_Post = new Button("Post");
	/** The area that holds the textfield for the user's new post.*/
	protected static VBox postContainer = new VBox(10);
	protected static ComboBox <String> combobox_SelectThread = new ComboBox <String>();
	
	/** UI Elements for search */
	private static TextField text_SearchTags = new TextField();
	/** The button that searches to see what posts align with the tags.*/
	private static Button button_Search = new Button("Search");
	
	/** Clears the search area for tags.*/
	private static Button button_ClearSearch = new Button("Clear");
	/** The area where you input the tags you want associated with your post.*/
	private static TextField text_PostTags = new TextField();
	
	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	/** The button that will take you to view your posts.*/
	protected static Button button_YourPosts = new Button("Your Posts");
	/** The button to go to the Unread posts page.*/
	protected static Button button_UnreadPosts = new Button("Unread Posts");
	protected static Button button_Back_Unread = new Button("Back");
	protected static Button button_Back_Yours = new Button("Back");
	
	
	// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
	// would contain the widgets needed for the user to play the assigned role.
	
	
	
	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	/**
	 * GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	 * logging out.
	 */
	
	protected static Button button_BackToHome = new Button("Back To Home");
	/** The button that is used to quit the program.*/
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	/** These attributes are used to configure the page and populate it with this user's information */
	private static ViewDiscussion theView;		// Used to determine if instantiation of the class
												// is needed

	/** Reference for the in-memory database so this package has access */
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/** The Stage that JavaFX has established for us */
	protected static Stage theStage;			
	/** The Pane that holds all the GUI widgets*/
	protected static Pane theRootPane;		
	/** The current logged in User*/
	protected static User theUser;				
	
	/** The shared Scene each invocation populates*/
	private static Scene theDiscussion;	
	

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
	 * <p> Method: ViewDiscussion() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayDiscussion method.
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
		
		//Set up the selector for which threads are displayed
				setupComboBoxUI(combobox_SelectThread, "Dialog", 16, 50, 150, 630, 55);
		 		List<String> threadList = theDatabase.getThreadsListWithAll(true);
		 		
				combobox_SelectThread.setItems(FXCollections.observableArrayList(threadList));
				combobox_SelectThread.getSelectionModel().select(0);
				combobox_SelectThread.getSelectionModel().selectedItemProperty()
		     	.addListener((ObservableValue<? extends String> observable, 
		     		String oldvalue, String newValue) -> {displayPostsByThread(null, newValue);});
		
		
		buildPostContainer(null, null);
        
        
        ScrollPane scrollPane = new ScrollPane(postContainer);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(90);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(410);
 	    scrollPane.setPannable(true);

		// GUI Area 2
		setupButtonUI(button_YourPosts, "Dialog", 18, 200, Pos.CENTER, 410, 55);
        button_YourPosts.setOnAction((event) -> {
        	page = "User Posts";
        	enterUserPosts();
        	});
        
        setupButtonUI(button_UnreadPosts, "Dialog", 18, 200, Pos.CENTER, 190, 55);
        button_UnreadPosts.setOnAction((event) -> {
        	page = "Unread Posts";
        	enterUnreadPosts();
        });

		// GUI Area 3
        setupButtonUI(button_BackToHome, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_BackToHome.setOnAction((event) -> {ControllerDiscussion.goToUserHomePage(theStage, theUser);});
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerDiscussion.performQuit(); });
        

		setupButtonUI(button_Back_Unread, "Dialog", 18, 200, Pos.CENTER, 580, 540);
		button_Back_Unread.setVisible(false);
		setupButtonUI(button_Back_Yours, "Dialog", 18, 200, Pos.CENTER, 580, 540);
		button_Back_Yours.setVisible(false);
		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
        
        
        
        theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, combobox_SelectThread, line_Separator1,
	        line_Separator4, button_BackToHome, button_Quit, button_Back_Yours, 
	        button_Back_Unread, button_YourPosts, button_UnreadPosts, scrollPane);
	}
	
	/**********
	 * <p> Method: buildPostContainer(searchTag String, String thread) </p>
	 * 
	 * <p> Description: This method builds the elements of the graphical user interface that allow
	 * users to create new posts. This gives users a text field where they can input the text they 
	 * wish to post. It also allows users to attribute tags to posts as well as specifying a thread
	 * for the post to show up on. </p>
	 * 
	 * This method is called again when the post button is pressed.
	 * 
	 * @param searchTag specifies the tags that are associated with a post
	 * 
	 * @param thread specifies if you are in a thread
	 */
	
	protected static void buildPostContainer(String searchTag, String thread) {
		if(page.equals("User Posts")) {
			enterUserPosts();
			return;
		} else if (page.equals("Unread Posts")) {
			enterUnreadPosts();
			return;
		}
		if(thread != null) {
			displayPostsByThread(searchTag, thread); 
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
			buildPostContainer(tag, null);
		});
		
		button_ClearSearch.setOnAction((event) -> {
			text_SearchTags.clear();
			postContainer.getChildren().clear();
			buildPostContainer(null, thread);
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
		
		HBox postControls = new HBox(10); 
 		postControls.setAlignment(Pos.CENTER_LEFT);
 		postControls.setPadding(new Insets(0, 10, 0, 10));
 		
 		Label label_Thread = new Label("Thread:");
 		label_Thread.setFont(Font.font("Arial", 16));
    	//setupLabelUI(label_Thread, "Arial", 16, 100, Pos.BASELINE_LEFT, 90, 55);
    	ComboBox <String> combobox_selectPostThread = new ComboBox <String>();
    	//setupComboBoxUI(combobox_selectPostThread, "Dialog", 16, 320, 500, 280, 150);
    	combobox_selectPostThread.setStyle("-fx-font: 16 Dialog;");
    	combobox_selectPostThread.setMinWidth(150);
    	combobox_selectPostThread.setMaxWidth(150);
 		List<String> threadList = theDatabase.getThreadsListWithAll(false);
 		
 		
 		combobox_selectPostThread.setItems(FXCollections.observableArrayList(threadList));
 		combobox_selectPostThread.getSelectionModel().select(0);
 		combobox_selectPostThread.getSelectionModel().selectedItemProperty()
     	.addListener((ObservableValue<? extends String> observable, 
     		String oldValue, String newValue) -> {ControllerDiscussion.doSelectThread();});
		postContainer.setSpacing(10);
		
		Button button_Post = new Button("Post");
		setupButtonUI(button_Post, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
        button_Post.setOnAction((event) -> {
        	// Pass the tags text to storePost
        	entityClasses.ManagePost.storePost(theUser, text_PostText.getText(), combobox_selectPostThread.getValue(), text_PostTags.getText(), false); 
        	text_PostText.clear();	// Clear fields after posting
        	text_PostTags.clear();
            postContainer.getChildren().clear(); 
            buildPostContainer(searchTag, null); // Refresh with search
        });
        
        postControls.getChildren().addAll(label_Thread, combobox_selectPostThread, button_Post);
        
        // Add all elements to the container
        postContainer.getChildren().addAll(searchBox, new Separator(), label_CreatePost, text_PostText, text_PostTags, postControls);
        
        displayPosts(searchTag);
	}
	
	/**********
	 * <p> Method: enterUserPosts() </p>
	 * 
	 * <p> Description: This method enters the UserPosts page where users can see the posts that they 
	 * have made. This method calls displayUserPosts where the users posts are displayed.</p>
	 * 
	 * 
	 */

	/**********
	 * <p> Method: enterUserPosts() </p>
	 * 
	 * <p> Description: This method enters the UserPosts page where users can see the posts that they 
	 * have made. This method calls displayUserPosts where the users posts are displayed.</p>
	 * 
	 * 
	 */
	
	protected static void enterUserPosts() {
		postContainer.getChildren().clear();
		button_YourPosts.setVisible(false);
		button_UnreadPosts.setVisible(false);
		combobox_SelectThread.setVisible(false);
		button_Back_Yours.setVisible(true);
		boolean flag = false;
		/*for (var child : theRootPane.getChildren()) {
			if ("Back_User".equals(child.getId()))
				return;
		}
		*/
		if (!flag) {
			button_Back_Yours.setId("Back_User");
			button_Back_Yours.setOnAction((event) -> {
				button_YourPosts.setVisible(true); 
			    button_UnreadPosts.setVisible(true);
			    combobox_SelectThread.setVisible(true);
			    page = "Default";
			    combobox_SelectThread.setValue("All Threads");
				postContainer.getChildren().clear();
				buildPostContainer(null, null);
				button_Back_Yours.setVisible(false);
				});
		}
		displayUsersPosts();
	}
	
	/**********
	 * <p> Method: enterUnreadPosts() </p>
	 * 
	 * <p> Description: This method enters the UnreadPosts page where users can see the posts that they 
	 * have not read. This method calls displayUnreadPosts where the users unread posts are displayed.</p>
	 * 
	 * 
	 */
	
	protected static void enterUnreadPosts() {
		System.out.println("in");
		postContainer.getChildren().clear();
		button_UnreadPosts.setVisible(false);
		button_YourPosts.setVisible(false);
		combobox_SelectThread.setVisible(false);
		button_Back_Unread.setVisible(true);
		boolean flag = false;
		/*for (var child : theRootPane.getChildren()) {
			if ("Back_Unread".equals(child.getId()))
				return;
		}*/
		System.out.println("yes it gets to here");
		if (!flag) {
			button_Back_Unread.setId("Back_Unread");
			button_Back_Unread.setOnAction((event) -> {
				button_UnreadPosts.setVisible(true); 
				button_YourPosts.setVisible(true); 
				combobox_SelectThread.setVisible(true);
				page = "Default";
				combobox_SelectThread.setValue("All Threads");
				postContainer.getChildren().clear();
				buildPostContainer(null, null);	
				button_Back_Unread.setVisible(false);
				});
		}
		displayUnreadPosts();
	}
	
	/**********
	 * <p> Method: displayPosts(searchTag String) </p>
	 * 
	 * <p> Description: This method creates the area where all the posts are shown and filtered. This method
	 * reads the contents of the database to get a list of all the posts present. It also calls another method
	 * that gathers the replies for each post. Each of these posts are shown with the User who posted it, the 
	 * tags that are tied to the post, the thread the post is in, the time the post was created, and how many 
	 * likes and views the post has.</p>
	 * 
	 * This area also filters the posts by the User's selected thread as well as the ability to search for 
	 * specific keywords directed towards posts.
	 * 
	 * @param searchTag This specifies the tags associated with a post
	 * 
	 */
	
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
				createPostBoxes(post, searchTag, null);
		    }
		}
	}
	
	/**********
	 * <p> Method: displayRepliesForPost(post Post, searchTag String) </p>
	 * 
	 * <p> Description: This method creates the replies that are tied to the specified posts. When 
	 * this method is called a list of replies tied to that post will be pulled from the database.
	 * The necessary information about the post will be displayed below the post.
	 * 
	 * @param post specifies the current post
	 * 
	 * @param searchTag specifies the tags associated with the post
	 * 
	 */
	
	protected static void displayRepliesForPost(Post post, String searchTag) {
		
		TextArea text_ReplyText = new TextArea();
        text_ReplyText.setPrefRowCount(3);
        text_ReplyText.setWrapText(true);
        text_ReplyText.setMaxWidth(width);
        int postID = post.getPostID();
        
        HBox buttons = new HBox(5);
        
//        Button button_Reply = new Button("Reply");
//        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
//                                postContainer.getChildren().clear(); buildPostContainer(searchTag);});
//        
//        Button button_Like = new Button("Like");
//        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser); 
//                                postContainer.getChildren().clear(); buildPostContainer(searchTag);});
        
//        buttons.getChildren().addAll(button_Reply, button_Like);
        
//        postContainer.getChildren().addAll(buttons);
        
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
	/**********
	 * <p> Method: refreshPosts </p>
	 * 
	 * <p> Description: This method refreshes the user drop down menu after delete action. </p>
	 * 
	 */
	public static void refreshPosts() {
		postContainer.getChildren().clear();
		buildPostContainer(null, null);
		
		
	}
	
	/**********
	 * <p> Method: displayPostsByThread(thread String) </p>
	 * 
	 * <p> Description: This method takes a thread parameter that is used to filter the posts on screen. 
	 * This method scans through all of the posts stored in the database and adds the ones that are a part
	 * of the same thread as the current thread.
	 * 
	 * @param thread specifies the thread the user has chosen to display
	 * 
	 */
	
	protected static void displayPostsByThread(String searchTag, String thread) {
		postContainer.getChildren().clear();
		if(thread == "All Threads") buildPostContainer(null, null);
		
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
			buildPostContainer(tag, thread);
		});
		
		button_ClearSearch.setOnAction((event) -> {
			text_SearchTags.clear();
			postContainer.getChildren().clear();
			buildPostContainer(null, thread);
		});
		
		searchBox.getChildren().addAll(new Label("Search Tags:"), text_SearchTags, button_Search, button_ClearSearch);
				
		postContainer.getChildren().addAll(searchBox, new Separator());
				
		List<Post> posts = new ArrayList<>();
		posts = applicationMain.FoundationsMain.database.getAllPosts();
		
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
				if(post.getThread() == thread) {
					createPostBoxes(post, searchTag, thread);
				}
		    }
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
	 * Private local method to initialize the standard fields for a textUI
	 * 
	 * @param t		The TextArea object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param x		The alignment (e.g. left, centered, or right)
	 * @param y		The location from the top edge (y axis)
	 * @param e		The ability to edit the text
	 */
	
	private void setupTextUI(TextArea t, String ff, double f, double w, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param mw	The max width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double mw, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setMaxWidth(mw);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}

	
	
	/*-********************************************************************************************

	Helper methods to reduce code length


	 */
	
	/**********
	 * <p> Method: updateLikes(post Post, label_User Label) </p>
	 * 
	 * <p> Description: This is the method for managing likes. It checks the database to see
	 * how many people have liked a specific post and adds the number to the User label .
	 * 
	 * @param post specifies post that the likes are tied to
	 * 
	 * @param label_User specifies label that shows the users name, and other features of the post
	 * 
	 */
	
	protected static void updateLikes(Post post, Button button_Like) {
		List<Post> posts = applicationMain.FoundationsMain.database.getAllPosts();

		for (Post p : posts)
			if (p.getPostID() == post.getPostID()) post.setLikes(p.getLikes()); {
		
		List<String> likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		int likesNum = likes.size() - 1;
		if (likes.contains(theUser.getUserName()))
			button_Like.setText("Liked");
		else
			button_Like.setText("Like");
		}
		//label_User.setText("User: " + post.getUserName() + "  " + post.getPostTime() + "  Likes: " + likesNum);
	}
	
	/**********
	 * <p> Method: updateLikes(post Post, label_User Label) </p>
	 * 
	 * <p> Description: This is the method for managing views. It checks the database to see
	 * how many people have viewed a specific post and adds the number to the User label. If 
	 * the user has already read a post, they can mark it as unread.
	 * 
	 * @param post specifies the post that the views are tied to
	 * 
	 * @param button_View specifies the button that is pressed to register a view
	 * 
	 */
	
	protected static void updateViews(Post post, Button button_View) {
		List<Post> posts = applicationMain.FoundationsMain.database.getAllPosts();

		for (Post p : posts)
			if (p.getPostID() == post.getPostID()) post.setViews(p.getViews()); {
				List<String> views = applicationMain.FoundationsMain.database.getViewsToList(post);
				System.out.println("views: " + views);
				if (views.contains(theUser.getUserName()))
					button_View.setText("Mark as Unread");
				else
					button_View.setText("Mark as Read");
		}
	}
	
	/**********
	 * <p> Method: displayUsersPosts() </p>
	 * 
	 * <p> Description: This method builds the UserPosts page where users can see the posts that they 
	 * have made. All posts that have been made by the current user will appear here along with the 
	 * replies to those posts.</p>
	 * 
	 * 
	 */
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
				createPostBoxes(post, null, null);
			}
		}
	}
	
	/**********
	 * <p> Method: displayUnreadPosts() </p>
	 * 
	 * <p> Description: This method builds the UnreadPosts page where users can see the posts that they 
	 * have not yet read. This shows the posts that the current user has not read and displays the replies
	 * to those posts as well.</p>
	 * 
	 * 
	 */
	
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
				if(!post.getSoftDelete())
					createPostBoxes(post, null, null);
			}
		}
	}
	
	/**********
	 * <p> Method: createPostBoxes(Post post, String searchTag, String thread) </p>
	 * 
	 * <p> Description: This method should create the posts to fill out the 
	 * discussions, depending on what search and threads are inputed </p>
	 * 
	 * @param post takes the post provided and creates a section for its information
	 * 
	 * @param searchTag takes the tag that is searched, and allows the post to 
	 * refresh that searches values
	 * 
	 * @param thread allows the post to refresh the thread when something is
	 * liked, viewed, updated, or replied to
	 * 
	 */
	protected static void createPostBoxes(Post post, String searchTag, String thread) {
		
		VBox singlePostBox = new VBox(5);
		singlePostBox.setPadding(new Insets(10));
		singlePostBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
        singlePostBox.setMaxWidth(Double.MAX_VALUE);
        
        
        List<String> likes = new ArrayList<>();
		likes = applicationMain.FoundationsMain.database.getLikesToList(post);
        int likesNum = likes.size() - 1;
        
        List<String> views = new ArrayList<>();
		views = applicationMain.FoundationsMain.database.getViewsToList(post);
        int viewsNum = views.size() - 1;
        
        HBox postLabel = new HBox(20); 
 		postLabel.setAlignment(Pos.CENTER_LEFT);
        if(!post.getSoftDelete()) {
		Label label_User = new Label("User: " + post.getUserName());
		// + "  " + post.getPostTime() + "  Likes: " + likesNum);
		label_User.setFont(new Font("Arial", 18));
		
		
		Label label_Thread = new Label("Thread: " + post.getThread());
		label_Thread.setFont(new Font("Arial", 16));
		
		Label label_Created = new Label("Date Created: " + post.getPostTime());
		label_Created.setFont(new Font("Arial", 16));
		
		Label label_Likes = new Label("Likes: " + likesNum);
		label_Likes.setFont(new Font("Arial", 16)); 
		
		Label label_Views = new Label("Views: " + viewsNum);
		label_Views.setFont(new Font("Arial", 16)); 
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		postLabel.getChildren().addAll(label_User, spacer, label_Thread, label_Created, label_Likes, label_Views);
		
		Label postTextLabel = new Label(post.getPostText());
        postTextLabel.setFont(new Font("Arial", 16));
        postTextLabel.setWrapText(true);
        postTextLabel.setPadding(new Insets(5, 10, 5, 10));
       
        singlePostBox.getChildren().addAll(postLabel, postTextLabel);
        } else {
        	Label label_deleted = new Label("This post was deleted.");
        	label_deleted.setFont(new Font("Arial", 20));
        	postLabel.setAlignment(Pos.CENTER);
			postLabel.getChildren().addAll(label_deleted);
			singlePostBox.getChildren().addAll(postLabel);
        }
        
        String tags = post.getTags();
        Label tagsLabel = new Label("Tags: " + (tags != null && !tags.isEmpty() ? tags : "None"));
        tagsLabel.setFont(new Font("Arial", 12));
        tagsLabel.setStyle("-fx-font-style: italic;");
        
        
        HBox buttons = new HBox(0); 
 		buttons.setAlignment(Pos.BASELINE_LEFT);
 		
 		HBox editPost = new HBox(10); 
 		editPost.setAlignment(Pos.BASELINE_LEFT);
        
 		if(!post.getSoftDelete() ) {
        	
        	buttons.setPadding(new Insets(0, 10, 0, 10));
        	Button button_openReply = new Button("Create a Reply");
        	button_openReply.setOnAction((event) -> {
        		button_openReply.setDisable(true);
        		TextArea text_createReply = new TextArea();
        		text_createReply.setPrefRowCount(5);
        		text_createReply.setWrapText(true);
        		text_createReply.setMaxWidth(width);
        		editPost.getChildren().add(text_createReply);
        	
        	    Button button_reply = new Button("Reply");
        	    button_reply.setOnAction(ev -> {
        	    	entityClasses.ManageReply.storeReply(post, theUser, text_createReply.getText()); 
        	    	button_openReply.setDisable(false);
        	    	 postContainer.getChildren().clear();
        	    	 if(thread == null) {buildPostContainer(searchTag, null);}
 	       	    	 else if (thread != null) {displayPostsByThread(searchTag, thread);}
        	    });
        	    setupButtonUI(button_reply, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
        	    editPost.setMargin(button_reply, new Insets(0, 10, 0, 10));
        	    editPost.getChildren().add(button_reply);
            });
        	
	        setupButtonUI(button_openReply, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
	        buttons.setMargin(button_openReply, new Insets(0, 10, 0, 10));
	        buttons.getChildren().addAll(button_openReply);
	        	
	        if(post.getUserName() != theDatabase.getCurrentUsername()) {
	        	Button button_Like = new Button("Like");
		        button_Like.setOnAction((event) -> {
		        	entityClasses.ManagePost.registerLike(post, theUser); 
		        	updateLikes(post, button_Like); 
		        	
		             postContainer.getChildren().clear(); 
		             if(thread == null) {buildPostContainer(searchTag, null);}
		       	     else if (thread != null) {displayPostsByThread(searchTag, thread);}
		        	});
		        updateLikes(post, button_Like);
		        setupButtonUI(button_Like, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
		        buttons.setMargin(button_Like, new Insets(0, 10, 0, 10));
		        buttons.getChildren().addAll(button_Like);
		      
		        // --- END OF ADDED BLOCK ---
		        
		        Button button_View = new Button("Mark as Read");
		        button_View.setOnAction((event) -> {
		        	entityClasses.ManagePost.registerView(post, theUser);
		            updateViews(post, button_View); 
		            postContainer.getChildren().clear(); 
		            if(thread == null) {buildPostContainer(searchTag, null);}
	       	    	else if (thread != null) {displayPostsByThread(searchTag, thread);}
		            });
		        updateViews(post, button_View);
		        setupButtonUI(button_View, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
		        buttons.setMargin(button_View, new Insets(0, 10, 0, 10));
		        buttons.getChildren().addAll(button_View);
	        }
	              
	        if(post.getUserName() == theDatabase.getCurrentUsername()) {
	        	Button button_editPost = new Button("Edit Post");
	        	button_editPost.setOnAction((event) -> {
	        		button_editPost.setDisable(true);
	        		TextArea text_editPost = new TextArea();
	        		text_editPost.setPrefRowCount(5);
	        		text_editPost.setWrapText(true);
	        		text_editPost.setMaxWidth(width);
	        		editPost.getChildren().add(text_editPost);
	        	
	        	    Button button_saveChanges = new Button("Save Changes");
	        	    button_saveChanges.setOnAction(ev -> {
	        	    	button_editPost.setDisable(false);
	        	    	String updatedText = text_editPost.getText();
	        	    	if (!updatedText.isEmpty()) {
	        	    		try {
	                        theDatabase.setPostText(post, updatedText);}
	        	    		catch (SQLException e) {
	        	    			e.printStackTrace();
	        	    		}
	                        postContainer.getChildren().clear();
	                        if(thread == null) {buildPostContainer(searchTag, null);}
	    	       	    	else if (thread != null) {displayPostsByThread(searchTag, thread);}
	                    }
	        	    });	
	        	    setupButtonUI(button_saveChanges, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
	        	    editPost.setMargin(button_saveChanges, new Insets(0, 10, 0, 10));
	        	    editPost.getChildren().add(button_saveChanges);
	        	});
		        setupButtonUI(button_editPost, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
		        buttons.setMargin(button_editPost, new Insets(0, 10, 0, 10));
		        buttons.getChildren().addAll(button_editPost);
		        
		        Button button_deletePost = new Button("Delete Post");
		        button_deletePost.setOnAction((event) -> {
		        	ControllerDiscussion.performDeletePost(post);});
		        setupButtonUI(button_deletePost, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
		        buttons.setMargin(button_deletePost, new Insets(0, 10, 0, 10));
		        buttons.getChildren().addAll(button_deletePost);
        	}
	        singlePostBox.getChildren().addAll(tagsLabel, buttons, editPost);
        } 
        
        
        
        postContainer.getChildren().add(singlePostBox);
        displayRepliesForPost(post, null);
        postContainer.getChildren().addAll(new Separator());
        
		
	}
}
