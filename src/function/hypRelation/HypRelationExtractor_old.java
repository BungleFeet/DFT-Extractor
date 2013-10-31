package function.hypRelation;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import function.crawler.WikiCategoryCrawler;
import function.linkAnalysis.NavboxExtractor;
import function.linkAnalysis.URLtoExcel;
import function.linkAnalysis.redirectProcess.RedirectProcess;
import function.netAnalysis.motif.Motif;
import function.util.DownloadFile;
import function.util.ExcelUtil;
import function.util.MapUtil;
import function.util.SetUtil;

public class HypRelationExtractor_old {

	public Vector<String> vGraphRoot = new Vector<String>();// navbox��category���ɵ�ͼ�ĸ�

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String htmlPath = "F:\\Data\\����λ���ݼ�\\DS\\DS_html";
		HypRelationExtractor_old hre = new HypRelationExtractor_old();
		hre.extractHypRelation(htmlPath);
	}

	public void extractHypRelation(String htmlPath) {
		/********* ����fanmod *********/
		String fanmodPath = "C:\\Program Files\\fanmod.exe";
		File f = new File(fanmodPath);
		if (!f.exists()){
			new DownloadFile().downloadFanmod();
		}
		String desDir = htmlPath + "-hypRelation";
		File fDesDir = new File(desDir);
		fDesDir.mkdirs();
		/********* URLtoExcel *********/
		System.out.println("���ڳ�ȡhtml�����ӡ���");
		String rawXlsPath = desDir + "/relation-raw.xls";
		URLtoExcel utoe=new URLtoExcel();
		utoe.extractNoRepeatUrlToExcel(htmlPath, rawXlsPath);
		/********* ȥ���ض��� *********/
		System.out.println("���ڼ���ض��򡭡�");
		RedirectProcess rp=new RedirectProcess(htmlPath,true,rawXlsPath,1);
		rp.run(rp);
		System.out.println("�����滻�ض��򡭡�");
		RedirectReplace rr=new RedirectReplace(rawXlsPath,0,1,2); rr.run(rr);
		
		System.out.println("���ڴ����µĹ�ϵ����");
		String newRelationXlsFileName = desDir + "/relation.xls";
	    generateFinalRelationExcel(rawXlsPath,2,newRelationXlsFileName);
		/********* ����Motif *********/
		System.out.println("���ڼ���Motif����");
		Motif motif=new Motif();
		motif.computeMotif3(newRelationXlsFileName);
		/********* ��ȡNAVBOX *********/
		System.out.println("���ڳ�ȡNavbox����");

		String navboxPath = desDir + "/navbox";
		NavboxExtractor.extractNavBox(htmlPath, navboxPath);

		/********* ͳ��superCategory *********/
		System.out.println("����ͳ��superCategory����");
		String catDir=desDir + "/category";
		File fCatDir=new File(catDir);
		fCatDir.mkdirs();
		String categoryXlsPath = catDir + "/category.xls";
		ExtractCategory ec = new ExtractCategory(htmlPath, categoryXlsPath);
		ec.run(ec);
		/********* ѡ�������ȡĿ¼ *********/
		System.out.println("������ȡĿ¼����");
		Vector<String> vCatResult = getCatRoot(categoryXlsPath);
		String catPath = catDir + "/root.txt";
		SetUtil.writeSetToFile(vCatResult, catPath);
		WikiCategoryCrawler wcc=new WikiCategoryCrawler();
		wcc.buildCategoryTree(catPath,4);
		/********* ��ע���� *********/

		System.out.println("���ڱ�ע���ݡ���");
		HashMap<String, String> hmRedirect = new HashMap<String, String>();
		String columnNames[] = new String[2];
		columnNames[0] = "term";
		columnNames[1] = "title";
		Vector<String[]> vRedirect = ExcelUtil.readSetFromExcel(rawXlsPath, 1,
				columnNames);
		for (int i = 0; i < vRedirect.size(); i++) {
			String record[] = vRedirect.get(i);
			hmRedirect.put(record[0], record[1]);
		}// �ض����
		TermGraph tg = new TermGraph();
		String navboxDataPath = navboxPath + "/data";
		tg = addEdge(tg, navboxDataPath, hmRedirect);
		String categoryDataPath = desDir + "/category/category_data";
		tg = addEdge(tg, categoryDataPath, hmRedirect);
		Annotator at = new Annotator(newRelationXlsFileName, 0, tg, vGraphRoot);
		at.run(at);

		/********* ѵ������ *********/
		System.out.println("���ڲ���ѵ�����Ͳ��Լ�����");
		String trainFileName = desDir + "/train.csv";
		String testFileName = desDir + "/test.csv";
		generateTrainTestFile(newRelationXlsFileName,trainFileName,testFileName);
		System.out.println("����ѵ�����ԡ���");
		WekaTrainTest wtt = new WekaTrainTest();
		HashMap<Integer, String> hmPredict = wtt.trainTest(trainFileName,
				testFileName, "weka.classifiers.trees.RandomForest");
		AddPredirectRelation gfr = new AddPredirectRelation(
				newRelationXlsFileName, hmPredict);
		gfr.run(gfr);
		System.out.println("����λ��ȡ��ϣ�");
	}

	/**
	 * ���ݸ�ͳ�Ʊ�����Ҫ��ȡ�ĸ�
	 * 
	 * @param xlsFileName
	 * @return
	 */
	public Vector<String> getCatRoot(String xlsFileName) {
		Vector<String> vCatResult = new Vector<String>();// ����ѡ���ĸ���Ž��
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		String columnNames[] = new String[2];
		columnNames[0] = "superTerm";
		columnNames[1] = "frequency";
		Vector<String[]> vCat = ExcelUtil.readSetFromExcel(xlsFileName, 1,
				columnNames);
		for (int i = 0; i < vCat.size(); i++) {
			String record[] = vCat.get(i);
			hm.put(record[0],
					Integer.valueOf(record[1].substring(0,
							record[1].indexOf("."))));
		}
		Vector<String[]> v = MapUtil.sortMapValueDes(hm);
		for (int i = 0; i < 6; i++) {// 6�Ǹ�������
			String root = v.get(i)[0];
			vCatResult.add(root);
			System.out.println("root:" + root);
		}
		return vCatResult;
	}

	/**
	 * ��ӱߣ�������Ҫ����ָ�����ض������滻
	 * 
	 * @param dataDir
	 * @param hmRedirect
	 * @return
	 */
	public TermGraph addEdge(TermGraph tg, String dataDir,
			HashMap<String, String> hmRedirect) {
		File f = new File(dataDir);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			String path = childs[i].getAbsolutePath();
			System.out.println("��ӱ��ļ���" + path);
			Vector<String> vTemp = SetUtil.readSetFromFile(path);
			for (int j = 0; j < vTemp.size(); j++) {
				String record[] = vTemp.get(j).split(",");
				if (hmRedirect.containsKey(record[0]))
					record[0] = hmRedirect.get(record[0]);
				if (hmRedirect.containsKey(record[1]))
					record[1] = hmRedirect.get(record[1]);
				tg.addEdge(record[0], record[1]);
				if (j == 0)
					vGraphRoot.add(record[0]);// �Ѹ���������
			}
		}
		return tg;
	}

	/**
	 * ��relation-raw�еĹ�ϵsheet���������
	 * 
	 * @param xlsFileName
	 * @param sheetID
	 * @param desXlsFileName
	 */
	private void generateFinalRelationExcel(String xlsFileName, int sheetID,
			String desXlsFileName) {
		String ColumnNames[] = new String[2];
		ColumnNames[0] = "sourceURLName";
		ColumnNames[1] = "toURLName";
		Vector<String[]> vRecord = ExcelUtil.readSetFromExcel(xlsFileName,
				sheetID, ColumnNames);
		Vector<Vector<Serializable>> vRecordWithId=new Vector<Vector<Serializable>>();
		for (int i = 0; i < vRecord.size(); i++) {
			Vector<Serializable> recordWithId = new Vector<Serializable>();
			String record[] = vRecord.get(i);
			recordWithId.add(String.valueOf(i + 1));
			recordWithId.add(record[0]);
			recordWithId.add(record[1]);
			vRecordWithId.add(recordWithId);
		}
		String ColumnNamesWithId[] = new String[3];
		ColumnNamesWithId[0] = "id";
		ColumnNamesWithId[1] = ColumnNames[0];
		ColumnNamesWithId[2] = ColumnNames[1];
		ExcelUtil.writeSetToExcel(vRecordWithId, desXlsFileName, 0,
				ColumnNamesWithId);
	}

	/**
	 * ���ݸ�����ϵ�����ѵ�����Ͳ��Լ�
	 * 
	 * @param xlsFileName
	 * @param trainFileName
	 * @param testFileName
	 */
	public void generateTrainTestFile(String xlsFileName, String trainFileName,
			String testFileName) {
		String ColumnNames[] = new String[15];
		ColumnNames[0] = "id";
		ColumnNames[1] = "m000000110";
		ColumnNames[2] = "m000001100";
		ColumnNames[3] = "m000001110";
		ColumnNames[4] = "m000100100";
		ColumnNames[5] = "m000100110";
		ColumnNames[6] = "m000101110";
		ColumnNames[7] = "m001001110";
		ColumnNames[8] = "m001100110";
		ColumnNames[9] = "m010001100";
		ColumnNames[10] = "m010100100";
		ColumnNames[11] = "m010100110";
		ColumnNames[12] = "m010101110";
		ColumnNames[13] = "m011101110";
		ColumnNames[14] = "relation";
		Vector<String[]> vRecord = ExcelUtil.readSetFromExcel(xlsFileName, 0,
				ColumnNames);
		Vector<String> vTrain = new Vector<String>();// ���ѵ������
		Vector<String> vTest = new Vector<String>();// ��Ų�������
		String title = "";
		for (int i = 0; i < ColumnNames.length; i++) {
			if (i < ColumnNames.length - 1)
				title = title + ColumnNames[i] + ",";
			else
				title = title + ColumnNames[i];
		}
		vTrain.add(title);
		vTest.add(title);
		String relation[] = new String[3];
		relation[0] = "A is a B";
		relation[1] = "B is a A";
		relation[2] = "Other";
		Random rd = new Random();
		for (int i = 0; i < vRecord.size(); i++) {
			String record[] = vRecord.get(i);
			if (!record[14].equals("noExist")) {
				String trainRecord = "";
				for (int j = 0; j < ColumnNames.length; j++) {
					if (j < ColumnNames.length - 1)
						trainRecord = trainRecord + record[j] + ",";
					else
						trainRecord = trainRecord + record[j];
				}
				vTrain.add(trainRecord);
			} else {
				String testRecord = "";
				for (int j = 0; j < ColumnNames.length - 1; j++) {
					testRecord = testRecord + record[j] + ",";
				}
				testRecord = testRecord + relation[Math.abs(rd.nextInt()%3)];
				vTest.add(testRecord);
			}
		}// end for
		SetUtil.writeSetToFile(vTrain, trainFileName);
		SetUtil.writeSetToFile(vTest, testFileName);
	}
}
