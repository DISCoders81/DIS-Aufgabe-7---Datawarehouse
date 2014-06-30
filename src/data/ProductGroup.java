package data;

public class ProductGroup {

	String			name;
	int				productGroupId;
	ProductFamily	productFamily;


	public ProductGroup(String name, int productGroupId, ProductFamily productFamily) {

		super();
		this.name = name;
		this.productGroupId = productGroupId;
		this.productFamily = productFamily;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getProductGroupId() {

		return productGroupId;
	}


	public void setProductGroupId(int productGroupId) {

		this.productGroupId = productGroupId;
	}


	public ProductFamily getProductFamily() {

		return productFamily;
	}


	public void setProductFamily(ProductFamily productFamily) {

		this.productFamily = productFamily;
	}
}
