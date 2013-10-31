package function.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import function.DTExtraction.ExtractorUtil;
import function.util.FileUtil;
import function.util.SetUtil;

/*****�������****/
/******���ܼ��÷�******/
/**
 * @author MJ
 * @Description 1).�����������·��filePath,��Ϊ������ҳ�ĸ�Ŀ¼;
 *              2).crawlPageByUrl()�Ǹ���url��ȡ����crawlPageByUrl("www.baidu.com",
 *              "f:/baidu.html");//ע�⣬���������Ҫ�趨����·������WebCrawler�Ĳ���Ҫ
 *              3).crawlPageByList(termSetPath)��ָ����termSetPath��ȡ���Ｏ�ϣ���ȡ��ʷҳ�棬���
 *              ���浽ͬ·���µ�termSetPath(ȥ����׺)_history����
 */

public class WikiHistoryCrawler_new {
	public String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		File f = new File(filePath);
		f.mkdirs();
	}

	public String crawlPageName = "";// ����������ȡ�����

	/**
	 * ���캯������Ҫ��������Ŀ¼
	 */
	public WikiHistoryCrawler_new() {
		Proxy proxy = new Proxy();
		proxy.setLocalRandomHttpProxy();// ���ô���
	}// WikiHistoryCrawler()
	
	/**
	 * ȥ������
	 */
	public void removeProxy(){
		Proxy proxy=new Proxy();
		proxy.removeLocalHttpProxy();
		System.out.println("ȥ������");
	}
	
	/**
	 * �򿪴���
	 */
	public void openProxy(){
		Proxy proxy=new Proxy();
		proxy.setLocalRandomHttpProxy();//���ô���
		System.out.println("�򿪴���");
	}

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
			System.out.println(url + "\t" + time);
			is = my_url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				sb.append(strTemp + "\r\n");
			}
			System.out.println("get page success from " + url);
			FileWriter fw = new FileWriter(filePath + "\\" + fileName);
			String s = sb.toString();
			fw.write(s);
			fw.flush();
			fw.close();
			br.close();
			resultTag = true;
		} catch (Exception ex) {
			System.err.println("page not found: " + url);
		} finally {
			return resultTag;
		}
	}// crawlPageByUrl()

	/**
	 * ����ָ���б�·����ȡҳ��
	 * 
	 * @param listFilePath
	 */
	public void crawlPageByList(String listFilePath, String savePath,
			int ThreadNumber) {
		int aliveThread = ThreadNumber;
		Vector<Thread> v = new Vector<Thread>();
		ThreadCrawlByList tcl = new ThreadCrawlByList(listFilePath, savePath);
		for (int i = 0; i < ThreadNumber; i++) {
			Thread t = new Thread(tcl);
			v.add(t);
			t.start();
		}
		while (true) {
			aliveThread = 0;
			for (int i = 0; i < ThreadNumber; i++) {
				if (v.get(i).isAlive())
					aliveThread++;
			}
			if (aliveThread == 0)
				break;
		}
		System.out.println("ȫ����ȡ��ϣ�");
	}

	/**
	 * ����ָ���б�·����ȡҳ��
	 * 
	 * @param listFilePath
	 * @param haveCrawlDir
	 *            �Ѿ���ȡ��ҳ���ļ���
	 */
	public void crawlPageByList(String listFilePath, String haveCrawlDir,
			String savePath, int ThreadNumber) {
		ThreadCrawlByList tcl = new ThreadCrawlByList(listFilePath,
				haveCrawlDir, savePath);
		for (int i = 0; i < ThreadNumber; i++) {
			Thread t = new Thread(tcl);
			t.start();
		}
		System.out.println("ȫ����ȡ��ϣ�");

	}
	

	/**
	 * 
	 * @author MJ
	 * @description ���б���ȡ���߳���
	 */
	public class ThreadCrawlByList implements Runnable {

		private Vector<String> vHavenotCrawl = new Vector<String>();
		int size = 0;
		int curId = 1;
		int failedNumber = 0;// ��ȡʧ�ܵ�����
		Proxy proxy = new Proxy();

		public ThreadCrawlByList(String listFilePath, String savePath) {
			Vector<String> vTerm = SetUtil.readSetFromFile(listFilePath);
			Vector<String> vHaveCrawl = new Vector<String>();
			setFilePath(savePath);// ���ñ���·��
			File f = new File(savePath);
			File childs[] = f.listFiles();
			for (int i = 0; i < childs.length; i++) {
				String pageName = childs[i].getName();
				String term = pageName.substring(0, pageName.lastIndexOf("."));
				vHaveCrawl.add(term);
			}
			vHavenotCrawl.addAll(SetUtil.getSubSet(vTerm, vHaveCrawl));
			size = vHavenotCrawl.size();
		}

		public ThreadCrawlByList(String listFilePath, String haveCrawlDir,
				String savePath) {
			Vector<String> vTerm = SetUtil.readSetFromFile(listFilePath);
			Vector<String> vHaveCrawl = new Vector<String>();
			Vector<String> vExistPage = new Vector<String>();
			setFilePath(savePath);// ���ñ���·��
			File f = new File(savePath);
			File childs[] = f.listFiles();
			for (int i = 0; i < childs.length; i++) {
				String pageName = childs[i].getName();
				String term = pageName.substring(0, pageName.lastIndexOf("."));
				vExistPage.add(term);
			}// ����·�����е�
			File fHave = new File(haveCrawlDir);
			File childsHave[] = fHave.listFiles();
			for (int i = 0; i < childsHave.length; i++) {
				String pageName = childsHave[i].getName();
				String term = pageName.substring(0, pageName.lastIndexOf("."));
				vHaveCrawl.add(term);
				if (vTerm.contains(term)) {
					try {
						FileUtil.copyFile(new File(haveCrawlDir + "/"
								+ pageName),
								new File(savePath + "/" + pageName));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}// ����ļ���������ȡ��
			vHavenotCrawl.addAll(SetUtil.getSubSet(
					SetUtil.getSubSet(vTerm, vHaveCrawl), vExistPage));
			size = vTerm.size();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (vHavenotCrawl.size() != 0) {
				String term = "";
				synchronized ("") {
					if (vHavenotCrawl.size() != 0) {
						term = vHavenotCrawl.get(0);
						vHavenotCrawl.removeElementAt(0);
						curId = size - vHavenotCrawl.size();
					}
					if (failedNumber >= 10) {
						failedNumber = 0;
						proxy.setLocalRandomHttpProxy();// �����������10���������ô���
					}
				}
				if (term.length() != 0) {
					String url = "http://en.wikipedia.org/w/index.php?title="
							+ term + "&dir=prev&action=history";
					String fileName = term + ".html";
					boolean b = crawlPageByUrl(url, fileName);
					if (b == false) {
						try {
							failedNumber++;
							System.out.println(fileName + "------"
									+ Thread.currentThread().getName()
									+ "------(" + "��ȡʧ�ܣ�������ӡ���" + ")");
							Thread.currentThread();
							System.out.println(Thread.currentThread().getName()
									+ "�������ߡ���");
							vHavenotCrawl.add(term);
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println(fileName + "------"
								+ Thread.currentThread().getName() + "------("
								+ ExtractorUtil.scanDirPage(getFilePath()).size() + "/" + size + ")");
						failedNumber = 0;// ���ۼӣ�ֻ��������ʧ�ܴ���
					}
				}
			}
		}

	}
}
