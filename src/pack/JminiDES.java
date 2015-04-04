package pack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class JminiDES {
	
	
	public static char[] key;
	public static char[] K1;
	public static char[] plain;
	public static char[] crypto;
	public static char[] dectypt;
	
	public static final int P8[] = {1,2,4,3,4,3,5,6};
		// public static final int S0[][] = {{1,0,1},{0,1,0},{};
	public static final String S1[][] = {	{"101", "010", "001", "110"} ,
											{"011", "100", "111", "000"} ,
											{"001", "100", "110", "010"} ,
											{"000", "111", "101", "011"} };
	
	public static final String S2[][] = {	{"100", "000", "110", "101"} ,
										{"111", "001", "011", "010"} ,
										{"101", "011", "000", "111"} ,
										{"110", "010", "001", "100"} };
	
	
	
	
	private static void ReadFile(String name, int length){
		
        try {
            FileReader fileReader = new FileReader(name+".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            System.out.println(line);
            char[] wynik = null;
            switch(name){
            	case("key"):
            		key = line.toCharArray();
            		wynik = line.toCharArray();
            		break;
            	case("plain"):
            		plain = line.toCharArray();
            		wynik = line.toCharArray();
            		break;
            	case("crypto"):
            		crypto = line.toCharArray();
            		wynik = line.toCharArray();
            		break;
            	default:
            		System.err.println("Niepoprawny plik");
            		System.exit(0);
            }
             
            
            if(wynik.length!=length){
	        	System.err.println("Niepoprawna dlugosc w "+wynik.toString());
	        	System.exit(0);
            }
            
            for(int i=0;i<wynik.length;i++){
    			if ((wynik[i] != '0' && wynik[i] != '1')){
    				throw new IllegalArgumentException("Niepoprawny znak "+ wynik[i] +" w "+wynik.toString());
    			}
            }
            
            bufferedReader.close();
            
    	}
	    catch(FileNotFoundException ex) {
	        System.err.println("Unable to open file 'key.txt'");   
	        ex.printStackTrace();
	        System.exit(0);
	    }
	    catch(IOException ex) {
	        System.err.println("Error reading file 'key.txt'");  
	        ex.printStackTrace();
	        System.exit(0);
	    }
	    catch(NullPointerException ex){
	    	ex.printStackTrace();
	    	System.exit(0);
	    }
	}
	
	public static char[] RotateLeft(char[] tab){
		char pom = tab[0];
		for(int i=0;i<tab.length-1;i++){
			tab[i]=tab[i+1];
		}
		tab[tab.length-1] = pom;
		return tab;
	}
	
	public static char[] RotateRight(char[] tab){
		char pom = tab[tab.length-1];
		for(int i=tab.length-2;i>=0;i--){
			tab[i+1]=tab[i];
		}
		tab[0] = pom;
		return tab;
	}
	public static String SBoxReturn(int boxNo, String bits){
		String row = bits.charAt(0)+""+bits.charAt(3);
		String column = bits.charAt(1)+""+bits.charAt(2);
		int rowNumberInt = Integer.parseInt(row, 2);
		int rowColumnInt = Integer.parseInt(column, 2);
		if(boxNo == 1){
			return S1[rowNumberInt][rowColumnInt];
		}else if(boxNo == 2){
			return S2[rowNumberInt][rowColumnInt];
		}
		return " ";
	}
	
	
	public static String Permutation(String input){
		char[] PermutationOutput = new char[P8.length];
		
		for(int i=0;i<P8.length;i++){
			PermutationOutput[i] = input.charAt(P8[i]-1);
		}
		
		return String.valueOf(PermutationOutput);
	}
	
	
	public static String GenerateKey( int runda ){
		K1 = new char[8];
		for(int i = 0 ; i<8;i++){
			K1[i] = key[(runda+i)%(key.length-1)];
		}
		
		return " ";
	}
	
	
	public static void main(String[] args) {
		switch(args[0]){
			case("-e"):
				ReadFile("key",9);
				ReadFile("plain",12);
				for(int round = 1 ; round<9 ; round++){
					GenerateKey(round);
					
					String part1 = new String(plain).substring(0, 6);
					String part2 = new String(plain).substring(6, 12);
					char[] part2KeyXor = new char[8];
					String part2AfterPermutation = Permutation(part2);
					for(int i = 0 ;i<8;i++){
						int value =  Character.getNumericValue(part2AfterPermutation.charAt(i))^Character.getNumericValue(K1[i]);
						part2KeyXor[i] = (char)(value+48);
					}
					String Sbox1Result = SBoxReturn(1,new String(part2KeyXor).substring(0, 4));
					String Sbox2Result = SBoxReturn(2,new String(part2KeyXor).substring(4, 8));
					String SboxFullResult = Sbox1Result + Sbox2Result ;
					char[] part1KeyXor = new char[6];			
					for(int i = 0 ;i<6;i++){
						int value =  Character.getNumericValue(part1.charAt(i))^Character.getNumericValue(SboxFullResult.charAt(i));
						part1KeyXor[i] = (char)(value+48);
					}		
					String Result = part2+new String(part1KeyXor);
					System.out.println(Result);
				}
			break;
		}

		
	}

}
