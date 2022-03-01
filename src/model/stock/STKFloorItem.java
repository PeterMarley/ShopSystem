package model.stock;

import model.stock.STKEnums.CategoryFloorItem;
import model.stock.STKEnums.StorageTemp;

public class STKFloorItem extends STKAbstractItem {

	private CategoryFloorItem category;

	/**
	 * Fully parameterised constructor for a FloorItem object
	 * @param name
	 * @param ageRestricted
	 * @param priceInPence
	 * @param storageTemp
	 * @param category
	 */
	public STKFloorItem(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategoryFloorItem category) {
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
