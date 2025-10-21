package guiNewAccount;
import javafx.scene.paint.Color;

/*******
 * <p> Title: ModelNewAccount Class. </p>
 * 
 * <p> Description: The NewAccount Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting role information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */
public class ModelNewAccount {
	/**** Username  *****/
	// Username Variables
	public static String userNameRecognizerErrorMessage = ""; // The error message text
	public static String userNameRecognizerInput = "";        // The input being processed
	public static int userNameRecognizerIndexofError = -1;    // The index of error location
	private static int state = 0;                             // The current state value
	private static int nextState = 0;                         // The next state value
	private static boolean finalState = false;                // Is this state a final state?
	private static String unInputLine = "";                   // The input line
	private static char unCurrentChar;                        // The current character in the line
	private static int unCurrentCharNdx;                      // The index of the current character
	
	public static boolean foundAlphabetic = false;            // The conditions for the username
	public static boolean foundSPChar = false;
	public static boolean foundUNTooLong = false;
	public static boolean foundUNLongEnough = false;
	
	private static boolean unRunning; // The flag that specifies if the FSM is running
	private static int userNameSize = 0; // A numeric value may not exceed 16 characters
	
	private static boolean validUsername; // Boolean condition to check if the username is valid
	
	
	// Code to check Username 
	protected static void checkUsername() {
		ViewNewAccount.resetUsernameDisplay();   //resets Username Display
		String username = ViewNewAccount.text_Username.getText(); // Fetches input from text input for username
		// If the input is empty, clear the display
		if (username.isEmpty()) {
			ViewNewAccount.resetUsernameDisplay();  // Reset the password requirements to be blank
		} else { 
		// Reset the boolean values
		foundAlphabetic = false;
		foundSPChar = false;
		foundUNLongEnough = false;
		foundUNTooLong = false;
		validUsername = false;
		// If there is input, evaluate the username
		evaluateUsername(username);
		//Based on the username, update flags
		updateUNFlags();
		}
	}
	
	public static String evaluateUsername(String input) {
		// The local variables used to perform the Finite State Machine simulation
		state = 0;                       // This is the FSM state number
		unInputLine = input;             // Save the reference to the input line as a global
		unCurrentCharNdx = 0;            // The index of the current character
		unCurrentChar = input.charAt(0); // The current character from above indexed position
		
		// The Finite State Machines continues until the end of the input is reached or at some
		// state the current character does not match any valid transition to a next state
		
		userNameRecognizerInput = input; // Save a copy of the input
		unRunning = true; // Start the loop
		nextState = -1; // There is no next state
		
		// Check if the password is of the correct length
			if(input.length() < 4) {
				foundUNLongEnough = false;
			} else if (input.length() > 16) {
				foundUNTooLong = true;
				foundUNLongEnough = true;
			} else {
				foundUNLongEnough = true;
				foundUNTooLong = false;
			}
	
		userNameSize = 0; // Initialize the UserName size
		// The Finite State Machines continues until the end of the input is reached or at some
		// state the current character does not match any valid transition to a next state		
		while (unRunning) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a next state
			switch (state) {
			case 0:
				// State 0 has 1 valid transition that is addressed by an if statement.
				// The current character is checked against A-Z, a-z, 0-9. If any are matched
				// the FSM goes to state 1
			
				// A-Z, a-z-> State 1
				if ((unCurrentChar >= 'A' && unCurrentChar <= 'Z') || // Check for A-Z
					(unCurrentChar >= 'a' && unCurrentChar <= 'z')) { // Check for a-z
					// Do Not Check for 0-9
					nextState = 1;
					foundAlphabetic = true;
			
					// Count the character
					userNameSize++;
			
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else {
					unRunning = false;
					foundAlphabetic = false;
				}
				// The execution of this state is finished
				break;
			
			case 1:
				// State 1 has two valid transitions,
				// 1: a A-Z, a-z, 0-9 that transitions back to state 1
				// 2: a period that transitions to state 2
			
				// A-Z, a-z, 0-9 -> State 1
				if ((unCurrentChar  >= 'A' && unCurrentChar  <= 'Z') || // Check for A-Z
						(unCurrentChar  >= 'a' && unCurrentChar  <= 'z') || // Check for a-z
						(unCurrentChar  >= '0' && unCurrentChar  <= '9')) { // Check for 0-9
					nextState = 1;
			
					// Count the character
					userNameSize++;
				}
				// . -> State 2
				else if ((unCurrentChar  == '.') || // Check for .
						(unCurrentChar  == '-') || // Check for -
						(unCurrentChar  == '_')) { // Check for _
					nextState = 2;
			
					// Count the .
					userNameSize++;
				}
				// If it is none of those characters, the FSM halts
				else
					unRunning = false;
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					unRunning = false;
					break;
			
			case 2:
				// State 2 deals with a character after a period in the name.
			
				// A-Z, a-z, 0-9 -> State 1
				if ((unCurrentChar  >= 'A' && unCurrentChar  <= 'Z') || // Check for A-Z
						(unCurrentChar  >= 'a' && unCurrentChar  <= 'z') || // Check for a-z
						(unCurrentChar  >= '0' && unCurrentChar  <= '9')) { // Check for 0-9
					nextState = 1;
			
					// Count the odd digit
					userNameSize++;
			
				}
				// If it is none of those characters, the FSM halts
				else
					unRunning = false;
			
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					unRunning = false;
				break;
			}
			
			if (unRunning) {
				//displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar. If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();
			
				// Move to the next state
				state = nextState;
			
				// Is the new state a final state? If so, signal this fact.
				if (state == 1)
					finalState = true;
			
				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
				// Should the FSM get here, the loop starts again
				
		}
			updateUNFlags();
			//displayDebuggingInfo();
			
			System.out.println("The loop has ended.");
			//This takes the final state of the password and  determines if it is valid or not
			switch (state) {
			case 0:
				// State 0 is not a final state, so we can return a very specific error message
				userNameRecognizerErrorMessage += "A UserName must start with A-Z or a-z.\n";
				return userNameRecognizerErrorMessage;
			
			case 1:
				// State 1 is a final state. Check to see if the UserName length is valid. If so
				// we must ensure the whole string has been consumed.
				
				if (userNameSize < 4) {
					// UserName is too small
					userNameRecognizerErrorMessage += "A UserName must have at least 4 characters.\n";
					return userNameRecognizerErrorMessage;
				} else if (userNameSize > 16) {
					// UserName is too long
					userNameRecognizerErrorMessage += "A UserName must have no more than 16 characters.\n";
					return userNameRecognizerErrorMessage;
				} else if (unCurrentCharNdx < input.length()) {
					// There are characters remaining in the input, so the input is not valid
					userNameRecognizerErrorMessage += "A UserName character may only contain the characters A-Z, a-z, 0-9.\n";
					return userNameRecognizerErrorMessage;
				} else {
					// UserName is valid, update conditions to match
					userNameRecognizerIndexofError = -1;
					userNameRecognizerErrorMessage = "";
					validUsername = true;
					updateUNFlags();
					adminButtonEnabled();
					return userNameRecognizerErrorMessage;
				}
			
			case 2:
				// State 2 is not a final state, so we can return a very specific error message
				userNameRecognizerErrorMessage += "A UserName character after a period must be A-Z, a-z, 0-9.\n";
				foundSPChar = true;
				return userNameRecognizerErrorMessage;
			
			default:
				// This is for the case where we have a state that is outside of the valid range.
				// This should not happen
				return "";
			}
		}
		
	// Moves the Username to the next character for the while loop
		private static void moveToNextCharacter() {
			unCurrentCharNdx++;
			if (unCurrentCharNdx < unInputLine.length())
				unCurrentChar =unInputLine.charAt(unCurrentCharNdx);
			else {
				unCurrentChar = ' ';
				unRunning = false;
			}
	}
	
		// Update the displayed requirements based on currently satisfied requirements
	private static void updateUNFlags() {
		ViewNewAccount.resetPasswordDisplay();
		ViewNewAccount.resetComparisonDisplay();
		ViewNewAccount.label_UNRequirements.setText("These are the Username requirements: ");
		ViewNewAccount.label_UNRequirements.setTextFill(Color.BLACK);
		if (foundAlphabetic) {
			ViewNewAccount.label_Alphabetic.setText("The First Character Must be a letter (A-Z or a-z) - Satisfied");
			ViewNewAccount.label_Alphabetic.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_Alphabetic.setText("The First Character Must be a letter (A-Z or a-z) - Not Satisified");
			ViewNewAccount.label_Alphabetic.setTextFill(Color.RED);
		}
		if (foundSPChar) {
			ViewNewAccount.label_SPChar.setText("The Username may contain a period, dash, or underscore (.,-,_)\n"
				+ "if it is followed by an alphanumerica character (A-Z, a-z, 0-9) - Not Satisified");
			ViewNewAccount.label_SPChar.setTextFill(Color.RED);
		} else {
			ViewNewAccount.label_SPChar.setText("The Username may contain a period, dash, or underscore (.,-,_)\n"
				+ "if it is followed by an alphanumerica character (A-Z, a-z, 0-9) - Satisified");
			ViewNewAccount.label_SPChar.setTextFill(Color.GREEN);
		}
		if (foundUNLongEnough) {
			ViewNewAccount.label_UNLongEnough.setText("At least four characters - Satisfied");
			ViewNewAccount.label_UNLongEnough.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_UNLongEnough.setText("At least four characters - Not Satisfied");
			ViewNewAccount.label_UNLongEnough.setTextFill(Color.RED);
		}
		if (foundUNTooLong) {
			ViewNewAccount.label_UNTooLong.setText("At most sixteen characters - Not Satisfied");
			ViewNewAccount.label_UNTooLong.setTextFill(Color.RED);
		} else {
			ViewNewAccount.label_UNTooLong.setText("At most sixteen characters - Satisfied");
			ViewNewAccount.label_UNTooLong.setTextFill(Color.GREEN);
		}
		if (validUsername) {
			ViewNewAccount.label_validUsername.setText("The Username is valid!");
			ViewNewAccount.label_validUsername.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_validUsername.setText("The Username is not valid!");
			ViewNewAccount.label_validUsername.setTextFill(Color.RED);
		}
	}
	
	
					/**** Password  *****/
	// Password Variables
	public static String pwErrorMessage = ""; // The error message text
	public static String pwInput = "";        // The input being processed
	private static String pwInputLine = "";   // The input line
	public static int pwIndexofError = -1;    // The index where the error was located
	private static char pwCurrentChar;        // The current character in the line
	private static int pwCurrentCharNdx;      // The index of the current character
	
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean foundTooLong = false; // Added code for too long passwords
	
	private static boolean pwRunning; // The flag that specifies if the FSM is running
	private static boolean validPassword;
	
	// Code to check Password 
	protected static void checkPassword() {
		ViewNewAccount.resetPasswordDisplay();   //resets Password Display
		String password = ViewNewAccount.text_Password1.getText(); // Fetches input from text input for password
		// If the input is empty, clear the display
		if (password.isEmpty()) {
			ViewNewAccount.resetPasswordDisplay();  // Reset the password requirements to be blank
		} else { 
		// If there is input, evaluate the passwords
		String errMessage = evaluatePassword(password);
		//Based on the password, update flags
		updatePWFlags();
		
		// If the errMessage is not empty, it means there are errors
		// and the password is invalid
		if (errMessage != "") { 
			// Display to console, so you know the error.
			System.out.println(errMessage);
			
			// Then set up the error, so the user knows what to correct
			// The part of the password that has no error
			ViewNewAccount.errPasswordPart1.setText(password.substring(0, pwIndexofError));
			// The red arrow
			ViewNewAccount.errPasswordPart2.setText("\u21EB");
			// Tell the user about the meaning of the red up arrow
			ViewNewAccount.errPasswordPart3.setText("The red arrow points at the character causing the error!");
			// Tell the user that the password is not valid with a red message
			ViewNewAccount.validPassword.setTextFill(Color.RED);
			ViewNewAccount.validPassword.setText("Failure! The password is not valid.");
		} else {                  // Display for if the password is valid
			// All the requirements were satisfied - the password is valid
			System.out.println("Success! The password satisfies the requirements.");
			
			// Hide all of the error messages elements
			updatePWFlags();
			ViewNewAccount.errPasswordPart1.setText("");
			ViewNewAccount.errPasswordPart2.setText("");
			ViewNewAccount.errPasswordPart3.setText("");
			// Tell the user that the password is valid with a green message
			ViewNewAccount.validPassword.setTextFill(Color.GREEN);
			ViewNewAccount.validPassword.setText("Success! The password satisfies the requirements.");
		
			// Test if all inputs are valid and check to enable the sign up button
			adminButtonEnabled();
		}	}	
		updatePWFlags();
	}
	
	//This code evaluates the password
	public static String evaluatePassword(String input) { 
		// The following are the local variable used to perform the Directed Graph simulation
		pwErrorMessage = "";   // Initialize no current errors
		pwIndexofError = 0;    // Initialize the IndexofError
		pwInputLine = input;   // Save the reference to the input line as a global
		pwCurrentCharNdx = 0;  // The index of the current character
		
		// Check if there is a password
		if (input.length() <= 0) {
			return "*** Error *** The password is empty!";
		}
		// Else the password is not empty, and we can access the first character
		pwCurrentChar = input.charAt(0); // The current character from the above indexed position
		
		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state. This 
		// local variable is a working copy of the input.
		pwInput = input; // Save a copy of the input
		
		// The following are the attributes associated with each of the requirements
		//Reset the Boolean Flags
		foundUpperCase = false; 
		foundLowerCase = false; 
		foundNumericDigit = false; 
		foundSpecialChar = false; 
		foundNumericDigit = false; 
		foundLongEnough = false; 
		foundTooLong = false; 
		
		// This flag determines whether the directed graph (FSM) loop is operating or not
		pwRunning = true; // Start the loop
		// The Directed Graph simulation continues until the end of the input is reached or 
		// at some state the current character does not match any valid transition
		while (pwRunning) {
		//displayInputState();
			// The cascading if statement sequentially tries the current character against 
			// all of the valid transitions, each associated with one of the requirements
			if (pwCurrentChar >= 'A' && pwCurrentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (pwCurrentChar >= 'a' && pwCurrentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (pwCurrentChar >= '0' && pwCurrentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(pwCurrentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else {
				pwIndexofError = pwCurrentCharNdx;
				return "*** Error *** An invalid character has been found!";
			}
			if (pwCurrentCharNdx >= 7) {
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			// Added Code for identifying if the password is too long
			if (pwCurrentCharNdx > 32) {
				System.out.println("More than 32 characters found");
				foundTooLong = true;
			}
		
			// Go to the next character if there is one
			// Runs through the loop for every char in the password, 
			// every time the password is updated
			pwCurrentCharNdx++;
			if (pwCurrentCharNdx >= pwInputLine.length())
				pwRunning = false;
			else
				pwCurrentChar = input.charAt(pwCurrentCharNdx);
			
			System.out.println();
		}
		// Construct the error message if there is one
		String errMessage = "";
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
		if (!foundSpecialChar)
			errMessage += "Special character; ";
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		// Add Error Message for Too Long
		if (foundTooLong)
			errMessage += "Too Long; ";
		if (errMessage == "") {
			validPassword = true;
			return "";
		}
		
		// If it gets here, there something was not found, so return an appropriate
		// message
		pwIndexofError = pwCurrentCharNdx;
		return errMessage + "conditions were not satisfied";
	}
	
	
	// Code that controls Password Requirements Display
	private static void updatePWFlags() {
		ViewNewAccount.resetUsernameDisplay();
		ViewNewAccount.resetComparisonDisplay();
		ViewNewAccount.label_Requirements.setText("These are the pasword requirements: ");
		ViewNewAccount.label_Requirements.setTextFill(Color.BLACK);
		if (foundUpperCase) {
			ViewNewAccount.label_UpperCase.setText("At least one upper case letter - Satisfied");
			ViewNewAccount.label_UpperCase.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_UpperCase.setText("At least one upper case letter - Not Satisfied");
			ViewNewAccount.label_UpperCase.setTextFill(Color.RED);
		}
		if (foundLowerCase) {
			ViewNewAccount.label_LowerCase.setText("At least one lower case letter - Satisfied");
			ViewNewAccount.label_LowerCase.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_LowerCase.setText("At least one lower case letter - Not Satisfied");
			ViewNewAccount.label_LowerCase.setTextFill(Color.RED);
		}
		if (foundNumericDigit) {
			ViewNewAccount.label_NumericDigit.setText("At least one numeric digit - Satisfied");
			ViewNewAccount.label_NumericDigit.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_NumericDigit.setText("At least one numeric digit - Not Satisfied");
			ViewNewAccount.label_NumericDigit.setTextFill(Color.RED);
		}
		if (foundSpecialChar) {
			ViewNewAccount.label_SpecialChar.setText("At least one special character - Satisfied");
			ViewNewAccount.label_SpecialChar.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_SpecialChar.setText("At least one special character - Not Satisfied");
			ViewNewAccount.label_SpecialChar.setTextFill(Color.RED);
		}
		if (foundLongEnough) {
			ViewNewAccount.label_LongEnough.setText("At least eight characters - Satisfied");
			ViewNewAccount.label_LongEnough.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_LongEnough.setText("At least eight characters - Not Satisfied");
			ViewNewAccount.label_LongEnough.setTextFill(Color.RED);
		}
		if (foundTooLong) {
			ViewNewAccount.label_TooLong.setText("At most thirty-two characters - Not Satisfied");
			ViewNewAccount.label_TooLong.setTextFill(Color.RED);
		} else {
			ViewNewAccount.label_TooLong.setText("At most thirty-two characters - Satisfied");
			ViewNewAccount.label_TooLong.setTextFill(Color.GREEN);
		}
	}
	
	
	
	
	
					/**** Password Comparison  *****/
	// Comparison Variables
	private static int currentCharNdx;
	private static char password1Char;
	private static char password2Char;
	private static boolean running;
	
	
	// Code to check Comparison 
		protected static boolean comparePasswords() {
		currentCharNdx = 0;
		String password1 = ViewNewAccount.text_Password1.getText();
		String password2 = ViewNewAccount.text_Password2.getText();
		// If either passwords is not provided, do not compare them
			if(password1.length() <= 0 || password2.length() <= 0)
				return false;
		ViewNewAccount.resetPasswordDisplay();
		password1Char = password1.charAt(0);
		password2Char = password2.charAt(0);
		running = true;
		if (password1.length() != password2.length()) 
		return false;
		while(running) 
		{
		if (password1Char == password2Char) {
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= password1.length())
				running = false;
			else {
				password1Char = password1.charAt(currentCharNdx);
				password2Char = password2.charAt(currentCharNdx);	
			}	
		} else 
			return false;
		}	
		return true;
		}
		
		// Code that controls Comparison Requirements Display
		protected static void updateComparisonFlags() {
			ViewNewAccount.resetUsernameDisplay();
			ViewNewAccount.resetComparisonDisplay();
		if(comparePasswords()) {
			ViewNewAccount.label_PasswordsDoNotMatch.setText("The passwords must match! - Satisified");
			ViewNewAccount.label_PasswordsDoNotMatch.setTextFill(Color.GREEN);
		} else {
			ViewNewAccount.label_PasswordsDoNotMatch.setText("The passwords must match! - Not Satisified");
			ViewNewAccount.label_PasswordsDoNotMatch.setTextFill(Color.RED);
		}
	}
	
	
	
	public static void adminButtonEnabled() {
		if (validUsername && validPassword && comparePasswords()) {
			ViewNewAccount.button_UserSetup.setDisable(false); 
		return;
		} else {
		System.out.println("Not Valid");
			ViewNewAccount.button_UserSetup.setDisable(true); 
		return;
		}
	}
}
