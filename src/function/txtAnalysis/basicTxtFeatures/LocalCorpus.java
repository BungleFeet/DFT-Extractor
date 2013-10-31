package function.txtAnalysis.basicTxtFeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
/*
 * 1���Ա��ص�HTML��Text���в�������Ҫ�Ƕ��롣
 * 2�����������Ｏ���в�������ȡ��д�룬������
 */
public class LocalCorpus {
	MOREConfig mc=null;
	public Vector<String> TermSet=new Vector<String>();
	Set<String> lowercaseTermSet = new HashSet<String>();
	
	public LocalCorpus(MOREConfig mc){
		this.mc=mc;
	}
	
	public boolean isTerm(String term){
		
		return lowercaseTermSet.contains(term.toLowerCase());
	}
	
	public String getTrueTerm(String term){
		for(String temp:TermSet){
			if(temp.equalsIgnoreCase(term.toLowerCase())){
				return temp;
			}
		}
		return term;
	}
	
	//���ı��ļ��õ��������Ｏ���ļ���word list��ÿ��һ����
	public Vector<String> getTermSetFromFile(String filePath){
		FileReader fr;		
		try {
			fr = new FileReader(filePath);
			System.out.println("��ȡ�ļ�����  "+ filePath);
			BufferedReader br = new BufferedReader(fr);
				
			String s;
			TermSet.removeAllElements();
			while ((s = br.readLine()) != null) {
				TermSet.add(s);
				lowercaseTermSet.add(s.toLowerCase());
			}
			System.out.println("����TermSet�ɹ�������="+ TermSet.size());			
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return TermSet;
	}
	//��ȡ����Ŀ¼��html�ļ�������Ϊ�������Ｏ��
	public Vector<String> getFileNamesFromDirectory(String dir){
		File f = new File(dir);
		File[] childs = f.listFiles();
		System.out.println("��ȡĿ¼����  "+ dir);
		TermSet.removeAllElements();
		for(File file:childs){
			String fileName = file.getName();
			String realName = fileName.substring(0, fileName.length() - 5);
			TermSet.add(realName);
			lowercaseTermSet.add(realName.toLowerCase());
		}
		System.out.println("����TermSet�ɹ�������="+ TermSet.size());	
		return TermSet;
	}
	//�����Ｏ���text�ļ���word list��ʽ
	public void storeTermSet(String filePath){
		FileWriter fw=null;
		try {
			fw = new FileWriter(filePath);
			System.out.println("TermSet���浽��"+filePath);
			for(String term:TermSet)
				//������Сд����wiki��ȡʱ����ҳ������
				fw.write(term+"\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TermSet������ϡ�������"+TermSet.size());
	}
	

	
	//�����ļ�������Ŀ¼������չ�����ӱ��ض�ȡ�ļ����ݵ��ڴ滺����
	public StringBuffer getHtmlFileBuffer(String term){
		String fileName=mc.htmlPath +"/"+term+".html";		
		return getFileBuffer(fileName,null);
	}
	public StringBuffer getTextFileBuffer(String term){
		String fileName=mc.txtPath +"/"+term+".txt";		
		return getFileBuffer(fileName,null);
	}
	//ParaRange����ָ��ÿ���Σ��У��Ľ���λ�ã��ϸ���˵���¸��εĿ�ʼλ�á�
	//Ԫ��0��Զ������ļ���С��
	public StringBuffer getTextFileBuffer(String term,Vector<Integer> ParaRange){
		String fileName=mc.txtPath +"/"+term+".txt";
		return getFileBuffer(fileName,ParaRange);
	}
	public StringBuffer getFileBuffer(String fileName,Vector<Integer> ParaRange){
		FileReader fr;
		StringBuffer sb=null;
		try {
			fr = new FileReader(fileName);
			System.out.println("��ȡ�ļ�����  "+ fileName);
			BufferedReader br = new BufferedReader(fr);
			sb = new StringBuffer();	
			if(ParaRange!=null)ParaRange.add(sb.length());
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
				if(ParaRange!=null)ParaRange.add(sb.length());
			}
			System.out.println("��ȡ�ļ����������ɹ����� �ļ���С="+ sb.length());			
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return sb;
	}

}
