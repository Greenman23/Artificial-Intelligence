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

    public static final int DEPTH = 21;
    public static final int HISTORY_SIZE = 20;
    private static final double EXPLORATION_PROBABILITY = 0.002;
    private static final double LEARNING_RATE = 0.03;
    private static final double DISCOUNT_FACTOR = 0.9;
    private static final double FOOD_REWARD = .01;
    private static final double NO_FOOD_REWARD = -.01;
    private static final double EAT_GHOST = 0;
    private static final double ALIVE = 0;
    private static final double LOSE_LIFE = -100;
    private static final double EXPLORE_REWARD = .01;
    private static final double NO_EXPLORE_REWARD = -.01;
    NeuralNetwork neuralNetwork;
    private Maze maze;
    private PacMan pacman;
    private Ghost[] ghosts;
    private Random r = new Random();
    private double explorationProbability;
    private double reward;
    private double rewardForCurrentState;
    private int uniqueStates;
    private boolean[][] locations;
    int xCoord;
    int yCoord;
    int lastPacManMove;

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
        locations = new boolean[MazeData.GRID_SIZE_X][MazeData.GRID_SIZE_Y];
        for(int i = 0; i < MazeData.GRID_SIZE_X; i++)
            for(int j = 0; j < MazeData.GRID_SIZE_Y; j++)
                if(MazeData.getData(i,j) == MazeData.BLOCK)
                    locations[i][j] = true;
                else
                    locations[i][j] = false;
                xCoord = 0;
                yCoord = 0;
                lastPacManMove = 0;
    }

    int makeDecision() {
            addState(estimateNextState(-1));
            xCoord = pacman.x;
            yCoord = pacman.y;
            if (getCurrentReward() == LOSE_LIFE) {

                neuralNetwork.history.clear();
            }

            boolean newPlace = (!locations[pacman.x][pacman.y]);
            reachNewLocation(newPlace);
            locations[pacman.x][pacman.y] = true;
            int a = estimateNextBestState();
            if (Math.random() < EXPLORATION_PROBABILITY) {
                System.out.println("Explore");
                return (int) Math.random() * 3;
            }
            lastPacManMove = a;
            return a;
        }




    void addState(int[] state){
        // Modify this state based on what happened
        if(!this.neuralNetwork.history.isEmpty()) {
            if (!state.equals(this.neuralNetwork.history.get(this.neuralNetwork.history.size() - 1))) {
                this.neuralNetwork.history.add(state);
                rewardForCurrentState = getCurrentReward();
                updateStateValues();

            }
            else if(getCurrentReward() == LOSE_LIFE){
                updateStateValues();
                setCurrentReward(0);
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
            if (currentStateValue == 0.000) uniqueStates++;
            possibleNextStateValue = getStateValue(estimateNextState(estimateNextBestState()));

            setCurrentReward(getCurrentReward() + ghostsFarAway());
        
            currentStateValue += LEARNING_RATE * (getCurrentReward() + (DISCOUNT_FACTOR * possibleNextStateValue) - currentStateValue);
//            if(getCurrentReward() != LOSE_LIFE && currentStateValue < -3){
//                currentStateValue = -9.999999;
//            }
            //  System.out.println("Current " + currentStateValue);

            setStateValue(this.neuralNetwork.history.getLast(), currentStateValue);
        if (getCurrentReward() == LOSE_LIFE) {
            setCurrentReward(0);
                System.out.println("Current Dead State " + currentStateValue);
        }
        else{
            if(currentStateValue < -.9)
            System.out.println("Current State " + currentStateValue);

        }

//        if(getCurrentReward() == LOSE_LIFE) {
//            currentStateValue = getStateValue(this.neuralNetwork.history.get(this.neuralNetwork.history.size() - 2));
//            possibleNextStateValue = getStateValue(estimateNextState(estimateNextBestState()));
//            currentStateValue += LEARNING_RATE * (getCurrentReward() + (DISCOUNT_FACTOR * possibleNextStateValue) - currentStateValue);
//
//            setStateValue(this.neuralNetwork.history.get(this.neuralNetwork.history.size() - 2), currentStateValue);
//            setStateValue(this.neuralNetwork.history.get(this.neuralNetwork.history.size() - 1), currentStateValue);
//            for(int s:neuralNetwork.history.get(neuralNetwork.history.size()-2)){
//                System.out.print(s +   " ");
//            }
//            System.out.println("");
//            this.neuralNetwork.history.clear();
//
//        }
        setCurrentReward(0);
    }


        double ghostsFarAway(){
        double minGhostDistance = 1000;
        double tempGhostDistance = 0;
        for(int i = 0; i < ghosts.length; i++)
            tempGhostDistance = MathUtils.getRealDistance(pacman.x,pacman.y,ghosts[i].x,ghosts[i].y);
            if(tempGhostDistance > minGhostDistance )
                minGhostDistance = tempGhostDistance;
            return minGhostDistance / 100;
        }

    int[] getNearestNewLocation(int direction){

        int[] distance = new int[5];
        int[] tempDistance = new int[2];
        int currentWidth = 0;
        int currentHeight = 0;
        boolean alter = false;
        for (int s: distance) {
            s = 0;
        }
        for(int width = -2; width <= 2; width++)
            for (int height = -3; height <= 3; height++){
                alter = false;
                if (pacman.x + width >= 0 && pacman.x + width < MazeData.GRID_SIZE_X && pacman.y + height >= 0 && pacman.y + height < MazeData.GRID_SIZE_Y && (width != 0 || height != 0)) {
                    if (!locations[pacman.x + width][pacman.y + height]) {
                        if (distance[0] == 0) {
                            alter = true;
                        }

                        else if (Math.abs(width) + Math.abs(height) < Math.abs(currentHeight) + Math.abs(currentWidth)) {
                            alter = true;
                        }

                        else if(Math.abs(width) + Math.abs(height) == Math.abs(currentHeight) + Math.abs(currentWidth)){
                            switch (direction) {
                                case MOVE_LEFT:
                                    if(width < currentWidth)
                                        alter = true;
                                    break;

                                case MOVE_DOWN:
                                    if(height < currentHeight)
                                        alter = true;
                                    break;

                                case MOVE_RIGHT:
                                    if(width > currentWidth)
                                        alter = true;
                                    break;

                                case MOVE_UP:
                                    if(height > currentHeight)
                                        alter = true;
                                    break;
                            }
                        }
                        if(alter){
                            tempDistance = MathUtils.convertIntToBinary(width + 2, 2);
                            distance[0] = tempDistance[0];
                            distance[1] = tempDistance[1];
                            tempDistance = MathUtils.convertIntToBinary(height + 3, 3);
                            distance[2] = tempDistance[0];
                            distance[3] = tempDistance[1];
                            distance[4] = tempDistance[2];
                            currentWidth = width;
                            currentHeight = height;
                        }
                    }
                }
            }
        return distance;
    }

    int[] getGhostManhattanData(int pacX, int pacY ,int ghostPosX, int ghostPosY){
        int diffX = Math.abs(pacX - ghostPosX - 1);
        int difY = Math.abs(pacY - ghostPosY - 1);
        int[] diffXBinary = MathUtils.convertIntToBinary(diffX, 2);
        int[] diffYBinary = MathUtils.convertIntToBinary(difY, 2);
        int[] manhattanDistance = {diffYBinary[0], diffYBinary[1], diffXBinary[0], diffXBinary[1]};
        return manhattanDistance;
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
            return getState(pacman.x,pacman.y, ghostPos, direction);
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

//            if(ghosts[i].xDirection !=0)
//            {
//                currentGhostX = ghosts[i].xDirection + ghosts[i].x;
//                ghostPos[i][0] = currentGhostX;
//                if(ghostPos[i][0] == -1)
//                    ghostPos[i][0] = MazeData.GRID_SIZE_X - 1;
//                if(ghostPos[i][0] == MazeData.GRID_SIZE_X)
//                    ghostPos[i][0] = 0;
//            }
//            if(ghosts[i].yDirection != 0)
//            {
//                currentGhostY = ghosts[i].yDirection + ghosts[i].y;
//                ghostPos[i][1] = currentGhostY;
//            }
            // Check Right
            double closeness = 10000000;
            double tempCloseNess = 0;
            int decision = 0;
            if(ghostPos[i][0] + 1 < MazeData.GRID_SIZE_X)
                if(MazeData.getData(ghostPos[i][0] + 1, ghostPos[i][1]) != MazeData.BLOCK) {
                    tempCloseNess = MathUtils.getRealDistance(pacman.x, pacman.y, ghostPos[i][0] + 1, ghostPos[i][1]);
                    if(tempCloseNess < closeness){
                        closeness = tempCloseNess;
                        decision = MOVE_RIGHT;
                    }
                }

            // Check Left
            if(ghostPos[i][0] - 1 >= 0)
                if(MazeData.getData(ghostPos[i][0] - 1, ghostPos[i][1]) != MazeData.BLOCK) {
                    tempCloseNess = MathUtils.getRealDistance(pacman.x, pacman.y, ghostPos[i][0] - 1, ghostPos[i][1]);
                    if(tempCloseNess < closeness){
                        closeness = tempCloseNess;
                        decision = MOVE_LEFT;
                    }
                }


            // Check Up

            if(MazeData.getData(ghostPos[i][0], ghostPos[i][1] + 1) != MazeData.BLOCK) {
                tempCloseNess = MathUtils.getRealDistance(pacman.x, pacman.y, ghostPos[i][0], ghostPos[i][1] + 1);
                if(tempCloseNess < closeness){
                    closeness = tempCloseNess;
                    decision = MOVE_UP;
                }
            }

            // Check Down

            if(MazeData.getData(ghostPos[i][0], ghostPos[i][1] - 1) != MazeData.BLOCK) {
                tempCloseNess = MathUtils.getRealDistance(pacman.x, pacman.y, ghostPos[i][0], ghostPos[i][1] - 1);
                if(tempCloseNess < closeness){
                    closeness = tempCloseNess;
                    decision = MOVE_DOWN;
                }
            }

            switch (decision){
                case MOVE_DOWN:
                    ghostPos[i][1] += -1;
                    break;
                case MOVE_LEFT:
                    ghostPos[i][0] += -1;
                case MOVE_RIGHT:
                    ghostPos[i][0] += 1;
                case MOVE_UP:
                    ghostPos[i][1] += 1;
            }
        }


        if(direction ==MOVE_RIGHT){
            if(pacman.x != MazeData.GRID_SIZE_X - 1)
                return getState(pacman.x+1, pacman.y, ghostPos,direction);
            else
                return getState(0, pacman.y, ghostPos,direction);
        }

        else if(direction ==MOVE_LEFT){
            if(pacman.x != 0)
                return getState(pacman.x-1, pacman.y, ghostPos,direction);
            else
                return getState(MazeData.GRID_SIZE_X - 1, pacman.y, ghostPos,direction);
        }

        else if(direction == MOVE_DOWN){
            return getState(pacman.x, pacman.y-1, ghostPos,direction);
        }

        else {
            return getState(pacman.x, pacman.y+1, ghostPos,direction);
        }
    }


    int[] getState(int pacX, int pacY, int[][] ghostPos, int direction) {


        int[] state = new int[DEPTH];
        int position = 0;
        int[] nearLocation = getNearestNewLocation(direction);
        for(int i = 0; i < nearLocation.length; i++){
            state[position] = nearLocation[i];
            position++;
        }
        int[] ghostDistance;
        for(int i = 0; i < 4; i++){
            ghostDistance = getGhostManhattanData(pacX,pacY,ghostPos[i][0], ghostPos[i][1]);
            for(int j = 0; j < 4; j++){
                state[position] = ghostDistance[j];
                position++;
            }
        }


        // Find data for all of ghosts

        return state;
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

    double getCurrentReward(){

        return this.reward;
    }

    void setCurrentReward(double reward){

        this.reward = reward;
    }

    void loseLife(){
        setCurrentReward(LOSE_LIFE);
        System.out.println("Unique States visited " + uniqueStates);
        neuralNetwork.storeCurrentTree();
        for(int i = 0; i < MazeData.GRID_SIZE_X; i++)
            for(int j = 0; j < MazeData.GRID_SIZE_Y; j++)
                if(MazeData.getData(i,j) == MazeData.BLOCK)
                    locations[i][j] = true;
                else
                    locations[i][j] = false;
        makeDecision();
    }

    void eatGhost(){
        //  setCurrentReward(EAT_GHOST);
    }

    void eatFood(){

        //setCurrentReward(FOOD_REWARD);
    }

    void eatNothing(){

        //setCurrentReward(NO_FOOD_REWARD);
    }

    void reachNewLocation(boolean newState){
        if(getCurrentReward() != LOSE_LIFE) {
            if (newState)
                setCurrentReward(EXPLORE_REWARD);
            else
                setCurrentReward(NO_EXPLORE_REWARD);
        }
    }

    void alive(){
        //setCurrentReward(ALIVE);
    }
}
