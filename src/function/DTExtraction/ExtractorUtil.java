package function.DTExtraction;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import function.linkAnalysis.NavboxExtractor;
import function.util.FileUtil;

public class ExtractorUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * ���fileName�����Ƿ��а���fileName��Navbox������У��򷵻����������
	 * 
	 * @param fileName
	 * @return
	 */
	public static Vector<String> checkNavbox(String fileName) {
		String term = "";
		String s = FileUtil.readFile(fileName);
		Vector<String> vWikiTerm = new Vector<String>();
		fileName.replace("\\", "/");
		term = fileName.substring(fileName.lastIndexOf("/") + 1,
					fileName.indexOf(".html"));
		if(term.contains("("))
			term=term.replace("(", "");
		if(term.contains(")"))
			term=term.replace(")", "");
		NavboxExtractor ne = new NavboxExtractor();
		HashMap<String, Vector<String>> hm = ne.extractNavboxFromString(s);
		Iterator<String> it = hm.keySet().iterator();
		while (it.hasNext()) {
			String tableName = it.next();
			if (tableName.contains(term)) {
				Vector<String> v = hm.get(tableName);
				for (int i = 0; i < v.size(); i++) {
					String record[] = v.get(i).split(",");
					System.out.println(record[0]+","+record[1]);
					if (record[2].toLowerCase().equals("true")) {
						if(checkTerm(record[0]))
						vWikiTerm.add(record[0]);
					}
					if (record[3].toLowerCase().equals("true")) {
						if(checkTerm(record[1]))
						vWikiTerm.add(record[1]);
					}
				}
			}
		}
		return vWikiTerm;
	}
	
	/**
	 * 
	 * @param vExistPage
	 *            �Ѿ����ڵ���ҳ
	 * @param hm
	 *            ��ɨ�����ҳ����·��ӳ��
	 * @return ����ӵ�ҳ������
	 */
	public static Vector<String> getNewAddPage(Vector<String> vExistPage,
			HashMap<String, String> hm) {
		Vector<String> vNewAddPage = new Vector<String>();
		Iterator<String> it = hm.keySet().iterator();
		while (it.hasNext()) {
			String pageName = it.next();
			if (!vExistPage.contains(pageName))
				vNewAddPage.add(pageName);
		}
		return vNewAddPage;
	}

	/**
	 * 
	 * @param dir
	 * @param hm
	 *            �����ҳ���ƺ�·��
	 * @return dirĿ¼�µ���ҳ
	 */
	public static HashMap<String, String> scanDirPage(String dir) {
		HashMap<String, String> hm = new HashMap<String, String>();
		File f = new File(dir);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			hm.put(childs[i].getName(), childs[i].getAbsolutePath());
		}
		return hm;
	}
	
	/**
	 * ���term�Ƿ���ϻ����淶
	 * @param term
	 * @return
	 */
	public static boolean checkTerm(String term){
		Vector<String> vIllegal=new Vector<String>();
		vIllegal.add(":");
		vIllegal.add("*");
		vIllegal.add("/");
		vIllegal.add(",");
		vIllegal.add("Main_Page");
		for(String s:vIllegal){
			if(term.contains(s))
				return false;
		}
		return true;
	}

}
