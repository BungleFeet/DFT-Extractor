package function.jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author MJ
 * @description ���ά���ٿƵ�html2txt
 */
public class WikiHtml2Txt {

	private Vector<String> vWikiTag = new Vector<String>();
	private Vector<String> vDivId = new Vector<String>();
	private Vector<String> vDivClass = new Vector<String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WikiHtml2Txt ht = new WikiHtml2Txt();
		/*
		 * // parseHtmlString���� String html =
		 * "<html><head><title>First parse</title></head>" +
		 * "<body><p>Parsed HTML into a doc.</p></body></html>"; String text =
		 * ht.parseHtmlString(html); System.out.println(text); //
		 * parseBodyString���� String bodyStr = "<div><p>Lorem ipsum.</p>"; String
		 * bodyText = ht.parseBodyString(bodyStr); System.out.println(bodyText);
		 * // parseURLText���� String url =
		 * "http://en.wikipedia.org/wiki/Data_mining"; String urlText =
		 * ht.parseURLText(url); System.out.println(urlText); // parseURLHtml����
		 * String urlHtml = ht.parseURLHtml(url); System.out.println(urlHtml);
		 */
		// parsePathText����
		/*String path = "f:/Data_mining.html";
		String pathText = ht.parsePathText(path);
		System.out.println(pathText);*/
		
		//parseDirText���� 
		String dir="f:/DM_html"; 
		ht.parseDirText(dir);
		 

	}

	/**
	 * ���캯��
	 */
	public WikiHtml2Txt() {
		// vWikiTag
		vWikiTag.add("p");
		vWikiTag.add("h1");
		vWikiTag.add("h2");
		vWikiTag.add("h3");
		vWikiTag.add("h4");
		// vDivId
		vDivId.add("mw-normal-catlinks");// Ŀ¼
		vDivId.add("contentSub");// �ض���
		// vDivClass
		vDivClass.add("rellink relarticle mainarticle");// Algorithmsҳ��������
		vDivClass.add("rellink boilerplate seealso");// Algorithmsҳ��������
		vDivClass.add("rellink boilerplate further");// Algorithmsҳ��������
		vDivClass.add("dablink");// Data_miningҳ��������
		vDivClass.add("rellink");// Data_miningҳ��������
	}

	/**
	 * 
	 * @param htmlStr
	 * @return ������htmlStr���������ı�
	 */
	public String parseHtmlString(String htmlStr) {
		Document doc = Jsoup.parse(htmlStr);
		String text = parseWikiHtml(doc);
		return text;
	}

	/**
	 * 
	 * @param bodyStr
	 * @return �Ӹ�����bodyStr���������ı�
	 */
	public String parseBodyString(String bodyStr) {
		Document doc = Jsoup.parseBodyFragment(bodyStr);
		String text = parseWikiHtml(doc);
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
		String text = parseWikiHtml(doc);
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
	 * 
	 * @param path
	 */
	public String parsePathText(String path) {
		Document doc = null;
		String result = "";
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = parseWikiHtml(doc);
		return result;
	}

	/**
	 * ����ָ��Ŀ¼��html�ļ������������ŵ�dir-txtĿ¼����
	 * 
	 * @param dir
	 */
	public void parseDirText(String dir) {
		String desDir = "";
		if (dir.endsWith("/"))
			desDir = dir.substring(0, dir.length() - 1) + "_txt/";
		else
			desDir = dir + "_txt/";
		File desFile = new File(desDir);
		desFile.mkdirs();
		File f = new File(dir);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			String fileName = childs[i].getName().replace(".html", ".txt");
			System.out.println((i+1)+"/"+childs.length);
			Document doc = null;
			try {
				doc = Jsoup.parse(childs[i], "UTF-8", "");
				String text = parseWikiHtml(doc);
				FileWriter fw = new FileWriter(desDir + fileName);
				BufferedWriter bw = new BufferedWriter(fw);
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

	/**
	 * ����ά����ҳ
	 * 
	 * @param doc
	 * @return
	 */
	public String parseWikiHtml(Document doc) {
		String result = "";
		Elements es = doc.body().getAllElements();
		for (int i = 0; i < es.size(); i++) {
			Element e = es.get(i);
			String addText="";
			if (vWikiTag.contains(e.tagName()) || vDivId.contains(e.id()) || vDivClass
					.contains(e.className()))
				addText= e.text();
			else if (e.tagName().equals("li")) {
				addText= pureLiText(e.html());
			}
			else if (e.tagName().equals("dl")) {
				addText= pureLiText(e.html());
			}
			if(addText.length()>1)
				result+=addText + "\n";
			if (e.id().equals("mw-normal-catlinks"))
				break;// ������Ŀ¼��ֹͣ
		}
		return result;
	}

	/**
	 * ȥ��li��ǩ��Ƕ��li��ǩ�������ظ�
	 * 
	 * @param html
	 * @return
	 */
	public String pureLiText(String html) {
		Document doc = Jsoup.parseBodyFragment(html);
		Elements es = doc.body().getAllElements();
		for (int i = 0; i < es.size(); i++) {
			Element e = es.get(i);
			if (e.tagName().equals("li"))
				e.text("");
		}
		return doc.body().text();
	}
}
