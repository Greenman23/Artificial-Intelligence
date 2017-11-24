package pacman;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

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
    void storeCurrentTree()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("History.txt"))) {
            bw.write(depth + " " + historySize);
            bw.newLine();
            String currentValue = "";
            for (int i = 0; i > tree.length; i++){
                currentValue = String.valueOf(tree[i]);
                bw.write(currentValue);
                bw.newLine();
            }
            bw.close();
        } catch(IOException e){
            e.printStackTrace();
        }

    }


    NeuralNetwork getTree(String fileName) {
        File file = new File(fileName);
        try (Scanner scan = new Scanner(file)){
            int count = 0;
            if (scan.hasNextInt())
                depth = scan.nextInt();
            else depth = 7;         //Dummy values
            tree = new double[((int)Math.pow(2, depth))]; //Create our tree
            if(scan.hasNextInt())
                historySize = scan.nextInt();
            else historySize = 14;  //Dummy values

            while (scan.hasNextDouble() && count < tree.length){
               tree[count] = scan.nextDouble();
               count++;
            }
            scan.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File " + fileName + "not found");
        }

        //NeuralNetwork x = new NeuralNetwork();
        return new NeuralNetwork(2, 2);

    }



}