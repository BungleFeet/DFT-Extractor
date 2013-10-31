package function.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author MJ
 * @description �󼯺ϵ����ɲ���������: 1).���������ϵĽ��� 2).���������Ϻ��Դ�Сд�Ľ��� 3).���������ϵĲ���
 *              4).���������Ϻ��Դ�Сд�Ĳ��� 5).���������ϵĲ 6).��ĳ������ת��ΪСд 7).��ָ��·����ȡ����
 *              8).������д�뵽ָ��·�� 9).��Vector���ϵĲ����}���� 10).��HashSetת����Vector
 */
public class SetUtil {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Vector<String> v1 = new Vector<String>();
		Vector<String> v2 = new Vector<String>();
		v1.add("MaJian");
		v1.add("ZhaoTing");
		v1.add("baobao");
		v2.add("Majian");
		v2.add("Zhaoting");
		v2.add("baobao");
		Vector<String> inter = getInterSet(v1, v2);
		System.out.println("������");
		for (String s : inter) {
			System.out.println(s);
		}
		Vector<String> interIgnoreCase = getInterSetIgnoreCase(v1, v2);
		System.out.println("����(���Դ�Сд)��");
		for (String s : interIgnoreCase) {
			System.out.println(s);
		}
		Vector<String> union = getUnionSet(v1, v2);
		System.out.println("������");
		for (String s : union) {
			System.out.println(s);
		}
		Vector<String> unionIgnoreCase = getInterSetIgnoreCase(v1, v2);
		System.out.println("����(���Դ�Сд)��");
		for (String s : unionIgnoreCase) {
			System.out.println(s);
		}
		writeSetToFile(unionIgnoreCase, "f:/testSet.txt");*/
		Vector<String> select = readSetFromFile("F://layer1.txt");
		Vector<String> wcc_select = readSetFromFile("F://layer2.txt");
		Vector<String> interIgnoreCase = getSubSet(wcc_select,select);
		writeSetToFile(interIgnoreCase,"f://2.txt");
		System.out.print(interIgnoreCase);

	}
	
	/**
	 * 
	 * @param v1
	 *            ����V1
	 * @param v2
	 *            ����V2
	 * @return V1��V2�Ľ���
	 */
	public static Vector<String> getInterSet(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> interV = new Vector<String>();
		for (String s1 : v1) {
			if (v2.contains(s1) && !interV.contains(s1))
				interV.add(s1);
		}
		return interV;
	}

	/**
	 * 
	 * @param v1����V1
	 * @param v2����V2
	 * @return V1��V2���Դ�Сд�Ľ���
	 */
	public static Vector<String> getInterSetIgnoreCase(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> v1L = getLowerCaseSet(v1);
		Vector<String> v2L = getLowerCaseSet(v2);
		Vector<String> interV = new Vector<String>();
		for (String s1 : v1L) {
			if (v2L.contains(s1) && !interV.contains(s1))
				interV.add(s1);
		}
		return interV;
	}

	/**
	 * 
	 * @param v1
	 *            ����V1
	 * @param v2
	 *            ����V2
	 * @return V1��V2�Ĳ���
	 */
	public static Vector<String> getUnionSet(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> unionV = new Vector<String>();
		for (String s1 : v1) {
			if (!unionV.contains(s1))
				unionV.add(s1);
		}
		for (String s2 : v2) {
			if (!unionV.contains(s2))
				unionV.add(s2);
		}
		return unionV;
	}

	/**
	 * 
	 * @param v1
	 *            ����V1
	 * @param v2
	 *            ����V2
	 * @return V1��V2���Դ�Сд�Ĳ���
	 */
	public static Vector<String> getUnionSetIgnoreCase(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> v1L = getLowerCaseSet(v1);
		Vector<String> v2L = getLowerCaseSet(v2);
		Vector<String> unionV = new Vector<String>();
		for (String s1 : v1L) {
			if (!unionV.contains(s1))
				unionV.add(s1);
		}
		for (String s2 : v2L) {
			if (!unionV.contains(s2))
				unionV.add(s2);
		}
		return unionV;
	}

	/**
	 * 
	 * @param v1
	 *            ����v1
	 * @param v2
	 *            ����v2
	 * @return v1��v2�Ĳ������v1�е�����v2��
	 */
	public static Vector<String> getSubSet(Vector<String> v1, Vector<String> v2) {
		Vector<String> subV = new Vector<String>();
		for (String s1 : v1) {
			if (!v2.contains(s1))
				subV.add(s1);
		}
		return subV;
	}

	/**
	 * 
	 * @param v
	 *            Ҫת���ļ���
	 * @return vȫ����Сд��ļ���
	 */
	public static Vector<String> getLowerCaseSet(Vector<String> v) {
		Vector<String> lowerV = new Vector<String>();
		for (String s : v) {
			lowerV.add(s.toLowerCase());
		}
		return lowerV;
	}

	/**
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �Ӹ��ļ���ȡ���ַ�������
	 */
	public static Vector<String> readSetFromFile(String filePath) {
		Vector<String> v = new Vector<String>();
		FileReader fr;
		try {
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			while (s != null) {
				v.add(s);
				s = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * 
	 * @param v
	 *            Ҫд��ļ���
	 * @param filePath
	 *            д����ļ�·��
	 */
	public static void writeSetToFile(Vector<String> v, String filePath) {
		try {
			File f=new File(filePath);
			File fParent=f.getParentFile();
			fParent.mkdirs();
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String s : v) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param v
	 * @param filePath ��v�е�����׷�ӵ�ָ�����ļ���
	 */
	public static void appendSetToFile(Vector<String> v, String filePath) {
		try {
			FileWriter fw = new FileWriter(filePath,true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String s : v) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param v
	 *            ��Ҫ���ظ���Vector����
	 * @return v���ظ��ļ���
	 */
	public static Vector<String> getNoRepeatVector(Vector<String> v) {
		Vector<String> vNoRepeat = new Vector<String>();
		for (int i = 0; i < v.size(); i++) {
			String s = v.get(i);
			if (!vNoRepeat.contains(s))
				vNoRepeat.add(s);
		}
		return vNoRepeat;
	}
	
	/**
	 * 
	 * @param v
	 *            ��Ҫ���ظ���Vector����
	 * @return v ���Դ�Сд���ظ��ļ���
	 */
	public static Vector<String> getNoRepeatVectorIgnoreCase(Vector<String> v) {
		Vector<String> vNoRepeat = new Vector<String>();
		Vector<String> vIgnoreCase=new Vector<String>();
		for (int i = 0; i < v.size(); i++) {
			String s = v.get(i);
			if (!vNoRepeat.contains(s)&&!vIgnoreCase.contains(s.toLowerCase())){
				vNoRepeat.add(s);
				vIgnoreCase.add(s.toLowerCase());
			}
		}
		return vNoRepeat;
	}
	
	/**
	 * 
	 * @param hs
	 * @return ��hsת����Vector
	 */
	public static Vector<String> getVectorFromHashSet(HashSet<String> hs){
		Vector<String> v=new Vector<String>();
		Iterator<String> it=hs.iterator();
		while(it.hasNext()){
			v.add(it.next());
		}
		return v;
	}

}
