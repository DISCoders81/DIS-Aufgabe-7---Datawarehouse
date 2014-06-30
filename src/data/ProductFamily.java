package data;

public class ProductFamily {

	String			name;
	int				productFamiliyId;
	ProductCategory	productCategory;


	public ProductFamily(String name, int productFamiliyId, ProductCategory productCategory) {

		super();
		this.name = name;
		this.productFamiliyId = productFamiliyId;
		this.productCategory = productCategory;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getProductFamiliyId() {

		return productFamiliyId;
	}


	public void setProductFamiliyId(int productFamiliyId) {

		this.productFamiliyId = productFamiliyId;
	}


	public ProductCategory getProductCategory() {

		return productCategory;
	}


	public void setProductCategory(ProductCategory productCategory) {

		this.productCategory = productCategory;
	}

}
