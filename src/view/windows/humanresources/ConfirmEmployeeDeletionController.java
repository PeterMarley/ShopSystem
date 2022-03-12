package view.windows.humanresources;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class ConfirmEmployeeDeletionController {
	@FXML
	CheckBox checkboxConfirmDelete;

	public void confirmDelete() {
		System.out.println(getClass() + " confirmDelete() stub");
		if (checkboxConfirmDelete.isSelected()) {
			MainController.deleteEmployeeInDatabase();
		}
	}
}
