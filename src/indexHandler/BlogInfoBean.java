package indexHandler;

public class BlogInfoBean {
	private String title = null;
	private String url = null;
	private String sum = null;
	private String content = null;
	
	BlogInfoBean(String title, String url, String sum, String content) {
		this.title = title;
		this.url = url;
		this.sum = sum;
		this.content = content;
	}
	
	BlogInfoBean() {
		super();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
