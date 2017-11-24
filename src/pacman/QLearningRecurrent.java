package pacman;

import org.omg.CORBA.INTERNAL;
import java.util.HashMap;
import java.util.Random;

public class QLearningRecurrent {

    enum Direction
    {
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public static final int DEPTH = 17;
    public static final int HISTORY_SIZE = 20;
    public static final int NUM_VARIABLES = 16;
    private static final double EXPLORATION_PROBABILITY = 0.5;
    private static final double LEARNING_RATE = 0.3;
    private static final double DISCOUNT_FACTOR = 0.8;
    private static final double FOOD_REWARD = 1;
    private static final double NO_FOOD_REWARD = -1;
    private static final double LOSE_LIFE = -10;
    NeuralNetwork neuralNetwork;
    Neuron current;
    Neuron[] nextPossible;
    private Maze maze;
    private PacMan pacman;
    private Ghost[] ghosts;
    private Random r = new Random();


     QLearningRecurrent(Maze maze, PacMan pacman, Ghost[] ghosts, double[] initialWeights) {
        this.maze = maze;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.neuralNetwork = new NeuralNetwork(DEPTH, HISTORY_SIZE);
    }

    boolean makeDecision(){
         boolean possibleStates[] = new boolean[4];
         int currentState[] = new int[DEPTH];
         double currentStateValue;
         double possibleStateValues[] = new double[4];

         currentState = this.estimateNextState(Direction.NONE);
         modifyStates(currentState);

         // Look at current State

        // Check position

        // Discover which directions are valid

        // Get Values for Next States

        // Pick the next one or choose a random option

         return true;
    }


    void modifyStates(int[] state){
    // Modify this state based on what happened

    // Then modify the history

    }

    int[] getState(int pacX, int pacY, int[][] ghostPos) {
        int fillCount = 0;
        int[] state = new int[DEPTH];
        if (MazeData.getData(pacX, pacY) == MazeData.MAGIC_DOT || MazeData.getData(pacX, pacY) == MazeData.NORMAL_DOT)
            state[fillCount] = 1;
        else
            state[fillCount] = 0;
        fillCount++;

        int[] ghostDistances = new int[4];
        int[] binaryGhostDistance = new int[4];
        for (int i = 0; i < 4; i++) {
            ghostDistances[i] = (int) MathUtils.getRealDistance(pacX, pacY, ghostPos[i][0], ghostPos[i][1]);
            MathUtils.convertIntToBinary(binaryGhostDistance, ghostDistances[i], 4);
            for (int j = 0; j < 4; j++) {
                state[fillCount] = binaryGhostDistance[j];
                fillCount++;
            }
            if (ghosts[i].isHollow) // At the moment if ghosts were hollow in the present state, they are assumed to
                // hollow in the next state. This could be problematic.
                state[fillCount] = 0;
            else
                state[fillCount] = 1;
            fillCount++;
        }
            return state;
    }


    // Depending on the Direction give, this will estimate the future position of pacman, the 4 ghosts
        int[] estimateNextState(Direction d )
        {
            if(d == Direction.NONE)
            {
                int[][] ghostPos = {
                        {ghosts[0].x, ghosts[0].y},
                        {ghosts[1].x, ghosts[1].y},
                        {ghosts[2].x, ghosts[2].y},
                        {ghosts[3].x, ghosts[3].y}
                };
                return getState(pacman.x,pacman.y, ghostPos);
            }

            int currentGhostX =0;
            int currentGhostY=0;
            int[][] ghostPos = {
                    {ghosts[0].x, ghosts[0].y},
                    {ghosts[1].x, ghosts[1].y},
                    {ghosts[2].x, ghosts[2].y},
                    {ghosts[3].x, ghosts[3].y}
            };
            for (int i = 0; i < ghosts.length;i++){

                if(ghosts[i].xDirection !=0)
                {
                    currentGhostX = ghosts[i].xDirection + ghosts[i].x;
                    ghostPos[i][0] = currentGhostX;
                }
                if(ghosts[i].yDirection != 0)
                {
                    currentGhostY = ghosts[i].yDirection + ghosts[i].y;
                    ghostPos[i][0] = currentGhostY;
                }

            }

            if(d == Direction.RIGHT){
                return getState(pacman.x+1, pacman.y, ghostPos);
            }

            else if(d == Direction.LEFT){
                return getState(pacman.x-1, pacman.y, ghostPos);
            }

            else if(d == Direction.UP){
                return getState(pacman.x, pacman.y-1, ghostPos);
            }

            else {
                return getState(pacman.x, pacman.y+1, ghostPos);
            }
        }

        double getStateValue(int[] arr){

        }

        boolean setStateValue(int[] arr, double newValue){

        }
}
