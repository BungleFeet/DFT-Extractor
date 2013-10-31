package function.txtAnalysis;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Vector;

import function.util.FileUtil;
import function.util.SetUtil;

/**
 * html��ҳ������
 * @author MJ
 * @description
 */
public class Vectorization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String txtDir="F:\\Data\\�����ȡ���ݼ�\\DMDataSet\\DM_experiment\\removeNoise\\layer3-new-txt";
		Vectorization vz=new Vectorization();
		vz.wordVector(txtDir);
	}
	/**
	 * ��txt�ļ��н���������
	 * @param txtDir
	 * @return �ı�����������ļ���·��
	 */
	public String wordVector(String txtDir) {
		String vectorDir = txtDir + "_wordVector";
		File fSave = new File(vectorDir);
		fSave.mkdirs();
		File f = new File(txtDir);
		File childs[] = f.listFiles();
		int size=childs.length;
		for (int i = 0; i <size; i++) {
			String srcPath = childs[i].getAbsolutePath();
			String fileName = childs[i].getName();
			String desPath = vectorDir +"/"+ fileName;
			Vector<String> vTerm = getWord(srcPath);
			SetUtil.writeSetToFile(vTerm, desPath);
			System.out.println("��������"+fileName+"--------("+(i+1)+"/"+size+")");
		}
		return vectorDir;
	}
	public Vector<String> getWord(String txtPath) {
		URL stopWordFile=getClass().getResource("/resources/stopWord.txt");
		Vector<String> vStopWord=SetUtil.readSetFromFile(stopWordFile.toString());
		Vector<String> vTxtString =SetUtil.readSetFromFile(txtPath);
		Vector<String> vWord = new Vector<String>();
		for(String txtString:vTxtString){
			int p=0,q=0;
			while(p<txtString.length()&&q<txtString.length()){
				if(isWord(txtString.charAt(p))){
					q=p+1;
					while(q<txtString.length()&&isWord(txtString.charAt(q)))q++;
					String word=txtString.substring(p, q).toLowerCase();
					if(!isStopWord(word,vStopWord)&&!isNumber(word)&&word.length()>1){//����ͣ�ô�
						vWord.add(word);//�ŵ�vector��
					}
					p=q+1;
				}
				else p++;
			}//end while
		}
		return vWord;
	}
	public boolean isWord(char c){
		if(c>='a'&&c<='z'||c>='A'&&c<='Z'||c>='0'&&c<='9')return true;
		else return false;
	}//�б��ַ�����
	public boolean isNumber(String s){
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)>='0'&&s.charAt(i)<='9')continue;
			else return false;
		}
		return true;
	}//�б��ַ�����
	public boolean isStopWord(String s,Vector<String> vStopWord){
		for(int i=0;i<vStopWord.size();i++){
			if(s.equals(vStopWord.get(i)))return true;
		}
		return false;
	}//�б�ͣ�ôʺ���
	/**
	 * ��html�ļ��н���������
	 * @param htmlDir
	 * @return �ı�����������ļ���·��
	 */
	public String wikiVector(String htmlDir) {
		String vectorDir = htmlDir + "_wikiVector";
		File fSave = new File(vectorDir);
		fSave.mkdirs();
		File f = new File(htmlDir);
		File childs[] = f.listFiles();
		int size=childs.length;
		for (int i = 0; i <size; i++) {
			String srcPath = childs[i].getAbsolutePath();
			String fileName = childs[i].getName().replace(".html", ".txt");
			String desPath = vectorDir +"/"+ fileName;
			Vector<String> vTerm = getWikiTerm(srcPath);
			SetUtil.writeSetToFile(vTerm, desPath);
			System.out.println("��������"+fileName+"--------("+(i+1)+"/"+size+")");
		}
		return vectorDir;
	}

	/**
	 * ��ȡָ��·����wikiҳ��Ĵ���
	 * @param filePath
	 * @return
	 */
	public Vector<String> getWikiTerm(String filePath) {
		String s = FileUtil.readFile(filePath);
		try {
			s=new String(s.getBytes("gbk"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Vector<String> vRemoveTag = new Vector<String>();
		vRemoveTag.add("History");
		vRemoveTag.add("External links");
		vRemoveTag.add("Further reading");
		vRemoveTag.add("Software");
		vRemoveTag.add("References");
		Vector<int[]> vInterval = getInterval(s, vRemoveTag);
		Vector<String> vTerm = new Vector<String>();
		String wikiTag = "a href=\"/wiki/";
		int posA = s.indexOf(wikiTag);
		int posB = 0;
		while (posA != -1) {
			posB = s.indexOf("\"", posA + wikiTag.length());
			String temp = s.substring(posA + wikiTag.length(), posB);
			if (temp.contains("#"))
				temp = temp.substring(0, temp.indexOf("#"));
			if (existInAmong(posA, vInterval) == false&&!temp.contains(":")&&!temp.equals("Main_Page"))
				vTerm.add(temp);
			posA = s.indexOf(wikiTag, posB);
		}
		return vTerm;
	}

	/**
	 * 
	 * @param s
	 * @param vRemoveTag
	 * @return ��ȡ��ҳ�ַ���removeTag������λ�ã�����History
	 */
	public Vector<int[]> getInterval(String s, Vector<String> vRemoveTag) {
		Vector<int[]> v = new Vector<int[]>();
		for (String tag : vRemoveTag) {
			String t2Tag = tag + "</span></h2>";
			if (s.contains(t2Tag)) {
				int interval[] = new int[2];
				interval[0] = s.indexOf(t2Tag);
				int pos = s.indexOf("</h2>", interval[0] + t2Tag.length());
				if (pos != -1)
					interval[1] = pos;
				else
					interval[1] = s.length();
				v.add(interval);
			}
		}
		return v;
	}

	/**
	 * 
	 * @param pos
	 * @param vInterval
	 * @return �жϸ�����pos�Ƿ��ڸ�����������������
	 */
	public boolean existInAmong(int pos, Vector<int[]> vInterval) {
		for (int i = 0; i < vInterval.size(); i++) {
			int interval[] = vInterval.get(i);
			if (pos > interval[0] && pos < interval[1]) {
				return true;
			} else
				continue;
		}
		return false;
	}

}
