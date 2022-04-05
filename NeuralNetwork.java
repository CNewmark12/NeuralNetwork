//This class contains an object which when initialized, will ask for user inputs to create a Neural Network

import java.util.*;
import java.io.*;
public class NeuralNetwork
{
  //These are our Network data arrays
  private Input[] inputs; //an array in input objects
  private Neuron[][] layers; // an array of arrays of Neuron objects 
  private int[] holder; //place holder values
  
  
  //This is our general constructor for creating a network
  public NeuralNetwork(){
  
     Scanner scan = new Scanner(System.in);
    
    //Prompt user to enter the Neural Network Structure
    
      System.out.println("Enter the number of inputs you would like: ");
      inputs = new Input[scan.nextInt()];//Create input layer vector
      
      System.out.println("Enter the number of hidden layers you would like(including the output layer): ");
      layers = new Neuron[scan.nextInt()][];//Create Neuron vector that holds Neuron arrays
      
      //This will hold onto the values that we want in each layer
      holder = new int[layers.length];
      for(int i = 0; i < layers.length; i++){
        System.out.println("Please enter the number of Neurons you would like in layer " + (i+1));
        holder[i] = scan.nextInt();
        layers[i] = new Neuron[holder[i]];//New Neuron array at layers[l] of size holder[l]
      }
      //This will determine the size of the input arrays
      System.out.println("Please enter the number of objects in the input files.");
      int inputSize = scan.nextInt();
      
      object_initialization(inputSize);   
  }//End Constructor
  
  //This network object will read in a file and create a network with the structure in the file
  //File format
  //Inputs = a Layers = b Neurons in Layer 1 ...Neurons in Layer b inputSize = i
  public NeuralNetwork(String file){  
    object_initialization(load_Network(file));
  }//End Constructor
  
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  //This function will run a preloaded NN object n number of times, starting at data point x
  //and return an array of results, given 1 output
  //show_layer_activations will show each Neuron's activation propogate through if equal to 1
  //Sigmoid if activation == 1
  
  public double[] single_output_netRun(int n, int startingPoint, int show_layer_activations, int activation_type)
  {
    double[] output = new double[n+1];
    int counter = 0;
    
    while(counter <= n)
    {
      if(activation_type == 1){
      net_run_sigmoid(show_layer_activations, counter+startingPoint);
     }//sigmoid Activation if activation_type == 1
      else{
        net_run_linear(show_layer_activations, counter+startingPoint);
      }//non activation function case
      
      output[counter] = single_output();
      counter++;
     }
    return output;
  }//End single_output_netrun
  
  //This will train the weights of a network to training set target[]
  //activation function is sigmoid if activation_type == 1

  public void single_output_netTraining(String filename, double learningParam, int training_set, double error_param, int desired_epochs, int activation_type)
  {
    int temp = inputs[0].getArrayLength();//Each input Neuron has the same length as the target array
    int n = training_set;
    int epoch = 0;
    int counter = 0;
    
    double[] error_array = new double[n];
    double[] target = dataLoad_returnArray(filename, temp);
    double total_error = 0;
    
    do{
    while(counter<n){
  
  if(activation_type == 1){
    net_run_sigmoid(0,counter);
    total_error = target[counter] - sumActivation(layers[layers.length-1][0].getActivityArray());//calculate the error
    error_array[counter] = Math.abs((0.5)*total_error);
    double[][]error = hidden_error(total_error);//Adjust the weights from the input layer
    weight_training_sigmoid(error,total_error,learningParam,counter);
  }//sigmoid
  
  else{
    net_run_linear(0,counter);
    total_error = target[counter] - single_output();//calculate the error
    error_array[counter] = Math.abs((0.5)*total_error);
    double[][]error = hidden_error_nonSigmoid(total_error);//Adjust the weights from the input layer
    weight_training_linear(error,total_error,learningParam,counter);
  }//linear
  
       counter++;
    }
     epoch++;
     counter = 0;//reset counter
     
     if(epoch % 100 == 0)
     System.out.println("Epoch: " + epoch + " Total Error: " + sumOfSquaredErrors(error_array));
  }
    while(sumOfSquaredErrors(error_array) > error_param && epoch < desired_epochs);
}//End single_output_netTraining
  
  //This method will use the single_output_netRun method to compare its results to the
  //desired outputs
  public void single_output_netTesting(double[] outputArray, String target, int training_Set){
    int temp = inputs[0].getArrayLength();
    double[] targetArray = dataLoad_returnArray(target,temp);
    for(int i = training_Set; i < targetArray.length; i++){
      System.out.println("Test number " + (i-training_Set + 1) + ": ");
      System.out.println("Desired: " + targetArray[i]);
      System.out.println("Predicted: " + outputArray[i-training_Set]);
      System.out.println("Error: " + (targetArray[i]-outputArray[i-training_Set]));
      System.out.println("");
    }
  }
  
  //This method will take in a file of input files to create a working 
  //network that will make predictions from its inputs
  public void single_output_netPredictions(String file, int activationType){
    loadInputs(file);
    int startingPoint = 0;
    double[] output = single_output_netRun(inputs[0].getArrayLength(),startingPoint,0,activationType);
    for(int i = 0; i < output.length; i++){
       System.out.println("Prediction " + i + ": " + output[i]);
    }
  }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //TEN FOLD METHODS
  
  //This method will take aside about 10% of the data randomly
  //for testing and train on the other 90%
  //returns our left out data array indexes
  public int[] ten_fold_training(String filename, double learningParam, double error_param, int desired_epochs, int activation_type){
    int temp = inputs[0].getArrayLength();//Each input Neuron has the same length as the target array
    int n = temp;
    int epoch = 0;
    int counter = 0;
    
    double[] error_array = new double[n];
    double[] target = dataLoad_returnArray(filename, temp);
    double total_error = 0;
    
    //Skip the data at our check indexes
    int[] check = dataSplit((int)(temp/10));//10% of our data
    
    //go from 0 - length of data skipping indexes in our check array
    do{
    while(counter<n){
  //train only if counter is not in check
      //checkArray returns true if counter is in check
      if(!checkArray(counter, check)){
       if(activation_type == 1){
         net_run_sigmoid(0,counter);
         total_error = target[counter] - sumActivation(layers[layers.length-1][0].getActivityArray());//calculate the error
         error_array[counter] = Math.abs((0.5)*total_error);
         double[][]error = hidden_error(total_error);//Adjust the weights from the input layer
         weight_training_sigmoid(error,total_error,learningParam,counter);
       }//sigmoid
  
       else{
         net_run_linear(0,counter);
         total_error = target[counter] - single_output();//calculate the error
         error_array[counter] = Math.abs((0.5)*total_error);
         double[][]error = hidden_error_nonSigmoid(total_error);//Adjust the weights from the input layer
         weight_training_linear(error,total_error,learningParam,counter);
       }//linear
      }//train only if counter is not in check
  
       counter++;
    }
     epoch++;
     counter = 0;//reset counter
     
     if(epoch % 100 == 0)
     System.out.println("Epoch: " + epoch + " Total Error: " + sumOfSquaredErrors(error_array));
  }
    while(sumOfSquaredErrors(error_array) > error_param && epoch < desired_epochs);
    
    return check;
  }
  
  //This method will be used in conjunction with our ten fold cross validation
  //function. We will make the testing predictions on the indexes of the array
  //returned by the ten_fold_training method
  public void ten_fold_testing(int[] indexes, String filename, int activation_type, int show_layer_activations){
    double[] output = new double[indexes.length];
    int counter = 0;
    
    double[] target = dataLoad_returnArray(filename,inputs[0].getArrayLength());
    
    while(counter < indexes.length)
    {
      if(activation_type == 1){
      net_run_sigmoid(show_layer_activations, indexes[counter]);
     }//sigmoid Activation if activation_type == 1
      else{
        net_run_linear(show_layer_activations, indexes[counter]);
      }//non activation function case
      
      output[counter] = single_output();
      counter++;
     }
    for(int i = 0; i < output.length; i++){
       System.out.println("");
       System.out.println("Prediction " + (i+1) + ": " + output[i]);
       System.out.println("Target " + indexes[i] + ": " + target[indexes[i]]);
       System.out.println("Error: " + (output[i] - target[indexes[i]]));
    }
  }
  
  
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //Here are our functions that we use inside of the network
  
  //Shows input at index
  public double getInput(int index){
  return inputs[index].getInput();}
  
  //Returns the number of input objects in the input layer
  public int numberOfInputs()
  {return inputs.length;}
  
  //Returns the number of layers
  public int layer_numbers()
  {return layers.length;}
  
  //Returns the structure of the network
  public void net_structure()
  {
    System.out.print(this.numberOfInputs() + " - ");
    
    int count = 0;
    int counter = 0;
    while(count < layers.length){
      counter = 0;
      for(int i = 0; i < layers[count].length; i++){
        if(layers[count][i] != null){
          counter++;
      }
    }//end for
      if(count < layers.length-1)
        System.out.print(counter + " - ");
      if(count == layers.length-1)
        System.out.println(counter);
      count++;
   }//end while
  }
  
  //Loads in data from a file into the input at that input Neuron
  public void dataLoad(String file, int at_Neuron)
  {inputs[at_Neuron].loadInput(file);}
  
  //Loads data into an array and returns the array
  //size is the number of points in the file
  public static double[] dataLoad_returnArray(String file, int size)
  {
    double data[] = new double[size];
    try{
    Scanner scan = new Scanner(new File(file));
    for(int i = 0; i < size; i++){
      data[i] = scan.nextDouble();
      }
    }
    catch(IOException ioe)
    {
      System.err.println("Could not open the file named: " + file);
      System.exit(0);
    }
    return data;
  }
  
  //Prints the contents of a data object at input location index
  public void printData(int index)
  {inputs[index].printInput();}
  
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
  
  //this method calculates each units contribution to the total error of the system
  public double[][] hidden_error(double network_error){
    double[][] error = new double[layers.length][];
    double sum_errors;
    //copy the structure of the layers array
    for(int a = 0; a < layers.length; a++){
        error[a] = new double[layers[a].length];
    }
    
    //calculate each layer's error
    for(int j = layers.length-2; j >= 0; j--){
      for(int k = 0; k < layers[j].length; k++){
        if(j == layers.length-2){
          error[j][k] = ddxSigmoid(sumActivation(layers[j][k].getActivityArray()),1)*network_error*layers[j][k].getWeight(0);
        }//this is for the layer that connects to the output layer, each Neuron has only one connection weight
        
        else{
          sum_errors = 0;
          for(int m = 0; m < layers[j+1].length; m++){
            sum_errors += error[j+1][m]*layers[j][k].getWeight(m);
          }
          error[j][k] = ddxSigmoid(sumActivation(layers[j][k].getActivityArray()),1)*sum_errors;
        }
      }
      
    }//this is for the hidden layer errors
    return error;
  }//End hidden_error
  
  //this function calculates the error of the network on a linear activation function network
  public double[][] hidden_error_nonSigmoid(double network_error){
    double[][] error = new double[layers.length][];
    //copy the structure of the layers array
    for(int a = 0; a < layers.length; a++){
        error[a] = new double[layers[a].length];
    }
    
    //calculate each layer unit's error
    
    for(int j = layers.length-2; j >= 0; j--){
      double sum_errors = 0;
      for(int k = 0; k < layers[j].length; k++){
        if(j == layers.length-2){
          error[j][k] = network_error*layers[j][k].getWeight(0);
        }//this is for the layer that connects to the output layer, each Neuron has only one connection weight
        
        else{
          sum_errors = 0;
          for(int m = 0; m < layers[j+1].length; m++){
            sum_errors += error[j+1][m]*layers[j][k].getWeight(m);
          }
          error[j][k] = sum_errors;
        }
      }
    }//this is for the hidden layer errors
    return error;
  }//End hidden_error_nonSigmoid
  
    public void object_initialization(int inputSize){
    //This will initialize each layer  
    int counter = 0;
    while(counter < layers.length)
    {
      //Set the number of neurons in each layer
      
      //Do this the first time only, initialize inputs
      if(counter == 0){
        for(int j = 0; j < inputs.length; j++){
        inputs[j] = new Input(inputSize, holder[0]);//holder[0] has the number of neurons in the first hidden layer
        }               //Input(length of array slots for holding data, # of connections to 1st hidden layer)
        if(holder.length != 1){
        for(int k = 0; k < holder[0]; k++){
        layers[0][k] = new Neuron(inputs.length,holder[counter+1]);//Initialize input to 1st hidden layer
        }                  //Neuron(activity array length, # of connections to next layer)
       }
        else{
          for(int x = 0; x < holder[0]; x++){
        layers[0][x] = new Neuron(inputs.length);//Initialize input to 1st hidden layer
          }
        }//Special case for input to output layer directly
      }
      
      else if(counter < holder.length-1){
         for(int l = 1; l < holder[counter]+1; l++){
         layers[counter][l-1] = new Neuron(holder[counter-1], holder[counter+1]);
         }
      }
      else{
         for(int m = 0; m < holder[counter]; m++){
         layers[counter][m] = new Neuron(holder[counter-1]);//Output Neuron Layer
      }
     }
    counter++;
   }
  }//end object initialization
  
  //This method will show us the weight connections
  public void show_weights(){
    //show Input Weights
    for(int i = 0; i < inputs.length; i++){
      System.out.println("Input " + (i+1) +": ");
      for(int j = 0; j < layers[0].length; j++){
        System.out.println("Weight " +(j+1)+" has value: " +inputs[i].getWeightAt(j)+" ");
      }
      System.out.println("");
    }
    //show all other weights
    for(int i = 1; i < layers.length; i++){
      System.out.println("Layer " + (i+1) +": ");
      for(int j = 0; j < layers[i-1].length; j++){
        System.out.println("Neuron " +(j+1)+ ": ");
        for(int k = 0; k < layers[i].length; k++){
           System.out.println("Weight " +(k+1)+" has value: " +layers[i-1][j].getWeight(k)+" ");
        }
      }
      System.out.println("");
    }
  }//End show_weights
  
  //This will calculate the activation of the Neuron
  public double sumActivation(double[] theActivity)
  {
    double act = 0;
    for(int i = 0; i < theActivity.length; i++){
      act = act + theActivity[i];
    }
    return act;
  }
  
  //This calculates the final output of a network from the one output Neuron
  public double single_output(){
    double output = sumActivation(layers[layers.length-1][0].getActivityArray());
    return output;
  }
  
  //This will calculate the sum of squared errors
  public double sumOfSquaredErrors(double[] theErrorArray)
  {
    double sum = 0;
    for(int i = 0; i < theErrorArray.length; i++){
      sum = sum + theErrorArray[i]*theErrorArray[i];
    }
    return sum;
  }
  
  public void net_run_sigmoid(int show_layer_activations, int counter){
  //For input to neuron
      for(int i = 0; i < inputs.length; i++){
        for(int j = 0; j < layers[0].length; j++){
          layers[0][j].setActivity(i,(inputs[i].getInputAt(counter))*(inputs[i].getWeightAt(j)));
          if(show_layer_activations == 1){
            System.out.println("Layer: 1");
            System.out.println("Neuron: " +(j+1)+ " Activation: " +sumActivation(layers[0][j].getActivityArray()));
          }
        }//i goes through each input
      }//j goes through each weight
      
      //For the rest of the layers
      for(int k = 1; k < layers.length; k++)
      {
        for(int a = 0; a < layers[k-1].length; a++){
          for(int b = 0; b < layers[k].length; b++){
        layers[k][b].setActivity(a,layers[k-1][a].getWeight(b)*sigmoid(sumActivation(layers[k-1][a].getActivityArray()), 1.0));
  
        if(show_layer_activations == 1){
          System.out.println("Layer: " +(k+1));
          System.out.print("Neuron: " +(a+1)+ " Activation: " +sumActivation(layers[k-1][a].getActivityArray()));
          System.out.println("");
        }
          }//b goes through each weight
        }//a goes through each Neuron in that layer
      }//k goes through each layer
   }
  
  public void net_run_linear(int show_layer_activations, int counter){
  //For input to neuron
      for(int i = 0; i < inputs.length; i++){
        for(int j = 0; j < layers[0].length; j++){
          layers[0][j].setActivity(i,(inputs[i].getInputAt(counter))*(inputs[i].getWeightAt(j)));
          //System.out.println(layers[0][j].getActivation());
          if(show_layer_activations == 1){
            System.out.println("Layer: 1");
            System.out.println("Neuron: " +(j+1)+ " Activation: " +sumActivation(layers[0][j].getActivityArray()));
          }
        }//i goes through each input
      }//j goes through each weight
      
      //For the rest of the layers
      for(int k = 1; k < layers.length; k++)
      {
        for(int a = 0; a < layers[k-1].length; a++){
          for(int b = 0; b < layers[k].length; b++){
        layers[k][b].setActivity(a,layers[k-1][a].getWeight(b)*sumActivation(layers[k-1][a].getActivityArray()));
  
        if(show_layer_activations == 1){
          System.out.println("Layer: " +(k+1));
          System.out.print("Neuron: " +(a+1)+ " Activation: " +sumActivation(layers[k-1][a].getActivityArray()));
          System.out.println("");
        }
          }//b goes through each weight
        }//a goes through each Neuron in that layer
       }//k goes through each layer
   }
  
//Trains the weights of a network which is using a sigmoid activation function
public void weight_training_sigmoid(double[][] error, double total_error,double learningParam, int counter){
  for(int m = 0; m < inputs.length; m++){
        for(int l = 0; l < layers[0].length; l++){
          inputs[m].setWeight(l,inputs[m].getWeightAt(l)+learningParam*error[0][l]*inputs[m].getInputAt(counter));
        }
      }//input-to-first hidden layer connections
      
      for(int e = 0; e < layers.length-1; e++){
        for(int f = 0; f < layers[e].length; f++){
          for(int g = 0; g < layers[e+1].length; g++){
          if(e < layers.length-2 && layers.length != 2){
            layers[e][f].setWeight(g,layers[e][f].getWeight(g)+learningParam*error[e+1][g]*sigmoid(sumActivation(layers[e][f].getActivityArray()),1));
          }//all connected hidden layers not connected to output
           else{
            layers[e][f].setWeight(0,layers[e][f].getWeight(0)+learningParam*total_error*sigmoid(sumActivation(layers[e][f].getActivityArray()),1));
               }//connections between last hidden layer and output
          }
       }
    }
  }
//Trains the weights of a network which is using a linear activation function
public void weight_training_linear(double[][] error, double total_error,double learningParam, int counter){
  for(int m = 0; m < inputs.length; m++){
        for(int l = 0; l < layers[0].length; l++){
          inputs[m].setWeight(l,inputs[m].getWeightAt(l)+learningParam*error[0][l]*inputs[m].getInputAt(counter));
        }
      }//input-to-first hidden layer connections
      
      for(int e = 0; e < layers.length-1; e++){
        for(int f = 0; f < layers[e].length; f++){
          for(int g = 0; g < layers[e+1].length; g++){
          if(e < layers.length-2 && layers.length != 2){
            layers[e][f].setWeight(g,layers[e][f].getWeight(g)+learningParam*error[e+1][g]);
          }//all connected hidden layers not connected to output
          else{
            layers[e][f].setWeight(0,layers[e][f].getWeight(0)+learningParam*total_error*sumActivation(layers[e][f].getActivityArray()));
          }//connections between last hidden layer and output
         }
       }
     }
  }

//This method will write the configured weights to a txt file in the Neural Nets folder on my computer
public void weightExportTo(String file){
  try{
    
      FileWriter write = new FileWriter(file, false);
      PrintWriter print_line = new PrintWriter(write);
      
      for(int i = 0; i < layers.length; i++){
        if(i == 0){
          for(int j = 0; j < inputs.length; j++){
              print_line.printf("Input " + "\"%d\"\n",(j+1));//In the file, tells us which input has which weights
              print_line.println("");
            for(int k = 0; k < inputs[j].getWeightArray().length; k++){
                print_line.print(inputs[j].getWeightAt(k) +" ");//prints each inputs weight with a space in between
            }//for each weight at input j
            print_line.println("");
        }//for each input
          print_line.println("");
      }//for our inputs
        else{
          print_line.printf("Layer "+ "\"%d\"\n",(i+1));//Write us the layer
          print_line.println("");
          for(int j = 0; j < layers[i-1].length; j++){
            print_line.printf("Neuron "+ "\"%d\"\n",(j+1));//Write us the Neuron number
            print_line.println("");
            for(int k = 0; k < layers[i].length; k++){
              print_line.print(layers[i-1][j].getWeight(k) +" ");
            }//for each weight of that Neuron
            print_line.println("");
          }//for each Neuron
          print_line.println("");
        }//for rest of layers
    }
      print_line.close();
  }
    catch(IOException e){
      System.out.println("Problem...");
    }
}

//This method will import a set of weights to a pre-specified network
public void weightImportFrom(String file){
  try{
  Scanner scan = new Scanner(new File(file));
    double data[] = new double[number_of_weights()];
    int counter = 0;
    while(scan.hasNext()){
      while(scan.hasNextDouble()){
        data[counter] = scan.nextDouble();
        counter++;
      }//if theres a double, load it into the data array at index counter
      if(scan.hasNext())
      scan.next();
    }//goes through file
  counter = 0;
  
  for(int i = 0; i < layers.length; i++){
        if(i == 0){
          for(int j = 0; j < inputs.length; j++){
            for(int k = 0; k < inputs[j].getWeightArray().length; k++){
                inputs[j].setWeight(k,data[counter]);
                counter++;
            }//for each weight at input j
        }//for each input
      }//for our inputs
        else{
          for(int j = 0; j < layers[i-1].length; j++){
            for(int k = 0; k < layers[i].length; k++){
              layers[i-1][j].setWeight(k,data[counter]);
              counter++;
            }//for each weight of that Neuron
          }//for each Neuron
        }//for rest of layers
       }
  }
  catch(IOException e){
    System.out.println("uh oh");
  }
}

//This method will return an integer which is the number of weights in the network
public int number_of_weights(){
  int counter = 0;
  
  counter = inputs.length*layers[0].length;//for the weights from the input to first layer
  
  for(int i = 0; i < layers.length-1; i++){
    counter += layers[i].length*layers[i+1].length;//the number of weights at layer i 
  }//each layer
  
  return counter;
}

//This method is used in conjunction with a NN object to create a specified NN structure without
//the typing
public int load_Network(String file){
  int inputSize = 0;
  try{
    Scanner scan = new Scanner(new File(file));
    int counter = 0;
    int inner_counter = 0;
    
    
    while(scan.hasNext()){
    if(scan.hasNextInt()){
       switch (counter){
         case 0: inputs = new Input[scan.nextInt()];
                 counter++;//go to next case next time
                 break;
         case 1: layers = new Neuron[scan.nextInt()][];
                 counter++;
                 holder = new int[layers.length];
                 break;
         case 2: while(scan.hasNext() && inner_counter < layers.length){
                 if(scan.hasNextInt()){
                    holder[inner_counter] = scan.nextInt();
                    inner_counter++;
                  }
                 scan.next();
                 }
                 counter++;
                 break;
         case 3: for(int i = 0; i < layers.length; i++){
                   layers[i] = new Neuron[holder[i]];
                 }
                 inputSize = scan.nextInt();
                 break;
       }//end switch
      }
    if(scan.hasNext())
      scan.next();
     }//traverse file
  }
  catch(IOException e){
      System.out.println("File not found.");
    }
  return inputSize;
}

//This method reads in a txt file with a list of txt file names and returns them in a string array
//of the size of the inputs
public String[] read_in_files(String file){
  String[] fileNames = new String[inputs.length];
  try{
    Scanner scan = new Scanner(new File(file));
    for(int i = 0; i < inputs.length; i++){
      fileNames[i] = scan.next();
    }
  }
  catch(IOException e){
    System.out.println("File not found.");
  }
 return fileNames;
}

//This method will load data points to the inputs from the files returned
//by the read_in_files method
public void loadInputs(String file){
  String[] files = read_in_files(file);
  for(int i = 0; i < inputs.length; i++){
    dataLoad(files[i],i);
    System.out.println("data file " + (i+1) + " : " + files[i]);
  }
}

//This method will return an array of size n with n random integers
//between zero and the length of our output array.
//This method will be used in my 10 fold cross validation method
//The return array will hold the index numbers of which data we want to hold aside
public int[] dataSplit(int n){
  int[] randomInts = new int[n];
  int range = inputs[0].getArrayLength()-1;
  int counter = 0;
  int value = 0;
  
  do{
    value =  (int)(Math.random()*range);
    //check to make sure value is not already in there
    if(!checkArray(value, randomInts)){//check array returns false if value is not in randomInts
      randomInts[counter] = value;//rand # between 0 - range
      counter++;
    }
  }
  //checkArray must == false and counter < n == false to move out of loop
  while(counter < n);
  
  return randomInts;
}

//This method will be used in conjunction with dataSplit to 
//make sure that we do not put duplicate "random" numbers in the same
//Array, returns false if array is good, returns true if check matches a
//number in the array
public boolean checkArray(int check, int[] array){
  for(int i = 0; i < array.length; i++){
    if(array[i] == check){//go through array, return true if theres a match
      return true;
    }
  }
  return false;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}//End NeuralNetwork Class