package model.stock;

import model.stock.STKEnums.CategoryLiquidFuel;
import model.stock.STKEnums.StorageTemp;

public class STKLiquidFuelItem extends STKAbstractItem {

	private CategoryLiquidFuel category;
	private int volumeInMillilitres;

	public STKLiquidFuelItem(String name, boolean ageRestricted, int priceInPence, StorageTemp storageTemp, CategoryLiquidFuel category, int volumeInMillilitres) {
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
