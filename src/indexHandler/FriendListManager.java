package indexHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FriendListManager {
	private List<Integer> friendList = null;
	private static final String friendListDir = Thread.currentThread()
			.getContextClassLoader().getResource("").getPath().substring(1)
			+ "../../friendListDir/";
	private static String friendRec = "friendRec0.dat";
	private DefaultHttpClient httpclient = null;
	private String friendListURL = "http://friend.renren.com/myfriendlistx.do";
	private HttpGet httpget = null;

	public FriendListManager(DefaultHttpClient httpclient) {
		friendList = new ArrayList<Integer>();
		this.httpclient = httpclient;
		httpget = new HttpGet(friendListURL);
		recordFriendList();
	}

	public FriendListManager() {
		friendList = new ArrayList<Integer>();
	}

	private void recordFriendList() {
		FileOutputStream stream = null;
		PrintWriter writer = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));
			StringBuffer doc = new StringBuffer();
			String line = null;
			while ((line = bf.readLine()) != null)
				doc.append(line);
			bf.close();
			String jsonText = extractJson(doc.toString());
			JSONArray friends = (JSONArray) JSONValue.parse(jsonText);
			for (Object friendInfo : friends) {
				String id = ((JSONObject) friendInfo).get("id").toString();
				friendList.add(Integer.valueOf(id));
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			httpget.abort();
		}

		try {
			File rec = new File(friendListDir + friendRec);
			if (rec.exists()) {
				int index = Integer.valueOf(friendRec.substring(9, 10))
						.intValue();
				index++;
				friendRec = "friendRec" + index + ".dat";
			}
			stream = new FileOutputStream(friendListDir + friendRec);
			writer = new PrintWriter(stream);
			for (Integer friend : friendList) {
				writer.println(friend.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String extractJson(String doc) {
		String jsonText = null;
		int i = doc.indexOf("var friends=");
		jsonText = doc.substring(i).split("[=;]")[1];
		return jsonText;
	}

	public List<Integer> getFriendList() {
		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader bfReader = null;
		try {
			stream = new FileInputStream(friendListDir + friendRec);
			reader = new InputStreamReader(stream);
			bfReader = new BufferedReader(reader);
			String buff;
			try {
				while ((buff = bfReader.readLine()) != null) {
					friendList.add(Integer.valueOf(buff));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bfReader.close();
				reader.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return friendList;
	}
}
