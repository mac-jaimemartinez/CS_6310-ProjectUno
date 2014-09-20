package Gallhp;

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
	private double bytesToMB(double val){
		return (double)Math.round((double) val / (1024.0*1024.0) * 100) / 100;
	}
	
	// Print the meta data about the simulation
	public String print() {
		String output = "";
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    //Run the garbage collector
	    //runtime.gc();
	    output += "##### Heap utilization statistics #####\n";
	    
		// Print used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		output += ("Used memory in bytes: " + memory + "\n");
		output += ("Used memory in megabytes: " +  bytesToMB(memory) + "\n");

		//Print free memory
		output += ("Free memory in bytes:" + runtime.freeMemory() + "\n");
		output += ("Free memory in megabytes: " + bytesToMB(runtime.freeMemory()) + "\n");

		//Print total available memory
		output += ("Total available memory in bytes:" + runtime.totalMemory() + "\n");
		output += ("Total available memory in megabytes: " + bytesToMB(runtime.totalMemory()) + "\n");
		output += ("##### Runtime statistics #####" + "\n");
	    
	    // Print execution time
	    long endTime   = System.currentTimeMillis();
	    long totalTime = endTime - startTime;
	    output += ("Total execution time: " +totalTime +" milliseconds" + "\n");
	    
	    // Print how many iterations of the Simulation were performed
	    output += ("Number of Iterations Performed: " + numIterations + "\n");
	    return output;
	}    
}

