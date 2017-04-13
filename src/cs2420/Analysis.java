package cs2420;

import java.util.Random;

/**
 * This class contains timing code for the analysis of the priority
 * queue data structure. It both times insertion and deletion calls
 * and counts how many times the bubbleUp function must move an
 * inserted element.
 * 
 * @author Kylee Fluckiger & Chloe Josien
 */
public class Analysis {

	public static void main(String[] args) {
		
		//Repetition loop for insertion.
		for(int numberOfElements = 10000; numberOfElements<= 300000; numberOfElements += 10000) {
			
			Heap<Integer> heap = new Heap<>();
			Random generator = new Random();
			
			long startInsertion = System.nanoTime();

			//Add all of the elements.
			for(int index=0; index<numberOfElements; index++) {
				heap.add(generator.nextInt(5000));
			}
			
			long totalInsertion = (System.nanoTime() - startInsertion) / numberOfElements;
						
			System.out.println("Stats for " + numberOfElements + " (insertion, bubble ups)");
			System.out.println(totalInsertion);
			System.out.println(heap.numberOfBubbleUps);
			
		}
		
		//Separate loop for the dequeue function.
		for(int numberOfElements=50000; numberOfElements<=1000000; numberOfElements+=50000) {
			
			Heap<Integer> heap = new Heap<>();
			Random generator = new Random();
			
			long startDeletion = 0;
			long totalDeletion = 0;
			
			for(int averagingCount=0; averagingCount<25; averagingCount++) {
				
				//Add all of the elements.
				for(int index=0; index<numberOfElements; index++) {
					heap.add(generator.nextInt(5000));
				}
				
				startDeletion = System.nanoTime();
				
				//Delete all of the elements.
				for(int index=0; index<numberOfElements; index++) {
					heap.dequeue();
				}
				
				heap.clear();
				
				totalDeletion += (System.nanoTime() - startDeletion) / numberOfElements;
				
			}
			
			//To average out all of the averaging runs.
			totalDeletion /= 25;
			
			System.out.println("Stats for " + numberOfElements);
			System.out.println(totalDeletion);

		}

	}
}
