package PE1;

/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Sana Morshedzadeh
Student Number: 218079079
Course Section: A
*/


import java.util.ArrayList;


public class PE1 {
	Maze dogMaze;
	/**
	 * This method sets up the maze using the given input argument
	 * @param maze is a maze that is used to construct the dogMaze
	 */
	public void setup(String[][] maze) {
		/* insert your code here to create the dogMaze
		 * using the input argument.
		 */
		this.dogMaze = new Maze(maze);
	}
	public int findUpperGate(int column, int count){

		String[][] aMaze = this.dogMaze.getMaze();
		int columnLength = aMaze[0].length;

		if (columnLength == column)
			return count;

		if (aMaze[0][column].charAt(0) == '0')
			return findUpperGate(column+1,count+1);

		else
			return findUpperGate(column+1, count);
	}

	public int findRightGate(int row, int count){

		String[][] aMaze = this.dogMaze.getMaze();
		int rowLength = aMaze.length;

		if (row == rowLength)
			return count;

		else if (aMaze[row][aMaze[0].length-1].charAt(3) == '0')
			return findRightGate(row+1,count+1);

		else
			return findRightGate(row+1, count);
	}
	public int findLowerGate(int column, int count){

		String[][] aMaze = this.dogMaze.getMaze();
		int columnLength = aMaze[0].length;

		if (columnLength == column)
			return count;

		else if (aMaze[aMaze.length-1][column].charAt(0) == '0')
			return findLowerGate(column+1, count+1);

		else
			return findLowerGate(column+1, count);
	}
	/**
	 * @param row is the number of elements in each row
	 * @param count is the number of the gates on the left side
	 */
	public int findLeftGate(int row, int count){

		String[][] aMaze = this.dogMaze.getMaze();
		int rowLength = aMaze.length;

		if (rowLength == row)
			return count;

		else if (aMaze[row][0].charAt(1) == '0')
			return findLeftGate(row+1, count +1);

		else
			return findLeftGate(row+1, count);
	}


	/**
	 * This method returns true if the number of
	 * gates in dogMaze >= 2.
	 * @return it returns true, if enough gate exists (at least 2), otherwise false.
	 */

	public boolean enoughGate () {
		// insert your code here. Change the return value to fit your purpose.


		 //totalGates is the total sum of all the gates from each gate finder
		 //this method returns the boolean answer to if totalGates is more than or equal to 2

		int totalGates = 0;
		totalGates = findUpperGate(0, 0) + findLeftGate(0, 0) + findLowerGate(0, 0)
				+ findRightGate(0, 0);

		return totalGates >= 2;
	}

	/**
	 * This method finds a path from the entrance gate to
	 * the exit gate.
	 * @param row is the index of the row, where the entrance is.
	 * @param column is the index of the column, where the entrance is.
	 * @return it returns a string that contains the path from the start to the end.
	 * The return value should have a pattern like this (i,j)(k,l),...
	 * The first pair of the output must show the entrance given as the
	 * input parameter (i.e. (row,column)
	 * No whitespace is allowed in the output.
	 */
	public String findPath (int row, int column) {
		// insert your code here. Change the return value to fit your purpose.

		// inorder to find a path, we need to find the exit first. create a separate method called containsExit
		String result = "";

		Location start = new Location(row,column);

		String[][] aMaze = dogMaze.getMaze();

		ArrayList<Location> path = new ArrayList<>();

		boolean[][] checked = new boolean[aMaze.length][aMaze[0].length];

		boolean[][] multiPath = new boolean[aMaze.length][aMaze[0].length];


		search(path, checked, multiPath, aMaze, start, start);

		int i = 0;
		while (i <= path.size()-1){
			result += path.get(i).toString();
			if (i == path.size()-1) break;
			else i++;
		}
		return result;
	}

	/**
	 * this method checks if there is an unchecked index connected to the given index
	 * @param multiPath a 2D array containing booleans of all indexes with more than one connected indexes
	 * @param checked a 2D array containing booleans of whether an index has been checked or not
	 * @param x the x-coordinate of the location of the index
	 * @param y the y-coordinate of the location of the index
	 * @return an ArrayList of all the possible index's Locations
	 */
	public ArrayList<Location> connected(boolean[][] multiPath, boolean[][] checked, int x, int y) {
		int nextRow = 0;
		int nextColumn = 0;
		String[][] aMaze = dogMaze.getMaze();
		ArrayList<Location> connect = new ArrayList<>();

		for (int i = 0; i < aMaze.length; i++) {
			for (int j = 0; j < aMaze[0].length; j++) {

				nextRow = i;
				nextColumn = j;

				if (((x - nextRow == -1 && aMaze[nextRow][nextColumn].charAt(0) == '0')
						|| (x - nextRow == 1 && aMaze[nextRow][nextColumn].charAt(2) == '0')
						|| (y - nextColumn == -1 && aMaze[nextRow][nextColumn].charAt(1) == '0')
						|| (y - nextColumn == 1 && aMaze[nextRow][nextColumn].charAt(3) == '0'))
						&& (((nextRow == x && (nextColumn - y >= -1 && nextColumn - y <= 1))
						|| (nextColumn == y && (nextRow - x >= -1 && nextRow - x <= 1)))
						&& !checked[nextRow][nextColumn] && !multiPath[nextRow][nextColumn])) {

					Location aLocation = new Location(nextRow, nextColumn);
					connect.add(aLocation);
				}
			}
		}
		return connect;
	}

	/**
	 * this method checks whether the maze contains an exit or not.
	 * @param aMaze a 2D string array representing the maze
	 * @param start is where we started
	 * @param current is where we are right now. This point will be checked
	 * @return true if the location is the exit. Else will return false
	 */
	public boolean containsExit(String[][] aMaze, Location start, Location current){

		int currentX = current.getxCoordinate();
		int currentY = current.getyCoordinate();
		String index = aMaze[currentX][currentY];

		boolean exit = (index.charAt(0) == '0' && index.charAt(3) == '0' && currentX == 0 && currentY == aMaze[0].length - 1)
				//if 0110 exists at the top right edge
				|| (index.charAt(0) == '0' && index.charAt(1) == '0' && currentX == 0 && currentY == 0)
				//if 0011 exists at the top-left edge
				|| (index.charAt(1) == '0' && index.charAt(2) == '0' && currentX == aMaze.length - 1 && currentY == 0)
				//if 1001 exists at the bottom-left edge
				|| (index.charAt(1) == '0' && index.charAt(3) == '0' && currentX == aMaze.length - 1 && currentY == aMaze[0].length - 1)
				//if 1010 exists at the bottom-right edge
				|| (index.charAt(0) == '0' && !current.equals(start) && current.getxCoordinate() == 0)
				//it is at the top row (not on the corner) and the top section is open
				|| (index.charAt(1) == '0' && !current.equals(start) && current.getxCoordinate() == 0)
				//if the x-coordinates is 0 and it is x0xxx
				|| (index.charAt(2) == '0' && !current.equals(start) && current.getxCoordinate() == aMaze.length - 1)
				// if the third index is 0, is not the same Location as where we started and it is located at the right side of the maze
				|| (index.charAt(3) == '0' && !current.equals(start) && current.getyCoordinate() == aMaze[0].length - 1);
				// if the last index is 0, is not the same Location as the start and it is on the bottom of the maze

			return exit;
	}

	/**
	 * In this method, we remove the top index if it has been checked
	 * @param checked a 2D array that checks whether an index have been checked or not
	 * @param path	an ArrayList containing the pathway we are taking
	 */
	public void emptyStack(boolean[][] checked, ArrayList<Location> path){

		int x = 0;
		int y = 0;

		if(path.size() >= 1){

			x = path.get(path.size() - 1).getxCoordinate();
			y = path.get(path.size() - 1).getyCoordinate();

			if(checked[x][y]){

				path.remove(path.size()-1);
				emptyStack(checked, path);
			}
		}
	}

	/**
	 * This method uses deep first search to look for a continuous pathway
	 * @param path a 2D ArrayList containing the Locations of each index used in the pathway
	 * @param checked a 2D array containing the Boolean of each visited index
	 * @param multiPath a 2D array containing booleans of all indexes with more than one connected indexes
	 * @param aMaze a 2D string array representing the maze
	 * @param start the Location of the starting index/gate
	 * @param current the current index's Location
	 */
	public void search(ArrayList<Location> path, boolean[][] checked, boolean[][] multiPath, String[][] aMaze, Location start, Location current){

		int x = current.getxCoordinate();
		int y = current.getyCoordinate();
		path.add(current);

		ArrayList<Location> nextIndex = new ArrayList<>();

		if(!containsExit(aMaze, start, current)){

			nextIndex = connected(multiPath, checked, x, y);

			if(nextIndex.size()==1)
				checked[x][y] = true;

			else if(nextIndex.size() >= 2)
				multiPath[x][y] = true;

			if(nextIndex.size() == 0){
				checked[x][y] = true;
				emptyStack(checked, path);
			}
			else {
				for(Location flag: nextIndex) {
					search(path, checked, multiPath, aMaze, start, flag);
				}
			}
			checked[x][y] = true;
		}
	}
}


/**
 * This class defines a <code> maze </code> using a 2D array.
 * To complete the code, you should not change the method
 * signatures (header).
 *
 */

class Maze{
	private String [][] maze;

	/**
	 * This constructor makes the maze.
	 * @param maze is a 2D array that contains information
	 * on how each cell of the array looks like.
	 */
	public Maze(String[][] maze) {
		/*complete the constructor so that the maze is
		 * a deep copy of the input parameter.
		 */

		String[][] aMaze = new String[maze.length][maze[0].length];

		for (int i =0; i < maze.length; i++){

			for (int j = 0; j < maze[i].length; j++)
				aMaze[i][j] = maze[i][j];
		}

		this.maze = aMaze;
	}

	/**
	 * This accessor (getter) method returns a 2D array that
	 * represents the maze
	 * @return it returns a reference to the maze
	 */
	public String[][] getMaze(){
		/* complete this method providing that a clone of
		 * the maze should be returned.
		 * you may want to change the return value to fit your purpose.
		 */
		String[][] bMaze = new String[this.maze.length][this.maze[0].length];

		for (int i = 0; i < this.maze.length; i++) {

			for (int j = 0; j < this.maze[i].length; j++)
				bMaze[i][j] = this.maze[i][j];

		}
		return bMaze;
	}

	/**
	 * This method will help us in turning the maze into a string named result
	 * @return result: a string format of the given maze
	 */

	@Override
	public String toString() {
		//insert your code here. Change the return value to fit your purpose.
		String result = "";

		for (int i = 0; i <= maze.length-1; i++) {

			if (i == 0)
				result = "[";

			else
				result += "]\n[";


			for(int j = 0; j <= maze[i].length-1; j++){

				if (j==0)
					result += maze[i][j];

				else
					result = result + " " + maze[i][j];
			}

			if (i == maze.length-1)
				result += "]";
		}

		return result;
	}

}// end of class Maze

/**
 * This class defines a location with <code>xCoordinate</code> and <code>yCoordinate</code> in the maze
 * This will help us in finding the pathways
 */
class Location{

	private int xCoordinate = 0;
	private int yCoordinate = 0;

	/**
	 * the constructor creates a location using the given xCoordinates and yCoordinates
	 * @param xCoordinate the location of the point on the x-axis
	 * @param yCoordinate the location of the point on the y-axis
	 */
	public Location(int xCoordinate, int yCoordinate){

		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	/**
	 * this is the getter for out xCoordinate
	 * @return xCoordinate
	 */
	public int getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * this is the getter method for our yCoordinate
	 * @return yCoordinate
	 */
	public int getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * This is a copy constructor. It shallow-copies the input to our x and y coordinates
	 * @param copyLocation it is a Location that we will copy in our copy constructor
	 */
	public Location(Location copyLocation){
		this.xCoordinate = copyLocation.getxCoordinate();
		this.yCoordinate = copyLocation.getyCoordinate();
	}

	/**
	 * This is a boolean method where the equivalence of our locations are being checked
	 * @param anotherLocation is a Location that will be compared to our original Location
	 * @return true if anotherLocation's coordinates match our x and y coordinates, and returns false otherwise
	 */
	public Boolean equivalent(Location anotherLocation){

		//checking if the x and y coordinates of the given Location and the coordinates of our location is the same
		if ((anotherLocation.getxCoordinate() == this.getxCoordinate()) &&
				(anotherLocation.getyCoordinate() == this.yCoordinate))
			return true;

		else return false;
	}

	/**
	 * this toString method will return a string format of our Location
	 * @return stringLocation: the string format of our Location
	 */
	@Override
	public String toString(){

		String stringLocation = "(" + this.xCoordinate + "," + this.yCoordinate + ")";
		return stringLocation;
	}
}// end of class Location
