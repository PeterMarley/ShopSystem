package view.windows.humanresources;

import java.io.IOException;

import view.windows.AbstractView;

public class Hr extends AbstractView {
	
	private static HrController hrController;

	public Hr() throws IOException {
		super("Hr", "Human Resources");
		hrController = super.getLoader().getController();
	}

	public void refresh() {
		hrController.fillTableEmployees(null);
	}

}
