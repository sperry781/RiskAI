public class Personality{
	
	public double diplomacy;
	public double aggression;
	public double defensive;
	
	
	public Personality(double ddiplomacy, double daggression, double ddefensive) {
		diplomacy = ddiplomacy;
		aggression = daggression;
		defensive = ddefensive;
	}
	
	public double getDiplomacy()
	{
		return diplomacy;
	}
	
	public double getAggression()
	{
		return aggression;
	}
	
	public double getDefensive()
	{
		return defensive;
	}
	
	public String personalityList()
	{
		StringBuilder boardString = new StringBuilder();
		boardString.append(String.format("%1$,.2f", getDiplomacy()));
		boardString.append("\n");
		boardString.append(String.format("%1$,.2f", getAggression()));
        boardString.append("\n");
		boardString.append(String.format("%1$,.2f", getDefensive()));
		return boardString.toString();
	}
}