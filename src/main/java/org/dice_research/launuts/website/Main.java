package org.dice_research.launuts.website;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
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

		// Schemes
		List<NutsScheme> nutsSchemes = getNutsSchemes();

		// NUTS-0
		int counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			counter += getCoutries(nutsScheme).size();
		}
		System.out.println("NUTS-0 " + counter);

		// NUTS-1
		counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			for (UniqueNutsEntity nuts0 : nutsScheme.nuts0) {
				counter += getNarrowerNuts(nutsScheme, nuts0).size();
			}
		}
		System.out.println("NUTS-1 " + counter);

		// NUTS-2
		counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			for (UniqueNutsEntity nuts0 : nutsScheme.nuts0) {
				for (UniqueNutsEntity nuts1 : nuts0.narrowerNuts) {
					counter += getNarrowerNuts(nutsScheme, nuts1).size();
				}
			}
		}
		System.out.println("NUTS-2 " + counter);

		// NUTS-3
		counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			for (UniqueNutsEntity nuts0 : nutsScheme.nuts0) {
				for (UniqueNutsEntity nuts1 : nuts0.narrowerNuts) {
					for (UniqueNutsEntity nuts2 : nuts1.narrowerNuts) {
						counter += getNarrowerNuts(nutsScheme, nuts2).size();
					}
				}
			}
		}
		System.out.println("NUTS-3 " + counter);
	}

	private List<NutsScheme> getNutsSchemes() {
		List<NutsScheme> nutsSchemes = new LinkedList<>();
		ResIterator resIt = model.listSubjectsWithProperty(RDF.type, RES_SCHEME);
		while (resIt.hasNext()) {
			nutsSchemes.add(new NutsScheme(resIt.next().getURI()));
		}
		Collections.sort(nutsSchemes, new Comparator<NutsScheme>() {

			@Override
			public int compare(NutsScheme o1, NutsScheme o2) {
				return o2.uri.compareTo(o1.uri);
			}
		});
		return nutsSchemes;
	}

	private List<UniqueNutsEntity> getCoutries(NutsScheme nutsScheme) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" //
				+ "SELECT ?uniqueNuts ?nuts ?code ?label ?related WHERE {\n" //
				+ "  ?uniqueNuts skos:inScheme <https://w3id.org/launuts/nuts/scheme/2021> .\n" //
				+ "  ?nuts <http://data.europa.eu/nuts/level> <https://w3id.org/launuts/level/0> .\n" //
				+ "  ?nuts skos:notation ?code .\n" //
				+ "  ?uniqueNuts skos:hasTopConcept ?nuts .\n" //
				+ "  ?uniqueNuts skos:prefLabel ?label .\n" //
				+ "  OPTIONAL { ?uniqueNuts skos:related ?related } .\n" //
				+ "}\n" //
				+ "ORDER BY ASC(?code)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<UniqueNutsEntity> nutsEntities = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			UniqueNutsEntity uniqueNutsEntity = new UniqueNutsEntity(
					querySolution.get("uniqueNuts").asResource().getURI());
			uniqueNutsEntity.nutsEntityUri = querySolution.get("nuts").asResource().getURI();
			uniqueNutsEntity.code = querySolution.get("code").asLiteral().getString();
			uniqueNutsEntity.label = querySolution.get("label").asLiteral().getString();
			nutsEntities.add(uniqueNutsEntity);

			nutsScheme.nuts0.add(uniqueNutsEntity);
		}
		queryExecution.close();
		return nutsEntities;
	}

	private List<UniqueNutsEntity> getNarrowerNuts(NutsScheme nutsScheme, UniqueNutsEntity uniqueNutzEntity) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" //
				+ "SELECT ?uniqueNuts ?nuts ?code ?label ?related WHERE {\n" //
				+ "  ?uniqueNuts skos:inScheme <" + nutsScheme.uri + "> .\n" //
				+ "  ?uniqueNutsBroader skos:hasTopConcept <" + uniqueNutzEntity.nutsEntityUri + "> .\n" //
				+ "  ?uniqueNuts skos:broader ?uniqueNutsBroader .\n" //
				+ "  ?uniqueNuts skos:hasTopConcept ?nuts .\n" //
				+ "  ?nuts skos:notation ?code .\n" //
				+ "  ?uniqueNuts skos:prefLabel ?label .\n" //
				+ "  OPTIONAL { ?uniqueNuts skos:related ?related } .\n" //
				+ "}\n" + "ORDER BY ASC(?code)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			System.out.println(queryString);
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<UniqueNutsEntity> nutsEntities = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			UniqueNutsEntity uniqueNutsEntity = new UniqueNutsEntity(
					querySolution.get("uniqueNuts").asResource().getURI());
			uniqueNutsEntity.nutsEntityUri = querySolution.get("nuts").asResource().getURI();
			uniqueNutsEntity.code = querySolution.get("code").asLiteral().getString();
			uniqueNutsEntity.label = querySolution.get("label").asLiteral().getString();
			nutsEntities.add(uniqueNutsEntity);

			uniqueNutzEntity.narrowerNuts.add(uniqueNutsEntity);
		}
		queryExecution.close();
		return nutsEntities;
	}

}