package guiRequestAdminAction;

import javafx.scene.paint.Color;

/**
 * <p> Title: ControllerRequestAdminAction Class. </p>
 * * <p> Description: The controller handles user events from the ViewRequestAdminAction. </p>
 * * <p> Copyright: Arnav Pushkarna © 2025 </p>
 * * @author Arnav Pushkarna
 * * @version 1.00
 */
public class ControllerRequestAdminAction {

    private ModelRequestAdminAction model;

    public ControllerRequestAdminAction(ModelRequestAdminAction model) {
        this.model = model;
    }

    public void performSubmit() {
        String description = ViewRequestAdminAction.descriptionArea.getText();
        String username = ViewRequestAdminAction.theUser.getUserName();
        
        boolean success = model.submitRequest(description, username);
        
        ViewRequestAdminAction.statusLabel.setText(model.getStatusMessage());
        
        if (success) {
            ViewRequestAdminAction.statusLabel.setTextFill(Color.GREEN);
            ViewRequestAdminAction.descriptionArea.clear();
        } else {
            ViewRequestAdminAction.statusLabel.setTextFill(Color.RED);
        }
    }

    public void performCancel() {
        guiStaff.ViewStaffHome.displayStaffHome(ViewRequestAdminAction.theStage, ViewRequestAdminAction.theUser);
    }
}