package data;

public class Day {

	String	date;
	int		dateId;
	Month	month;


	public Day(String date, int dateId, Month month) {

		super();
		this.date = date;
		this.dateId = dateId;
		this.month = month;
	}


	public String getDate() {

		return date;
	}


	public void setDate(String date) {

		this.date = date;
	}


	public int getDateId() {

		return dateId;
	}


	public void setDateId(int dateId) {

		this.dateId = dateId;
	}


	public Month getMonth() {

		return month;
	}


	public void setMonth(Month month) {

		this.month = month;
	}

}
