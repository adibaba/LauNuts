package org.dice_research.launuts;

/**
 * Configuration.
 * 
 * TODO replace by JSON based {@link Config}.
 *
 * @author Adrian Wilke
 */
public abstract class Configuration {

	/**
	 * NUTS CSV in resources directory
	 */
	public static final String DIRECTORY_CSV_NUTS = "data/csv/nuts-csv";

	/**
	 * LAU CSV directory
	 */
	public static final String DIRECTORY_CSV_LAU = "data/csv/lau";

	/**
	 * Location of https://data.europa.eu/euodp/repository/ec/estat/nuts/nuts.rdf
	 */
	public static final String FILE_RDF_NUTS = "data/download/nuts-rdf/nuts.rdf";

	/**
	 * Caching directory
	 */
	public static final String DIRECTORY_CACHE = "cache/";

}