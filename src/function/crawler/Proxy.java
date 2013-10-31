package function.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import function.util.SetUtil;

public class Proxy {

	/**
	 * @param args
	 */
	private String proxyList = getClass()
			.getResource("/resources/proxyList.txt").getPath()
			.replace("%20", " ");

	public String getProxyList() {
		return proxyList;
	}

	public void setProxyList(String proxyList) {
		this.proxyList = proxyList;
	}
	
	/**
	 * �����������
	 */
	public void setLocalRandomHttpProxy(){
		Vector<String> vProxy=SetUtil.readSetFromFile(getProxyList());
		int i=(int)(997*Math.random());
		String record[]=vProxy.get(i).split(":");
		setLocalHttpProxy(record[0],record[1]);
	}

	/**
	 * ����http����
	 */
	public void setLocalHttpProxy(String host, String port) {
		Properties prop = System.getProperties();
		System.out.println("����HTTP������" + host + ":" + port);
		// ����HTTP����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("http.proxyHost", host);
		// ����HTTP����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("http.proxyPort", port);
	}

	/**
	 * ����https����
	 */
	public void setLocalHttpsProxy(String host, String port) {
		Properties prop = System.getProperties();
		// ����HTTPS����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("https.proxyHost", host);
		// ����HTTPS����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("https.proxyPort", port);
	}

	/**
	 * ����ftp����
	 */
	public void setLocalFtpProxy(String host, String port) {
		Properties prop = System.getProperties();
		// ����FTP����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("ftp.proxyHost", host);
		// ����FTP����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("ftp.proxyPort", port);
	}

	/**
	 * ����socks����
	 */
	public void setLocalSocksProxy(String host, String port) {
		Properties prop = System.getProperties();
		// ����socks����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("socks.proxyHost", host);
		// ����socks����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("socks.proxyPort", port);
	}

	/**
	 * ���http��������
	 */
	public void removeLocalHttpProxy() {
		Properties prop = System.getProperties();
		// ���HTTP���ʵĴ������������
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
	}

	/**
	 * ���https��������
	 */
	public void removeLocalHttpsProxy() {
		Properties prop = System.getProperties();
		// ���HTTPS���ʵĴ������������
		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");
	}

	/**
	 * ���ftp��������
	 */
	public void removeLocalFtpProxy() {
		Properties prop = System.getProperties();
		// ���ftp���ʵĴ������������
		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
	}

	/**
	 * ���socks��������
	 */
	public void removeLocalSocksProxy() {
		Properties prop = System.getProperties();
		// ���socks���ʵĴ������������
		prop.remove("socks.proxyHost");
		prop.remove("socks.proxyPort");
	}

	// ����HTTP����
	public void testHttpProxy() throws MalformedURLException, IOException {
		URL url = new URL("http://en.wikipedia.org/wiki/Data_mining");
		// ֱ�Ӵ����ӣ���ϵͳ����ø����õ�HTTP���������
		URLConnection conn = url.openConnection(); // ��
		Scanner scan = new Scanner(conn.getInputStream());
		// ��ȡԶ������������
		while (scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
	}
	
	/**
	 * ����Ĭ��·������
	 */
	public void updateProxy(){
		updateProxy(getProxyList());
	}

	/**
	 * ���´���ָ�����ļ�
	 * 
	 * @param fileName
	 */
	public void updateProxy(String fileName) {
		String proxyUrlPrefix = "http://www.cnproxy.com/proxy";
		String proxyUrlSuffix = ".html";
		Vector<String> vProxySite = new Vector<String>();
		for (int i = 1; i <= 10; i++) {// ��10��ҳ���ȡ
			String proxyUrl = proxyUrlPrefix + i + proxyUrlSuffix;// �����ַ
			System.out.println("�����ַ��" + proxyUrl);
			String proxyPageContent = WebCrawler.getPageStringFromWeb(proxyUrl);// ����ҳ������
			if (proxyPageContent.length() != 0) {
				vProxySite.addAll(getProxyList(proxyPageContent));
			}
		}
		SetUtil.writeSetToFile(vProxySite, fileName);
	}

	/**
	 * ��ȡ����ҳ���еĴ����ַ
	 * 
	 * @param proxyPageContent
	 * @return
	 */
	public Vector<String> getProxyList(String proxyPageContent) {
		Vector<String> vProxy = new Vector<String>();
		proxyPageContent = proxyPageContent.toLowerCase();
		/****** �Ҷ˿ںŶ�Ӧ��ĸ *******/
		HashMap<String, String> hm = new HashMap<String, String>();
		String scriptBegin = "<script type=\"text/javascript\">";
		String scriptEnd = "</script>";
		int scriptBeginPos = proxyPageContent.indexOf(scriptBegin)
				+ scriptBegin.length();
		int scriptEndPos = proxyPageContent.indexOf(scriptEnd, scriptBeginPos);
		String keyString = proxyPageContent.substring(scriptBeginPos,
				scriptEndPos);
		keyString = keyString.replace("\n", "");
		keyString = keyString.replace("\r", "");
		System.out.println(keyString);
		String record[] = keyString.split(";");
		for (int i = 0; i < record.length; i++) {
			String key = record[i].substring(0, 1);
			String value = record[i].substring(3, 4);
			hm.put(key, value);
			System.out.println("�˿ڣ�"+key+","+value);
		}
		/********** ��Ӵ����ַ **********/
		String tag = "ip:port";
		String proxyTag1 = "<tr><td>";
		String proxyTag2 = "<script type=text/javascript>";
		String proxyTag3 = "document.write(\":\"+";
		String proxyTag4 = "</td><td>";
		int tablePosBegin = proxyPageContent.indexOf(tag);
		int tablePosEnd = proxyPageContent.indexOf("</table>", tablePosBegin);
		int pos1 = proxyPageContent.indexOf(proxyTag1, tablePosBegin);
		while (pos1 < tablePosEnd && pos1 != -1) {
			pos1 += +proxyTag1.length();
			int pos2 = proxyPageContent.indexOf(proxyTag2, pos1);
			String ip = proxyPageContent.substring(pos1, pos2);
			int pos3 = proxyPageContent.indexOf(proxyTag3, pos1)
					+ proxyTag3.length();
			int pos4 = proxyPageContent.indexOf(")", pos3);
			int pos5 = proxyPageContent.indexOf(proxyTag4, pos1)
					+ proxyTag4.length();
			int pos6 = proxyPageContent.indexOf(proxyTag4, pos5);
			String protocol = proxyPageContent.substring(pos5, pos6);
			if (protocol.equals("http")) {
				String portChar[] = proxyPageContent.substring(pos3, pos4)
						.replace("+", ",").split(",");
				String port = "";
				for (int i = 0; i < portChar.length; i++) {
					port = port + hm.get(portChar[i]);
				}
				vProxy.add(ip + ":" + port);
				System.out.println(ip + ":" + port);
			}
			pos1 = proxyPageContent.indexOf(proxyTag1, pos6);
		}
		return vProxy;
	}

	public static void main(String[] args) throws IOException {
		Proxy proxy = new Proxy();
		proxy.updateProxy();
	}

}
