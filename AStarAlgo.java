import java.io.File;
import java.io.FileNotFoundException;

public class AStarAlgo
{
   public static void main(String[] args)
   {
      AStar obj = null;
      File file = null;
      long startTime = 0, endTime = 0;
      
      try
      {
         String inputDirectory = "TextConvertedImages";
         String outputDirectory = "SolutionText";
         File inputDir = new File(inputDirectory);
         File outputDir = new File(outputDirectory);
         String[] mapFiles = inputDir.list();
         int numOfFiles = mapFiles.length;
         int indexOfDot = 0;
         String filename;
         
         if(!outputDir.exists())
            outputDir.mkdir();
         
         startTime = System.currentTimeMillis();
         for(int i=0; i<numOfFiles; i++)
         {
            file = new File(inputDirectory + "\\" + mapFiles[i]);
            obj = new AStar(file);
            
            indexOfDot = mapFiles[i].indexOf('.');
            filename = mapFiles[i].substring(0, indexOfDot);
            obj.solve(outputDirectory + "\\" + filename + "_Soln.txt");
         }
         endTime = System.currentTimeMillis();
      }
      catch(FileNotFoundException e)
      {
         System.out.println("An error occured.");
         e.printStackTrace();         
      }
      
      System.out.println("Execution Time: " + (endTime - startTime));
            
   }   
}