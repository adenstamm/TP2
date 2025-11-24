package guiStudentNotes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.Post;
import entityClasses.StudentNote;
import entityClasses.User;
import guiDiscussion.ControllerDiscussion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

public class ViewStudentNotes {
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
	protected static VBox studentContainer = new VBox(10);
	
	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	/** This is a separator and it is used to partition the GUI for various tasks */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
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
	private static ViewStudentNotes theView;		// Used to determine if instantiation of the class
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
	
	
	
	
	
	public static void displayStudentNotes(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewStudentNotes();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		
		label_UserDetails.setText("User: " + theUser.getUserName());// Set the username

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Student Notes");
		theStage.setScene(theThreads);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	private ViewStudentNotes() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theThreads = new Scene(theRootPane, width, height);	// Create the scene
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Student Notes");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		buildStudentContainer();
        
        ScrollPane scrollPane = new ScrollPane(studentContainer);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(90);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(410);
 	    scrollPane.setPannable(true);
		
		

		// GUI Area 3
        setupButtonUI(button_BackToHome, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_BackToHome.setOnAction((event) -> {ControllerStudentNotes.goToUserHomePage(theStage, theUser);});
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerStudentNotes.performQuit(); });
		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children

        theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
	        line_Separator4, button_BackToHome, button_Quit, scrollPane);
	}
	
	private static void buildStudentContainer(){
		List<User> users = theDatabase.getAllUsers();
		
		for(User user : users){
			if(user.getStudentRole()) {
				createNoteBoxes(user);
				
			}
		}
	}
	
	protected static void createNoteBoxes(User student) {
		VBox singleStudentBox = new VBox();
		HBox buttons = new HBox();
		
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
 		
 		HBox editPost = new HBox(10); 
 		editPost.setAlignment(Pos.BASELINE_LEFT);
        
 		
        buttons.setPadding(new Insets(0, 10, 0, 10));
        Button button_createNote = new Button("Create a Note");
        button_createNote.setOnAction((event) -> {
        	button_createNote.setText("Cancel");
        	TextArea text_createNote = new TextArea();
        	text_createNote.setPrefRowCount(5);
        	text_createNote.setWrapText(true);
        	text_createNote.setMaxWidth(width);
        	editPost.getChildren().add(text_createNote);
        	
        	button_createNote.setOnAction((eve) -> {
        		button_createNote.setText("Create a Note");
    	    	studentContainer.getChildren().clear();
    	    	buildStudentContainer();
            });
        	
        	Button button_makeNote = new Button("Make Note");
    	    button_makeNote.setOnAction((ev) -> {
    	    	ModelStudentNotes.registerNote(student.getUserName(), theUser, text_createNote.getText());
    	    	studentContainer.getChildren().clear();
    	    	buildStudentContainer();
    	    });
    	    editPost.getChildren().add(button_makeNote);
        });
        
        Button button_viewNotes = new Button("View Notes");
        button_viewNotes.setOnAction((event) -> {
        	button_viewNotes.setText("Cancel");
        	buildNoteUI(student.getUserName(), singleStudentBox);
        	button_viewNotes.setOnAction((eve) -> {
        		button_viewNotes.setText("View Notes");
    	    	studentContainer.getChildren().clear();
    	    	buildStudentContainer();
            });
        });
        Label label_student = new Label(student.getUserName());
	    buttons.getChildren().addAll(label_student, spacer, button_createNote, button_viewNotes);
        
	   	singleStudentBox.getChildren().addAll(buttons, editPost);
        
        studentContainer.getChildren().addAll(singleStudentBox,new Separator());
        	
	}
	
	protected static void buildNoteUI(String student, VBox theVBox) {
		List<StudentNote> notes = new ArrayList<>();
		try {
			notes = theDatabase.getNotesForStudent(student);
		} catch (SQLException e) {
			System.out.println("Failed to fetch student notes.");
		}
		for(StudentNote note : notes) {
			VBox singleNoteBox = new VBox();
			singleNoteBox.setPadding(new Insets(10));
			Label label_note = new Label(note.getNote());
	        label_note.setTranslateX(30);
	        label_note.setWrapText(true);
			singleNoteBox.getChildren().addAll(label_note);
			theVBox.getChildren().addAll(singleNoteBox, new Separator());
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
	
	
}