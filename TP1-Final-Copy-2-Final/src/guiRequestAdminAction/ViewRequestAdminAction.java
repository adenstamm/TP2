package guiRequestAdminAction;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import entityClasses.User;
import database.Database;

/**
 * <p> Title: ViewRequestAdminAction Class. </p>
 * <p> Description: The View class creates the user interface for submitting admin requests. </p>
 * <p> Copyright: Arnav Pushkarna © 2025 </p>
 * @author Arnav Pushkarna
 * @version 1.00
 */
public class ViewRequestAdminAction {

    // Attributes required by the user interface
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // UI Widgets
    protected static VBox mainContainer;
    protected static Label titleLabel;
    protected static Label instructionLabel;
    protected static TextArea descriptionArea;
    protected static Button submitButton;
    protected static Button cancelButton;
    protected static Label statusLabel;

    // Static Attributes
    private static ViewRequestAdminAction theView;
    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static Scene theScene;
    protected static User theUser;

    // MVC Components
    protected static ModelRequestAdminAction model;
    protected static ControllerRequestAdminAction controller;

    private static Database theDatabase = applicationMain.FoundationsMain.database;

    /**
     * Display method to launch the scene.
     */
    public static void displayRequestAdminAction(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        if (theView == null) {
            theView = new ViewRequestAdminAction();
            // Initialize Model and Controller here once
            model = new ModelRequestAdminAction(theDatabase);
            controller = new ControllerRequestAdminAction(model);
        }

        // Reset fields
        statusLabel.setText("");
        descriptionArea.clear();

        theStage.setTitle("CSE 360 Foundations: Request Admin Action");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Private constructor (Singleton)
     */
    private ViewRequestAdminAction() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPrefWidth(width);
        mainContainer.setPrefHeight(height);

        // Title
        titleLabel = new Label("Request Admin Action");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Instructions
        instructionLabel = new Label("Please describe the action you require from an administrator:");
        instructionLabel.setFont(Font.font("Arial", 14));

        // Text Area
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter details here (e.g., 'Please reset password for user X' or 'Delete offensive post Y')");
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setWrapText(true);
        descriptionArea.setMaxWidth(600);

        // Submit Button
        submitButton = new Button("Submit Request");
        submitButton.setPrefWidth(150);
        submitButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(e -> controller.performSubmit());

        // Cancel Button
        cancelButton = new Button("Back");
        cancelButton.setPrefWidth(100);
        cancelButton.setOnAction(e -> controller.performCancel());

        // Status Label
        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        mainContainer.getChildren().addAll(
            titleLabel, 
            instructionLabel, 
            descriptionArea, 
            submitButton, 
            cancelButton, 
            statusLabel
        );

        theRootPane.getChildren().add(mainContainer);
    }
}