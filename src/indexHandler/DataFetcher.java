package indexHandler;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

public class DataFetcher {

	private List<Integer> friendList = null;
	private IndexManager indexManager = null;
	FriendListManager friendListManager = null;

	public DataFetcher(DefaultHttpClient httpclient) {
		indexManager = new IndexManager(httpclient);
		friendListManager = new FriendListManager();
	}

	public void start() {
		friendList = friendListManager.getFriendList();
		for (Integer friend : friendList) {
			try {
				indexManager.createIndex(friend.intValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
