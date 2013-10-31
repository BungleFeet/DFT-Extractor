package function.txtAnalysis.basicTxtFeatures;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MOREConfig {

	public String htmlPath ="";// html���ݼ���Ŀ¼
	public String getHtmlPath() {
		return htmlPath;
	}


	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String txtPath =htmlPath+ "_txt";// txt���ݼ���Ŀ¼
	public String xlsPath =htmlPath+"_xls";// Excel�ļ���
	public String xlsName = xlsPath + "/relation-BTF.xls";//�����ı�������excel�ļ�
	public String termSetPath=htmlPath+"_termset.txt";//���Ｏ�ļ�

	
	public void createFile() {
		this.txtPath =htmlPath+ "_txt";// txt���ݼ���Ŀ¼
		this.xlsPath =htmlPath+"_xls";// Excel�ļ���
		this.xlsName = xlsPath + "/relation-BTF.xls";//�����ı�������excel�ļ�
		termSetPath=htmlPath+"_termset.txt";//���Ｏ�ļ�
		File txt = new File(txtPath);
		File xls = new File(xlsPath);
		txt.mkdirs();
		xls.mkdirs();
		String[] headName = { "ID", "sourceURLName", "toURLName", "Relation",
				"srcID", "toID", "Note","snTag", "DeltaText", "posInHtml",
				"AnchorText", "linkSameAhchor", "htmlLen", "posInHtml2",
				"LogicPos", "repeatNum", "linkMode", "LinkSequence",
				"PosInTxt", "txtLen", "PosInTxt2", "ParaInTxt", "ParaNum",
				"ParaInTxt2", "posInPara", "ParaLen", "posInPara2"};
		try {
			WritableWorkbook wb = Workbook.createWorkbook(new File(xlsName));
			// �������ݱ��
			WritableSheet wsheet = wb.createSheet("BTF",0);
			for (int i = 0; i < headName.length; i++) {
				Label label = new Label(i, 0, headName[i]);
				wsheet.addCell(label);
			}
			wb.write();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("����ļ������ɹ�����");
	}
}
