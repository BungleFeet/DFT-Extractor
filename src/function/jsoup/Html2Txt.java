package function.jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Html2Txt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Html2Txt ht = new Html2Txt();
		// parseHtmlString����
		String html = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		String text = ht.parseHtmlString(html);
		System.out.println(text);
		// parseBodyString����
		String bodyStr = "<div><p>Lorem ipsum.</p>";
		String bodyText = ht.parseBodyString(bodyStr);
		System.out.println(bodyText);
		// parseURLText����
		String url = "http://en.wikipedia.org/wiki/Data_mining";
		String urlText = ht.parseURLText(url);
		System.out.println(urlText);
		// parseURLHtml����
		String urlHtml = ht.parseURLHtml(url);
		System.out.println(urlHtml);
		//parsePathText����
		String path="f:/AVL_tree.html";
		String pathText= ht.parsePathText(path);
		System.out.println(pathText);
		//parseDirText����
		String dir="f:/DM_html";
		ht.parseDirText(dir);

	}

	/**
	 * 
	 * @param htmlStr
	 * @return ������htmlStr���������ı�
	 */
	public String parseHtmlString(String htmlStr) {
		Document doc = Jsoup.parse(htmlStr);
		String text = doc.body().text();
		return text;
	}

	/**
	 * 
	 * @param bodyStr
	 * @return �Ӹ�����bodyStr���������ı�
	 */
	public String parseBodyString(String bodyStr) {
		Document doc = Jsoup.parseBodyFragment(bodyStr);
		String text = doc.body().text();
		return text;
	}

	/**
	 * 
	 * @param url
	 * @return �Ӹ�����url���������ı�
	 */
	public String parseURLText(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla")
					.cookie("auth", "token").timeout(20000).post();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String text = doc.body().text();
		return text;
	}
	
	/**
	 * 
	 * @param url
	 * @return �Ӹ�����url���html����
	 */
	public String parseURLHtml(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla")
					.cookie("auth", "token").timeout(20000).post();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = doc.body().html();
		return html;
	}
	
	/**
	 * ����ָ��·����html�ļ�
	 * @param path
	 */
	public String parsePathText(String path) {
			Document doc = null;
			try {
				File input = new File(path);
				doc = Jsoup.parse(input, "UTF-8", "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String text = doc.body().text();
			return text;
	}
	
	/**
	 * ����ָ��Ŀ¼��html�ļ������������ŵ�dir-txtĿ¼����
	 * @param dir
	 */
	public void parseDirText(String dir) {
		String desDir="";
		if(dir.endsWith("/"))
			desDir=dir.substring(0, dir.length()-1)+"_txt/";
		else
			desDir=dir+"_txt/";
		File desFile=new File(desDir);
		desFile.mkdirs();
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName().replace(".html", ".txt");
			Document doc = null;
			try {
				doc = Jsoup.parse(childs[i], "UTF-8", "");
				String text = doc.body().text();
				FileWriter fw=new FileWriter(desDir+fileName);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write(text);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
