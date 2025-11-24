package guiStudentNotes;

import java.sql.SQLException;

import entityClasses.StudentNote;
import entityClasses.User;

/*******
 * <p> Title: ModelStudentNotes Class. </p>
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

public class ModelStudentNotes {
	/**
	 * Default constructor for ControllerDiscussion.
	 * Initializes the controller with default values and no special setup.
	 */
	public ModelStudentNotes() {
		// No initialization required at this time
	}

	protected static void registerNote(String student, User staff, String noteText) {
		StudentNote note = new StudentNote(student, staff.getUserName(), noteText, false);
		try {
			applicationMain.FoundationsMain.database.register(note);
			} catch (SQLException e) {
				System.out.println("Failed to register note.");
				System.exit(0);
			}
	}
}