package domain.maze.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedPathSet implements SortedSet<MazePath>, Iterable<MazePath> {

	private TreeSet<MazePath> sortedSet;
	
	public SortedPathSet(MazePath startingPath){
		sortedSet = new TreeSet<MazePath>(new Comparator<MazePath>(){
			public int compare(MazePath path1, MazePath path2){
				return path1.compareTo(path2)*Math.abs(path1.hashCode()-path2.hashCode());
			}
		});
		sortedSet.add(startingPath);
	}
	
	public void expand(){
		MazePath expansionPath = sortedSet.first();
		sortedSet.remove(expansionPath);
		sortedSet.addAll(expansionPath.expand());
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
		clone.addAll(sortedSet);
		return clone;
	}
	
	@Override
	public boolean add(MazePath arg0) {
		return sortedSet.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends MazePath> arg0) {
		return sortedSet.addAll(arg0);
	}

	@Override
	public void clear() {
		sortedSet.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return sortedSet.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return sortedSet.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return sortedSet.isEmpty();
	}

	@Override
	public Iterator<MazePath> iterator() {
		return sortedSet.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return sortedSet.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return sortedSet.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return sortedSet.retainAll(arg0);
	}

	@Override
	public int size() {
		return sortedSet.size();
	}

	@Override
	public Object[] toArray() {
		return sortedSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return sortedSet.toArray(arg0);
	}

	@Override
	public Comparator<? super MazePath> comparator() {
		return sortedSet.comparator();
	}

	@Override
	public MazePath first() {
		return sortedSet.first();
	}

	@Override
	public SortedSet<MazePath> headSet(MazePath arg0) {
		return sortedSet.headSet(arg0);
	}

	@Override
	public MazePath last() {
		return sortedSet.last();
	}

	@Override
	public SortedSet<MazePath> subSet(MazePath arg0, MazePath arg1) {
		return sortedSet.subSet(arg0, arg1);
	}

	@Override
	public SortedSet<MazePath> tailSet(MazePath arg0) {
		return sortedSet.tailSet(arg0);
	}
	
	@Override
	public String toString(){
		String ret = "";
		for(MazePath path: sortedSet){
			ret+=path.toString()+", ";
		}
		return ret.substring(0, ret.length()-2);
	}

}
