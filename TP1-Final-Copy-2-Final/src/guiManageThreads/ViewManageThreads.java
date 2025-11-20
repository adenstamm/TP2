package guiManageThreads;

import java.sql.SQLException;
import java.util.List;

import database.Database;
import entityClasses.Post;
import entityClasses.User;
import guiDiscussion.ControllerDiscussion;
import guiDiscussion.ViewDiscussion;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ModelDiscussion Class. </p>
 * 
 * <p> Description: The Discussion Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting discussion information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ViewManageThreads {
	/**
	 * Default constructor for ControllerDiscussion.
	 * Initializes the controller with default values and no special setup.
	 */
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	/** These are the application values required by the user interface */
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	/** Indicates the window height.*/
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	/** These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	   GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	   and a button to allow this user to update the account settings 
	*/
	
	protected static Label label_PageTitle = new Label();
	/** Will show the current user.*/
	protected static Label label_UserDetails = new Label();
	
	protected static Label label_Thread = new Label();

	/** The area that holds the textfield for the user's new post.*/
	protected static VBox threadContainer = new VBox(10);
	
	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	private static Button button_CreateThread = new Button("Create Thread");
	
	private static TextField text_ThreadText = new TextField();
	/**
	 * GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	 * logging out.
	 */
	
	protected static Button button_BackToHome = new Button("Back To Home");
	/** The button that is used to quit the program.*/
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	/** These attributes are used to configure the page and populate it with this user's information */
	private static ViewManageThreads theView;		// Used to determine if instantiation of the class
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
	private static Scene theThreads;	
	
	public static void displayThreads(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewManageThreads();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		
		label_UserDetails.setText("User: " + theUser.getUserName());// Set the username

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Manage Threads");
		theStage.setScene(theThreads);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	private ViewManageThreads() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theThreads = new Scene(theRootPane, width, height);	// Create the scene
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Manage Threads");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_CreateThread, "Dialog", 18, 250, Pos.CENTER, 150, 55);
		
		setupTextUI(text_ThreadText, "Dialog", 18, 250, 425, 55, true);
		
		text_ThreadText.setPromptText("Create a new thread");
		button_CreateThread.setOnAction((event) -> {
			String newThread = text_ThreadText.getText();
			try {
			theDatabase.registerThread(newThread);
			} catch (SQLException e) {
				System.out.println("Failed to create new Thread.");
			}
			text_ThreadText.clear();
			threadContainer.getChildren().clear();
			buildThreadContainer();
		});
		
		buildThreadContainer();
        
        ScrollPane scrollPane = new ScrollPane(threadContainer);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(90);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(410);
 	    scrollPane.setPannable(true);
		
		

		// GUI Area 3
        setupButtonUI(button_BackToHome, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_BackToHome.setOnAction((event) -> {ControllerManageThreads.goToUserHomePage(theStage, theUser);});
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerManageThreads.performQuit(); });
		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children

        theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
	        line_Separator4, button_BackToHome, button_Quit, scrollPane, button_CreateThread, text_ThreadText);
	}
	
	private static void buildThreadContainer(){
		List<String> threads = theDatabase.getThreadsListWithAll(false);
		
		for(String thread : threads){
			HBox threadBox = new HBox(5);
			Button button_DeleteThread = new Button("Delete Thread");
			button_DeleteThread.setOnAction((event) -> {
				theDatabase.deleteThread(thread);
				threadContainer.getChildren().clear();
				buildThreadContainer();
			});
			
			threadBox.getChildren().addAll(new Label(thread), button_DeleteThread);
			threadContainer.getChildren().addAll(threadBox);
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
	
	private void setupTextUI(TextField t, String ff, double f, double w, double x, double y, boolean e){
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