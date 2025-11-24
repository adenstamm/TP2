/*******
 * <p> Title: FoundationsF25 module. </p>
 * 
 * <p> Description: I am not too sure what this does but i need a comment
 * for the javadoc..</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */


module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}
