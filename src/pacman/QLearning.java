package pacman;

import java.util.Random;

public class QLearning {

    public static final int NUM_VARIABLES = 16;

    private static final double EXPLORATION_PROBABILITY = 0.5;
    private static final double LEARNING_RATE = 0.3;
    private static final double DISCOUNT_FACTOR = 0.8;

    private static final double FOOD_REWARD = 1;
    private static final double LOSE_LIFE = -10;

    private double[] weights;
    private double[] lastActionState;

    private Maze maze;
    private PacMan pacman;
    private Ghost[] ghosts;

    private Random rand = new Random();

    public QLearning(Maze maze, PacMan pacman, Ghost[] ghosts, double[] initialWeights) {
        this.maze = maze;
        this.pacman = pacman;
        this.ghosts = ghosts;
        lastActionState = null;
        if(initialWeights == null) initialWeights = new double[NUM_VARIABLES];
        weights = initialWeights;
    }

    public int getMove() {
        if(rand.nextDouble() > (1 - EXPLORATION_PROBABILITY)) {
            return rand.nextInt(4);
        } else {
            //Q values for each of the directions
            double[] qValues = new double[4];
            double[][] states = new double[4][];
            for (int i = 0; i < 4; i++) {
                states[i] = getState(i);
                qValues[i] = Q(states[i]);
            }
            //Return highest QValue direction
            int action = MathUtils.maxIndex(qValues);
            lastActionState = states[action];
            return action;
        }
    }

    public double Q(double[] state) {
        return state == null ? Double.MIN_VALUE : MathUtils.dotProduct(weights, state);
    }

    /**
     * A state contains:
     * - 1 empty for now
     * - pacman x
     * - pacman y
     * - nearest ghost x
     * - nearest ghost y
     * - nearest ghost isHollow
     * - nearest ghost hollow counter
     * - nearest pellet x
     * - nearest pellet y
     * - nearest pellet isPowerPellet
     * - direction to nearest ghost
     * - direction to nearest pellet
     * - left blocked
     * - right blocked
     * - up blocked
     * - down blocked
     */
    public double[] getState(int direction) {
        //The possible state's x&y coords
        int x = pacman.x,y = pacman.y;
        switch (direction) {
            case MovingObject.MOVE_LEFT: x--; break;
            case MovingObject.MOVE_RIGHT: x++; break;
            case MovingObject.MOVE_UP: y--; break;
            case MovingObject.MOVE_DOWN: y++; break;
        }
        x = Math.max(Math.min(x, MazeData.GRID_SIZE_X), 0);
        y = Math.max(Math.min(y, MazeData.GRID_SIZE_Y), 0);
        double[] state = new double[NUM_VARIABLES];

        //Get Ghost data
        double[] ghostDist = new double[ghosts.length];
        for (int i = 0; i < ghostDist.length; i++) {
            ghostDist[i] = MathUtils.distance(x, y, ghosts[i].x, ghosts[i].y);
        }
        int minGhostDistIndex = MathUtils.minIndex(ghostDist);
        int isHollow = ghosts[minGhostDistIndex].isHollow ? 0 : 1;

        //Get Dot/Pellet data

        //Get Path data
        int leftBlocked = MazeData.getData(Math.max(x-1, 0), y) == MazeData.BLOCK ? 0 : 1;
        int rightBlocked = MazeData.getData(Math.min(x+1, MazeData.GRID_SIZE_X), y) == MazeData.BLOCK ? 0 : 1;
        int upBlocked = MazeData.getData(x, Math.max(y-1, 0)) == MazeData.BLOCK ? 0 : 1;
        int downBlocked = MazeData.getData(x, Math.min(y+1, MazeData.GRID_SIZE_Y)) == MazeData.BLOCK ? 0 : 1;

        //Fill array with gathered data
        state[0] = 1;
        state[1] = pacman.x;
        state[2] = pacman.y;
        state[3] = ghosts[minGhostDistIndex].x;
        state[4] = ghosts[minGhostDistIndex].y;
        state[5] = isHollow;
        state[6] = ghosts[minGhostDistIndex].hollowCounter;
        //nearest pellet x
        //nearest pellet y
        //nearest pellet isPower
        //direction to nearest ghost
        //direction to nearest pellet
        state[12] = leftBlocked;
        state[13] = rightBlocked;
        state[14] = upBlocked;
        state[15] = downBlocked;

        return state;
    }

    public void eatFood(){
        double[] qValues = new double[4];
        for (int i = 0; i < 4; i++) {
            qValues[i] = Q(getState(i));
        }
        reward(FOOD_REWARD, qValues[MathUtils.maxIndex(qValues)]);
    }

    public void loseLife() {
        reward(LOSE_LIFE , 0);
    }

    public void reward(double reward, double maxNextState) {
        double correction = (reward + DISCOUNT_FACTOR * maxNextState);
        double[] correctedWeights = MathUtils.subtract(weights, correction);
        double[] update = MathUtils.multiply(correctedWeights, LEARNING_RATE);
        if(lastActionState != null) {
            update = MathUtils.multiply(update, lastActionState);
        }
        if(Maze.DEBUG) {
            System.out.print("Rewarding " + reward + ". Updating weights by: ");
            printArray(update);
        }
        weights = MathUtils.add(weights, update);
    }

    public void printArray(double[] a) {
        System.out.print("[");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
            if(i < a.length-1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
