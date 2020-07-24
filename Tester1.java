package il.ac.tau.cs.sw1.ex5;

import java.io.IOException;
import java.util.Arrays;

public class Tester1 {

	

	
		public static final String ALL_YOU_NEED_FILENAME = "resources/hw5/all_you_need.txt";
		public static final String EMMA_FILENAME = "resources/hw5/emma.txt";
		public static final String ALL_YOU_NEED_MODEL_DIR = "resources/hw5/all_you_need_model";

	public static void main(String[] args) throws IOException{
			BigramModel sG = new BigramModel();
			String[] voc = sG.buildVocabularyIndex(ALL_YOU_NEED_FILENAME);
			int[][] counts = sG.buildCountsArray(ALL_YOU_NEED_FILENAME, voc);
			System.out.println(Arrays.deepToString(counts));
			System.out.println(Arrays.toString(voc));
			
	}

}
