package indexHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexManager {
	private final static String indexDir = Thread.currentThread()
	.getContextClassLoader().getResource("").getPath().substring(1)
	+ "../../indexDir/";
	DefaultHttpClient httpclient = null;

	IndexManager(DefaultHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public boolean createIndex(int friendId) throws IOException {
		File dir = new File(indexDir + friendId);
		if (!dir.exists()) {
			dir.mkdirs();
			Directory fsDirectory = FSDirectory.open(dir);
			Analyzer analyzer = new PaodingAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
					Version.LUCENE_35, analyzer);
			IndexWriter indexWriter = new IndexWriter(fsDirectory,
					indexWriterConfig);

			PageParser parser = new PageParser(friendId, httpclient);
			List<BlogInfoBean> blogsInfo = parser.getblogsInfo();
			for (BlogInfoBean item : blogsInfo)
				addDocument(item, indexWriter);
			indexWriter.close();
			return true;
		}
		return false;
	}

	public void addDocument(BlogInfoBean blogInfo, IndexWriter indexWriter) {
		String title = blogInfo.getTitle();
		String url = blogInfo.getUrl();
		String sum = blogInfo.getSum();
		String content = blogInfo.getContent();

		Document document = new Document();
		document.add(new Field("title", title, Field.Store.YES,
				Field.Index.ANALYZED));
		document.add(new Field("url", url, Field.Store.YES, Field.Index.NO));
		document.add(new Field("sum", sum, Field.Store.YES, Field.Index.NO));
		document.add(new Field("content", content, Field.Store.NO,
				Field.Index.ANALYZED));
		try {
			indexWriter.addDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getIndexDir() {
		return indexDir;
	}
}
