package model;

import model.ModelEnums.Numbers;

public class ModelEnums {
	/**
	 * Storage temperatures for Stock
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum StorageTemp {
		NON_SPECIFIC,
		ROOM_TEMPERATURE,
		FRIDGE,
		FREEZER;
	}

	/**
	 * General Stock categories
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum Categories {
		FLOOR_ITEM,
		LIQUID_FUEL,
		SOLID_FUEL;
	}

	/**
	 * Floor Item Stock categories
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum CategoryFloorItem {
		FRUIT_VEG,
		SUNDRY,
		HOUSEHOLD,
		CONFECTION,
		MEAT,
		BAKERY,
		FUEL,
		STATIONARY,
		OTHER;
	}

	/**
	 * Solid Fuel Stock categories
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum CategorySolidFuel {
		COAL,
		CHARCOAL,
		WOOD,
		OTHER;
	}

	/**
	 * Liquid Fuel Stock categories
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum CategoryLiquidFuel {
		UNLEADED,
		SUPER_UNLEADED,
		DIESEL,
		HEATING_OIL;
	}

	/**
	 * Integers used throughout data models
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum Numbers {
		INT_MIN_WAGE(800),
		INT_MIN_WEEKLY_HOURS(0),
		PERSON_NAME_MIN_LENGTH(2),
		EMPLOYEE_MAX_WEEKLY_HOURS(112); // allows 8-hours of sleep a night AND THATS ALL DAYUM

		private int number;

		private Numbers(int number) {
			this.number = number;
		}

		public int get() {
			return this.number;
		}
	}

	/**
	 * Messages used throughout data models
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum Messages {
		// Person
		PERSON_SET_EMAIL_INVALID("Person email parameter failed validation: "),
		PERSON_SET_NAME_BLANK("Person name (forename or surname) cannot be blank"),
		PERSON_SET_NAME_NULL("Person name (forename or surname) cannot be set to null"),
		PERSON_SET_NAME_INVALID_CHARS("Person name (forename or surname) must only have a-z, A-Z or - characters"),
		PERSON_SET_NAME_TOO_FEW_CHARS("Name must be at least 2 characters long"),
		// Employee
		EMPLOYEE_SET_HOURLY_RATE_INVALID("Specified hourly rate is invalid "),
		EMPLOYEE_SET_DATE_OF_START_NULL("Date of Employment Start was null"),
		EMPLOYEE_SET_DATE_OF_END_IMPOSSIBLE("Date of Employment End was, impossibly, before the employment Start Date"),
		EMPLOYEE_SET_SHIFTS_EMPTY("HREmployee Constructor: Provided list of shifts was empty"),
		EMPLOYEE_SET_SHIFTS_NULL("HREmployee Constructor: Provided list of shifts was null"),
		EMPLOYEE_ADD_SHIFT_SINGLE_FAILED("HREmployee Constructor: Specified shift could not be added:"),
		EMPLOYEE_SET_WEEKLY_HOURS_NEGATIVE("Hours per week cannot be set to a negative value."),
		EMPLOYEE_SET_WEEKLY_HOURS_OVER_MAX("Hours per week cannot be set to a value over " + Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get()),
		// Stock
		STOCK_EQUALS_NULL_PARAM("StockItem.equals() cannot accept null parameters"),
		STOCK_SET_NAME_NULL("StockItem name cannot be null!"),
		STOCK_SET_NAME_BLANK("StockItem name cannot be blank!"),
		STOCK_SET_SELLHISTORY_NULL("StockItem sellHistory cannot be set to null"),
		STOCK_SET_SELLHISTORY_EMPTY("StockItem sellHistory cannot be set to empty"),
		STOCK_SET_PRICE_LESS_THAN_ONE("StockItem priceInPence must be 1 or greater but was "),
		STOCK_SET_STORAGETEMP_NULL("StockItem StorageTemp cannot be set to null");

		private String text;

		private Messages(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}
}