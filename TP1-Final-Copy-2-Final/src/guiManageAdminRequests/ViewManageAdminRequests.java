package guiManageAdminRequests;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import entityClasses.User;
import entityClasses.AdminRequest;
import database.Database;
import java.util.List;

/**
 * <p> Title: ViewManageAdminRequests Class. </p>
 * <p> Description: The View class creates the user interface for viewing and managing admin requests.
 * Admins can close requests with resolution notes. Both staff and admins can view the request list. </p>
 * @author Aden Stamm
 * @version 1.00
 */
public class ViewManageAdminRequests {

    // Attributes required by the user interface
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // GUI Widgets
    protected static Label label_PageTitle = new Label("Admin Requests");
    protected static Label label_Status = new Label("");
    
    // Separator lines
    private static Line line_Separator1 = new Line(20, 100, width-20, 100);
    private static Line line_Separator2 = new Line(20, 500, width-20, 500);
    
    protected static Button button_Return = new Button("Return");
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");
    
    // A VBox to hold the request entries
    private static VBox requestDisplayBox = new VBox(15);
    
    // Singleton instance
    private static ViewManageAdminRequests theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    protected static Stage theStage;
    private static Pane theRootPane;
    protected static User theUser;
    private static Scene theScene;
    
    // MVC Components
    protected static ModelManageAdminRequests model;
    protected static ControllerManageAdminRequests controller;

    /**
     * Display method to launch the scene.
     */
    public static void displayManageAdminRequests(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        if (theView == null) {
            theView = new ViewManageAdminRequests();
            // Initialize Model and Controller here once
            model = new ModelManageAdminRequests(theDatabase);
            controller = new ControllerManageAdminRequests(model);
        }
        
        // Clear previous entries and refresh the list
        requestDisplayBox.getChildren().clear();
        label_Status.setText("");
        refreshRequestList();

        theStage.setTitle("CSE 360 Foundations: Manage Admin Requests");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Refreshes the request list from the database.
     */
    protected static void refreshRequestList() {
        // Clear existing entries
        requestDisplayBox.getChildren().clear();
        
        List<AdminRequest> requests = model.getOpenRequests();
        
        if (requests != null && !requests.isEmpty()) {
            for (int i = 0; i < requests.size(); i++) {
                AdminRequest request = requests.get(i);
                VBox requestEntry = createRequestEntry(request);
                requestDisplayBox.getChildren().add(requestEntry);
                
                // Add a separator line if it's not the last request
                if (i < requests.size() - 1) {
                    Line separator = new Line(0, 0, width - 120, 0);
                    requestDisplayBox.getChildren().add(separator);
                }
            }
        } else {
            Label noRequestsLabel = new Label("No open requests found");
            noRequestsLabel.setFont(Font.font("Arial", 18));
            noRequestsLabel.setTextFill(Color.GRAY);
            requestDisplayBox.getChildren().add(noRequestsLabel);
        }
    }

    /**
     * Creates a VBox entry for displaying a single admin request.
     */
    private static VBox createRequestEntry(AdminRequest request) {
        VBox entryVBox = new VBox(10);
        entryVBox.setStyle("-fx-background-color: #F5F5F5;");
        entryVBox.setPadding(new Insets(15));
        entryVBox.setPrefWidth(width - 60);

        // Request ID
        Label idLabel = new Label("Request ID: " + request.getRequestID());
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        idLabel.setTextFill(Color.BLACK);
        entryVBox.getChildren().add(idLabel);

        // Creator Username
        Label creatorLabel = new Label("Created by: " + request.getCreatorUsername());
        creatorLabel.setFont(Font.font("Arial", 14));
        creatorLabel.setTextFill(Color.BLACK);
        entryVBox.getChildren().add(creatorLabel);

        // Description
        Label descLabel = new Label("Description:");
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        descLabel.setTextFill(Color.BLACK);
        entryVBox.getChildren().add(descLabel);
        
        TextArea descArea = new TextArea(request.getDescription());
        descArea.setEditable(false);
        descArea.setWrapText(true);
        descArea.setPrefRowCount(3);
        descArea.setMaxWidth(width - 100);
        descArea.setStyle("-fx-background-color: #FFFFFF;");
        entryVBox.getChildren().add(descArea);

        // Status
        Label statusLabel = new Label("Status: " + request.getStatus());
        statusLabel.setFont(Font.font("Arial", 14));
        if ("Open".equals(request.getStatus())) {
            statusLabel.setTextFill(Color.DARKGREEN);
        } else {
            statusLabel.setTextFill(Color.DARKRED);
        }
        entryVBox.getChildren().add(statusLabel);

        // Resolution Note
        if (request.getResolutionNote() != null && !request.getResolutionNote().isEmpty()) {
            Label resolutionLabel = new Label("Resolution Note:");
            resolutionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            resolutionLabel.setTextFill(Color.BLACK);
            entryVBox.getChildren().add(resolutionLabel);
            
            TextArea resolutionArea = new TextArea(request.getResolutionNote());
            resolutionArea.setEditable(false);
            resolutionArea.setWrapText(true);
            resolutionArea.setPrefRowCount(2);
            resolutionArea.setMaxWidth(width - 100);
            resolutionArea.setStyle("-fx-background-color: #FFFFFF;");
            entryVBox.getChildren().add(resolutionArea);
        }

        // Action buttons (only for admins and only for open requests)
        if (theUser.getAdminRole() && "Open".equals(request.getStatus())) {
            Button closeButton = new Button("Close Request");
            closeButton.setStyle("-fx-font-size: 12px; -fx-background-color: #F44336; -fx-text-fill: white;");
            closeButton.setOnAction(e -> {
                controller.performCloseRequest(request.getRequestID());
            });
            
            entryVBox.getChildren().add(closeButton);
        }

        return entryVBox;
    }

    /**
     * Private constructor (Singleton)
     */
    private ViewManageAdminRequests() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);
        theRootPane.setStyle("-fx-background-color: #FFFFFF;");

        // Title
        label_PageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        label_PageTitle.setLayoutX(0);
        label_PageTitle.setLayoutY(20);
        label_PageTitle.setPrefWidth(width);
        label_PageTitle.setAlignment(Pos.CENTER);

        // Status label
        label_Status.setFont(Font.font("Arial", 14));
        label_Status.setLayoutX(20);
        label_Status.setLayoutY(70);
        label_Status.setPrefWidth(width - 40);

        // ScrollPane for requests
        ScrollPane scrollPane = new ScrollPane(requestDisplayBox);
        scrollPane.setLayoutX(20);
        scrollPane.setLayoutY(110);
        scrollPane.setPrefWidth(width - 40);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(380);
        scrollPane.setPannable(true);
        requestDisplayBox.setPadding(new Insets(10));

        // Buttons
        button_Return.setFont(Font.font("Dialog", 18));
        button_Return.setPrefWidth(210);
        button_Return.setLayoutX(20);
        button_Return.setLayoutY(540);
        button_Return.setOnAction(e -> controller.performReturn());

        button_Logout.setFont(Font.font("Dialog", 18));
        button_Logout.setPrefWidth(210);
        button_Logout.setLayoutX(300);
        button_Logout.setLayoutY(540);
        button_Logout.setOnAction(e -> controller.performLogout());

        button_Quit.setFont(Font.font("Dialog", 18));
        button_Quit.setPrefWidth(210);
        button_Quit.setLayoutX(570);
        button_Quit.setLayoutY(540);
        button_Quit.setOnAction(e -> controller.performQuit());

        theRootPane.getChildren().addAll(
            label_PageTitle,
            label_Status,
            line_Separator1,
            scrollPane,
            line_Separator2,
            button_Return,
            button_Logout,
            button_Quit
        );
    }
}

