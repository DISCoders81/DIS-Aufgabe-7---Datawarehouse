package data;

public class Shop {

	String	name;
	int		shopId;
	City	city;


	public Shop(String name, int shopId, City city) {

		super();
		this.name = name;
		this.shopId = shopId;
		this.city = city;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getShopId() {

		return shopId;
	}


	public void setShopId(int shopId) {

		this.shopId = shopId;
	}


	public City getCity() {

		return city;
	}


	public void setCity(City city) {

		this.city = city;
	}

}
