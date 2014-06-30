package data;

public class Sales {

	Article	article;
	Shop	shop;
	Day		day;
	int		amount;
	double	revenue;


	public Sales(Article article, Shop shop, Day day, int amount, double revenue) {

		super();
		this.article = article;
		this.shop = shop;
		this.day = day;
		this.amount = amount;
		this.revenue = revenue;
	}


	public Article getArticle() {

		return article;
	}


	public void setArticle(Article article) {

		this.article = article;
	}


	public Shop getShop() {

		return shop;
	}


	public void setShop(Shop shop) {

		this.shop = shop;
	}


	public Day getDay() {

		return day;
	}


	public void setDay(Day day) {

		this.day = day;
	}


	public int getAmount() {

		return amount;
	}


	public void setAmount(int amount) {

		this.amount = amount;
	}


	public double getRevenue() {

		return revenue;
	}


	public void setRevenue(double revenue) {

		this.revenue = revenue;
	}
}
