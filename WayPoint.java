/* WayPoint.java
 * Authors: Armen Arslanian
 *          Anderson Giang
 * COMP 282
 * November 8, 2015
 * Professor Mike Barnes
 */
import SimulationFramework.*;
import java.awt.*;
import java.util.*;
/**<p>A WayPoint inherits from Marker in order to color it and set it a size.</p>
 * @author Armen Arslanian
 * @author Anderson Giang
 * @version 1.0
 * @since 11/8/2015
 */
public class WayPoint extends Marker {
   /**"x" value of WayPoint*/
   private int x = 0;
   /**"y" value of WayPoint*/
   private int y = 0;
   /**Height of WayPoint*/
   private double height = 0;
   /**Cost of WayPoint if it's a city*/
   private int cityCost = 0;
   /**WayPoint's amount of gold*/
   private int gold = 0;
   /**Treasure map "x" value*/
   private int mapX = 0;
   /**Treasure map "y" value*/
   private int mapY = 0;
   /**Count of how many neighbors a WayPoint has*/
   private int countOfNeighbors = 0;
   /**If a WayPoint is visited or not*/
   private boolean visited= false;
   /**Neighbors of a WayPoint*/
   private ArrayList<Point> neighbors = new ArrayList<Point>();
   /**Parent of a WayPoint*/
   private WayPoint parent = null;
   /**Keep a current WayPoint*/
   private WayPoint current;
   /**Goal WayPoint*/
   private WayPoint goal;
   /**Three dimensional distance from current WayPoint to the neighbor WayPoint*/
   private double g = 0;
   /**Two dimensional distance from adjacent neighbor WayPoint to the goal WayPoint (heuristic distance)*/
   private double h = 0;
   /**Total cost: f = g + h*/
   private double f = 0;
   /**<p>Constructor that creates a WayPoint.</p>
    * <p>By default, the WayPoints are black and have a size of 2.
    * City, gold, and map WayPoints have a size of 5.
    * City WayPoints are cyan.
    * Gold Waypoints are yellow.
    * Map WayPoints are magenta.</p>
    *
    * @param x WayPoint x coordinate
    * @param y WayPoint y coordinate
    * @param height WayPoint location height (elevation)
    * @param cityCost If WayPoint has city cost
    * @param gold If WayPoint has gold
    * @param mapX If WayPoint has mapX coordinate
    * @param mapY If WayPoint has mapY coordinate
    * @param countOfNeighbors How many neighbors does the WayPoint have
    * @param neighbors WayPoint's neighbor coordinates
    */
   public WayPoint(int x, int y, int height, int cityCost, int gold, 
                   int mapX, int mapY, int countOfNeighbors, ArrayList<Point> neighbors) {
      super(x, y, Color.black, 2); //use suitable constructor from Marker class
      this.height = height;
      this.cityCost = cityCost;
      this.gold = gold;
      this.mapX = mapX;
      this.mapY = mapY;
      this.countOfNeighbors = countOfNeighbors;
      this.neighbors = neighbors;
      //set the color and size of non-default WayPoints accordingly
      if (cityCost > 0) {
         setColor(Color.cyan);
         setSize(5);
      }
      if (gold > 0) {
         setColor(Color.yellow);
         setSize(5);
      }
      if ((mapX > 0) && (mapY > 0)) {
         setColor(Color.magenta);
         setSize(5);
      }
   }
   /**<p>True means the point is visited.
    * False means the point is not visited.</p>
    * @return Visited boolean value.      
    */
   public boolean isVisited() {
      return visited;
   }
   /**<p>Set Method to set visited point to true.
    *    True means the point is visited.
    *    False means the point is not visited.</p>
    * @param visited boolean variable.
    */
   public void setVisited(boolean visited) {
      this.visited = visited;
   }
   /**<p>Get Method to return height of a WayPoint</p>
    * @return Height of WayPoint.
    */
   public double getHeight() {
      return height;
   }
   /**<p>Get Method to return gold value of a WayPoint</p>
    * @return Gold value of Waypoint.
    */
   public int getGold() {
      return gold;
   }
   /**<p>Set Method to set gold values.</p>
    * @param gold Set gold value of a WayPoint if needed.
    */
   public void setGold(int gold) {
      this.gold = gold;
   }
   /**<p>Get Method to return neighbors of a WayPoint</p>
    * @return ArrayList of Points (Neighbors).
    */
   public ArrayList<Point> getNeighbors() {
      return neighbors;
   }
   /**<p>Get Method to return city cost of a WayPoint</p>
    * @return City cost of WayPoint.
    */
   public int getCityCost() {
      return cityCost;
   }
   /**<p>Get Method to return the parent of a given WayPoint</p>
    * @return Parent of WayPoint.
    */
   public WayPoint getParent() {
      return parent;
   }
   /**<p>Set Method to set the parent of a WayPoint.</p>
    * @param parent WayPoint variable. Set a parent of a WayPoint.
    */
   public void setParent(WayPoint parent) {
      this.parent = parent;
   }
   /**<p>Get Method to return the three dimensional distance from current WayPoint to the neighbor WayPoint.</p>
    * @return Three dimensional distance.
    */
   public double getG() {
      return g;
   }
   /**<p>Set Method to set the three dimensional distance from current WayPoint to the neighbor WayPoint.</p>
    * @param g double variable. Set what the g value is.
    */
   public void setG(double g) {
      this.g = g;
   }
   /**<p>Get Method to return the two dimensional distance from adjacent neighbor WayPoint to the goal WayPoint (heuristic distance).</p>
    * @return Two dimensional distance.
    */
   public double getH() {
      return h;
   }
   /**<p>Set Method to set the two dimensional distance from adjacent neighbor WayPoint to the goal WayPoint (heuristic distance).</p>
    * @param h double variable. Set what the h value is.
    */
   public void setH(double h) {
      this.h = h;
   }
   /**<p>Get Method to return the g + h distance.</p>
    * @return g + h distance.
    */
   public double getF() {
      return f;
   }
   /**<p>Set Method to set the g + h distance.</p>
    * @param f double variable. Set what the f value is.
    */
   public void setF(double f) {
      this.f = f;
   }
   /**<p>Get Method to return the x value for a treasure map.</p>
    * @return mapX int variable. x value.
    */
   public int getMapX() {
      return mapX;
   }
   /**<p>Set Method to set the mapX value.</p>
    * @param mapX Set the mapY value if needed..
    */
   public void setMapX(int mapX) {
      this.mapX = mapX;
   }
   /**<p>Get Method to return the y value for a treasure map.</p>
    * @return int variable. y value.
    */
   public int getMapY() {
      return mapY;
   }
   /**<p>Set Method to set the mapY value.</p>
    * @param mapY Set the mapY value if needed.
    */
   public void setMapY(int mapY) {
      this.mapY = mapY;
   }
}//end WayPoint class