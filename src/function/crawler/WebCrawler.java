package function.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import function.DTExtraction.ExtractorUtil;
import function.DTExtraction.WikiHrefProcess;
import function.util.FileUtil;
import function.util.SetUtil;

/*****�������****/
/******���ܼ��÷�******/
/**
 * @author MJ
 * @Description 1).�����������·��filePath,��Ϊ������ҳ�ĸ�Ŀ¼;
 *              2).crawlPageByUrl()�Ǹ���url��ȡ����crawlPageByUrl("www.baidu.com",
 *              "baidu.html");
 *              3).crawlPageByLayer()�ǰ��ճ����Ӱ�����ȡ����crawlPageByLayer(
 *              "http://en.wikipedia.org/wiki/Volcanoes_of_Java"
 *              ,������������Ȱ���������������Լ��趨�£�һ�������ӹ���һ������������ һ���Ǹ����ļ����������ӵ�ַ����
 *              4).crawlPageByList()����ָ���б���ȡ
 */

public class WebCrawler {
	public String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		File f = new File(filePath);
		f.mkdirs();
	}

	/**
	 * ���캯������Ҫ��������Ŀ¼
	 */
	public WebCrawler() {
		Proxy proxy = new Proxy();
		proxy.setLocalRandomHttpProxy();// ���ô���
		//proxy.setLocalHttpProxy("127.0.0.1", "8087"); //����goagent����
	}// WebCrawler()

	/**
	 * ȥ������
	 */
	public void removeProxy() {
		Proxy proxy = new Proxy();
		proxy.removeLocalHttpProxy();
		System.out.println("ȥ������");
	}

	/**
	 * �򿪴���
	 */
	public void openProxy() {
		Proxy proxy = new Proxy();
		proxy.setLocalRandomHttpProxy();// ���ô���
		System.out.println("�򿪴���");
	}

	/**
	 * ���ָ��Ŀ¼��ҳ���������
	 * 
	 * @param dir
	 */
	public void checkDirCompleteness(String dir, int ThreadNumber) {
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int j=0;j<childs.length;j++){
			if(childs[j].isDirectory())
				checkDirCompleteness(childs[j].getAbsolutePath(),10);
			else{
				break;
			}
		}
		Vector<String> vList = FileUtil.getDirFileSet(dir);
		int aliveThread = ThreadNumber;
		Vector<Thread> v = new Vector<Thread>();
		ThreadCheckByList tcl = new ThreadCheckByList(vList, dir);
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
		System.out.println("ȫ�������ϣ�");
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
	 * ���̰߳������ȡ
	 * 
	 * @param url
	 * @param layer
	 * @param savePath
	 * @param ThreadNumber
	 */
	public void crawlPageByLayer(String url, int layer, String savePath,
			int ThreadNumber) {
		WikiHrefProcess whp = new WikiHrefProcess();
		for (int i = 1; i <= layer; i++) {
			System.out.println("��ȡ��" + i + "�㡭��");
			// ����ÿ��ҳ�汣����ļ���
			String layerPagePath = savePath + "/layer" + i;
			File fLayerPage = new File(layerPagePath);
			fLayerPage.mkdirs();
			// ���ɲ�������б�
			Vector<String> vTerm = new Vector<String>();
			if (i == 1)
				vTerm.add(url.substring(url.lastIndexOf("/") + 1, url.length()));
			else {
				String lastLayerPagePath = savePath + "/layer" + (i - 1);// ��һ��
				File fLastLayerPage = new File(lastLayerPagePath);
				File childsLastLayer[] = fLastLayerPage.listFiles();
				for (int j = 0; j < childsLastLayer.length; j++) {
					vTerm.addAll(whp.getWikiTermFromFile(childsLastLayer[j]
							.getAbsolutePath()));
				}
			}
			String layerListPath = savePath + "/layer" + i + ".txt";
			SetUtil.writeSetToFile(vTerm, layerListPath);
			// ��ȡ
			crawlPageByList(layerListPath, layerPagePath, ThreadNumber);
		}
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

		Proxy proxy=new Proxy();

		public ThreadCrawlByList(String listFilePath, String savePath) {
			Vector<String> vTerm = SetUtil.readSetFromFile(listFilePath);
			setFilePath(savePath);// ���ñ���·��
			for (String term : vTerm) {
				String filePath = savePath + "/" + term + ".html";
				File f = new File(filePath);
				if (!f.exists())
					vHavenotCrawl.add(term);
			}
			size = vTerm.size();
		}

		public ThreadCrawlByList(String listFilePath, String haveCrawlDir,
				String savePath) {
			Vector<String> vTerm = SetUtil.readSetFromFile(listFilePath);
			setFilePath(savePath);// ���ñ���·��
			File fHave = new File(haveCrawlDir);
			File childsHave[] = fHave.listFiles();
			for (int i = 0; i < childsHave.length; i++) {
				String pageName = childsHave[i].getName();
				String term = pageName.substring(0, pageName.lastIndexOf("."));
				String filePath = savePath + "/" + pageName;
				File fSave = new File(filePath);
				if (vTerm.contains(term) && !fSave.exists()) {
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
			for (String term : vTerm) {
				String filePath = savePath + "/" + term + ".html";
				File f = new File(filePath);
				if (!f.exists())
					vHavenotCrawl.add(term);
			}
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
						proxy.setLocalRandomHttpProxy();//�����������10���������ô���
					}
				}
				if (term.length() != 0) {
					String url = "http://en.wikipedia.org/wiki/" + term;
					String fileName = term + ".html";
					boolean b = crawlPageByUrl(url, fileName);
					String s = FileUtil.readFile(getFilePath()+"/"+fileName);
					// ��������ҳ������ȡ����
					while (!s.endsWith("</html>")) {
						System.out.println(term + ":������");
						crawlPageByUrl(url, fileName);
						s = FileUtil.readFile(getFilePath()+"/"+fileName);
						failedNumber++;
						if(failedNumber>=10)
							break;
					}
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
							if(failedNumber>=10);
								//proxy.setLocalRandomHttpProxy();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println(fileName
								+ "------"
								+ Thread.currentThread().getName()
								+ "------("
								+ ExtractorUtil.scanDirPage(getFilePath())
										.size() + "/" + size + ")");
						failedNumber = 0;// ���ۼӣ�ֻ��������ʧ�ܴ���
					}
				}
			}
		}

	}

	/**
	 * 
	 * @author MJ
	 * @description ���б�����߳���
	 */
	public class ThreadCheckByList implements Runnable {

		private Vector<String> vCheckList = new Vector<String>();
		int size = 0;
		int curId = 1;
		int failedNumber = 0;// ��ȡʧ�ܵ�����

		Proxy proxy=new Proxy();

		public ThreadCheckByList(Vector<String> vCheckList, String savePath) {
			this.vCheckList = vCheckList;
			setFilePath(savePath);// ���ñ���·��
			this.size=vCheckList.size();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (vCheckList.size() != 0) {
				String term = "";
				synchronized ("") {
					if (vCheckList.size() != 0) {
						term = vCheckList.get(0);
						vCheckList.removeElementAt(0);
						curId = size - vCheckList.size();
					}
					if (failedNumber >= 10) {
						failedNumber = 0;
						proxy.setLocalRandomHttpProxy();//�����������10���������ô���
					}
				}
				if (term.length() != 0) {
					String fileName = term + ".html";
					String path = getFilePath() + "/" + fileName;
					String s = FileUtil.readFile(path);
					// ��������ҳ������ȡ����
					while (!s.endsWith("</html>")) {
						System.out.println(term + ":������");
						String url = "http://en.wikipedia.org/wiki/" + term;
						crawlPageByUrl(url, fileName);
						s = FileUtil.readFile(path);
						failedNumber++;
						if(failedNumber>=10)
							break;
					}
					System.out.println(fileName + "------"
							+ Thread.currentThread().getName() + "------("
							+ curId
							+ "/" + size + ")");
					failedNumber = 0;// ���ۼӣ�ֻ��������ʧ�ܴ���
				}
			}
		}

	}

	/**
	 * ���ղ���ָ����url��ʼ���ճ����Ӳ����ȡ�� �ڶ��������ǲ���������ָ���������������
	 * 
	 * @param url
	 * @param layer
	 */
	public void crawlPageByLayer(String url, int layer) {
		HashSet<String> hsCrawled = new HashSet<String>();// �����Ѿ���ȡ����ҳ�漯��
		for (int i = 1; i <= layer; i++) {
			System.out.println("Crawl layer" + i + "����");
			String layerFilePath = filePath + "layer" + i + "/";
			File f = new File(layerFilePath);
			f.mkdirs(); // �������Ŀ¼
			Vector<String> urlLayer = new Vector<String>();
			// ���Ҫ��ȥ��ҳ���ַ����http://en.wikipedia.org/wiki/Data_mining
			if (i == 1) {
				String layer1TxtName = filePath + "layer1.txt";
				File temp1F = new File(layer1TxtName);
				if (temp1F.exists())
					urlLayer.addAll(getHavenotCrawlUrlSet(1));
				else {
					urlLayer.add(url);
					try {
						FileWriter fw = new FileWriter(layer1TxtName);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(getFileName(url));
						bw.flush();
						bw.close();
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				File parentLayerFile = new File(filePath + "layer" + (i - 1));
				File[] childs = parentLayerFile.listFiles();
				HashSet<String> hsTemp = new HashSet<String>();
				for (int j = 0; j < childs.length; j++) {
					hsTemp.addAll(getHrefSet(childs[j].getName(), i - 1));
				}// ��ȡ��i������
				String layeriTxtName = filePath + "/" + "layer" + i + ".txt";
				File tempiF = new File(layeriTxtName);
				if (tempiF.exists())
					urlLayer.addAll(getHavenotCrawlUrlSet(i));
				else {
					try {
						FileWriter fw = new FileWriter(layeriTxtName);
						BufferedWriter bw = new BufferedWriter(fw);
						Iterator<String> it = hsTemp.iterator();
						while (it.hasNext()) {
							String urlTemp = it.next();
							if (getUrl(urlTemp) != null) {
								if (!urlLayer.contains(getUrl(urlTemp))) {
									urlLayer.add(getUrl(urlTemp));
								}
								bw.write(getFileName(urlTemp));// ���ļ����ֱ��浽layer1.txt��
								bw.newLine();
							}
						}
						bw.flush();
						bw.close();
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}// end else
			int layerSum = urlLayer.size();
			int n = 1;
			while (!urlLayer.isEmpty()) {
				String crawlUrl = urlLayer.get(0);// ��ȡ��ַ
				String fileName = getFileName(crawlUrl);// fileName
				if (!hsCrawled.contains(crawlUrl)) {
					String layerFileName = "layer" + i + "/" + fileName;
					if (crawlPageByUrl(crawlUrl, layerFileName)) {
						hsCrawled.add(fileName);// �����ȡ�ɹ����t��ӵ�����ȡ�б���
						urlLayer.remove(0);
						System.out.println(layerFileName + "------(" + (n++)
								+ "/" + layerSum + ")");
					} else {// ��ȡʧ��
						urlLayer.remove(0);
						urlLayer.add(crawlUrl);
						System.out.println("wait����");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					System.out.println(fileName + "------(" + (n++) + "/"
							+ layerSum + ")--------(Crawled)");
				}
			}

		}// end for
		System.out.println("Crawl success!");

	}// crawlPageByLayer()

	/**
	 * ָ����ȡ���ӹ���
	 * 
	 * @param url
	 * @return ����url���������ӹ���
	 */
	public String getUrl(String url) {
		String prefix = "http://en.wikipedia.org";
		String regex = "^/wiki/[a-z A-Z 0-9 _ -]*";
		if (url.matches(regex)) {
			return prefix + url;
		} else
			return null;
	}// crawlLinkRule()

	/**
	 * ָ���ļ���������
	 * 
	 * @param url
	 * @return FileName
	 */
	public String getFileName(String url) {
		String fileName = "";
		fileName = url.substring(url.lastIndexOf("/") + 1, url.length())
				+ ".html";
		return fileName;
	}// getFileName()

	/**
	 * ͨ���ļ�������url��ַ
	 * 
	 * @param fileName
	 * @return
	 */
	public String getUrlByFileName(String fileName) {
		String suffix = ".html";
		String prefix = "http://en.wikipedia.org/wiki/";
		String pureName = fileName.substring(0,
				fileName.length() - suffix.length());
		return prefix + pureName;
	}

	/**
	 * ��ָ�����ļ��л�ȡ����
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param layer
	 *            ����
	 */
	public HashSet<String> getHrefSet(String fileName, int layer) {
		FileReader fr;
		String s = "";
		HashSet<String> hrefSet = new HashSet<String>();
		try {
			fr = new FileReader(filePath + "layer" + layer + "/" + fileName);
			BufferedReader br = new BufferedReader(fr);
			s = br.readLine();
			while (s != null) {
				if (s.contains("href")) {
					int indexA = 0;
					int indexB = 0;
					String herfStr = "";
					String tempStr = s;
					while (tempStr.contains("href")) {
						indexA = tempStr.indexOf("href") + 6;// ��һ�����ź���һ���ַ�
						indexB = tempStr.indexOf("\"", indexA);// ��������
						if (indexB > indexA) {
							herfStr = tempStr.substring(indexA, indexB);
							hrefSet.add(herfStr);
						}
						tempStr = tempStr.substring(indexA + 3,
								tempStr.length());
					}// ��ÿһ�е�����
				}
				s = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sparse success:" + fileName + "-----layer" + layer);
		return hrefSet;
	}// getHrefSet()

	/**
	 * 
	 * @param fileName
	 *            ��filePath·���µ�ĳ���ļ��������Դ������·��
	 * @return ������ָ���ļ���HrefSet
	 */
	public HashSet<String> getHrefSet(String fileName) {
		FileReader fr;
		String s = "";
		HashSet<String> hrefSet = new HashSet<String>();
		try {
			fr = new FileReader(filePath + fileName);
			BufferedReader br = new BufferedReader(fr);
			s = br.readLine();
			while (s != null) {
				if (s.contains("href")) {
					int indexA = 0;
					int indexB = 0;
					String herfStr = "";
					String tempStr = s;
					while (tempStr.contains("href")) {
						indexA = tempStr.indexOf("href") + 6;// ��һ�����ź���һ���ַ�
						indexB = tempStr.indexOf("\"", indexA);// ��������
						if (indexB > indexA) {
							herfStr = tempStr.substring(indexA, indexB);
							hrefSet.add(herfStr);
						}
						tempStr = tempStr.substring(indexA + 3,
								tempStr.length());
					}// ��ÿһ�е�����
				}
				s = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hrefSet;
	}

	/**
	 * ����Layer�㻹û����ȡ��ҳ��url����
	 * 
	 * @param layer
	 * @return
	 */
	public Vector<String> getHavenotCrawlUrlSet(int layer) {
		File f = new File(filePath + "layer" + layer);
		File childs[] = f.listFiles();
		Vector<String> haveCrawlSet = new Vector<String>();
		Vector<String> havenotCrawlSet = new Vector<String>();
		for (int j = 0; j < childs.length; j++) {
			haveCrawlSet.add(childs[j].getName());
		}
		Vector<String> LayerTermSet = getLayerTermSet(layer);
		for (String s : LayerTermSet) {
			if (!haveCrawlSet.contains(s) && !havenotCrawlSet.contains(s)) {
				havenotCrawlSet.add(getUrlByFileName(s));
			}
		}
		return havenotCrawlSet;
	}

	/**
	 * ���ص�i����layeri.txt�е����Ｏ��
	 * 
	 * @param layer
	 *            ��layer��
	 * @return ��i��layeri��layeri.txt�����Ｏ��
	 */
	public Vector<String> getLayerTermSet(int layer) {
		String layerPath = filePath + "/layer" + layer + ".txt";
		Vector<String> vTerm = new Vector<String>();
		FileReader fr;
		try {
			fr = new FileReader(layerPath);
			BufferedReader br = new BufferedReader(fr);
			String temp;
			temp = br.readLine();
			while (temp != null) {
				vTerm.add(temp);
				temp = br.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (vTerm.size() == 0)
			return null;
		else
			return vTerm;
	}

	/**
	 * 
	 * @param url
	 * @return ��ȡָ��url����ҳ���ݵ�String����
	 */
	public static String getPageStringFromWeb(String url) {
		String s = "";
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
			try {
				s = new String(sb.toString().getBytes("gbk"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.err.println("page not found: " + url);
		}
		return s;
	}

	/**
	 * 
	 * @param s
	 * @return page's HrefSet
	 */
	public static Vector<String[]> getHrefSetFromPageString(String s) {
		Vector<String[]> vHref = new Vector<String[]>();
		String hrefTag = "<a href=\"/wiki/";
		String regex = "^[a-z A-Z 0-9 _ -]*";
		int posA = s.indexOf(hrefTag) + hrefTag.length();
		while (posA != -1) {
			int posB = s.indexOf("\"", posA);
			String wikiTerm = s.substring(posA, posB);
			if (wikiTerm.matches(regex)) {
				String temp[] = new String[2];
				temp[0] = wikiTerm;
				temp[1] = String.valueOf(posA);
			}
			posA = s.indexOf(hrefTag, posB) + hrefTag.length();
		}
		return vHref;
	}
}
