package guiNewAccount;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiFirstAdmin.ModelFirstAdmin;

/*******
 * <p> Title: ViewNewAccount Class. </p>
 * 
 * <p> Description: The ViewNewAccount Page is used to enable a potential user with an invitation
 * code to establish an account after they have specified an invitation code on the standard login
 * page. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-19 Initial version
 *  
 */

public class ViewNewAccount {
	
	/*-********************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// This is a simple GUI login Page, very similar to the FirstAdmin login page.  The only real
	// difference is in this case we also know an email address, since it was used to send the
	// invitation to the potential user.
	private static Label label_ApplicationTitle = 
			new Label("Foundation Application Account Setup Page");
    protected static Label label_NewUserCreation = new Label(" User Account Creation.");
    protected static Label label_NewUserLine = new Label("Please enter a username and a password.");
    protected static TextField text_Username = new TextField();
    protected static PasswordField text_Password1 = new PasswordField();
    protected static PasswordField text_Password2 = new PasswordField();
    protected static Button button_UserSetup = new Button("User Setup");
    protected static TextField text_Invitation = new TextField();

	// This alert is used should the invitation code be invalid
    protected static Alert alertInvitationCodeIsInvalid = new Alert(AlertType.INFORMATION);
    
    // This alert is used should the invitation code be expired
    protected static Alert alertInvitationCodeIsExpired = new Alert(AlertType.INFORMATION);

	// This alert is used should the user enter two passwords that do not match
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

    protected static Button button_Quit = new Button("Quit");

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewNewAccount theView;		// Is instantiation of the class needed?

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User
   
    protected static String theInvitationCode;	// The invitation code links to an email address
    											// and a role for this user
    protected static String emailAddress;		// Established here for use by the controller
    protected static String theRole;			// Established here for use by the controller
	public static Scene theNewAccountScene = null;	// Access to the User Update page's GUI Widgets
	

	/*-********************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayNewAccount(Stage ps, String ic) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the NewAccount page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param ic specifies the user's invitation code for this GUI and it's methods
	 * 
	 */
	public static void displayNewAccount(Stage ps, String ic) {
		// This is the only way some component of the system can cause a New User Account page to
		// appear.  The first time, the class is created and initialized.  Every subsequent call it
		// is reused with only the elements that differ being initialized.
		
		// Establish the references to the GUI and the current user
		theStage = ps;				// Save the reference to the Stage for the rest of this package
		theInvitationCode = ic;		// Establish the invitation code so it can be easily accessed
		
		if (theView == null) theView = new ViewNewAccount();
		
		text_Username.setText("");	// Clear the input fields so previously entered values do not
		text_Password1.setText("");	// appear for a new user
		text_Password2.setText("");
		resetPasswordDisplay();
		resetComparisonDisplay();
		resetUsernameDisplay();
		
		// Fetch the role for this user
		theRole = theDatabase.getRoleGivenAnInvitationCode(theInvitationCode);
		
		if (theRole.length() == 0) {// If there is an issue with the invitation code, display a
			alertInvitationCodeIsInvalid.showAndWait();	// dialog box saying that are when it it
			return;					// acknowledged, return so the proper code can be entered
		}

		// Get the email address associated with the invitation code
		emailAddress = theDatabase.getEmailAddressUsingCode(theInvitationCode);
	
		// Delete expired code and send expiration error
		if(theDatabase.deleteExpiredCode(theInvitationCode)) {
			alertInvitationCodeIsExpired.showAndWait();	// dialog box saying that are when it it
			return;					// ackAdminowledged, return so the proper code can be entered
			//add text so that it says invalid for being out of date
		}
		
		// Delete code after use
		theDatabase.deleteInvitationCode(theInvitationCode);
		
		
    	// Place all of the established GUI elements into the pane
    	theRootPane.getChildren().clear();
    	theRootPane.getChildren().addAll(label_NewUserCreation, label_NewUserLine, text_Username,
    			text_Password1, text_Password2, button_UserSetup, button_Quit, 
    			//Added labels for Requirements
    			label_PasswordsDoNotMatch, label_Alphabetic, label_validUsername, label_SPChar, 
				label_UNRequirements, label_UNTooLong, label_UNLongEnough, 
				label_errPassword, errPassword, errPasswordPart3, validPassword,
				label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
				label_SpecialChar, label_LongEnough, label_TooLong);    	

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: New User Account Setup");	
        theStage.setScene(theNewAccountScene);
		theStage.show();
	}
	
	/**********
	 * <p> Constructor: ViewNewAccount() </p>
	 * 
	 * <p> Description: This constructor is called just once, the first time a new account needs to
	 * be created.  It establishes all of the common GUI widgets for the page so they are only
	 * created once and reused when needed.
	 * 
	 * The do
	 * 		
	 */
	private ViewNewAccount() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theNewAccountScene = new Scene(theRootPane, width, height);

		// Label the Panle with the name of the startup screen, centered at the top of the pane
		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		
    	// Label to display the welcome message for the new user
    	setupLabelUI(label_NewUserCreation, "Arial", 32, width, Pos.CENTER, 0, 10);
	
    	// Label to display the  message for the first user
    	setupLabelUI(label_NewUserLine, "Arial", 24, width, Pos.CENTER, 0, 70);
		
		// Establish the text input operand asking for a username
		setupTextUI(text_Username, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, true);
		text_Username.setPromptText("Enter the Username");
		text_Username.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelNewAccount.checkUsername(); });
		
		// Establish the text input operand field for the password
		setupTextUI(text_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		text_Password1.setPromptText("Enter the Password");
		text_Password1.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelNewAccount.checkPassword(); });
		
		// Establish the text input operand field to confirm the password
		setupTextUI(text_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, true);
		text_Password2.setPromptText("Enter the Password Again");
		text_Password2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelNewAccount.comparePasswords(); });
		text_Password2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelNewAccount.updateComparisonFlags(); });
		text_Password2.textProperty().addListener((observable, oldValue, newValue)
				-> {ModelNewAccount.adminButtonEnabled(); });
		
		// If the invitation code is wrong, this alert dialog will tell the user
		alertInvitationCodeIsInvalid.setTitle("Invalid Invitation Code");
		alertInvitationCodeIsInvalid.setHeaderText("The invitation code is not valid.");
		alertInvitationCodeIsInvalid.setContentText("Correct the code and try again.");

		// If the invitation code is wrong, this alert dialog will tell the user
		alertInvitationCodeIsExpired.setTitle("Invalid Invitation Code: Expired");
		alertInvitationCodeIsExpired.setHeaderText("The invitation code is not valid.");
		alertInvitationCodeIsExpired.setContentText("Correct the code and try again.");
		
		// If the passwords do not match, this alert dialog will tell the user
		alertUsernamePasswordError.setTitle("Passwords Do Not Match");
		alertUsernamePasswordError.setHeaderText("The two passwords must be identical.");
		alertUsernamePasswordError.setContentText("Correct the passwords and try again.");

        // Set up the account creation and login
        setupButtonUI(button_UserSetup, "Dialog", 18, 200, Pos.CENTER, 475, 210);
        button_UserSetup.setOnAction((event) -> {ControllerNewAccount.doCreateUser(); });
		
        // Enable the user to quit the application
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerNewAccount.performQuit(); });
        
        /**** Set up for Labels****/
		//Username
		setupLabelUI(label_UNRequirements, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 300);
		setupLabelUI(label_Alphabetic, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 330);
		setupLabelUI(label_UNTooLong, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 360);
		setupLabelUI(label_UNLongEnough, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 390);
		setupLabelUI(label_SPChar, "Arial", 16, width, Pos.BASELINE_LEFT, 10, 420);
		setupLabelUI(label_validUsername, "Arial", 18, width, Pos.BASELINE_LEFT, 10, 470);
		
		//Password
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
		
		//Comparison
		setupLabelUI(label_PasswordsDoNotMatch, "Arial", 18, width, Pos.CENTER, 0, 300);
	}
		
					/*** Custom Code to clear conditions***/
		//Username
		static protected Label label_Alphabetic = new Label();
		static protected Label label_validUsername = new Label();
		static protected Label label_SPChar = new Label();
		static protected Label label_UNRequirements = new Label();
		static protected Label label_UNTooLong = new Label();
		static protected Label label_UNLongEnough = new Label();
		//Password
		static protected Label label_errPassword = new Label();	// There error labels change based on the
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
		//Comparison
		static protected Label label_PasswordsDoNotMatch = new Label();
		
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
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	
}
