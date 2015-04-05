package pack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class JminiDES {
	
	
	public static char[] key;
	public static char[] K1;
	public static char[] plain;
	public static char[] plain1;
	public static char[] plain2;
	public static char[] crypto;
	public static char[] decrypto;
	
	public static final int P8[] = {1,2,4,3,4,3,5,6};
	
	public static final String S1[][] = {	{"101", "010", "001", "110", "011", "100", "111", "000"} ,
											{"001", "100", "110", "010", "000", "111", "101", "011"} };
	
	public static final String S2[][] = {	{"100", "000", "110", "101", "111", "001", "011", "010"} ,
											{"101", "011", "000", "111", "110", "010", "001", "100"} };
	
	
	
	
	private static void ReadFile(String name, int length){
		
        try {
            FileReader fileReader = new FileReader(name+".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
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
		String row = bits.charAt(0)+"";
		String column = bits.charAt(1)+""+bits.charAt(2)+""+bits.charAt(3);
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
	
	
	public static void GenerateKey( int runda ){
		K1 = new char[8];
		for(int i = 0 ; i<8;i++){
			K1[i] = key[(runda+i)%(key.length-1)];
		}
	}
	
	public static void GenerateDECKey( int runda ){
		K1 = new char[8];
		for(int i = 7 ; i>=0;i--){
			K1[i] = key[((runda-i)+7)%(key.length-1)];
		}		
	}
	
	public static void ReadToAnalyze(){
		 try {
	            FileReader fileReader = new FileReader("plain.txt");
	            BufferedReader bufferedReader = new BufferedReader(fileReader);
	            String line = bufferedReader.readLine();
	            
	            String wynik1Str = line.substring(0, 12);
	            String wynik2Str = line.substring(13, 25);
	            
	            plain1 = wynik1Str.toCharArray();
	            plain2 = wynik2Str.toCharArray();
	            
	            if(plain1.length!=12 ||plain2.length!=12){
		        	System.err.println("Niepoprawna dlugosc");
		        	System.exit(0);
	            }
	            
	            for(int i=0;i<plain1.length;i++){
	    			if ((plain1[i] != '0' && plain1[i] != '1')){
	    				throw new IllegalArgumentException("Niepoprawny znak ");
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
	
	public static String Crypt(int count){
		String Result = null;
		for(int round = 0 ; round<count ; round++){
			GenerateKey(round);
			String Left = new String(plain).substring(0, 6);
			String Right = new String(plain).substring(6, 12);
			char[] RightKeyXor = new char[8];
			String RightAfterPermutation = Permutation(Right);
			for(int i = 0 ;i<8;i++){
				int value =  Character.getNumericValue(RightAfterPermutation.charAt(i))^Character.getNumericValue(K1[i]);
				RightKeyXor[i] = (char)(value+48);
			}
			String Sbox1Result = SBoxReturn(1,new String(RightKeyXor).substring(0, 4));
			String Sbox2Result = SBoxReturn(2,new String(RightKeyXor).substring(4, 8));
			String SboxFullResult = Sbox1Result + Sbox2Result ;		
			char[] LeftKeyXor = new char[6];			
			for(int i = 0 ;i<6;i++){
				int value =  Character.getNumericValue(Left.charAt(i))^Character.getNumericValue(SboxFullResult.charAt(i));
				LeftKeyXor[i] = (char)(value+48);
			}		
			Result = Right+new String(LeftKeyXor);
			System.out.println(Result);
		}
		return Result;
	}
	
	
	public static void main(String[] args) throws IOException {
		switch(args[0]){
			case("-e"):
				ReadFile("key",9);
				ReadFile("plain",12);
				String Result = Crypt(8);
				try {
					FileWriter fileCryptoW = new FileWriter("crypto.txt");
					fileCryptoW.write(new String(plain));
					fileCryptoW.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			break;
			
			case("-d"):
				ReadFile("key",9);
				ReadFile("crypto",12);
				for(int round = 0 ; round<1 ; round++){
					GenerateDECKey(round);
					String Left = new String(crypto).substring(0, 6);
					String Right = new String(crypto).substring(6, 12);
		System.out.println("1. L1 "+ Left +" R1 "+ Right +" K1 "+new String(K1).length());			
					char[] RightKeyXor = new char[8];
					String RightAfterPermutation = Permutation(Right);
					
		System.out.println("2. E(R) "+RightAfterPermutation);
					
					for(int i = 0 ;i<8;i++){
						int value =  Character.getNumericValue(RightAfterPermutation.charAt(i))^Character.getNumericValue(K1[i]);
						RightKeyXor[i] = (char)(value+48);
					}
		
		System.out.println("3. ER xor K "+new String(RightKeyXor));
					
					String Sbox1Result = SBoxReturn(1,new String(RightKeyXor).substring(0, 4));
					String Sbox2Result = SBoxReturn(2,new String(RightKeyXor).substring(4, 8));
					String SboxFullResult = Sbox1Result + Sbox2Result ;
					
		System.out.println("4. S1 "+ Sbox1Result +" S2 "+Sbox2Result+" f: "+ SboxFullResult);
					
					
					char[] LeftKeyXor = new char[6];			
					for(int i = 0 ;i<6;i++){
						int value =  Character.getNumericValue(Right.charAt(i))^Character.getNumericValue(SboxFullResult.charAt(i));
						LeftKeyXor[i] = (char)(value+48);
					}	
		System.out.println("5. "+new String(LeftKeyXor));
					String Result = Right+new String(LeftKeyXor);
					crypto = Result.toCharArray();
				}
				try {
					FileWriter fileCryptoW = new FileWriter("decrypto.txt");
					fileCryptoW.write(new String(crypto));
					fileCryptoW.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
				
			break;
		case("-a"):
			ReadFile("key",9);
			ReadToAnalyze();
			FileWriter fileCryptoW = new FileWriter("analyze.txt");
			char[] test = new char[12];
			for(int i=0;i<12;i++){
				int value = Character.getNumericValue(plain1[i])^Character.getNumericValue(plain2[i]);
				test[i] = (char)(value+48);
			}
			fileCryptoW.write(new String(plain1)+" "+new String(plain2)+" "+new String(test)+"\n");
			for(int round = 1 ; round<9 ; round++){
				GenerateKey(round);
				
				String LeftA = new String(plain1).substring(0, 6);
				String RightA = new String(plain1).substring(6, 12);
				char[] RightKeyXorA = new char[8];
				String RightAfterPermutationA = Permutation(RightA);
				for(int i = 0 ;i<8;i++){
					int value =  Character.getNumericValue(RightAfterPermutationA.charAt(i))^Character.getNumericValue(K1[i]);
					RightKeyXorA[i] = (char)(value+48);
				}
				String Sbox1ResultA = SBoxReturn(1,new String(RightKeyXorA).substring(0, 4));
				String Sbox2ResultA = SBoxReturn(2,new String(RightKeyXorA).substring(4, 8));
				String SboxFullResultA = Sbox1ResultA + Sbox2ResultA ;
				char[] LeftKeyXorA = new char[6];			
				for(int i = 0 ;i<6;i++){
					int value =  Character.getNumericValue(LeftA.charAt(i))^Character.getNumericValue(SboxFullResultA.charAt(i));
					LeftKeyXorA[i] = (char)(value+48);
				}		
				String ResultA = RightA+new String(LeftKeyXorA);
				
				String LeftB = new String(plain2).substring(0, 6);
				String RightB = new String(plain2).substring(6, 12);
				char[] RightKeyXorB = new char[8];
				String RightAfterPermutationB = Permutation(RightB);
				for(int i = 0 ;i<8;i++){
					int value =  Character.getNumericValue(RightAfterPermutationB.charAt(i))^Character.getNumericValue(K1[i]);
					RightKeyXorB[i] = (char)(value+48);
				}
				String Sbox1ResultB = SBoxReturn(1,new String(RightKeyXorB).substring(0, 4));
				String Sbox2ResultB = SBoxReturn(2,new String(RightKeyXorB).substring(4, 8));
				String SboxFullResultB = Sbox1ResultB + Sbox2ResultB ;
				char[] LeftKeyXorB = new char[6];			
				for(int i = 0 ;i<6;i++){
					int value =  Character.getNumericValue(LeftB.charAt(i))^Character.getNumericValue(SboxFullResultB.charAt(i));
					LeftKeyXorB[i] = (char)(value+48);
				}		
				String ResultB = RightB+new String(LeftKeyXorB);
								
				
				for(int i=0;i<12;i++){
					int value = Character.getNumericValue(ResultA.charAt(i))^Character.getNumericValue(ResultB.charAt(i));
					test[i] = (char)(value+48);
				}
				fileCryptoW.write(new String(ResultA)+" "+new String(ResultB)+" "+new String(test)+"\n");
				plain1 = ResultA.toCharArray();
				plain2 = ResultB.toCharArray();
			}
			fileCryptoW.close();
			break;
		}
	}
}
