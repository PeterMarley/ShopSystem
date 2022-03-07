package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import view.windows.AbstractView;
import view.windows.humanresources.ViewEmployeeDetail;
import view.windows.humanresources.ViewHumanResources;
import view.windows.humanresources.ViewEmployeeDetail.AddOrEdit;
import view.windows.splash.ViewSplash;

public class View extends Application {

	private static ViewSplash viewSplash;
	private static ViewHumanResources viewHumanResources;

	/**
	 * JavaFX GUI Launch point. Launched from MainController
	 */
	@Override
	public void start(Stage discardedStage) throws Exception {
		this.setViewSplash();
		this.setViewHumanResources();
		viewSplash.getStage().show();
	}

	/**
	 * @return the viewSplash
	 */
	public ViewSplash getViewSplash() {
		return viewSplash;
	}

	/**
	 * @param create the ViewSplash
	 * @throws IOException 
	 */
	private void setViewSplash() throws IOException {
		viewSplash = new ViewSplash();
	}

	/**
	 * @return the viewHumanResources
	 */
	public ViewHumanResources getViewHumanResources() {
		return viewHumanResources;
	}

	/**
	 * @param create the ViewHumanResources
	 * @throws IOException 
	 */
	private void setViewHumanResources() throws IOException {
		viewHumanResources = new ViewHumanResources();
	}


}
