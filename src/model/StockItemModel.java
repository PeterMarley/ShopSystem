package model;

import java.time.LocalDateTime;
import java.util.Hashtable;

import model.stock.STKEnums.StockMessages;
import model.stock.STKEnums.StorageTemp;

public class StockItemModel {
	
	private static int uniqueIDSeed = 0;
	
	/**
	 * 
	 * @author Peter Marley
	 *         StockItem
	 *         - uniqueID : long
	 *         - ageRestricted : boolean
	 *         - sellHistory : Hashtable<Date, Integer>
	 *         - price : double
	 *         - name : String
	 *         - supplier : Supplier
	 *         - storageTemperature : StorageTemp
	 * 
	 */
	public abstract class AbstractStockItem {

		private String name;
		private String uniqueID;
		private boolean ageRestricted;
		private Hashtable<LocalDateTime, Integer> sellHistory;
		private int priceInPence;
		//private Supplier supplier;
		private StorageTemp storageTemp;

		/**
		 * Fully parameterised constructor for this StockItem object
		 * 
		 * @param name
		 * @param ageRestricted
		 * @param priceInPence
		 * @param storageTemp
		 */
		public AbstractStockItem(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp) {
			this.setName(name);
			this.setAgeRestricted(ageRestricted);
			this.setPriceInPence(priceInPence);
			this.setStorageTemp(storageTemp);
			this.sellHistory = new Hashtable<LocalDateTime, Integer>();
			this.setUniqueID();
		}

		/**
		 * @return the uniqueIDSeed. Visibility is package-protected as this method is intended for use in unit testing only
		 */
		int getUniqueIDSeed() {
			return uniqueIDSeed;
		}

		/**
		 * Compares an Object {@code obj} with this {@code StockItem} object comparing the following fields for equality:<br>
		 * - {@code name}<br>
		 * - {@code ageRestricted}<br>
		 * - {@code priceInPence}<br>
		 * - {@code storageTemp}<br>
		 * <hr>
		 * Fields intentionally NOT checked for equality:<br>
		 * - {@code uniqueID}<br>
		 * - {@code sellHistory}<br>
		 * - {@code supplier}<br>
		 * <br>
		 * 
		 * @param obj any Object
		 * @return true if the parameter object is an instanceof {@code StockItem}, and the fields {@code name}, {@code ageRestricted}, {@code priceInPence}
		 *         and {@code storageTemp} have equal values, otherwise returns false
		 */
		@Override
		public boolean equals(Object obj) throws IllegalArgumentException {
			if (obj == null) {
				throw new IllegalArgumentException(StockMessages.MSG_EQUALS_NULL_PARAM.get());
			}
			if (!(obj instanceof AbstractStockItem)) {
				return false;
			}

			AbstractStockItem paramItem = (AbstractStockItem) obj;

			if (!this.getName().equals(paramItem.getName())
					|| this.isAgeRestricted() != paramItem.isAgeRestricted()
					|| this.getPriceInPence() != paramItem.getPriceInPence()
					|| this.getStorageTemp() != paramItem.getStorageTemp()) {
				return false;
			}

			return true;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 * @throws IllegalArgumentException if name is null or blank
		 */
		public void setName(String name) throws IllegalArgumentException {
			if (name != null && !name.isBlank()) {
				this.name = name;
			} else if (name == null) {
				throw new IllegalArgumentException(StockMessages.MSG_SET_NAME_NULL.get());
			} else {
				throw new IllegalArgumentException(StockMessages.MSG_SET_NAME_BLANK.get());
			}
		}

		/**
		 * @return the uniqueID
		 */
		public String getUniqueID() {
			return uniqueID;
		}

		/**
		 * Generates a unique 15 character identifier for this item base on the prefix incremented {@code uniqueIDSeed} field, and sets the {@code uniqueID}
		 * field to this
		 * value. Eg, "123456789123456". This identifier is intended to be immutable and unchanging. A confidently unique permanent identifier.
		 */
		private void setUniqueID() {
			this.uniqueID = String.format("%015d", ++uniqueIDSeed);
		}

		/**
		 * @return the ageRestricted
		 */
		public boolean isAgeRestricted() {
			return ageRestricted;
		}

		/**
		 * @param ageRestricted the ageRestricted to set
		 */
		public void setAgeRestricted(boolean ageRestricted) {
			this.ageRestricted = ageRestricted;
		}

		/**
		 * @return the sellHistory
		 */
		public Hashtable<LocalDateTime, Integer> getSellHistory() {
			return sellHistory;
		}

		/**
		 * @param sellHistory the sellHistory to set
		 * @throws IllegalArgumentException if sellHistory parameter is null or empty (specifically is of size 0)
		 */
		public void setSellHistory(Hashtable<LocalDateTime, Integer> sellHistory) throws IllegalArgumentException {
			if (sellHistory != null && sellHistory.size() > 0) {
				this.sellHistory = sellHistory;
			} else if (sellHistory == null) {
				throw new IllegalArgumentException(StockMessages.MSG_SET_SELLHISTORY_NULL.get());
			} else {
				throw new IllegalArgumentException(StockMessages.MSG_SET_SELLHISTORY_EMPTY.get());
			}
		}

		/**
		 * @return the priceInPence
		 */
		public int getPriceInPence() {
			return priceInPence;
		}

		/**
		 * @param priceInPence the priceInPence to set
		 */
		public void setPriceInPence(int priceInPence) throws IllegalArgumentException {
			if (priceInPence >= 1) {
				this.priceInPence = priceInPence;
			} else {
				throw new IllegalArgumentException(StockMessages.MSG_SET_PRICE_LESS_THAN_ONE.get() + priceInPence);
			}
		}

		/**
		 * @return the storageTemp
		 */
		public StorageTemp getStorageTemp() {
			return storageTemp;
		}

		/**
		 * @param storageTemp the storageTemp to set
		 */
		public void setStorageTemp(StorageTemp storageTemp) throws IllegalArgumentException {
			if (storageTemp != null) {
				this.storageTemp = storageTemp;
			} else {
				throw new IllegalArgumentException(StockMessages.MSG_SET_STORAGETEMP_NULL.get());
			}
		}

	}

}
