package view.windows.splash;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class ViewSplashController {

	@FXML
	private Button buttonHR;
	@FXML
	private Button buttonPOS;

	public void openHumanResources() {
		MainController.showHumanResources();
	}

	public void openPointOfSales() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Point Of Sales is not yet implemented");
		alert.setTitle("Sorry!");
		Stage parentStage = (Stage) alert.getDialogPane().getScene().getWindow();
		parentStage.getIcons().add(new Image(getClass().getResource("../../img/info_icon.png").toExternalForm()));
		alert.show();
	}
	
}
