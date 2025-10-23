package guiManageInvites;

import java.util.Optional;

import database.Database;
import guiAdminHome.ViewAdminHome;
import guiDeleteUser.ViewDeleteUser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ControllerManageInvites {

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
		String emailAddress = ViewManageInvites.text_InvitationEmailAddress.getText();
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
			ViewManageInvites.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewManageInvites.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewManageInvites.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String deadline = theDatabase.getDeadline(invitationCode);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress + "\nThe deadline is: " + deadline;
		System.out.println(msg);
		ViewManageInvites.alertEmailSent.setContentText(msg);
		ViewManageInvites.alertEmailSent.showAndWait();
		ViewManageInvites.refreshInvites();

		
	}
	
	/**********
	 * <p> Method: performDeleteInvite() </p>
	 * 
	 * <p> Description: This method adds a new role to the list of role in the ComboBox select
	 * list. </p>
	 * 
	 */
	protected static void performDeleteInvite() {

		// Create alert window to confirm user choice
		Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDialog.setTitle("Confirm Deletion");
		confirmDialog.setHeaderText("Are you sure you want to delete this invitation?");
		confirmDialog.setContentText("This action cannot be undone.");

		// Show the dialog and capture the result
		Optional<ButtonType> result = confirmDialog.showAndWait();

		String code = "";
		code = ViewManageInvites.theSelectedInvite.substring(0,6);
		
		
		// If confirmed, delete account
		if (result.isPresent() && result.get() == ButtonType.OK) {
			theDatabase.deleteInvitationCode(code);
			ViewManageInvites.refreshInvites();

			
		}

		ViewManageInvites.theSelectedInvite = "<Select an Invite>";

	}
	
	
	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectInvite() {
		ViewManageInvites.theSelectedInvite = 
				(String) ViewManageInvites.combobox_SelectInvite.getValue();
		theDatabase.getUserAccountDetails(ViewManageInvites.theSelectedInvite);
		setupDeleteButton();

	}
	
	/**********
	 * <p> Method: setupDeleteButton() </p>
	 * 
	 * <p> Description: This method fetches the current values for the widgets whose values change
	 * based on which user has been selected and any actions that the admin takes. </p>
	 * 
	 */
	private static void setupDeleteButton() {

		// Disable button if no user is selected
		if (ViewManageInvites.theSelectedInvite != null &&
				ViewManageInvites.theSelectedInvite.compareTo("<Select an Invite>") != 0) {
			ViewManageInvites.button_DeleteInvite.setDisable(false);
		}
		else
			ViewManageInvites.button_DeleteInvite.setDisable(true);

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
			ViewManageInvites.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewManageInvites.alertEmailError.showAndWait();
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
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewManageInvites.theStage,
				ViewManageInvites.theUser);
	}


	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewManageInvites.theStage);
	}


	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}

