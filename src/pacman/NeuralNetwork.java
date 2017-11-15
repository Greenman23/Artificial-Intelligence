package pacman;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class NeuralNetwork {
private Neuron base;
private int depth;
private int historySize;
ArrayList<EndNeuron> history;

NeuralNetwork(int depth, int historySize)
{
    base = new Neuron(null);
    this.depth = depth;
    this.historySize = historySize;
    this.history = new ArrayList<EndNeuron>();
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
            throw new InvalidParameterException("Input can only contain ones and 0s");
    }
    return current; 
}

void chooseState(EndNeuron endNeuron)
{
    history.add(endNeuron);
}

}
