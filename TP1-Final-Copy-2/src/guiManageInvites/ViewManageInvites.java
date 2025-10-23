package guiManageInvites;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.User;
import guiAdminHome.ControllerAdminHome;
import guiDeleteUser.ControllerDeleteUser;
import guiDeleteUser.ViewDeleteUser;
import entityClasses.Invite;

public class ViewManageInvites {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // GUI Widgets
    protected static Label label_PageTitle = new Label("Manage Invitations");
    
    // This is a separator and it is used to partition the GUI for various tasks
 	private static Line line_Separator1 = new Line(20, 100, width-20, 100);
 	private static Line line_Separator2 = new Line(20, 165, width-20, 165);
 	private static Line line_Separator3 = new Line(20, 200, width-20, 200);
 	private static Line line_Separator4 = new Line(20, 500, width-20, 500);
 	
 	
    protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	//Add an invite
	protected static Label label_Invitations = new Label("Send An Invitation");
	protected static Label label_InvitationEmailAddress = new Label("Email Address");
	protected static TextField text_InvitationEmailAddress = new TextField();
	protected static ComboBox <String> combobox_SelectRole = new ComboBox <String>();
	protected static String [] roles = {"Admin", "Role1", "Role2"};
	protected static Button button_SendInvitation = new Button("Send Invitation");
	protected static Alert alertEmailError = new Alert(AlertType.INFORMATION);
	protected static Alert alertEmailSent = new Alert(AlertType.INFORMATION);
	
	//Select an invite to delete
	protected static Label label_SelectInvite = new Label("Select an invite to be deleted:");
	protected static ComboBox <String> combobox_SelectInvite = new ComboBox <String>();
	
	// Area 2b: When a user has been selected these widgets are shown and can be used
	protected static Button button_DeleteInvite = new Button("Delete Invite");
    
    // A VBox to hold the invite info
	private static VBox inviteDisplayBox = new VBox(15);
    private static VBox userDisplayBox = new VBox(10);
    
    // Singleton instance
    private static ViewManageInvites theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    protected static Stage theStage;
    private static Pane theRootPane;
    protected static User theUser;
    private static Scene theScene;
    protected static String theSelectedInvite = "<Select an Invite>";	// The user whose roles are being updated
    
    
    
 

    public static void displayManageInvites(Stage ps, User user) {
    	label_PageTitle.setTextFill(Color.BLACK);
    	label_PageTitle.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        theStage = ps;
        theUser = user;
        if (theView == null) {
            theView = new ViewManageInvites();
        }

        // Clear previous entries from the display box
        inviteDisplayBox.getChildren().clear();
        inviteDisplayBox.setStyle("-fx-background-color: #FFFFFF;");
        
        List<Invite> inviteList = theDatabase.getAllInvites();
        
        System.out.println("--- Raw Invite Data ---");
        if (inviteList != null && !inviteList.isEmpty()) {
            // Log all users to the console for verification and add them to the display
            for (int i = 0; i < inviteList.size(); i++) {
                Invite inv = inviteList.get(i);
                
                System.out.println("Code: " + inv.getInviteCode() + " Email: " + inv.getEmailAddress() +
                		" Role: " + inv.getRole() + "Deadline: " + inv.getDeadlineString());
                
                userDisplayBox.getChildren().add(createInviteEntry(inv));
                VBox inviteEntry = createInviteEntry(inv);
                inviteDisplayBox.getChildren().add(inviteEntry);
                
             // Add a separator line if it's not the last invite
                if (i < inviteList.size() - 1) {
                    userDisplayBox.getChildren().add(new Line(0, 0, width - 120, 0));
                }
            } 
        } else {
            System.out.println("No invites found in the database.");
            Label noInvitesLabel = new Label("No invites found");
            noInvitesLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
            noInvitesLabel.setFont(Font.font("Arial", 18));
            userDisplayBox.getChildren().add(noInvitesLabel);
       }
       System.out.println("---------------------");
        
       theStage.setTitle("CSE 360 Foundation Code: Manage Invitations");
       theStage.setScene(theScene);
       theStage.show();    	
    }
    
   

    private ViewManageInvites() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);
        theRootPane.setStyle("-fx-background-color: #FFFFFF;");

        setupLabelUI(label_PageTitle, "Arial", 32, width, Pos.CENTER, 0, 20);
//        label_PageTitle.setTextFill(Color.BLACK);
        
        // GUI Area 2
 		setupLabelUI(label_Invitations, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 65);
 	
 		setupLabelUI(label_InvitationEmailAddress, "Arial", 16, width, Pos.BASELINE_LEFT,
 		20, 100);
 		
 		setupTextUI(text_InvitationEmailAddress, "Arial", 16, 355, Pos.BASELINE_LEFT,
 		135, 95, true);
 	
 		setupComboBoxUI(combobox_SelectRole, "Dialog", 16, 90, 500, 95);
 	
 		List<String> list = new ArrayList<String>();	// Create a new list empty list of the
 		for (int i = 0; i < roles.length; i++) {		// roles this code currently supports
 			list.add(roles[i]);
 		}
 		combobox_SelectRole.setItems(FXCollections.observableArrayList(list));
 		combobox_SelectRole.getSelectionModel().select(0);
 		alertEmailSent.setTitle("Invitation");
 		alertEmailSent.setHeaderText("Invitation was sent");

 		setupButtonUI(button_SendInvitation, "Dialog", 18, 170, Pos.CENTER, 610, 95);
 		button_SendInvitation.setOnAction((event) -> {ControllerManageInvites.performInvitation(); });
        
     // GUI Area 3a
 		setupLabelUI(label_SelectInvite, "Arial", 18, 300, Pos.BASELINE_LEFT, 20, 155);
 		
 		
 		setupComboBoxUI(combobox_SelectInvite, "Dialog", 16, 320, 280, 150);
 		List<String> inviteList = theDatabase.getInviteList();
 		//System.out.println("Line 167");
 		
 		combobox_SelectInvite.setItems(FXCollections.observableArrayList(inviteList));
 		combobox_SelectInvite.getSelectionModel().select(0);
 		combobox_SelectInvite.getSelectionModel().selectedItemProperty()
     	.addListener((ObservableValue<? extends String> observable, 
     		String oldvalue, String newValue) -> {ControllerManageInvites.doSelectInvite();});
     		
 		// GUI Area 3b
 		setupButtonUI(button_DeleteInvite, "Dialog", 18, 170, Pos.CENTER, 610, 150);
 		
 		button_DeleteInvite.setOnAction((event) -> 
 			{ControllerManageInvites.performDeleteInvite(); });
 		button_DeleteInvite.setDisable(true);

     // Wrap the entire entries list in one ScrollPane
 		//GUI Area 4
 	    ScrollPane scrollPane = new ScrollPane(inviteDisplayBox);
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY(200);
		scrollPane.setPrefWidth(width);
 	    
 	    scrollPane.setFitToWidth(true);
 	    scrollPane.setPrefViewportHeight(300);
 	    scrollPane.setPannable(true);
 	    
        
     // GUI Area 5
     		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
     		button_Return.setOnAction((event) -> {ControllerManageInvites.performReturn(); });

     		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
     		button_Logout.setOnAction((event) -> {ControllerManageInvites.performLogout(); });
         
     		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
     		button_Quit.setOnAction((event) -> {ControllerManageInvites.performQuit(); });
     		

        theRootPane.getChildren().addAll(label_PageTitle, scrollPane, button_Return, button_Logout, button_Quit,
        		label_SelectInvite, combobox_SelectInvite, button_DeleteInvite, label_Invitations, 
        		label_InvitationEmailAddress, text_InvitationEmailAddress,
        		combobox_SelectRole, button_SendInvitation);
    }
    
    private static VBox createInviteEntry(Invite invite) {
        VBox entryVBox = new VBox(10);
        entryVBox.setStyle("-fx-background-color: #FFFFFF;");
        entryVBox.setPadding(new Insets(10));
        entryVBox.setAlignment(Pos.CENTER);

        // Invite Code
        String code = (invite.getInviteCode() != null && !invite.getInviteCode().isEmpty()) ? invite.getInviteCode() : "N/A";
        
        Label codeLabel = new Label("Code: " + code);
        codeLabel.setFont(Font.font("Arial", 14));
        codeLabel.setTextFill(Color.BLACK);
        //emailLabel.setBackground(null);
        codeLabel.setStyle("-fx-background-color: #FFFFFF;");
        entryVBox.getChildren().add(codeLabel);

        
        
        // Details using GridPane for alignment
        GridPane detailsGrid = new GridPane();
        detailsGrid.setPadding(new Insets(0, 20, 0, 20));
        detailsGrid.setPrefWidth(width - 140);
        detailsGrid.setAlignment(Pos.CENTER);
        

        // Setup column constraints to divide space
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(15);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(35);
        detailsGrid.getColumnConstraints().addAll(col1, col2, col3, col4);


        // Email
        String email = (invite.getEmailAddress() != null && !invite.getEmailAddress().isEmpty()) ? invite.getEmailAddress() : "N/A";
        Label emailLabel = new Label("Email: " + email);
        emailLabel.setFont(Font.font("Arial", 14));
        emailLabel.setTextFill(Color.BLACK);
        //emailLabel.setBackground(null);
        emailLabel.setStyle("-fx-background-color: #FFFFFF;");

        //Role
        String roleCode = invite.getRole();
        if(roleCode == "1") {roleCode = "Admin";}
        if(roleCode == "2") {roleCode = "Role 1";}
        if(roleCode == "3") {roleCode = "Role 2";}
        
        Label roleLabel = new Label("Role: " + roleCode);
        roleLabel.setFont(Font.font("Arial", 14));
        roleLabel.setTextFill(Color.BLACK);
        roleLabel.setStyle("-fx-background-color: #FFFFFF;");
        
        // Deadline
        String deadline = (invite.getDeadlineString() != null && !invite.getDeadlineString().isEmpty()) ? invite.getDeadlineString() : "N/A";
        Label deadlineLabel = new Label("Deadline: " + deadline);
        deadlineLabel.setFont(Font.font("Arial", 14));
        deadlineLabel.setTextFill(Color.BLACK);
        //deadlineLabel.setBackground(null);
        deadlineLabel.setStyle("-fx-background-color: #FFFFFF;");
        
        codeLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        emailLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        roleLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        deadlineLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        
        detailsGrid.add(codeLabel, 0, 0);
        detailsGrid.add(emailLabel, 1, 0);
        detailsGrid.add(roleLabel, 2, 0);
        detailsGrid.add(deadlineLabel, 3, 0);
        
        // Align content within each grid cell
        GridPane.setHalignment(codeLabel, HPos.LEFT);
        GridPane.setHalignment(emailLabel, HPos.CENTER);
        GridPane.setHalignment(roleLabel, HPos.CENTER);
        GridPane.setHalignment(deadlineLabel, HPos.RIGHT);

        entryVBox.getChildren().add(detailsGrid);

        return entryVBox;
    }
    
    /**********
	 * <p> Method: refreshInvites </p>
	 * 
	 * <p> Description: This method refreshes the user drop down menu after delete action. </p>
	 * 
	 */
	public static void refreshInvites() {
		//clear scroll box
		inviteDisplayBox.getChildren().clear();
		List<Invite> inviteList = theDatabase.getAllInvites();
		
		System.out.println("--- Raw Invite Data ---");
        if (inviteList != null && !inviteList.isEmpty()) {
            // Log all users to the console for verification and add them to the display
            for (int i = 0; i < inviteList.size(); i++) {
                Invite inv = inviteList.get(i);
                
                System.out.println("Code: " + inv.getInviteCode() + " Email: " + inv.getEmailAddress() +
                		" Role: " + inv.getRole() + "Deadline: " + inv.getDeadlineString());
                
                userDisplayBox.getChildren().add(createInviteEntry(inv));
                VBox inviteEntry = createInviteEntry(inv);
                inviteDisplayBox.getChildren().add(inviteEntry);
                
             // Add a separator line if it's not the last invite
                if (i < inviteList.size() - 1) {
                    userDisplayBox.getChildren().add(new Line(0, 0, width - 120, 0));
                }
            } 
        } else {
            System.out.println("No invites found in the database.");
            Label noInvitesLabel = new Label("No invites found");
            noInvitesLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
            noInvitesLabel.setFont(Font.font("Arial", 18));
            userDisplayBox.getChildren().add(noInvitesLabel);
       }
       System.out.println("---------------------");
        
		
		
		// Gets invite list from database
		List<String> inviteListSelector = theDatabase.getInviteList();
		
		
		combobox_SelectInvite.setItems(FXCollections.observableArrayList(inviteListSelector));
		combobox_SelectInvite.getSelectionModel().select(0);
		combobox_SelectInvite.getSelectionModel().selectedItemProperty()
    	.addListener((ObservableValue<? extends String> observable, 
    		String oldvalue, String newValue) -> {ControllerManageInvites.doSelectInvite();});
	}

	
 


  

    private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, FontWeight.BOLD, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, FontWeight.NORMAL, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

/**********
 * Private local method to initialize the standard fields for a text input field
 * 
 * @param b		The TextField object to be initialized
 * @param ff	The font to be used
 * @param f		The size of the font to be used
 * @param w		The width of the Button
 * @param p		The alignment (e.g. left, centered, or right)
 * @param x		The location from the left edge (x axis)
 * @param y		The location from the top (y axis)
 * @param e		Is this TextField user editable?
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
private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y){
	c.setStyle("-fx-font: " + f + " " + ff + ";");
	c.setMinWidth(w);
	c.setLayoutX(x);
	c.setLayoutY(y);
}
}
