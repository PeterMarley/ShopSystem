package view.windows.humanresources;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import view.windows.AbstractView;

public class ViewEmployeeDetail extends AbstractView {

	public enum AddOrEdit {
		ADD,
		EDIT;
	}

	private ViewEmployeeDetailController controller;

	public ViewEmployeeDetail(AddOrEdit type) throws IOException {
		super("ViewEmployeeDetail.fxml",
				"ViewEmployeeDetail.css",
				"../img/icon_hr.png",
				(type == AddOrEdit.ADD) ? "Add Employee" : "Edit Employee",
				ControllerType.VIEW_EMPLOYEE_DETAIL_ADD);

		controller = super.getLoader().getController();
	}
	
	public ViewEmployeeDetailController getController() {
		return this.controller;
	}

}
