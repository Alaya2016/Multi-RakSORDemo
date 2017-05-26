package com.na.multiraksor.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.univ.raksor.services.learning.multiranking.MultiRakSORLearningManager;

/**
 * This class shows how to train a MultiRakSOR model. The 10-fold Cross validation will be used to evaluate
 * both the base learners (MLC : multi-class classification with the Binary Relevance algorithm (BR))  (MTR: multi-targed regression with multi-stack regressor (MTSR) ) 
 * @author  Nourhene ALAYA (n.alaya@iut.univ-paris8.fr)
 * @version 1.0.0
 * Date : April 2017
 *
 */
public class MRakSORTrainDemo {


	/**
	 * Runs the program, the command line looks like this:<br/>
	 * MRakSORTrainDemo -mlcfilepath filepath -mtrfilepath filepath -nbrTargets number -outputDir filepath -cv
	 * </br>
	 *  
	 * @param args <br/>
	 * -mlcfilepath: path to the multi-label classification dataset file (required).</br> This is the train file which records the ontology feature vectors toghether with the reasone relevance vectors. <br/>
	 * -mtrfilepath: path to file having the serialized MultiRakSOR predictive model (required)<br/>
	 * -nbrTargets: this option indicates the number of reasoners under examination (required).</br> It should be the same number of reasoners as in the input data. </br>
	 * -outputDir: path to the directory where the output file will be saved.</br>
	 * -cv: This option indicates that the 10-fold cross validation should be used to assess the base learners (optional). </br> A file recording the evaluation results will be generated. <
	 * 
	 * e.g, You can use the MultiRakSOR generated  model for ISWC 2017 paper <br/>
	 * java -cp <jar_name.jar:lib/*> MRakSORTrainDemo -mlcfilepath ./MultiRakSOR_Data_ISWC2017/inputData/Train/MultiRakSOR_Training_MLC_ORE2017.arff -mtrfilepath ./MultiRakSOR_Data_ISWC2017/inputData/Train/MultiRakSOR_Training_MTR_ORE2017.arff -nbrTargets 10 -outputDir ./MultiRakSOR_Data_ISWC2017/outputData/ -cv 
	 * 
	 * <br/>
	 * @throws Exception 
	 */
	
	
	public static void main(String args[]) throws Exception{
		
	    System.out.println("\n## Demo of RakSOR Application Programming Interface for Training a Predictive Model To Automatically Rank Ontology Reasoners ## \n ");
	
		
	    if(args.length < 6){
	    	  System.out.println("Error: Not enough arguments !");     
	    	  System.out.println("Required argements: '-mlcfilepath filepath -mtrfilepath filepath -nbrTargets number'");
	            System.exit(0);
	    }
	    
        if(!args[0].equals("-mlcfilepath")) {
            System.out.println("Erro: the path to MLC train data file must be set.\n");         
            System.exit(0);
        }
        

        if(!args[2].equals("-mtrfilepath")) {
            System.out.println("Erro: the path to MTR train data file file must be set.\n");         
            System.exit(0);
        }
        
        if(!args[4].equals("-nbrTargets")) {
            System.out.println("Erro: the number of reasoners must be set.\n");         
            System.exit(0);
        }
        
        
        
        Path po = Paths.get(args[1]);
        if(!Files.isRegularFile(po) || !Files.isReadable(po)) {
            System.out.println("Error: file not found '" + args[1] + "'");
            System.exit(0);
        }
        
		Path pm = Paths.get(args[3]);
		if (!Files.isRegularFile(pm) || !Files.isReadable(pm)) {
			System.out.println("Error: file not found '" + args[3] + "'");
			System.exit(0);
		}

		int nbrTargets = 10;
		try {
			nbrTargets = Integer.parseInt(args[5]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Incorrect number of reasoners '"
					+ args[5] + "'");
			System.exit(0);
		}

		String outputDir = "./MultiRakSOR_"+new Date().getTime()+"/";
		if (args.length > 6 && args[6].equals("-outputDir")) {
			outputDir = args[7];
		}
		
		boolean crossValidation=false ;
		if(args.length > 6 && args[8].equals("-cv")){
			crossValidation= true ;
		}
        
	    
        String datasetfileMLC=po.toString() ;
		String datasetfileMTR= pm.toString() ;
				
		System.out.println("\n--- Start processing "+new Date()+"-----\n");
		System.out.println("  # Working directory : "+ outputDir+"\n");
		MultiRakSORLearningManager learner =new MultiRakSORLearningManager() ;
		try {
			learner.buildSaveDefaultMultiRakSORModelCrossValidationEvaluation(datasetfileMLC, datasetfileMTR, nbrTargets, outputDir, crossValidation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\n--- Finish processing "+new Date()+" ---- ");
	}
}

