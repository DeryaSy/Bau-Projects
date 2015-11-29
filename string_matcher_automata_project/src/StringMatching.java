
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

//created by Derya Sismanyazici for CMP 3004 Formal Languages & Automata Theory Project
public class StringMatching {
	
 static boolean DEBUG = false; //while it is true it will show the index (pattern occurs with shift)
 //since example output doesn't include i hide in this way
 static String alphabet = "0123456789 abcçdefgðhýijklmnoöprqsþtuüvwxyz";
 static int[][] map;//transition map, this maps state, input -> new state
 static HashMap<Character, Integer> charmap;

 public static void main(String args[]){
	 
     Scanner input = new Scanner(System.in);
     System.out.print("Please enter the pattern: ");
     String pattern = input.nextLine();
     pattern = pattern.toLowerCase();
     System.out.print("Please enter the file name: ");
     String file = input.nextLine();
     
     map = createTransition(pattern,alphabet);
     
     System.out.println("Naive String Matcher:");
     long t1=search(file,pattern,true);
     System.out.println("Time for Naive-String-Matching:"+t1+"\n");
     
     
     System.out.println("Finite Automata Matcher:");
     long t2=search(file,pattern,false);
     System.out.println("Time for Finite-Automata-Matcher:"+t2+"\n");
     
 }
 static long search(String filename, String pattern, boolean useNaive){
     long start = System.currentTimeMillis();
     try {
         FileReader fr = new FileReader(filename);
          BufferedReader br = new BufferedReader(fr);
      String line;
      int i=1;
      while((line = br.readLine()) != null){
          line = line.toLowerCase();
          int count = 0;
          if(useNaive) {
              count=naiveMatcher(line, pattern);
          }else {
              count=finite_automata_matcher(line,map, pattern.length());
          }
          if(count > 0){
              System.out.println("Line "+i+": "+count+" occurance");
          }
          i++;
      }
     }  
 catch (FileNotFoundException e1) {
			System.out
					.println("ERROR! Please put the file in project folder and enter the file name properly. eg: text.txt");
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return System.currentTimeMillis() - start;
	}
 static int finite_automata_matcher(String T, int[][] map, int m){
     int count = 0;
     int q=0; //ln2
     for (int i=0;i<T.length();i++){ //ln3 &ln1
         int column = charmap.get(T.charAt(i));//column ID in the map
         q = map[q][column]; //ln4
         if(q == m){ //ln5
             count++;
             if(DEBUG) System.out.println("Occurs at index: "+(i-m+1)); //ln6
         }
     }
     return count;
 }
 static int[][] createTransition(String P, String Alphabet){
     //I use a hashmap remember the index of each char in the alphabet. 
     charmap=new HashMap();
     for(int i=0;i<alphabet.length();i++){
         charmap.put(alphabet.charAt(i), i);
     }
     //I create a mapping from states + chars to states. The map is implemented as an integer double array
     //rows represent states, and columns represent characters. Character indexes are stored in the variable "charmap".
     int [][] map = new int[P.length()+1][Alphabet.length()];
     int m = P.length(); //ln1
     for(int q=0;q<=m;q++){  //ln2
         for(int i=0;i<Alphabet.length();i++){ //ln3
             char a = Alphabet.charAt(i);
             int k = Math.min(m,q+1); //ln4
             //while Pk is NOT suffix of Pq.a, decrement k
             while(!(str(P,q)+a).endsWith(str(P,k)) ){ //ln5-6-7
                 k--;
             }
             map[q][i] = k; //ln8
         }
     }
     return map;
 }

    private static String str(String P, int i) {
        return P.substring(0, i);
    }

    private static int naiveMatcher(String T, String P) {
        int count = 0;
        int n = T.length(); //ln1
        int m = P.length(); //ln2
        for (int s = 0; s <= n - m; s++) { // s will move until the end of the text, ln3
            int i=0;
            for (i = 0; i < m; i++) { // during the text // i is position
                if (T.charAt(i + s) != P.charAt(i)) { //ln4
                    break;
                }
            }
            if(i==m){
                count++;
                if(DEBUG) System.out.println("Occurs at index: "+s); //ln5
            }
        }
        return count;
    }
}
