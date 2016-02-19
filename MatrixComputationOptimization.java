import java.util.*;
import java.io.*;
public class MatrixComputationOptimization{
	public static int row = 10000;
	public static int col = 10000;
	int[][] matrix = null;
	PrintWriter	writer = null;
	PrintWriter resultWriter = null;
	int[] direction = new int[]{-1,0,1};
	long startTime;
	long endTime;
	//Problem description: given a large matrix, update the each cell by computing the average of itself and its neighboors
	//Goal: try to optimize the computing speed by integrating parallel computing techinques and improving cache hit rates


	/**
	 * Load matrix into memory
	 */
	public void loadMatrix(){
		try{
			String line = null;
			BufferedReader bufferReader = new BufferedReader(new FileReader("Matrix"));
			int r = 0;
			matrix = new int[row][col];
			while((line = bufferReader.readLine())!=null){
				String[] nums = line.split(" ");
				// if(nums.length < 100){
					// System.out.println("line"+r+" "+nums.length);
					// System.out.println(line);
				// }
				for(int i = 0; i < col; i++){
					matrix[r][i] = Integer.parseInt(nums[i]);
				}
				r++;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public void basicCompuateAverage(int times){
		try{
			resultWriter = new PrintWriter(new BufferedWriter(new FileWriter("Result",true)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		startTime = System.currentTimeMillis();
		//to save the heap memory we won't create another matrix to save the result
		compute(0,row);
		endTime = System.currentTimeMillis();
		resultWriter.println("Basic Matrix Computation "+times+" row:"+row+" column:"+col);
		resultWriter.println("	time:"+(endTime-startTime));
		resultWriter.println();
		resultWriter.close();
	}
	public void computeOneBlock(int startRow,int endRow,int startCol, int endCol){
		for(int i = startRow; i < endRow; i++){
			for(int j = startCol; j < endCol; j++){
				int sum = 0;
				for(int r = Math.max(0,i-1); r <= Math.min(row-1,i+1); r++){
					for(int c = Math.max(0,j-1); c <= Math.min(col-1,j+1); c++){
						sum += matrix[r][c];
					}
				}	
			}
		}
	}
	public void improvedCompute(int start,int end,int block){
		int r = start;
		int rowBlockCount = (end-start)/block;
		int colBlockCount = col/block;
		int i = 0;
		while(i++<rowBlockCount){
			int j = 0;
			int c = 0;
			while(j++ < colBlockCount){
				computeOneBlock(r,r+block,c,c+block);
				c += block;
			}
			r+=block;

		}
	}
	public void compute(int start,int end){
		for(int i = start;i < end; i++){
			for(int j = 0; j < col;j++){
				int sum = 0;
				for(int r = Math.max(0,i-1); r <= Math.min(row-1,i+1); r++){
					for(int c = Math.max(0,j-1); c <= Math.min(col-1,j+1); c++){
						sum += matrix[r][c];
					}
				}	
			}
		}
	}
	class Job extends Thread{
		int start;
		int end;
		int chose;
		int block;
		Job(int start,int end,int chose,int block){
			this.start = start;
			this.end = end;
			this.chose = chose;
			this.block = block;
		}
		@Override 
		public void run(){
			if(chose == 1){
				compute(start,end);	
			}
			else{
				improvedCompute(start,end,10);
			}
		}
	}
	public void compuateAverageWithDifferentThread(int times,int threads){
		try{
			resultWriter = new PrintWriter(new BufferedWriter(new FileWriter("Result",true)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		startTime = System.currentTimeMillis();
		//to save the heap memory we won't create another matrix to save the result
		Job[] jobs = new Job[threads];
		int range = row/threads;
		for(int i = 0; i< threads; i++){
			jobs[i] = new Job(i*range,(i+1)*range,1,0);
			jobs[i].start();
		}
		try{
			for(int i =0; i< threads;i++){
				jobs[i].join();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();	
		resultWriter.println("Basic Matrix Computation "+times+" with "+threads+" threads row:"+row+" column:"+col);
		resultWriter.println("	time:"+(endTime-startTime));
		resultWriter.println();
		resultWriter.close();
	}

	// to improve the cache hit ratio, we will compute the matrix block by block rather than line by line
	public void compuateByImprovingCacheHit(int times, int threads,int block){
		try{
			resultWriter = new PrintWriter(new BufferedWriter(new FileWriter("Result",true)));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		startTime = System.currentTimeMillis();
		Job[] jobs = new Job[threads];
		int range = row/threads;
		for(int i = 0; i< threads; i++){
			jobs[i] = new Job(i*range,(i+1)*range,0,block);
			jobs[i].start();
		}
		try{
			for(int i =0; i< threads;i++){
				jobs[i].join();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();	
		resultWriter.println("Matrix Computation By improving Cache hit "+times+" with "+threads+" threads block size:"+block+" row:"+row+" column:"+col);
		resultWriter.println("	time:"+(endTime-startTime));
		resultWriter.println();
		resultWriter.close();

	}
	public static void main(String[] args){
		MatrixComputationOptimization matrixCO = new MatrixComputationOptimization();
		// GenerateMatrix.generateMatrix();
		matrixCO.loadMatrix();
		for(int i =0; i < 10; i++){
			matrixCO.basicCompuateAverage(i);
		}

		for(int i = 0;i< 10; i++){
			matrixCO.compuateAverageWithDifferentThread(i,4);
		}
		for(int i = 0;i< 10; i++){
			matrixCO.compuateAverageWithDifferentThread(i,3);
		}
		for(int i = 0;i< 10; i++){
			matrixCO.compuateAverageWithDifferentThread(i,2);
		}
		for(int i = 0;i< 10; i++){
			matrixCO.compuateAverageWithDifferentThread(i,1);
		}
		for(int i = 0;i< 10; i++){
			matrixCO.compuateByImprovingCacheHit(i,2,10);
		}
	}
}