package indexHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String userName = null;
	private static String password = null;
	private static String redirectURL = "http://www.renren.com/home";
	private static String renrenLoginURL = "http://www.renren.com/PLogin.do";

	private HttpResponse response;
	private DefaultHttpClient httpclient = new DefaultHttpClient();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		userName = request.getParameter("email");
		password = request.getParameter("password");
		if (login()) {
			response.sendRedirect("search.jsp");
		} else {
			response.sendRedirect("index.jsp");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	private boolean login() {
		HttpPost httpost = new HttpPost(renrenLoginURL);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("origURL", redirectURL));
		nvps.add(new BasicNameValuePair("domain", "renren.com"));
		nvps.add(new BasicNameValuePair("isplogin", "true"));
		nvps.add(new BasicNameValuePair("formName", ""));
		nvps.add(new BasicNameValuePair("method", ""));
		nvps.add(new BasicNameValuePair("submit", "µÇÂ¼"));
		nvps.add(new BasicNameValuePair("email", userName));
		nvps.add(new BasicNameValuePair("password", password));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			httpost.abort();
		}

		String redirectLocation = response.getFirstHeader("Location")
				.getValue();
		HttpGet httpget = new HttpGet(redirectLocation);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		// String responseBody = "";
		try {
			/* responseBody = */httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			// responseBody = null;
		} finally {
			httpget.abort();
		}

		@SuppressWarnings("unused")
		FriendListManager friendListManager = new FriendListManager(httpclient);
		DataFetcher dataFetcher = new DataFetcher(httpclient);
		dataFetcher.start();

		return true;
	}
}
