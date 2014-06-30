package data;

public class Article {

	String			name;
	double			price;
	int				articleId;
	ProductGroup	productGroup;


	public Article(String name, int articleId, ProductGroup productGroup) {

		super();
		this.name = name;
		this.articleId = articleId;
		this.productGroup = productGroup;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getArticleId() {

		return articleId;
	}


	public void setArticleId(int articleId) {

		this.articleId = articleId;
	}


	public ProductGroup getProductGroup() {

		return productGroup;
	}


	public void setProductGroup(ProductGroup productGroup) {

		this.productGroup = productGroup;
	}


	public double getPrice() {

		return price;
	}


	public void setPrice(double price) {

		this.price = price;
	}

}
