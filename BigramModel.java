package il.ac.tau.cs.sw1.ex5;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class BigramModel {
	public static final int MAX_VOCABULARY_SIZE = 14000;
	public static final String VOC_FILE_SUFFIX = ".voc";
	public static final String COUNTS_FILE_SUFFIX = ".counts";
	public static final String SOME_NUM = "some_num";
	public static final int ELEMENT_NOT_FOUND = -1;
	
	String[] mVocabulary;
	int[][] mBigramCounts;
	
	// DO NOT CHANGE THIS !!! 
	public void initModel(String fileName) throws IOException{
		mVocabulary = buildVocabularyIndex(fileName);
		mBigramCounts = buildCountsArray(fileName, mVocabulary);
		
		
	}
	
	
	
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public  String[] buildVocabularyIndex(String fileName) throws IOException{ // Q 1
		// replace with your code
		File fromFile = new File(fileName);
		BufferedReader bufferedReader= new BufferedReader(new FileReader (fromFile));
		String line;
		int cnt=0;
		String [] vocabulary = new String[MAX_VOCABULARY_SIZE];
		//String [] tempArray;
		while ((line=bufferedReader.readLine()) !=null && cnt < MAX_VOCABULARY_SIZE) {
			String[] temp_arr=line.split(" ");
			for (int i=0; i< temp_arr.length; i++) {
				if (IslegalWord(temp_arr[i])) {
				String str=getLegalWord(temp_arr[i]);
					if (!contains (vocabulary, str)) {
						vocabulary[cnt]=str;
						cnt++;
					}
				}
			}
		}
		bufferedReader.close();
		String [] new_voc= new String[cnt];
		if (cnt< MAX_VOCABULARY_SIZE) {
			for (int i=0; i<cnt; i++) {
				new_voc[i]=vocabulary[i];
				}
			vocabulary=new_voc;
		}	
		
		return vocabulary;
	}
	
	
	
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public int[][] buildCountsArray(String fileName, String[] vocabulary) throws IOException{ // Q - 2
		// replace with your code
		int l= vocabulary.length;
		int[][] bigramcounts= new int [l][l];
		File fromFile = new File(fileName);
		
		
		String line;
		BufferedReader bufferedReader= new BufferedReader(new FileReader (fromFile));
		while((line=bufferedReader.readLine()) !=null ) {
			String [] line_array= line.split(" ");
			for (int n=0; n<line_array.length-1; n++) {
					String word1=getLegalWord(line_array[n]);
					String word2=getLegalWord(line_array[n+1]);
					if(contains(vocabulary, word1) &&contains (vocabulary, word2)) {
						int i= getWordIndex(vocabulary,word1);
						int j =getWordIndex(vocabulary, word2);
						bigramcounts[i][j]++;
							
						}
					}
				}
			bufferedReader.close();
		
		
		return bigramcounts;

	}
	
	
	/*
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: fileName is a legal file path
	 */
	public void saveModel(String fileName) throws IOException{ // Q-3
		// add your code here
		int l=mVocabulary.length;
		File tofile1= new File(fileName+VOC_FILE_SUFFIX);
		File tofile2=  new File (fileName+COUNTS_FILE_SUFFIX);
		BufferedWriter bufferedWriter1= new BufferedWriter(new FileWriter(tofile1));
		bufferedWriter1.write(Integer.toString(l)+" words");
		for (int i=0; i<l; i++) {
			bufferedWriter1.write(Integer.toString(i)+","+mVocabulary[i]);
			
		}
		bufferedWriter1.close();
		BufferedWriter bufferedWriter2= new BufferedWriter(new FileWriter(tofile2));
			for (int i=0; i<l; i++) {
				for (int j =0; j< l; j++) {
					if(mBigramCounts[i][j]>0) {
						bufferedWriter2.write(Integer.toString(i)+","+Integer.toString(j)+":"+mBigramCounts[i][j]);
					}
				}
			}
		bufferedWriter2.close();
	}
	
	 
	
	/*
	 * @pre: fileName is a legal file path
	 */
	public void loadModel(String fileName) throws IOException{ // Q - 4
		// add your code here
	}

	
	
	/*
	 * @pre: word is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: word is in lowercase
	 * @post: $ret = -1 if word is not in vocabulary, otherwise $ret = the index of word in vocabulary
	 */
	public int getWordIndex(String word){  // Q - 5
		int len= mVocabulary.length;
		for (int i=0; i<len; i++) {
			if (mVocabulary[i].equals(word)) {
				return i;
			}
				
			}return ELEMENT_NOT_FOUND;
		}
		
	
	
	
	
	/*
	 * @pre: word1, word2 are in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = the count for the bigram <word1, word2>. if one of the words does not
	 * exist in the vocabulary, $ret = 0
	 */
	public int getBigramCount(String word1, String word2){ //  Q - 6
		int i= getWordIndex(word1);
		int j =getWordIndex(word2);
		if (i>-1 && j>-1) {
			return mBigramCounts[i][j];
		}
		
		return 0;
	}
	
	
	/*
	 * @pre word in lowercase, and is in mVocabulary
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post $ret = the word with the lowest vocabulary index that appears most fequently after word (if a bigram starting with
	 * word was never seen, $ret will be null
	 */
	public String getMostFrequentProceeding(String word){ //  Q - 7
		int i = getWordIndex(word);
		int len =mVocabulary.length;
		int max = 0;
		int n = 0;
		for (int j = len-1; j >= 0; j--) {
			if (mBigramCounts[i][j]>= max) {
				max= mBigramCounts[i][j];
				n=j;
			}
		}
		if (max >0) {
			return mVocabulary[n];
			}else {
		return null;
	}
	}		
	
	
	/* @pre: sentence is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: each two words in the sentence are are separated with a single space
	 * @post: if sentence is is probable, according to the model, $ret = true, else, $ret = false
	 */
	public boolean isLegalSentence(String sentence){  //  Q - 8
		String [] sent =sentence.split(" ");
		for (int i=0; i< sent.length; i++) {
			if(!contains(mVocabulary, sent[i])) {
				return false;
			}

		}
		for (int i=0; i< sent.length-1; i++) {
			int x= getWordIndex(sent[i]);
			int y=getWordIndex(sent[i+1]);
			if(mBigramCounts[x][y]==0) {
				return false;			}
		}
		return true;
	}
	
	
	
	/*
	 * @pre: arr1.length = arr2.legnth
	 * post if arr1 or arr2 are only filled with zeros, $ret = 0, otherwise
	 */
	public static double calcCosineSim(int[] arr1, int[] arr2){ //  Q - 9
		if (!is_0_Vector(arr1) && !is_0_Vector(arr2)) {
			double res= Sigma(arr1,arr2)/(Math.pow(Sigma(arr1,arr1),0.5)*Math.pow(Sigma(arr2, arr2),0.5));
			return res;
		}
			return 0.;
	}

	
	/*
	 * @pre: word is in vocabulary
	 * @pre: the method initModel was called (the language model is initialized), 
	 * @post: $ret = w implies that w is the word with the largest cosineSimilarity(vector for word, vector for w) among all the
	 * other words in vocabulary
	 */
	public String getClosestWord(String word){ //  Q - 10
		int i=IndexofClosest(word);
		return mVocabulary[i];
	}
	
	//*for Q -1 return if a string is contain a letter or represent an integer
	
	public static boolean IslegalWord(String word) {
		
		for (int i=0; i< word.length(); i++) {
			if ((int)word.charAt(i)>=97 && (int)word.charAt(i)<=122) {
				return true;
			}
			
		}
		try {
			Integer.parseInt(word);
			return true;
		}catch (NumberFormatException e) {
			return false;
			
		}
	}
	// for Q-1 return appropriate word for vocabulary
	public static String getLegalWord (String word) {
		String str="some_num";
		try {
			Integer.parseInt(word);
			return str;
		}catch(NumberFormatException e){
			return word.toLowerCase();
			
		}
	}
	
	// for Q-1 return if a given string is existing in array
	public static boolean contains (String [] array, String str) {
		for (int i=0; i<array.length; i++ ) {
			if(str.equals(array[i])) {
				return true;
			}
		}return false;
	}
	
	// for Q-9 calculate duplication of  2 vectors
	
	public static int Sigma (int [] a, int [] b) {
		int l =a.length;
		int sigma =0;
		for (int i=0; i< l ; i++) {
			sigma= sigma + (a[i]*b[i]);
			
		}return sigma;
		
	}
	
	// for Q-9 return if vector == 0 vector 
	public static boolean is_0_Vector (int [] a) {
		int i=0;
		while  (i<a.length ) {
			if(a[i]!=0) {
				return false;
				}else {
					i++;
				}
		}return true;
	}
	// for Q-10;
	public  int IndexofClosest(String word) {
		double distance=0;
		int n=0;
		int i = getWordIndex(word);
		int [] vector1=mBigramCounts[i];
		for (int j=0; j<mBigramCounts.length; j++) {
			if (j==i) {
				continue;
			}else {
				double res=calcCosineSim(vector1, mBigramCounts[j]);
				if (res>=distance) {
					distance=res;
					n=j;
				}
			}
		}
	return n;	
	}
	public static int getWordIndex(String [] array, String word){  // Q - 5
		int len= array.length;
		for (int i=0; i<len; i++) {
			if (array[i].equals(word)) {
				return i;
			}
				
			}return ELEMENT_NOT_FOUND;
		}
}
