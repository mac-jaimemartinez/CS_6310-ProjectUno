/**
 * 
 */
package tpfahp;

/**
 * Tpfahp Application
 * <p>
 * This program simulates the diffusion of heat through a uniform two-dimensional metal plate.
 * The specification for lattice dimension, and the top/bottom/left/right edge values of the plate
 * are supplied as command line arguments.
 * The computations in this program are performed in 32 bit precision using a two-dimensional 
 * array of floats to represent the plate 
 * <p>
 * @author Katherine Peatman Addison; GTID: kpeatman3
 *
 */

public class Tpfahp {


	// Create arrays oldPlate and newPlate
	private static int d;
	private static int numIterations= 0;
	private static float[][] oldPlate;
	private static float[][] newPlate;
	private static float t, b, l, r;

	// NOTE: below is the command line syntax for running the java program
	// java Tpfahp -d # -l # -r # -t # -b #

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
	public static void initialize(float[][] oldPlate, float top, float bot, float left, float right) {
		for(int i=0; i<oldPlate.length; i++){
			for(int j=0; j<oldPlate.length; j++){
				if(i==0){
					oldPlate[i][j]=top;
				}
				else if(i==oldPlate.length-1){
					oldPlate[i][j]=bot;
				}
				else if(i>0 && i<oldPlate.length && j==0){
					oldPlate[i][j]=left;
				}
				else if (i>0 && i<oldPlate.length && (j==oldPlate.length-1)){
					oldPlate[i][j]=right;
				}
			}
		}
	}

	// Loop until exit criteria are met, updating each newPlate cell from the
	//   average temperatures of the corresponding neighbors in oldPlate
	public static void diffuse(float[][] oldPlate, float[][] newPlate) {
		// diffuse the oldPlate into the newPlate
		for (int i = 1; i <= d; i++) {
			for (int j = 1; j <= d; j++) {
				newPlate[i][j] = (oldPlate[i + 1][j] + oldPlate[i - 1][j] +
						oldPlate[i][j + 1] + oldPlate[i][j - 1]) / 4.0f;
			}
		}
	}


	// Swap the plates and continue ...
	public static void swap(float[][] oldPlate, float[][] newPlate) {
		// put the values of newPlate into oldPlate
		for(int i= 0; i < oldPlate.length; i++) {
			for(int j= 0; j < oldPlate.length; j++) {
				oldPlate[i][j]= newPlate[i][j];
			}
		}
	}

	public static boolean done() {
		return numIterations > 80;
	}


	// Print out the values of the array called for in main
	public static void print(float[][] grid) {

		for(int i= 0; i < grid.length; i++) {
			for(int j= 0; j < grid.length; j++) {
				grid[i][j] = (float)Math.round(grid[i][j] * 100) / 100;
				if(i!=0 && i!=grid.length-1 && j!=0 && j!=grid.length-1){
					System.out.print(grid[i][j]+" ");

				}

			}
			System.out.print("\n");
		}
	}


	public static void main(String[] args) {

		//Process the command line options
		Tpfahp.getInput(args);

		// Initialize plates before simulation		
		oldPlate= new float[d + 2][d + 2];
		initialize(oldPlate, t, b, l, r);
		newPlate= new float[d + 2][d + 2];
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

	}

}