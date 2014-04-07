/* Honey.java
 *
 * Honey class used by ArtificialBeeColony.java
 * Also known as food source.
 * Contains the positions of the queens in a solution as well as its conflicts, trials, fitness and selection probability. 
 * Base code at http://mf.erciyes.edu.tr/abc/
 *
 * @author: James M. Bayon-on
 * @version: 1.3
 */

public class Honey implements Comparable<Honey> {
	private int MAX_LENGTH;
	private int nectar[]; 							//solution or placement of queens
    private int trials;
    private int conflicts;
    private double fitness;
    private double selectionProbability;
    
    /* Instantiate a Honey.
     *
     * @param: size of n
     */
    public Honey(int size) {
        this.MAX_LENGTH = size;
        this.nectar = new int[MAX_LENGTH];
        this.conflicts = 0;
        this.trials = 0;
        this.fitness = 0.0;
        this.selectionProbability = 0.0;
        initNectar();
    }
    
    /* Compares two Honeys.
	 *
	 * @param: a Honey to compare with
	 */	
    public int compareTo(Honey h) {
    	return this.conflicts - h.getConflicts();
    }

    /* Initializes the Honey into diagonal queens.
	 *
	 */
    public void initNectar() { 
        for(int i = 0; i < MAX_LENGTH; i++) {		//initialize the solution to 1... n
            nectar[i] = i;
        }
    }
    
    /* Computes the conflicts in the nxn board.
	 *
	 */
    public void computeConflicts() { 				//compute the number of conflicts to calculate fitness
        String board[][] = new String[MAX_LENGTH][MAX_LENGTH]; 
        int x = 0; 									//row
        int y = 0; 									//column
        int tempx = 0;
        int tempy = 0; 
        
        int dx[] = new int[] {-1, 1, -1, 1}; 		//to check for diagonal
        int dy[] = new int[] {-1, 1, 1, -1}; 		//paired with dx to check for diagonal
        
        boolean done = false; 						//used to check is checking fo diagonal is out of bounds
        int conflicts = 0; 							//number of conflicts found
        
        board = clearBoard(board); 					//clears the board into empty strings
        board = plotQueens(board); 					//plots the Q in the board
 
        // Walk through each of the Queens and compute the number of conflicts.
        for(int i = 0; i < MAX_LENGTH; i++) {
            x = i;
            y = this.nectar[i]; 					//will result to no horizontal and vertical conflicts because it will result to diagonal 

            // Check diagonals.
            for(int j = 0; j < 4; j++) { 			//because of dx and dy where there are 4 directions for diagonal searching for conflicts
                tempx = x;
                tempy = y; 							// store coordinate in temp
                done = false;
                
                while(!done) {
                    tempx += dx[j];
                    tempy += dy[j];
                    
                    if((tempx < 0 || tempx >= MAX_LENGTH) || (tempy < 0 || tempy >= MAX_LENGTH)) { //if exceeds board
                        done = true;
                    } else {
                        if(board[tempx][tempy].equals("Q")) {
                            conflicts++;
                        }
                    }
                }
            }
        }

        this.conflicts = conflicts; 				//set conflicts of this chromosome
        
    }
    
    /* Plots the queens in the board.
	 *
	 * @param: a nxn board
	 */
    public String[][] plotQueens(String[][] board) {
        for(int i = 0; i < MAX_LENGTH; i++) {
            board[i][this.nectar[i]] = "Q";
        }
        return board;
    }
    
    /* Clears the board.
	 *
	 * @param: a nxn board
	 */
    public String[][] clearBoard(String[][] board) {
        // Clear the board.
        for(int i = 0; i < MAX_LENGTH; i++) {
            for(int j = 0; j < MAX_LENGTH; j++) {
                board[i][j] = "";
            }
        }
        return board;
    }

    /* Gets the conflicts of the Honey.
	 *
	 * @return: number of conflicts of the honey
	 */
    public int getConflicts() {
        return conflicts;
    }

    /* Sets the conflicts of the honey.
	 *
	 * @param: new number of conflicts
	 */
    public void setConflicts(int mConflicts) {
        this.conflicts = mConflicts;
    }

    /* Gets the selection probability of the honey.
	 *
	 * @return: selection probability of the honey
	 */
    public double getSelectionProbability() {
        return selectionProbability;
    }

    /* sets the selection probability of the honey.
	 *
	 * @param: new selection probability of the honey
	 */
    public void setSelectionProbability(double mSelectionProbability) {
        this.selectionProbability = mSelectionProbability;
    }

    /* Gets the fitness of a honey.
	 *
	 * @return: fitness of honey
	 */
    public double getFitness() {
        return fitness;
    }

    /* Sets the fitness of the honey.
	 *
	 * @param: new fitness
	 */
    public void setFitness(double mFitness) {
        this.fitness = mFitness;
    }

    /* Gets the data on a specified index.
	 *
	 * @param: index of data
	 * @return: position of queen
	 */
    public int getNectar(int index) {
        return nectar[index];
    }

    /* Gets the index on a specified data.
	 *
	 * @param: index of data
	 * @return: position of queen
	 */
    public int getIndex(int value) {
        int k = 0;
        for(; k < MAX_LENGTH; k++) {
            if(nectar[k] == value) {
                break;
            }
        }
        return k;
    }

    /* Sets the data on a specified index.
	 *
	 * @param: index of data
	 * @param: new position of queen
	 */
    public void setNectar(int index, int value) {
        this.nectar[index] = value;
    }

	/* Gets the number of trials of a solution.
	 *
	 * @return: number of trials
	 */
    public int getTrials() {
        return trials;
    }

    /* Sets the number of trials of a solution.
	 *
	 * @param: new number of trials
	 */
    public void setTrials(int trials) {
        this.trials = trials;
    }   
    
    /* Gets the max length.
	 *
	 * @return: max length
	 */
    public int getMaxLength() {
    	return MAX_LENGTH;
    }
}
//end honey