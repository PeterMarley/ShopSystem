package view.windows;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * An abstract superclass for all View Windows of the Shop Administration System
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public abstract class AbstractView {

	private Region root;
	private Scene scene;
	private Stage stage;
	private FXMLLoader loader;

	/**
	 * Parameterised constructor for an AbstractView subclass
	 * 
	 * @param filepathFXML the name of the FXML file containing this SceneGraphs mark-down
	 * @param filepathCSS  the name of the CSS file containing this SceneGraphs style sheets
	 * @param width        the width of this SceneGraph in pixels
	 * @param height       the height of this SceneGraph in pixels
	 * @throws IOException if error occurs while loading the FXML file
	 */
	public AbstractView(String filePrefix, String title) throws IOException {

		this.loader = new FXMLLoader(getClass().getResource(filePrefix + ".fxml"));
		this.root = loader.load();
		this.scene = new Scene(root);
		this.scene.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
		this.scene.getStylesheets().add(getClass().getResource(filePrefix + ".css").toExternalForm());
		this.stage = new Stage();
		this.stage.setScene(scene);
		this.stage.getIcons().add(new Image(getClass().getResource("../img/" + filePrefix.toLowerCase() + "_icon.png").toExternalForm()));
		this.stage.setTitle(title);
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				stage.close();
			}
		});
	}

	/**
	 * @return the Stage of this AbstractView
	 */
	public Stage getStage() {
		return this.stage;
	}

	public FXMLLoader getLoader() {
		return this.loader;
	}

}
