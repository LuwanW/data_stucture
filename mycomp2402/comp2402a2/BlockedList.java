package comp2402a2;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import comp2402a2.ArrayDeque;
/**
 * @author luwan
 *
 * @param <T> the type of objects stored in the List
 */
public class BlockedList<T> extends AbstractList<T> {
	/**
	 * keeps track of the class of objects we store
	 */
	Factory<T> f;

	/**
	 * The number of elements stored
	 */
	int n;

	/**
	 * The block size
	 */
	int b;

    /**
	 * Holds our stuff
	 */
	ArrayDeque<ArrayDeque<T>> blocks;
	ArrayDeque<T> block1, block2;
	/**
	 * Constructor
	 * @param t the type of objects that are stored in this list
	 * @param b the block size
	 */
  @SuppressWarnings("unchecked")
	public BlockedList(Class<T> t, int b) {
		this.b = b;
		f = new Factory<T>(t);
		n = 0;
		this.b = b;
		blocks = new ArrayDeque<ArrayDeque<T>>((Class<ArrayDeque<T>>)(new ArrayDeque<T>(t)).getClass());
		block1 = new ArrayDeque<T>(t);
		block2 = new ArrayDeque<T>(t);
		blocks.add(block2);
		blocks.add(block1);

	}

	public int size() {
		return n;
	}

	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		
		ArrayDeque<T> first_block = blocks.get(0);
		//if i is in the first block
		if (i < first_block.size()) {
			return first_block.get(i);
		}
		//if i not in the first block
		int processed_index = i - first_block.size();
		int outter_index = (processed_index)/this.b+1;
		int inner_index = processed_index%this.b;
		return blocks.get(outter_index).get(inner_index);
	}

	public T set(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		ArrayDeque<T> first_block = blocks.get(0);
		//if i is in the first block
		if (i < first_block.size()) {
			return first_block.set(i, x);
		}
		//if i not in the first block
		int processed_index = i - first_block.size();
		int outter_index = (processed_index)/this.b+1;
		int inner_index = processed_index%this.b;
		
		return blocks.get(outter_index).set(inner_index, x);
	}

	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		// find index
		if (blocks.size() == 0) {
			ArrayDeque<T> newblock = new ArrayDeque<T>(f.type());
			newblock.add(x);
			blocks.add(newblock);
			
		}else {
			ArrayDeque<T> first_block = blocks.get(0);
			ArrayDeque<T> last_block = blocks.get(blocks.size()-1);
			int outter_index;
			int inner_index;
			if (i <= first_block.size()) {
			// first block
				outter_index = 0;
				inner_index = i;
				
			}
			else{
				//other blocks
				int processed_index = i - first_block.size();		
				outter_index = (processed_index)/this.b+1;
				inner_index = processed_index%this.b;
			}
			
			// add 	
			ArrayDeque<T> target;
			if (outter_index >= blocks.size()) {
				outter_index = outter_index -1;
				target = blocks.get(outter_index);
				target.add(x);
			}	
			else{
				target = blocks.get(outter_index);
				target.add(inner_index,x);
			}
	
			if (target.size() > b) {
				// shift
				if (i < n/2) {
					//shift left
					while(outter_index != 0) {
						ArrayDeque<T> current_block = blocks.get(outter_index);
						// add this last element to previous first element
						blocks.get(outter_index-1).add(blocks.get(outter_index-1).size(),current_block.get(0));
						current_block.remove(0);
						// todo: need to resize???
						outter_index--;
					}
					// if first block is full, add new block to front
	
					if (blocks.get(0).size() > b) {
						ArrayDeque<T> newblock = new ArrayDeque<T>(f.type());
						// add element to first
						ArrayDeque<T> old_first_block = blocks.get(0);
						newblock.add(0,old_first_block.get(0));
						old_first_block.remove(0);
						blocks.add(0,newblock);
					}
				}else {
					//shift right
					if (i >= n/2) {
						//shift left
						while(outter_index != blocks.size()-1) {
							ArrayDeque<T> current_block = blocks.get(outter_index);
							// add this last element to previous first element
							blocks.get(outter_index+1).add(0,current_block.get(b));
							current_block.remove(b);
							// todo: need to resize???
							outter_index++;
						}
						// if last block is full, add new block to the end
						if (blocks.get(blocks.size()-1).size() > b) {
							ArrayDeque<T> newblock = new ArrayDeque<T>(f.type());
							// add element to first
							ArrayDeque<T> old_last_block = blocks.get(blocks.size()-1);
							newblock.add(0,old_last_block.get(b));
							old_last_block.remove(b);
							blocks.add(blocks.size(),newblock);
						}
					
					}
				
				}
			}
		}
		n++;
	}

	public T remove(int i) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		// find index
		ArrayDeque<T> first_block = blocks.get(0);
		ArrayDeque<T> last_block = blocks.get(blocks.size()-1);
		int outter_index;
		int inner_index;
		if (i < first_block.size()) {
		// first block
			outter_index = 0;
			inner_index = i;
		}
		else{
			//other blocks
			int processed_index = i - first_block.size();		
			outter_index = (processed_index)/this.b+1;
			inner_index = processed_index%this.b;
		}
		
		// remove 
		ArrayDeque<T> target = blocks.get(outter_index);
		T return_target = target.get(inner_index);
		target.remove(inner_index);
		if (target.size() < b) {
			// shift
			if (i < n/2) {


				//shift right
				while(outter_index != 0) {
					ArrayDeque<T> current_block = blocks.get(outter_index);
					// add previous last element to this block
					ArrayDeque<T> previous_block = blocks.get(outter_index-1);
					current_block.add(0,previous_block.get(previous_block.size()-1));
					previous_block.remove(previous_block.size()-1);
					outter_index--;
				}
				// if first block is empty, remove first
				if (blocks.get(0).size() == 0) {
					blocks.remove(0);
				}

			}else {
				//shift left
				if (i >= n/2) {
					while(outter_index != blocks.size()-1) {
						ArrayDeque<T> current_block = blocks.get(outter_index);
						// add previous last element to this block

						ArrayDeque<T> after_block = blocks.get(outter_index+1);
						current_block.add(current_block.size(),after_block.get(0));
						after_block.remove(0);
						outter_index++;
					}
					// if first block is full, add new block to front
					if (blocks.get(blocks.size()-1).size() == 0) {
						blocks.remove(blocks.size()-1);
					}
				
				}
			
			}
		}
		n--;
		return return_target;

	}

	public static void main(String[] args) {

	}

}
