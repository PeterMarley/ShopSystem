package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import view.windows.AbstractView;
import view.windows.humanresources.Hr;
import view.windows.splash.Splash;

public class View extends Application {
	
	private Splash viewSplash;
	private Hr viewHumanResources;
	private AbstractView viewHumanResourcesAddEmployee;
	private AbstractView viewHumanResourcesEditEmployee;
	
	public View() {

	}
		
	public void showHumanResources() {
		try {
			viewHumanResources = new Hr();
			viewHumanResources.getStage().show();
		} catch (IOException e) {
			
		}
	}

	@Override
	public void start(Stage discardedStage) throws Exception {
		this.viewSplash = new Splash();
		viewSplash.getStage().show();
	}
	
//	public void getHumanResources() {
//		viewHumanResources = new 
//		viewHumanResources.getStage().show();
//	}
}
