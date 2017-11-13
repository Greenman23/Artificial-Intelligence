package pacman;
/*
The pacman.Neuron Class is used in the pacman.NeuralNetwork class as individual components of a binary tree.
Each pacman.Neuron has two children and a parent.
pacman.EndNeuron extends pacman.Neuron and has a weight value in addition to the other values. Only endpoints
on the neural network have weights.
 */

public class Neuron {
    private Neuron parent;
    private Neuron child0;
    private Neuron child1;

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

    Neuron getParent(){
        return parent;
    }
}


class EndNeuron extends Neuron
{
    private double weight ;
    EndNeuron(Neuron parent, double weight)
    {
    super(parent);
    this.weight = weight;
    }

    double getWeight()
    {
        return weight;
    }

    void setWeight(double weight)
    {
     this.weight = weight;
    }


    Neuron getChild0()
    {
        return null;
    }

    Neuron getChild1()
    {
        return null;
    }

}
