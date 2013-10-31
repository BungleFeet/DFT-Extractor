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
public class AddMotif3 extends ExcelBase{
	
	String fileName="";
	String m3EdgeFile="";
	
	public AddMotif3(String m3XlsFile,String m3EdgeFile){
		this.fileName=m3XlsFile;
		this.m3EdgeFile=m3EdgeFile;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AddMotif3 am=new AddMotif3("f:/test.xls","F:\\Data\\����λ���ݼ�\\DM\\DM_motif\\DM2_m3\\DM2_Edge.txt.csv.dump");
		am.run(am);
	}
	
	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hmHead = new HashMap<String, Integer>();
		HashMap<String, Integer> hmSrcTo = new HashMap<String, Integer>();
		int size =getRows();
		int matrix[][] = new int[size - 1][15];
		/****** ���������motifAtt3Name��hm�� ******/
		String[] m3Head = {"m000000110", "m000001100", "m000001110",
				"m000100100", "m000100110", "m000101110", "m001001110",
				"m001100110", "m010001100", "m010100100", "m010100110",
				"m010101110", "m011101110" };
		for (int i = 0; i < m3Head.length; i++) {
			System.out.println(m3Head[i]+"------"+i);
			hmHead.put(m3Head[i], i+2);
		}//
		/******�����ǳ�ʼ��ÿ�������θ���Ϊ0*******/
		for (int i = 0; i < size - 1; i++) {
			for (int j = 2; j < 15; j++) {
				matrix[i][j] = 0;
			}// ��ʼ��
		}
		for (int i = 1; i < size; i++) {
			int srcID=getIntegerValue("srcID",i);
			int toID=getIntegerValue("toID",i);
			matrix[i - 1][0] = srcID;
			matrix[i - 1][1] = toID;
			hmSrcTo.put(srcID+"-"+toID, i-1);
		}// ����source��to����������
		HashMap<Integer, Integer> hmTag = new HashMap<Integer, Integer>();
		int tag[][] = { {0,1,2},{2,0,1},{1,2,0},{0,2,1},{2,1,0},{1,0,2} };// ����˳���
		int sum = 0,m=1;// �Ҳ����ĸ���
		FileReader fR = new FileReader(m3EdgeFile);
		BufferedReader bR = new BufferedReader(fR);
		String contentStr = bR.readLine();
		contentStr = bR.readLine();//����ǰ����
		contentStr = bR.readLine();
		while (contentStr != null) {
			m++;
			String s[] = contentStr.split(",");
			String AttName = "m".concat(s[0]);
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
					c = AttName.charAt(j);
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
					for (int j = 1; j < 10; j++) {
						c = AttName.charAt(j);
						if (c == '1'){
							matrix[pos[j - 1]][hmHead.get(AttName)]++;
						}
							
					}
					break;
				}
			}// end for����ÿ��˳��
			if (i == 6) {
				System.out.println("�Ҳ���");
				sum++;
			}
		}//end while
		/*********** ������д����� ************/
		for (int i = 1; i < size; i++) {
			for (int j = 0; j < 13; j++) {
				setIntegerValue(m3Head[j],i,matrix[i - 1][j +2]);
			}
		}
		System.out.print("��"+m+"�����Ҳ���������"+sum);
		fR.close();
		bR.close();
		/************** �رղ��� ****************/
		System.out.println("������ϣ�");
	}
	
	@Override
	public void run(ExcelBase eb) {
		// TODO Auto-generated method stub
		eb.go(fileName);
	}
}
