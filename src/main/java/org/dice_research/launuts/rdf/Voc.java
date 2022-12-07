package org.dice_research.launuts.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.SKOS;

/**
 * LauNuts Vocabulary.
 * 
 * Example URI: https://w3id.org/launuts/nuts/2016#DEA47 (Paderborn)
 * 
 * Example URI: https://w3id.org/launuts/lau/2016/DE#05774032 (Paderborn, Stadt)
 *
 * @author Adrian Wilke
 */
public abstract class Voc {

	private static final Model model = ModelFactory.createDefaultModel();

	public static final String PREFIX_DBO = "https://dbpedia.org/ontology/";
	public static final String PREFIX_NUTS = "https://w3id.org/launuts/nuts/";
	public static final String PREFIX_LAU = "https://w3id.org/launuts/lau/";

	// --- PROPERTIES ---

	// SKOS

	public static final Property SKOS_hasTopConcept = SKOS.hasTopConcept;
	public static final Property SKOS_inScheme = SKOS.inScheme;

	public static final Property SKOS_broader = SKOS.broader;
	public static final Property SKOS_narrower = SKOS.narrower;

	public static final Property SKOS_notation = SKOS.notation;
	public static final Property SKOS_prefLabel = SKOS.prefLabel;
	public static final Property SKOS_altLabel = SKOS.altLabel;

	// DBpedia

	// https://dbpedia.org/ontology/populationTotal
	public static final Property DBO_populationTotal = model.createProperty(PREFIX_DBO + "populationTotal");

	// https://dbpedia.org/ontology/area
	public static final Property DBO_area = model.createProperty(PREFIX_DBO + "area");

	/**
	 * Constructs NUTS URI including NUTS scheme and NUTS CODE.
	 * 
	 * Example URI: https://w3id.org/launuts/nuts/2016#DEA47 (Paderborn).
	 * 
	 * @param nutsSchema schema, e.g. 2016
	 * @param nutsCode   nutsCode, e.g. "FR101" (Paris) or "DE" (Germany)
	 * @return LauNuts URI of NUTS schema and NUTS code
	 */
	public static String getNutsWithSchemeUri(Integer nutsSchema, String nutsCode) {
		return PREFIX_NUTS + nutsSchema + "#" + nutsCode;
	}

	/**
	 * Constructs LAU URI.
	 * 
	 * For LAU until 2016, two LAU codes were used. However, in some cases the first
	 * LAU code is not available or does not exist. Therefore, no URI can be
	 * created. As a result, two-level LAU items are encoded as one combined URI.
	 * See the following examples.
	 * 
	 * Example URI: https://w3id.org/launuts/lau/2021/DE#05774032 (Paderborn,
	 * Stadt).
	 * 
	 * Example URI: https://w3id.org/launuts/lau/2016/DE#-05774032 (Paderborn,
	 * Stadt).
	 * 
	 * Example URI: https://w3id.org/launuts/lau/2016/FR#75-75056 (Paris).
	 * 
	 * @param lauSchema     schema, e.g. 2016
	 * @param countryCode   countryCode, e.g. FR
	 * @param lauCode,      e.g. 75
	 * @param lauCodeSecond e.g. 75056, null or empty. Only used until 2016,
	 * @return
	 */
	public static String getLauUri(Integer lauSchema, String countryCode, String lauCode, String lauCodeSecond) {
		if (lauSchema <= 2016) {
			if (lauCodeSecond == null)
				lauCodeSecond = "";
			return PREFIX_LAU + lauSchema + "/" + countryCode + "#" + lauCode + "-" + lauCodeSecond;
		} else {
			return PREFIX_LAU + lauSchema + "/" + countryCode + "#" + lauCode;
		}
	}
}