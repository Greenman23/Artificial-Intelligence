package pacman;
/*
The pacman.Neuron Class is used in the pacman.NeuralNetwork class as individual components of a binary tree.
Each pacman.Neuron has two children and a parent.
pacman.EndNeuron extends pacman.Neuron and has a weight value in addition to the other values. Only endpoints
on the neural network have weights.
 */

public class Neuron {
    protected Neuron parent;
    protected Neuron child0;
    protected Neuron child1;
    protected double weight;

    Neuron(Neuron parent)
    {
        this.parent = parent;
        child0 = null;
        child1 = null;
    }

    Neuron getChild0() {
        if(child0 == null)
            child0 = new Neuron(this);
        return child0;
    }

    Neuron getChild1(){
        if(child1 == null)
            child1 = new Neuron(this);
        return child1;
    }


    boolean checkFinalChild0()
    {
        if(this.child0 == null)
            return false;
        else
            return true;
    }

    boolean checkFinalChild1()
    {
        if(this.child1 == null)
            return false;
        else
            return true;
    }

    double getWeight()
    {
        return weight;
    }

    void setWeight(double weight)
    {
     this.weight = weight;
    }
}
