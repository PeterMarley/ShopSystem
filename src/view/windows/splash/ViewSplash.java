package view.windows.splash;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.windows.AbstractView;

public class ViewSplash extends AbstractView {

	public ViewSplash() throws IOException {
		super("ViewSplash.fxml","ViewSplash.css","../img/icon_splash.png", "SASpm", ControllerType.VIEW_SPLASH);
		super.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
			}
		});

		//bg image https://www.companyshopgroup.co.uk/content/files/images/018%20CSG%201281219.jpg
		// icon: <a href="https://www.flaticon.com/free-icons/supermarket" title="supermarket icons">Supermarket icons created by Freepik - Flaticon</a>
		// icon: <a href="https://www.flaticon.com/free-icons/profile" title="profile icons">Profile icons created by Bingge Liu - Flaticon</a>
		// icon: <a href="https://www.flaticon.com/free-icons/info" title="info icons">Info icons created by Freepik - Flaticon</a>
	}
}
