This repository contains a JAVA code implementation for the Artificial Bee Colony Algorithm in solving the N-Queens problem.

Classes include:

Honey.java - class which contains the solutions.
ArtificialBeeColony.java - class which implements the ABC algorithm. Algorithm parameters are defined here.
Writer.java - class which holds a string list to be written in a log file.
TesterABC.java - class which runs the tests and invokes the creation of the log file. 

Sample log file:

ABC-N4-50-1000.txt

Artificial Bee Colony Algorithm
Parameters
MAX_LENGTH/N: 4
STARTING_POPULATION: 40
MAX_EPOCHS: 1000
FOOD_NUMBER: 20.0
TRIAL_LIMIT: 50
MINIMUM_SHUFFLES: 8
MAXIMUM_SHUFFLES: 20

Run: 1
Runtime in nanoseconds: 11411487
Found at epoch: 2
Population size: 20

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. Q . . 
. . . Q 
Q . . . 
. . Q . 

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. Q . . 
. . . Q 
Q . . . 
. . Q . 

Run: 2
Runtime in nanoseconds: 2161878
Found at epoch: 1
Population size: 20

. Q . . 
. . . Q 
Q . . . 
. . Q . 

Run: 3
Runtime in nanoseconds: 8841041
Found at epoch: 1
Population size: 20

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. . Q . 
Q . . . 
. . . Q 
. Q . . 

. Q . . 
. . . Q 
Q . . . 
. . Q . 

. . Q . 
Q . . . 
. . . Q 
. Q . . 


...