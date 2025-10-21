package guiFirstAdmin;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
//import passwordPopUpWindow.Model;
import javafx.scene.Scene;
// Import the Color File
import javafx.scene.paint.Color;


/*******
 * <p> Title: ViewFirstAdmin Class</p>
 * 
 * <p> Description: The FirstAdmin Page View.  This class is used to require the very first user of
 * the system to specify an Admin Username and Password when there is no database for the
 * application.  This avoids the common weakness practice of hard coding credentials into the 
 * application.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ViewFirstAdmin {

	/*-********************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// These are the widget attributes for the GUI
	
	// The GUI informs the user about the purpose of this page, provides three text inputs fields
	// for the user to specify a username for this account and two copies of the password to be
	// used (they must match), a button to request that the account be established, and a quit
	// but to abort the action and stop the application.
	private static Label label_ApplicationTitle = new Label("Foundation Application Startup Page");
	private static Label label_TitleLine1 = 
			new Label(" You are the first user.  You must be an administrator.");
	
	private static Label label_TitleLine2 = 
			new Label("Enter the Admin's Username, the Password twice, and then click on " + 
					"Setup Admin Account.");
	
	protected static Label label_PasswordsDoNotMatch = new Label();
	protected static TextField text_AdminUsername = new TextField();
	protected static PasswordField text_AdminPassword1 = new PasswordField();
	protected static PasswordField text_AdminPassword2 = new PasswordField();
	static Button button_AdminSetup = new Button("Setup Admin Account");
	
	// This code was added to identify if the username is acceptable
	static protected Label label_Alphabetic = new Label();
	static protected Label label_validUsername = new Label();
	static protected Label label_SPChar = new Label();
	static protected Label label_UNRequirements = new Label();
	static protected Label label_UNTooLong = new Label();
	static protected Label label_UNLongEnough = new Label();
	
	
	// This code was added to identify if the password is acceptable
	static protected Label label_errPassword = new Label();	// There error labels change based on the
	//removed no input error, as I do not show requirements until after there is an input
	static private TextFlow errPassword;
	static protected Text errPasswordPart1 = new Text();		// This contains the user's input
	static protected Text errPasswordPart2 = new Text();		// This is the up arrow
	static protected Label errPasswordPart3 = new Label();		// This is the concatenation of the two
	
	static protected Label validPassword = new Label();		// This only appears with a valid password
	static protected Label label_Requirements = new Label();
	static protected Label label_UpperCase = new Label();		// These empty labels change based on the
	static protected Label label_LowerCase = new Label();		// user's input
	static protected Label label_NumericDigit = new Label();	
	static protected Label label_SpecialChar = new Label();
	static protected Label label_LongEnough = new Label();
	static protected Label label_TooLong = new Label();

	// This alert is used should the user enter two passwords that do not match
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

	// This button allow the user to abort creating the first admin account and terminate
	private static Button button_Quit = new Button("Quit");

	// These attributes are used to configure the page and populate it with this user's information
	protected static Stage theStage;	
	private static Pane theRootPane;
	private static Scene theFirstAdminScene = null;
	private static final int theRole = 1;		// Admin: 1; Role1: 2; Role2: 3
		
	
	/*-********************************************************************************************

	Constructor

	 */
	
	/**********
	 * <p> Method: public displayFirstAdmin(Stage ps) </p>
	 * 
	 * <p> Description: This method is called when the application first starts. It create an
	 * an instance of the View class.  
	 * 
	 * NOTE: As described below, this code does not implement MVC using the singleton pattern used
	 * by most of the pages as the code is written this way because we know with certainty that it
	 * will only be called once.  For this reason, we directly call the private class constructor.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 */
	public static void displayFirstAdmin(Stage ps) {
		
		// Establish the references to the GUI.  There is no user yet.
		theStage = ps;			// Establish a reference to the JavaFX Stage
		
		// This page is only called once so there is no need to save the reference to it
		new ViewFirstAdmin();	// Create an instance of the class
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		applicationMain.FoundationsMain.activeHomePage = theRole;	// 1: Admin; 2: Role1; 3 Roles2

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: First User Account Setup");	
		theStage.setScene(theFirstAdminScene);
		theStage.show();
	}

	/**********
	 * <p> Method: private ViewFirstAdmin() </p>
	 * 
	 * <p> Description: This constructor is called when the application first starts. It must 
	 * handle when no user has been established.  It is never called again, either during the first
	 * run of the program after reboot or during normal operations of the program.  (This page is
	 * only used when there is no database for the application or there is a database, but there
	 * no users in the database.
	 * 
	 * If there is no database or there is a database, but there are no users, it means that the
	 * person starting the system is an administrator, so a special GUI is provided to allow this
	 * Admin to create an account by setting a username and password.
	 * 
	 * If there is at least one user, this page is not called.  Since this is only used one when
	 * the system is being set up, this MVC code does not use a singleton protocol.  For this
	 * reason, do not use this as a typical MVC pattern.</p>
	 * 
	 * This is the View of the Model, View, Controller architectural pattern.  Due to how it is
	 * called, it does not follow the usual singleton implementation pattern as explained above.
	 * 
	 * The view sets up the window and all of the Graphical User Interface (GUI) widgets.  Once
	 * they are all set, the window is displayed to the user, the View returns to the starting main
	 * method, and that execution thread ends.  The JavaFX thread continues to "execute" waiting
	 * for the user to interact with the GUI.  Based on which widget is used, changes to the
	 * display are made and/or a Controller method is called to perform some action.
	 * 
	 */
	private ViewFirstAdmin() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theFirstAdminScene = new Scene(theRootPane, width, height);

		// Label theScene with the name of the system startup screen
		setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine1, "Arial", 24, width, Pos.CENTER, 0, 70);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine2, "Arial", 18, width, Pos.CENTER, 0, 130);

		// Establish the text input operand field for the Admin username
		// And check if the username satisfies requirements
		setupTextUI(text_AdminUsername, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, 
				true);
		text_AdminUsername.setPromptText("Enter Admin Username");
		text_AdminUsername.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerFirstAdmin.setAdminUsername(); });
		text_AdminUsername.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelFirstAdmin.checkUsername(); });
		

		// Establish the text input operand field for the password
		// And call to check if the password satisfies the conditions
		setupTextUI(text_AdminPassword1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, 
				true);
		text_AdminPassword1.setPromptText("Enter Admin Password");
		text_AdminPassword1.textProperty().addListener((observable, oldValue, newValue)
				-> {ControllerFirstAdmin.setAdminPassword1(); });
		text_AdminPassword1.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelFirstAdmin.checkPassword(); });

		// Establish the text input operand field for the password
		// And call the check for if the passwords are the same
		setupTextUI(text_AdminPassword2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, 
				true);
		text_AdminPassword2.setPromptText("Enter Admin Password Again");
		text_AdminPassword2.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerFirstAdmin.setAdminPassword2(); });
		text_AdminPassword2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelFirstAdmin.comparePasswords(); });
		text_AdminPassword2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelFirstAdmin.updateComparisonFlags(); });
		text_AdminPassword2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelFirstAdmin.adminButtonEnabled(); });

		// Set up the Log In button
		setupButtonUI(button_AdminSetup, "Dialog", 18, 200, Pos.CENTER, 475, 210);
		// Set Up Button disabled until there are acceptable username, and acceptable, matching passwords
		button_AdminSetup.setDisable(true);
		button_AdminSetup.setOnAction((event) -> {
			ControllerFirstAdmin.doSetupAdmin(theStage,1); 
			});
		
		// Added code to set up the username requirements
		setupLabelUI(label_UNRequirements, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 300);
		setupLabelUI(label_Alphabetic, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 330);
		setupLabelUI(label_UNTooLong, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 360);
		setupLabelUI(label_UNLongEnough, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 390);
		setupLabelUI(label_SPChar, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 420);
		setupLabelUI(label_validUsername, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 470);
		
		// Establish an error message for the password, left aligned
		label_errPassword.setTextFill(Color.RED);
		label_errPassword.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelUI(label_errPassword, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 300);	
				
		// Error Message components for the Password
		errPasswordPart1.setFill(Color.BLACK);		// The user input is copied for this part
	    errPasswordPart1.setFont(Font.font("Cascadia Code", FontPosture.REGULAR, 18));
	    
	    errPasswordPart2.setFill(Color.RED);		// A red up arrow is added next
	    errPasswordPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		
	    errPassword = new TextFlow(errPasswordPart1, errPasswordPart2);
		errPassword.setMinWidth(10);	// The two parts are merged into one
		errPassword.setLayoutX(62);					// and positioned directly below the text
		errPassword.setLayoutY(300);					// input field
		
		setupLabelUI(errPasswordPart3, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 330);
													// Position the composition object on the window
		setupLabelUI(validPassword, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 480);
		
		setupLabelUI(label_Requirements, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 360);
		setupLabelUI(label_UpperCase, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 390);
		setupLabelUI(label_LowerCase, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 420);
		setupLabelUI(label_NumericDigit, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 450);
		setupLabelUI(label_SpecialChar, "Arial", 16, width, Pos.BASELINE_LEFT, 400, 390);
		setupLabelUI(label_LongEnough, "Arial", 16, width, Pos.BASELINE_LEFT, 400, 420);
		setupLabelUI(label_TooLong, "Arial", 16, width, Pos.BASELINE_LEFT, 400, 450);
		
		// Label to display the Passwords do not match error message
		setupLabelUI(label_PasswordsDoNotMatch, "Arial", 18, width, Pos.CENTER, 0, 300);

		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 520);
		button_Quit.setOnAction((event) -> {ControllerFirstAdmin.performQuit(); });

		// Place all of the just-initialized GUI elements into the pane
		theRootPane.getChildren().addAll(label_ApplicationTitle, label_TitleLine1,
				label_TitleLine2, text_AdminUsername, text_AdminPassword1, 
				text_AdminPassword2, button_AdminSetup, label_PasswordsDoNotMatch,
				button_Quit, label_Alphabetic, label_validUsername, label_SPChar, 
				label_UNRequirements, label_UNTooLong, label_UNLongEnough, 
				label_errPassword, errPassword, errPasswordPart3, validPassword,
				label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
				label_SpecialChar, label_LongEnough, label_TooLong);
	}
	
	
	//Reset Username Assessments
		static protected void resetUsernameDisplay() {
			label_UNRequirements.setText("");
			label_UNTooLong.setText("");
			label_UNLongEnough.setText("");
		    label_Alphabetic.setText("");
		    label_validUsername.setText("");
		    label_SPChar.setText("");
		}
	
	//Reset Password Assessments
	static protected void resetPasswordDisplay() {
		errPasswordPart1.setText("");
		errPasswordPart2.setText("");
		errPasswordPart3.setText("");
		validPassword.setText("");
		label_Requirements.setText("");
	    label_UpperCase.setText("");
	    label_LowerCase.setText("");
	    label_NumericDigit.setText("");
	    label_SpecialChar.setText("");
	    label_LongEnough.setText("");
	    label_TooLong.setText("");
	}
	
	//Reset Comparison Labels
	static protected void resetComparisonDisplay() {
		label_PasswordsDoNotMatch.setText("");
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
	 * @param w		The width of the Label
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */

	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
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
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	
	/**********
	 * Private local method to initialize the standard fields for a text field
	 * 
	 * @param t		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		The flag (Boolean) that specifies if this field is editable
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, 
			boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	
}