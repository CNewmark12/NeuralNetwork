import java.io.*;
import java.util.*;

//The purpose of this class will be to manipulate data
//into more meaningful feature extraction for our NN
//it will contain methods that will be able to take in a data vector
//manipulate it, and then write it to a txt file
public class DataManipulation extends NeuralNetwork{
  
  //this method will take a data array and write it to an output_file
  //without any manipulation
  public static void dataWriter(double[] data, String output_file){
    try{
    FileWriter write = new FileWriter(output_file, false);
    PrintWriter print_line = new PrintWriter(write);
    
       for(int i = 0; i < data.length; i++){
         print_line.println(data[i]);
       }
       print_line.close();
    }
    catch(IOException e){
      System.out.println("File not found.");
    }
  }
  //this method will take a data matrix and write each COLUMN sequentially 
  //to each output_file in our string array without any manipulation
  public static void dataWriter(double[][] data, String[] output_file){
    try{
    int counter = 0;
    
    while(counter < output_file.length){
      String temp = output_file[counter];
      //make a new write object for every file in output_file
      FileWriter write = new FileWriter(temp, false);
      PrintWriter print_line = new PrintWriter(write);
       for(int i = 0; i < data.length; i++){
         print_line.println(data[i][counter]);//goes through the "counter" column
       }
       print_line.close();
       counter++;
     }
    }
    catch(IOException e){
      System.out.println("File not found.");
    }
  }
  //This function will create a matrix(prices,days) of data to be used by the NN
  //It is to be matched with corresponding buy/sell signals
  //It is called linearNormalization
  public static double[][] linearNormalization(double[] data, int days){
    double[][] matrix = new double[data.length-days][days];
    double[] dynamicRange = new double[days];
    final double constant = (double)2/(data.length-days);
    double[] vector = new double[days];
    double[] vectorAbs = new double[days];
    
    //calculates vectorAbs
      for(int a = 1; a < days+1; a++){
        for(int b = (days+1); b < data.length; b++){
          vectorAbs[a-1] += Math.abs(data[b]-data[b-a])/data[b];
        }
      }//calculates vectorAbs
      //calculates matrix
    for(int i = days; i < data.length; i++){
      
      //calculates vector
      for(int c = 1; c < days+1; c++){
        vector[c-1] = (data[i]-data[i-c])/data[i];
      }//calculates vector
      
      //calculates dynamicRange
      for(int k = 0; k < days; k++){
        dynamicRange[k] = constant*vectorAbs[k];
      }//calculates dynamicRange
      
      //calculates matrix(i,j)
      for(int j = 0; j < days; j++){
        matrix[i-days][j] = vector[j]/dynamicRange[j];
      }//calculates matrix(i,j)
      
    }//calculates matrix
    return matrix;
  }
  
  //This function will calculate the exponential moving average of a set of data
  public static double[] ema(int days, double[] prices){
    double k = (double)2/(days+1);
    double[] ema = new double[prices.length-days];
    double theSum = 0;
    double startingEMA = 0;
    
    for(int i = 0; i < days; i++){
      theSum += prices[i];//add up beginning prices
    }
    startingEMA = theSum/days;//calculate the starting value
    ema[0] = startingEMA;
    
    for(int i = 1; i < prices.length-days; i++){
      ema[i] = (prices[i]*k)+ema[i-1]*(1-k);
    }//calculate the rest of the EMAs
    return ema;
  }
  
  //This function will calculate the MACD using the EMA function
  public static double[] macd(double[] twelve_day_prices, double[] twentysix_day_prices){
    double[] twelve_day_ema = ema(12,twelve_day_prices);
    System.out.println(twelve_day_ema.length + "12 day ema");
    double[] twentysix_day_ema = ema(26,twentysix_day_prices);
    System.out.println(twentysix_day_ema.length + "26 day ema");
    //macd will have the length of our 12_day_ema and 26_day_ema
    double[] macd = new double[twelve_day_ema.length];
    for(int i = 0; i < macd.length; i++){
      macd[i] = twelve_day_ema[i]-twentysix_day_ema[i];
    }
    return macd;
  }
  
  
}//End DataManipulation Class