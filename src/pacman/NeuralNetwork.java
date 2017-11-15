package pacman;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class NeuralNetwork {
private Neuron base;
private int depth;
private int historySize;
ArrayList<Neuron> history;

 NeuralNetwork(int depth, int historySize)
{
    base = new Neuron(null);
    this.depth = depth;
    this.historySize = historySize;
    this.history = new ArrayList<Neuron>();
}

 Neuron getData(int[] arr)
{
    Neuron current = base;
    for(int i = 0; i < depth; i++)
    {
        if (arr[i] == 0)
            current = base.getChild0();
        else if (arr[i] == 1)
            current = base.getChild1();
        else
            throw new InvalidParameterException("Input can only contain ones and zeros");
    }
    return current;
}

void chooseState(Neuron neuron)
{
    history.add(neuron);
}


// For Jade, find a way to store and retrieve the neural network from a notepad file as efficiently as possible.
// It would be best if only the bottom elements were stored in the notepad file, but I will leave that up to you. 
 void storeCurrentTree()
{

}


NeuralNetwork getTree(String fileName)
{

}

