package com.na.multiraksor.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.ontologyprofiler.core.OntologyProfileHandler;

/**
 * 
 * This class shows how to compute the ontology features vector using OntoProfiler.
 * @author Nourhene ALAYA (n.alaya@iut.univ-paris8.fr
 * @version 1.0.0
 * Date : April 2017
 * @author admin
 *
 */

public class OntoProfilerDemo {
	
	/**
	 * Runs the program, the command line looks like this:<br/>
	 * OntoProfilerDemo -ontopath filepath -outputDir filepath -all
	 * </br>
	 *  
	 * @param args <br/>
	 *<strong> -ontopath </strong>: path to the ontology file (required) <br/>
	 *<strong> -outputDir </strong>: path to the directory where the output file will be saved (optional). The output is an excel file recording the computed values.</br>
	 * <strong> -all </strong>: this option indicates that all the ontology features must be computed.</br> All features include computationally expensive ones that may require a lot of time.  </br> 	 
	 * </br>
	 * 
	 * e.g, <br/>
	 * java -cp <jar_name.jar:libs/*> OntoProilerDemo -ontopath ./ontologies/dpo-non-classified.owl<br/>
	 * @throws Exception 
	 */
	
	public static void main(String args[]) throws Exception{
		
		System.out.println("\nDemo of the Application Programming Interface of RakSOR for Extracting the Ontology Features ");
	    
		
        if(args.length < 2 || !args[0].equals("-ontopath")) {
            System.out.println("Error: At least the path-to-ontology must be set.\n");         
            System.exit(0);
        }

        Path p = Paths.get(args[1]);
        if(!Files.isRegularFile(p) || !Files.isReadable(p)) {
            System.out.println("Error: file not found '" + args[1] + "'");
            System.exit(0);
        }
        
        String evalPath="./MultiRakSOR_OntoProfile/" ;
        if(args.length >= 4 && args[2].equals("-outputDir")){
        	evalPath=args[3] ;
        }
        
       boolean allfeatures= false ;
       if(args.length >= 6 && args[4].equals("-all")){
       		allfeatures=true ;
       }
        
        System.out.println("------ Load ontology: " + p+ "-------");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(p.toUri()));
        
        System.out.println("---- Compute and save Features in "+evalPath+" -------");
        OntologyProfileHandler handler = new OntologyProfileHandler(ontology,allfeatures) ;
        handler.computeAndSerializeOntologyFeatures(evalPath+p.getFileName().toString()+"_");
        
        System.out.println(" ------ Finish Processing -------");
        
        

	}

}
