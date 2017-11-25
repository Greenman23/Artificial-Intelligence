package pacman;

import org.omg.CORBA.INTERNAL;
import java.util.HashMap;
import java.util.Random;

public class QLearningRecurrent {

//    enum Direction
//    {
//        NONE,
//        UP,
//        DOWN,
//        LEFT,
//        RIGHT
//    }


    protected static final int MOVE_LEFT = 0;
    protected static final int MOVE_UP = 1;
    protected static final int MOVE_RIGHT = 2;
    protected static final int MOVE_DOWN = 3;

    public static final int DEPTH = 17;
    public static final int HISTORY_SIZE = 20;
    public static final int NUM_VARIABLES = 16;
    private static final double EXPLORATION_PROBABILITY = 0.0002;
    private static final double LEARNING_RATE = 0.03;
    private static final double DISCOUNT_FACTOR = 0.9;
    private static final double FOOD_REWARD = 1;
    private static final double NO_FOOD_REWARD = -1;
    private static final double LOSE_LIFE = -10;
    NeuralNetwork neuralNetwork;
    private Maze maze;
    private PacMan pacman;
    private Ghost[] ghosts;
    private Random r = new Random();
    private double explorationProbability;
    private double reward;
    private double rewardForCurrentState;
    private int uniqueStates;
    int tries;

     QLearningRecurrent(Maze maze, PacMan pacman, Ghost[] ghosts) {
        this.maze = maze;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.neuralNetwork = new NeuralNetwork(DEPTH, HISTORY_SIZE);
        this.neuralNetwork.tree = this.neuralNetwork.getTree("History.txt");
        this.explorationProbability = EXPLORATION_PROBABILITY;
        this.reward = -.02;
        rewardForCurrentState = 0;
        uniqueStates = 0;
        tries = 0;


    }

    int makeDecision() {
        addState(estimateNextState(-1));
        boolean possibleStates[] = new boolean[4];
        int currentState[] = new int[DEPTH];
        double currentStateValue;


        int a = estimateNextBestState();
//        switch (a){
//              case MOVE_LEFT:
//              System.out.println("LEFT");
//             break;
//            case MOVE_RIGHT:
//                System.out.println("RIGHT");
//                break;
//            case MOVE_DOWN:
//                System.out.println("DOWN");
//                break;
//            case MOVE_UP:
//                System.out.println("UP");
//                break;
//        }
        if(Math.random() < EXPLORATION_PROBABILITY){
            return (int)Math.random() * 3;
        }
        return a;
    }

    void addState(int[] state){
    // Modify this state based on what happened
        if(!this.neuralNetwork.history.isEmpty()) {
            if (!state.equals(this.neuralNetwork.history.get(this.neuralNetwork.history.size() - 1))) {
                this.neuralNetwork.history.add(state);
                updateStateValues();
                rewardForCurrentState = getCurrentReward();
            }
        else{
                if(MathUtils.epsilon(rewardForCurrentState,getCurrentReward(),.01)){
                    rewardForCurrentState = getCurrentReward();
                    updateStateValues();
                }
            }
        }
        else
            this.neuralNetwork.history.add(state);
    // Then modify the history
    }

    void updateStateValues(){
        double currentStateValue;
        double possibleNextStateValue;
        currentStateValue = getStateValue(this.neuralNetwork.history.getLast());
        if(currentStateValue == 0.000)
            uniqueStates++;
        possibleNextStateValue = getStateValue(estimateNextState(estimateNextBestState()));

        currentStateValue += LEARNING_RATE * (getCurrentReward() + (DISCOUNT_FACTOR * possibleNextStateValue) - currentStateValue);
      //  System.out.println("Current " + currentStateValue);

        setStateValue(this.neuralNetwork.history.getLast(),currentStateValue);

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
        int[] binaryGhostDistance = new int[3];
        for (int i = 0; i < 4; i++) {
            ghostDistances[i] = (int)MathUtils.getRealDistance(pacX, pacY, ghostPos[i][0], ghostPos[i][1]);
          binaryGhostDistance =  MathUtils.convertIntToBinary( ghostDistances[i], 3);
            for (int j = 0; j < 3; j++) {
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
//        for (int s: state) {
//            System.out.print(s);
//        }
//        System.out.println("");
            return state;
    }


    // Depending on the Direction give, this will estimate the future position of pacman, the 4 ghosts
        int[] estimateNextState(int direction)
        {
            if(direction == -1)
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
                    if(ghostPos[i][0] == -1)
                        ghostPos[i][0] = MazeData.GRID_SIZE_X - 1;
                    if(ghostPos[i][0] == MazeData.GRID_SIZE_X)
                        ghostPos[i][0] = 0;
                }
                if(ghosts[i].yDirection != 0)
                {
                    currentGhostY = ghosts[i].yDirection + ghosts[i].y;
                    ghostPos[i][1] = currentGhostY;
                }
            }


            if(direction ==MOVE_RIGHT){
                if(pacman.x != MazeData.GRID_SIZE_X - 1)
                return getState(pacman.x+1, pacman.y, ghostPos);
                else
                    return getState(0, pacman.y, ghostPos);
            }

            else if(direction ==MOVE_LEFT){
                if(pacman.x != 0)
                return getState(pacman.x-1, pacman.y, ghostPos);
                else
                    return getState(MazeData.GRID_SIZE_X - 1, pacman.y, ghostPos);
            }

            else if(direction == MOVE_DOWN){
                return getState(pacman.x, pacman.y-1, ghostPos);
            }

            else {
                return getState(pacman.x, pacman.y+1, ghostPos);
            }
        }

        double getStateValue(int[] arr){
            return neuralNetwork.tree[MathUtils.convertToDecimal(arr)];
        }

        boolean setStateValue(int[] arr, double newValue){
            int integer  = MathUtils.convertToDecimal(arr);
            if(integer >= Math.pow(2,DEPTH))
                return false;
            neuralNetwork.tree[integer] = newValue;
            return true;
        }


        int estimateNextBestState(){
            double possibleStateValues[] = new double[4];
            if(MazeData.getData(pacman.x, pacman.y - 1) == 1)
                possibleStateValues[MOVE_UP] = -1000000;
            else
                possibleStateValues[MOVE_UP] = getStateValue(estimateNextState(MOVE_UP));

            if(MazeData.getData(pacman.x, pacman.y + 1) == 1)
                possibleStateValues[MOVE_DOWN] = -1000000;
            else
                possibleStateValues[MOVE_DOWN] = getStateValue(estimateNextState(MOVE_DOWN));

            if(MazeData.getData(pacman.x + 1, pacman.y) == 1)
                possibleStateValues[MOVE_RIGHT] = -1000000;
            else
                possibleStateValues[MOVE_RIGHT] = getStateValue(estimateNextState(MOVE_RIGHT));

            if(MazeData.getData(pacman.x - 1, pacman.y) == 1)
                possibleStateValues[MOVE_LEFT] = -100000;
            else
                possibleStateValues[MOVE_LEFT] = getStateValue(estimateNextState(MOVE_LEFT));
            double max = -1000;
            int maxState = -1;
            for(int i = 0; i < 4; i++){
                  if(possibleStateValues[i] > max) {
                    max = possibleStateValues[i];
                    maxState = i;
                  }
            }

            return maxState;
        }

        double getCurrentReward(){
            return this.reward;
        }

        void setCurrentReward(double reward){
            this.reward = reward;
        }

        void loseLife(){
            setCurrentReward(-10);
            System.out.println("Unique States visited " + uniqueStates + "\n" + "Tries " + tries);
            tries++;
            makeDecision();
            neuralNetwork.storeCurrentTree();
            for(int i = 0; i < 10; i++){
                if(i < neuralNetwork.history.size()){
                    for (int s: neuralNetwork.history.get(neuralNetwork.history.size() - 1 -i)) {
                        System.out.print(s + " ");
                    }
                    System.out.println("");
                }
            }
            this.neuralNetwork.history.clear();

        }

        void eatGhost(){
            setCurrentReward(.05);
        }

        void eatFood(){
            setCurrentReward(.01);
        }

        void eatNothing(){
            setCurrentReward(0);
        }


}
