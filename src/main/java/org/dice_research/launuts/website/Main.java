package org.dice_research.launuts.website;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

/**
 * Generator for LauNuts URI tables.
 * 
 * @author Adrian Wilke
 */
public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Please provide a LauNuts RDF file");
		} else {
			new Main().execute(args[0]);
		}
	}

	public static final Resource RES_SCHEME = ResourceFactory.createResource("https://w3id.org/launuts/nutsScheme");

	private Model model;

	private void execute(String filepath) {
		model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, new File(filepath).toURI().toString());

		List<NutsScheme> nutsSchemes = getNutsSchemes();
		List<NutsEntity> nuts0 = getCoutries(nutsSchemes.get(0));
		List<NutsEntity> nuts1 = getNarrowerNuts(nutsSchemes.get(0), nuts0.get(0));

		System.out.println(nutsSchemes.get(0));
		System.out.println(nuts0.get(0));
		System.out.println(nuts1.get(0));
	}

	private List<NutsScheme> getNutsSchemes() {
		List<NutsScheme> nutsSchemes = new LinkedList<>();
		ResIterator resIt = model.listSubjectsWithProperty(RDF.type, RES_SCHEME);
		while (resIt.hasNext()) {
			nutsSchemes.add(new NutsScheme(resIt.next().getURI()));
		}
		return nutsSchemes;
	}

	private List<NutsEntity> getCoutries(NutsScheme nutsScheme) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + "SELECT ?nuts ?code WHERE {\n"
				+ "  ?nuts skos:inScheme <" + nutsScheme.uri + "> .\n"
				+ "  ?nuts <http://data.europa.eu/nuts/level> <https://w3id.org/launuts/level/0> .\n"
				+ "  ?nuts skos:notation ?code .\n" + "}\n" + "ORDER BY ASC(?code)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<NutsEntity> nutsEntities = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			NutsEntity nutsEntity = new NutsEntity(querySolution.get("nuts").asResource().getURI(), nutsScheme);
			nutsEntities.add(nutsEntity);
			nutsScheme.nuts0.add(nutsEntity);
		}
		queryExecution.close();
		return nutsEntities;
	}

	private List<NutsEntity> getNarrowerNuts(NutsScheme nutsScheme, NutsEntity nutsEntity) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
				+ "SELECT (?uniqueNuts as ?nuts) ?code ?label ?related WHERE {\n" + "  ?uniqueNuts skos:inScheme <"
				+ nutsScheme.uri + "> .\n" + "  ?uniqueNutsBroader skos:hasTopConcept <" + nutsEntity.uri + "> .\n"
				+ "  ?uniqueNuts skos:broader ?uniqueNutsBroader .\n"
				+ "  ?uniqueNuts skos:hasTopConcept ?nutsBroader .\n" + "  ?nutsBroader skos:notation ?code .\n"
				+ "  ?uniqueNuts skos:prefLabel ?label .\n" + "  OPTIONAL { ?uniqueNuts skos:related ?related } .\n"
				+ "}\n" + "ORDER BY ASC(?code)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<NutsEntity> nutsEntities = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			NutsEntity narrowerNutsEntity = new NutsEntity(querySolution.get("nuts").asResource().getURI(), nutsScheme);
			nutsEntity.narrower.add(narrowerNutsEntity);
			nutsEntities.add(narrowerNutsEntity);
		}
		queryExecution.close();
		return nutsEntities;
	}

}