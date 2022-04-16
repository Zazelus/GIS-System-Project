package com.GIS.world;
// Represents a point in the xy-plane with integer-valued coordinates.
// Supplies comparison functions specified in the Compare2D interface.
//

public class Point implements Compare2D<Point> {

    public long xcoord;
    public long ycoord;
    
    public Point() {
       xcoord = 0;
       ycoord = 0;
    }
    public Point(long x, long y) {
       xcoord = x;
       ycoord = y;
    }
    public long getX() {
       return xcoord;
    }
    public long getY() {
       return ycoord;
    }
    
    // Returns indicator of the direction to the user data object from the 
    // location (X, Y) specified by the parameters.
    // The indicators are defined in the enumeration Direction, and are used
    // as follows:
    //
    //    NE:  locations are the same, or vector from (X, Y) to user data object
    //         has direction in [0, 90) degrees
    //    NW:  vector from (X, Y) to user data object has direction in [90, 180) 
    //    SW:  vector from (X, Y) to user data object has direction in [180, 270)
    //    SE:  vector from (X, Y) to user data object has direction in [270, 360)  
    //
    public Direction directionFrom(long X, long Y) {
    	long xDiff = xcoord - X;
		long yDiff = ycoord - Y;
		
		if(xDiff == 0 && yDiff == 0 || xDiff > 0 && yDiff >= 0) {
			return Direction.NE;
		} else if(xDiff <= 0 && yDiff > 0) {
			return Direction.NW;
		} else if(xDiff < 0 && yDiff <= 0) {
			return Direction.SW;
		} 
		
		return Direction.SE;
    }
    
    // Returns indicator of which quadrant of the rectangle specified by the
    // parameters that user data object lies in.
    // The indicators are defined in the enumeration Direction, and are used
    // as follows, relative to the center of the rectangle:
    //
    //    NE:  user data object lies in NE quadrant, including non-negative
    //         x-axis, but not the positive y-axis      
    //    NW:  user data object lies in the NW quadrant, including the positive
    //         y-axis, but not the negative x-axis
    //    SW:  user data object lies in the SW quadrant, including the negative
    //         x-axis, but not the negative y-axis
    //    SE:  user data object lies in the SE quadrant, including the negative
    //         y-axis, but not the positive x-axis
    //    NOQUADRANT:  user data object lies outside the specified rectangle
    //
    public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
       // We can check if it's in the rectangle at all by calling our inBox method.
    	if(inBox(xLo, xHi, yLo, yHi)) {
            double xMid = (xLo + xHi) / 2;
            double yMid = (yLo + yHi) / 2;
            
            if((xcoord == xMid && ycoord == yMid) || (xcoord > xMid && ycoord >= yMid)) {
                return Direction.NE;
            } else if(xcoord <= xMid && ycoord > yMid) {
                return Direction.NW;
            } else if(xcoord < xMid && ycoord <= yMid) {
                return Direction.SW;
            } else if(xcoord >= xMid && ycoord < yMid) {
                return Direction.SE;
            }
        }
    	
    	return Direction.NOQUADRANT;
    }
    
    // Returns true iff the user data object lies within or on the boundaries
    // of the rectangle specified by the parameters.
    public boolean   inBox(double xLo, double xHi, double yLo, double yHi) {
    	return xcoord >= xLo && xcoord < xHi && ycoord >= yLo && ycoord < yHi;
    }
    
    public String toString() {
        
       // Do not change...
       return new String("(" + xcoord + ", " + ycoord + ")");
    }
    
    /**
     * Equals method for Point.
     */
    public boolean equals(Object other) {
    	if(other instanceof Point) {
            Point p = (Point) other;
            return p.getX() == xcoord && p.getY() == ycoord;
        }
    	
    	// Should handle null case as well as object being of incorrect type.
    	// We should only have to check one condition and assume any other condition is false.
    	return false;
    }
}

//On my honor:
//
//- I have not discussed the Java language code in my program with
//anyone other than my instructor or the teaching assistants
//assigned to this course.
//
//- I have not used Java language code obtained from another student,
//or any other unauthorized source, including the Internet, either
//modified or unmodified.
//
//- If any Java language code or documentation used in my program
//was obtained from another source, such as a text book or course
//notes, that has been clearly noted with a proper citation in
//the comments of my program.
//
//- I have not designed this program in such a way as to defeat or
//interfere with the normal operation of the grading code.
//
// Mansour Najah
// mansourn
