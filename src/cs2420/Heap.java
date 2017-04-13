package cs2420;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Represents a priority queue of generically-typed items.
 * The queue is implemented as a min heap.
 * 
 * The min heap is implemented implicitly as an array.
 * 
 * @author germain & Kylee Fluckiger & Chloe Josien
 */
public class Heap<Type> implements Priority_Queue<Type>
{
	/** 
	 * The number of elements in the heap (NOT: the capacity of the array)
	 */
	private int							size;

	/**
	 * The implementation array used to store heap values.
	 * 
	 * NOTE: the capacity of the array will be larger (or equal) to the size (of the heap).
	 * 
	 * WARNING: to simplify math, you are to use a 1 INDEXED array. (this means you ignore 0 bucket) and
	 * the capacity of the array has to be 1 larger
	 */
	protected Type[]					heap_array;

	/**
	 * If the user provides a comparator, use it instead of default comparable
	 */
	private Comparator<? super Type>	comparator;
	
	/**
	 * This field is used for timing analysis purposes to record the number
	 * of times an inserted element must actually be moved up through the
	 * heap (numberofBubbleUps++ for every one element, no more).
	 */
	public int							numberOfBubbleUps;

	/**
	 * Constructs an empty priority queue. Orders elements according
	 * to their natural ordering (i.e., AnyType is expected to be Comparable)
	 * 
	 * AnyType is not forced to be Comparable.
	 */
	@SuppressWarnings("unchecked")
	public Heap()
	{
		size = 0;
		comparator = null;
		heap_array = (Type[]) new Object[10];
	}

	/**
	 * Construct an empty priority queue with a specified comparator.
	 * 
	 * Orders elements according to the input Comparator (i.e., AnyType need not be Comparable).
	 * 
	 * @param comparator c
	 */
	public Heap( Comparator<? super Type> c )
	{
		super();
		comparator = c;
	}
	
	/**
	 * Removes and returns the minimum item in this priority queue
	 * ASSUMING THAT THE QUEUE IS A MAXIMUM HEAP.
	 * 
	 * This O(N) dequeue function has been kept to compare timing
	 * with the efficient O(logN) dequeue below.
	 * 
	 * (This is for testing purposes; please disregard.)
	 * 
	 * @throws NoSuchElementException if this priority queue is empty.
	 * (Runs in N time.)
	 */
	public Type inefficientDequeue() throws NoSuchElementException
	{
		
		if(size==0) {
			throw new NoSuchElementException();
		}
		
		int spacesToLook = size/2 - 1;
		
		int indexOfMinimumValue = 0;
		Type minimumValue = heap_array[size];
		
		for(int index=size-1; index>spacesToLook; index--) {
			
			if(this.compare(heap_array[index], minimumValue) < 0) {
				minimumValue = heap_array[index];
				indexOfMinimumValue = index;
			}
			
		}
		
		heap_array[indexOfMinimumValue] = heap_array[size];
		heap_array[size] = null;
		
		size--;
		
		this.bubbleUp(indexOfMinimumValue);
		
		return minimumValue;

	}
	
	/**
	 * Removes and returns the minimum item in this priority queue.
	 * 
	 * @throws NoSuchElementException if this priority queue is empty.
	 * (Runs in logarithmic time.)
	 */
	public Type dequeue() throws NoSuchElementException {
		
		if(size==0) {
			throw new NoSuchElementException();
		}
		
		//Store the minimum element to return.
		Type returnType = heap_array[1];
		
		//Take the most recently added element and place it at the top of the heap.
		heap_array[1] = heap_array[size];

		//Decrement the size and sink the top element down to its proper location.
		size--;
		this.bubbleDown(1);

		return returnType;
		
	}

	/**
	 * Adds an item to this priority queue.
	 * (Runs in logarithmic time.) Can sometimes terminate early.
	 * 
	 * WARNING: make sure you use the compare method defined for you below
	 * 
	 * @param x
	 *            -- the item to be inserted
	 */
	public void add( Type x )
	{
		
		//Resize if necessary.
		if(heap_array.length-1 == size) {
			this.resize();
		}
		
		//Insert at end (size+1).
		heap_array[size+1] = x;
		
		size++;
		
		//Bubble up.
		this.bubbleUp(size);
		
	}
	
	/**
	 * This function resizes the backing store by copying all of the array to a new array of a larger capacity.
	 */
	@SuppressWarnings("unchecked")
	private void resize() {
		
		Type[] bigArray = (Type[]) new Object[heap_array.length*2];
		
		//Copy the array over to the new one exactly.
		for(int index=0; index<heap_array.length; index++) {
			bigArray[index] = heap_array[index];
		}
		
		this.heap_array = bigArray;
		
	}
	
	/**
	 * This function moves a newly added element to its proper location in the priority queue by moving
	 * it up if it is smaller than its parent.
	 * 
	 * @param index, the index to begin bubbling up
	 */
	public void bubbleUp(int index) {
		
		if(size==1) {
			return;
		}
		
		//Mathematical relationship between parent and children.
		int parent = index/(int)2;
		
		//Used for timing analysis; if the element must be moved, set to true.
		boolean bubbledUp = false;
	
		//Move the element up while it is less than its parent element.
		while((this.compare(heap_array[index], heap_array[parent]) < 0)) {
			
			bubbledUp = true;
			
			this.swap(index, parent);
			index = parent;
			
			//If we have reached the top, stop bubbling.
			if(index == 1) {
				return;
			}
			
			parent = index/(int)2;
			
		}
		
		//Increment our bubble counter if necessary.
		if(bubbledUp) {
			this.numberOfBubbleUps++;
		}
		
	}
	
	
	/**
	 * This function moves an element down in the queue if it is greater than its child.
	 *
	 * @param int index, the index to begin bubbling down (1 for top)
	 */
	public void bubbleDown(int index) {
		
		//If there is only one element in the heap, you are finished.
		if(size==1) {
			return;
		}
		
		//Traversing variables.
		int childOne = 0;
		int childTwo = 0;
		int minimumChild = 0;
		
		//While we are still within the bounds of the heap and have a child:
		while(index*2 <= size && heap_array[index*2] != null) {
			
			childOne = index * 2;
			childTwo = childOne + 1;
			
			//Start with the left child.
			if(heap_array[childOne] != null) {
				minimumChild = childOne;
			}
			
			//If there is a right child, compare it with the left to find the minimum value.
			if(childTwo < size && heap_array[childTwo] != null && this.compare(heap_array[childOne], heap_array[childTwo]) > 0) {
					
					minimumChild = childTwo;
			}

			//If the parent is greater than the child, swap; otherwise, we are finished bubbling down.
			if(this.compare(heap_array[index], heap_array[minimumChild]) > 0) {
				
				this.swap(index, minimumChild);
				index = minimumChild;
			
			} else {
				
				return;
			}
			
			//Move the cursor down to the element's new location.
			index = minimumChild;
			
		}
		
	}
	
	/**
	 * This function swaps the data found at the passed indices in the backing store.
	 * 
	 * @param itemOne, the first item to be swapped
	 * @param itemTwo, the second item to be swapped
	 */
	public void swap(int itemOne, int itemTwo) {
		
		Type temp = heap_array[itemTwo];
		
		heap_array[itemTwo] = heap_array[itemOne];
		heap_array[itemOne] = temp;
		
	}
	
	/**
	 * @return the smallest element the queue. 
	 * @throws NoSuchElementException if this priority queue is empty. (Must run in constant time.)
	 */
	public Type peek() {
		
		if(size==0) {
			throw new NoSuchElementException();
		}
		
		return heap_array[1];
		
	}
	
	/**
	 * Makes this priority queue empty. 
	 */
	public void clear() {
		
		for(int index=1; index<heap_array.length; index++) {
			heap_array[index] = null;
		}
		
		this.size = 0;
		
	}
	
	/**
	 * return the total number of elements in the queue
	 */
	public int size() {
		
		return this.size;
		
	}

	/**
	 * Generates a DOT file for visualizing the binary heap.
	 */
	public void generateDotFile( String filename )
	{
		try (PrintWriter out = new PrintWriter(filename))
		{
			out.println(this);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Internal method for comparing lhs and rhs using Comparator if provided by the
	 * user at construction time, or Comparable, if no Comparator was
	 * provided.
	 * 
	 * @param lhs, the first item
	 * @param rhs, the second item
	 */
	@SuppressWarnings("unchecked")
	private int compare( Type lhs, Type rhs )
	{
		if (comparator == null)
		{
			return ((Comparable<? super Type>) lhs).compareTo(rhs); // safe to ignore warning
		}

		// We won't test your code on non-Comparable types if we didn't supply a Comparator
		return comparator.compare(lhs, rhs);
	}

	/**
	 * @return a copy of the array used in the heap
	 */
	public Object[] toArray()
	{
		Object[] copy_of_array = new Object[size];

		for (int i = 1; i <= size; i++)
		{
			copy_of_array[i] = heap_array[i];
		}

		return copy_of_array;
	}

	/**
	 * @return a string representing the DOT data of the heap 
	 * 
	 * This can be further augmented to print out any instrumented values that you think
	 * are important.  Note: To allow them not to conflict with the DOT notation, simply
	 * preface them with the // comment characters: e.g., "// numbers of insertions: 1234"
	 */
	@Override
	public String toString()
	{
		String result = "digraph Heap {\n\tnode [shape=record]\n";
		for (int i = 1; i <= size; i++)
		{
			result += "\tnode" + i + " [label = \"<f0> |<f1> " + heap_array[i] + "|<f2> \"]\n";
			if (((i * 2)) <= size) result += "\tnode" + i + ":f0 -> node" + ((i * 2)) + ":f1\n";
			if (((i * 2) + 1) <= size) result += "\tnode" + i + ":f2 -> node" + ((i * 2) + 1) + ":f1\n";
		}
		result += "}";

		result += "\n//--------------------------------------------\n" + "// Any other info you want to put about your heap";

		return result;
	}

	/**
	 * 1) copy data from array into heap storage
	 * 2) do an "in place" creation of the heap
	 * 
	 * @param array
	 *            - random data (unordered)
	 */
	@SuppressWarnings("unchecked")
	public void build_heap_from_array( Type[] array )
	{
		
		heap_array = (Type[]) new Object[array.length+1];
		
		//Copy the array into the heap array.
		for(int count=0; count<array.length;count++){
			
			heap_array[count+1]= array[count];
			size++;
		}

		//Begin bubbling down from the second to bottom layer.
		for(int index=size/2; index>1; index--) {
			
			this.bubbleDown(index-1);
		}
		
		//Catch possible stragglers.
		this.bubbleUp(size);
		this.bubbleUp(size-1);

	}

	/**
	 * convert the heap array into a sorted array from largest to smallest by shrinking the virtual
	 * array and placing each dequeued item at the next available empty space in the physical array.
	 * 
	 * Note: this destroys the heap property of the array and should be a terminal operation, which
	 *       is not what we would likely do in a real program, but is appropriate to for our purposes (i.e.,
	 *       understanding how heap sort works in place).
	 * 
	 */
	public void heap_sort()
	{
		
		//Dequeue every item and place at the end of the virtual array/beginning of remaining physical array.
		for(int index=0; index<heap_array.length; index++) {
			
			if(heap_array[index] != null) {
				
				Type poppedItem = this.dequeue();
				heap_array[size+1] = poppedItem;
			}
		}	

	}

}
