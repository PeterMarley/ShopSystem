package view.windows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.windows.Window.ControllerType;
import view.windows.humanresources.ConfirmEmployeeDeletionController;
import view.windows.humanresources.ViewEmployeeDetailControllerAdd;
import view.windows.humanresources.ViewEmployeeDetailControllerEdit;
import view.windows.humanresources.ViewHumanResourcesController;
import view.windows.splash.ViewSplashController;

/**
 * An abstract superclass for all View Windows of the Shop System.
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public abstract class Window {

	private Region root;
	private Scene scene;
	private Stage stage;
	private FXMLLoader loader;

	/**
	 * Represents the different kind of controllers required in an Window
	 *
	 */
	public enum ControllerType {
		VIEW_SPLASH(ViewSplashController.class),
		VIEW_HUMAN_RESOURCES(ViewHumanResourcesController.class),
		VIEW_EMPLOYEE_DETAIL_ADD(ViewEmployeeDetailControllerAdd.class),
		VIEW_EMPLOYEE_DETAIL_EDIT(ViewEmployeeDetailControllerEdit.class),
		CONFIRM_EMPLOYEE_DELETION(ConfirmEmployeeDeletionController.class);

		private Class<?> controllerClass;

		private ControllerType(Class<?> controllerClass) {
			this.controllerClass = controllerClass;
		}

		public Class<?> getControllerClass() {
			return this.controllerClass;
		}
	}

	/**
	 * Parameterised constructor for an Window subclass
	 * 
	 * @param filepathFXML the name of the FXML file containing this SceneGraphs mark-down
	 * @param filepathCSS  the name of the CSS file containing this SceneGraphs style sheets
	 * @param filepathIcon the name of the Icon file containing this SceneGraphs icon
	 * @param title        the title of this SceneGraph
	 * @param type         ControllerType enum that represents the different types of controller
	 * @throws IOException if error occurs during FXML loading in {@code setRoot()}
	 */
	public Window(String filepathFXML, String filepathCSS, String filepathIcon, String title, ControllerType type) throws IOException {
		this.setLoader(filepathFXML);
		this.setController(type.getControllerClass());
		this.setRoot();
		this.setScene(filepathCSS);
		this.setStage(filepathIcon, title);
	}

	/**
	 * Set the of the FXMLLoader prior to Stage.setScene
	 * 
	 * @param type Controller - the class that is this JavaFX windows controller
	 */
	private void setController(Class<?> controller) {
		try {
			Constructor<?> controllerConstructor = controller.getConstructor((Class[]) null);
			loader.setController(controllerConstructor.newInstance((Object[]) null));
		} catch (InstantiationException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException controllerConstructorEx) {
			System.out.println("Window.setController(ControllerType) FAILED");
			System.err.println(controllerConstructorEx.getClass());
			System.err.println(controllerConstructorEx.getMessage());
		}
	}

	/**
	 * Get the FXMLLoader
	 * 
	 * @return the FXMLLoader
	 */
	public FXMLLoader getLoader() {
		return this.loader;
	}

	/**
	 * Set the FXMLLoader
	 * 
	 * @param loader A String representing the relative filepath to the FXML file
	 */
	private void setLoader(String filepathFXML) {
		this.loader = new FXMLLoader(getClass().getResource(filepathFXML));
	}

	public Region getRoot() {
		return this.root;
	}

	/**
	 * @param root the root to set
	 * @throws IOException
	 */
	private void setRoot() throws IOException {
		this.root = loader.load();
	}

	/**
	 * Set the Scene
	 * 
	 * @param filepathCSS A String representing the relative filepath to the CSS file
	 */
	private void setScene(String filepathCSS) {
		this.scene = new Scene(root);
		this.scene.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
		if (filepathCSS != null) {
			this.scene.getStylesheets().add(getClass().getResource(filepathCSS).toExternalForm());
		}
	}

	/**
	 * Get the Stage
	 * 
	 * @return the Stage of this Window
	 */
	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Set the Stage
	 * 
	 * @param filepathIcon A String representing the relative filepath to the Stage Icon
	 * @param title        A String - the title of this Stage
	 */
	private void setStage(String filepathIcon, String title) {

		this.stage = new Stage();
		this.stage.setScene(scene);
		this.stage.getIcons().add(new Image(getClass().getResource(filepathIcon).toExternalForm()));
		this.stage.setTitle(title);
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				stage.close();
			}
		});
	}

}
