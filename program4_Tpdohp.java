/**
 * Tpdohp Application
 * <p>
 * This program simulates the diffusion of heat through a uniform two-dimensional metal plate.
 * The specification for lattice dimension, and the top/bottom/left/right edge values of the plate
 * are supplied as command line arguments.
 * The computations in this program are performed in double precision using a two-dimensional
 * "lattice" of objects which only know of their neighbors.
 * <p>
 * @author Jaime Martinez; GTID: jmartinez66
 *
 */

public class program4_Tpdohp {
	// for displaying memory statistics in MB
	private static final long MEGABYTE = 1024L * 1024L;
	public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }
    
    //Values for input parameters.
    public static int d;
    public static double t, b, l, r;
    
    //Value for termination safeguard.
    private static int numIterations= 0;
    
    //Object to represent our lattice.
    private static class lattice{
        
        //Object to represent a node in our lattice.
        private class latticeNode{
            //neigboring nodes
            private latticeNode leftNode;
            private latticeNode rightNode;
            private latticeNode topNode;
            private latticeNode bottomNode;
            
            //Nodes temperature.
            private double temperature;
        }
        
        //Node pointers for holding root (top left), and for facilitating traversal of lattices.
        private final latticeNode topLeft;
        private latticeNode aboveCurrentNode;
        private latticeNode currentLeftEdge;
        private latticeNode currentNode;
        private latticeNode helperLeftEdge;
        private latticeNode helperCurrentNode;
        
        //initializer
        public lattice() {
            //intialize variables for navigating lattice
            topLeft = new latticeNode();
            currentNode = topLeft;
            currentLeftEdge = topLeft;
            aboveCurrentNode = null;
            
            for(int i=0; i<d; i++){          //i denotes row we are in
                for(int j=0; j<d; j++){      //j denotes column we are in
                    
                    //set top (left should already be set)
                    currentNode.topNode = aboveCurrentNode;
                    
                    //If above not null, set aboveNodes bottom to current to interlock.
                    if( aboveCurrentNode != null ) {
                        aboveCurrentNode.bottomNode = currentNode;
                        aboveCurrentNode = aboveCurrentNode.rightNode;
                    }
                    
                    //determine if we will have a new node to go to or not
                    if( j < d - 1) {
                        //create next node, and interlock current with next node, then move on (left & right set)
                        latticeNode nextNode = new latticeNode();
                        currentNode.rightNode = nextNode;
                        nextNode.leftNode = currentNode;
                        currentNode = nextNode;
                    }
                }
                //prepare to move on to new row
                aboveCurrentNode = currentLeftEdge;
                currentLeftEdge = new latticeNode();
                currentNode = currentLeftEdge;
            }
        }
        
        private void copyLattice(lattice latticeToCopy) {
            currentLeftEdge = topLeft;
            currentNode = currentLeftEdge;
            
            helperLeftEdge = latticeToCopy.topLeft;
            helperCurrentNode = helperLeftEdge;
            
            // Loop over the lattice.
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    //Copy and adjust position.
                    currentNode.temperature = helperCurrentNode.temperature;
                    
                    currentNode = currentNode.rightNode;
                    helperCurrentNode = helperCurrentNode.rightNode;
                }
                //Update our current pointers for traversing the lattices.
                currentNode = currentLeftEdge.bottomNode;
                currentLeftEdge = currentNode;
                helperCurrentNode = helperLeftEdge.bottomNode;
                helperLeftEdge = helperCurrentNode;
            }
        }
        
        // Loop until exit criteria are met, updating each newPlate cell from the
        //   average temperatures of the corresponding neighbors in oldPlate
        public void diffuse(lattice helper) {
            
            currentLeftEdge = topLeft;
            currentNode = currentLeftEdge;
            
            helperLeftEdge = helper.topLeft;
            helperCurrentNode = helperLeftEdge;
            
            //Holders for neighboring temperatures.
            double tempLeft;
            double tempRight;
            double tempTop;
            double tempBottom;
            
            // diffuse the oldPlate into the newPlate
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    
                    //get appropriate temperatures whether we are on edges or not.
                    tempLeft = helperCurrentNode.leftNode != null ? helperCurrentNode.leftNode.temperature : l;
                    tempRight = (helperCurrentNode.rightNode != null) ? helperCurrentNode.rightNode.temperature : r;
                    tempTop = (helperCurrentNode.topNode != null) ? helperCurrentNode.topNode.temperature : t;
                    tempBottom = (helperCurrentNode.bottomNode != null) ? helperCurrentNode.bottomNode.temperature : b;
                    
                    currentNode.temperature = (tempLeft + tempRight + tempTop + tempBottom) / 4;
                    currentNode = currentNode.rightNode;
                    helperCurrentNode = helperCurrentNode.rightNode;
                }
                //Update our current pointers for traversing the lattices.
                currentNode = currentLeftEdge.bottomNode;
                currentLeftEdge = currentNode;
                helperCurrentNode = helperLeftEdge.bottomNode;
                helperLeftEdge = helperCurrentNode;
            }
        }
        
        // Print out the values of the lattice.
        public  void print() {
            
            currentLeftEdge = topLeft;
            currentNode = currentLeftEdge;
            
            for(int i= 0; i < d; i++) {
                for(int j= 0; j < d; j++) {
                    // currentNode.temperature = (double)Math.round(currentNode.temperature * 100) / 100;
                    System.out.print( String.format("%.2f", currentNode.temperature)+" ");
                    currentNode = currentNode.rightNode;
                }
                //Print new line and update our current pointers for traversing lattice.
                System.out.print("\n");
                currentNode = currentLeftEdge.bottomNode;
                currentLeftEdge = currentNode;
            }
        }
    }
    
    // NOTE: below is the command line syntax for building and running the java program.
    // java.c Tpdohp.java
    // java Tpdohp -d # -l # -r # -t # -b #
    
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
    
    public static boolean done() {
        return numIterations > 80;
    }
    
    public static void main(String[] args) throws CloneNotSupportedException {
		// for displaying the runtime statistics in milliseconds
		long startTime = System.currentTimeMillis();
        
        //Process the command line options
        program4_Tpdohp.getInput(args);
        
        // Initialize lattices before simulation
        lattice helperLattice =  new lattice();
        lattice lattice = new lattice();
        
        // Simulate Diffusion by averaging the temperatures
        while(!done()) {
            lattice.diffuse( helperLattice);
            helperLattice.copyLattice( lattice );
            numIterations++;
        }
        
        // Display the results
        lattice.print();
        
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
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