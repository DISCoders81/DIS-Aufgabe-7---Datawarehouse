package data;

public class Month {

	String	name;
	int		monthId;
	Quarter	quarter;


	public Month(String name, int monthId, Quarter quarter) {

		super();
		this.name = name;
		this.monthId = monthId;
		this.quarter = quarter;
	}


	public String getName() {

		return name;
	}


	public void setName(String name) {

		this.name = name;
	}


	public int getMonthId() {

		return monthId;
	}


	public void setMonthId(int monthId) {

		this.monthId = monthId;
	}


	public Quarter getQuarter() {

		return quarter;
	}


	public void setQuarter(Quarter quarter) {

		this.quarter = quarter;
	}

}
