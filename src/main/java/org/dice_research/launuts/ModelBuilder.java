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

			// Main LAU item
			Resource resItem = null;
			if (item.hasLauCodeSecond()) {
				// TODO set good uri
				// TODO use blank nodes? -> probably not, as original graph may not contain
				// required nuts uris
				resItem = ResourceFactory.createResource(NS + item.nutsCodeToString() + "/" + item.lauCodeToString()
						+ "/" + item.lauCodeSecondToString());
			} else {
				resItem = ResourceFactory.createResource(NS + item.nutsCodeToString() + "/" + item.lauCodeToString());
			}

			// Names
			// TODO code as label? -> more labels required to uniquely add types
			if (item.hasNameLatin()) {
				if (item.hasNameNational()) {
					// latin and national
					if (item.nameLatinToString().equals(item.nameNationalToString())) {
						// latin and national same
						model.addLiteral(resItem, SKOS.prefLabel,
								ResourceFactory.createPlainLiteral(item.nameLatinToString()));
					} else {
						model.addLiteral(resItem, SKOS.prefLabel,
								ResourceFactory.createPlainLiteral(item.nameLatinToString()));
						model.addLiteral(resItem, SKOS.altLabel,
								ResourceFactory.createPlainLiteral(item.nameNationalToString()));
					}
				} else {
					// only latin
					model.addLiteral(resItem, SKOS.prefLabel,
							ResourceFactory.createPlainLiteral(item.nameLatinToString()));
				}
			} else if (item.hasNameNational()) {
				// only national
				model.addLiteral(resItem, SKOS.prefLabel,
						ResourceFactory.createPlainLiteral(item.nameNationalToString()));
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
			// TODO use correct broader, but do not change source graph -> possible to use
			// both
			// TODO create uris even if not in source graph -> open up opportunity to extend
			// original graph
		}
	}
}