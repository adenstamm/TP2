package guiListUsers;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

import database.Database;
import entityClasses.User;

public class ViewListUsers {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // GUI Widgets
    protected static Label label_PageTitle = new Label("User List");
    protected static Button button_Return = new Button("Return to Admin Home");
    
    
    
    // A VBox to hold the user info
    private static VBox userDisplayBox = new VBox(10);
    
    // Singleton instance
    private static ViewListUsers theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    protected static Stage theStage;
    private static Pane theRootPane;
    protected static User theUser;
    private static Scene theScene;

    public static void displayListUsers(Stage ps, User user) {
    	label_PageTitle.setTextFill(Color.BLACK);
    	label_PageTitle.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        theStage = ps;
        theUser = user;
        if (theView == null) {
            theView = new ViewListUsers();
        }

        // Clear previous entries from the display box
        userDisplayBox.getChildren().clear();
        userDisplayBox.setStyle("-fx-background-color: #FFFFFF;");

        List<User> allUsers = theDatabase.getAllUsers();
        
        System.out.println("--- Raw User Data ---");
        if (allUsers != null && !allUsers.isEmpty()) {
            // Log all users to the console for verification and add them to the display
            for (int i = 0; i < allUsers.size(); i++) {
                User u = allUsers.get(i);
                String firstName = u.getFirstName() != null ? u.getFirstName() : "N/A";
                String lastName = u.getLastName() != null ? u.getLastName() : "N/A";
                String email = u.getEmailAddress() != null ? u.getEmailAddress() : "N/A";
                boolean r1 = u.getAdminRole();
                boolean r2 = u.getStudentRole();
                boolean r3 = u.getStaffRole();
                String role = "";
                
                if (r1) {
                	role += "Admin ";
                }
                if (r2) {
                	role += "Role 1 ";
                }
                if (r3) {
                	role += "Role 2";
                }
                
                if (role.length() == 0) {
                	role = "N/A";
                }
                
                
                
                System.out.println(
                    "Username: " + u.getUserName() + 
                    ", Name: " + firstName + " " + lastName +
                    ", Email: " + email +
                    ", Role: " + role
                );
                
                userDisplayBox.getChildren().add(createUserEntry(u));

                // Add a separator line if it's not the last user
                if (i < allUsers.size() - 1) {
                    userDisplayBox.getChildren().add(new Line(0, 0, width - 120, 0));
                }
            }

        } else {
             System.out.println("No users found in the database.");
             Label noUsersLabel = new Label("No users found");
             noUsersLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
             noUsersLabel.setFont(Font.font("Arial", 18));
             userDisplayBox.getChildren().add(noUsersLabel);
        }
        System.out.println("---------------------");

        theStage.setTitle("CSE 360 Foundation Code: User List");
        theStage.setScene(theScene);
        theStage.show();
    }

    private ViewListUsers() {
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);
        theRootPane.setStyle("-fx-background-color: #FFFFFF;");

        setupLabelUI(label_PageTitle, "Arial", 32, width, Pos.CENTER, 0, 20);
//        label_PageTitle.setTextFill(Color.BLACK);

        // Position the VBox that will hold the user info
        userDisplayBox.setLayoutX(0);
        userDisplayBox.setLayoutY(80);
        userDisplayBox.setPrefWidth(width);
        userDisplayBox.setAlignment(Pos.CENTER);

        setupButtonUI(button_Return, "Dialog", 18, 250, Pos.CENTER, (width - 250) / 2, 540);
        button_Return.setOnAction(event -> ControllerListUsers.performReturn());

        theRootPane.getChildren().addAll(label_PageTitle, userDisplayBox, button_Return);
    }

    private static VBox createUserEntry(User user) {
        VBox entryVBox = new VBox(10);
        entryVBox.setStyle("-fx-background-color: #FFFFFF;");
        entryVBox.setPadding(new Insets(10));
        entryVBox.setAlignment(Pos.CENTER);

        // Username
        String username = (user.getUserName() != null && !user.getUserName().isEmpty()) ? user.getUserName() : "N/A";
        boolean r1 = user.getAdminRole();
        boolean r2 = user.getStudentRole();
        boolean r3 = user.getStaffRole();
        String role = "";
        
        if (r1) {
        	role += "Admin ";
        }
        if (r2) {
        	role += "Student ";
        }
        if (r3) {
        	role += "Staff";
        }
        
        if (role.length() == 0) {
        	role = "N/A";
        }
        Label usernameLabel = new Label(username);
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        usernameLabel.setTextFill(Color.BLACK);
//        usernameLabel.setBackground(null);
        usernameLabel.setPrefWidth(width - 140);
        usernameLabel.setAlignment(Pos.CENTER);
        usernameLabel.setStyle("-fx-background-color: #FFFFFF;");
        entryVBox.getChildren().add(usernameLabel);

        // Details using GridPane for alignment
        GridPane detailsGrid = new GridPane();
        detailsGrid.setPadding(new Insets(10, 20, 0, 20));
        detailsGrid.setPrefWidth(width - 140);
        detailsGrid.setAlignment(Pos.CENTER);

        // Setup column constraints to divide space
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        detailsGrid.getColumnConstraints().addAll(col1, col2, col3);

        // Name
        String firstName = (user.getFirstName() != null && !user.getFirstName().isEmpty()) ? user.getFirstName() : "";
        String lastName = (user.getLastName() != null && !user.getLastName().isEmpty()) ? user.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();
        Label nameLabel = new Label("Name: " + (fullName.isEmpty() ? "N/A" : fullName));
//        nameLabel.setBackground(null);
        nameLabel.setFont(Font.font("Arial", 14));
        nameLabel.setStyle("-fx-background-color: #FFFFFF;");
        nameLabel.setTextFill(Color.BLACK);

        // Email
        String email = (user.getEmailAddress() != null && !user.getEmailAddress().isEmpty()) ? user.getEmailAddress() : "N/A";
        Label emailLabel = new Label("Email: " + email);
        emailLabel.setFont(Font.font("Arial", 14));
        emailLabel.setTextFill(Color.BLACK);
//        emailLabel.setBackground(null);
        emailLabel.setStyle("-fx-background-color: #FFFFFF;");

        Label passwordLabel = new Label("Role: " + role);
        passwordLabel.setFont(Font.font("Arial", 14));
        passwordLabel.setTextFill(Color.BLACK);
        passwordLabel.setStyle("-fx-background-color: #FFFFFF;");
        
        usernameLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        emailLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        passwordLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        nameLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        
        detailsGrid.add(nameLabel, 0, 0);
        detailsGrid.add(emailLabel, 1, 0);
        detailsGrid.add(passwordLabel, 2, 0);
        
        // Align content within each grid cell
        GridPane.setHalignment(nameLabel, HPos.LEFT);
        GridPane.setHalignment(emailLabel, HPos.CENTER);
        GridPane.setHalignment(passwordLabel, HPos.RIGHT);

        entryVBox.getChildren().add(detailsGrid);

        return entryVBox;
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
}

