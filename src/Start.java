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

		ETL_Module etlModule = new ETL_Module();

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

}
