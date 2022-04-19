package com.GIS.world;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// To support testing, we will make certain elements of the generic.
//
// You may safely add data members and function members as needed, but
// you must not modify any of the public members that are shown.
//
public class prQuadTree<T extends Compare2D<? super T>> {

	// Inner classes for nodes (public so test harness has access)
	public abstract class prQuadNode {
	}

	public class prQuadLeaf extends prQuadNode {
		// Use an ArrayList to support a bucketed implementation later.
		ArrayList<T> Elements;

		public prQuadLeaf() {

		}

		public prQuadLeaf(T elem) {
			this.Elements = new ArrayList<T>();
			this.Elements.add(elem);
		}
	}

	public class prQuadInternal extends prQuadNode {
		// Use base-type pointers since children can be either leaf nodes
		// or internal nodes.
		prQuadNode NW, NE, SE, SW;

		// Pre: elem != null, Direction is a valid direction
		// Post: sets the corresponding child pointer to a leaf containing the element
		public void insertLeaf(T elem, Direction quadrant) {
			switch (quadrant) {
			case NE:
				this.NE = new prQuadLeaf(elem);
			case NW:
				this.NW = new prQuadLeaf(elem);
			case SE:
				this.SE = new prQuadLeaf(elem);
			case SW:
				this.SW = new prQuadLeaf(elem);
			default:
				break;
			}
		}
	}

	// prQuadTree elements (public so test harness has access)
	public prQuadNode root;
	public long xMin, xMax, yMin, yMax;

	// Add private data members as needed...

	// Initialize quadtree to empty state, representing the specified region.
	// Pre: xMin < xMax and yMin < yMax
	public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.yMin = yMin;
	}

	/**
	 * Helper function for insert and find. Returns an array of doubles bounding the
	 * desired quadrant.
	 * 
	 * @param direction: which direction we're looking in.
	 * @param xLo:       low x-coordinate.
	 * @param xHi:       high x-coordinate.
	 * @param yLo:       low y-coordinate.
	 * @param yHi:       high y-coordinate.
	 * @return array of doubles bounding target quadrant.
	 */
	private double[] getBounds(Direction direction, double xLo, double xHi, double yLo, double yHi) {
		double[] bounds = null;

		double xAxis = (yLo + yHi) / 2;
		double yAxis = (xLo + xHi) / 2;

		switch (direction) {
		case NE:
			bounds = new double[] { yAxis, xHi, xAxis, yHi };
			break;
		case NW:
			bounds = new double[] { xLo, yAxis, xAxis, yHi };
			break;
		case SE:
			bounds = new double[] { yAxis, xHi, yLo, xAxis };
			break;
		case SW:
			bounds = new double[] { xLo, yAxis, yLo, xAxis };
			break;
		default:
			break;
		}

		return bounds;
	}

	/**
	 * Insert helper function.
	 */
	@SuppressWarnings("unchecked")
	private prQuadNode insertHelper(prQuadNode sRoot, T elem, double xLo, double xHi, double yLo, double yHi) {

		// Check if tree is empty
		if (sRoot == null) {
			return new prQuadLeaf(elem);
		}

		// If sRoot is an internal node, we'll insert recursively.
		else if (sRoot instanceof prQuadTree.prQuadInternal) {

			prQuadInternal node = (prQuadInternal) sRoot;

			// Find the direction of where we should place the node and get the bounds of
			// that region.
			Direction quadrant = elem.inQuadrant(xLo, xHi, yLo, yHi);
			double[] bounds = getBounds(quadrant, xLo, xHi, yLo, yHi);

			// Depending on the direction, we'll insert it in that specific region.
			switch (quadrant) {
			case NE:
				node.NE = insertHelper(node.NE, elem, bounds[0], bounds[1], bounds[2], bounds[3]);
				break;
			case NW:
				node.NW = insertHelper(node.NW, elem, bounds[0], bounds[1], bounds[2], bounds[3]);
				break;
			case SE:
				node.SE = insertHelper(node.SE, elem, bounds[0], bounds[1], bounds[2], bounds[3]);
				break;
			case SW:
				node.SW = insertHelper(node.SW, elem, bounds[0], bounds[1], bounds[2], bounds[3]);
				break;
			default:
				break;
			}

			return node;
		}

		// Else, we need to create a new internal node and insert the old one as a new
		// leaf.
		else {
			prQuadInternal node = new prQuadInternal();
			prQuadLeaf leaf = (prQuadTree<T>.prQuadLeaf) sRoot;

			node = (prQuadInternal) insertHelper(node, leaf.Elements.get(0), xLo, xHi, yLo, yHi);
			node = (prQuadInternal) insertHelper(node, elem, xLo, xHi, yLo, yHi);

			return node;
		}
	}

	// Pre: elem != null
	// Post: If elem lies within the tree's region, and elem is not already
	// present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree.
	public boolean insert(T elem) {

		// Reject insertion if it's a duplicate or if it's outside of the world's
		// boundaries.
		if (this.find(elem) != null || !elem.inBox(this.xMin, this.xMax, this.yMin, this.yMax)) {
			return false;
		}

		// Else, we'll insert.
		this.root = insertHelper(this.root, elem, this.xMin, this.xMax, this.yMin, this.yMax);

		return true;
	}

	/**
	 * Helper function for find.
	 */
	@SuppressWarnings("unchecked")
	private T findHelper(T Elem, prQuadNode node, double xLo, double xHi, double yLo, double yHi) {

		// Check if tree is empty, obviously return null.
		if (node == null) {
			return null;
		}

		// If the node is an internal node, we'll find it recursively.
		else if (node instanceof prQuadTree.prQuadInternal) {
			prQuadInternal internalNode = (prQuadInternal) node;

			// Find the quadrant that the element should be in.
			Direction quadrant = Elem.inQuadrant(xLo, xHi, yLo, yHi);
			double[] bounds = getBounds(quadrant, xLo, xHi, yLo, yHi);

			// Look in that specific region.
			switch (quadrant) {
			case NE:
				return findHelper(Elem, internalNode.NE, bounds[0], bounds[1], bounds[2], bounds[3]);
			case NW:
				return findHelper(Elem, internalNode.NW, bounds[0], bounds[1], bounds[2], bounds[3]);
			case SE:
				return findHelper(Elem, internalNode.SE, bounds[0], bounds[1], bounds[2], bounds[3]);
			case SW:
				return findHelper(Elem, internalNode.SW, bounds[0], bounds[1], bounds[2], bounds[3]);
			default:
				return null;
			}

		}

		// Else, the node is a leaf and we should return the data element if it's equal
		// to what we're searching for.
		else {
			prQuadLeaf leaf = (prQuadTree<T>.prQuadLeaf) node;

			if (leaf.Elements.get(0).equals(Elem)) {
				return leaf.Elements.get(0);
			} else {
				return null;
			}
		}
	}

	/**
	 * Checks to see if two regions overlap/intersect.
	 * 
	 * @param firstRegionBounds: quadrant bounds of the first region.
	 * @param secondRegionXLo:   low x-coordinate of the second region.
	 * @param secondRegionXHi:   high x-coordinate of the second region.
	 * @param secondRegionYLo:   low y-coordinate of the second region.
	 * @param secondRegionYHi:   high y-coordinate of the second region.
	 * @return true if regions overlap, false otherwise.
	 */
	private boolean findOverlap(double[] firstRegionBounds, double secondRegionXLo, double secondRegionXHi,
			double secondRegionYLo, double secondRegionYHi) {

		double firstRegionHeight = Math.abs(firstRegionBounds[2]) + Math.abs(firstRegionBounds[3]);
		double firstRegionWidth = Math.abs(firstRegionBounds[0]) + Math.abs(firstRegionBounds[1]);

		double secondRegionHeight = Math.abs(secondRegionYLo) + Math.abs(secondRegionYHi);
		double secondRegionWidth = Math.abs(secondRegionXLo) + Math.abs(secondRegionXHi);

		return (firstRegionBounds[0] + firstRegionWidth >= secondRegionXLo
				&& firstRegionBounds[2] + firstRegionHeight >= secondRegionYLo
				&& firstRegionBounds[0] <= secondRegionXLo + secondRegionWidth
				&& firstRegionBounds[2] <= secondRegionYLo + secondRegionHeight);
	}

	// Pre: elem != null
	// Returns reference to an element x within the tree such that
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	public T find(T Elem) {
		return findHelper(Elem, this.root, this.xMin, this.xMax, this.yMin, this.yMax);
	}

	/**
	 * Void helper for find function.
	 */
	@SuppressWarnings("unchecked")
	private void findHelper(ArrayList<T> collection, prQuadNode node, long xLo, long xHi, long yLo, long yHi) {

		// Check if tree is empty.
		if (node == null) {
			return;
		}

		// Check if the node is an internal node.
		else if (node instanceof prQuadTree.prQuadInternal) {

			/**
			 * Find overlapping quadrants and check that their leaves are within the region.
			 */
			prQuadInternal internalNode = (prQuadInternal) node;

			double[] NEBounds = getBounds(Direction.NE, xLo, xHi, yLo, yHi);
			double[] NWBounds = getBounds(Direction.NW, xLo, xHi, yLo, yHi);
			double[] SEBounds = getBounds(Direction.SE, xLo, xHi, yLo, yHi);
			double[] SWBounds = getBounds(Direction.SW, xLo, xHi, yLo, yHi);

			if (findOverlap(NEBounds, xLo, xHi, yLo, yHi)) {
				findHelper(collection, internalNode.NE, xLo, xHi, yLo, yHi);
			}

			if (findOverlap(NWBounds, xLo, xHi, yLo, yHi)) {
				findHelper(collection, internalNode.NW, xLo, xHi, yLo, yHi);
			}

			if (findOverlap(SEBounds, xLo, xHi, yLo, yHi)) {
				findHelper(collection, internalNode.SE, xLo, xHi, yLo, yHi);
			}

			if (findOverlap(SWBounds, xLo, xHi, yLo, yHi)) {
				findHelper(collection, internalNode.SW, xLo, xHi, yLo, yHi);
			}

		}

		/**
		 * Else, we have a leaf node and if its element is in the region, we'll add it
		 * to our collection.
		 */
		else {
			prQuadLeaf leaf = (prQuadTree<T>.prQuadLeaf) node;

			if (leaf.Elements.get(0).inBox(xLo, xHi, yLo, yHi)) {
				collection.add(leaf.Elements.get(0));
			}
		}

	}

	// Pre: xLo < xHi and yLo < yHi
	// Returns a collection of (references to) all elements x such that x is
	// in the tree and x lies at coordinates within the defined rectangular
	// region, including the boundary of the region.
	public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {
		ArrayList<T> collection = new ArrayList<T>();
		findHelper(collection, this.root, xLo, xHi, yLo, yHi);
		return collection;
	}

	/**
	 * Displays the PRQuadTree, code is from T05_PRQuadTreeImplementation.pdf.
	 * @param sRoot: root of the prQuadTree.
	 * @param Padding: separation for printing's sake.
	 * @param fw: open to write to our log.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void display(prQuadNode sRoot, String Padding, FileWriter fw) throws IOException {
		// Check for empty leaf
		if (sRoot == null) {
			System.out.println(Padding + "*\n");
			return;
		}
		// Check for and process SW and SE subtrees
		if (sRoot.getClass().equals(prQuadInternal.class)) {
			prQuadInternal p = (prQuadInternal) sRoot;
			display(p.SW, Padding + " ", fw);
			display(p.SE, Padding + " ", fw);
		}
		// Display indentation padding for current node
		fw.write(Padding);
		// Determine if at leaf or internal and display accordingly
		if (sRoot.getClass().equals(prQuadLeaf.class)) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			fw.write(Padding + p.Elements);
		} else
			fw.write(Padding + "@\n");
		if (sRoot.getClass().equals(prQuadInternal.class)) {
			prQuadInternal p = (prQuadInternal) sRoot;
			display(p.NE, Padding + " ", fw);
			display(p.NW, Padding + " ", fw);
		}
	}
	
	/**
	 * Taken from Lewis.java from J2. We were told that we can use it on the forum:
	 * Topic Spring 2022 / CS 3114 - McQuain / Project 4 
	 */
	public void printTree(prQuadTree<Point> Tree, String Padding, FileWriter Out) {
    	printTreeHelper(Out,  Tree.root, "");
     }

	@SuppressWarnings("rawtypes")
	public void printTreeHelper(FileWriter Out, prQuadTree.prQuadNode sRoot, String Padding) {

		try {
			// Check for empty leaf
			if ( sRoot == null ) {
				Out.write(Padding + "*\n");
				return;
			}
			// Check for and process SW and SE subtrees
			if ( sRoot.getClass().equals(prQuadInternal.class) ) {
				prQuadTree.prQuadInternal p = (prQuadTree.prQuadInternal) sRoot;
				printTreeHelper(Out, p.SW, Padding + "---");
				printTreeHelper(Out, p.SE, Padding + "---");
			}
			// Display indentation padding for current node
			Out.write(Padding);

			// Determine if at leaf or internal and display accordingly
			if ( sRoot.getClass().equals(prQuadLeaf.class) ) {
				prQuadTree.prQuadLeaf p = (prQuadTree.prQuadLeaf) sRoot;
				for (int pos = 0; pos < p.Elements.size(); pos++) {
					Out.write(Padding + p.Elements.get(pos) + "\n" );
				}
			}
			else if ( sRoot.getClass().equals(prQuadInternal.class) )
				Out.write(Padding + "@\n" );
			else
				Out.write(sRoot.getClass().getName() + "#\n");

			// Check for and process NE and NW subtrees
			if ( sRoot.getClass().equals(prQuadInternal.class) ) {
				prQuadTree.prQuadInternal p = (prQuadTree.prQuadInternal) sRoot;
				printTreeHelper(Out, p.NE, Padding + "---");
				printTreeHelper(Out, p.NW, Padding + "---");
			}
		}
		catch ( IOException e ) {
			return;
		}
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
//Mansour Najah
//mansourn
