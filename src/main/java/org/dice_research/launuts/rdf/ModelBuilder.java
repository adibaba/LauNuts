package org.dice_research.launuts.rdf;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.launuts.csv.LauCsvCollection;
import org.dice_research.launuts.csv.LauCsvItem;
import org.dice_research.launuts.csv.NutsCsvCollection;
import org.dice_research.launuts.csv.NutsCsvItem;
import org.dice_research.launuts.linking.WikipediaLinking;

/**
 * Creates Knowledge Graph.
 * 
 * @author Adrian Wilke
 */
public class ModelBuilder {

	public List<LauCsvCollection> lauCsvCollections = new LinkedList<>();
	public List<NutsCsvCollection> nutsCsvCollections = new LinkedList<>();
	public Statistics statistics = new Statistics();

	public Model build() {
		Model model = ModelFactory.createDefaultModel();

		for (LauCsvCollection lauCsvCollection : lauCsvCollections) {
			addLauCsvCollection(model, lauCsvCollection);
		}

		for (NutsCsvCollection nutsCsvCollection : nutsCsvCollections) {
			addNutsCsvCollection(model, nutsCsvCollection);
		}

		addRelated(model);

		System.out.println(statistics);

		return model;
	}

	private void addLauCsvCollection(Model model, LauCsvCollection lauCsvCollection) {
		for (LauCsvItem item : lauCsvCollection.lauCsvItems) {

			// Mandatory values
			if (!item.hasLauSchema() || !item.hasNutsSchema() || !item.hasLauCode() || !item.hasNutsCode()) {
				System.err.println("Warning: Value missing " + item.toString() + " " + getClass().getSimpleName());
				continue;
			}

			// LAU to NUTS
			Resource resUniqueNuts = ResourceFactory
					.createResource(Voc.getUniqueNutsUri(item.nutsSchema, item.nutsCodeToString()));
			Resource resUniqueLau = ResourceFactory.createResource(Voc.getUniqueLauUri(item.lauSchema,
					item.getCountryCode(), item.lauCodeToString(), item.lauCodeSecondToString()));
			model.add(resUniqueLau, Voc.SKOS_broader, resUniqueNuts);

			// Schema
			Resource resLauSchema = ResourceFactory.createResource(Voc.getLauSchemeUri(item.lauSchema));
			if (!model.containsResource(resLauSchema)) {
				Literal litDate = ResourceFactory.createTypedLiteral(item.lauSchema + "-01-01", XSDDatatype.XSDdate);
				model.addLiteral(resLauSchema, Voc.DCT_issued, litDate);
			}
			model.add(resLauSchema, RDF.type, Voc.resLauScheme);
			model.add(resUniqueLau, Voc.SKOS_inScheme, resLauSchema);

			// General LAU
			Resource resLau = ResourceFactory.createResource(
					Voc.getLauUri(item.getCountryCode(), item.lauCodeToString(), item.lauCodeSecondToString()));
			if (!model.containsResource(resLau)) {
				model.addLiteral(resLau, Voc.SKOS_notation, ResourceFactory.createPlainLiteral(item.lauCode));
			}
			model.add(resLau, Voc.SKOS_inScheme, resLauSchema);
			model.add(resUniqueLau, Voc.SKOS_hasTopConcept, resLau);

			// Literals: Names
			if (item.hasNameLatin())
				model.addLiteral(resUniqueLau, Voc.SKOS_prefLabel, ResourceFactory.createPlainLiteral(item.nameLatin));
			if (item.hasNameNational())
				// National names are only added if not already addded in latin
				if (!item.nameNational.equals(item.nameLatin))
					model.addLiteral(resUniqueLau, Voc.SKOS_altLabel,
							ResourceFactory.createPlainLiteral(item.nameNational));

			// Literals: Numbers
			if (item.hasArea())
				model.addLiteral(resUniqueLau, Voc.DBO_area, ResourceFactory.createTypedLiteral(item.area));
			if (item.hasPopulation())
				model.addLiteral(resUniqueLau, Voc.DBO_populationTotal,
						ResourceFactory.createTypedLiteral(item.population));

			statistics.countLauCsvItem(item);
		}
	}

	private void addNutsCsvCollection(Model model, NutsCsvCollection nutsCsvCollection) {
		for (NutsCsvItem item : nutsCsvCollection.getAll()) {

			// Mandatory values
			if (!item.hasNutsSchema() || !item.hasName() || !item.hasNutsCode()) {
				System.err.println("Warning: Value missing " + item.toString() + " " + getClass().getSimpleName());
				continue;
			}

			Resource resUniqueNuts = ResourceFactory
					.createResource(Voc.getUniqueNutsUri(item.nutsSchema, item.nutsCode));

			// NUTS scheme
			Resource resNutsScheme = ResourceFactory.createResource(Voc.getNutsSchemeUri(item.nutsSchema));
			if (!model.containsResource(resNutsScheme)) {
				Literal litDate = ResourceFactory.createTypedLiteral(item.nutsSchema + "-01-01", XSDDatatype.XSDdate);
				model.addLiteral(resNutsScheme, Voc.DCT_issued, litDate);
			}
			model.add(resNutsScheme, RDF.type, Voc.resNutsScheme);
			model.add(resUniqueNuts, Voc.SKOS_inScheme, resNutsScheme);

			// NUTS
			Resource resNuts = ResourceFactory.createResource(Voc.getNutsCodeUri(item.nutsCode));
			if (!model.containsResource(resNuts)) {
				model.addLiteral(resNuts, Voc.SKOS_notation, ResourceFactory.createPlainLiteral(item.nutsCode));
			}
			model.add(resNuts, Voc.SKOS_inScheme, resNutsScheme);
			model.add(resUniqueNuts, Voc.SKOS_hasTopConcept, resNuts);

			// Add NUTS label/name
			model.addLiteral(resUniqueNuts, Voc.SKOS_prefLabel, ResourceFactory.createPlainLiteral(item.name));

			// Add hierarchy
			if (item.getLevel() > 0) {
				Resource resbroaderLaunutsNuts = ResourceFactory.createResource(
						Voc.getUniqueNutsUri(item.nutsSchema, item.nutsCode.substring(0, item.nutsCode.length() - 1)));
				model.add(resUniqueNuts, Voc.SKOS_broader, resbroaderLaunutsNuts);
			}
			if (item.getLevel() == 0) {
				model.add(resNuts, VocEu.EU_level, Voc.resLevel0);
			} else if (item.getLevel() == 1) {
				model.add(resNuts, VocEu.EU_level, Voc.resLevel1);
			} else if (item.getLevel() == 2) {
				model.add(resNuts, VocEu.EU_level, Voc.resLevel2);
			} else if (item.getLevel() == 3) {
				model.add(resNuts, VocEu.EU_level, Voc.resLevel3);
			}

			// Eurostat NUTS scheme
			Resource resEurostatNutsScheme = ResourceFactory.createResource(VocEu.getNutsSchemeUri(item.nutsSchema));
			model.add(resNutsScheme, Voc.OWL_sameAs, resEurostatNutsScheme);

			// Eurostat NUTS
			Resource resEurostatNuts = ResourceFactory.createResource(VocEu.getNutsCodeUri(item.nutsCode));
			model.add(resNuts, Voc.OWL_sameAs, resEurostatNuts);

			statistics.countNutsCsvItem(item);
		}
	}

	private void addRelated(Model model) {
		WikipediaLinking wikipediaLinking = new WikipediaLinking();
		String markdown = wikipediaLinking.downloadWpNuts1().getWpNuts1Markdown();
		Map<String, String> codesToUris = wikipediaLinking.getWpNuts1CodesToUris(markdown);
		for (Entry<String, String> codeToUri : codesToUris.entrySet()) {
			String uniqueNutsUri = Voc.getUniqueNutsUri(2021, codeToUri.getKey());
			Resource nutsRes = ResourceFactory.createResource(uniqueNutsUri);
			if (model.containsResource(nutsRes)) {
				Resource wpRes = ResourceFactory.createResource(codeToUri.getValue());
				model.add(nutsRes, Voc.SKOS_related, wpRes);
				statistics.countLinked();
			} else
				System.err.println("Info: Not linked: " + uniqueNutsUri + " " + getClass().getSimpleName());
		}
	}
}