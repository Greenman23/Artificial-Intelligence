package pacman;

import java.util.Random;

public class QLearning {

    public static final int NUM_VARIABLES = 16;

    private static final double EXPLORATION_PROBABILITY = 0.05;
    private static final double LEARNING_RATE = 0.3;
    private static final double DISCOUNT_FACTOR = 0.8;

    private static final double FOOD_REWARD = 1;
    private static final double WON_REWARD = 10;
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
            //return highest q value direction
            return pacman.currentDirection.get();
        }
    }

    public double Q(double[] state) {
        return state == null ? Double.MIN_VALUE : MathUtils.dotProduct(weights, state);
    }

    /**
     * A state contains:
     * - pacman xDir
     * - pacman yDir
     * - nearest ghost xDir
     * - nearest ghost yDir
     * - nearest ghost isHollow
     * - nearest pellet x
     * - nearest pellet y
     * - nearest pellet isPowerPellet
     * - direction to nearest ghost x
     * - direction to nearest ghost y
     * - direction to nearest pellet x
     * - direction to nearest pellet y
     * - left blocked
     * - right blocked
     * - up blocked
     * - down blocked
     */
    public double[] getState() {
        double[] state = new double[NUM_VARIABLES];

        return state;
    }

    public void eatFoot(){

    }

    public void loseLife() {

    }

    public void reward(double reward, double maxNextState) {
        double correction = (reward + DISCOUNT_FACTOR * maxNextState);
        double[] correctedWeights = MathUtils.subtract(weights, correction);
        double[] update = MathUtils.multiply(correctedWeights, LEARNING_RATE);
        if(lastActionState != null) {
            update = MathUtils.multiply(update, lastActionState);
        }
        if(Maze.DEBUG)
            System.out.println("Rewarding " + reward + ". Updating weights by: " + update);
        weights = MathUtils.add(weights, update);
    }
}
