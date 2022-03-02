package model;

import java.time.LocalDateTime;
import java.util.Hashtable;

import model.ModelEnums.CategoryFloorItem;
import model.ModelEnums.CategoryLiquidFuel;
import model.ModelEnums.CategorySolidFuel;
import model.ModelEnums.Messages;
import model.ModelEnums.Messages;
import model.ModelEnums.StorageTemp;

public class StockItemModel {

	private static int uniqueIDSeed = 0;

	
	/**
	 * The abstract superclass for all Stock objects
	 * 
	 * @author Peter Marley
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
				throw new IllegalArgumentException(Messages.STOCK_EQUALS_NULL_PARAM.get());
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
				throw new IllegalArgumentException(Messages.STOCK_SET_NAME_NULL.get());
			} else {
				throw new IllegalArgumentException(Messages.STOCK_SET_NAME_BLANK.get());
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
				throw new IllegalArgumentException(Messages.STOCK_SET_SELLHISTORY_NULL.get());
			} else {
				throw new IllegalArgumentException(Messages.STOCK_SET_SELLHISTORY_EMPTY.get());
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
				throw new IllegalArgumentException(Messages.STOCK_SET_PRICE_LESS_THAN_ONE.get() + priceInPence);
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
				throw new IllegalArgumentException(Messages.STOCK_SET_STORAGETEMP_NULL.get());
			}
		}

	}

	/**
	 * A Stock Item, any tangible "off the shelf" item from a shop
	 * 
	 * @author Peter Marley
	 *
	 */
	public class StockFloorItem extends AbstractStockItem {

		private CategoryFloorItem category;

		/**
		 * Fully parameterised constructor for a FloorItem object
		 * 
		 * @param name
		 * @param ageRestricted
		 * @param priceInPence
		 * @param storageTemp
		 * @param category
		 */
		public StockFloorItem(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategoryFloorItem category) {
			super(name, ageRestricted, priceInPence, storageTemp);
			this.setCategory(category);
		}

		/**
		 * @return the category
		 */
		public CategoryFloorItem getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 * @throws IllegalArgumentException if category is null
		 */
		public void setCategory(CategoryFloorItem category) throws IllegalArgumentException {
			if (category != null) {
				this.category = category;
			} else {
				throw new IllegalArgumentException("FloorItem category cannot be set to null");
			}
		}

	}

	/**
	 * Liquid Fuel, dispensed from pumps, or from a tank
	 * 
	 * @author Peter Marley
	 *
	 */
	public class StockLiquidFuel extends AbstractStockItem {

		private CategoryLiquidFuel category;
		private int volumeInMillilitres;

		public StockLiquidFuel(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategoryLiquidFuel category, int volumeInMillilitres) {
			super(name, ageRestricted, priceInPence, storageTemp);
			this.setCategory(category);
			this.setVolumeInMillilitres(volumeInMillilitres);
		}

		/**
		 * @return the category
		 */
		public CategoryLiquidFuel getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 * @throws IllegalArgumentException if category is null
		 */
		public void setCategory(CategoryLiquidFuel category) throws IllegalArgumentException {
			if (category != null) {
				this.category = category;
			} else {
				throw new IllegalArgumentException("LiquidFuelItem category cannot be null");
			}
		}

		/**
		 * @return the volumeInMillilitres
		 */
		public int getVolumeInMillilitres() {
			return volumeInMillilitres;
		}

		/**
		 * @param volumeInMillilitres the volumeInMillilitres to set
		 * @throws IllegalArgumentException if volumeInMillilitres is less than 0
		 */
		public void setVolumeInMillilitres(int volumeInMillilitres) throws IllegalArgumentException {
			if (volumeInMillilitres >= 0) {
				this.volumeInMillilitres = volumeInMillilitres;
			} else {
				throw new IllegalArgumentException("LiquidFuelItem volumeInMillilitres cannot be less than 0");
			}
		}

	}

	/**
	 * Solid Fuel, sold by bag of certain weight
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public class StockSolidFuel extends AbstractStockItem {

		private CategorySolidFuel category;
		private int unitsInStock;
		private int weightInGrams;

		/**
		 * Fully Parameterised Constructor for this SolidFuelItem object
		 * 
		 * @param name
		 * @param ageRestricted
		 * @param priceInPence
		 * @param storageTemp
		 * @param category
		 * @param weightInGrams
		 */
		public StockSolidFuel(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategorySolidFuel category, int weightInGrams) {
			super(name, ageRestricted, priceInPence, storageTemp);
			this.setCategory(category);
			this.setUnitsInStock(0);
			this.setWeightInGrams(weightInGrams);
		}

		/**
		 * @return the category
		 */
		public CategorySolidFuel getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 * @throws IllegalArgumentException if category is null
		 */
		public void setCategory(CategorySolidFuel category) throws IllegalArgumentException {
			if (category != null) {
				this.category = category;
			} else {
				throw new IllegalArgumentException("SolidFuelItem category cannot be null");
			}
		}

		/**
		 * @return the unitsInStock
		 */
		public int getUnitsInStock() {
			return unitsInStock;
		}

		/**
		 * @param unitsInStock the unitsInStock to set
		 * @throws IllegalArgumentException if unitsInStock is less than 0
		 */
		public void setUnitsInStock(int unitsInStock) throws IllegalArgumentException {
			if (unitsInStock >= 0) {
				this.unitsInStock = unitsInStock;
			} else {
				throw new IllegalArgumentException("SolidFuelItem unitsInStock cannot be less than 0 but was " + unitsInStock);
			}
		}

		/**
		 * Adds a specified number of units to {@code unitsInStock}
		 * 
		 * @param unitsToAdd
		 * @throws IllegalArgumentException if unitsToAdd is 0 or less
		 */
		public void addUnitsInStock(int unitsToAdd) throws IllegalArgumentException {
			if (unitsToAdd > 0) {
				this.setUnitsInStock(this.getUnitsInStock() + unitsToAdd);
			} else {
				throw new IllegalArgumentException("SolidFuelItem unitsToAdd must be greater than 0 but was " + unitsToAdd);
			}
		}

		/**
		 * Removes a specified number of units from {@code unitsInStock}
		 * 
		 * @param unitsToRemove
		 * @throws IllegalArgumentException if units to remove is 0 or less, or if the {@code unitsInStock} would be reduced to less than 0
		 */
		public void removeUnitsInStock(int unitsToRemove) throws IllegalArgumentException {
			if (unitsToRemove > 0 && this.getUnitsInStock() - unitsToRemove >= 0) {
				this.setUnitsInStock(this.getUnitsInStock() - unitsToRemove);
			} else if (unitsToRemove <= 0) {
				throw new IllegalArgumentException("SolidFuelItem unitsToRemove must be greater than 0 but was " + unitsToRemove);
			} else {
				throw new IllegalArgumentException("SolidFuelItem unitsInStock cannot be reduced below 0 but was attempted to be reduced to " + (this.getUnitsInStock() - unitsToRemove));
			}
		}

		/**
		 * @return the weightInGrams
		 */
		public int getWeightInGrams() {
			return weightInGrams;
		}

		/**
		 * @param weightInGrams the weightInGrams to set
		 * @throws IllegalArgumentException if weightInGrams is less than 1
		 */
		public void setWeightInGrams(int weightInGrams) throws IllegalArgumentException {
			if (weightInGrams >= 1) {
				this.weightInGrams = weightInGrams;
			} else {
				throw new IllegalArgumentException("SolidFuelItem weightInGrams must be 1 or greater but was " + weightInGrams);
			}
		}

	}

}
