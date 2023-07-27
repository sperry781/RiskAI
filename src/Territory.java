
import java.util.Arrays;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Comparator;


public class Territory{
	
	public String name;
	public int id;
	public int continentNum;
	public List<Territory> connectedCountries = new ArrayList<Territory>();
	
	
	public int playerId;
	public int armies;
	public List<Integer> previousOwners = new ArrayList<Integer>();
	
	
	public Territory(String desiredName, int desiredId, int desiredContinentNum)
	{
		name = desiredName;
		id = desiredId;
		continentNum = desiredContinentNum;
	}
	
	public void addConnectedCountry(Territory newCountry)
	{
		connectedCountries.add(newCountry);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getContinentNum()
	{
		return continentNum;
	}
	
	public List<Territory> getConnected()
	{
		return connectedCountries;
	}
	

	
	public int getOwner()
	{
		return playerId;
	}
	
	public int getArmies()
	{
		return armies;
	}
	
	public List<Integer> getPreviousOwners()
	{
		return previousOwners;
	}
	
	
	
	public void setOwner(int newPlayerId)
	{
		if(!previousOwners.contains(playerId))
			previousOwners.add(playerId);
		playerId = newPlayerId;
	}
	
	public void setArmies(int newArmies)
	{
		armies=newArmies;
	}
}