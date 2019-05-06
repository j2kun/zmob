package game.manager;

import game.Managed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Manager<E extends Managed> {

	private List<E> toRemove;
	
	public abstract Collection<E> getCollection();
	public abstract void updateIndividual(E individual);
	
	public void update(){
		toRemove = new ArrayList<E>();
		
		for(E item : getCollection()){
			item.update();
			updateIndividual(item);
			
			if(item.shouldRemove())
				toRemove.add(item);
		}
		
		safeRemove();
	}
	
	public void safeRemove(){
		getCollection().removeAll(toRemove);
	}
}
