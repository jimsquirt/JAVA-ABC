/* TesterABC.java
 *
 * Runs ArtificialBeeColony.java and logs the results into a file using Writer.java.
 * ABC testing setup is according to pass/fail criteria
 * Pass criteria - 50 success
 * Fail criteria - 100 failures
 *
 * @author: James M. Bayon-on
 * @version: 1.3
 */

public class TesterABC {
    Writer logWriter;
    ArtificialBeeColony abc;
    int MAX_RUN;
    int MAX_LENGTH;
    long[] runtimes;

    /* Instantiates the TesterABC class
     *
     */
    public TesterABC() {
        logWriter = new Writer();
        MAX_RUN = 50;
        runtimes = new long[MAX_RUN];
    }

    /* Test method accepts the N/max length, and parameters mutation rate and max epoch to set for the ABC accordingly.
     *
     * @param: max length/n
     * @param: trial limit for ABC
     * @param: max epoch for ABC
     */
    public void test(int maxLength, int trialLimit, int maxEpoch) {
        MAX_LENGTH = maxLength;
        abc = new ArtificialBeeColony(MAX_LENGTH);                                      //instantiate and define abc here
        abc.setLimit(trialLimit);
        abc.setMaxEpoch(maxEpoch);
        long testStart = System.nanoTime();
        String filepath = "ABC-N"+MAX_LENGTH+"-"+trialLimit+"-"+maxEpoch+".txt";
        long startTime = 0;
        long endTime = 0;
        long totalTime = 0;
        int fail = 0;
        int success = 0;
        
        logParameters();
        
        for(int i = 0; i < MAX_RUN; ) {                                             //run 50 sucess to pass passing criteria
            startTime = System.nanoTime();
            if(abc.algorithm()) {
                endTime = System.nanoTime();
                totalTime = endTime - startTime;
                
                System.out.println("Done");
                System.out.println("run "+(i+1));
                System.out.println("time in nanoseconds: "+totalTime);
                System.out.println("Success!");
                
                runtimes[i] = totalTime;
                i++;
                success++;
                
                //write to log
                logWriter.add((String)("Run: "+i));
                logWriter.add((String)("Runtime in nanoseconds: "+totalTime));
                logWriter.add((String)("Found at epoch: "+abc.getEpoch()));
                logWriter.add((String)("Population size: "+abc.getPopSize()));
                logWriter.add("");
                
                for(Honey h: abc.getSolutions()) {                              //write solutions to log file
                    logWriter.add(h);
                    logWriter.add("");
                }
            } else {                                                                //count failures for failing criteria
                fail++;
                System.out.println("Fail!");
            }
            
            if(fail >= 100) {
                System.out.println("Cannot find solution with these params");
                break;
            }
            startTime = 0;                                                          //reset time
            endTime = 0;
            totalTime = 0;
        }
    
        System.out.println("Number of Success: " +success);
        System.out.println("Number of failures: "+fail);
        logWriter.add("Runtime summary");
        logWriter.add("");
        
        for(int x = 0; x < runtimes.length; x++){                                   //print runtime summary
            logWriter.add(Long.toString(runtimes[x]));
        }
        
        long testEnd = System.nanoTime();
        logWriter.add(Long.toString(testStart));
        logWriter.add(Long.toString(testEnd));
        logWriter.add(Long.toString(testEnd - testStart));
        
      
        logWriter.writeFile(filepath);
        printRuntimes();
    }

    /* Converts the parameters of ABC to string and adds it to the string list in the writer class
     *
     */
    public void logParameters() {
        logWriter.add("Artificial Bee Colony Algorithm");
        logWriter.add("Parameters");
        logWriter.add((String)("MAX_LENGTH/N: "+MAX_LENGTH));
        logWriter.add((String)("STARTING_POPULATION: "+abc.getStartSize()));
        logWriter.add((String)("MAX_EPOCHS: "+abc.getMaxEpoch()));
        logWriter.add((String)("FOOD_NUMBER: "+abc.getFoodNum()));
        logWriter.add((String)("TRIAL_LIMIT: "+abc.getLimit()));
        logWriter.add((String)("MINIMUM_SHUFFLES: "+abc.getShuffleMin()));
        logWriter.add((String)("MAXIMUM_SHUFFLES: "+abc.getShuffleMax()));
        logWriter.add("");
    }

    /* Prints the runtime summary in the console
     *
     */
    public void printRuntimes() {
        for(long x: runtimes){
            System.out.println("run with time "+x+" nanoseconds");
        }   
    }

    public static void main(String args[]) {
        TesterABC tester = new TesterABC();

        tester.test(4, 50, 1000);
/*      tester.test(8, 50, 1000);
        tester.test(12, 50, 1000);
        tester.test(16, 50, 1000);
        tester.test(20, 50, 1000);
*/
        
    }
}
