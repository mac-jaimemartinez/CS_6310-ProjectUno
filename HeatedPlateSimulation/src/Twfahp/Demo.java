package Twfahp;
/**
 * Twfahp Application
 * <p>
 * This program simulates the diffusion of heat through a uniform two-dimensional metal plate.
 * The specification for lattice dimension, and the top/bottom/left/right edge values of the plate
 * are supplied as command line arguments.
 * The computations in this program are performed in Float precision using a two-dimensional
 * array of Floats to represent the plate
 * <p>
 * @author Kevin Jones; GTID: kjones314
 */


public class Demo {
	private static int d;
	private static double t, b, l, r;
	
    // NOTE: below is the command line syntax for running the java program
    // java Tpdahp.Demo -d # -l # -r # -t # -b #
    
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
	                    t = Double.parseDouble(commandOptions[++i]);
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
	                    l = Double.parseDouble(commandOptions[++i]);
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
	                    r = Double.parseDouble(commandOptions[++i]);
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
	                    b = Double.parseDouble(commandOptions[++i]);
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

    public static void main(String[] args) {
    	
		// Start the collection of meta data
    	MetaData metaData = new MetaData();
		metaData.startTiming();
		
        // Process the command line options
        Demo.getInput(args);
        
        // Run the simulation and print results
        ArrayFloatObjectPlate simulation = new ArrayFloatObjectPlate();
        simulation.runSimulation(d,(float)t,(float)b,(float)l,(float)r, metaData);
        
        // Print the meta data about the simulation
        metaData.print();
    }

}
