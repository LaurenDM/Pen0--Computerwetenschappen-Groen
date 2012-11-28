package domain.maze.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedPathSet implements Iterable<MazePath> {

	private ArrayList<MazePath> sortedQueue;
	
	public SortedPathSet(MazePath startingPath){
		sortedQueue = new ArrayList<MazePath>();
		sortedQueue.add(startingPath);
	}
	
	public void expand(){
		MazePath expansionPath = first();
		remove(expansionPath);
		addAll(expansionPath.expand());
		trim();
	}
	
	public boolean firstPathReachesGoal(){
		return first().contains(first().getGoalTile());
	}
	
	private void trim(){
		SortedPathSet clone = this.clone();
		Iterator<MazePath> it = this.iterator();
		while(it.hasNext()){
			MazePath path = it.next();
			for(MazePath otherPath:clone){
				if(otherPath.contains(path.getCurrentEndTile()) && path.getCurrentLength()>otherPath.getCurrentLength()){
					System.out.println("Trimmed path "+it);
					it.remove();
				}
			}
		}
	}
	
	@Override
	public SortedPathSet clone(){
		MazePath dummyPath = new MazePath(new TileNode(null,null), new TileNode(null,null));
		SortedPathSet clone = new SortedPathSet(dummyPath);
		clone.remove(dummyPath);
		clone.addAll(sortedQueue);
		return clone;
	}
	
	public boolean add(MazePath arg0) {
		boolean ret = false;
		if(contains(arg0)){
		} else {
			int i=0;
			while(i<sortedQueue.size() && arg0.getFScore()<=sortedQueue.get(i).getFScore()) {
				i++;
			}
			ret = sortedQueue.add(arg0);
		}
		return ret;
	}

	public boolean addAll(Collection<? extends MazePath> arg0) {
		boolean ret = true;
		for(MazePath path : arg0){
			ret&=add(path);
		}
		return ret;
	}

	public void clear() {
		sortedQueue.clear();
	}

	public boolean contains(Object arg0) {
		return sortedQueue.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		return sortedQueue.containsAll(arg0);
	}

	public boolean isEmpty() {
		return sortedQueue.isEmpty();
	}

	@Override
	public Iterator<MazePath> iterator() {
		return sortedQueue.iterator();
	}

	//removing externally is only possible through the iterator
	private boolean remove(Object arg0) {
		boolean ret = sortedQueue.remove(arg0);
		return ret;
	}

	public int size() {
		return sortedQueue.size();
	}

	public MazePath first() {
		return sortedQueue.get(0);
	}

	public MazePath last() {
		return sortedQueue.get(size()-1);
	}
	
	@Override
	public String toString(){
		String ret = "";
		for(MazePath path: sortedQueue){
			ret+=path.toString()+", ";
		}
		return ret.substring(0, ret.length()-2);
	}

}