/**
 * Tpdahp Application
 * <p>
 * This program simulates the diffusion of heat through a uniform two-dimensional metal plate.
 * The specification for lattice dimension, and the top/bottom/left/right edge values of the plate
 * are supplied as command line arguments.
 * The computations in this program are performed in double precision using a two-dimensional 
 * array of doubles to represent the plate 
 * <p>
 * @author Jane McDowell; GTID: jmcdowell7
 *
 */

public class program3_Twfahp { 
	// for displaying memory statistics in MB
	private static final long MEGABYTE = 1024L * 1024L;
	public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }

	// Create arrays oldPlate and newPlate with linear dimension d
	//   plus two extra rows and columns to hold edge temperatures
	private static int d;// d= 0
	private static int numIterations= 0;
	private static Float[][] oldPlate;// = new double[d + 2][d + 2];
	private static Float[][] newPlate;// = new double[d + 2][d + 2];
	private static float t, b, l, r;

	// NOTE: below is the command line syntax for running the java program
	// java Tpdahp -d # -l # -r # -t # -b #

	// Process the command line arguments and error check
	public static void getInput(String[] commandOptions){
		if(commandOptions.length != 10) {
			System.out.println("You did not supply the correct number of arguments.");
			System.exit(0);
		}

		int countD=0, countT=0, countB=0, countL=0, countR=0;
		for (int i=0; i<commandOptions.length; i++) {
			// Classify the argument as an option with the dash character 
			if (commandOptions[i].startsWith("-")) {
				// Testing for the the -d argument
				if (commandOptions[i].equals("-d") && commandOptions.length > i) {
					// Make sure no duplicate command line options
					if(countD==1){
						System.out.println("Duplicate -d entries, try again");
						System.exit(0);
					}
					// assign what the user has specified to d
					// We increment i by 1 more to skip over the value
					try {
						d = Integer.parseInt(commandOptions[++i]);
						if(d <= 0) {
							System.out.println("The parameter for -d must be greater than or equal to 0.");
							System.exit(0);
						}
					} catch(NumberFormatException e) {
						System.out.println("The value supplied for -d is  "+commandOptions[i]+" and is not an integer.");
						System.exit(0);
					}
					countD++;
				}
				// Testing for the the -t argument
				else if (commandOptions[i].equals("-t") && commandOptions.length > i){
					// Make sure no duplicate command line options
					if(countT==1){
						System.out.println("Duplicate -t entries, try again");
						System.exit(0);
					}
					// assign what the user has specified to t
					// We increment i by 1 more to skip over the value
					try {
						t = Float.parseFloat(commandOptions[++i]);
						if(t < 0.0 || t > 100.0) {
							System.out.println("The value for -t must be in the range [0.0,100.0].");
							System.exit(0);
						}
					} catch(NumberFormatException e) {
						System.out.println("The value of -t is "+commandOptions[i]+" and is not a number.");
						System.exit(0);
					}
					countT++;
				}
				// Testing for the the -l argument
				else if (commandOptions[i].equals("-l") && commandOptions.length > i){
					// Make sure no duplicate command line options
					if(countL==1){
						System.out.println("Duplicate -l entries, try again");
						System.exit(0);
					}
					// assign what the user has specified to l
					// We increment i by 1 more to skip over the value
					try {
						l = Float.parseFloat(commandOptions[++i]);
						if(l < 0.0 || l > 100.0) {
							System.out.println("The value for -l must be in the range [0.0,100.0].");
							System.exit(0);
						}
					} catch(NumberFormatException e) {
						System.out.println("The value of -l is "+commandOptions[i]+" and is not a number.");
						System.exit(0);
					}
					countL++;
				}
				// Testing for the the -r argument
				else if (commandOptions[i].equals("-r") && commandOptions.length > i){
					// Make sure no duplicate command line options
					if(countR==1){
						System.out.println("Duplicate -r entries, try again");
						System.exit(0);
					}
					// assign what the user has specified to r
					// We increment i by 1 more to skip over the value
					try {
						r = Float.parseFloat(commandOptions[++i]);
						if(r < 0.0 || r > 100.0) {
							System.out.println("The value for -r must be in the range [0.0,100.0].");
							System.exit(0);
						}
					} catch(NumberFormatException e) {
						System.out.println("The value of -r is "+commandOptions[i]+" and is not a number.");
						System.exit(0);
					}
					countR++;
				}
				// Testing for the the -b argument
				else if (commandOptions[i].equals("-b") && commandOptions.length > i){
					// Make sure no duplicate command line options
					if(countB==1){
						System.out.println("Duplicate -b entries, try again");
						System.exit(0);
					}
					// assign what the user has specified to b
					// We increment i by 1 more to skip over the value
					try {
						b = Float.parseFloat(commandOptions[++i]);
						if(b < 0.0 || b > 100.0) {
							System.out.println("The value for -b must be in the range [0.0,100.0].");
							System.exit(0);
						}
					} catch(NumberFormatException e) {
						System.out.println("The value of -b is "+commandOptions[i]+" and is not a number.");
						System.exit(0);
					}
					countB++;
				}
			}
		}

		if(!(countD==1 && countT==1 && countB==1 && countL==1 && countR==1)){
			System.out.println("Invalid arguments supplied");
			System.exit(0);
		}
	}

	// Initialize the temperatures of the edge values and the plate itself
	public static void initialize(Float[][] oldPlate2, float top, float bot, float left, float right) {
		for(int i=0; i<oldPlate2.length; i++){
			for(int j=0; j<oldPlate2.length; j++){
				if(i==0){
					oldPlate2[i][j]= top;
				}
				else if(i==oldPlate2.length-1){
					oldPlate2[i][j]= bot;
				}
				else if(i>0 && i<oldPlate2.length && j==0){
					oldPlate2[i][j]= left;
				}
				else if (i>0 && i<oldPlate2.length && (j==oldPlate2.length-1)){
					oldPlate2[i][j]= right;
				}
				else
					oldPlate2[i][j]= (float) 0;
			}
		}
		/*for(int i=0; i<oldPlate.length; i++){
			for(int j=0; j<oldPlate.length; j++){
				System.out.println(oldPlate[i][j]);
			
			}
		}*/
	}

	// Loop until exit criteria are met, updating each newPlate cell from the
	//   average temperatures of the corresponding neighbors in oldPlate
	public static void diffuse(Float[][] oldPlate2, Float[][] newPlate2) {
		// diffuse the oldPlate into the newPlate
		for (int i = 1; i <= d; i++) {
			for (int j = 1; j <= d; j++) {
				newPlate2[i][j] = (float) ((oldPlate2[i + 1][j] + oldPlate2[i - 1][j] +
						oldPlate2[i][j + 1] + oldPlate2[i][j - 1]) / 4.0);
			}
		}
	}

	// Swap the plates and continue ...
	public static void swap(Float[][] oldPlate2, Float[][] newPlate2) {
		// put the values of newPlate into oldPlate
		for(int i= 0; i < oldPlate2.length; i++) {
			for(int j= 0; j < oldPlate2.length; j++) {
				oldPlate2[i][j]= newPlate2[i][j];
			}
		}
	}

	public static boolean done() {
		return numIterations > 80;
	}

	// Print out the values of the array called for in main
	public static void print(Float[][] newPlate2) {

		for(int i= 0; i < newPlate2.length; i++) {
			for(int j= 0; j < newPlate2.length; j++) {
				newPlate2[i][j] = (float)Math.round(newPlate2[i][j] * 100) / 100;
				if(i!=0 && i!=newPlate2.length-1 && j!=0 && j!=newPlate2.length-1){
					System.out.print(newPlate2[i][j]+" ");
				}
			}
			System.out.print("\n");
		}
	}

	public static void main(String[] args) {
		// for displaying the runtime statistics in milliseconds
		long startTime = System.currentTimeMillis();

		//Process the command line options
		program3_Twfahp.getInput(args);

		// Initialize plates before simulation		
		oldPlate= new Float[d + 2][d + 2];
		initialize(oldPlate, t, b, l, r);
		newPlate= new Float[d + 2][d + 2];
		initialize(newPlate, t, b, l, r);

		// Simulate Diffusion by averaging the temperatures
		while(!done()) {
			//System.out.println("\n\n");
			diffuse(oldPlate, newPlate);
			swap(oldPlate, newPlate);
			numIterations++;
		}
		// Display the results
		print(newPlate);
		
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
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total execution time: " +totalTime +" milliseconds");
	}
}