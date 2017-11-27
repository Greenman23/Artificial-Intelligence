package pacman;

import java.io.*;
import java.util.Scanner;

public class StateTable {
    private int depth;
    private State[] stateTable;
    private int historySize;

    StateTable(int depth, int historySize){
        this.depth = depth;
        stateTable = new State[(int)Math.pow(2,depth)];
        for(int i = 0; i < stateTable.length; i++){
            stateTable[i] = new State();
        }
        this.historySize = historySize;
    }

    void storeHistoryFile(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("History.txt"))) {
            bw.write(depth + " " + historySize);
            bw.newLine();
            String currentValue = "";
            for (int i = 0; i < stateTable.length; i++) {
                for (int j = 0; j < 4; j++) {
                    currentValue = String.valueOf(stateTable[i].getActionWeight(j));
                    bw.write(currentValue);
                    bw.newLine();
                }
            }
            bw.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    State[] retrieveHistoryFile(String fileName){
            File file = new File(fileName);
            try (Scanner scan = new Scanner(file)){
                int count = 0;
                if (scan.hasNextInt())
                    depth = scan.nextInt();
                else depth = 7;         //Dummy values
                stateTable = new State[((int)Math.pow(2, depth))]; //Create our tree
                if(scan.hasNextInt())
                    historySize = scan.nextInt();
                else historySize = 14;  //Dummy values

                while (scan.hasNextDouble() && count < stateTable.length){
                    for(int i = 0; i < 4; i++) {
                        stateTable[count].setActionWeights(i, scan.nextDouble());
                    }
                    count++;
                }
                scan.close();
            }
            catch (FileNotFoundException e){
                System.out.println("File " + fileName + "not found");
            }

            return stateTable;
            //NeuralNetwork x = new NeuralNetwork();
        }


    State getState(int[] stateCode){
        return stateTable[MathUtils.convertToDecimal(stateCode)];
    }
}
