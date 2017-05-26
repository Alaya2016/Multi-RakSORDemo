package com.na.multiraksor.demo;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.univ.raksor.services.evaluation.RankingEvaluation;
import com.univ.raksor.services.ranking.multilabel.MultiRakSORPredictionManager;

/**
 * 
 * This class shows how to assess the quality of the predictions (rank, relevance) produced by a MultiRakSOR model.</br>
 * The example reproduces the results reported in the ISWC 2017 paper. </br>
 *
 * @author Nourhene ALAYA (n.alaya@iut.univ-paris8.fr)
 * @version 1.0.0
 * Date : April 2017
 *
 */

public class MRakSOREvaluationDemo {
	public static String MULTIRAKSOR_EVAL_FILE="MultiRakSOR_Evaluation_ORE2017_All.xls" ;

	public static void processMultiRakSORQualityEvaluations(String inputDir, String modelfilepath, String outputDir, int nbrTargets) throws Exception{
		/**
		 * Path to test data. In each file, the features of 500 ontologies are already recorded together with
		 * the actual ranking of the reasoners (MTR dataset) and their actual relevance (MLC dataset)
		 */
		String testDatasetRank=inputDir+"Test/MultiRakSOR_Testing_MTR_ORE2017.arff" ;
		String testDatasetBip=inputDir+"Test/MultiRakSOR_Testing_MLC_ORE2017.arff" ;
		
		
		/**
		 * Path to predicted data. The computed predictions, respectively the reasoners' rank and relevance
		 * will be saved in the following files.
		 */
		String rankOutputFile=outputDir+"outputData/Predictions/Prediction_MultiRakSOR_Ranking_ORE2017.arff" ;
		String bipOutputFile=outputDir+"outputData/Predictions/Prediction_MultiRakSOR_Bipartition_ORE2017.arff" ;
		
	
		/**
		 * Path where the evaluation file will be created
		 */
		String evalfile =outputDir+"outputData/evaluations/"+ MULTIRAKSOR_EVAL_FILE ;

		
		
		System.out.println("\n Step 1. Compute the predictions ...");
		MultiRakSORPredictionManager manager= new MultiRakSORPredictionManager(modelfilepath) ;
		manager.computeMultiRakSORPredictionForDataSet(testDatasetRank,nbrTargets, rankOutputFile, bipOutputFile) ;
		
		System.out.println("\n Step 3. Evaluate the predicted ranking of reasoners ...");
        RankingEvaluation evaluator = new RankingEvaluation(testDatasetRank,evalfile, nbrTargets,  false) ;
        evaluator.processRankingEvaluationSingleAlgorithm("RakSOR-Rank", rankOutputFile);

		
        System.out.println("\n Step 4. Evaluate the predicted relevance of reasoners ...");
        evaluator = new RankingEvaluation(testDatasetBip,evalfile, nbrTargets,  false) ;
        evaluator.processBipartitionEvaluationSingleAlgorithm("RakSOR-Bip", bipOutputFile);
	}
	
	/**
	 * Runs the program, the command line looks like this:<br/>
	 * MRakSOREvaluationDemo -inputDir filepath -modelfilepath filepath -nbrTargets number -outputDir filepath
	 * </br>
	 *  
	 * @param args <br/>
	 * -inputDir: path to the directory of the test data files. (required) <br/>
	 * -modelfilepath: path to file having the serialized MultiRakSOR predictive model  (required) <br/>
	 * -nbrTargets: this option indicates the number of reasoners under examination (required).</br> It should be the same number of reasoners as in the predictive model and the test data. </br>
	 * -outputDir: path to the directory which includes the test data file and the predictive mode (required) <br/>
	 * 
	 * e.g, You can use the test data generated for ISWC 2017 paper and the predictive model. <br/> First unzip the file containing the model. </br>
	 * java -cp <jar_name.jar:lib/*> MRakSOREvaluationDemo -inputDir ./MultiRakSOR_Data_ISWC2017/inputData/ -modelfilepath ./MultiRakSOR_Data_ISWC2017/Model/MultiRakSOR_ISWC2017.model -nbrTargets 10
	 * 
	 * @throws Exception 
	 */
	
	public static void main(String args[]) throws Exception{
		
      System.out.println("\n## Demo of RakSOR Application Programming Interface for Reasoner Ranking Quality Evaluation ## \n ");
	    
        if(args.length <6 ){
        	  System.err.println("Error: Not enough arguments !");     
	    	  System.err.println("Required argements: '-inputDir filepath -modelfilepath filepath -nbrTargets number'");
	          System.exit(0);
        }
		
        if(!args[0].equals("-inputDir")) {
            System.err.println("Error: the path to the test data must be set up (-inputDir).\n");         
            System.exit(0);
        }
        
        
        if(!args[2].equals("-modelfilepath")) {
            System.err.println("Error: the path to predictive model must be set up (-modelfilepath).\n");         
            System.exit(0);
        }
        
        Path po = Paths.get(args[1]);
        if(!Files.exists(po)|| !Files.isDirectory(po)) {
            System.err.println("Error: file not found '" + args[1] + "'");
            System.exit(0);
        }
        
		Path pm = Paths.get(args[3]);
		if (!Files.isRegularFile(pm) || !Files.isReadable(pm)) {
			System.err.println("Error: file not found '" + args[3] + "'");
			System.exit(0);
		}
        
		 if(!args[4].equals("-nbrTargets")) {
	            System.err.println("Erro: the number of reasoners must be set (-nbrTargets).\n");         
	            System.exit(0);
	     }
		 
		 int nbrTargets = 10;
			try {
				nbrTargets = Integer.parseInt(args[5]);
			} catch (NumberFormatException e) {
				System.err.println("Error: Incorrect number of reasoners '"
						+ args[5] + "'");
				System.exit(0);
			}
		 
		String outputDir = "./MultiRakSOR_"+new Date().getTime()+"/";
		if (args.length > 6 && args[6].equals("-outputDir")) {
			outputDir = args[7];
		}
		
        
        processMultiRakSORQualityEvaluations(args[1], args[3], outputDir,nbrTargets) ;
        
        System.out.println("\n ---- Finish processing "+ new Date()+ "-------------");
    }
}
