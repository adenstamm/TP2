package guiViewClosedRequests;

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
 * <p> Title: ControllerViewClosedRequests Class. </p>
 * <p> Description: The controller handles user events from the ViewViewClosedRequests.
 * It processes reopen requests and description updates. </p>
 * @author Auto-generated
 * @version 1.00
 */
public class ControllerViewClosedRequests {

    private ModelViewClosedRequests model;

    public ControllerViewClosedRequests(ModelViewClosedRequests model) {
        this.model = model;
    }

    /**
     * Handles the reopen request action.
     */
    public void performReopenRequest(int requestID) {
        boolean success = model.reopenRequest(requestID);

        ViewViewClosedRequests.label_Status.setText(model.getStatusMessage());

        if (success) {
            ViewViewClosedRequests.label_Status.setTextFill(Color.GREEN);

            // Show confirmation alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Request Reopened");
            alert.setHeaderText("Success");
            alert.setContentText("The request has been reopened successfully.");
            alert.showAndWait();

            // Refresh the list
            ViewViewClosedRequests.refreshRequestList();
        } else {
            ViewViewClosedRequests.label_Status.setTextFill(Color.RED);

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Reopen Request");
            alert.setContentText(model.getStatusMessage());
            alert.showAndWait();
        }
    }

    /**
     * Handles the update description action.
     */
    public void performUpdateDescription(int requestID, String currentDescription) {
        // Create a custom dialog for editing the description with TextArea
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Request Description");
        dialog.setHeaderText("Edit the request description:");

        // Set the button types
        ButtonType updateButtonType = new ButtonType("Update", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the TextArea
        TextArea textArea = new TextArea(currentDescription);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(50);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Description:"), 0, 0);
        grid.add(textArea, 0, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the text area
        Platform.runLater(() -> textArea.requestFocus());

        // Convert the result to a string when the update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String newDescription = result.get().trim();

            boolean success = model.updateRequestDescription(requestID, newDescription);

            ViewViewClosedRequests.label_Status.setText(model.getStatusMessage());

            if (success) {
                ViewViewClosedRequests.label_Status.setTextFill(Color.GREEN);

                // Show confirmation alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Description Updated");
                alert.setHeaderText("Success");
                alert.setContentText("The request description has been updated successfully.");
                alert.showAndWait();

                // Refresh the list
                ViewViewClosedRequests.refreshRequestList();
            } else {
                ViewViewClosedRequests.label_Status.setTextFill(Color.RED);

                // Show error alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Update Description");
                alert.setContentText(model.getStatusMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the return action - navigates back to the appropriate home page.
     */
    public void performReturn() {
        if (ViewViewClosedRequests.theUser.getAdminRole()) {
            guiAdminHome.ViewAdminHome.displayAdminHome(
                ViewViewClosedRequests.theStage, ViewViewClosedRequests.theUser);
        } else if (ViewViewClosedRequests.theUser.getStaffRole()) {
            guiStaff.ViewStaffHome.displayStaffHome(
                ViewViewClosedRequests.theStage, ViewViewClosedRequests.theUser);
        }
    }

    /**
     * Handles the logout action.
     */
    public void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewViewClosedRequests.theStage);
    }

    /**
     * Handles the quit action.
     */
    public void performQuit() {
        System.exit(0);
    }
}