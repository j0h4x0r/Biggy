package indexHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {
	private final static String blogListUrlToImplement = "http://blog.renren.com/blog/";
	private final static String blogListUrl = "http://blog.renren.com/GetBlog.do?id=";
	int uid;
	Document doc = null;
	Document contentDoc = null;
	List<BlogInfoBean> blogInfoList = new ArrayList<BlogInfoBean>();
	DefaultHttpClient httpclient = null;

	PageParser(int uid, DefaultHttpClient httpclient) {
		this.uid = uid;
		this.httpclient = httpclient;
		HttpGet httpget = new HttpGet(blogListUrl + uid);
		try {
			HttpResponse response = httpclient.execute(httpget);
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));
			StringBuffer docString = new StringBuffer();
			String line = null;
			while ((line = bf.readLine()) != null)
				docString.append(line);
			bf.close();
			doc = Jsoup.parse(docString.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
		}
	}

	public List<BlogInfoBean> getblogsInfo() {
		String totalStr = doc.getElementsByClass("total").get(0)
				.getElementsByTag("strong").get(0).getElementsByTag("a").get(0)
				.text().split("ƪ")[0];
		int total = Integer.valueOf(totalStr).intValue();
		int pageNum = (total % 10 == 0) ? (total / 10) : (total / 10 + 1);
		for (int i = 0; i < pageNum; i++) {
			String listUrl = blogListUrlToImplement + uid + "/friends?curpage="
					+ i;
			HttpGet httpget = new HttpGet(listUrl);
			try {
				HttpResponse response = httpclient.execute(httpget);
				BufferedReader bf = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent(), "utf-8"));
				StringBuffer docString = new StringBuffer();
				String line = null;
				while ((line = bf.readLine()) != null)
					docString.append(line);
				bf.close();
				doc = Jsoup.parse(docString.toString());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				httpget.abort();
			}

			Elements originInfo = doc.select("div.list-blog");
			String url = null;
			String title = null;
			for (Element blogItem : originInfo) {
				Elements inputs = blogItem.getElementsByTag("input");
				for (Element input : inputs) {
					if (input.attr("name").equals("link"))
						url = input.attr("value");
					if (input.attr("name").equals("title"))
						title = input.attr("value");
				}
				Element summary = blogItem.select("div.text-article").get(0);
				String sum = summary.text();
				String content = getFullContent(url);
				blogInfoList.add(new BlogInfoBean(title, url, sum, content));
			}
		}
		return blogInfoList;
	}

	public String getFullContent(String url) {
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpget);
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));
			StringBuffer docString = new StringBuffer();
			String line = null;
			while ((line = bf.readLine()) != null)
				docString.append(line);
			bf.close();
			contentDoc = Jsoup.parse(docString.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
		}

		Element contentElement = contentDoc.select("div.text-article").get(0);
		String content = contentElement.text();
		return content;
	}
}
