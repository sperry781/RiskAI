
import java.util.Arrays;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Comparator;


public class Player{
	public int playerId;
	
	
	public int armyCount;
	public double warmongerScore;
	
	public double[] grievances = new double[4];
	
	
	public int[] borderingArmyCount = new int[4];
	public int[] continentCount = new int[6];
	public double[] continentPercentage = new double[6];
	
	public List<Territory> territories = new ArrayList<Territory>();
	
	
	public Player(int dplayerId) {
		playerId = dplayerId;
		grievances[0]=1.0;
		grievances[1]=1.0;
		grievances[2]=1.0;
		grievances[3]=1.0;
	}
	
	
	public double getWarmongerScore()
	{
		calculateWarmongerScore();
		return warmongerScore;
	}
	
	public int getId()
	{
		return playerId;
	}
	
	
	public double getGrievances(int pid)
	{
		return grievances[pid];
	}
	
	public void setGrievances(int pid, double newVal)
	{
		grievances[pid]= newVal;
	}
	
	//The warmonger score between this player and the player in question, takes into account grievances(conquest history) and the percentage of armies the player in question has bordering this player
	public double getWeightedWarmongerScore(int pid, double pWarmongerScore, int pArmies)
	{
		calculateBorderingArmies();
		return grievances[pid]*pWarmongerScore*(borderingArmyCount[pid]/pArmies);
	}
	
	
	
	public List<Territory> getTerritories()
	{
		return territories;
	}
	
	public Territory getTerritory(int tid)
	{
		for(Territory i : territories)
		{
			if(i.getId()==tid)
				return i;
		}
		return new Territory("error", 100, 100);
	}
	
	public Territory getTerritory(String tName)
	{
		for(Territory i : territories)
		{
			if(i.getName().equals(tName))
				return i;
		}
		return new Territory("error", 100, 100);
	}
	
	public boolean doesPlayerHaveTerritory(String tName)
	{
		for(Territory i : territories)
		{
			if(i.getName().equals(tName))
			{
				return true;
			}
		}
		return false;
	}
	
	public void addTerritory(Territory newTerritory)
	{
		territories.add(newTerritory);
	}
	
	public void removeTerritory(Territory oldTerritory)
	{
		territories.remove(oldTerritory);
	}
	
	
	public double getPercentage(int continent)
	{
		calculateContinentPercentage();
		return continentPercentage[continent];
	}
	
	public double getContinentCount(int continent)
	{
		return continentCount[continent];
	}
	
	
	public double calculateWarmongerScore()
	{
		double percentageCaptured = calculateCapturedTerritories()*0.0/territories.size();
		double owned = 0;
		double armies = calculateArmyCount();
		calculateContinentPercentage();
		for(int i =0; i<6; i++)
		{
			owned+=continentPercentage[i];
		}
		warmongerScore = percentageCaptured*owned*armies;
		return warmongerScore;
	}
	
	
	public int calculateArmyCount()
	{
		int armies = 0;
		for(Territory i : territories)
		{
			armies+=i.getArmies();
		}
		armyCount=armies;
		return armyCount;
	}
	
	public int calculateCapturedTerritories()
	{
		int tCount = 0;
		for(Territory i : territories)
		{
			if(!i.getPreviousOwners().isEmpty())
				if(i.getPreviousOwners().get(0)!=playerId)
					tCount++;
		}
		return tCount;
	}
	
	public int[] calculateContinentCount()
	{
		int[] cCount = {0,0,0,0,0,0};
		
		for(Territory i : territories)
		{
			cCount[i.getContinentNum()]++;
		}
		continentCount=cCount;
		return cCount;
	}
	
	public double[] calculateContinentPercentage()
	{
		int[] pCount = calculateContinentCount();
		int[] cCount = {9,4,7,6,12,4};
		double[] cPercentage = {0.0,0.0,0.0,0.0,0.0,0.0};
		
		for(int i = 0; i<6; i++)
		{
			double a = pCount[0] * 1.0 / cCount[0];
			cPercentage[i]=a;
		}
		continentPercentage=cPercentage;
		
		return cPercentage;
	}
	
	public int[] calculateBorderingArmies()
	{
		int[] bCount = {0,0,0,0};
		
		List<Territory> visited = new ArrayList<Territory>();
		
		for(Territory i : territories)
		{
			for(Territory j : i.getConnected())
			{
				if(j.getOwner()!=playerId&&!visited.contains(j))
				{
					visited.add(j);
					bCount[j.getOwner()]+=j.getArmies();
				}
			}
		}
		borderingArmyCount=bCount;
		return bCount;
	}
	
	public Territory mostThreatenedTerritory()
	{
		Territory threatened = territories.get(0);
		int threatenedCount=0;
		Territory temp;
		int tempCount = 0;
		List<Territory> visited = new ArrayList<Territory>();
		calculateContinentPercentage();
		
		for(Territory i : territories)
		{
			for(Territory j : i.getConnected())
			{
				if(j.getId()!=playerId)
				{
					tempCount+=j.getArmies();
				}
			}
			tempCount=tempCount-i.getArmies();
			
			//Threatened territories are weighted by the army count of neighboring territories that don't belong to the player and the percentage of the continent that the player owns, territories that belong to continents mostly owned by the player and with high army counts from other players on their border are weighted higher
			if(tempCount*continentPercentage[i.getContinentNum()]>threatenedCount*continentPercentage[threatened.getContinentNum()])
			{
				threatenedCount=tempCount;
				threatened=i;
			}
			tempCount=0;
			
		}
		return threatened;
	}
	
	public Territory nextDesiredConquest()
	{
		Territory next = territories.get(0).getConnected().get(0);
		List<Territory> visited = new ArrayList<Territory>();
		calculateContinentPercentage();
		
		for(Territory i : territories)
		{
			for(Territory j : i.getConnected())
			{
				if(!visited.contains(j))
				{
					//Neighboring territories are weighted by the army count on the territory and how much of the rest of the continent the player owns, neighboring territories with a low army count that are part of a continent with the majority owned by the player are weighted heavily
					if(j.getArmies()/continentPercentage[j.getContinentNum()]<next.getArmies()/continentPercentage[next.getContinentNum()])
					{
						next=j;
					}
					visited.add(j);
				}
			}
		}
		return next;
	}
	
	public String territoryList()
	{
		StringBuilder boardString = new StringBuilder();
		for(Territory i : territories)
		{
			boardString.append(i.getName());
			boardString.append(" with ");
			boardString.append(String.format("%2d ", i.getArmies()));
            boardString.append(" armies\n");
		}
		return boardString.toString();
	}
}