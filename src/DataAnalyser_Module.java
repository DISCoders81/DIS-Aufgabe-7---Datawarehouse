import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;


/**
 * 
 */

/**
 * @author joantomaspape
 * 
 */
public class DataAnalyser_Module {

	int			articleDimension	= 1;
	int			timeDimension		= 1;
	int			shopDimension		= 1;
	Connection	outputConnection;


	public DataAnalyser_Module(Connection outputConnection) {

		this.outputConnection = outputConnection;
		readDataFromDB();
	}


	private void readDataFromDB() {

		PreparedStatement stmt;
		try
		{
			stmt = outputConnection
					.prepareStatement("SELECT S.name, D.day, A.name, SUM(SA.amount), SUM(SA.revenue) FROM SALES SA INNER JOIN ARTICLE A ON SA.article = A.articleId INNER JOIN DAY D ON SA.day = d.day INNER JOIN SHOP S ON SA.shop = S.shopId GROUP BY CUBE(S.name, D.day, A.name)");
			ResultSet rs = stmt.executeQuery();

			printResultSet(rs);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void printResultSet(ResultSet rs) {

		System.out.println("--------------------------------------------------------------------------");

		try
		{
			ResultSetMetaData rsmeta = rs.getMetaData();
			int columns = rsmeta.getColumnCount();
			int[] columnSizes = printTableHeaders(rsmeta);
			while (rs.next())
			{
				for (int i = 1; i <= columns; i++)
				{
					int columnSize = columnSizes[i - 1] - (rs.getString(i) != null ? rs.getString(i).length() : 4);
					if (i > 1)
					{
						System.out.print("-|-");
						if (columnSize > 3)
							columnSize = columnSize - 3;
					}

					char[] spaces = new char[columnSize / 2];
					Arrays.fill(spaces, ' ');
					String frontSeperator = new String(spaces);
					String backSeperator = new String(spaces);

					System.out.print(frontSeperator + rs.getString(i) + backSeperator);
				}
				System.out.println("");
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private int[] printTableHeaders(ResultSetMetaData rsmeta) throws SQLException {

		int[] displaySizes = new int[rsmeta.getColumnCount()];
		System.out.print("||");
		for (int i = 1; i <= rsmeta.getColumnCount(); i++)
		{
			displaySizes[i - 1] = rsmeta.getColumnDisplaySize(i);
			int size = rsmeta.getColumnDisplaySize(i) - 7; // 7 is the size of
															// the |-- . --|
			size -= rsmeta.getTableName(i).length();
			size -= rsmeta.getColumnLabel(i).length();
			char[] spaces = new char[size / 2];
			Arrays.fill(spaces, ' ');
			String frontSeperator = new String(spaces);
			String backSeperator = new String(spaces);

			System.out.print("|--" + frontSeperator + rsmeta.getTableName(i) + "." + rsmeta.getColumnLabel(i) + backSeperator + "--|");
		}
		System.out.print("||\n");
		return displaySizes;
	}


	public void printCube() {

		System.out.println("--------------------------------------------------------------------------");
		// System.out.println("Year|        Sales        |")
	}

}
