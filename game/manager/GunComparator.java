package game.manager;

import game.common.Constants;

import java.util.Comparator;
import java.util.HashMap;

public class GunComparator implements Comparator<String>{

	private static final HashMap<String, Integer> orderMap;
	private static final int SMALLER = -1, EQUAL = 0, GREATER = 1;
	
	static{
		orderMap = new HashMap<String, Integer>();
		orderMap.put(Constants.PISTOL, 1);
		orderMap.put(Constants.MACHINE_GUN, 2);
		orderMap.put(Constants.SHOTGUN, 3);
		orderMap.put(Constants.WAVE_BEAM, 4);
		orderMap.put(Constants.ROCKET_LAUNCHER, 5);
		orderMap.put(Constants.CHAIN_GUN, 6);
		orderMap.put(Constants.POISON_GUN, 7);
	}
	
	public int compare(String left, String right) {
		int leftInt = orderMap.get(left);
		int rightInt = orderMap.get(right);
		
		if(leftInt > rightInt)
			return GREATER;
		if(leftInt < rightInt)
			return SMALLER;
		if(leftInt == rightInt)
			return EQUAL;
		
		return EQUAL;
	}

}
