import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * 
 */

/**
 * @author joantomaspape
 * 
 */
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try
		{
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection outputConnection = connectToOutputDatabase();
		menu(outputConnection);

	}


	private static Connection connectToOutputDatabase() {

		Connection result = null;
		try
		{
			result = DriverManager.getConnection("jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP", "vsisp81", "of7GeMtY");
			result.prepareStatement("SET SCHEMA VSISP81").execute();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


	private static void loadData(Connection outputConnection) {

		ETL_Module etlModule = new ETL_Module(outputConnection);

		etlModule.loadAllProductCategories();
		etlModule.loadAllProductFamilies();
		etlModule.loadAllProductGroups();
		etlModule.loadAllArticles();

		etlModule.loadAllLands();
		etlModule.loadAllRegions();
		etlModule.loadAllCities();
		etlModule.loadAllShops();

		etlModule.readSales();
	}


	private static void startDataAnalysingTool(Connection outputConnection) {

		DataAnalyser_Module dataAnalyser = new DataAnalyser_Module(outputConnection);

	}


	private static void menu(Connection outputConnection) {

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("This is the database analysing tool.\n");
		try
		{

			while (true)
			{
				System.out.println("\n--MAIN MENU--");

				System.out.println("Import the data into the database - (1)");
				System.out.println("Start data analysing tool - (2)");

				String enable = inputReader.readLine();

				if (enable.equalsIgnoreCase("1"))
				{
					System.out.println("You chose to import data.");
					loadData(outputConnection);
					System.out.println("---");
				} else if (enable.equalsIgnoreCase("2"))
				{
					System.out.println("You chose to start the data analying tool.");
					startDataAnalysingTool(outputConnection);
					System.out.println("---");
				} else
				{
					System.err.println("Input incorrect. Try again..");
					Thread.sleep(150);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				inputReader.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}
}
