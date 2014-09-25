import java.util.ArrayList;
import java.util.Hashtable;


public class AnswerSet 
{
	public static final String NO_CATEGORY = "NC";
	public static ArrayList<String> hiddenKeys = new ArrayList<String>();;

	private String name;
	private Hashtable<String, ArrayList<String>> keys;

	public AnswerSet(String name)
	{
		this.name = name;
		keys = new Hashtable<String, ArrayList<String>>();
		addKey(NO_CATEGORY);
	}

	public void addKey(String key)
	{
		System.out.println("Added key: " + key);
		ArrayList<String> list = new ArrayList<String>();

		keys.put(key, list);
	}

	public void addToList(String key, String stringToAdd)
	{
		System.out.println("Adding to list with key " + key);
		keys.get(key).add(stringToAdd);
	}

	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		String fiveSpaces = "     ";
		ArrayList<String> currList;
		String outputString = "";

		outputString += name + "\n";

		for(String key : keys.keySet())
		{
			if(!hiddenKeys.contains(key))
			{
				currList = keys.get(key);

				for(int i = 0; i < currList.size(); i++)
				{
					if(i % 2 == 0)
						outputString += currList.get(i) + fiveSpaces;
					else
						outputString += currList.get(i) + "\n";

					if(i == currList.size() - 1)
						outputString += "\n\n";
				}
			}
		}

		return outputString;
	}

	public static void resetHiddenKeys()
	{
		hiddenKeys.clear();
	}

	public static void addToHiddenKeys(String s)
	{
		hiddenKeys.add(s);
	}
}
