package searchApp;

//import indexHandler.IndexManager;

import indexHandler.FriendListManager;
import indexHandler.IndexManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class SearchManager {
	private String searchWord;
//	private IndexManager indexManager;
	private Analyzer analyzer;
	private IndexSearcher indexSearcher = null;
	private List<SearchResultBean> searchResult = null;
	private FriendListManager friendListManager = null;
	private List<Integer> friendList = null;
	
	public SearchManager(String searchWord) {
		this.searchWord = searchWord;
//		this.indexManager = new IndexManager();
		this.analyzer = new PaodingAnalyzer(); //using paoding Chinese analyzer
//		this.analyzer = new StandardAnalyzer(Version.LUCENE_35);
		this.friendListManager = new FriendListManager();
	}
	
	public List<SearchResultBean> search() {
		searchResult = new ArrayList<SearchResultBean>();
		friendList = friendListManager.getFriendList();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", analyzer);
		Query query = null;
		try {
			query = queryParser.parse(searchWord);
		} catch(ParseException e) {
			e.printStackTrace();
		}
		for(Integer friendId : friendList) {
			File dir = new File(IndexManager.getIndexDir() + friendId.toString());
			try {
				indexSearcher = new IndexSearcher(IndexReader.open(FSDirectory.open(dir)));
			} catch(IOException e) {
				e.printStackTrace();
			}
			if(indexSearcher != null && query != null) {
				try {
					TopDocs hits = indexSearcher.search(query, 10);	//to be revised on multi-pages
					for(int i = 0; i < hits.totalHits; i++) {
						SearchResultBean resultBean = new SearchResultBean();
						resultBean.setTitle(indexSearcher.doc(i).get("title"));
						resultBean.setUrl(indexSearcher.doc(i).get("url"));
						resultBean.setSum(indexSearcher.doc(i).get("sum"));
						searchResult.add(resultBean);
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return searchResult;
	}
}
