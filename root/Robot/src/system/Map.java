package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Map {
	public int roomsCount;
	public HashMap<String ,Tuple> coordinates;
	public ArrayList<Room> rooms;
	public boolean generated = false;
	private static boolean instanceCreated = false;
	
	public Map()
	{
		this.roomsCount = 0;
		rooms = new ArrayList<>();				//current room limit = 10
		if(coordinates==null)
		{
		coordinates = new HashMap<>();
		}
	}
	
	/**
	 *  Returns an instance of the class Map the first time it's called.
	 *  for every subsequent time returns null
	 */
	public static Map getInstance()
	{
		if(!instanceCreated)
		{
		Map mapInstance = new Map();
		instanceCreated = true;
		return mapInstance;
		}
		return null;
	}
	
	/**
	 *  Add a room to map. The number of rooms can be unlimited, as long as it fits inside the mapPainter
	 *  @param x,y room coordinates
	 *  @param w,l room width and length (height in java.swing terms)
	 *  @param dX1,dY1, dX2, dY2 start and end coordinates of a door (for now only one door per room is supported)  
	 */
	public void addRoom(int x, int y, int w, int l, int dX1, int dY1, int dX2, int dY2) 
	{
		if(roomsCount<10)
		{
		
		Room room = new Room(x,y,w,l,dX1,dY1,dX2,dY2);
		room.roomIndex = roomsCount;
		rooms.add(room);
		roomsCount++;
		
		}
	}
	
	/**
	 *  Get a room by index
	 *  if the Room mode has been chosen.
	 */
	public Room getRoom(int index)
	{
		for(Room room : rooms) {
			if(room.roomIndex == index)
			{
				return room;
			}
		}
		return null;
	}
	
	/**
	 *  Get a room which contains the point (x,y)
	 *  @param x, y coordinates that are tested
	 */
	public Room getRoom(int x, int y)
	{
		for(Room room : rooms) {
			if((x>room.x && x<(room.x+room.width)) && (y>room.y && y<(room.y+room.length)))
			{
				return room;
			}
		}
		return null;
	}
	
	/**
	 *  Constructor of class MapPainter.
	 *  @param drawRoomIndexes boolean flag that indicates whether the room indexes should be printed. Is set to true 
	 *  if the Room mode has been chosen.
	 */
	public Tuple lerp(int x1, int y1, int x2, int y2, float t)
	{
		//System.out.println("			lerping between "+ x1 + ","+ y1 + " and " + x2 + "," + y2 );
		int newX = Math.round( ((float)x1) *(1-t)) + Math.round(((float)x2)*t);
		int newY = Math.round( ((float)y1)*(1-t)) + Math.round(((float)y2)*t);
		return new Tuple(newX,newY);
	}
	
	/**
	 *  Creates a hashmap that stores all the valid coordinates of where the robot is allowed to go. By default it's 
	 *  all the points within the rooms + doorways. The coordinates are stored in the app in a form of a hashmap, 
	 *  where the value is the tuple itself (X and y) and the key is it's string representation, whereas the data 
	 *  exported to be used by the robot is of type MapData which only contains an array of tuples.
	 */
	public void resetMapData() 
	{
		for(Room room : rooms)
		{
			int i,j;
			for ( i = room.x+1; i<(room.x+room.width); i++)
			{
				for ( j = room.y+1; j<(room.y+room.length); j++)
				{
					Tuple tuple = new Tuple(i,j);
					coordinates.put(tuple.toString(),tuple);
					//System.out.println("key " + tuple.toString().toString() + " value " + coordinates.get(tuple.toString()).toString());
				}
			}

			Tuple tuple = new Tuple(room.doorX1,room.doorY1 );
			//System.out.println(tuple.toString() + "  new door ");
			
			if(tuple.X >= 0)			//if there is a valid door
			{
			//float length = (float) Math.sqrt((room.doorY2-room.doorY1)*(room.doorY2-room.doorY1) + (room.doorX2-room.doorX1)*(room.doorX2-room.doorX1));

			float cumulative = (float) 0.01;
				while ((tuple.X < room.doorX2)||(tuple.Y < room.doorY2))
				{
					tuple = lerp(room.doorX1,room.doorY1,room.doorX2,room.doorY2,cumulative);
					System.out.println("lerp "+ tuple.toString() + "   at " + cumulative + " %");
					coordinates.put(tuple.toString(),tuple);
					cumulative+=0.005;
				
				}
			}
		}
	}
	
	/**
	 *  returns map data to be exported, and creates a new instance if no such exists at a time of function call.
	 *  Also cleans the coordinates in the process by deleting negative entries.
	 */
	public MapData getMapData()
	{
		if(coordinates.isEmpty())
		{
			System.out.println("The map hasn't been generated yet.");
			System.out.println("Try to generate it in auto mode or load previously saved coordinates.");
		}
		else
		{
		ArrayList<Tuple> coodrs
        = coordinates.values().stream().filter(x -> (x.X > 0)).collect(
            Collectors.toCollection(ArrayList::new));
		MapData mapData = new MapData(coodrs);
		
		return mapData;
		}
		
		return null;
	}
	
	/**
	 *  Deletes valid coordinates, not by removing them from the hashmao, but by rewriting the corresponding entry with negative numbers
	 *  that will be deleted later when exporting the coordinates inside the getMapData() function.
	 */
	public void updateMapData(ArrayList<Tuple> tuples) {
		

		if(coordinates.isEmpty())
		{
			System.out.println("no coordinates yet");
		}
		else
		{
			for (Tuple tuple: tuples)
			{coordinates.put(tuple.toString(),new Tuple(-1,-1));}
		}
	}
	
	public class Room{
		public int x;
		public int y;
		public int width;
		public int length;
		public int roomIndex;
		
		public int doorX1;
		public int doorY1;
		public int doorX2;
		public int doorY2;
		
		public Room(int x, int y, int w, int l, int dX1, int dY1, int dX2, int dY2)	{
			this.x = x;
			this.y = y;
			this.width = w;
			this.length = l;
			this.doorX1 = dX1;
			this.doorY1 = dY1;
			this.doorX2 = dX2;
			this.doorY2 = dY2;
		}
	}
	
	public class MapData
	{
		public ArrayList<Tuple> validCoordinates;
		public MapData(ArrayList<Tuple> tuples) {
			validCoordinates = tuples;
		}
		public ArrayList<Tuple> getCoordinates() {
			return validCoordinates;
		}
	}

	public void setMapData(MapData mapData) {
		ArrayList<Tuple> tuples = mapData.getCoordinates();
		coordinates = null;
		coordinates = new HashMap<>();
		
		for (Tuple tuple : tuples)
		{
			coordinates.put(tuple.toString(), tuple);
		}
		
		System.out.println("Setting loaded coordinates");
	}

	public void deletePoint(int i, int j) {
		Tuple tuple = new Tuple(i,j);
		try {
		//Tuple value = coordinates.get(tuple.toString());
		//System.out.println("trying to delete point " + value.toString() );
		coordinates.put(tuple.toString(), new Tuple(-1,-1));
		}catch (Exception e)
		{
			//System.out.println("no match for the key");
		}
	}
	
	public void printActivePoints()
	{
		System.out.println("print attempt ");
		for(Tuple tuple: coordinates.values())
		{
			 System.out.println("value: " + tuple.toString());
		}
	}
	
	public class Tuple{
	public Integer X;
	public Integer Y;
	public Tuple(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	
	@Override
	public String toString()
	{
		return new String(X + ","+Y);
	}
	
}
}
