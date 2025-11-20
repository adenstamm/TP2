package guiAdminHome;

import database.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	
	// This alert is used should the invitation code be invalid
    protected static Alert alertEmailIsInvalid = new Alert(AlertType.INFORMATION);
    
	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Call for the deletion of expired codes, that way if it is expired, you can send it again
		if(theDatabase.getNumberOfInvitations() > 0) {
			theDatabase.findExpiredCodes(); 
			System.out.println("deleting expired codes");
		}
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			// If the email is invalid, send a message
			alertEmailIsInvalid.setTitle("Invalid Email");
			alertEmailIsInvalid.setHeaderText("The email is is not valid.");
			alertEmailIsInvalid.setContentText("Correct the email and try again.");
			alertEmailIsInvalid.showAndWait();	
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String deadline = theDatabase.getDeadline(invitationCode);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress + "\nThe deadline is: " + deadline;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
		ViewAdminHome.label_NumberOfUsers.setText("Number of users: " + 
				theDatabase.getNumberOfUsers());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		/* System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();*/
		guiManageInvites.ViewManageInvites.displayManageInvites(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Assigns the user a one time password and shows the admin which password it is. </p>
	 */
	protected static void setOnetimePassword () {
		// Create a dialog to select user and set one-time password
	    javafx.scene.control.TextInputDialog userDialog = new javafx.scene.control.TextInputDialog();
	    userDialog.setTitle("Set One time Password");
	    userDialog.setHeaderText("Enter username to set one time password for:");
	    userDialog.setContentText("Username:");
	    
	    java.util.Optional<String> userResult = userDialog.showAndWait();
	    if (userResult.isPresent() && !userResult.get().isEmpty()) {
	        String username = userResult.get();
	        
	        // Check if user exists
	        if (!theDatabase.doesUserExist(username)) {
	            ViewAdminHome.alertNotImplemented.setTitle("Error");
	            ViewAdminHome.alertNotImplemented.setHeaderText("User Not Found");
	            ViewAdminHome.alertNotImplemented.setContentText("User '" + username + "' does not exist.");
	            ViewAdminHome.alertNotImplemented.showAndWait();
	            return;
	        }
	        
	        String oneTimePassword = generateRandomPassword();
	        
	        theDatabase.setOneTimePassword(username, oneTimePassword);
	        
	        // Show the one-time password to admin
	        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
	        alert.setTitle("One-Time Password Set");
	        alert.setHeaderText("One-time password has been set for user: " + username);
	        alert.setContentText("One-time password: " + oneTimePassword + 
	            "\n\nPlease provide this password to the user. They will be required to change it on first login.");
	        alert.showAndWait();
	        
	        System.out.println("One-time password set for user: " + username + " -> " + oneTimePassword);
	    }
	}	
	
	// Generates a random password
	private static String generateRandomPassword() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder password = new StringBuilder();
	    java.util.Random random = new java.util.Random();
	    
	    for (int i = 0; i < 8; i++) {
	        password.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    
	    return password.toString();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {
		/*
		System.out.println("\n*** WARNING ***: Delete User Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Delete User Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Delete User Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
		*/
		guiDeleteUser.ViewDeleteUser.displayDeleteUser(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	protected static void goToDiscussion() {
		
		guiDiscussion.ViewDiscussion.displayDiscussion(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
//		System.out.println("\n*** WARNING ***: List Users Not Yet Implemented");
//		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
//		ViewAdminHome.alertNotImplemented.setHeaderText("List User Issue");
//		ViewAdminHome.alertNotImplemented.setContentText("List Users Not Yet Implemented");
//		ViewAdminHome.alertNotImplemented.showAndWait();
		guiListUsers.ViewListUsers.displayListUsers(ViewAdminHome.theStage, ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  </p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 * 
	 * @return boolean value if email is valid or not true if invalid
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		System.out.println(checkEmailAddress(emailAddress));
		if(checkEmailAddress(emailAddress) == "") {
			return false;
		}
		return true;
	}
	//this is the variety of variables needed for checking the email address
	public static String emailAddressErrorMessage = "";	// The error message text
	public static String emailAddressInput = "";		// The input being processed
	public static int emailAddressIndexofError = -1;	// The index where the error was located
	private static int state = 0;						// The current state value
	private static int nextState = 0;					// The next state value
	private static boolean finalState = false;			// Is this state a final state?
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running
	private static int domainPartCounter = 0;			// A domain name may not exceed 63 characters
	/**********
	 * <p> 
	 * 
	 * Title: emailMoveToNextCharacter () Method. </p>
	 * 
	 * <p> Description: moves to the next character when checking the email provided  </p>
	 * 
	 */
	private static void emailMoveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			System.out.println("End of input was found!");
			currentChar = ' ';
			running = false;
		}
	}
	/**********
	 * <p> 
	 * 
	 * Title: displayInput(String input, int currentCharNdx) Method. </p>
	 * 
	 * <p> Description: displays the input from the email checker for invalid errors  </p>
	 * 
	 */
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input.substring(0,currentCharNdx) + "?\n";

		return result;
	}

	/**********
	 * <p> 
	 * 
	 * Title: displayDebuggingInfo() Method. </p>
	 * 
	 * <p> Description: displays debugging info for the email  </p>
	 * 
	 */
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
					nextState + "     " + domainPartCounter);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: checkEmailAddress(String input) Method. </p>
	 * 
	 * <p> Description: goes character by character to check if
	 *  the email inputed is a valid one  </p>
	 *  
	 *  @param String input this is the email its checking
	 *  
	 *  @return return an error message or "" if email is valid
	 * 
	 */
	public static String checkEmailAddress(String input) {
		// The following are the local variable used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailAddressInput = input;			// Save a copy of the input

		// Let's ensure there is input
		if (input.length() <= 0) {
			emailAddressErrorMessage = "There was no email address found.\n";
			return emailAddressErrorMessage + displayInput(input, 0);
		}
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// Let's ensure the address is not too long
		if (input.length() > 255) {
			emailAddressErrorMessage = "A valid email address must be no more than 255 characters.\n";
			return emailAddressErrorMessage + displayInput(input, 255);
		}
		running = true;						// Start the loop
		System.out.println("\nCurrent Final Input  Next  DomainName\nState   State Char  State  Size");

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			nextState = -1;						// Default to there is no next state		
			
			switch (state) {
			case 0: 
				// State 0 has just 1 valid transition.
				// The current character is must be checked against 62 options. If any are matched
				// the FSM must go to state 1
				// The first and the second check for an alphabet character the third a numeric
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;
				}
								
				// If it is none of those characters, the FSM halts
				else { 
					running = false;
				}
				
				break;				
				// The execution of this state is finished
			
			case 1: 
				// State 1 has three valid transitions.  

				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 1;                                         // Loop to 1
				} else if (currentChar == '@'){                         // @
					nextState = 2;                                         // Forward to 2
				} else if (currentChar == '.' || currentChar == '-' ||
						currentChar == '_'){                            // . , -, _
					nextState = 0;                                         // Back to 0
				} else { 
					running = false;
				}
				
				break;
				// The execution of this state is finished
							
			case 2: 
				// State 2 has one valid transition.
				
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;                                        // Go to final state 3
				}
								
				// If it is none of those characters, the FSM halts
				else { 
					running = false;
				}

				// The execution of this state is finished
				break;
	
			case 3:
				// State 3 has three valid transition.
				
				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;                                         // Loop back to 3
				} else if (currentChar == '-'){                         // -
					nextState = 4;                                          // Forwards to 4
				} else if (currentChar == '.'){                         // .  
					nextState = 2;                                          //Back to 2
				} else { 
					running = false;
				}

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has one valid transition.

				if ((currentChar >= 'A' && currentChar <= 'Z')|| 		// Upper case
						(currentChar >= 'a' && currentChar <= 'z') ||	// Lower case
						(currentChar >= '0' && currentChar <= '9')) {	// Digit
					nextState = 3;                                        // Go to final state 3
				}
								
				else { 
					running = false;
				}


				// The execution of this state is finished
				break;

			}
			
			if (running) {
				//displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next character
				// in the input and if there is one, it fetches that character and updates the 
				// currentChar.  If there is no next character the currentChar is set to a blank.
				
				emailMoveToNextCharacter();
				
				// Move to the next state
				state = nextState;
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again

		}
		//displayDebuggingInfo();
		
		System.out.println("The loop has ended.");

		emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
			emailAddressErrorMessage = "May only be alphanumberic.\n";
			return emailAddressErrorMessage;

		case 1:
			// State 1 is not a final state, so we can return a very specific error message

			emailAddressIndexofError = currentCharNdx;		
			emailAddressErrorMessage = "May be alphanumberic, '.' or '@'.\n";
			return emailAddressErrorMessage;

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
						
			emailAddressIndexofError = currentCharNdx;		
			emailAddressErrorMessage = "May only be alphanumberic.\n";
			return emailAddressErrorMessage;

		case 3:
			// State 3 is a Final State, so this is not an error if the input is empty, otherwise
			// we can return a very specific error message.

			if (currentCharNdx<input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				emailAddressIndexofError = currentCharNdx;		// Copy the index of the current character;
				emailAddressErrorMessage = "This must be the end of the input.\n";
				return emailAddressErrorMessage + displayInput(input, currentCharNdx);
			}
			else 
			{
				emailAddressIndexofError = -1;
				emailAddressErrorMessage = "";
				return emailAddressErrorMessage;
			}

		case 4:
			// State 4 is not a final state, so we can return a very specific error message. 

			emailAddressIndexofError = currentCharNdx;		
			emailAddressErrorMessage = "May only be alphanumberic.\n";
			return emailAddressErrorMessage;
			
		default:
			return "";
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
