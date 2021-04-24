package org.dice_research.launuts.rdf;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.dice_research.launuts.Configuration;

/**
 * Reads NUTS RDF.
 *
 * @author Adrian Wilke
 */
public class NutsRdfReader {

	private Model model;

	public NutsRdfReader read() {
		model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, new File(Configuration.FILE_RDF_NUTS).toURI().toString());
		return this;
	}

	/**
	 * @param scheme 2010 | 2013 | 2016
	 * @param level  0 | 1 | 2
	 */
	public SortedSet<String> getResourceUrisInSchemeAndLevel(int scheme, int level) {
		SortedSet<String> uris = new TreeSet<>();
		for (String uri : getResourceUrisInScheme(scheme)) {
			Resource res = model.getResource(uri);
			if (model.containsLiteral(res, VocEu.EU_level, level)) {
				uris.add(res.getURI());
			}
		}
		return uris;
	}

	/**
	 * @param scheme 2010 | 2013 | 2016
	 */
	public SortedSet<String> getResourceUrisInScheme(int scheme) {
		SortedSet<String> uris = new TreeSet<>();
		Resource resScheme = model.getResource(VocEu.getNutsSchemeUri(scheme));
		ResIterator resIt = model.listResourcesWithProperty(VocEu.SKOS_inScheme, resScheme);
		while (resIt.hasNext()) {
			uris.add(resIt.next().getURI());
		}
		return uris;
	}

	public SortedSet<String> getAllPredicateUris() {
		SortedSet<String> predicateUris = new TreeSet<>();
		StmtIterator stmtIt = model.listStatements();
		while (stmtIt.hasNext()) {
			predicateUris.add(stmtIt.next().getPredicate().getURI());
		}
		return predicateUris;
	}

	public SortedSet<String> getAllResourceUris() {
		SortedSet<String> resourceUris = new TreeSet<>();

		ResIterator resIt = model.listSubjects();
		while (resIt.hasNext()) {
			resourceUris.add(resIt.next().getURI());
		}

		NodeIterator nodeIt = model.listObjects();
		while (nodeIt.hasNext()) {
			RDFNode node = nodeIt.next();
			if (node.isURIResource())
				resourceUris.add(node.asResource().getURI());
		}

		return resourceUris;
	}
}