package comp2402a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.PriorityQueue;


public class Part7 {
	
	/**
	 * Your code goes here - see Part0 for an example
	 * @param r the reader to read from
	 * @param w the writer to write to
	 * @throws IOException
	 */
	public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
        // Your code goes here - see Part0 for an example;  
        int l_size = 1000;
        int counter = 0;
        PriorityQueue<String> large = new PriorityQueue<String>(String::compareTo);
        PriorityQueue<String> small = new PriorityQueue<String>(large.comparator().reversed());
        for (String line = r.readLine(); line != null; line = r.readLine()) {
        	if(counter < l_size) {
        		small.add(line);
        	}else if(counter < l_size*2) {
        		small.add(line);
        		String largest_in_small = small.poll();
        		large.add(largest_in_small);
        	}else {
        		
            	if (line.compareTo(small.peek())>0 && line.compareTo(large.peek())<0) {
            		w.println(line);
            	}
            	if (line.compareTo(small.peek())<0) {
            		small.add(line);
            		small.poll();    	
            	}else if(line.compareTo(large.peek())>0) {
            		large.add(line);
            		large.poll();
            	}	
        	}
        	counter ++;
        }
	}

	/**
	 * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
	 * and System.out or from filenames specified on the command line, then call doIt.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r;
			PrintWriter w;
			if (args.length == 0) {
				r = new BufferedReader(new InputStreamReader(System.in));
				w = new PrintWriter(System.out);
			} else if (args.length == 1) {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(System.out);				
			} else {
				r = new BufferedReader(new FileReader(args[0]));
				w = new PrintWriter(new FileWriter(args[1]));
			}
			long start = System.nanoTime();
			doIt(r, w);
			w.flush();
			long stop = System.nanoTime();
			System.out.println("Execution time: " + 10e-9 * (stop-start));
		} catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
}
