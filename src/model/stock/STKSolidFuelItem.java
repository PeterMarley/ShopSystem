package model.stock;

import model.stock.STKEnums.CategorySolidFuel;
import model.stock.STKEnums.StorageTemp;

/**
 * Class representing a SolidFuelItem such as a bag of coal, wood etc
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class STKSolidFuelItem extends STKAbstractItem {

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
	public STKSolidFuelItem(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategorySolidFuel category, int weightInGrams) {
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
