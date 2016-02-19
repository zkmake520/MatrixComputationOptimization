import java.io.*;
import java.util.*;
public class GenerateMatrix{
	static int[][]matrix = null;
	static PrintWriter writer = null;
	static int row = MatrixComputationOptimization.row;
	static int col = MatrixComputationOptimization.col;
	static Random ran = new Random();
	public static void generateMatrix(){
		//first lets generate a matrix and write to disk to each test
		if(matrix == null){
			matrix = new int[row][col];
		}

		try{
			File file = new File("Matrix");
			if(!file.exists()) {
			    file.createNewFile();
			} 
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file,false)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		//initial matrix with random number from 0-100
		for(int i = 0; i < row;i++){
			int j = 0;
			for( j = 0; j < col; j++){
				matrix[i][j] = ran.nextInt(101);
				if(j == col-1){
					writer.print(matrix[i][j]);
				}
				else{
					writer.print(matrix[i][j]+" ");
				}
			}
			// System.out.println(j);
			writer.println();
		}
		writer.close();
	}
}
