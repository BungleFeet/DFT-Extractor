package function.netAnalysis.motif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import function.base.ExcelBase;

/**
 * 
 * @author MJ
 * @description ����motif3��ֵ
 */
public class Motif3Example extends ExcelBase{
	
	String fileName="";
	String m3DumpFile="";
	HashMap<String, String[]> hmExample=new HashMap<String, String[]>();
	
	public Motif3Example(String m3XlsFile,String m3DumpFile){
		this.fileName=m3XlsFile;
		this.m3DumpFile=m3DumpFile;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Motif3Example me=new Motif3Example("f:/test.xls","F:\\Data\\����λ���ݼ�\\DM\\DM_motif\\DM2_m3\\DM2_Edge.txt.csv.dump");
		me.run(me);
	}
	
	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hmSrcTo = new HashMap<String, Integer>();
		HashMap<Integer, String> hmTermId = new HashMap<Integer, String>();
		int size =getRows();
		/******�����ǻ�ȡexcel��Ϣ*******/
		for (int i = 1; i < size; i++) {
			int srcID=getIntegerValue("srcID",i);
			int toID=getIntegerValue("toID",i);
			String sourceURLName=getStringValue("sourceURLName",i);
			String toURLName=getStringValue("toURLName",i);
			hmSrcTo.put(srcID+"-"+toID, i-1);
			hmTermId.put(srcID, sourceURLName);
			hmTermId.put(toID, toURLName);
		}// ��ȡexcel��Ϣ
		HashMap<Integer, Integer> hmTag = new HashMap<Integer, Integer>();
		int tag[][] = { {0,1,2},{2,0,1},{1,2,0},{0,2,1},{2,1,0},{1,0,2} };// ����˳���
		int sum = 0,m=1;// �Ҳ����ĸ���
		FileReader fR = new FileReader(m3DumpFile);
		BufferedReader bR = new BufferedReader(fR);
		String contentStr = bR.readLine();
		contentStr = bR.readLine();//����ǰ����
		contentStr = bR.readLine();
		while (contentStr != null) {
			m++;
			String s[] = contentStr.split(",");
			int A = Integer.parseInt(s[1]);
			int B = Integer.parseInt(s[2]);
			int C = Integer.parseInt(s[3]);
			contentStr=bR.readLine();
			char c;
			int x, y;
			int src, to;
			int i;
			boolean continueTag=false;
			for (i = 0; i < 6; i++) {// ����ÿ��˳��
				continueTag=false;
				int pos[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0};
				hmTag.clear();
				hmTag.put(tag[i][0], A);
				hmTag.put(tag[i][1], B);
				hmTag.put(tag[i][2], C);
				for (int j = 1; j < 10; j++) {// ���ÿ���ַ�
					c = s[0].charAt(j-1);
					if (c == '1') {
						x = (j - 1) / 3;
						y = (j - 1) % 3;
						src = hmTag.get(x);
						to = hmTag.get(y);
						String key=src+"-"+to;
						if(hmSrcTo.containsKey(key))
							pos[j - 1] =hmSrcTo.get(key);
						else{
							continueTag=true;
							break;// һ��û�ҵ����Ƴ�����˳��
						}
					}// end if
				}// end for���ÿ���ַ�
				if (continueTag)
					continue;
				else {
					if(hmExample.containsKey(s[0])){
						String term[]=hmExample.get(s[0]);
						String termTemp[]=new String[3];
						termTemp[0]=hmTermId.get(hmTag.get(0));
						termTemp[1]=hmTermId.get(hmTag.get(1));
						termTemp[2]=hmTermId.get(hmTag.get(2));
						if(termTemp[1].length()+termTemp[2].length()<term[1].length()+term[2].length())
							hmExample.put(s[0], termTemp);
						 break;
					}
					else{
						String termTemp[]=new String[3];
						termTemp[0]=hmTermId.get(hmTag.get(0));
						termTemp[1]=hmTermId.get(hmTag.get(1));
						termTemp[2]=hmTermId.get(hmTag.get(2));
					    hmExample.put(s[0], termTemp);
					    break;
					}
				}
			}// end for����ÿ��˳��
			if (i == 6) {
				System.out.println("�Ҳ���");
				sum++;
			}
		}//end while
		System.out.println("������ϣ�");
	}
	
	@Override
	public void run(ExcelBase eb) {
		// TODO Auto-generated method stub
		eb.go(fileName);
	}
}
