import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class BaseLine {
	
	static ArrayList<String> stopWordsList = new ArrayList<String>();
	static ArrayList<String> four_classes = new ArrayList<>(Arrays.asList("politics","religion","science","sports"));
	
	static Map<String,Integer> bag_size = new HashMap<String,Integer>();
	static ArrayList<Integer> thresold = new ArrayList<>(Arrays.asList(5,4,1,4));
	static Map<String,Integer> thresold_map = new HashMap<String,Integer>();
	static Map<String, TreeMap<String,Integer>> tokenMap =  new TreeMap<String,TreeMap<String,Integer>>();
	
	//static Map<String, Integer> each_class =new HashMap<String, Integer>();
	static int[][] count_table = new int[four_classes.size()][four_classes.size()];
	static String potential_filename = "";
	static  int index=0;
	static  int index1=0;
	public static void main(String[] args) throws  IOException {

		//Arrays.fill(four_classes,0 );	
		
		
		
		String filePath = "dataSet";
		String testpath = "testing";
		File stopwordsFile = new File("stopwords.txt");
		
		stopWordsList = extractStopWords(stopwordsFile);
		//System.out.println(stopWordsList.size());
		 for(int i=0; i<four_classes.size(); i++)
			 thresold_map.put(four_classes.get(i),thresold.get(i));
		 
		//System.out.println(thresold_map);
		scanFiles(filePath);
		//System.out.println("HardikS");
	    testDocuments(testpath);  
		//System.out.println(tokenMap);
	    /*for (Map.Entry<String, TreeMap < String, Integer >> entry : tokenMap.entrySet()) {
         	System.out.println("ejqwieioqwjeioqwjeiojqweiqwieqwijeqwijeioqwejoiqwjeioqwjeoiqwjeoiqwjeioejioqwejHere at Main::"+entry.getKey());
         	//System.out.println("Map::: "+entry. getValue());
       
         	for(Map.Entry<String, Integer> entry1 : entry.getValue().entrySet())
         		System.out.println(entry1.getKey()+"::::"+entry1.getValue());
         	}*/
	    
	    //System.out.print("\t");
	    
	    int []total_predicted = new int[four_classes.size()];
	    int []total_gold = new int[four_classes.size()];
	    double [] recall = new double[four_classes.size()];
	    double [] precision = new double[four_classes.size()];
	    
	    
	    System.out.print("   \t\t");
	    for(int i=0; i<four_classes.size(); i++)
	    System.out.print(four_classes.get(i)+" ");//+count_table[i][j]+"    ");
		System.out.print("total-predicted\n");
		
	    for(int i=0; i<four_classes.size(); i++){
	    	//total_predicted[i] = 0;
	    		System.out.print(four_classes.get(i));
	    		for(int j=0; j<four_classes.size(); j++){
	    			System.out.print("  \t"+count_table[i][j]+" ");
	    			total_predicted[i]+=count_table[i][j];
	    		}
	    		System.out.print("  \t|"+total_predicted[i]+"\n");
	    }	
	    System.out.print("---------------------------------------------\n");
	    System.out.print("totalgold");
	    for(int i=0; i<four_classes.size(); i++){
	    	total_gold[i]=0;
		    	for(int j=0; j<four_classes.size(); j++){
		    		total_gold[i]+=count_table[j][i];
		    	}
		    	System.out.print(" \t"+total_gold[i]+" ");
	    }
	    System.out.print("\n---------------------------------------------\n");
	    for(int i=0; i<four_classes.size(); i++){
	    	recall[i]= (double)count_table[i][i]/total_gold[i];
	    	precision[i]=(double)count_table[i][i]/total_predicted[i];
		    System.out.println("Class::   "+four_classes.get(i)+"\t  Recall:: "+recall[i]+"\t  Precision::"+precision[i]);
		    }
	}
	
	public static ArrayList<String> extractStopWords(File stopwordsFile) throws FileNotFoundException, IOException {

		
		String line = "";
		FileReader fileReader = new FileReader(stopwordsFile);
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			stopWordsList.add(line);
			//System.out.println(line);
		}

		return stopWordsList;
	}

	
	public static TreeMap<String, Integer> sortDecreasing(final TreeMap<String, Integer> tokens){
        Comparator<String> stringComparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
            	//System.out.println(o1+" "+o2);
            	//if(tokens.get(o1)==null) return 0; //+" "+tokens.get(o2));
                if (tokens.get(o2).compareTo(tokens.get(o1)) == 0)
                    return 1;
                else
                    return tokens.get(o2).compareTo(tokens.get(o1));
                    
            	//return tokens.get(o2) - tokens.get(o1);
            }
        };

        TreeMap<String, Integer> sortedTokens = new TreeMap<String, Integer>(stringComparator);
        sortedTokens.putAll(tokens);
        return sortedTokens;
    }
	
	private static void testDocuments(String filePath) throws IOException {
		File file1 = new File(filePath);
        File [] listOfFiles1 = file1.listFiles();
        
        
	    for(int i=0; i<listOfFiles1.length; i++){
	    	
	    	//System.out.println(listOfFiles1[i].getName().split("\\.")[0]+"  And "+four_classes.indexOf(listOfFiles1[i].getName().split("\\.")[0]));
        	if(listOfFiles1[i].isFile() && !listOfFiles1[i].getName().equals(".DS_Store")){
        		@SuppressWarnings("resource")
    			Scanner inputFile = new Scanner(listOfFiles1[i]);
        		//TreeMap<String, Integer> train_set = tokenMap.get(inputFile.toString());
        		//System.out.println("\nHardik::: "+listOfFiles1[i].getName());
        		while(inputFile.hasNextLine()){
        			
    	            String currentLine = inputFile.nextLine();
    	            if(currentLine!=null ){
    	            	TreeMap<String, Integer> count = new TreeMap<String, Integer>();
    	            	//for(String temp : four_classes)
    	            	//count.put(temp,0);
    	                currentLine = currentLine.replaceAll("\\s+", " ").trim();
    	                StringTokenizer stringTokenizer = new StringTokenizer(currentLine);
    	                
    	                //for(String file : tokenMap.keySet())
    	                //	System.out.println(file+" "+tokenMap.get(file).size());
    	                while(stringTokenizer.hasMoreTokens()){
    	                    String currentToken = stringTokenizer.nextToken().toLowerCase().trim();
    	                    if(currentToken.contains("."))continue;
    	                    String currentTokenModified = currentToken.replaceAll("[^a-zA-Z]", "");
    	                    
    	                    if(stopWordsList.contains(currentTokenModified))
    	                    	continue;
    	                    if(!currentTokenModified.equals("")&&!stopWordsList.contains(currentTokenModified))
    	            		{
    	                    	double prob = 0.0;
    	                    	potential_filename="";
    	                    	for (Map.Entry<String, TreeMap < String, Integer >> entry : tokenMap.entrySet()) {
    	                    		
    	                    		Map <String, Integer>  each_class = entry.getValue();
    	                    		Map <String, Integer> new_map = new HashMap<String,Integer>();
    	                    		
    	                    		for(Map.Entry<String, Integer> entry1 : each_class.entrySet())
    	                    			new_map.put(entry1.getKey(), entry1.getValue());
    	                    		
    	                    		if(new_map.containsKey(currentTokenModified))
    	                    		{
    	                    			
    	                    			double temp = (double)new_map.get(currentTokenModified)/bag_size.get(entry.getKey());
    	                    				if(temp>prob){
    	                    					
    	                    					prob=temp;
    	                    					potential_filename=entry.getKey();
    	                    					//System.out.print(currentTokenModified +" Prob::"+ prob +" File : "+potential_filename+"\n");
    	                    				}
    	                    		}
    	                    	}
    	                    	if(!potential_filename.equals("")){
    	                    	if(count.get(potential_filename)==null)
                      				count.put(potential_filename,1);
                      			else
                      				count.put(potential_filename,count.get(potential_filename)+1);	
    	                    	}
    	                    	//System.out.println("\n");
    	            		}
    	                }
    	               
    	                count = sortDecreasing(count);
    	                //System.out.println(count);
    	                if(count.isEmpty()){
    	                	System.out.println("Cannot Be classified::"+listOfFiles1[i].getName()+"\tDocument Content:::"+currentLine);
    	                	continue;
    	                }
    	                String  classifiedAs = count.keySet().toArray()[0].toString().split("\\.")[0];
    	                
    	                String  OriginalAs = listOfFiles1[i].getName().split("\\.")[0];
    	                index = four_classes.indexOf(OriginalAs);
    	                index1 =four_classes.indexOf(classifiedAs);
    	                //System.out.println(index +" "+index1);
    	                if(OriginalAs.equals(classifiedAs))
    	                	
    	                	count_table[index][index]+=1;//= (Integer)((Integer)count_table[index][index]); 
    	                	
    	                else
    	                	count_table[index1][index]+=1;//= (Integer)((Integer)count_table[index1+1][index+1]+1);
    	                	
    	                //System.out.print("\n"+OriginalAs+"\t\t"+classifiedAs);
    	                //System.out.print("\nOriginl class:::"+listOfFiles1[i].getName()+" \nTested as:::"+count.keySet().toArray()[0]);//count);//Collections.max(count.entrySet(), Map.Entry.comparingByValue()).getKey()+"\n");
    	                //count.clear();
    	            }
        		}
        	}
        }
	}

	private static void scanFiles(String filePath) throws IOException {
        
		File file = new File(filePath);
        File listOfFiles[] = file.listFiles();
        
       // for(int i=0; i<listOfFiles.length; i++){
         //   System.out.println(listOfFiles[i]);
           // }
        
        //System.out.println("Herererere:::"+listOfFiles.length);
        for(int i=0; i<listOfFiles.length; i++){
        	
        	if(listOfFiles[i].isFile()&&!listOfFiles[i].getName().equals(".DS_Store")){
        			fetchWords(listOfFiles[i]);
        	}
        }
    }
	
	static void fetchWords(File file) throws IOException {
        
			TreeMap<String, Integer> temp = new TreeMap<String, Integer>();
			
			@SuppressWarnings("resource")
			Scanner inputFile = new Scanner(file);
			
	        while(inputFile.hasNextLine()){
	        
	        	String currentLine = inputFile.nextLine();
	            if(currentLine!=null ){
	                currentLine = currentLine.replaceAll("\\s+", " ").trim();
	                StringTokenizer stringTokenizer = new StringTokenizer(currentLine);
	                while(stringTokenizer.hasMoreTokens()){
	                    String currentToken = stringTokenizer.nextToken().toLowerCase().trim();
	                    if(currentToken.contains("."))continue;
	                    String currentTokenModified = currentToken.replaceAll("[^a-zA-Z]", "");
	                    if(stopWordsList.contains(currentTokenModified))continue;
	                    
	                    if(!currentTokenModified.equals("")&&!stopWordsList.contains(currentTokenModified))
	            		{
	                        //add token of each document on TreeMap of tokens here
	                    	//System.out.println(currentToken+"  "+currentTokenModified); 
	                        if(temp.get(currentTokenModified) == null)
	                        	temp.put(currentTokenModified, 1);
	                        else
	                        	temp.put(currentTokenModified, temp.get(currentTokenModified) + 1);
	                    }
	                }
	            }
	        }
	        
	        	temp = sortDecreasing(temp);
	        	//System.out.print(file.getName()+" Hardik ");    
	    		temp = getKeyOfthresold(temp,thresold_map.get(file.getName().split("\\.")[0]));
	    		//temp.headMap(thresoldKey);
	    		temp = sortDecreasing(temp);
	            //System.out.println(file.getName()+" Hardik");
	    		//for(Map.Entry<String, Integer> entry1 : temp.entrySet()){
	    		//System.out.println(temp.size());
	    		//}
	    		int sum=0;
		    	for(Map.Entry<String, Integer> entry1 : temp.entrySet()){
		    		sum+= entry1.getValue();
		    	}
	        		//System.out.println(entry1.getKey()+"::::"+entry1.getValue());
		    	 bag_size.put(file.getName().split("\\.")[0] , sum);
		    	
		    	tokenMap.put(file.getName().split("\\.")[0], temp);
		    	//System.out.println(tokenMap.size());
	}
	public static TreeMap<String, Integer> getKeyOfthresold(TreeMap<String, Integer> tokens, Integer t){
		//System.out.println("\t"+t);
		TreeMap<String, Integer> returnTree = new TreeMap<String, Integer>(); 
		for (Map.Entry<String,Integer> entry : tokens.entrySet()){ 
			if(entry.getValue()<=t.intValue()){
			//	System.out.print(entry.getKey()+":::"+entry.getValue()+"    ");
				//System.out.println(returnTree.remove(entry.getKey(),entry.getValue()));//,entry.getValue());
			}else
				returnTree.put(entry.getKey(), entry.getValue());
				
		}
		//for(Map.Entry<String, Integer> entry1 : returnTree.entrySet())
			//System.out.println(entry1.getKey()+"::::"+entry1.getValue());
		
		
		return returnTree;
	}
	
	       // temp = sortDecreasing(temp);
	        //System.out.println(file.getName()+" Hardik");
//	        for(Map.Entry<String, Integer> entry1 : temp.entrySet())
  //       		System.out.println(entry1.getKey()+"::::"+entry1.getValue());
	        
	       // String thresoldKey = getKeyOfthresold(temp,thresold_map.get(file.getName().split("\\.")[0]));
            //temp.headMap(thresoldKey);
            
            
	        //for(Map.Entry<String, Integer> entry1 : temp.entrySet())
         	//	System.out.println(entry1.getKey()+"::::"+entry1.getValue());
	        	//System.out.println(file.getName()+"file name:::::"+temp+"\n");
	        //tokenMap.put(file.getName(), temp);
	        //temp.clear();
		
	
	
}
			
		    
