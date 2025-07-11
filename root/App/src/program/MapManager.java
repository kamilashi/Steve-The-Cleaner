package program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;

import program.Map.MapData;
import program.Map.Room;

public abstract class MapManager {

	private static Map currentMap;

	public static Map getMap() {
		return currentMap;
	}

	public static void storeMap(Map newMap) throws IOException {
		currentMap = newMap;
	}

	public static void saveSettings() throws IOException {

		String directory = Paths.get("maps").toAbsolutePath().toString();
		File pathAsFile = new File(directory);

		if (!Files.exists(Paths.get(directory))) {
			System.out.println(directory + "\" will be created.");
			pathAsFile.mkdir();
		}

		Path filePath = Paths.get("maps/map.json");
		System.out.println("saving into " + filePath.toAbsolutePath());

		String jsonObject = new Gson().toJson(currentMap.getMapData());
		FileWriter file = new FileWriter(filePath.toAbsolutePath().toString());

		try {
			file.write(jsonObject);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadSettings() throws IOException {
		Path filePath = Paths.get("maps/map.json");
		try {
			System.out.println("loading from " + filePath.toAbsolutePath());
			String jsonObject = new String(Files.readAllBytes(filePath.toAbsolutePath()));
			MapData mapData = new Gson().fromJson(jsonObject, MapData.class);
			currentMap.setMapData(mapData);
		} catch (Exception e) {
			System.out.println("no saved maps yet" + filePath.toAbsolutePath());
		}
	}

	public static void updateRegionSettings(int x, int y, int brushSize) {

		int i;
		int j;
		int halfSize = (brushSize / 2);
		for (i = (x - halfSize); i < (x + halfSize); i++) {
			for (j = (y - halfSize); j < (y + halfSize); j++) {
				currentMap.deletePoint(i, j);
			}
		}
	}

	public static void updateRoomSettings(int x, int y) {
		Room room = currentMap.getRoom(x, y);

		if (room != null) {
			int i;
			int j;
			for (i = room.x; i < (room.x + room.width); i++) {
				for (j = room.y; j < (room.y + room.length); j++) {
					currentMap.deletePoint(i, j);
				}
			}
		}

	}

	public static void resetMap() {
		currentMap.resetMapData();
	}

	public static void printMap() {
		currentMap.printActivePoints();
	}

}
