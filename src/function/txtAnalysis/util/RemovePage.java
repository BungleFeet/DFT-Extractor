package function.txtAnalysis.util;

import java.io.File;
import java.util.Vector;

import function.util.SetUtil;

/**
 * 
 * @author MJ
 * @description ����ָ���б�ɾ����ҳ
 */
public class RemovePage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String dir="F:\\Data\\�����ȡ���ݼ�\\DMDataSet\\DM_experiment\\removeNoise\\layer3-new\\";
		String delListPath="F:\\Data\\�����ȡ���ݼ�\\DMDataSet\\DM_experiment\\removeNoise\\delList.txt";
		Vector<String> vDelTerm=SetUtil.readSetFromFile(delListPath);
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName().replace(".html", "");
			if(!vDelTerm.contains(fileName)){
				childs[i].deleteOnExit();
				System.out.println(fileName);
			}
		}
	}

}
