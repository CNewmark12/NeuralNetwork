public class Neuron extends Input
{
  //Data
  private double activation;
  private double[] activity;
  private double[] weight;
  
  //Object
  public Neuron(int size, int weight_size)
  {
    activity = new double[size]; //holds weighted multiplications from the previous layer
    
    weight = new double[weight_size];
    for(int i = 0; i < weight_size; i++)
      weight[i] = Math.random();
    
    activation = sumActivation();
  }
  //Output Neuron
  public Neuron(int size)
  {
    activity = new double[size];
    
    activation = sumActivation();
  }
  
  //Set Methods
  public void setActivity(int index, double theAct)
  {
    activity[index] = theAct;
  }
  public void setWeight(int index, double theWeight)
  {
    weight[index] = theWeight;
  }
  public void setActivation(double act)
  {
    activation = act;
  }
  //Get Methods
  public double getActivity(int index)
  {return activity[index];}
  
  public double[] getActivityArray()
  {return activity;}
  
  public double getWeight(int index)
  {return weight[index];}
  
  public double getActivation()
  {return activation;}
  
  //This is the sigmoid function
  public static double sigmoid(double a, double range)
  {
    double output = (range)/(1+Math.exp(-1*a));
    return output;
  }
  //This is the derivative of the sigmoid
  public static double ddxSigmoid(double a, double range)
  {
    double output = (range)*Math.exp(a)/((Math.exp(a)+1)*(Math.exp(a)+1));
    return output;
  }
  //This will calculate the activation of the Neuron
  public double sumActivation()
  {
    double act = 0;
    for(int i = 0; i < activity.length; i++){
      act = act + activity[i];
    }
    return act;
  }
}
  
  