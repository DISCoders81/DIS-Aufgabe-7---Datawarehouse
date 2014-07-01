import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * @author joantomaspape
 * 
 */
public class ETL_Module {

	Connection	inputConnection;
	Connection	outputConnection;


	public ETL_Module() {

		try
		{
			Class.forName("com.ibm.db2.jcc.DB2Driver");

			this.inputConnection = DriverManager.getConnection("jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP", "vsisp81", "of7GeMtY");
			inputConnection.prepareStatement("SET SCHEMA DB2INST1").execute();

			this.outputConnection = DriverManager.getConnection("jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP", "vsisp81", "of7GeMtY");
			outputConnection.prepareStatement("SET SCHEMA VSISP81").execute();

		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	public void readSales() {

		BufferedReader reader = null;
		try
		{
			String fileName = "src/resources/sales.csv";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));
			String line = reader.readLine();
			line = reader.readLine(); // Skipping the Name Line

			String[] values;
			String previousDate = "";
			int previousYear = 0;
			PreparedStatement dayStatement = outputConnection.prepareStatement("INSERT INTO DAY(day, month, year) VALUES (?, ?, ?)");
			PreparedStatement yearStatement = outputConnection.prepareStatement("INSERT INTO YEAR(year) VALUES (?)");
			PreparedStatement salesStatement = outputConnection.prepareStatement("INSERT INTO SALES(article, shop, day, amount, revenue) VALUES (?,?,?,?,?)");

			while (line != null)
			{
				String currentOperation = "Added to Batch: ";
				values = line.split("\\;");

				int articleId = getIdForArticle(values[2]);
				int shopId = getIdForShop(values[1]);

				salesStatement.setInt(1, articleId);
				currentOperation += " ArticleID = " + articleId + " |";

				salesStatement.setInt(2, shopId);
				currentOperation += " ShopID = " + shopId + " |";

				salesStatement.setString(3, values[0]);
				currentOperation += " Day = " + values[0] + " |";

				salesStatement.setInt(4, Integer.parseInt(values[3]));
				currentOperation += " amount = " + Integer.parseInt(values[3]) + " |";

				NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

				salesStatement.setDouble(5, format.parse(values[4]).doubleValue());
				currentOperation += " revenue = " + format.parse(values[4]).doubleValue() + " |";

				salesStatement.addBatch();
				System.out.println(currentOperation);

				// Adding the Dates on the fly..
				previousYear = addDateToDB(values[0], previousDate, previousYear, dayStatement, yearStatement);
				previousDate = values[0];

				line = reader.readLine();

			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				if (reader != null)
					reader.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private int getIdForShop(String shopName) throws Exception {

		ResultSet rs = outputConnection.createStatement().executeQuery("SELECT * FROM Shop WHERE name = '" + shopName + "'");
		rs.next();
		return rs.getInt("SHOPID");
	}


	private int getIdForArticle(String articleName) throws Exception {

		ResultSet rs = outputConnection.createStatement().executeQuery("SELECT * FROM Article WHERE name = '" + articleName + "'");
		rs.next();
		return rs.getInt("ARTICLEID");
	}


	private int addDateToDB(String dateString, String previousDate, int previousYear, PreparedStatement dayStatement, PreparedStatement yearStatement) throws Exception {

		if (!dateString.equalsIgnoreCase(previousDate))
		{
			String[] date = dateString.split("\\.");

			if (previousYear < Integer.parseInt(date[2]))
			{
				String currentOperation = "Added to Batch: ";
				yearStatement.setInt(1, Integer.parseInt(date[2]));
				currentOperation += " Year = " + date[2];

				yearStatement.addBatch();
				System.out.println(currentOperation);
			}

			String currentOperation = "Added to Batch: ";

			dayStatement.setString(1, dateString);
			currentOperation += "date = " + dateString;

			dayStatement.setInt(2, Integer.parseInt(date[1]));
			currentOperation += " | month = " + Integer.parseInt(date[1]);

			dayStatement.setInt(3, Integer.parseInt(date[2]));
			currentOperation += " | year = " + Integer.parseInt(date[2]);

			dayStatement.addBatch();
			System.out.println(currentOperation);

			return Integer.parseInt(date[2]);
		}

		return previousYear;

	}


	public void loadAllArticles() {

		System.out.println("\nLoading Articles..");
		try
		{
			String selectSQL = "SELECT * FROM ARTICLEID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO Article(ARTICLEID,NAME,PRICE,PRODUCTGROUPID) VALUES (?,?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("ARTICLEID"));
				currentOperation += " ArticleId = '" + rs.getInt("ARTICLEID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setDouble(3, rs.getDouble("PREIS"));
				currentOperation += " Price = '" + rs.getDouble("PREIS") + "' -";

				insertStatement.setInt(4, rs.getInt("PRODUCTGROUPID"));
				currentOperation += " PrGroupID = '" + rs.getInt("PRODUCTGROUPID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);
			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllProductGroups() {

		System.out.println("\nLoading Product Groups..");

		try
		{
			String selectSQL = "SELECT * FROM PRODUCTGROUPID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO ProductGroup(PRODUCTGROUPID,NAME,PRODUCTFAMILYID) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("PRODUCTGROUPID"));
				currentOperation += " ProductGroupID = '" + rs.getInt("PRODUCTGROUPID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("PRODUCTFAMILYID"));
				currentOperation += " PrFamilyID = '" + rs.getInt("PRODUCTFAMILYID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllProductFamilies() {

		System.out.println("\nLoading Product Families..");
		try
		{
			String selectSQL = "SELECT * FROM PRODUCTFAMILYID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO ProductFamily(PRODUCTFAMILYID,NAME,PRODUCTCATEGORYID) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("PRODUCTFAMILYID"));
				currentOperation += " ProductFamilyID = '" + rs.getInt("PRODUCTFAMILYID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("PRODUCTCATEGORYID"));
				currentOperation += " PrCategoryID = '" + rs.getInt("PRODUCTCATEGORYID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllProductCategories() {

		System.out.println("\nLoading Product Categories..");
		try
		{
			String selectSQL = "SELECT * FROM PRODUCTCATEGORYID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO ProductCategory(PRODUCTCATEGORYID, NAME) VALUES (?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("PRODUCTCATEGORYID"));
				currentOperation += " ProductCategoryID = '" + rs.getInt("PRODUCTCATEGORYID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}

		}

	}


	public void loadAllShops() {

		System.out.println("\nLoading Shops..");
		try
		{
			String selectSQL = "SELECT * FROM SHOPID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO Shop(SHOPID, NAME, CITY) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("SHOPID"));
				currentOperation += " ShopId = '" + rs.getInt("SHOPID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("STADTID"));
				currentOperation += " PrGroupID = '" + rs.getInt("STADTID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllCities() {

		System.out.println("\nLoading Cities..");

		try
		{
			String selectSQL = "SELECT * FROM STADTID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO City(CITYID, NAME, REGION) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("STADTID"));
				currentOperation += " CityId = '" + rs.getInt("STADTID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("REGIONID"));
				currentOperation += " RegionID = '" + rs.getInt("REGIONID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllRegions() {

		System.out.println("\nLoading Regions..");
		try
		{
			String selectSQL = "SELECT * FROM REGIONID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO Region(REGIONID, NAME,LAND) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("REGIONID"));
				currentOperation += " RegionId = '" + rs.getInt("REGIONID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("LANDID"));
				currentOperation += " Land = '" + rs.getInt("LANDID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}


	public void loadAllLands() {

		System.out.println("\nLoading Lands..");
		try
		{
			String selectSQL = "SELECT * FROM LANDID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO LAND(LANDID, NAME) VALUES (?, ?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setString(1, rs.getString("LANDID"));
				currentOperation += " LandID = '" + rs.getString("LANDID") + "'";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

			insertStatement.executeBatch();

		} catch (SQLException e)
		{
			while (e.getNextException() != null)
			{
				if (e.getErrorCode() == -803)
				{
					System.err.println("Entry is already in Database");
				}
				if (e.getErrorCode() != -803 && e.getErrorCode() != -99999)
				{
					e.printStackTrace();
				}
				e = e.getNextException();
			}
		}

	}

}
