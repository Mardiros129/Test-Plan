import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


public class TestPlan {

	int dataSize;
	ArrayList<JSONObject> weaponList;
	
	
	public TestPlan(int dataSize) throws JSONException, IOException {
		this.dataSize = dataSize;
		weaponList = new ArrayList<JSONObject>();
		
		for (int i = 0; i < dataSize; i++) {
			JSONObject item = getObject(i);
			if (item.has("weaponInfo")) {
				weaponList.add(item);
			}
		}
	}
	
	
	private JSONObject getObject(Integer index) throws IOException, JSONException {
		JSONObject itemData;
		
		// Connect to the url and setup input stream
		URL url = new URL("http://us.battle.net/api/wow/item/" + index.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try {
			InputStream stream = new BufferedInputStream(conn.getInputStream());
			
			// Read from the input stream and build the JSON object
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			itemData = new JSONObject(builder.toString());
		}
		catch (FileNotFoundException e) {
			itemData = new JSONObject();
		}
		
		conn.disconnect();
		return itemData;
	}
	
	
	private long round(double value, int place) {
		value = value * Math.pow(10, place);
		long newValue = Math.round(value);
		newValue = (long) (newValue / Math.pow(10, place));
		return newValue;
	}
	
	
	/* After retrieving a given Item with weapon data (i.e. has a “weaponInfo” object), 
	 * verify that the “weaponInfo” object contains a “dps” value, a “weaponSpeed” value, 
	 * and a “damage” object; also verify that the “weaponInfo” object contains exactly 3 
	 * values. 
	 */
	public boolean Test1() throws JSONException {
		boolean testResults = true;
		
		for (int i = 0; i < weaponList.size(); i++) {
			JSONObject weaponInfo = weaponList.get(i).getJSONObject("weaponInfo");
			if (!weaponInfo.has("dps"))
				testResults = false;
			if (!weaponInfo.has("weaponSpeed"))
				testResults = false;
			if (!weaponInfo.has("damage"))
				testResults = false;
			if (weaponInfo.length() > 3)
				testResults = false;
		}
		
		return testResults;
	}

	
	/* After retrieving a given Item with weapon data, verify that the “damage” object 
	 * contains an “exactMin” value, an “exactMax” value, a “min” value, and a “max” 
	 * value; also verify that the “damage” object contains exactly 4 values.
	 */
	public boolean Test2() throws JSONException {
		boolean testResults = true;
		
		for (int i = 0; i < weaponList.size(); i++) {
			JSONObject damage = weaponList.get(i).getJSONObject("weaponInfo").getJSONObject("damage");
			if (!damage.has("min"))
				testResults = false;
			if (!damage.has("max"))
				testResults = false;
			if (!damage.has("exactMin"))
				testResults = false;
			if (!damage.has("exactMax"))
				testResults = false;
			if (damage.length() > 4)
				testResults = false;
		}
		
		return testResults;
	}

	
	/* After retrieving a given Item with weapon data, verify that the “min” value equals 
	 * the “exactMin” value rounded.
	 */
	public boolean Test3() throws JSONException {
		boolean testResults = true;
		
		for (int i = 0; i < weaponList.size(); i++) {
			JSONObject damage = weaponList.get(i).getJSONObject("weaponInfo").getJSONObject("damage");
			
			if (damage.getInt("min") != (int)damage.getDouble("exactMin"))
				testResults = false;
		}
		
		return testResults;	
	}
	
	
	/* After retrieving a given Item with weapon data, verify that the “max” value equals 
	 * the “exactMax” value rounded.
	 */
	public boolean Test4() throws JSONException {
		boolean testResults = true;
		
		for (int i = 0; i < weaponList.size(); i++) {
			JSONObject damage = weaponList.get(i).getJSONObject("weaponInfo").getJSONObject("damage");
			
			if (damage.getInt("max") != (int)damage.getDouble("exactMax"))
				testResults = false;
		}
		
		return testResults;	
	}
	
	
	/* After retrieving a given Item with weapon data, verify that the product of “dps” 
	 * and “weaponSpeed” equal the average of  “exactMax” and “exactMin” (rounded to the 
	 * nearest hundredth place).
	 */
	public boolean Test5() throws JSONException {
		boolean testResults = true;
		
		for (int i = 0; i < weaponList.size(); i++) {
			JSONObject weaponInfo = weaponList.get(i).getJSONObject("weaponInfo");
			JSONObject damage = weaponInfo.getJSONObject("damage");
			
			long damageValue = round(weaponInfo.getDouble("dps") * weaponInfo.getDouble("weaponSpeed"), 2);
			long minmaxAvg = round((damage.getDouble("exactMin") + damage.getDouble("exactMax")) / 2, 2);
			
			if (damageValue != minmaxAvg) {
				System.out.println(weaponList.get(i).get("id"));
				testResults = false;
			}
		}
		
		return testResults;
	}

}
