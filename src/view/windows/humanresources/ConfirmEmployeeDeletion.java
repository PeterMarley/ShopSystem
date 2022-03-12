package view.windows.humanresources;

import java.io.IOException;

import view.windows.Window;

public class ConfirmEmployeeDeletion extends Window {

	public ConfirmEmployeeDeletion() throws IOException {
		super("ConfirmEmployeeDeletion.fxml", 
				null, 
				"../img/icon_hr.png", 
				"Confirm Employee Deletion", 
				ControllerType.CONFIRM_EMPLOYEE_DELETION);
	}

}
