package guiUserUpdate;

import entityClasses.User;
import guiManageInvites.ViewManageInvites;
import javafx.stage.Stage;

public class ControllerUserUpdate {
	/*-********************************************************************************************

	The Controller for ViewUserUpdate 
	
	**********************************************************************************************/

	/**********
	 * <p> Title: ControllerUserUpdate Class</p>
	 * 
	 * <p> Description: This static class supports the actions initiated by the ViewUserUpdate
	 * class. In this case, there is just one method, no constructors, and no attributes.</p>
	 *
	 */

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	**********************************************************************************************/

	
	/**********
	 * <p> Method: public goToUserHomePage(Stage theStage, User theUser) </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the button to
	 * proceed to the user's home page.
	 * 
	 * @param theStage specifies the JavaFX Stage for next next GUI page and it's methods
	 * 
	 * @param theUser specifies the user so we go to the right page and so the right information
	 */
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiStudent.ViewStudentHome.displayStudentHome(theStage, theUser);
			break;
		case 3:
			guiStaff.ViewStaffHome.displayStaffHome(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
	protected static void updatePassword(Stage theStage, User theUser) {
	    String newPassword = ViewUserUpdate.text_NewPassword.getText();
	    String confirmPassword = ViewUserUpdate.text_ConfirmPassword.getText();
	    
	    // Validate password
	    if (newPassword.isEmpty()) {
	        ViewUserUpdate.alertPasswordError.setContentText("Password cannot be empty.");
	        ViewUserUpdate.alertPasswordError.showAndWait();
	        return;
	    }
	    
	    if (!newPassword.equals(confirmPassword)) {
	        ViewUserUpdate.alertPasswordError.setContentText("Passwords do not match.");
	        ViewUserUpdate.alertPasswordError.showAndWait();
	        return;
	    }
	    
	    applicationMain.FoundationsMain.database.updatePassword(theUser.getUserName(), newPassword);
	    
	    // Show success message
	    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
	    alert.setTitle("Password Updated");
	    alert.setHeaderText("Password successfully updated");
	    alert.setContentText("Your password has been changed. You can now proceed to your home page.");
	    alert.showAndWait();
	    
	    goToUserHomePage(theStage, theUser);
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
			ViewUserUpdate.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewUserUpdate.alertEmailError.showAndWait();
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
}
