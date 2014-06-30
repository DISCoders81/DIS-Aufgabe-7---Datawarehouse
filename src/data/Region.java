package data;

public class Region {

	String	name;
	int		regionId;
	Land	land;


	public Region(String name, int regionId, Land land) {

		super();
		this.name = name;
		this.regionId = regionId;
		this.land = land;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getRegionId() {

		return regionId;
	}


	public void setRegionId(int regionId) {

		this.regionId = regionId;
	}


	public Land getLand() {

		return land;
	}


	public void setLand(Land land) {

		this.land = land;
	}

}
