package function.DTExtraction;

import java.io.File;
import java.util.Vector;

import function.util.SetUtil;

public class CategoryAnalysis {

	String selectTermPath="F:\\extractTest\\Data_mining\\process\\layer12-select.txt";//�Ѿ�ѡ��������б�·��
	String termSetPath="F:\\extractTest\\Data_mining\\process\\DM_termset.txt";
	String categoryDir="C:\\Users\\Lenovo\\Desktop\\category_data";
	String desTermListPath="";//category����ѡ����������λ��
	
	public CategoryAnalysis(String selectTermPath,String categoryDir,String desTermListPath) {
		super();
		this.selectTermPath = selectTermPath;
		this.categoryDir = categoryDir;
		this.desTermListPath=desTermListPath;
	}
	
	public CategoryAnalysis(String selectTermPath, String termSetPath,
			String categoryDir,String desTermListPath) {
		super();
		this.selectTermPath = selectTermPath;
		this.termSetPath = termSetPath;
		this.categoryDir = categoryDir;
		this.desTermListPath=desTermListPath;
	}
	
	public void getCategoryTerm(){
		Vector<String> vSelectTerm=SetUtil.readSetFromFile(selectTermPath);
		Vector<String> vCatRecallTerm=new Vector<String>();
		File f=new File(categoryDir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String path=childs[i].getAbsolutePath();
			String name=childs[i].getName();
			String category=name.substring(0, name.lastIndexOf("_"));
			Vector<String> vCatString=SetUtil.readSetFromFile(path);
			Vector<String> vCatTerm=new Vector<String>();
			for(String temp:vCatString){
				String term[]=temp.split(",");
				if(!term[0].contains(":")&&!term[0].contains("/"))
				vCatTerm.add(term[0]);
				if(!term[1].contains(":")&&!term[1].contains("/"))
				vCatTerm.add(term[1]);
			}
			vCatTerm=SetUtil.getNoRepeatVector(vCatTerm);
			int sum=vCatTerm.size();
			Vector<String> vSelectCatTerm=SetUtil.getInterSet(vSelectTerm, vCatTerm);//ѡ���
			int selectCatTermNumber=vSelectCatTerm.size();//ѡ�������
			int selectCatIsTermNumber=0;//ѡ��������������
			Vector<String> vNoSelectCatTerm=SetUtil.getSubSet(vCatTerm, vSelectCatTerm);//û��ѡ���
			int noSelectCatTermNumber=vNoSelectCatTerm.size();//ûѡ�������
			int noSelectCatIsTermNumber=0;//ûѡ��������������
			vCatRecallTerm.addAll(vNoSelectCatTerm);
			System.out.println(category+":("+sum+"),("+selectCatIsTermNumber+"/"+selectCatTermNumber+"),("+noSelectCatIsTermNumber+"/"+noSelectCatTermNumber+")");
		}
		vCatRecallTerm=SetUtil.getNoRepeatVector(vCatRecallTerm);
		int recallNumber=vCatRecallTerm.size();
		int recallIsTermNumber=0;
		System.out.println("�ٻ������"+recallIsTermNumber+"/"+recallNumber);
		SetUtil.writeSetToFile(vCatRecallTerm, desTermListPath);
	}
	
	/**
	 * Ŀ¼�������ɸѡ�����ע��������Ľ���
	 */
	public void InterAnalysis(){
		Vector<String> vSelectTerm=SetUtil.readSetFromFile(selectTermPath);
		Vector<String> vTerm=SetUtil.readSetFromFile(termSetPath);
		Vector<String> vCatRecallTerm=new Vector<String>();
		File f=new File(categoryDir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String path=childs[i].getAbsolutePath();
			String name=childs[i].getName();
			String category=name.substring(0, name.lastIndexOf("_"));
			Vector<String> vCatString=SetUtil.readSetFromFile(path);
			Vector<String> vCatTerm=new Vector<String>();
			for(String temp:vCatString){
				String term[]=temp.split(",");
				vCatTerm.add(term[0]);
				vCatTerm.add(term[1]);
			}
			vCatTerm=SetUtil.getNoRepeatVector(vCatTerm);
			int sum=vCatTerm.size();
			Vector<String> vSelectCatTerm=SetUtil.getInterSet(vSelectTerm, vCatTerm);//ѡ���
			int selectCatTermNumber=vSelectCatTerm.size();//ѡ�������
			int selectCatIsTermNumber=SetUtil.getInterSet(vSelectCatTerm, vTerm).size();//ѡ��������������
			Vector<String> vNoSelectCatTerm=SetUtil.getSubSet(vCatTerm, vSelectCatTerm);//û��ѡ���
			int noSelectCatTermNumber=vNoSelectCatTerm.size();//ûѡ�������
			int noSelectCatIsTermNumber=SetUtil.getInterSet(vNoSelectCatTerm, vTerm).size();//ûѡ��������������
			vCatRecallTerm.addAll(vNoSelectCatTerm);
			System.out.println(category+":("+sum+"),("+selectCatIsTermNumber+"/"+selectCatTermNumber+"),("+noSelectCatIsTermNumber+"/"+noSelectCatTermNumber+")");
		}
		vCatRecallTerm=SetUtil.getNoRepeatVector(vCatRecallTerm);
		int recallNumber=vCatRecallTerm.size();
		int recallIsTermNumber=SetUtil.getInterSet(vCatRecallTerm, vTerm).size();
		System.out.println("�ٻ������"+recallIsTermNumber+"/"+recallNumber);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
