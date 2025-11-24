package guiAdminRequests;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;
import guiAddRemoveRoles.ViewAddRemoveRoles;
import guiRequestAdminAction.ControllerRequestAdminAction;
import guiRequestAdminAction.ModelRequestAdminAction;
import guiRequestAdminAction.ViewRequestAdminAction;

import java.util.ArrayList;
import java.util.List;

import database.Database;


/**
 * <p> Title: ViewRequestAdminAction Class. </p>
 * <p> Description: The View class creates the user interface for submitting admin requests. </p>
 * <p> Copyright: Weston Benson-Soranson © 2025 </p>
 * @author Weston Benson-Soranson
 * @version 1.00
 */
public class ViewAdminRequests {

    // Attributes required by the user interface
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
    
    protected static Label titleLabel = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    // UI Widgets
    protected static VBox mainContainer;
    protected static Label instructionLabel;
    protected static ComboBox<String> combobox_requestSelect = new ComboBox<>();
    protected static Button button_addDocs = new Button("Add Documentation");
    protected static Button button_viewDocs = new Button("View Documentation");
    protected static Button button_originalRequest = new Button("Go to Original");
    protected static Button button_back = new Button("Back");
    protected static VBox vbox_docs = new VBox(10);
    protected static ScrollPane scroll_docs = new ScrollPane(vbox_docs);
    protected static TextArea text_addDocs = new TextArea();
    protected static Button button_submit = new Button("Save");
    

    // Static Attributes
    private static ViewAdminRequests theView;
    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static Scene theScene;
    protected static User theUser;
    
    // MVC Components
    protected static ModelAdminRequests model;
    protected static ControllerAdminRequests controller;
    
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    
 // This is a separator and it is used to partition the GUI for various tasks
 	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
 	
 	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application, logging
 	// out, and on other pages a return is provided so the user can return to a previous page when
 	// the actions on that page are complete.  Be advised that in most cases in this code, the 
 	// return is to a fixed page as opposed to the actual page that invoked the pages.
 	protected static Button button_Return = new Button("Return");
 	protected static Button button_Logout = new Button("Logout");
 	protected static Button button_Quit = new Button("Quit");

    /**
     * Display method to launch the scene.
     */
    public static void displayAdminRequests(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        if (theView == null)
            theView = new ViewAdminRequests();

        theStage.setTitle("CSE 360 Foundations: Request Admin Action");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Private constructor (Singleton)
     */
    private ViewAdminRequests() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPrefWidth(width);
        mainContainer.setPrefHeight(height);
        
     	titleLabel.setText("Admin Requests");
     	setupLabelUI(titleLabel, "Arial", 28, width, Pos.CENTER, 0, 5);
     		
     	label_UserDetails.setText("User: " + theUser.getUserName());
     	setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        // Instructions
        instructionLabel = new Label("Please Select a Request: ");
        setupLabelUI(instructionLabel, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);
        
        // Request drop down
        setupComboBoxUI(combobox_requestSelect, "Dialog", 16, 100, 250, 125);
		List<String> requestList = controller.loadRequests();
		combobox_requestSelect.setItems(FXCollections.observableArrayList(requestList));
		combobox_requestSelect.getSelectionModel().select(0);
		combobox_requestSelect.getSelectionModel().selectedItemProperty()
		.addListener((ObservableValue<? extends String> observable, 
	    		String oldvalue, String newValue) -> {controller.doSelectRequest(newValue);});
		
		
		setupButtonUI(button_addDocs, "Dialog", 14, 130, Pos.CENTER, 460, 125);			
		ViewAdminRequests.button_addDocs.setOnAction((event) -> 
			{ControllerAdminRequests.performAddDocumentation(); });
		
		setupButtonUI(button_viewDocs, "Dialog", 14, 130, Pos.CENTER, 615, 125);			
		ViewAdminRequests.button_viewDocs.setOnAction((event) -> 
			{ControllerAdminRequests.performViewDocumentation(); });
		
		setupButtonUI(button_originalRequest, "Dialog", 16, 150, Pos.CENTER, 425, 275);			
		ViewAdminRequests.button_originalRequest.setOnAction((event) -> 
			{ControllerAdminRequests.performOriginalRequest(); });
		
		setupButtonUI(button_back, "Dialog", 16, 150, Pos.CENTER, 330, 480);			
		ViewAdminRequests.button_back.setOnAction((event) -> 
			{ControllerAdminRequests.performBack(); });
		
		/*
		vbox_docs.setAlignment(Pos.CENTER);
		vbox_docs.setLayoutX(30);
		vbox_docs.setLayoutY(300);
		*/
		
		scroll_docs.setFitToWidth(true);       // VBox matches width
		scroll_docs.setPannable(true);
		scroll_docs.setMaxHeight(280);  
		scroll_docs.setPrefViewportHeight(280);
		
		scroll_docs.setLayoutX(20);
		scroll_docs.setLayoutY(170);
		
		/*
		setupTextUI(text_docs, "Dialog", 16, 200, Pos.CENTER, 280, 325, false);		
		
		
		text_docs.setEditable(false);    // optional: make it read-only
		text_docs.setWrapText(true);     // optional: wrap lines
		text_docs.setPrefHeight(300);    // size as needed
		*/
		
		setupTextUI(text_addDocs, "Dialog", 16, width - 40, Pos.CENTER, 20, 180, true);	
		text_addDocs.setWrapText(true);
		
		//text_addDocs.setWrapText(true);     // optional: wrap lines
		//text_addDocs.setPrefHeight(300);    // size as needed
		
		setupButtonUI(button_submit, "Dialog", 16, 150, Pos.CENTER, 500, 480);			
		ViewAdminRequests.button_submit.setOnAction((event) -> 
			{ControllerAdminRequests.performSubmit(); });
        
        // GUI Area 3		
        setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
     	button_Return.setOnAction((event) -> {ControllerAdminRequests.performReturn(); });

     	setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
     	button_Logout.setOnAction((event) -> {ControllerAdminRequests.performLogout(); });
         
     	setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
     	button_Quit.setOnAction((event) -> {ControllerAdminRequests.performQuit(); });
     	
     	theRootPane.getChildren().addAll(titleLabel, label_UserDetails, instructionLabel, line_Separator1, 
     			combobox_requestSelect, line_Separator4, button_Return, button_Logout, button_Quit);
    }
    
    
    
    /*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
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
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextArea t, String ff, double f, double w, Pos p, double x, double y, boolean e){
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
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
    
    
}