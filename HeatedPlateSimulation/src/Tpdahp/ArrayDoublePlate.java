package Tpdahp;

public class ArrayDoublePlate {
	// Create arrays oldPlate and newPlate with linear dimension d
    //   plus two extra rows and columns to hold edge temperatures
    private static int dimension;
    private static int maxIterations= 100;
    private static double relChgStopCriteria = 1.00;
    private static double[][] oldPlate;
    private static double[][] newPlate;
    private static double topEdgeTemp, bottomEdgeTemp, leftEdgeTemp, rightEdgeTemp;
    
    // Run the simulation
    public void runSimulation(int dim, double top, double bottom, double left,
    		double right, MetaData metaData) {
   
        dimension = dim;
        topEdgeTemp = top;
        bottomEdgeTemp = bottom;
        leftEdgeTemp = left;
        rightEdgeTemp = right;
        
        // Initialize plates before simulation		
        oldPlate= new double[dimension + 2][dimension + 2];
        initialize(oldPlate, topEdgeTemp, bottomEdgeTemp, leftEdgeTemp, rightEdgeTemp);
        newPlate= new double[dimension + 2][dimension + 2];
        initialize(newPlate, topEdgeTemp, bottomEdgeTemp, leftEdgeTemp, rightEdgeTemp);
        
        // Simulate Diffusion by averaging the temperatures
        double maxChange = 100;
        int i = 0;
        for ( ; i < maxIterations && maxChange > relChgStopCriteria; i++) {
            //System.out.println("\n\n");
            maxChange = diffuse(oldPlate, newPlate);
            swap(oldPlate, newPlate);
        }
        metaData.setNumIterations(i);
        // Display the results
        print(newPlate);
    }

    // Initialize the temperatures of the edge values and the plate itself
    public static void initialize(double[][] plate, double top, double bot, double left, double right) {
        for(int i=0; i<plate.length; i++){
            for(int j=0; j<plate.length; j++){
                if(i==0){
                    plate[i][j]=top;
                }
                else if(i==plate.length-1){
                    plate[i][j]=bot;
                }
                else if(i>0 && i<plate.length && j==0){
                    plate[i][j]=left;
                }
                else if (i>0 && i<plate.length && (j==plate.length-1)){
                    plate[i][j]=right;
                }
                else {
                	plate[i][j] = (float) 0;
                }
            }
        }
    }
    
    // Loop until exit criteria are met, updating each newPlate cell from the
    //   average temperatures of the corresponding neighbors in oldPlate
    // Returns the maximum change between oldPlate and newPlate cells
    public static double diffuse(double[][] oldPlate, double[][] newPlate) {
        // diffuse the oldPlate into the newPlate
    	double maxChange = 0;
        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                newPlate[i][j] = (oldPlate[i + 1][j] + oldPlate[i - 1][j] +
                                  oldPlate[i][j + 1] + oldPlate[i][j - 1]) / 4.0;
                maxChange = Math.max(maxChange, Math.abs(newPlate[i][j] - oldPlate[i][j]));
            }
        }
        return maxChange;
    }
    
    
    // Swap the plates and continue ...
    public static void swap(double[][] oldPlate, double[][] newPlate) {
        // put the values of newPlate into oldPlate
        for(int i= 0; i < oldPlate.length; i++) {
            for(int j= 0; j < oldPlate.length; j++) {
                oldPlate[i][j]= newPlate[i][j];
            }
        }
    }
    
    // Print out the values of the array called for in main
    public static void print(double[][] grid) {
        
        for(int i= 0; i < grid.length; i++) {
            for(int j= 0; j < grid.length; j++) {
                grid[i][j] = (double)Math.round(grid[i][j] * 100) / 100;
                if(i!=0 && i!=grid.length-1 && j!=0 && j!=grid.length-1){
                    System.out.print(grid[i][j]+" ");
                    
                }
                
            }
            System.out.print("\n");
        }
    }
}
