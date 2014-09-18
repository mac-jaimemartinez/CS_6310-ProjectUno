package Twfahp;

public class MetaData {
	// for displaying the runtime statistics in milliseconds
	long startTime;
	int numIterations;
	
	public void startTiming() {
		startTime = System.currentTimeMillis();
	}
	
	public void setNumIterations(int i) {
		numIterations = i;
	}
	
	// for displaying memory statistics in MB
	private static final long MEGABYTE = 1024L * 1024L;
	public long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	// Print the meta data about the simulation
	public void print() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		//Run the garbage collector
		runtime.gc();
		System.out.println("##### Heap utilization statistics #####");
		
		// Print used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Used memory in bytes: " + memory);
		System.out.println("Used memory in megabytes: " + bytesToMegabytes(memory));
		
		//Print free memory
		System.out.println("Free memory in bytes:" + runtime.freeMemory());
		System.out.println("Free memory in megabytes: " + bytesToMegabytes(runtime.freeMemory()));
		
		//Print total available memory
		System.out.println("Total available memory in bytes:" + runtime.totalMemory());
		System.out.println("Total available memory in megabytes: " + bytesToMegabytes(runtime.totalMemory()));
		System.out.println("##### Runtime statistics #####");
		
		// Print execution time
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total execution time: " +totalTime +" milliseconds");
		
		// Print how many iterations of the Simulation were performed
		System.out.println("Number of Iterations Performed: " + numIterations);
	}    
}
