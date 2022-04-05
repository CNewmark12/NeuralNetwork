public class Correlation
{
  //This class will have methods that will calculate the correlation between two sets of Data
  //This method can then be used in a loop to figure out which stocks are best correlated
  
  
  //calculates the mean of a data vector
  public static double mean(double[] data){
    double mean;
    double sum = 0;
    for(int i = 0; i < data.length; i++)
      sum += data[i];//adding up all the points
    
  mean = sum/data.length;
  return mean;
  }
  
  //calculates the standard deviation of a data vector
  public static double stdDeviation(double[] data){
    double mean = mean(data);
    double sum = 0;
    
    double stdDev;
    
    for(int i = 0; i < data.length; i++){
      data[i] = data[i] - mean;
    }
    for(int i = 0; i < data.length; i++){
      sum += data[i]*data[i];
    }
    
    stdDev = Math.sqrt(sum/(data.length-1));
    return stdDev;
  }

  //calculates the covariance of two data vectors
public static double covariance(double[] data1, double[] data2){
    double mean1 = mean(data1);
    double mean2 = mean(data2);
    
    double sum = 0;
    double covariance;
  
    //subtract the average from each point
    for(int i = 0; i < data1.length; i++){
      data1[i] = data1[i] - mean1;
      data2[i] = data2[i] - mean2;
    }
    
    for(int i = 0; i < data1.length; i++){
      sum += data1[i]*data2[i];
    }
    
    covariance = sum/(data1.length-1);
    return covariance;
    
  }

//calculates the correlation between two data vectors
public static double correlation(double[] data1, double[] data2){
  double correlation = covariance(data1,data2)/(stdDeviation(data1)*stdDeviation(data2));
  return correlation;
}

}//end Correlation