package cs2420;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class HeapTest {
	
	

//	 // sample simple test you can use
////    @Test
////    public void test_basic_insertion()
////    {
////            Heap<Integer> heap = new Heap<>();
////            
////            heap.add( 5 );
////            heap.add( 6 );
////            heap.add( 3 );
////            heap.add( 7 );
////            heap.add( 8 );
////            heap.add( 1 );
////            
////            assertEquals(6, heap.size());
//////            
//////            Object[] temp = heap.toArray();
//////            
//////            assertArrayEquals(new Integer[]{null, 1,6,3,7,8,5}, temp);
////
////            // if you want to look at your heap, uncomment this line to generate a graph file,
////            //
////            //       heap.generateDotFile("Documents/test_heap.dot");
////            //
////            // or uncomment this line, run the tests:
////            //
////                   //System.out.println(heap);
////            //
////            // and then paste the output of the console into: http://www.webgraphviz.com/
////    }
////    
////    @Test
////    public void findMinTest(){
////    	 
////    	Heap<Integer> heap = new Heap<>();
////        
////        heap.add( 5 );
////        heap.add( -1 );
////        heap.add( 3 );
////        heap.add( 0 );
////        heap.add( 8 );
////        heap.add( 2 );
////        
////        System.out.println(heap);
////
////        
////        assertEquals(-1, (int)heap.peek());
////    	
////    }
////    
////    @Test
////    public void stringTest(){
////    	 
////    	Heap<String> heap = new Heap<>();
////        
////        heap.add( "dog" );
////        heap.add( "purple" );
////        heap.add( "beans" );
////        heap.add( "waterbottle" );
////        heap.add( "a" );
////        heap.add( "an" );
////        
////        System.out.println(heap);
////
////        assertEquals("a", (String)heap.peek());
////    	
////    }
////    
////    @Test
////    public void clearTest(){
////    	 
////    	Heap<Integer> heap = new Heap<>();
////        
////        heap.add( 5 );
////        heap.add( -1 );
////        heap.add( 3 );
////        heap.add( 0 );
////        heap.add( 8 );
////        heap.add( 2 );
////        
////        System.out.println(heap);
////
////        heap.clear();
////        
////        System.out.println(heap);
////        
////        assertEquals(0, heap.size());
////    	
////    }
//    
//    @Test
//    public void dequeue(){
//    	 
//    	Heap<Integer> heap = new Heap<>();
//        
//        heap.add( 4 );
//        heap.add( -1 );
//        heap.add( 3 );
//        heap.add( 0 );
//        heap.add( 8 );
//        heap.add( 6 );
//        
//        System.out.println(heap);
//
//        assertEquals(-1, (int)heap.dequeue());
//        
//        System.out.println(heap);
//        
//        assertEquals(5, heap.size());
//    	
//    }


    // sample advanced test you might want to implement
    @Test
    public void test_lots_of_insertions_deletions_peeks()
    {
            Heap<Integer> heap = new Heap<>();
            
            final int COUNT = 10;
            Random generator = new Random();
            
            // add COUNT elements to HEAP
            for(int num=0; num<COUNT; num++){
            	heap.add(Math.abs(generator.nextInt(50)));
            }

            assertEquals(COUNT, heap.size());
            
            System.out.println(heap);
            
            int smallest = heap.dequeue();
            
            // while the heap has elements
            // remove one, make sure it is larger than smallest, update smallest 
            while(heap.size()>0){
            	int lastSmallest = smallest;
            	smallest = heap.dequeue();
            	System.out.println(heap);
            	
            	boolean flag = smallest >= lastSmallest;
            	assertTrue(smallest>= lastSmallest);
            }
    }

}
