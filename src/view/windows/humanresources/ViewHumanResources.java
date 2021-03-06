package view.windows.humanresources;

import java.io.IOException;

import controller.MainController;
import javafx.scene.control.TableView.TableViewSelectionModel;
import model.HumanResourcesModel.Employee;
import view.windows.Window;
import view.windows.humanresources.ViewEmployeeDetail.AddOrEdit;

/**
 * A JavaFX Window that handles all Human Resources actions of the ShopSystem
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class ViewHumanResources extends Window {

	private ViewHumanResourcesController hrController;
	private ViewEmployeeDetail employeeDetailAdd;
	private ViewEmployeeDetail employeeDetailEdit;
	private ConfirmEmployeeDeletion employeeDeleteConfirm;

	/**
	 * Constructor for ViewHumanResources
	 * 
	 * @throws IOException if FXMLLoading error occurs in parent class, or if either setter fails to instantiate ViewEmployeeDetail
	 */
	public ViewHumanResources() throws IOException {
		super("ViewHumanResources.fxml", "ViewHumanResources.css", "../img/icon_hr.png", "Human Resources", ControllerType.VIEW_HUMAN_RESOURCES);
		hrController = super.getLoader().getController();
		this.setEmployeeDetailAdd();
		this.setEmployeeDetailEdit();
		this.setEmployeeDeleteConfirm();
	}

	/**
	 * Refresh the employee TableView
	 */
	public void refreshEmployeeTableView() {
		hrController.fillTableViewEmployees(null);
	}

	/**
	 * @return a ViewEmployeeDetail object (employeeDetailAdd)
	 */
	public ViewEmployeeDetail getEmployeeDetailAdd() {
		return employeeDetailAdd;
	}

	/**
	 * Instantiate the ViewEmployeeDetail to handling adding a new employee
	 * 
	 * @throws IOException if instantiation fails
	 */
	private void setEmployeeDetailAdd() throws IOException {
		this.employeeDetailAdd = new ViewEmployeeDetail(AddOrEdit.ADD);
	}

	/**
	 * 
	 * @return a ViewEmployeeDetail object (employeeDetailEdit)
	 */
	public ViewEmployeeDetail getEmployeeDetailEdit() {
		return this.employeeDetailEdit;
	}

	/**
	 * Instantiate the ViewEmployeeDetail to handing editing an existing employee
	 * 
	 * @throws IOException if instantiation fails
	 */
	private void setEmployeeDetailEdit() throws IOException {
		this.employeeDetailEdit = new ViewEmployeeDetail(AddOrEdit.EDIT);

	}

	public ConfirmEmployeeDeletion getEmployeeDeleteConfirm() {
		return employeeDeleteConfirm;
	}

	/**
	 * Instantiate the ConfirmEmployeeDeletion to handle confirmation that a user wishes to truly delete an employee
	 * 
	 * @throws IOException
	 */
	public void setEmployeeDeleteConfirm() throws IOException {
		this.employeeDeleteConfirm = new ConfirmEmployeeDeletion();
	}

	/**
	 * Returns the employee currently selection in the Employees tab of the {@link view.windows.humanresources.ViewHumanResources} TableView JavaFX control
	 * @return the currently selected {@code Employee} from the employees {@code TableView} in {@link view.windows.humanresources.ViewHumanResources}
	 */
	public Employee getSelectedEmployee() {
		return hrController.getSelectedEmployeeAsEmployee();
	}

}
