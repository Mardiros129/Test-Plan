import java.io.IOException;

import org.json.*;


public class Main {

	public static void main(String[] args) throws JSONException, IOException {
		int dataSize = 1000;
		TestPlan testPlan = new TestPlan(dataSize);
		
		System.out.println(testPlan.Test1());
		System.out.println(testPlan.Test2());
		System.out.println(testPlan.Test3());
		System.out.println(testPlan.Test4());
		System.out.println(testPlan.Test5());
	}
}