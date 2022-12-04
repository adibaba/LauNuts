package org.dice_research.launuts;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.SKOS;
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

	public static final String NS = "http://example.com/";

	List<LauCsvCollection> lauCsvCollections = new LinkedList<>();
	List<NutsCsvCollection> nutsCsvCollections = new LinkedList<>();

	public Model build() {
		Model model = ModelFactory.createDefaultModel();
		for (NutsCsvCollection nutsCsvCollection : nutsCsvCollections) {
			addNutsCsvCollection(model, nutsCsvCollection);
		}
		for (LauCsvCollection lauCsvCollection : lauCsvCollections) {
			addLauCsvCollection(model, lauCsvCollection);
		}
		return model;
	}

	private void addLauCsvCollection(Model model, LauCsvCollection lauCsvCollection) {
		for (LauCsvItem item : lauCsvCollection.lauCsvItems) {

			// Mandatory values
			if (!item.hasLauCode() || !item.hasNutsCode())
				continue;

			Resource resItem = null;
			if (item.hasLauCodeSecond()) {
				resItem = ResourceFactory.createResource(NS + item.nutsCodeToString() + "/" + item.lauCodeToString()
						+ "/" + item.lauCodeSecondToString());
			} else {
				resItem = ResourceFactory.createResource(NS + item.nutsCodeToString() + "/" + item.lauCodeToString());
			}

			if (item.hasNameLatin()) {
				model.addLiteral(resItem, SKOS.altLabel, ResourceFactory.createPlainLiteral(item.nameLatinToString()));
			}

			Resource resNuts = ResourceFactory.createResource(NS + item.nutsCodeToString());
			if (model.containsResource(resNuts)) {
				model.add(resItem, SKOS.related, resNuts);
			}
		}
	}

	private void addNutsCsvCollection(Model model, NutsCsvCollection nutsCsvCollection) {
		for (NutsCsvItem item : nutsCsvCollection.nuts3) {
			Resource resItem = ResourceFactory.createResource(NS + item.nutsCode);
			model.addLiteral(resItem, SKOS.altLabel, ResourceFactory.createPlainLiteral(item.name));
		}
	}
}