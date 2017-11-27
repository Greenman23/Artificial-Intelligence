package pacman;

public class State {
    private double[] actionWeights;

    void setActionWeights(int actionNo, double weight){
        this.actionWeights[actionNo] = weight;
    }

    double getActionWeight(int actionNo){
        return actionWeights[actionNo];
    }

    State(){
        actionWeights = new double[4];
        for(int i = 0; i < actionWeights.length; i++){
            actionWeights[i] = 0;
        }
    }
}
