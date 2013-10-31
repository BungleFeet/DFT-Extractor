package function.DTExtraction;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import function.base.ExcelBase;
import function.util.FileUtil;
import function.util.SetUtil;

/**
 * 
 * @author MJ
 * @description ��ȡ��ʷ�༭��Ϣ
 */
public class ExtractHistory extends ExcelBase {

	String xlsFileName = "";
	String htmlPath = "";// ��ҳ�ļ���

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Data_mining Data_structure Computer_network
		String htmlPath = "F:\\FacetedTaxonomy\\Data_structure\\history\\layer2-select";
		String xlsFileName = "F:\\FacetedTaxonomy\\Data_structure\\process\\layer2-select-history.xls";
		ExtractHistory ec = new ExtractHistory(htmlPath,
				xlsFileName);
		ec.run(ec);
	}

	// ���캯��
	public ExtractHistory(String htmlPath, String xlsFileName) {
		this.xlsFileName = xlsFileName;
		this.htmlPath = htmlPath;
	}

	public void extractHistory() {
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		File f = new File(htmlPath);
		File childs[] = f.listFiles();
		int recordID = 0;
		int stringRecordID = 1;
		for (int i = 0; i < childs.length; i++) {// ����ÿ���ļ�
			System.out.println(i);
			String fileName = childs[i].getName();
			String filePath = childs[i].getAbsolutePath();
			Vector<String> hisV = extractHistoryEditor(filePath);// ��ȡÿ���ļ���supterm
			String superTermString = "";
			String term = fileName.substring(0, fileName.length() - 5);
			for (int j = 0; j < hisV.size(); j++) {
				String superTerm = hisV.get(j);
				superTermString += superTerm + ",";
				if (hm.containsKey(superTerm)) {
					int n = hm.get(superTerm);
					n++;
					hm.put(superTerm, n);
				} else
					hm.put(superTerm, 1);
				setStringValue(0, "term", ++recordID, term);
				setStringValue(0, "editor", recordID, superTerm);
			}
			if (superTermString.length() >= 1)
				superTermString = superTermString.substring(0,
						superTermString.length() - 1);
			setStringValue(1, "term", stringRecordID, term);
			setStringValue(1, "editor", stringRecordID++, superTermString);
		}// end for
		Iterator<String> it = hm.keySet().iterator();
		int number = 0;
		while (it.hasNext()) {
			String superTerm = it.next();
			int n = hm.get(superTerm);
			setStringValue(2, "editor", ++number, superTerm);
			setIntegerValue(2, "frequency", number, n);
		}
	}

	/**
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �༭��Ϣ���������ߺͱ༭���ֽڴ�С
	 */
	public static Vector<String> extractHistoryInfo(String filePath) {
		String s=FileUtil.readFile(filePath);
		Vector<String> vHistory = new Vector<String>();
		String editorTag = "<span class='history-user'>";
		String byteTag = "bytes after change\">(";
		int pos = s.indexOf(editorTag);
		while (pos != -1) {
			int posL1 = s.indexOf("<", pos + 1);// ��һ���������
			int posR1 = s.indexOf(">", posL1);// ��һ���Ҽ�����
			int posL2 = s.indexOf("<", posR1);// �ڶ����������
			String editor = s.substring(posR1 + 1, posL2);// ����
			int bytePos1 = s.indexOf(byteTag, pos) + byteTag.length();
			int bytePos2 = s.indexOf(")", bytePos1);
			String editByteStr = s.substring(bytePos1, bytePos2);
			int editByte = Integer.valueOf(editByteStr.replace("+", "")
					.replace(",", ""));// �ֽڴ�С
			String editInfo = editor + "@@@@" + editByte;
			vHistory.add(editInfo);
			pos = s.indexOf(editorTag, posL2);
		}
		return vHistory;
	}
	
	/**
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �༭����Ϣ
	 */
	public static Vector<String> extractHistoryEditor(String filePath) {
		String s=FileUtil.readFile(filePath);
		Vector<String> vHistory = new Vector<String>();
		String editorTag = "<span class='history-user'>";
		int pos = s.indexOf(editorTag);
		while (pos != -1) {
			int posL1 = s.indexOf("<", pos + 1);// ��һ���������
			int posR1 = s.indexOf(">", posL1);// ��һ���Ҽ�����
			int posL2 = s.indexOf("<", posR1);// �ڶ����������
			String editor = s.substring(posR1 + 1, posL2);// ����
			String editInfo = editor;
			vHistory.add(editInfo);
			pos = s.indexOf(editorTag, posL2);
		}
		vHistory=SetUtil.getNoRepeatVector(vHistory);
		return vHistory;
	}

	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		extractHistory();
	}

	@Override
	public void run(ExcelBase eb) {
		// TODO Auto-generated method stub
		eb.go(xlsFileName);
	}
}
