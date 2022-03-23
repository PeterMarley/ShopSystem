package controller;

import java.util.ArrayList;

import log.Logger;

public class AdHocTesting {

	public static void main(String[] args) {
		//Logger log = Logger.getInstance();
		ArrayList<String> l = new ArrayList<String>();
		l.add("Test String 1");
		l.add("Test String 2");
		Logger.logThis(l);
	}

}
