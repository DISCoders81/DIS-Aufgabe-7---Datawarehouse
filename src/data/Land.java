package data;

public class Land {

	String	name;
	int		landId;


	public Land(String name, int landId) {

		super();
		this.name = name;
		this.landId = landId;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getLandId() {

		return landId;
	}


	public void setLandId(int landId) {

		this.landId = landId;
	}

}
