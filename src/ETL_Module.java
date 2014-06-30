import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
			reader = new BufferedReader(new FileReader("/DIS Aufgabe 7 - Datawarehouse/src/resources/sales.csv"));
			String line = reader.readLine();
			String[] values;

			while (line != null)
			{
				values = line.split("\\,");
				System.out.println("Values: " + values[0] + " | " + values[1] + " | " + values[2] + " | " + values[3] + " | " + values[4]);

			}

		} catch (IOException e)
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

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void loadAllShops() {

		System.out.println("\nLoading Shops..");
		try
		{
			String selectSQL = "SELECT * FROM SHOPID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO Shop(SHOPID, NAME,CITYID) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("SHOPID"));
				currentOperation += " ShopId = '" + rs.getInt("SHOPID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("CITYID"));
				currentOperation += " PrGroupID = '" + rs.getInt("CITYID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void loadAllCities() {

		System.out.println("\nLoading Cities..");

		try
		{
			String selectSQL = "SELECT * FROM CITYID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO City(CITYID, NAME,REGIONID) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("CITYID"));
				currentOperation += " CityId = '" + rs.getInt("CITYID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("REGIONID"));
				currentOperation += " RegionID = '" + rs.getInt("REGIONID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void loadAllRegions() {

		System.out.println("\nLoading Regions..");
		try
		{
			String selectSQL = "SELECT * FROM REGIONID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO Region(RegionID, NAME,LANDID) VALUES (?,?,?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setInt(1, rs.getInt("REGIONID"));
				currentOperation += " RegionId = '" + rs.getInt("REGIONID") + "' -";

				insertStatement.setString(2, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "' -";

				insertStatement.setInt(3, rs.getInt("LANDID"));
				currentOperation += " LandID = '" + rs.getInt("LANDID") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void loadAllLands() {

		System.out.println("\nLoading Lands..");
		try
		{
			String selectSQL = "SELECT * FROM PRODUCTCATEGORYID";
			PreparedStatement pstmt = inputConnection.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement insertStatement = outputConnection.prepareStatement("INSERT INTO LAND(LANDID, NAME) VALUES (?)");

			while (rs.next())
			{
				String currentOperation = "Added to Batch: ";

				insertStatement.setString(1, rs.getString("LANDID"));
				currentOperation += " LandID = '" + rs.getString("LANDID") + "'";

				insertStatement.setString(1, rs.getString("NAME"));
				currentOperation += " Name = '" + rs.getString("NAME") + "'";

				insertStatement.addBatch();

				System.out.println(currentOperation);

			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
