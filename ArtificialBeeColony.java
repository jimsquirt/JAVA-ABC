/* ArtificialBeeColony.java
 *
 * Solves the N-Queens puzzle using Artificial Bee Colony Algorithm.
 * Code inspired by the java code for abc algorith at artificial bee colony's website
 * Found at http://mf.erciyes.edu.tr/abc/.
 *
 * Special thanks to Professor Bahriye Basturk Akay for pointing me to abc's website for the source code
 * http://mf.erciyes.edu.tr/abc/software.htm
 *
 * @author: James M. Bayon-on
 * @version: 1.0
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class ArtificialBeeColony {
	/*ABC PARAMETERS*/
	public int MAX_LENGTH; 		/*The number of parameters of the problem to be optimized*/
    public int NP; 				/*The number of total bees/colony size. employed + onlookers*/
    public int FOOD_NUMBER; 	/*The number of food sources equals the half of the colony size*/
    public int LIMIT;  			/*A food source which could not be improved through "limit" trials is abandoned by its employed bee*/
    public int MAX_EPOCH; 		/*The number of cycles for foraging {a stopping criteria}*/
    public int MIN_SHUFFLE;
    public int MAX_SHUFFLE;

    public Random rand;
    public ArrayList<Honey> foodSources;
    public ArrayList<Honey> solutions;
    public Honey gBest;
    public int epoch;

    /* Instantiates the artificial bee colony algorithm along with its parameters.
	 *
	 * @param: size of n queens
	 */
    public ArtificialBeeColony(int n) {
        MAX_LENGTH = n;
        NP = 40;					//pop size 20 to 40 or even 100
        FOOD_NUMBER = NP/2;
        LIMIT = 50;
        MAX_EPOCH = 1000;
        MIN_SHUFFLE = 8;
        MAX_SHUFFLE = 20;
        gBest = null;
        epoch = 0;
    }

    /* Starts the particle swarm optimization algorithm solving for n queens.
	 *
	 */
    public boolean algorithm() {
    	foodSources = new ArrayList<Honey>();
    	solutions = new ArrayList<Honey>();
        rand = new Random();
        boolean done = false;
        epoch = 0;

        initialize();
        memorizeBestFoodSource();

        while(!done) {
            if(epoch < MAX_EPOCH) {
                if(gBest.getConflicts() == 0) {
                    done = true;
                }
                sendEmployedBees();
                getFitness();
                calculateProbabilities();
                sendOnlookerBees();
                memorizeBestFoodSource();
                sendScoutBees();
                
                epoch++;
                // This is here simply to show the runtime status.
                System.out.println("Epoch: " + epoch);
            } else {
                done = true;
            }
            
        }
        
		if(epoch == MAX_EPOCH) {
			System.out.println("No Solution found");
			done = false;
		}
		
        System.out.println("done.");
        System.out.println("Completed " + epoch + " epochs.");
        
        for(Honey h: foodSources) {
            if(h.getConflicts() == 0) {
                System.out.println("SOLUTION");
                solutions.add(h);
                printSolution(h);
                System.out.println("conflicts:"+h.getConflicts());
            }
        }
        
        return done;
    }

    /* Sends the employed bees to optimize the solution
	 *
	 */
    public void sendEmployedBees() {
        int neighborBeeIndex = 0;
        Honey currentBee = null;
        Honey neighborBee = null;
        
        for(int i = 0; i < FOOD_NUMBER; i++) {
            //A randomly chosen solution is used in producing a mutant solution of the solution i
            //neighborBee = getRandomNumber(0, Food_Number-1);
            neighborBeeIndex = getExclusiveRandomNumber(FOOD_NUMBER-1, i);
            currentBee = foodSources.get(i);
            neighborBee = foodSources.get(neighborBeeIndex);
            sendToWork(currentBee, neighborBee);
        }
    }

    /* Sends the onlooker bees to optimize the solution. Onlooker bees work on the best solutions from the employed bees. best solutions have high selection probability. 
	 *
	 */
    public void sendOnlookerBees() {
    	int i = 0;
        int t = 0;
        int neighborBeeIndex = 0;
        Honey currentBee = null;
        Honey neighborBee = null;

        while(t < FOOD_NUMBER) {
            currentBee = foodSources.get(i);
            if(rand.nextDouble() < currentBee.getSelectionProbability()) {
                t++;
                neighborBeeIndex = getExclusiveRandomNumber(FOOD_NUMBER-1, i);
	            neighborBee = foodSources.get(neighborBeeIndex);
	            sendToWork(currentBee, neighborBee);
            }
            i++;
            if(i == FOOD_NUMBER) {
                i = 0;
            }
        }
    }

	/* The optimization part of the algorithm. improves the currentbee by choosing a random neighbor bee. the changes is a randomly generated number of times to try and improve the current solution.
	 *
	 * @param: the currently selected bee
	 * @param: a randomly selected neighbor bee
	 * @param: the number of times to try and improve the solution
	 */
    public void sendToWork(Honey currentBee, Honey neighborBee) {
    	int newValue = 0;
        int tempValue = 0;
        int tempIndex = 0;
        int prevConflicts = 0;
        int currConflicts = 0;
        int parameterToChange = 0;

        //get number of conflicts
        prevConflicts = currentBee.getConflicts();

        //The parameter to be changed is determined randomly
        parameterToChange = getRandomNumber(0, MAX_LENGTH-1);

        /*v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij}) 
        solution[param2change]=Foods[i][param2change]+(Foods[i][param2change]-Foods[neighbour][param2change])*(r-0.5)*2;
        */
        tempValue = currentBee.getNectar(parameterToChange);
        newValue = (int)(tempValue+(tempValue - neighborBee.getNectar(parameterToChange))*(rand.nextDouble()-0.5)*2);

        //trap the value within upper bound and lower bound limits
        if(newValue < 0) {
            newValue = 0;
        }
        if(newValue > MAX_LENGTH-1) {
            newValue = MAX_LENGTH-1;
        }

        //get the index of the new value
        tempIndex = currentBee.getIndex(newValue);

        //swap
        currentBee.setNectar(parameterToChange, newValue);
        currentBee.setNectar(tempIndex, tempValue);
        currentBee.computeConflicts();
        currConflicts = currentBee.getConflicts();
        
        //greedy selection
        if(prevConflicts < currConflicts) {						//No improvement
            currentBee.setNectar(parameterToChange, tempValue);
            currentBee.setNectar(tempIndex, newValue);
            currentBee.computeConflicts();
            currentBee.setTrials(currentBee.getTrials() + 1);
        } else {												//improved solution
            currentBee.setTrials(0);
        }   
        
    }

    /* Finds food sources which have been abandoned/reached the limit.
     * Scout bees will generate a totally random solution from the existing and it will also reset its trials back to zero.
     *
     */
    public void sendScoutBees() {
        Honey currentBee = null;
        int shuffles = 0;

        for(int i =0; i < FOOD_NUMBER; i++) {
            currentBee = foodSources.get(i);
            if(currentBee.getTrials() >= LIMIT) {
                shuffles = getRandomNumber(MIN_SHUFFLE, MAX_SHUFFLE);
                for(int j = 0; j < shuffles; j++) {
                    randomlyArrange(i);
                }
                currentBee.computeConflicts();
                currentBee.setTrials(0);

            }
        }
    }

	/* Sets the fitness of each solution based on its conflicts
	 *
	 */
    public void getFitness() {
    	// Lowest errors = 100%, Highest errors = 0%
        Honey thisFood = null;
        double bestScore = 0.0;
        double worstScore = 0.0;

        // The worst score would be the one with the highest energy, best would be lowest.
        worstScore = Collections.max(foodSources).getConflicts();

        // Convert to a weighted percentage.
        bestScore = worstScore - Collections.min(foodSources).getConflicts();

        for(int i = 0; i < FOOD_NUMBER; i++) {
            thisFood = foodSources.get(i);
            thisFood.setFitness((worstScore - thisFood.getConflicts()) * 100.0 / bestScore);
        }   
    }

    /* Sets the selection probability of each solution. the higher the fitness the greater the probability 
	 *
	 */ 
	public void calculateProbabilities() {
    	Honey thisFood = null;
        double maxfit = foodSources.get(0).getFitness();
        
        for(int i = 1; i < FOOD_NUMBER; i++) {
            thisFood = foodSources.get(i);
            if(thisFood.getFitness() > maxfit) {
                maxfit = thisFood.getFitness();
            }
        }
         
        for(int j = 0; j < FOOD_NUMBER; j++) {
            thisFood = foodSources.get(j);
            thisFood.setSelectionProbability((0.9*(thisFood.getFitness()/maxfit))+0.1);
        }
    }

    /* Initializes all of the solutions' placement of queens in ramdom positions.
	 *
	 */ 
    public void initialize() {
    	int newFoodIndex = 0;
        int shuffles = 0;
        
        for(int i = 0; i < FOOD_NUMBER; i++) {
            Honey newHoney = new Honey(MAX_LENGTH);
       
            foodSources.add(newHoney);
            newFoodIndex = foodSources.indexOf(newHoney);
            
            shuffles = getRandomNumber(MIN_SHUFFLE, MAX_SHUFFLE);
            
            for(int j = 0; j < shuffles; j++) {
                randomlyArrange(newFoodIndex);
            }
            
            foodSources.get(newFoodIndex).computeConflicts();
        } // i
    }

    /* Gets a random number in the range of the parameters
	 *
	 * @param: the minimum random number
	 * @param: the maximum random number
	 * @return: random number
	 */ 
    public int getRandomNumber(int low, int high) {
        return (int)Math.round((high - low) * rand.nextDouble() + low);
    }

    /* Gets a random number with the exception of the parameter
	 *
	 * @param: the maximum random number
	 * @param: number to to be chosen
	 * @return: random number
	 */ 
    public int getExclusiveRandomNumber(int high, int except) {
        boolean done = false;
        int getRand = 0;

        while(!done) {
            getRand = rand.nextInt(high);
            if(getRand != except){
                done = true;
            }
        }

        return getRand;     
    }

    /* Changes a position of the queens in a particle by swapping a randomly selected position
	 *
	 * @param: index of the solution
	 */ 
    public void randomlyArrange(int index) {
        int positionA = getRandomNumber(0, MAX_LENGTH - 1);
        int positionB = getExclusiveRandomNumber(MAX_LENGTH - 1, positionA);
        Honey thisHoney = foodSources.get(index);
        int temp = thisHoney.getNectar(positionA);
        thisHoney.setNectar(positionA, thisHoney.getNectar(positionB));
        thisHoney.setNectar(positionB, temp);          
    }

    /* Memorizes the best solution
	 *
	 */ 
    public void memorizeBestFoodSource() {
    	gBest = Collections.min(foodSources);
    }

    /* Prints the nxn board with the queens
	 *
	 * @param: a chromosome
	 */ 
    public void printSolution(Honey solution) {
        String board[][] = new String[MAX_LENGTH][MAX_LENGTH];
        
        // Clear the board.
        for(int x = 0; x < MAX_LENGTH; x++) {
            for(int y = 0; y < MAX_LENGTH; y++) {
                board[x][y] = "";
            }
        }

        for(int x = 0; x < MAX_LENGTH; x++) {
            board[x][solution.getNectar(x)] = "Q";
        }

        // Display the board.
        System.out.println("Board:");
        for(int y = 0; y < MAX_LENGTH; y++) {
            for(int x = 0; x < MAX_LENGTH; x++) {
                if(board[x][y] == "Q") {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.print("\n");
        }
    }    

    /* gets the solutions
 	 *
 	 * @return: solutions
 	 */  
 	public ArrayList<Honey> getSolutions() {
 		return solutions;
 	}

 	 /* gets the epoch
 	 *
 	 * @return: epoch
 	 */ 
 	public int getEpoch() {
 		return epoch;
 	}

     /* sets the max epoch
     *
     * @return: new max epoch value
     */ 
 	public void setMaxEpoch(int newMaxEpoch) {
 		this.MAX_EPOCH = newMaxEpoch;
 	}

 	/* gets the population size
 	 *
 	 * @return: pop size
 	 */ 
 	public int getPopSize() {
 		return foodSources.size();
 	}

 	/* gets the start size
 	 *
 	 * @return: start size
 	 */ 
 	public int getStartSize() {
 		return NP;
 	}

 	/* gets the number of food
 	 *
 	 * @return: food number
 	 */ 
 	public double getFoodNum() {
 		return FOOD_NUMBER;
 	}

 	/* gets the limit for trials for all food sources
 	 *
 	 * @return: number of trials limit
 	 */ 
 	public int getLimit() {
 		return LIMIT;
 	}

    /* sets the limit for trials for all food sources
     *
     * @param: new trial limit
     */    
 	public void setLimit(int newLimit) {
 		this.LIMIT = newLimit;
 	}

 	/* gets the max epoch
 	 *
 	 * @return: max epoch
 	 */ 
 	public int getMaxEpoch() {
 		return MAX_EPOCH;
 	}

 	/* gets the min shuffle
 	 *
 	 * @return: min shuffle
 	 */ 
 	public int getShuffleMin() {
 		return MIN_SHUFFLE;
 	}
    
 	/* gets the max shuffle
 	 *
 	 * @return: max shuffle
 	 */ 
 	public int getShuffleMax() {
 		return MAX_SHUFFLE;
 	}  
}