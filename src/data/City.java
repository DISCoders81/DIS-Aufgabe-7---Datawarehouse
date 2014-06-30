package data;

public class City {

	String	name;
	int		cityId;
	Region	region;


	public City(String name, int cityId, Region region) {

		super();
		this.name = name;
		this.cityId = cityId;
		this.region = region;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getCityId() {

		return cityId;
	}


	public void setCityId(int cityId) {

		this.cityId = cityId;
	}


	public Region getRegion() {

		return region;
	}


	public void setRegion(Region region) {

		this.region = region;
	}

}
