package function.netAnalysis.motif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import function.base.ExcelBase;

/**
 * 
 * @author MJ
 * @description 计算motif4数值
 */
public class AddMotif4 extends ExcelBase{
	
	String fileName="";
	String m4EdgeFile="";
	
	public AddMotif4(String m4XlsFile,String m4EdgeFile){
		this.fileName=m4XlsFile;
		this.m4EdgeFile=m4EdgeFile;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Integer> hmHead = new HashMap<String, Integer>();
		HashMap<String, Integer> hmSrcTo = new HashMap<String, Integer>();
		int size =getRows();
		int matrix[][] = new int[size - 1][201];
		/****** 以下是添加motifAtt4Name到hm中 ******/
		String[] m4Head = { "m0000000000001110", "m0000000000011100",
				"m0000000000011110", "m0000000001001010", "m0000000010001100",
				"m0000000010001110", "m0000000010010110", "m0000000010011110",
				"m0000000011001010", "m0000000011001100", "m0000000011001110",
				"m0000000011011110", "m0000000100011100", "m0000000100011110",
				"m0000000101001100", "m0000000101001110", "m0000000101011110",
				"m0000000110000110", "m0000000110001000", "m0000000110001100",
				"m0000000110001110", "m0000000110011110", "m0000000111001110",
				"m0000001000011000", "m0000001000011100", "m0000001001001100",
				"m0000001001001110", "m0000001001011010", "m0000001001011100",
				"m0000001001011110", "m0000001010001100", "m0000001010010110",
				"m0000001010011010", "m0000001010011100", "m0000001010011110",
				"m0000001011000110", "m0000001011001010", "m0000001011001100",
				"m0000001011001110", "m0000001011011110", "m0000001101011010",
				"m0000001101011110", "m0000001110011100", "m0000001111001100",
				"m0000001111001110", "m0000001111011110", "m0000100000011100",
				"m0000100001000100", "m0000100001000110", "m0000100001001100",
				"m0000100001001110", "m0000100001010110", "m0000100001011100",
				"m0000100001011110", "m0000100010000110", "m0000100010001000",
				"m0000100010001100", "m0000100010001110", "m0000100010010110",
				"m0000100010011010", "m0000100010011100", "m0000100010011110",
				"m0000100011001100", "m0000100011001110", "m0000100011011110",
				"m0000100101011110", "m0000100110011010", "m0000100110011110",
				"m0000100111001110", "m0000101010011100", "m0000101011000110",
				"m0000101011001110", "m0000101011011110", "m0000101111011110",
				"m0001000100011100", "m0001000100011110", "m0001000101010110",
				"m0001000101011010", "m0001000110001100", "m0001000110001110",
				"m0001000110011110", "m0001000111001010", "m0001000111001110",
				"m0001000111011110", "m0001001000011100", "m0001001001001110",
				"m0001001001010110", "m0001001010001100", "m0001001010011110",
				"m0001001011001010", "m0001001011001110", "m0001001101011110",
				"m0001001111001110", "m0001100001001110", "m0001100010001100",
				"m0001100010001110", "m0001100010011110", "m0001100011001110",
				"m0001100101011110", "m0001100110011110", "m0001100111001110",
				"m0001101011001110", "m0001101011011110", "m0010000101011010",
				"m0010000110001100", "m0010000110010110", "m0010000110011110",
				"m0010000111001010", "m0010000111001100", "m0010000111011110",
				"m0010001000011100", "m0010001010001100", "m0010001010011110",
				"m0010001011001010", "m0010001011001100", "m0010001011001110",
				"m0010001111011110", "m0010100001001000", "m0010100010000100",
				"m0010100010001100", "m0010100010011110", "m0010101011011110",
				"m0011001110011110", "m0011001111001110", "m0011001111011110",
				"m0011101001011110", "m0011101011001100", "m0100000100011100",
				"m0100000110001100", "m0100000110011110", "m0100000111000110",
				"m0100000111001010", "m0100001000011000", "m0100001000011100",
				"m0100001001010110", "m0100001001011010", "m0100001010001100",
				"m0100001010001110", "m0100001010010110", "m0100001010011010",
				"m0100001010011110", "m0100001011000110", "m0100001011001010",
				"m0100001011001100", "m0100001011001110", "m0100001011011110",
				"m0100001101010110", "m0100001101011100", "m0100001101011110",
				"m0100001110010110", "m0100001110011010", "m0100001110011100",
				"m0100001110011110", "m0100001111001100", "m0100001111001110",
				"m0100001111011110", "m0100100000011100", "m0100100001001000",
				"m0100100001010110", "m0100100010001000", "m0100100010001100",
				"m0100100010001110", "m0100100010010110", "m0100100010011110",
				"m0100100011001010", "m0100100011001100", "m0100100011001110",
				"m0100100011011110", "m0100100101011110", "m0100100110011110",
				"m0100100111001110", "m0100101001010110", "m0100101001011010",
				"m0100101001011100", "m0100101001011110", "m0100101010010110",
				"m0100101010011010", "m0100101010011100", "m0100101010011110",
				"m0100101011000110", "m0100101011001010", "m0100101011001100",
				"m0100101011001110", "m0100101011011110", "m0100101111011110",
				"m0101001110011110", "m0101100111011110", "m0110001101011110",
				"m0110001110011100", "m0110001111011110", "m0110100110010110",
				"m0110100111001010", "m0110100111001100", "m0110100111011110",
				"m0110101010011110", "m0110101011001100", "m0110101011001110",
				"m0110101111011110", "m0111101111011110" };
		for (int i = 0; i < m4Head.length; i++) {
			System.out.println(m4Head[i]+"------"+i);
			hmHead.put(m4Head[i], i+2);
		}//
		/******以下是初始化每个四边形个数为0*******/
		for (int i = 0; i < size - 1; i++) {
			for (int j = 2; j < 201; j++) {
				matrix[i][j] = 0;
			}// 初始化
		}
		for (int i = 1; i < size; i++) {
			int srcID=getIntegerValue("srcID",i);
			int toID=getIntegerValue("toID",i);
			matrix[i - 1][0] = srcID;
			matrix[i - 1][1] = toID;
			hmSrcTo.put(srcID+"-"+toID, i-1);
		}// 拷贝source和to到矩阵里面
		HashMap<Integer, Integer> hmTag = new HashMap<Integer, Integer>();
		int tag[][] = { { 0, 1, 2, 3 }, { 3, 0, 1, 2 }, { 2, 3, 0, 1 },
				{ 1, 2, 3, 0 }, { 0, 1, 3, 2 }, { 0, 2, 1, 3 },
				{ 0, 2, 3, 1 }, { 0, 3, 1, 2 }, { 0, 3, 2, 1 },
				{ 3, 0, 2, 1 }, { 3, 1, 0, 2 }, { 3, 1, 2, 0 },
				{ 3, 2, 0, 1 }, { 3, 2, 1, 0 }, { 2, 3, 1, 0 },
				{ 2, 0, 1, 3 }, { 2, 0, 3, 1 }, { 2, 1, 0, 3 },
				{ 2, 1, 3, 0 }, { 1, 2, 0, 3 }, { 1, 0, 2, 3 },
				{ 1, 0, 3, 2 }, { 1, 3, 0, 2 }, { 1, 3, 2, 0 } };// 保存顺序表
		int sum = 0,m=1;// 找不到的个数
		FileReader fR = new FileReader(m4EdgeFile);
		BufferedReader bR = new BufferedReader(fR);
		String contentStr = bR.readLine();
		contentStr = bR.readLine();//跳过前两行
		contentStr = bR.readLine();
		while (contentStr != null) {
			m++;
			String s[] = contentStr.split(",");
			String AttName = "m".concat(s[0]);
			int A = Integer.parseInt(s[1]);
			int B = Integer.parseInt(s[2]);
			int C = Integer.parseInt(s[3]);
			int D = Integer.parseInt(s[4]);
			contentStr=bR.readLine();
			char c;
			int x, y;
			int src, to;
			int i;
			boolean continueTag=false;
			for (i = 0; i < 24; i++) {// 遍历每种顺序
				continueTag=false;
				int pos[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0 };
				hmTag.clear();
				hmTag.put(tag[i][0], A);
				hmTag.put(tag[i][1], B);
				hmTag.put(tag[i][2], C);
				hmTag.put(tag[i][3], D);
				for (int j = 1; j < 17; j++) {// 针对每个字符
					c = AttName.charAt(j);
					if (c == '1') {
						x = (j - 1) / 4;
						y = (j - 1) % 4;
						src = hmTag.get(x);
						to = hmTag.get(y);
						String key=src+"-"+to;
						if(hmSrcTo.containsKey(key))
							pos[j - 1] =hmSrcTo.get(key);
						else{
							continueTag=true;
							break;// 一个没找到，推出整个顺序
						}
					}// end if
				}// end for针对每个字符
				if (continueTag)
					continue;
				else {
					for (int j = 1; j < 17; j++) {
						c = AttName.charAt(j);
						if (c == '1'){
							matrix[pos[j - 1]][hmHead.get(AttName)]++;
						}
							
					}
					break;
				}
			}// end for遍历每种顺序
			if (i == 24) {
				System.out.println("找不到");
				sum++;
			}
		}//end while
		/*********** 下面是写入个数 ************/
		for (int i = 1; i < size; i++) {
			for (int j = 0; j < 199; j++) {
				setIntegerValue(m4Head[j],i,matrix[i - 1][j +2]);
			}
		}
		System.out.print("共"+m+"个，找不到个数："+sum);
		fR.close();
		bR.close();
		/************** 关闭操作 ****************/
		System.out.println("处理完毕！");
	}

	@Override
	public void run(ExcelBase eb) {
		// TODO Auto-generated method stub
		eb.go(fileName);
	}
}
