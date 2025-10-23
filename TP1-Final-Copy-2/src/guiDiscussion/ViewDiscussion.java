package guiDiscussion;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
import entityClasses.User;
import entityClasses.Post;
import entityClasses.Reply;
import guiDiscussion.ModelDiscussion;

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
	protected static ComboBox <String> combobox_SelectThread = new ComboBox <String>();
	
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
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
		refreshPosts();

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
		
		//Set up the selector for which threads are displayed
		setupComboBoxUI(combobox_SelectThread, "Dialog", 16, 50, 150, 630, 55);
 		List<String> threadList = theDatabase.getThreadsListWithAll(true);
 		
		combobox_SelectThread.setItems(FXCollections.observableArrayList(threadList));
		combobox_SelectThread.getSelectionModel().select(0);
		combobox_SelectThread.getSelectionModel().selectedItemProperty()
     	.addListener((ObservableValue<? extends String> observable, 
     		String oldvalue, String newValue) -> {displayPostsByThread(newValue);});
 		
		//setupButtonUI(button_Threads, "Dialog", 16, 70, Pos.CENTER, 660, 55);
        //button_Threads.setOnAction((event) -> {ControllerDiscussion.openThreads(); });
		
		buildPostContainer();
		
        
        ScrollPane scrollPane = new ScrollPane(postContainer);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(90);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(410);
 	    scrollPane.setPannable(true);
		// GUI Area 3
        setupButtonUI(button_BackToHome, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_BackToHome.setOnAction((event) -> {ControllerDiscussion.goToUserHomePage(theStage, theUser);});
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerDiscussion.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
        
        
        
        theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, combobox_SelectThread,
			line_Separator1, line_Separator4, button_BackToHome, button_Quit, scrollPane);
	}
	
	protected static void buildPostContainer() {
		postContainer.setPadding(new Insets(5, 20, 5, 20));
		Label label_CreatePost = new Label("Create a Post:");
		label_CreatePost.setFont(new Font("Arial", 20));
		
		TextArea text_PostText = new TextArea();
		text_PostText.setPromptText("Enter your Post");
		text_PostText.setPrefRowCount(5);
		text_PostText.setWrapText(true);
		text_PostText.setMaxWidth(width);
		
		
 		
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
 		
 		Button button_Post = new Button("Post");
        button_Post.setOnAction((event) -> {
        	String selectedThread = combobox_selectPostThread.getValue();
        	entityClasses.ManagePost.storePost(theUser, text_PostText.getText(), selectedThread, false); 
                                postContainer.getChildren().clear(); buildPostContainer();});
        setupButtonUI(button_Post, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
 		
 		
 		postControls.getChildren().addAll(label_Thread, combobox_selectPostThread, button_Post);

 		
      //Select an thread
    	
        postContainer.getChildren().addAll(label_CreatePost, text_PostText, postControls );
        displayPosts();
	}
	
	protected static void displayPosts() {
		List<Post> posts = new ArrayList<>();
		posts = applicationMain.FoundationsMain.database.getAllPosts();
        
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
		        
		        HBox postLabel = new HBox(20); 
		 		postLabel.setAlignment(Pos.CENTER_LEFT);
		        
				Label label_User = new Label("User: " + post.getUserName());
				// + "  " + post.getPostTime() + "  Likes: " + likesNum);
				label_User.setFont(new Font("Arial", 20));
				
				
				Label label_Thread = new Label("Thread: " + post.getThread());
				label_Thread.setFont(new Font("Arial", 18));
				
				Label label_Created = new Label("Date Created: " + post.getPostTime());
				label_Created.setFont(new Font("Arial", 18));
				
				Label label_Likes = new Label("Likes: " + likesNum);
				label_Likes.setFont(new Font("Arial", 18));
				
				Region spacer = new Region();
				HBox.setHgrow(spacer, Priority.ALWAYS);
				
				postLabel.getChildren().addAll(label_User, spacer, label_Thread, label_Created, label_Likes);
				
				
				Label postTextLabel = new Label(post.getPostText());
		        postTextLabel.setFont(new Font("Arial", 16));
		        postTextLabel.setWrapText(true);
		        postTextLabel.setPadding(new Insets(5, 10, 5, 10));
		       
		        singlePostBox.getChildren().addAll(postLabel, postTextLabel);
		        
		        HBox buttons = new HBox(0); 
		 		buttons.setAlignment(Pos.BASELINE_LEFT);
		 		
		        if(!post.getSoftDelete() ) {
		        	
		        	/*buttons.setPadding(new Insets(0, 10, 0, 10));
		        	Button button_openReply = new Button("Create a Reply");
		        	button_openReply.setOnAction((event) -> {
			        	openReply();});
			        setupButtonUI(button_openReply, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
			        buttons.setMargin(button_openReply, new Insets(0, 10, 0, 10));
			        buttons.getChildren().addAll(button_openReply);
			        		
			        if(post.getUserName() != theDatabase.getCurrentUsername()) {
				        Button button_like = new Button("Like");
				        button_like.setOnAction((event) -> {
				        	ControllerDiscussion.performDeletePost(post);});
				        setupButtonUI(button_like, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
				        buttons.setMargin(button_like, new Insets(0, 10, 0, 10));
				        buttons.getChildren().addAll(button_like);
			        }
			        */      
			        if(post.getUserName() == theDatabase.getCurrentUsername()) {
				        Button button_deletePost = new Button("Delete Post");
				        button_deletePost.setOnAction((event) -> {
				        	ControllerDiscussion.performDeletePost(post);});
				        setupButtonUI(button_deletePost, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
				        buttons.setMargin(button_deletePost, new Insets(0, 10, 0, 10));
				        buttons.getChildren().addAll(button_deletePost);
		        	}
			        singlePostBox.getChildren().addAll(buttons);
		        } 
		        postContainer.getChildren().add(singlePostBox);
		        displayRepliesForPost(post);
		        
		        postContainer.getChildren().addAll(new Separator());
			}
		}
	}
	
	protected static void displayRepliesForPost(Post post) {
		int postID = post.getPostID();
		if(!post.getSoftDelete()) {
			TextArea text_ReplyText = new TextArea();
	        text_ReplyText.setPrefRowCount(5);
	        text_ReplyText.setWrapText(true);
	        text_ReplyText.setMaxWidth(width);
	        
	        
	        HBox buttons = new HBox(5);
	        
	        Button button_Reply = new Button("Post Reply");
	        button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
	                                postContainer.getChildren().clear(); buildPostContainer();});
	        
	        Button button_Like = new Button("Like");
	        button_Like.setOnAction((event) -> {entityClasses.ManagePost.registerLike(post, theUser); 
	                                postContainer.getChildren().clear(); buildPostContainer();});
	        
	        buttons.getChildren().addAll(button_Reply, button_Like);
	        
	        postContainer.getChildren().addAll(text_ReplyText, buttons);
		}
		
		
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
			label_User.setFont(new Font("Arial", 16));
			label_User.setTranslateX(30);
        	
			Label replyTextLabel = new Label(reply.getReplyText());
	        replyTextLabel.setFont(new Font("Arial", 14));
	        replyTextLabel.setWrapText(true);
	        replyTextLabel.setTranslateX(30);
	        replyTextLabel.setPadding(new Insets(5, 5, 5, 5));
	        
	        
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
		buildPostContainer();
		
		
	}
	
	protected static void displayPostsByThread(String thread) {
		postContainer.getChildren().clear();
		if(thread == "All Threads") buildPostContainer();
		
		List<Post> posts = new ArrayList<>();
		posts = applicationMain.FoundationsMain.database.getAllPosts();
        
		if(posts.size() == 0) {
			return;
		} else {
			for(Post post : posts){
				if(post.getThread() == thread) {
				VBox singlePostBox = new VBox(5);
				singlePostBox.setPadding(new Insets(10));
				singlePostBox.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 8;");
		        singlePostBox.setMaxWidth(Double.MAX_VALUE);
		        
		        List<String> likes = new ArrayList<>();
				likes = applicationMain.FoundationsMain.database.getLikesToList(post);
		        int likesNum = likes.size() - 1;
		        
		        HBox postLabel = new HBox(20); 
		 		postLabel.setAlignment(Pos.CENTER_LEFT);
		        
				Label label_User = new Label("User: " + post.getUserName());
				// + "  " + post.getPostTime() + "  Likes: " + likesNum);
				label_User.setFont(new Font("Arial", 20));
				
				
				Label label_Thread = new Label("Thread: " + post.getThread());
				label_Thread.setFont(new Font("Arial", 18));
				
				Label label_Created = new Label("Date Created: " + post.getPostTime());
				label_Created.setFont(new Font("Arial", 18));
				
				Label label_Likes = new Label("Likes: " + likesNum);
				label_Likes.setFont(new Font("Arial", 18));
				
				Region spacer = new Region();
				HBox.setHgrow(spacer, Priority.ALWAYS);
				
				postLabel.getChildren().addAll(label_User, spacer, label_Thread, label_Created, label_Likes);
				
				
				Label postTextLabel = new Label(post.getPostText());
		        postTextLabel.setFont(new Font("Arial", 16));
		        postTextLabel.setWrapText(true);
		        singlePostBox.getChildren().addAll(postLabel, postTextLabel);
		        
		        HBox buttons = new HBox(0); 
		 		buttons.setAlignment(Pos.BASELINE_LEFT);
		 		
		        if(!post.getSoftDelete() ) {
		        	
		        	/*buttons.setPadding(new Insets(0, 10, 0, 10));
		        	Button button_openReply = new Button("Create a Reply");
		        	button_openReply.setOnAction((event) -> {
			        	openReply();});
			        setupButtonUI(button_openReply, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
			        buttons.setMargin(button_openReply, new Insets(0, 10, 0, 10));
			        buttons.getChildren().addAll(button_openReply);
			        		
			        if(post.getUserName() != theDatabase.getCurrentUsername()) {
				        Button button_like = new Button("Like");
				        button_like.setOnAction((event) -> {
				        	ControllerDiscussion.performDeletePost(post);});
				        setupButtonUI(button_like, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
				        buttons.setMargin(button_like, new Insets(0, 10, 0, 10));
				        buttons.getChildren().addAll(button_like);
			        }
			        */
			        if(post.getUserName() == theDatabase.getCurrentUsername()) {
				        Button button_deletePost = new Button("Delete Post");
				        button_deletePost.setOnAction((event) -> {
				        	ControllerDiscussion.performDeletePost(post);});
				        setupButtonUI(button_deletePost, "Dialog", 16, 50, Pos.BASELINE_RIGHT, 20, 55);
				        buttons.setMargin(button_deletePost, new Insets(0, 10, 0, 10));
				        buttons.getChildren().addAll(button_deletePost);
		        	}
			        singlePostBox.getChildren().addAll(buttons);
		        } 
		        postContainer.getChildren().add(singlePostBox);
		        displayRepliesForPost(post);
		        
		        postContainer.getChildren().addAll(new Separator());
			}}
		}
	}
	
	/*protected static void openReply() {
		TextArea text_ReplyText = new TextArea();
	    text_ReplyText.setPrefRowCount(5);
	    text_ReplyText.setWrapText(true);
	    text_ReplyText.setMaxWidth(width);
	    
	    
	    HBox buttons = new HBox(5);
	    
	    Button button_Reply = new Button("Post Reply");
	    button_Reply.setOnAction((event) -> {entityClasses.ManageReply.storeReply(post, theUser, text_ReplyText.getText()); 
	                            postContainer.getChildren().clear(); buildPostContainer();});	
		}
	}*/
	
	
	
	
	
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
}

