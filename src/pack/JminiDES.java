package pack;

public class JminiDES {
	
	 public static final int P8[] = {1,2,4,3,4,3,5,6};
	// public static final int S0[][] = {{1,0,1},{0,1,0},{};
	 public static final int S1[][] = {{ 0, 1, 2, 3},{ 2, 0, 1, 3},{ 3, 0, 1,
         2},{ 2, 1, 0, 3}};

	public static int permutacje( int x, int p[], int pmax){
      int y = 0;
      for( int i = 0; i < p.length; ++i){
         
    	 y <<= 1;
         y |= (x >> (pmax - p[i])) & 1;
     }
      return y;
    }
	
	public static void main(String[] args) {
		int x=1401;
		System.out.println(x+" "+permutacje(x,P8,9));
		
	}

}
