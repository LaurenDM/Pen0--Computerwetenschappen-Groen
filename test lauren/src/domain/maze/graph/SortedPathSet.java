package domain.maze.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import domain.maze.Orientation;

public class SortedPathSet implements Iterable<MazePath> {

	private ArrayList<MazePath> sortedQueue;
	
	public SortedPathSet(MazePath startingPath){
		this();
		sortedQueue.add(startingPath);
	}
	
	public SortedPathSet() {
		sortedQueue = new ArrayList<MazePath>();
	}

	public void expand(){
		MazePath expansionPath = first();
		remove(expansionPath);
		addAll(expansionPath.expand());
		trim();
	}
	
	public boolean firstPathReachesGoal(){
		return first()!=null?first().contains(first().getGoalTile()):true;
	}
	
	private void trim(){
		Iterator<MazePath> it = this.iterator();
		ArrayList<MazePath> toBeTrimmed = new ArrayList<MazePath>();
		HashMap<MazeNode,MazePath> checkMap = new HashMap<MazeNode,MazePath>();
		while(it.hasNext()){
			MazePath current = it.next();
			if(checkMap.containsKey(current.getCurrentEndTile())){
				MazePath shortestFromMap = checkMap.get(current.getCurrentEndTile());
				if(shortestFromMap.getCurrentCost()<current.getCurrentCost()){
					toBeTrimmed.add(current);
				} else if(shortestFromMap.getCurrentCost()>current.getCurrentCost()) {
					toBeTrimmed.add(shortestFromMap);
					checkMap.put(current.getCurrentEndTile(), current);
				}
			} else {
				checkMap.put(current.getCurrentEndTile(), current);
			}
		}		
		
//		SortedPathSet clone = this.clone();
//		Iterator<MazePath> it = this.iterator();
//		Iterator<MazePath> otherIt = clone.iterator();
//		ArrayList<MazePath> toBeTrimmed = new ArrayList<MazePath>();
//		while(it.hasNext()){
//			MazePath path = it.next();
//			while(otherIt.hasNext()){
//				MazePath otherPath = otherIt.next();
//				if(otherPath.contains(path.getCurrentEndTile()) && path.getCurrentLength()>otherPath.getCurrentLength()){
//					System.out.println("Trimmed path "+path);
//					toBeTrimmed.add(path);
//				}
//			}
//		}
		
		for(MazePath m : toBeTrimmed){
			remove(m);
		}
	}
	
	@Override
	public SortedPathSet clone(){
		MazePath dummyPath = new MazePath(new TileNode(null,null), Orientation.NORTH, new TileNode(null,null));
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

	/**
	 * Using the iterator to remove paths is preferred.
	 */
	public boolean remove(Object arg0) {
		return sortedQueue.remove(arg0);
	}

	public int size() {
		return sortedQueue.size();
	}

	public MazePath first() {
		return sortedQueue.size()!=0?sortedQueue.get(0):null;
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