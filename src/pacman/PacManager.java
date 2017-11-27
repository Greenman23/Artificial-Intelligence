package pacman;

import java.util.LinkedList;

public class PacManager {
    private int currentDirection;
    private int currentAction;
    private int uniqueStates;
    private double currentReward;
    private boolean[][] locations;
    private boolean isDead;
    private Maze maze;
    private PacMan pacMan;
    private Ghost[] ghosts;
    private StateTable stateTable;
    private Coordinate lastCoordinate;
    private LinkedList<Coordinate> coordinates;
    private State currentState;
    private static final int DEPTH = 15;
    private static final int HISTORY_SIZE = 10;
    protected static final int MOVE_LEFT = 0;
    protected static final int MOVE_UP = 1;
    protected static final int MOVE_RIGHT = 2;
    protected static final int MOVE_DOWN = 3;
    protected static final double REWARD_DEATH = -5;
    protected static final double REWARD_EXPLORE = .01;
    protected static final double LEARN_RATE = .03;
    protected static final double DISCOUNT_RATE = .95;
    protected static final int PROXIMITY = 2;


    PacManager(Maze maze, PacMan pacman, Ghost[] ghosts){
        this.maze = maze;
        this.pacMan = pacman;
        this.ghosts = ghosts;
        locations = new boolean[MazeData.GRID_SIZE_X][MazeData.GRID_SIZE_Y];
        currentState = new State();
        lastCoordinate = new Coordinate(0,0);
        coordinates = new LinkedList<Coordinate>();
        for(int i = 0; i < MazeData.GRID_SIZE_X; i++)
        for(int j = 0; j < MazeData.GRID_SIZE_Y; j++)
            if(MazeData.getData(i,j) == MazeData.BLOCK)
                locations[i][j] = true;
            else
                locations[i][j] = false;
            isDead = false;
            currentDirection = 0;
            currentAction = 0;
            currentReward = 0;
            stateTable = new StateTable(DEPTH,HISTORY_SIZE);
            stateTable.storeHistoryFile();
            uniqueStates = 0;

    }

    int makeDecision(){
        if(isDead){
            isDead = false;
            updateStateOnDeath();
            return currentDirection;
        }

        else if(pacMan.x != lastCoordinate.getX() || pacMan.y != lastCoordinate.getY()){
            updateState();
            lastCoordinate.setCoordinates(pacMan.x,pacMan.y);
            return takeBestAction();
        }

        else{
            currentDirection = takeBestAction();
            return currentDirection;
        }
    }

    void updateStateOnDeath(){
        currentState.setActionWeights(currentAction, REWARD_DEATH);
        for(int i = 0; i < MazeData.GRID_SIZE_X; i++)
            for(int j = 0; j < MazeData.GRID_SIZE_Y; j++)
                if(MazeData.getData(i,j) == MazeData.BLOCK)
                    locations[i][j] = true;
                else
                    locations[i][j] = false;
    }

    void updateState(){
        if(locations[pacMan.x][pacMan.y] == false){
            currentReward = .01;
        }
        else
            currentReward = -.01;
        locations[pacMan.x][pacMan.y] = true;
        State state = this.currentState;
        this.currentState = getState();
        Q_Learn(state);
    }


    void Q_Learn(State state){
        if(state.getActionWeight(currentAction) == 0) uniqueStates++;
        state.setActionWeights(currentAction,state.getActionWeight(currentAction) +
        LEARN_RATE * (currentReward + DISCOUNT_RATE * (getMaxActionValue(currentState)) -
        state.getActionWeight(currentAction)));

    }

    double getMaxActionValue(State state){
        double maxWeight = -1000000;
        for(int i = 0; i < 4; i++){
            if(state.getActionWeight(i) > maxWeight)
                maxWeight = state.getActionWeight(i);
        }
        return maxWeight;
    }

    int takeBestAction(){
        int maxAction = -1;
        double maxWeight = -1000000;
        for(int i = 0; i < 4; i++){
            if(actionIsPossible(i))
            if(currentState.getActionWeight(i) > maxWeight) {
                maxWeight = currentState.getActionWeight(i);
                maxAction = i;
            }
        }
        currentDirection = maxAction;
        currentAction = maxAction;
        return maxAction;
    }

    boolean actionIsPossible(int i){
        switch (i){
            case MOVE_DOWN:
                if(MazeData.getData(pacMan.x,pacMan.y + 1) == 1)
                    return  false;
                break;
            case MOVE_LEFT:
                if(MazeData.getData(pacMan.x - 1,pacMan.y) == 1)
                    return  false;
                if(pacMan.x == 6 && pacMan.y == 14)
                    return false;
                break;
            case MOVE_RIGHT:
                if(MazeData.getData(pacMan.x + 1,pacMan.y) == 1)
                    return  false;
                if(pacMan.x == 23 && pacMan.y ==14 )
                    return false;
                break;
            case MOVE_UP:
                if(MazeData.getData(pacMan.x,pacMan.y - 1) == 1)
                    return  false;
                break;
        }
        return true;
    }

    State getState(){
        int[] sortedGhostNum = new int[4];
        int[] stateData = new int[DEPTH];
        int counter = 0;
        stateData[counter] = isPacmanCloseToGhosts(sortedGhostNum);
        counter++;


        // Pacman is not close the ghosts
        if(stateData[0] == 0){
            switch (currentDirection){
                case MOVE_LEFT:
                    stateData[counter] = 0;
                    stateData[counter + 1] = 0;
                    break;
                case MOVE_UP:
                    stateData[counter] = 0;
                    stateData[counter + 1] = 1;
                    break;
                case MOVE_RIGHT:
                    stateData[counter] = 1;
                    stateData[counter + 1] = 0;
                    break;
                case MOVE_DOWN:
                    stateData[counter] = 1;
                    stateData[counter + 1] = 1;
                    break;

            }
        }


        // Pacman is close to ghosts
        else{
            int numCloseGhosts = numGhostsCloseToPacman(sortedGhostNum);
            switch(numCloseGhosts - 1){
                case 0:
                    stateData[counter] = 0;
                    stateData[counter + 1] = 0;
                    break;
                case 1:
                    stateData[counter] = 0;
                    stateData[counter + 1] = 1;
                    break;
                case 2:
                    stateData[counter] = 1;
                    stateData[counter + 1] = 0;
                case 3:
                    stateData[counter] = 1;
                    stateData[counter + 1] = 1;
                    break;
            }
        }

        counter++;
        counter++;

        int[] direction;
        for(int i = 0; i < 4; i++){
            direction = getGhostDirection(pacMan.x,pacMan.y,ghosts[sortedGhostNum[i]].x,
                    ghosts[sortedGhostNum[i]].y);
                stateData[counter] = direction[0];
                counter++;
                stateData[counter] = direction[1];
                counter++;
                stateData[counter] = direction[2];
                counter++;

        }

        return stateTable.getState(stateData);

    }


    int isPacmanCloseToGhosts(int[] sortGhosts){
        boolean closeness;
        for(int i = 0; i < sortGhosts.length; i++){
            sortGhosts[i] = i;
        }

        double tempLength = 10000;
        int tempInt = 0;
        int tempPlace = 0;
        int placeHolder = 0;
        for(int i = 0; i < sortGhosts.length; i++){
            tempInt = sortGhosts[i];
            for(int j = i; j < sortGhosts.length; j++){
                if(MathUtils.distance(pacMan.x,pacMan.y,ghosts[sortGhosts[j]].x,
                        ghosts[sortGhosts[j]].y ) < tempLength) {
                    tempInt = sortGhosts[j];
                    tempPlace = j;
                }
            }
            placeHolder = sortGhosts[i];
            sortGhosts[i] = tempInt;
            sortGhosts[tempPlace] = placeHolder;
        }
        if(MathUtils.getRealDistance(pacMan.x,pacMan.y,ghosts[sortGhosts[0]].x,
                ghosts[sortGhosts[0]].y) < PROXIMITY)



            return 1;
        else return 0;
    }


    int numGhostsCloseToPacman(int[] a){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(MathUtils.getRealDistance(pacMan.x,pacMan.y,ghosts[a[i]].x,
                    ghosts[a[i]].y) < PROXIMITY)
                count++;
        }

        return count;
    }



int[] getGhostDirection(double pacX, double pacY, double ghostX, double ghostY) {
    int[] directon = new int[3];

    boolean isAbove = false;
    boolean isBelow = false;
    boolean isForward = false;
    boolean isBehind = false;

    if (ghostY > pacY)
        isAbove = true;
    if (ghostY < pacY)
        isBelow = true;
    if (ghostX > pacX)
        isForward = true;
    if (ghostX < pacX)
        isForward = false;

    if (isAbove && isForward){
        directon[0] = 0;
        directon[1] = 0;
        directon[2] = 0;
        return directon;
    }
    if (isAbove && isBehind) {
        directon[0] = 0;
        directon[1] = 0;
        directon[2] = 1;
        return directon;
    }
    if (isAbove)
    {
        directon[0] = 0;
        directon[1] = 1;
        directon[2] = 0;
        return directon;
    }

    if (isBelow && isForward)
    {
        directon[0] = 0;
        directon[1] = 1;
        directon[2] = 1;
        return directon;
    }
    if (isBelow && isBehind)
    {
        directon[0] = 1;
        directon[1] = 0;
        directon[2] = 0;
        return directon;
    }
    if (isBelow)
    {
        directon[0] = 1;
        directon[1] = 0;
        directon[2] = 1;
        return directon;
    }
    if (isForward)
    {
        directon[0] = 1;
        directon[1] = 1;
        directon[2] = 0;
        return directon;
    }
    if (isBehind)
    {
        directon[0] = 1;
        directon[1] = 1;
        directon[2] = 1;
        return directon;
    }

     directon[0] = 0;
    directon[1] = 0;
    directon[2] = 0;
    return directon;
}

void loseLife(){
        isDead = true;
    System.out.println("UNIQUE STATES " + uniqueStates);
        makeDecision();
}

void alive(){}



}