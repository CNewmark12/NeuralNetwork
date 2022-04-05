import java.util.*;
import java.io.*;

public class Input
{
  //Data
  private double input;
  private double weight;
  private double[] weightArray;
  private double[] array;
  
  //Objects
  public Input()
  {
    input = 0;
  }
  public Input(double theInput)
  {
    input = theInput;
    weight = 0;
  }
  
  public Input(double theInput, int size, int arraySlot)
  {
    array = new double[size];
    array[arraySlot] = theInput;
    weight = 0;
    
  }
  
  public Input(double theInput, int size, int arraySlot, double theWeight )
  {
    double[] array = new double[size];
    array[arraySlot] = theInput;
    weight = theWeight;
  }
  
  //Sets the weight initially to random values
  //Sets the starting inputs of the array to zeroes
  public Input( int size, int size_weight)
  {
    array = new double[size];
    for(int i = 0; i < size; i++)
      array[i] = 0;
    weightArray = new double[size_weight];
    for(int i = 0; i < size_weight; i++)
      weightArray[i] = Math.random();
  }
  //Set Methods
  public void setInput(double theInput)
  {
    input = theInput;
  }
  public void setWeight(double theWeight)
  {
    weight = theWeight;
  }
  //SetRandomWeight
  public void setRandWeight()
  {
    weight = Math.random();
  }
  public void setArray(int index, double value)
  {
    array[index] = value;
  }
  public void setWeight(int index, double value)
  {
    weightArray[index] = value;
  }
  
  
  //Get Methods
  public double getInput()
  {return input;}
  public double getWeight()
  {return weight;}
  public double getInputAt(int index)
  {return array[index];}
  public int getArrayLength()
  {return array.length;}
  public double getWeightAt(int index)
  {return weightArray[index];}
  public double[] getWeightArray()
  {return weightArray;}
  
  //Will print an entire inputs data contents
  public void printInput()
  {
    int count = 0;
    while(count < array.length){
      System.out.println(array[count]);
      count++;
    }
  }
  
  //This method will load a particular file(of doubles) into an input object
  public void loadInput(String filename)
  {
    try{
    Scanner scan = new Scanner(new File(filename));
    
    for(int i = 0; i < array.length && scan.hasNextDouble(); i++)
      array[i] = scan.nextDouble();
    
    }
    catch(IOException ioe)
    {
      System.err.println("Could not open the file named: " + filename);
      System.exit(0);
    }
  }
  //This method will calculate the connections matrix of the input
  public double[] connectMatrix(int index)
  {
    double[] matrix = new double[weightArray.length];
    for(int i = 0; i < weightArray.length; i++)
    matrix[i] = array[index]*weightArray[i];
    
    return matrix;
  }
  
}