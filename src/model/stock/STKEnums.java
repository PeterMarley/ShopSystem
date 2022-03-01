package model.stock;

public class STKEnums {
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
}
