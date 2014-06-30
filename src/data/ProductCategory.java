package data;

public class ProductCategory {

	String	name;
	int		productCategoryId;


	public ProductCategory(String name, int productCategoryId) {

		super();
		this.name = name;
		this.productCategoryId = productCategoryId;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getProductCategoryId() {

		return productCategoryId;
	}


	public void setProductCategoryId(int productCategoryId) {

		this.productCategoryId = productCategoryId;
	}

}
