package view.windows.humanresources;

import java.io.IOException;

import controller.MainController;
import view.windows.AbstractView;
import view.windows.humanresources.ViewEmployeeDetail.AddOrEdit;

public class ViewHumanResources extends AbstractView {

	private ViewHumanResourcesController hrController;
	private ViewEmployeeDetail employeeDetailAdd;
	private ViewEmployeeDetail employeeDetailEdit;

	public ViewHumanResources() throws IOException {
		super("ViewHumanResources.fxml", "ViewHumanResources.css", "../img/icon_hr.png", "Human Resources", ControllerType.VIEW_HUMAN_RESOURCES);
		hrController = super.getLoader().getController();
		this.setEmployeeDetailAdd();
		this.setEmployeeDetailEdit();
	}

	public void refresh() {
		hrController.fillTableEmployees(null);
	}

	public ViewEmployeeDetail getEmployeeDetailAdd() {
		return employeeDetailAdd;
	}

	private void setEmployeeDetailAdd() throws IOException {
		this.employeeDetailAdd = new ViewEmployeeDetail(AddOrEdit.ADD);
	}

	public ViewEmployeeDetail getEmployeeDetailEdit() {
		return this.employeeDetailEdit;
	}

	private void setEmployeeDetailEdit() throws IOException {
		this.employeeDetailEdit = new ViewEmployeeDetail(AddOrEdit.EDIT);

	}

	//	public void showAddEmployee() {
	//		try {
	//			employeeDetailAdd = new ViewEmployeeDetail(AddOrEdit.ADD);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		this.employeeDetailAdd.getStage().show();
	//	}
	//
	//	public void hideAddEmployee() {
	//		employeeDetailAdd.getStage().hide();
	//	}
	//
	//	public void showEditEmployee() {
	//		try {
	//			employeeDetailEdit = new ViewEmployeeDetail(AddOrEdit.EDIT);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		this.employeeDetailEdit.getStage().show();
	//	}
	//
	//	public void hideEditEmployee() {
	//		employeeDetailEdit.getStage().hide();
	//	}

}
