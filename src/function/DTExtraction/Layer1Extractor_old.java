package function.DTExtraction;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import function.crawler.WebCrawler;
import function.crawler.WikiCategoryCrawler;
import function.linkAnalysis.NavboxExtractor;
import function.txtAnalysis.TFIDF;
import function.txtAnalysis.Vectorization;
import function.util.FileUtil;
import function.util.SetUtil;

public class Layer1Extractor_old {

	private String DTPath = "";// ����������·��
	private String DomainTerm = "";// ��������
	private String layer0Path = "";// ���������ʼ���·��
	private String layer1RawPath = "";// ��һ����ȡ��������·��
	private String layer1Path = "";// ��1��ɸѡ��������·��
	private String layer01RawPath = "";// 0,1��ȡ��������·��
	private double processId = 0;// ���̿��Ʊ�ǩ
	private String processIdFile = "";// ���̿����ļ�·��
	private String layer0DomainPath ="";// ����ҳ���ַ
	//private String catPath="";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String basePath="f:/FacetedTaxonomy/";
		String DomainTerm="Data_structure";
		Layer1Extractor_old l1e=new Layer1Extractor_old(basePath,DomainTerm);
		Vector<String> v=SetUtil.getNoRepeatVector(l1e.checkNavbox(l1e.layer0DomainPath));
		System.out.println(v.size());
		for(String term :v){
			System.out.println(term);
		}
	}

	public Layer1Extractor_old(String basePath,String DomainTerm) {
		this.DTPath =basePath+DomainTerm+"/html";
		this.DomainTerm = DomainTerm;
		this.layer0Path = this.DTPath + "/layer0";
		this.layer1RawPath = this.DTPath + "/layer1-raw";
		this.layer1Path = this.DTPath + "/layer1";
		this.layer01RawPath = this.DTPath + "/layer01-raw";
		this.layer0DomainPath = layer0Path + "/" + DomainTerm + ".html";
		//this.catPath=basePath+DomainTerm+"/category";
		File f0 = new File(layer0Path);
		File f1raw = new File(layer1RawPath);
		File f1 = new File(layer1Path);
		File f01raw = new File(layer01RawPath);
		f0.mkdirs();
		f1raw.mkdirs();
		f1.mkdirs();
		f01raw.mkdirs();
		this.processIdFile = DTPath + "/processId.txt";
		File fProcess = new File(processIdFile);
		if (!fProcess.exists())
			FileUtil.writeStringFile("0", processIdFile);
	}

	public double getProcessId() {
		double id = Double.valueOf(FileUtil.readFile(processIdFile));
		processId = id;
		return processId;
	}

	public void setProcessId(double processId) {
		this.processId = processId;
		String id = String.valueOf(processId);
		FileUtil.writeStringFile(id, processIdFile);
	}
	
	public void extract() {
		WebCrawler wc = new WebCrawler();
		String layer0HrefPath = this.DTPath + "/layer0-href.txt";
		Vector<String> vHaveCrawl = new Vector<String>();
		String haveCrawlPath = this.DTPath + "/haveCrawl.txt";
		File fCrawl = new File(haveCrawlPath);
		if (fCrawl.exists())
			vHaveCrawl = SetUtil.readSetFromFile(haveCrawlPath);
		else
			SetUtil.writeSetToFile(vHaveCrawl, haveCrawlPath);
		// ��ȡ��ʼҳ��
		if (getProcessId() < 0.1) {
			System.out.println("������ȡ�����ʼҳ�桭��");
			String dtUrl = "http://en.wikipedia.org/wiki/" + DomainTerm;// �����ʼ·��
			while (!wc.crawlPageByUrl(dtUrl, layer0DomainPath))
				;
			vHaveCrawl.add(DomainTerm);
			setProcessId(0.1);
		}
		// ����Wiki����
		if (getProcessId() < 0.2) {
			System.out.println("���ڽ��������ʼҳ���е�ά�������");
			WikiHrefProcess whp = new WikiHrefProcess();
			Vector<String> vWikiTerm = SetUtil.getNoRepeatVector(whp
					.getWikiTermFromFile(layer0DomainPath));
			vWikiTerm.addAll(checkNavbox(layer0DomainPath));// ���NavBox��ҳ��
			Vector<String> vHaveNotCrawl = SetUtil.getSubSet(vWikiTerm,
					vHaveCrawl);
			vHaveNotCrawl = SetUtil.getNoRepeatVector(vHaveNotCrawl);// ȥ���ظ�
			SetUtil.writeSetToFile(vHaveNotCrawl, layer0HrefPath);
			vHaveCrawl.addAll(vHaveNotCrawl);
			SetUtil.writeSetToFile(vHaveCrawl, haveCrawlPath);
			setProcessId(0.2);
		}
		// ��ȡlayer1-raw
		if (getProcessId() < 0.3) {
			System.out.println("������ȡlayer1-raw����");
			wc.crawlPageByList(layer0HrefPath, layer1RawPath, 10);
			setProcessId(0.3);
		}
		// �ϲ�layer0,1 raw
		if (getProcessId() < 0.4) {
			System.out.println("���ںϲ�0,1 raw����");
			try {
				FileUtil.copyDirectiory(layer1RawPath, layer01RawPath);
				FileUtil.copyFile(new File(layer0Path + "/" + DomainTerm
						+ ".html"), new File(layer01RawPath + "/" + DomainTerm
						+ ".html"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setProcessId(0.4);
		}
		// ������
		if (getProcessId() < 0.5) {
			System.out.println("��������������");
			Vectorization vz = new Vectorization();
			vz.wikiVector(layer01RawPath);
			setProcessId(0.5);
		}
		// TF/IDF����
		if (getProcessId() < 0.6) {
			System.out.println("���ڼ���TF/IDF����");
			TFIDF tfidf = new TFIDF();
			tfidf.computeTFIDF_wv(layer01RawPath + "_wikiVector");
			setProcessId(0.6);
		}
		// ��������ҳ�������Ŀ¼������
		if (getProcessId() < 0.7) {
			System.out.println("���ڹ�������ҳ�������Ŀ¼�������");
			buildDomainPageCategory();
			setProcessId(0.7);
		}
	}

	/**
	 * ���fileName�����Ƿ��а���fileName��Navbox������У��򷵻����������
	 * 
	 * @param fileName
	 * @return
	 */
	public Vector<String> checkNavbox(String fileName) {
		String term = "";
		String s = FileUtil.readFile(fileName);
		Vector<String> vWikiTerm = new Vector<String>();
		if (fileName.contains("\\"))
			term = fileName.substring(fileName.lastIndexOf("\\") + 1,
					fileName.indexOf(".html"));
		else
			term = fileName.substring(fileName.lastIndexOf("/") + 1,
					fileName.indexOf(".html"));
		NavboxExtractor ne = new NavboxExtractor();
		HashMap<String, Vector<String>> hm = ne.extractNavboxFromString(s);
		Iterator<String> it = hm.keySet().iterator();
		while (it.hasNext()) {
			String tableName = it.next();
			if (tableName.contains(term)) {
				Vector<String> v = hm.get(tableName);
				for (int i = 0; i < v.size(); i++) {
					String record[] = v.get(i).split(",");
					if (record[2].toLowerCase().equals("true")) {
						vWikiTerm.add(record[0]);
					}
					if (record[3].toLowerCase().equals("true")) {
						vWikiTerm.add(record[1]);
					}
				}
			}
		}
		return vWikiTerm;
	}

	/**
	 * ��������ҳ�������Ŀ¼������
	 * 
	 */
	public void buildDomainPageCategory() {
		WikiCategoryCrawler wcc = new WikiCategoryCrawler();
		Vector<String> vCat = wcc.extractSupCategory(FileUtil
				.readFile(layer0DomainPath));
		//String domainCat = "";// ����Ŀ¼��������������ͬ���߶��s
		for (String cat : vCat) {
			if (cat.toLowerCase().equals(DomainTerm.toLowerCase())
					|| cat.toLowerCase().equals(DomainTerm.toLowerCase() + "s")) {
				//domainCat = cat;
				break;
			}
		}
        //wcc.buildCategoryTree(domainCat, catPath+"/layer0-category");
	}
	
}
