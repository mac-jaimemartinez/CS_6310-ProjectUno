package Tpdohp;

public class LatticePlate {
    
    public static int dimension;
    private static int maxIterations= 100;
    private static  double relChgStopCriteria = 1.00;
    public static double topEdgeTemp, bottomEdgeTemp, leftEdgeTemp, rightEdgeTemp;
     
        //Object to represent our lattice.
        private static class Lattice{
        	//Object to represent a node in our lattice.
            private class LatticeNode{
                //neighboring nodes
                private LatticeNode leftNode;
                private LatticeNode rightNode;
                private LatticeNode topNode;
                private LatticeNode bottomNode;
                
                //Nodes temperature.
                private double temperature;
            }
	        //Node pointers for holding root (top left), and for facilitating traversal of lattices.
	        private final LatticeNode topLeft;
	        private LatticeNode aboveCurrentNode;
	        private LatticeNode currentLeftEdge;
	        private LatticeNode currentNode;
	        
	        //Constructor
	        public Lattice() {
	        	//initialize variables for navigating lattice
	        	topLeft = new LatticeNode();
	        	currentNode = topLeft;
	        	currentLeftEdge = topLeft;
	        	aboveCurrentNode = null;
            
	        	for(int i=0; i<dimension; i++){          //i denotes row we are in
	        		for(int j=0; j<dimension; j++){      //j denotes column we are in
                    
	        			//set top (left should already be set)
	        			currentNode.topNode = aboveCurrentNode;
                    
	        			//If above not null, set aboveNodes bottom to current to interlock.
	        			if( aboveCurrentNode != null ) {
	        				aboveCurrentNode.bottomNode = currentNode;
	        				aboveCurrentNode = aboveCurrentNode.rightNode;
	        			}
                    
	        			//determine if we will have a new node to go to or not
	        			if( j < dimension - 1) {
	        				//create next node, and interlock current with next node, then move on (left & right set)
	        				LatticeNode nextNode = new LatticeNode();
	        				currentNode.rightNode = nextNode;
	        				nextNode.leftNode = currentNode;
	        				currentNode = nextNode;
                    }
                }
                //prepare to move on to new row
                aboveCurrentNode = currentLeftEdge;
                currentLeftEdge = new LatticeNode();
                currentNode = currentLeftEdge;
	        	}
            }
        }
        
        // Run the simulation
        public void runSimulation(int dim, double top, double bottom, double left,
        		double right, MetaData metaData) {
        	
        	dimension = dim;
        	topEdgeTemp = top;
        	bottomEdgeTemp = bottom;
        	leftEdgeTemp = left;
        	rightEdgeTemp = right;
        	
        	// Initialize plates before simulation
        	Lattice oldPlate = new Lattice();
        	Lattice newPlate = new Lattice();
        	
        	// Simulate Diffusion by averaging the temperatures
        	double maxChange = 100;
        	int i = 0;
        	for ( ; i < maxIterations && maxChange > relChgStopCriteria; i++) {
        			maxChange = diffuse(oldPlate, newPlate);
        			copy(oldPlate, newPlate);
        	}
        	metaData.setNumIterations(i);
        	
        	// Display the results
        	print(newPlate);
        }
      
        
        private void copy(Lattice destination, Lattice source) {
        	
            source.currentLeftEdge = source.topLeft;
            source.currentNode = source.currentLeftEdge;
            
            destination.currentLeftEdge = destination.topLeft;
            destination.currentNode = destination.currentLeftEdge;
            
            // Loop over the lattices.
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    //Copy and adjust position.
                    destination.currentNode.temperature = source.currentNode.temperature;
                    
                    source.currentNode = source.currentNode.rightNode;
                    destination.currentNode = destination.currentNode.rightNode;
                }
                //Update our current pointers for traversing the lattices.
                source.currentNode = source.currentLeftEdge.bottomNode;
                source.currentLeftEdge = source.currentNode;
                destination.currentNode = destination.currentLeftEdge.bottomNode;
                destination.currentLeftEdge = destination.currentNode;
            }
        }
        
        // Loop until exit criteria are met, updating each newPlate cell from the
        //   average temperatures of the corresponding neighbors in oldPlate
        public static double diffuse(Lattice oldPlate, Lattice newPlate) {
            
        	double maxChange = 0;
            newPlate.currentLeftEdge = newPlate.topLeft;
            newPlate.currentNode = newPlate.currentLeftEdge;
            
            oldPlate.currentLeftEdge = oldPlate.topLeft;
            oldPlate.currentNode = oldPlate.currentLeftEdge;
            
            //Holders for neighboring temperatures.
            double tempLeft;
            double tempRight;
            double tempTop;
            double tempBottom;
            double oldTemperature;
            double newTemperature;
            
            // diffuse the oldPlate into the newPlate
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    
                    //get appropriate temperatures whether we are on edges or not.
                    tempLeft = oldPlate.currentNode.leftNode != null ? oldPlate.currentNode.leftNode.temperature : leftEdgeTemp;
                    tempRight = (oldPlate.currentNode.rightNode != null) ? oldPlate.currentNode.rightNode.temperature : rightEdgeTemp;
                    tempTop = (oldPlate.currentNode.topNode != null) ? oldPlate.currentNode.topNode.temperature : topEdgeTemp;
                    tempBottom = (oldPlate.currentNode.bottomNode != null) ? oldPlate.currentNode.bottomNode.temperature : bottomEdgeTemp;
                    
                    oldTemperature = oldPlate.currentNode.temperature;
                    newTemperature = (tempLeft + tempRight + tempTop + tempBottom) / 4;
                    maxChange = Math.max(maxChange, Math.abs(newTemperature - oldTemperature));
                    newPlate.currentNode.temperature = newTemperature;
                    // move right
                    newPlate.currentNode = newPlate.currentNode.rightNode;
                    oldPlate.currentNode = oldPlate.currentNode.rightNode;
                }
                // move down.
                newPlate.currentNode = newPlate.currentLeftEdge.bottomNode;
                newPlate.currentLeftEdge = newPlate.currentNode;
                oldPlate.currentNode = oldPlate.currentLeftEdge.bottomNode;
                oldPlate.currentLeftEdge = oldPlate.currentNode;
            }
            return maxChange;
        }
        
        // Print out the values of the lattice.
        public void print(Lattice Plate) {
            
            Plate.currentLeftEdge = Plate.topLeft;
            Plate.currentNode = Plate.currentLeftEdge;
            
            for(int i= 0; i < dimension; i++) {
                for(int j= 0; j < dimension; j++) {
                    // currentNode.temperature = (double)Math.round(currentNode.temperature * 100) / 100;
                    System.out.print( String.format("%.2f", Plate.currentNode.temperature)+" ");
                    Plate.currentNode = Plate.currentNode.rightNode;
                }
                //Print new line and update our current pointers for traversing lattice.
                System.out.print("\n");
                Plate.currentNode = Plate.currentLeftEdge.bottomNode;
                Plate.currentLeftEdge = Plate.currentNode;
            }
        }
    }
