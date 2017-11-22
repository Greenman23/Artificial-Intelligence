package pacman;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class NeuralNetwork {
    private int depth;
    private int historySize;
    ArrayList<int[]> history;
    double tree[];

    NeuralNetwork(int depth, int historySize) {
        this.depth = depth;
        this.historySize = historySize;
        this.history = new ArrayList<int[]>();
        int tree_size = (int)Math.pow(2,depth);
        tree = new double[tree_size];
    }

    double getData(int[] arr) {
//        Neuron current = base;
//        for (int i = 0; i < depth; i++) {
//            if (arr[i] == 0)
//                current = base.getChild0();
//            else if (arr[i] == 1)
//                current = base.getChild1();
//            else
//                throw new InvalidParameterException("Input can only contain ones and zeros");
//        }
//        return current;

        double value = tree[MathUtils.convertToDecimal(arr)];
        return value;


    }

    void addState(int[] a) {
        history.add(a);
    }


    // For Jade, find a way to store and retrieve the neural network from a notepad file as efficiently as possible.
// It would be best if only the bottom elements were stored in the notepad file, but I will leave that up to you. 
    void storeCurrentTree() {

    }


    NeuralNetwork getTree(String fileName) {
    return new NeuralNetwork(2,2);
    }

}