package guiManageAdminRequests;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.Optional;

/**
 * <p> Title: ControllerManageAdminRequests Class. </p>
 * <p> Description: The controller handles user events from the ViewManageAdminRequests.
 * It processes closing requests with resolution notes. </p>
 * @author Aden stamm
 * @version 1.00
 */
public class ControllerManageAdminRequests {

    private ModelManageAdminRequests model;

    public ControllerManageAdminRequests(ModelManageAdminRequests model) {
        this.model = model;
    }

    /**
     * Handles the close request action.
     */
    public void performCloseRequest(int requestID) {
        // Create a custom dialog for entering the resolution note
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Close Admin Request");
        dialog.setHeaderText("Enter a resolution note for this request:");

        // Set the button types
        ButtonType closeButtonType = new ButtonType("Close Request", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType, ButtonType.CANCEL);

        // Create the TextArea for resolution note
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(50);
        textArea.setPromptText("Enter resolution note (optional)");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Resolution Note:"), 0, 0);
        grid.add(textArea, 0, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the text area
        Platform.runLater(() -> textArea.requestFocus());

        // Convert the result to a string when the close button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            String resolutionNote = result.get().trim();
            
            boolean success = model.closeRequest(requestID, resolutionNote);
            
            ViewManageAdminRequests.label_Status.setText(model.getStatusMessage());
            
            if (success) {
                ViewManageAdminRequests.label_Status.setTextFill(Color.GREEN);
                
                // Show confirmation alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Request Closed");
                alert.setHeaderText("Success");
                alert.setContentText("The request has been closed successfully.");
                alert.showAndWait();
                
                // Refresh the list
                ViewManageAdminRequests.refreshRequestList();
            } else {
                ViewManageAdminRequests.label_Status.setTextFill(Color.RED);
                
                // Show error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Close Request");
                alert.setContentText(model.getStatusMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the return action - navigates back to the appropriate home page.
     */
    public void performReturn() {
        if (ViewManageAdminRequests.theUser.getAdminRole()) {
            guiAdminHome.ViewAdminHome.displayAdminHome(
                ViewManageAdminRequests.theStage, ViewManageAdminRequests.theUser);
        } else if (ViewManageAdminRequests.theUser.getStaffRole()) {
            guiStaff.ViewStaffHome.displayStaffHome(
                ViewManageAdminRequests.theStage, ViewManageAdminRequests.theUser);
        }
    }

    /**
     * Handles the logout action.
     */
    public void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewManageAdminRequests.theStage);
    }

    /**
     * Handles the quit action.
     */
    public void performQuit() {
        System.exit(0);
    }
}

