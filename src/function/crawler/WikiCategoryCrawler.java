/**
 * 
 */
package function.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import function.txtAnalysis.util.OddComplexReplace;
import function.util.SetUtil;

/** 
 * ����1����ָ�������ɷ���ҳ�濪ʼ����ȡ����ṹ�����浽csv�ļ���
 * ����2����ȡ����һ��wikiҳ�棬�õ����ࡣ
 * 
 * 1��������Ϣ��Դ������ҳ�棺һ��wikiҳ���ר�õ�wiki����ҳ�档
 *   һ��wikiҳ��ֻ�и��ࡣ
 *   wiki����ҳ�����3�ࣺ���࣬���࣬��ҳ�档
 * 2�������ϵ����ѭ��������wiki����ҳ������౾���ѭ����
 * 3��2�ֹ�����ʽ��cvs�ļ���׷�ӷ�ʽ�򿪡�
 *   1��ȫ�¹�����cvs�ļ���գ������Ϊȫ����
 *   2������������cvs�ļ�����գ�ȥ����ȡ���ĸ���ֻ�����µĸ���
 *
 */
public class WikiCategoryCrawler {

	public URL dicURL=getClass().getResource("/resources/standardspellings.txt");
	public Vector<String> vDic = SetUtil.readSetFromFile(dicURL.getPath().replace("%20", " "));//�ֵ�
	public String crawlPageName="";//����������ȡ�����
	public HashSet<String> hsHaveStore=new HashSet<String>();//�����Ѿ�д���ļ�¼
	String cvsCategoryFile = "";
	FileWriter cvsWriter;

	/**
	 * 
	 */
	public WikiCategoryCrawler() {
		// TODO Auto-generated constructor stub
		System.out.println("cvsCategoryFile:"+cvsCategoryFile);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		WikiCategoryCrawler wcc=new WikiCategoryCrawler();
		String catTermPath="C:\\Users\\Lenovo\\Desktop\\category.txt";
		wcc.buildCategoryTree(catTermPath,1);
	}
	
	/**
	 * ����ָ���ļ��Ͻ�Ŀ¼��ȡ��ָ����·��
	 * @param vCat
	 * @param desCatPath
	 */
	public void buildCategoryTree(Vector<String> vCat,String desCatDir,int depth){
		for(String s:vCat){
			buildCategoryTree(s,desCatDir,depth);
		}
	}
	
	/**
	 * ����ָ������ȡĿ¼��ָ�����ļ�����
	 * @param catTerm
	 * @param desCatDir
	 * @param depth ���
	 */
	public void buildCategoryTree(String catTerm,String desCatDir,int depth){
		WikiCategoryCrawler wps = new WikiCategoryCrawler();
		Vector<String> cat = new Vector<String>();
		cat.add(catTerm);
		desCatDir=desCatDir.replace("\\", "/");
		File fCatDataDir=new File(desCatDir);
		fCatDataDir.mkdirs();
	    cvsCategoryFile=desCatDir+"/"+catTerm+"_category.csv";
		wps.init(cvsCategoryFile);
		wps.travelCategoryPages(cat, depth);
		System.out.println("end");
	}

	/*// �ӱ���Ŀ¼��ȡ��Ŀ¼����һ��ҳ�棬��ø��ࡣ
	//�ӱ���Ŀ¼��ȡ��Ŀ¼����wiki����ҳ�棬��ø��࣬���࣬��ҳ�档
	public static void testPagesCategoryFromLocal() {
		WikiCategoryScrawl wps = new WikiCategoryScrawl();
		
		LocalCorpus corpus = new LocalCorpus();
		String dir= "F:/Data/wei/DM/Category";
		Vector<String> fileNames=corpus.getFileNamesFromDirectory(dir);

		Vector<String> cat = null;
		for (String name: fileNames) {
			StringBuffer buffer = corpus.getHtmlFileBuffer(name);
			buffer = corpus.getHtmlFileBuffer(name);
			cat = wps.extractSupCategory(buffer);
			cat = wps.extractSubPages(buffer);
			System.out.println(cat);
		}
	}*/

	// ���ض��ķ���ҳ��ڵ㿪ʼ�����߻�ȡ���࣬���࣬���ӽڵ㡣Ȼ��������ࡣ
	// ���ǲ��������ࡣ����������ӽڵ㡣 �����ӡ����Ļ�ͱ��浽�ļ�
	//��ȫ�£�����������
	public void buildCategoryTree(String catTermPath,int depth) {

		WikiCategoryCrawler wps = new WikiCategoryCrawler();
		Vector<String> vTerm = SetUtil.readSetFromFile(catTermPath);
		for(String s:vTerm){
			Vector<String> cat = new Vector<String>();
			cat.add(s);
			catTermPath=catTermPath.replace("\\", "/");
			String catDataDir=catTermPath.substring(0, catTermPath.lastIndexOf("/")+1)+"category_data";
			File fCatDataDir=new File(catDataDir);
			fCatDataDir.mkdirs();
		    cvsCategoryFile=catDataDir+"/"+s+"_category.csv";
		    File f=new File(cvsCategoryFile);
		    if(!f.exists()){
		    	wps.init(cvsCategoryFile);
				wps.travelCategoryPages(cat, depth);
				System.out.println("end");
		    }
		}
		// �Ѿ�������
		/**
		 * DM��������Ŀ
		 */
		/*cat.add("Data_mining");
		cat.add("Data_analysis");
		cat.add("Machine_learning");
		cat.add("Computational_statistics");
		cat.add("Data_management");*/
		/*cat.add("Artificial_intelligence");
		cat.add("Algorithms");
		cat.add("Statistical_theory");
		cat.add("Knowledge");
		cat.add("Business_intelligence");*/
		
		/*cat.add("Search_algorithms");
		cat.add("Statistical_models");
		cat.add("Trees_(structure)");*/
		/**
		 * CN��������Ŀ
		 */
		/*cat.add("Computer_networks");
		cat.add("Local_area_networks");
		cat.add("Metropolitan_area_networks");
		cat.add("Personal_area_networks");
		cat.add("Wide_area_networks");
		cat.add("Internet");
		cat.add("Internet_terminology");
		cat.add("World_Wide_Web");
		cat.add("Mobile_Web");
		cat.add("Internet_architecture");
		cat.add("Wireless_networking");
		cat.add("Wi-Fi");*/
		/*cat.add("Data_transmission");
		cat.add("Telecommunications_equipment");
		cat.add("Telecommunications_standards");
		cat.add("Internet access");*/
		/*cat.add("Interoperability");
		cat.add("Radio_spectrum");
		cat.add("Electromagnetic_radiation");
		cat.add("Cables");*/
		//cat.add("Network_protocols");
		//cat.add("Servers_(computing)");
		/**
		 * DS��������Ŀ
		 */
		/*cat.add("Trees_(data_structures)");
		cat.add("Sorting_algorithms");
		cat.add("Binary_trees");
		cat.add("Graph_families");
		cat.add("Search_algorithms");
		cat.add("Algorithms_and_data_structures_stubs");
		cat.add("Data_types");
		cat.add("Comparison_sorts");
		cat.add("Graph_theory");
		cat.add("Graph_algorithms");
		cat.add("Abstract_data_types");
		cat.add("Data_structures");*/
		
		/**
		 * DSnew
		 *//*
		cat.add("Data_structures");
		cat.add("Graphs");	
		cat.add("Graph_algorithms");		
		cat.add("Abstract_data_types");
		cat.add("Algorithms");
		cat.add("Machine_learning");*/

		// IR������ϵ�����⣬ѭ��������������ϵͳ�����������
		// cat.add("Information_retrieval");
	}

	

	// ��������ҳ��,�ݹ��ӷ��࣬���ݹ鸸����
	// �ݹ��쳣��Microsoft server software�� Microsoft server technologyѭ��
	public void travelCategoryPages(Vector<String> urlTerms, int level) {
		if (urlTerms.isEmpty())
			return;
		if (level-- < 1)
			return;

		Vector<String> cat = null;
		for (String term : urlTerms) {
			
			//�����ϵõ�wiki����ҳ��
			String url = buildCategoryUrl(term);			
			StringBuffer buffer =getPageBufferFromWeb(url);
			
			/*//��ȡ����
			cat = extractSupCategory(buffer);
			System.out.println("parent category=" + cat);
			cvsStore(term, 1, cat);*/
			
			//��ȡ��ҳ��
			cat = extractSubPages(buffer);
			System.out.println("sub pages=" + cat);
			cvsStore(term, 0, cat);
			
			//��ȡ����
			cat = extractSubCategory(buffer);
			System.out.println("sub category=" + cat);
			cvsStore(term, 0, cat);
			filterSubCategory(cat);
			
			//�ݹ��������
			travelCategoryPages(cat, level);
		}
	}
	
	// ����һЩ���õ�term
	public void filterSubCategory(Vector<String> urlTerms) {
		Set<String> delTerms = new HashSet<String>();
		delTerms.add("Microsoft");
		delTerms.add("Windows");
		for (Iterator<String> i = urlTerms.iterator(); i.hasNext();) {
			String str = i.next();
			if (str.toLowerCase().indexOf("internet by country") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("internet by continent") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("internet-related lists") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("companies") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("researcher") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("tool") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("microsoft") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("windows") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("free_") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("_software") != -1) {
				i.remove();
				continue;
			}
		}
	}


	// ����һЩ���õ�term
	public void filterSubCategory2(Vector<String> urlTerms) {
		Set<String> delTerms = new HashSet<String>();
		delTerms.add("Microsoft");
		delTerms.add("Windows");
		for (Iterator<String> i = urlTerms.iterator(); i.hasNext();) {
			String str = i.next();
			if (str.toLowerCase().indexOf("microsoft") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("windows") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("free_") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("_software") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("researcher") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("tool") != -1) {
				i.remove();
				continue;
			}
			if (str.toLowerCase().indexOf("google") != -1) {
				i.remove();
				continue;
			}
		}
	}

	// ��ȡ����ҳ������࣬��Է���ҳ�档������ҳ������ࡣ
	public Vector<String> extractSubCategory(StringBuffer buffer) {
		/*
		 * <a href="/wiki/Category:Classification_algorithms"
		 * title="Category:Classification algorithms">Classification
		 * algorithms</a>
		 */
		String startFeature0 = "mw-subcategories";
		String startFeature = "Subcategories";
		String feature = "href=\"/wiki/Category:";
		int delta = feature.length();
		String endFeature = "mw-pages";

		int startPos0 = buffer.indexOf(startFeature0);
		int startPos = buffer.indexOf(startFeature, startPos0);
		if (startPos == -1)
			startPos = Integer.MAX_VALUE;
		int endPos = buffer.indexOf(endFeature, startPos);

		int pos = buffer.indexOf(feature, startPos);

		Vector<String> cat = new Vector<String>();
		while (pos != -1) {
			if (pos < startPos)
				break;
			if (pos > endPos)
				break;

			int end = buffer.indexOf("\"", pos + delta);
			cat.add(buffer.substring(pos + delta, end));
			pos = buffer.indexOf(feature, pos + delta);
		}
		return cat;
	}

	// ��ȡ����ҳ�����ҳ�棬��Է���ҳ�档������ҳ������ࡣ
	public Vector<String> extractSubPages(StringBuffer buffer) {
		/*
		 * <a href="/wiki/Category:Classification_algorithms"
		 * title="Category:Classification algorithms">Classification
		 * algorithms</a>
		 */
		String startFeature0 = "Pages in category";
		String startFeature = "mw-content-ltr";
		String feature = "href=\"/wiki/";
		int delta = feature.length();
		String endFeature = "</div></div></div>";

		int startPos0 = buffer.indexOf(startFeature0);
		int startPos = buffer.indexOf(startFeature, startPos0);
		if (startPos == -1)
			startPos = Integer.MAX_VALUE;
		int endPos = buffer.indexOf(endFeature, startPos);

		int pos = buffer.indexOf(feature, startPos);

		Vector<String> cat = new Vector<String>();
		while (pos != -1) {
			if (pos < startPos)
				break;
			if (pos > endPos)
				break;

			int end = buffer.indexOf("\"", pos + delta);
			cat.add(buffer.substring(pos + delta, end));
			pos = buffer.indexOf(feature, pos + delta);
		}
		return cat;
	}
	
	// ��ȡҳ��ĸ��࣬���һ��ҳ��ͷ���ҳ��
	public Vector<String> extractSupCategory(String s) {
		/*
		 * <a href="/wiki/Category:Classification_algorithms"
		 * title="Category:Classification algorithms">Classification
		 * algorithms</a>
		 */
		String startFeature0 = "catlinks";
		String startFeature = "mw-normal-catlinks";
		String feature = "href=\"/wiki/Category:";
		int delta = "href=\"/wiki/Category:".length();
		String endFeature = "</div></div>";

		int startPos0 = s.indexOf(startFeature0);
		int startPos = s.indexOf(startFeature, startPos0);
		// �쳣�����ڷ������ܻ���һЩ���صģ����õķ����ǩ
		String exceptFeature = "mw-hidden-catlinks";
		//String exceptFeature2 = "mw-hidden-cats-hidden";
		int exceptPos = s.indexOf(exceptFeature);
		if (exceptPos == -1)
			exceptPos = Integer.MAX_VALUE;
		int endPos = s.indexOf(endFeature, startPos);

		int pos = s.indexOf(feature, startPos);

		Vector<String> cat = new Vector<String>();
		while (pos != -1) {
			if (pos > endPos)
				break;
			if (pos > exceptPos)
				break;

			int end = s.indexOf("\"", pos + delta);
			cat.add(s.substring(pos + delta, end));
			pos = s.indexOf(feature, pos + delta);
		}
		return cat;
	}

	// ��ȡҳ��ĸ��࣬���һ��ҳ��ͷ���ҳ��
	public Vector<String> extractSupCategory(StringBuffer buffer) {
		/*
		 * <a href="/wiki/Category:Classification_algorithms"
		 * title="Category:Classification algorithms">Classification
		 * algorithms</a>
		 */
		String startFeature0 = "catlinks";
		String startFeature = "mw-normal-catlinks";
		String feature = "href=\"/wiki/Category:";
		int delta = "href=\"/wiki/Category:".length();
		String endFeature = "</div></div>";

		int startPos0 = buffer.indexOf(startFeature0);
		int startPos = buffer.indexOf(startFeature, startPos0);
		// �쳣�����ڷ������ܻ���һЩ���صģ����õķ����ǩ
		String exceptFeature = "mw-hidden-catlinks";
		//String exceptFeature2 = "mw-hidden-cats-hidden";
		int exceptPos = buffer.indexOf(exceptFeature);
		if (exceptPos == -1)
			exceptPos = Integer.MAX_VALUE;
		int endPos = buffer.indexOf(endFeature, startPos);

		int pos = buffer.indexOf(feature, startPos);

		Vector<String> cat = new Vector<String>();
		while (pos != -1) {
			if (pos > endPos)
				break;
			if (pos > exceptPos)
				break;

			int end = buffer.indexOf("\"", pos + delta);
			cat.add(buffer.substring(pos + delta, end));
			pos = buffer.indexOf(feature, pos + delta);
		}
		return cat;
	}
	
	
	
	// ��������ҳ���URL
	public Vector<String> buildCategoryUrls(Vector<String> urls) {
		Vector<String> fullUrls = new Vector<String>();
		for (String url : urls) {
			fullUrls.add(buildCategoryUrl(url));
		}
		return fullUrls;
	}

	public String buildCategoryUrl(String url) {
		return "http://en.wikipedia.org/wiki/Category:" + url;
	}

	// ��׷�ӷ�ʽ���ļ�
	public void init(String file) {
		System.out.println("CSV file open... " + file);
		try {
			cvsWriter = new FileWriter(file, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// cvs�ļ�д�롣����type=1��ʾ�Ǹ��࣬�����0��ʾ����
	public void cvsStore(String node, int type, Vector<String> lines) {
		try {
			node = getOdd(node);
			for (String line : lines) {
				line =getOdd(line);
				if (line.equals(node))
					continue;
				String record="";
				if (1 == type) {
					record=line + "," + node + "\n";
				} else {
					record=node + "," + line + "\n";
				}
				if(!hsHaveStore.contains(record)){
					cvsWriter.write(record);
					hsHaveStore.add(record);
				}
			}
			cvsWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����ULR�����ϻ�ȡHTML�ļ�
	public static StringBuffer getPageBufferFromWeb(String url) {
		return getPageBufferFromWeb2(url,null);
	}	

	//�õ�ҳ������ݣ�����ֵɾ���˻س�����������δɾ���س�
	public static StringBuffer getPageBufferFromWeb2(String url,StringBuffer outBuffer) {
		StringBuffer sb = new StringBuffer();
		try {
			URL my_url = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				sb.append(strTemp);
				if(null != outBuffer) outBuffer.append(strTemp+"\r\n");
			}
			System.out.println("get page successful form " + url);
			if(null != outBuffer) outBuffer.append(br.toString());
		} catch (Exception ex) {
			//ex.printStackTrace();
                        System.err.println("page not found: " + url);
		}

		return sb;
	}
	
	/**
	 * 
	 * @param str ĳ������
	 * @return str�ĵ�����ʽ
	 */
	public String getOdd(String str){
		String a="",b=str;
		if(str.contains("_")){
			a=str.substring(0, str.lastIndexOf("_")+1);
			b=str.substring(str.lastIndexOf("_")+1,str.length());
		}
		String result=a+new OddComplexReplace().replace(b, vDic);
		return result;
	}
}
