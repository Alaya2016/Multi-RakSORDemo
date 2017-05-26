package com.na.multiraksor.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.univ.raksor.services.ranking.MultiRankingHandler;

/**
 * This class shows how to use an existing MultiRakSOR model to get reasoner ranking prediction
 * over an input ontology
 * @author Nourhene ALAYA (n.alaya@iut.univ-paris8.fr)
 * @version 1.0.0
 * Date : April 2017
 *
 */

public class MRakSORDemo {
	

	/**
	 * Runs the program, the command line looks like this:<br/>
	 * MRakSORDemo -ontopath filepath -modelfilepath filepath -evalpath filepath 
	 * </br>
	 *  
	 * @param args <br/>
	 * -ontopath: path to the ontology file (required) <br/>
	 * -modelfilepath: path to file having the serialized MultiRakSOR predictive model (required)<br/>
	 * -evalpath: path to the directory where the output file will be saved (optional). The output is an excel file recording the computed values.</br>
	 * 
	 * e.g, You can use the MultiRakSOR model generated for ISWC 2017 paper <br/>
	 * java -cp <jar_name.jar:lib/*> MRakSORDemo -ontopath ./ontologies/dpo-non-classified.owl -modelfilepath ./MultiRakSOR_Data_ISWC2017/Model/MultiRakSOR_ISWC2017.model<br/>
	 * @throws Exception 
	 */
	
	
	public static void main(String args[]) throws Exception{
		
	    System.out.println("\n## Demo of RakSOR Application Programming Interface for Reasoner Ranking over an input Ontology ## \n ");
	    
		
	    if(args.length < 4){
	  	  System.out.println("Error: Not enough arguments !");     
    	  System.out.println("Required argements: '-ontopath filepath -modelfilepath filepath'");
          System.exit(0);
	    }
        if(args.length < 2 || !args[0].equals("-ontopath")) {
            System.out.println("Error: the path to ontology file must be set.\n");         
            System.exit(0);
        }
        

        if(args.length < 4 || !args[2].equals("-modelfilepath")) {
            System.out.println("Error: the path to MultiRakSOR model file must be set.\n");         
            System.exit(0);
        }

        
        Path po = Paths.get(args[1]);
        if(!Files.isRegularFile(po) || !Files.isReadable(po)) {
            System.out.println("Error: file not found '" + args[1] + "'");
            System.exit(0);
        }
        
        Path pm = Paths.get(args[3]);
        if(!Files.isRegularFile(pm) || !Files.isReadable(pm)) {
            System.out.println("Error: file not found '" + args[3] + "'");
            System.exit(0);
        }
        
        String evalPath="./MultiRakSOR_"+po.getFileName().toString()+ "_/" ;
        if(args.length > 4 && args[4].equals("-evalpath")){
        	evalPath=args[5] ;
        }
        
       
        
        System.out.println("\n --- Start processing "+ new Date()+ "---- \n");
        System.out.println(" - Step1. Load ontology: " + po+ "\n");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(po.toUri()));
        
        System.out.println("  -Step 2. Compute and save the reasoner rankings ... \n");
		System.out.println("    - Output Dir: "+ evalPath);
        MultiRankingHandler handler = new MultiRankingHandler(ontology, pm.toString()) ;
		handler.computeAndSaveReasonerRankingsOverOntology(evalPath);
		
		System.out.println("\n --- Finish processing "+ new Date()+ "---- \n");
	}

}
