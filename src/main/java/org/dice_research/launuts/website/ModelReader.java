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
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Generator for LauNuts URI tables.
 * 
 * @author Adrian Wilke
 */
public class ModelReader {

	public static final Resource RES_SCHEME = ResourceFactory.createResource("https://w3id.org/launuts/nutsScheme");

	private Model model;

	public List<NutsScheme> nutsSchemes;

	public List<NutsScheme> readModel(String filepath) {
		model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, new File(filepath).toURI().toString());

		// Schemes
		nutsSchemes = getNutsSchemes();
		System.out.println("Schemes " + nutsSchemes.size());

		// NUTS-0
		int counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			counter += getNuts0(nutsScheme).size();
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

		// LAU
		counter = 0;
		for (NutsScheme nutsScheme : nutsSchemes) {
			for (UniqueNutsEntity nuts0 : nutsScheme.nuts0) {
				for (UniqueNutsEntity nuts1 : nuts0.narrowerNuts) {
					for (UniqueNutsEntity nuts2 : nuts1.narrowerNuts) {
						for (UniqueNutsEntity nuts3 : nuts2.narrowerNuts) {
							counter += getLau(nuts3).size();
						}
					}
				}
			}
		}
		System.out.println("LAU " + counter);

		return nutsSchemes;
	}

	private List<NutsScheme> getNutsSchemes() {
		String queryString = "SELECT ?scheme ?eurostat ?date WHERE {\n" //
				+ "  ?scheme a <https://w3id.org/launuts/nutsScheme> .\n" //
				+ "  ?scheme <http://www.w3.org/2002/07/owl#sameAs> ?eurostat .\n" //
				+ "  ?scheme <http://purl.org/dc/terms/issued> ?date .\n" //
				+ "}\n" //
				+ "ORDER BY DESC(?date)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<NutsScheme> nutsSchemes = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			NutsScheme nutsScheme = new NutsScheme(querySolution.get("scheme").asResource().getURI());
			nutsScheme.eurostatUri = querySolution.get("eurostat").asResource().getURI();
			nutsSchemes.add(nutsScheme);
		}
		queryExecution.close();
		return nutsSchemes;
	}

	private List<UniqueNutsEntity> getNuts0(NutsScheme nutsScheme) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" //
				+ "SELECT ?uniqueNuts ?nuts ?code ?label ?related WHERE {\n" //
				+ "  ?uniqueNuts skos:inScheme <" + nutsScheme.uri + "> .\n" //
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
			RDFNode rdfNode = querySolution.get("related");
			if (rdfNode != null)
				uniqueNutsEntity.related = rdfNode.asResource().getURI();
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
			RDFNode rdfNode = querySolution.get("related");
			if (rdfNode != null)
				uniqueNutsEntity.related = rdfNode.asResource().getURI();
			nutsEntities.add(uniqueNutsEntity);

			uniqueNutzEntity.narrowerNuts.add(uniqueNutsEntity);
		}
		queryExecution.close();
		return nutsEntities;
	}

	private List<UniqueLauEntity> getLau(UniqueNutsEntity uniqueNutzEntity) {
		String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" //
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>\n" //
				+ "SELECT (?uniqueLau as ?lau) ?code ?name ?national ?area ?population WHERE {\n" //
				+ "  ?uniqueLau skos:broader <" + uniqueNutzEntity.uri + "> .\n" //
				+ "  ?uniqueLau skos:hasTopConcept ?lauBroader .\n" //
				+ "  ?lauBroader skos:notation ?code .\n" //
				+ "  OPTIONAL { ?uniqueLau skos:prefLabel ?name } .\n" //
				+ "  OPTIONAL { ?uniqueLau skos:altLabel ?national } .\n" //
				+ "  OPTIONAL { ?uniqueLau dbo:area ?area } .\n" //
				+ "  OPTIONAL { ?uniqueLau dbo:populationTotal ?population } .\n" //
				+ "}\n" //
				+ "ORDER BY ASC(?code)";
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		if (Boolean.FALSE)
			System.out.println(queryString);
		if (Boolean.FALSE)
			ResultSetFormatter.out(System.out, resultSet, query);
		List<UniqueLauEntity> lauEntities = new LinkedList<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			UniqueLauEntity uniqueLauEntity = new UniqueLauEntity(querySolution.get("lau").asResource().getURI());
			uniqueLauEntity.code = querySolution.get("code").asLiteral().getString();

			RDFNode rdfNode = querySolution.get("name");
			if (rdfNode != null)
				uniqueLauEntity.name = rdfNode.asLiteral().getString();
			rdfNode = querySolution.get("national");
			if (rdfNode != null)
				uniqueLauEntity.national = rdfNode.asLiteral().getString();
			rdfNode = querySolution.get("area");
			if (rdfNode != null)
				uniqueLauEntity.area = rdfNode.asLiteral().getInt();
			rdfNode = querySolution.get("population");
			if (rdfNode != null)
				uniqueLauEntity.population = rdfNode.asLiteral().getInt();

			lauEntities.add(uniqueLauEntity);

			uniqueNutzEntity.lau.add(uniqueLauEntity);
		}
		queryExecution.close();
		return lauEntities;
	}

}