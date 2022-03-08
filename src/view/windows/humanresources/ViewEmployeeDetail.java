package view.windows.humanresources;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import view.windows.AbstractView;

/**
 * A JavaFX Window that can handle adding adding new employee to database, or editing an existing employee in database.
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class ViewEmployeeDetail extends AbstractView {
	/**
	 * Enumerated type for the kind of ViewEmployeeDetail that can be created
	 */
	public enum AddOrEdit {
		ADD,
		EDIT;
	}

	private ViewEmployeeDetailController controller;

	/**
	 * Construct a ViewEmployeeDetail object of type ADD or EDIT
	 * 
	 * @param type
	 * @throws IOException if FXMLLoader fails in parent class
	 */
	public ViewEmployeeDetail(AddOrEdit type) throws IOException {
		super("ViewEmployeeDetail.fxml",
				"ViewEmployeeDetail.css",
				"../img/icon_hr.png",
				(type == AddOrEdit.ADD) ? "Add Employee" : "Edit Employee",
				(type == AddOrEdit.ADD) ? ControllerType.VIEW_EMPLOYEE_DETAIL_ADD : ControllerType.VIEW_EMPLOYEE_DETAIL_EDIT);

		controller = super.getLoader().getController();
	}

	/**
	 * Get this ViewEmployeeDetails controller
	 * 
	 * @return a ViewEmployeeDetailController subclass object
	 */
	public ViewEmployeeDetailController getController() {
		return this.controller;
	}

}
