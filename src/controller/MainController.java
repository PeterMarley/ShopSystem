package controller;

import view.View;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import model.DatabaseAccessObject;

public class MainController {

	private static DatabaseAccessObject dao;
	private static View view;
	
	public static void main(String[] args) {
		try {
			dao = new DatabaseAccessObject("./src/model/shop.db");
			view = new View();
		} catch (SQLException controllerInitException) {
			System.err.println(controllerInitException.getMessage());
		}
		Application.launch(View.class);
		
	}
	
	public static View getView() {
		return view;
	}
	
	public static DatabaseAccessObject getDatabase() {
		return dao;
	}

}
