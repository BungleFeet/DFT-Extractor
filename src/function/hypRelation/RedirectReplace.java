package function.hypRelation;

import java.util.HashMap;
import java.util.HashSet;

import function.base.ExcelBase;

/**
 * 
 * @author MJ
 * @description ���ճ�ȡ���ض�������ӹ�ϵ�����滻
 */
public class RedirectReplace extends ExcelBase {

	String xlsFileName = "";
	int relationSheetID = 0;// ��ϵsheet
	int redirectSheetID = 1;// �ض���sheet
	int relationRRSheetID = 1;// �滻��Ĺ�ϵsheet

	public RedirectReplace(String xlsFileName, int relationSheetID,
			int redirectSheetID, int relationRRSheetID) {
		this.xlsFileName = xlsFileName;
		this.relationSheetID = relationSheetID;
		this.redirectSheetID = redirectSheetID;
		this.relationRRSheetID = relationRRSheetID;
	}

	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		HashSet<String> hsRelation = new HashSet<String>();// ֱ��ȥ���ظ���
		HashMap<String, String> hmRedirect = new HashMap<String, String>();
		// �����ض���
		for (int i = 1; i < getRows(redirectSheetID); i++) {
			String term = getStringValue(redirectSheetID, "term", i);
			String title = getStringValue(redirectSheetID, "title", i);
			hmRedirect.put(term, title);
		}
		// �滻�ض���
		int relationNumber = 0;
		for (int i = 1; i < getRows(relationSheetID); i++) {
			String sourceURLName = getStringValue(relationSheetID,
					"sourceURLName", i);
			String toURLName = getStringValue(relationSheetID, "toURLName", i);
			if (hmRedirect.containsKey(sourceURLName))
				sourceURLName = hmRedirect.get(sourceURLName);
			if (hmRedirect.containsKey(toURLName))
				toURLName = hmRedirect.get(toURLName);
			if (!sourceURLName.equals(toURLName)
					&& !hsRelation.contains(sourceURLName + "->" + toURLName)) {
				hsRelation.add(sourceURLName + "->" + toURLName);
				setStringValue(relationRRSheetID, "sourceURLName",
						++relationNumber, sourceURLName);
				setStringValue(relationRRSheetID, "toURLName", relationNumber,
						toURLName);
			}
		}
	}

	@Override
	public void run(ExcelBase eb) {
		// TODO Auto-generated method stub
		eb.go(xlsFileName);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String
		// xlsFileName="F:\\FacetedTaxonomy\\Data_mining\\layer01-relation.xls";
		String xlsFileName = "F:\\Data\\testData\\DM-3_relation.xls";
		int relationSheetID = 0;// ��ϵsheet
		int redirectSheetID = 1;// �ض���sheet
		int relationRRSheetID = 2;// �滻��Ĺ�ϵsheet
		RedirectReplace rr = new RedirectReplace(xlsFileName, relationSheetID,
				redirectSheetID, relationRRSheetID);
		rr.run(rr);
	}

}
