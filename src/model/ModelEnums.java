package model;

public class ModelEnums {
	public enum StorageTemp {
		NON_SPECIFIC,
		ROOM_TEMPERATURE,
		FRIDGE,
		FREEZER;
	}

	public enum Categories {
		FLOOR_ITEM,
		LIQUID_FUEL,
		SOLID_FUEL;
	}

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

	public enum CategorySolidFuel {
		COAL,
		CHARCOAL,
		WOOD,
		OTHER;
	}

	public enum CategoryLiquidFuel {
		UNLEADED,
		SUPER_UNLEADED,
		DIESEL,
		HEATING_OIL;
	}

	public enum StockMessages {
		MSG_EQUALS_NULL_PARAM("StockItem.equals() cannot accept null parameters"),
		MSG_SET_NAME_NULL("StockItem name cannot be null!"),
		MSG_SET_NAME_BLANK("StockItem name cannot be blank!"),
		MSG_SET_SELLHISTORY_NULL("StockItem sellHistory cannot be set to null"),
		MSG_SET_SELLHISTORY_EMPTY("StockItem sellHistory cannot be set to empty"),
		MSG_SET_PRICE_LESS_THAN_ONE("StockItem priceInPence must be 1 or greater but was "),
		MSG_SET_STORAGETEMP_NULL("StockItem StorageTemp cannot be set to null");

		private String text;

		private StockMessages(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}

	/**
	 * Constant numbers for HREmployee class
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum EmployeeNumbers {
		INT_MIN_WAGE(800),
		INT_MIN_WEEKLY_HOURS(0);

		private int number;

		private EmployeeNumbers(int number) {
			this.number = number;
		}

		public int get() {
			return this.number;
		}
	}

	/**
	 * Constant Strings for HREmployee class
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum EmployeeStrings {
		MSG_HOURLY_RATE_INVALID("HREmployee Constructor: Specified hourly rate is invalid "),
		MSG_HOURS_PER_WEEK_INVALID("HREmployee Constructor: specified hoursPerWeek was a negative number"),
		MSG_DATE_OF_START_NULL("HREmployee Constructor: Date of Employment Start was null"),
		MSG_DATE_OF_END_IMPOSSIBLE("HREmployee Constructor: The employment end date was, impossibly, before the employment start date"),
		MSG_SHIFTS_EMPTY("HREmployee Constructor: Provided list of shifts was empty"),
		MSG_SHIFTS_NULL("HREmployee Constructor: Provided list of shifts was null"),
		MSG_SHIFTS_ADD_SINGLE_FAILED("HREmployee Constructor: Specified shift could not be added:");

		private String text;

		private EmployeeStrings(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}

	/**
	 * Constant Strings for HRPerson class
	 * 
	 * @author Peter Marley
	 *
	 */
	public enum PersonStrings {
		MSG_EMAIL_INVALID("Person email parameter failed validation: "),
		MSG_NAME_BLANK("Person name (forename or surname) cannot be blank"),
		MSG_NAME_NULL("Person name (forename or surname) cannot be set to null"),
		MSG_NAME_INVALID_CHARS("Person name (forename or surname) must only have a-z, A-Z or - characters"),
		VALUE_DEFAULT_EMAIL("no email provided"),
		VALUE_DEFAULT_PHONE_NUMBER("no phone number provided");

		private String text;

		private PersonStrings(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}

}
