package data;

public class Quarter {

	String	name;
	int		quarterId;
	Year	year;


	public Quarter(String name, int quarterId, Year year) {

		super();
		this.name = name;
		this.quarterId = quarterId;
		this.year = year;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getQuarterId() {

		return quarterId;
	}


	public void setQuarterId(int quarterId) {

		this.quarterId = quarterId;
	}


	public Year getYear() {

		return year;
	}


	public void setYear(Year year) {

		this.year = year;
	}

}
