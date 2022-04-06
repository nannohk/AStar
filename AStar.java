import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Stack;

public class AStar
{
   private int rows = 0;
   private int cols = 0;

   private Node[][] map;
   private Node start = null;
   private Node end = null;
   
   private PriorityQueue<Node> openNodes;
   private HashMap<Node, Boolean> closedNodes; // don't really care what the value is. Just want a fast way to know if something is in this set 
   
   
   // Creates the map of Nodes and initializes the start and end variables
   public AStar(File file) throws FileNotFoundException
   {
      openNodes = new PriorityQueue<Node>();
      closedNodes = new HashMap<Node, Boolean>();
   
      Scanner scan = new Scanner(file);
      
      String line = scan.nextLine();
      String[] arrSizes = line.split(" ");
      this.rows = Integer.parseInt(arrSizes[0]);
      this.cols = Integer.parseInt(arrSizes[1]);
      
      this.map = new Node[rows][cols];
      
      String[] colCell;
      String cellVal;
      
      for(int i=0; i<this.rows; i++)
      {
         line = scan.nextLine();
         colCell = line.split(" ");
         
         for(int j=0; j<this.cols; j++)
         {
            cellVal = colCell[j];
            
            if(cellVal.equals("S"))
            {
               this.start = new Node(i, j, true);
               this.map[i][j] = this.start;
               this.closedNodes.put(this.start, true);
            }
            else if(cellVal.equals("E"))
            {
               this.end = new Node(i, j, true);
               this.map[i][j] = this.end;
            }   
            else if(cellVal.equals("."))
            {
               this.map[i][j] = new Node(i, j, true);
            }
            else
            {
               this.map[i][j] = new Node(i, j, false);
            }   
         }
      }
   
      scan.close();
   }
   
   
   private int calcDist(int x1, int y1, int x2, int y2)
   {
      int x_dist = Math.abs(x1 - x2);
      int y_dist = Math.abs(y1 - y2);
      
      if(x_dist == 0 || y_dist == 0)
         return (x_dist * 10) + (y_dist * 10);
      else if(x_dist == y_dist)
         return x_dist * 14;
      else
      {
         int diagonals = Math.min(x_dist, y_dist);
         x_dist -= diagonals;
         y_dist -= diagonals;
         return (diagonals * 14) + (x_dist + y_dist) * 10;
      }
   }


   private void calcNeighbors(Node cur)
   {
      int row = cur.getRow();
      //System.out.println(row);
      int col = cur.getCol();
      int g_cost = 0;
      int h_cost = 0;
      int f_cost = 0;
      int oldFCost = 0;
      
      for(int i=row-1; i<=row+1; i++)
      {
         if(i >= 0 && i < this.rows)
         {
            for(int j=col-1; j<=col+1; j++)
            {
               if(j >= 0 && j < this.cols)
               { 
                  // if neighbor is traversable and isn't the current
                  if(this.map[i][j].isWalkable() && this.map[i][j] != cur)
                  {
                     // if the closed HashMap doesn't already contain this Node
                     if(!this.closedNodes.containsKey(map[i][j]))
                     {
                        // calculate g, h, f
                        g_cost = cur.getGCost() + calcDist(cur.getCol(), cur.getRow(), this.map[i][j].getCol(), this.map[i][j].getRow());
                        h_cost = calcDist(this.map[i][j].getCol(), this.map[i][j].getRow(), this.end.getCol(), this.end.getRow());
                        f_cost = g_cost + h_cost;
                        
                        oldFCost = this.map[i][j].getFCost(); 
                        
                        // if a neighbor already has values
                        if(oldFCost != 0)
                        {
                           Node newNode = new Node(i, j, g_cost, h_cost, f_cost, cur);
                           // if old values are worse, update
                           if(oldFCost > f_cost)
                           {
                              this.map[i][j].setGCost(g_cost);
                              this.map[i][j].setHCost(h_cost);
                              this.map[i][j].setFCost(f_cost);
                              this.map[i][j].setComesFrom(cur);
                           }
                        }
                        else // otherwise just set values and add to list of open Nodes
                        {
                           this.map[i][j].setGCost(g_cost);
                           this.map[i][j].setHCost(h_cost);
                           this.map[i][j].setFCost(f_cost);
                           this.map[i][j].setComesFrom(cur);
                           this.openNodes.add(this.map[i][j]);
                        }
                     }
                  }
                }
            }
          }
      }
   }
   
   
   public void solve()
   {
      Node current = this.start;
      calcNeighbors(current); 
           
      while(current != this.end)
      {
         current = openNodes.poll();
         openNodes.remove(current);
         closedNodes.put(current, true);
         
         calcNeighbors(current);
      } 
      
      printShortestPath();
   }
   
   public void solve(String filename)
   {
      int count = 0;
      try
      {
         Node current = this.start;
         calcNeighbors(current); 
         count++;
         
         while(current != this.end)
         {
            current = openNodes.poll();
            openNodes.remove(current);
            closedNodes.put(current, true);
            
            calcNeighbors(current);
            count++;
            //System.out.println("Put in CLOSED: " + current);
         } 
      }   
      catch(Exception e)
      {
         System.out.println("You managed to calc " + count + "neighbors");   
      }
 
      fileWriteShortestPath(filename);
   }
   
   
   private void printShortestPath()
   {
      HashMap<Node, Boolean> shortestPath = new HashMap<Node, Boolean>();
      Node current = this.end;
      
      while(current.comesFrom() != null)
      {
         shortestPath.put(current, true);
         current = current.comesFrom();
      }
      shortestPath.put(this.start, true);
      
      // printing
      for(int i=0; i<this.rows; i++)
      {
         for(int j=0; j<this.cols; j++)
         {
            if(this.map[i][j] == this.start)
               System.out.print("S ");
            else if(this.map[i][j] == this.end)
               System.out.print("E ");
            else if(!this.map[i][j].isWalkable())
               System.out.print("# ");
            else if(shortestPath.containsKey(this.map[i][j]))
               System.out.print("* ");
            else
               System.out.print(". ");
         }
         System.out.println();
      }
   }
   
   
   private void fileWriteShortestPath(String filename)
   {
      HashMap<Node, Boolean> shortestPath = new HashMap<Node, Boolean>();
      Node current = this.end;
      
      while(current.comesFrom() != null)
      {
         shortestPath.put(current, true);
         current = current.comesFrom();
      }
      shortestPath.put(this.start, true);
      
      
      try
      {
         FileWriter fw = new FileWriter(filename);
         // file writing
         for(int i=0; i<this.rows; i++)
         {
            for(int j=0; j<this.cols; j++)
            {
               if(this.map[i][j] == this.start)
                  fw.write("S ");
               else if(this.map[i][j] == this.end)
                  fw.write("E ");
               else if(!this.map[i][j].isWalkable())
                  fw.write("# ");
               else if(shortestPath.containsKey(this.map[i][j]))
                  fw.write("* ");
               else
                  fw.write(". ");
            }
            fw.write("\n");
         }
       }
       catch(Exception e)
       {
         System.out.println("something went wrong...");
       }
   }

   
   
   public String toString()
   {
      String str = "";
      
      for(int i=0; i<this.rows; i++)
      {
         for(int j=0; j<this.cols; j++)
         {
            str += this.map[i][j].comesFrom();
         }
         str += "\n";
      }
      
      return str;
   }


}