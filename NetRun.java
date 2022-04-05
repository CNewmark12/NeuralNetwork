//This is for a feedforward multilayer perceptron
import java.util.Scanner;
public class NetRun
{
  
  public static void main(String[] args)
  {
   final int training_testing_split = 238;
   final double learning_error_parameter = 0.1;
   final double learning_parameter = 0.12;
   final int desired_epochs = 8000;
   final int activation_type = 1;//1 for sigmoid, 0 for linear
   //final String file = "C:/Users/Dell 15r/Documents/Albany Hw/Neural Nets/testWrite.txt";//this is the file we export the network to
   final String netStructure = "NetStructureTest.txt";
   final String loadInputfilesFrom = "LoadFiles.txt";
   final String targetFile = "CIMTclose.txt";
   Scanner scan = new Scanner(System.in); 
   NeuralNetwork net1 = new NeuralNetwork(netStructure);
   net1.net_structure();
   net1.loadInputs(loadInputfilesFrom);
   
   /*
   
   
   //net1.dataLoad("probabilityOfIncrease.txt",0);
   net1.dataLoad("CIMTfiveDAYscaled.txt",0);
   net1.dataLoad("CIMToneDAYscaled.txt",1);
   
   net1.dataLoad("NASDAQfiveDAYscaled.txt",3);
   net1.dataLoad("NASDAQoneDAYscaled.txt",4);
   net1.dataLoad("VolumeScaled.txt",5);
   net1.dataLoad("SandPCloseScaled.txt",2);
   net1.dataLoad("probabilityOfIncreaseSP.txt",6);
   net1.dataLoad("CIMTmovingAverage.txt",7);
   net1.dataLoad("NASDAQmovingAvg.txt",8);
   net1.dataLoad("SandPOneDayChangeScaled.txt",13);
   //net1.dataLoad("SandPVolumeScaled.txt",8);
   net1.dataLoad("CIMTpriceFlow1.txt",9);
   net1.dataLoad("CIMTpriceFlow2.txt",10);
   net1.dataLoad("CIMTpriceFlow3.txt",11);
   net1.dataLoad("CIMTpriceFlow4.txt",12);
   
   net1.dataLoad("Monday.txt",16);
   net1.dataLoad("Tuesday.txt",17);
   net1.dataLoad("Wednesday.txt",15);
   net1.dataLoad("Thursday.txt",18);
   net1.dataLoad("Friday.txt",14);
   
   
   
   net1.dataLoad("Monday.txt",0);
   net1.dataLoad("Tuesday.txt",1);
   net1.dataLoad("Wednesday.txt",2);
   net1.dataLoad("Thursday.txt",3);
   net1.dataLoad("Friday.txt",4);
   net1.dataLoad("CIMTfiveDAYscaled.txt",5);
   net1.dataLoad("CIMToneDAYscaled.txt",6);
   net1.dataLoad("NASDAQfiveDAYscaled.txt",8);
   net1.dataLoad("NASDAQoneDAYscaled.txt",9);
   net1.dataLoad("CIMTmovingAverage.txt",7);
   net1.dataLoad("NASDAQmovingAvg.txt",10);
   
   
   
   //net1.net_structure();
   //net1.printData(0);
   */
   //net1.single_output_netTraining(targetFile,learning_parameter,training_testing_split,learning_error_parameter,desired_epochs, activation_type);
   //testing
   
   //(run n times, starting at mth datum, 1 = show neuron activations, 1 for sigmoid)
   //net1.single_output_netTesting(net1.single_output_netRun(9,training_testing_split,0,activation_type),targetFile,training_testing_split); 
   net1.ten_fold_testing(net1.ten_fold_training(targetFile,learning_parameter,learning_error_parameter,desired_epochs, activation_type),targetFile,activation_type,0);
   
   //export Weights?
   /*
   String check;
   //do{
   System.out.println("Do you want to save the weights? Y/N");
   check = scan.next();
   //}
   */
   //while(check.compareTo("y") != 0 || check.compareTo("Y") != 0 && check.compareTo("n") != 0 || check.compareTo("N") != 0);
   /*
   if(check.compareTo("y") == 0 || check.compareTo("Y") == 0){
     System.out.println("check");
     net1.weightExportTo(file);
   }
   */
   /*
   //test importing the weights
   NeuralNetwork net2 = new NeuralNetwork();
   net2.weightImportFrom(file);
   net2.show_weights();
   */
   
  }
}