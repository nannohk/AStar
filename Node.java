public class Node implements Comparable<Node>
{
   private int row;
   private int col;
   private int g_cost;
   private int h_cost;
   private int f_cost;
   private boolean walkable;
   private Node comesFrom;
   private boolean inShortestPath;
   
   // If all of this info is entered in then it is assumed that the node is a walkable Node
   // so don't this one if you don't want it to be walkable
   public Node(int row, int col, int g_cost, int h_cost, int f_cost, Node comesFrom)
   {
      this.row = row;
      this.col = col;
      this.g_cost = g_cost;
      this.h_cost = h_cost;
      this.f_cost = f_cost;
      this.walkable = true;
      this.comesFrom = comesFrom;
      this.inShortestPath = false;
   }
   
   // For initial intialization
   public Node(int row, int col, boolean walkable)
   {
      this.row = row;
      this.col = col;
      this.g_cost = 0;
      this.h_cost = 0;
      this.f_cost = 0;
      this.walkable = walkable;
      this.comesFrom = null;
      this.inShortestPath = false;
   }
   
   
   // Getter Methods ----------------------------------
   public int getRow() { return this.row; }
   
   public int getCol() { return this.col; }
   
   public int getGCost() { return this.g_cost; }
   
   public int getHCost() { return this.h_cost; }
   
   public int getFCost() { return this.f_cost; }
   
   public Node comesFrom() { return this.comesFrom; }
   
   public boolean isWalkable() { return this.walkable; }
   
   public boolean isInShortestPath() { return this.inShortestPath; }
   // -------------------------------------------------
   
   
   // Setter Methods ----------------------------------
   public void setRow(int row) { this.row = row; }
   
   public void setCol(int col) { this.col = col; }
   
   public void setGCost(int g) { this.g_cost = g; }
   
   public void setHCost(int h) { this.h_cost = h; }
   
   public void setFCost(int f) { this.f_cost = f; }
   
   public void setComesFrom(Node n) { this.comesFrom = n; }
   // -------------------------------------------------
   
   public void putInShortestPath() { this.inShortestPath = true; }
   
   
   public int compareTo(Node n)
   {
      int otherFCost = n.getFCost();
      int otherHCost = n.getHCost();
      
      if(this.f_cost == otherFCost)
      {
         if(this.h_cost == otherHCost)
            return 0;
         else
            return this.h_cost < otherHCost ? -1 : 1;
      }
      else
         return this.f_cost < otherFCost ? -1 : 1;
   }
   
   
   public String toString()
   {
     return "(" + this.f_cost + "," + this.g_cost + "," + this.h_cost + ") ";
   }
}