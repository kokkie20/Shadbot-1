package me.shadorc.shadbot.data.fishing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.data.DataManager;
import me.shadorc.shadbot.utils.LogUtils;
import sx.blah.discord.handle.obj.IUser;

public class DatabaseFishing {

	private static List<IUser> users;
	private static JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	public static void AddItem(String Filename, String KEY, long Total)
			throws FileNotFoundException, IOException, ParseException {
		Object obj;
		JSONObject jsonObject;
		obj = parser.parse(new FileReader(DataManager.FISH_DIR + "/" + Filename + ".json"));
		jsonObject = (JSONObject) obj;

		try {
			jsonObject.remove(KEY);
			jsonObject.put(KEY, Total);
			SaveDB(jsonObject, Filename);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtils.error(e, "An error occurred while adding an item.");
		}
	}

	public static void RemoveItem(String Filename, String KEY) throws FileNotFoundException, IOException, ParseException {
		Object obj;
		JSONObject jsonObject;
		obj = parser.parse(new FileReader(DataManager.FISH_DIR + "/" + Filename + ".json"));
		jsonObject = (JSONObject) obj;

		try {
			jsonObject.remove(KEY);
			jsonObject.put(KEY, 0);
			SaveDB(jsonObject, Filename);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtils.error(e, "An error occurred while removing an item.");
		}
	}

	public static void SaveDB(JSONObject obj, String Filename) throws IOException {
		File FILE;
		FILE = new File(DataManager.FISH_DIR, Filename + ".json");
		if (FILE.exists()) {
			try (FileWriter writer = new FileWriter(FILE)) {
				writer.write(obj.toJSONString());
			}
		}
	}

	public static void CreateUserFishingDB(Context context) throws JSONException, IOException {
		File FILE;
		String UserID = context.getAuthor().getStringID();
			FILE = new File(DataManager.FISH_DIR, UserID.toString().substring(3, 21) + ".json");
			if (!FILE.exists()) {
				try (FileWriter writer = new FileWriter(FILE)) {
					writer.write(writeFishJson().toJSONString());
				}
			}
		}

	public static Boolean UserExist(String Filename) throws JSONException, IOException {

		File FILE = new File(DataManager.FISH_DIR, "/" + Filename + ".json");
		if (!FILE.exists()) {
			return false;
		} else {
			return true;
		}
	}

	public static void CreateAllUserFishingDB(Context context) throws JSONException, IOException {
		File FILE;
		users = getUsers(context);
		for (int i = 0; i < users.size(); i++) {
			FILE = new File(DataManager.FISH_DIR, users.get(i).toString().substring(3, 21) + ".json");
			if (!FILE.exists()) {
				try (FileWriter writer = new FileWriter(FILE)) {
					writer.write(writeFishJson().toJSONString());
				}
			}
		}
	}

	public static List<IUser> getUsers(Context context) {
		return context.getGuild().getUsers();
	}

	public static JSONObject getJsonObjectInformation(String Filename)
			throws FileNotFoundException, IOException, ParseException {
		Object obj;
		JSONObject jsonObject;
		obj = parser.parse(new FileReader(DataManager.FISH_DIR + "/" + Filename + ".json"));
		jsonObject = (JSONObject) obj;
		return jsonObject;

	}

	public static JSONObject writeFishJson() {
		JSONObject fishObj = new JSONObject();
		fishObj.put("FISHING_POLE", 0);
		fishObj.put("BOOT", 0);
		fishObj.put("FROG", 0);
		fishObj.put("POOP", 0);
		fishObj.put("SANDAL", 0);
		fishObj.put("CRAB", 0);
		fishObj.put("OCTOPUS", 0);
		fishObj.put("TURTLE", 0);
		fishObj.put("FISH", 0);
		fishObj.put("BLOWFISH", 0);
		fishObj.put("DOLPHIN", 0);
		fishObj.put("WHALE", 0);
		fishObj.put("CROCODILE", 0);
		fishObj.put("SHARK", 0);
		fishObj.put("SQUID", 0);
		fishObj.put("TREASURE", 0);
		fishObj.put("SHRIMP", 0);
		fishObj.put("SHELL", 0);

		return fishObj;
	}
}
