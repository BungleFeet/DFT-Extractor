package function.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import function.util.SetUtil;

/*****�������****/
/******���ܼ��÷�******/
/**
 * @author MJ
 * @Description 1).�����������·��filePath,��Ϊ������ҳ�ĸ�Ŀ¼;
 *              2).crawlPageByUrl()�Ǹ���url��ȡ����crawlPageByUrl("www.baidu.com",
 *              "f:/baidu.html");//ע�⣬���������Ҫ�趨����·������WebCrawler�Ĳ���Ҫ
 *              3).crawlPageByList(termSetPath)��ָ����termSetPath��ȡ���Ｏ�ϣ���ȡ��ʷҳ�棬���
 *               ���浽ͬ·���µ�termSetPath(ȥ����׺)_history����
 */

public class WikiHistoryCrawler {
	public String crawlPageName="";//����������ȡ�����
	/**
	 * ���캯������Ҫ��������Ŀ¼
	 */
	public WikiHistoryCrawler() {
	}// WikiHistoryCrawler()

	/**
	 * ���ղ���ָ����url��ȡ���ڶ��������Ǵ�ŵ��ļ���
	 * 
	 * @param url
	 * @param fileName
	 */
	@SuppressWarnings("finally")
	public boolean crawlPageByUrl(String url, String fileName) {
		boolean resultTag = false;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		InputStream is = null;
		try {
			URL my_url = new URL(url);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = dateFormat.format(new Date());
			System.out.println(url + time);
			is = my_url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				sb.append(strTemp + "\r\n");
			}
			System.out.println("get page success from " + url);
			FileWriter fw = new FileWriter(fileName);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
			br.close();
			resultTag = true;
			crawlPageName=fileName;
		} catch (Exception ex) {
			System.err.println("page not found: " + url);
		} finally {
			return resultTag;
		}
	}// crawlPageByUrl()

	/**
	 * 
	 * @param termSetPath ָ������ȡ�����ļ�·��
	 * 
	 */
	public void crawlPageByList(String termSetPath) {
		String filePath="";
		if(termSetPath.contains("\\"))
			filePath=termSetPath.substring(0, termSetPath.lastIndexOf("."))+"_history";
		else
			filePath=termSetPath.substring(0, termSetPath.lastIndexOf("."))+"_history";
		File f=new File(filePath);
		f.mkdirs();
		Vector<String> v=SetUtil.readSetFromFile(termSetPath);
		v.retainAll(getHavenotCrawlUrlSet(v,filePath));//��ȡû����ȡ�ļ���
		int sum = v.size();
		int n = 1;
		while (!v.isEmpty()) {
			HashSet<String> hsCrawled = new HashSet<String>();//�����Ѿ���ȡ����ҳ�漯��
			String crawlUrl = getUrlByTerm(v.get(0));// ��ȡ��ַ
			String term0 = v.get(0);// termName
			String fileName=term0+"_historyE.html";
			if (!hsCrawled.contains(crawlUrl)) {
				String pathFileName = filePath + "/" + fileName;
				if (crawlPageByUrl(crawlUrl, pathFileName)) {
					hsCrawled.add(fileName);// �����ȡ�ɹ����t��ӵ�����ȡ�б���
					v.remove(0);
					System.out.println(pathFileName + "------(" + (n++) + "/"
							+ sum + ")");
				} else {// ��ȡʧ��
					v.remove(0);
					v.add(term0);
					System.out.println("wait����");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				System.out.println(fileName + "------(" + (n++) + "/" + sum
						+ ")--------(Crawled)");
			}
		}// end while
		System.out.println("Crawled success!");
	}

	/**
	 * ͨ������������url��ַ
	 * 
	 * @param termName
	 * @return
	 */
	public String getUrlByTerm(String termName) {
		//String fileNameSuffix = ".html";
		String prefix = "http://en.wikipedia.org/w/index.php?title=";
		String suffix="&dir=prev&action=history";
		//String prefix = "http://en.wikipedia.org/wiki/";
		//String suffix="";
		/*String pureName = fileName.substring(0,
				fileName.length() - fileNameSuffix.length());*/
		return prefix + termName+suffix;
	}

	/**
	 * 
	 * @param v �ܵļ���
	 * @param savePath ����ȡ���ϵı���·��
	 * @return δ��ȡ�ļ���
	 */
	public Vector<String> getHavenotCrawlUrlSet(Vector<String> v,String savePath) {
		File f = new File(savePath);
		File childs[] = f.listFiles();
		Vector<String> haveCrawlSet = new Vector<String>();
		Vector<String> havenotCrawlSet = new Vector<String>();
		for (int j = 0; j < childs.length; j++) {
			haveCrawlSet.add(childs[j].getName());
		}
		for (String s : v) {
			String temp=s+"_historyE.html";//��ʱ��ӵĹ���
			if (!haveCrawlSet.contains(temp) && !havenotCrawlSet.contains(temp)) {
				havenotCrawlSet.add(s);
			}
		}
		return havenotCrawlSet;
	}
}
