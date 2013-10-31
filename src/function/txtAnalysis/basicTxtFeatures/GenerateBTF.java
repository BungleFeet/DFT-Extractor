package function.txtAnalysis.basicTxtFeatures;

import function.jsoup.WikiHtml2Txt;

/*
 * ��Сд���⣺
 * 1��ԴURL name����Ҫ������Сд�������wiki����ҳ��ʱ���ָ��������⡣
 * 2��Ŀ��URL name���뱣����Сд����html����ʱ������Ҫ����Сд��
 * 
 * ���Ի�ȡ
 * 1���������ԣ��������������ԣ�ֻ����html�ļ�
 * 2���������ԣ������������ԣ�������text�ļ�
 */

public class GenerateBTF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String htmlPath="f:/DM_html";
		GenerateBTF gbtf=new GenerateBTF();
		gbtf.generateBTF(htmlPath);
	}
	
	/**
	 * ���ɻ������ı�����
	 * @param htmlDir
	 */
	public void generateBTF(String htmlDir){
		/**********htmlת��txt************/
		WikiHtml2Txt wht=new WikiHtml2Txt();
		wht.parseDirText(htmlDir);
		/*********** ����������html�ʼ� **********/
		MOREConfig mc = new MOREConfig();
		mc.setHtmlPath(htmlDir);
		mc.createFile();
		String RdWrFile = mc.xlsName;
		BasicFeatures BFss = new BasicFeatures(mc);
		BFss.init(RdWrFile);
		BFss.createTermSet();
		/*********** �����ǳ�ȡ�����ӹ��� **********/
		BFss.go();
		/**********����������txt�������************/
		BasicTxtFeatures BTFss=new BasicTxtFeatures(mc);
		BTFss.init(RdWrFile);
		BTFss.go();
		/************* ȫ��������� ***********/
		System.out.println("���ɳ����ӻ���������ϣ�");
	}

}
