package org.dice_research.launuts.rdf;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.dice_research.launuts.csv.LauCsvCollection;
import org.dice_research.launuts.csv.LauCsvItem;
import org.dice_research.launuts.csv.NutsCsvCollection;
import org.dice_research.launuts.csv.NutsCsvItem;

/**
 * Creates Knowledge Graph.
 * 
 * @author Adrian Wilke
 */
public class ModelBuilder {

	public List<LauCsvCollection> lauCsvCollections = new LinkedList<>();
	public List<NutsCsvCollection> nutsCsvCollections = new LinkedList<>();

	public Model build() {
		Model model = ModelFactory.createDefaultModel();
		for (LauCsvCollection lauCsvCollection : lauCsvCollections) {
			addLauCsvCollection(model, lauCsvCollection);
		}
		for (NutsCsvCollection nutsCsvCollection : nutsCsvCollections) {
			addNutsCsvCollection(model, nutsCsvCollection);
		}
		return model;
	}

	private void addLauCsvCollection(Model model, LauCsvCollection lauCsvCollection) {
		for (LauCsvItem item : lauCsvCollection.lauCsvItems) {

			// Mandatory values
			if (!item.hasLauSchema() || !item.hasNutsSchema() || !item.hasLauCode() || !item.hasNutsCode()) {
				System.err.println("Warning: Value missing " + item.toString() + " " + getClass().getSimpleName());
				continue;
			}

			// Resources
			Resource resLaunuts = ResourceFactory
					.createResource(Voc.getNutsWithSchemeUri(item.nutsSchema, item.nutsCodeToString()));
			Resource resLau = ResourceFactory.createResource(Voc.getLauUri(item.lauSchema, item.getCountryCode(),
					item.lauCodeToString(), item.lauCodeSecondToString()));

			// Hierarchy relations
			model.add(resLau, Voc.SKOS_broader, resLaunuts);
			model.add(resLaunuts, Voc.SKOS_narrower, resLau);

			// Literals: Mandatory
			model.addLiteral(resLau, Voc.SKOS_notation, ResourceFactory.createPlainLiteral(item.lauCode));

			// Literals: Names
			if (item.hasNameLatin())
				model.addLiteral(resLau, Voc.SKOS_prefLabel, ResourceFactory.createPlainLiteral(item.nameLatin));
			if (item.hasNameNational())
				// National names are only added if not already addded in latin
				if (!item.nameNational.equals(item.nameLatin))
					model.addLiteral(resLau, Voc.SKOS_altLabel, ResourceFactory.createPlainLiteral(item.nameNational));

			// Literals: Numbers
			if (item.hasArea())
				model.addLiteral(resLau, Voc.DBO_area, ResourceFactory.createTypedLiteral(item.area));
			if (item.hasPopulation())
				model.addLiteral(resLau, Voc.DBO_populationTotal, ResourceFactory.createTypedLiteral(item.population));
		}
	}

	private void addNutsCsvCollection(Model model, NutsCsvCollection nutsCsvCollection) {
		for (NutsCsvItem item : nutsCsvCollection.nuts3) {

			// Mandatory values
			if (!item.hasNutsSchema() || !item.hasName() || !item.hasNutsCode()) {
				System.err.println("Warning: Value missing " + item.toString() + " " + getClass().getSimpleName());
				continue;
			}

			Resource resLaunutsNuts = ResourceFactory
					.createResource(Voc.getNutsWithSchemeUri(item.nutsSchema, item.nutsCode));
			Resource resEurostatNuts = ResourceFactory.createResource(VocEu.getNutsCodeUri(item.nutsCode));
			Resource resEurostatNutsScheme = ResourceFactory.createResource(VocEu.getNutsSchemeUri(item.nutsSchema));

			// Add LauNuts NUTS item to Eurostat NUTS and SCHEME
			model.add(resLaunutsNuts, Voc.SKOS_hasTopConcept, resEurostatNuts);
			model.add(resLaunutsNuts, Voc.SKOS_inScheme, resEurostatNutsScheme);

			// Add NUTS label/name
			model.addLiteral(resLaunutsNuts, Voc.SKOS_prefLabel, ResourceFactory.createPlainLiteral(item.name));

			// Add NUTS code to Eurostat NUTS
			Statement nutsCodeStmt = ResourceFactory.createStatement(resEurostatNuts, Voc.SKOS_notation,
					ResourceFactory.createPlainLiteral(item.nutsCode));
			model.add(nutsCodeStmt);

			// If exists a broader NUTS, add hierarchy relations
			if (item.getLevel() > 0) {
				Resource resbroaderLaunutsNuts = ResourceFactory.createResource(Voc
						.getNutsWithSchemeUri(item.nutsSchema, item.nutsCode.substring(0, item.nutsCode.length() - 1)));
				model.add(resLaunutsNuts, Voc.SKOS_broader, resbroaderLaunutsNuts);
				model.add(resbroaderLaunutsNuts, Voc.SKOS_narrower, resLaunutsNuts);
			}
		}
	}
}