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
		etlModule.loadAllArticles();
		etlModule.loadAllProductGroups();
		etlModule.loadAllProductFamilies();
		etlModule.loadAllProductCategories();

		etlModule.readSales();

	}

}
