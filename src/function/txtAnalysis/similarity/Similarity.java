package function.txtAnalysis.similarity;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import function.txtAnalysis.TFIDF;
import function.txtAnalysis.Vectorization;
import function.util.SetUtil;

public class Similarity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String base = "F:\\Data\\�����ȡ���ݼ�\\DMDataSet\\DM_experiment\\removeNoise\\";
		String vectorDir = base + "layer3-new-txt_wordVector";
		Similarity cs = new Similarity();
		String listAPath = base + "listA.txt";
		String listBPath = base + "listB.txt";
		Vector<String> vListA = SetUtil.readSetFromFile(listAPath);
		Vector<String> vListB = SetUtil.readSetFromFile(listBPath);
		cs.computeSimilarity(vectorDir, 2, vListA, vListB);
	}

	/**
	 * 
	 * @param txtHtmlDir �ı�����ҳ·��
	 * @param listAPath �б�A��·��
	 * @param listBPath �б�B��·��
	 * @param tfidfTag Ȩ�ط���
	 * @param vectorMethod �������ķ���,0��ʾ����������1��ʾ����������
	 * @return
	 */
	public HashMap<String[], Double> computeSimilarity(String txtHtmlDir,
			String listAPath, String listBPath, int tfidfTag, int vectorMethod) {
		/************ ������ *******************/
		Vectorization vz = new Vectorization();
		String wordVectorPath = "";
		if (vectorMethod == 0)
			wordVectorPath = vz.wordVector(txtHtmlDir);
		else if (vectorMethod == 1)
			wordVectorPath = vz.wikiVector(txtHtmlDir);
		/************ ��ȡlist�б� *******************/
		Vector<String> vAllList = new Vector<String>();
		Vector<String> vAList = null;
		Vector<String> vBList = null;
		File f = new File(wordVectorPath);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			String fileName = childs[i].getName();
			String term = fileName.substring(0, fileName.lastIndexOf("."));
			vAllList.add(term);
		}
		if (listAPath.length() == 0 || listAPath == null) {
			vAList = new Vector<String>();
			vAList.addAll(vAllList);
		}// û��ָ���б����ȫ��
		else
			vAList = SetUtil.readSetFromFile(listAPath);
		if (listBPath.length() == 0 || listBPath == null) {
			vBList = new Vector<String>();
			vBList.addAll(vAllList);
		} else
			vBList = SetUtil.readSetFromFile(listBPath);
		/************ ���ƶȼ��� *******************/
		HashMap<String[], Double> hmResult = computeSimilarity(wordVectorPath,
				tfidfTag, vAList, vBList);
		return hmResult;
	}

	/**
	 * 
	 * @param vectorDir
	 *            �����ļ���
	 * @param tfidfTag
	 *            Ȩ�ر�ǣ�0��ʾtf��1��ʾidf��2��ʾtf*idf
	 * @param vListA
	 *            ��vListB ��Ҫ������б�A��B
	 * @return ͬʱ�ѽ��д����ͬ��Ŀ¼��-similarity.csv��
	 */
	public HashMap<String[], Double> computeSimilarity(String vectorDir,
			int tfidfTag, Vector<String> vListA, Vector<String> vListB) {
		String desPath = vectorDir + "-similarity.csv";
		Vector<String> vResult = new Vector<String>();
		vResult.add("termA,termB,similarity");
		HashMap<String[], Double> hmResult = new HashMap<String[], Double>();
		// ����word
		Vector<String> vAllWord = new Vector<String>();
		HashSet<String> hsAllWord = new HashSet<String>();
		TFIDF ti = new TFIDF();
		// �ļ��ͣ��ʡ���Ȩ�أ��Ķ�Ӧ��ϵ
		System.out.println("���������Ȩ�ء���");
		HashMap<String, HashMap<String, double[]>> hmFileWordWeight = ti
				.computeTFIDF_app(vectorDir);
		// ��ȡ�ļ�������word��λ�õĶ�Ӧ��ϵ
		System.out.println("��ȡ�ļ�������word��λ�õĶ�Ӧ��ϵ����");
		File f = new File(vectorDir);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			String fileName = childs[i].getName();
			String filePath = childs[i].getAbsolutePath();
			Vector<String> vWord = SetUtil.readSetFromFile(filePath);
			hsAllWord.addAll(vWord);
			System.out.println(fileName + "����" + i + "/" + childs.length);
		}// end for
		vAllWord.addAll(hsAllWord);
		System.out.println("�������ƶȡ���");
		int vectorSize = vAllWord.size();
		Iterator<String> iti = hmFileWordWeight.keySet().iterator();
		while (iti.hasNext()) {
			Iterator<String> itj = hmFileWordWeight.keySet().iterator();
			String titlei = iti.next();
			String termi = titlei.substring(0, titlei.lastIndexOf("."));
			if (!vListA.contains(termi))
				continue;
			HashMap<String, double[]> hmVectori = hmFileWordWeight.get(titlei);
			double weighti[] = new double[vectorSize];
			for (int i = 0; i < vectorSize; i++) {
				String word = vAllWord.get(i);
				if (hmVectori.containsKey(word))
					weighti[i] = hmVectori.get(word)[tfidfTag];
				else
					weighti[i] = 0;
			}
			while (itj.hasNext()) {
				String titlej = itj.next();
				String termj = titlej.substring(0, titlej.lastIndexOf("."));
				if (!vListB.contains(termj) || termj.equals(termi))
					continue;
				HashMap<String, double[]> hmVectorj = hmFileWordWeight
						.get(titlej);
				double weightj[] = new double[vectorSize];
				for (int i = 0; i < vectorSize; i++) {
					String word = vAllWord.get(i);
					if (hmVectorj.containsKey(word))
						weightj[i] = hmVectorj.get(word)[tfidfTag];
					else
						weightj[i] = 0;
				}
				double similarity = cosSimilarity(
						new WordVector(termi, weighti), new WordVector(termj,
								weightj));
				System.out.println(termi + "����" + termj + ":" + similarity);
				vResult.add(termi + "," + termj + "," + similarity);
				String term[]={termi,termj};
				hmResult.put(term, similarity);
			}
		}// end while
		SetUtil.writeSetToFile(vResult, desPath);
		System.out.println("OK");
		return hmResult;
	}

	/**
	 * 
	 * @param vA
	 * @param vB
	 * @return �����������������������ƶ�
	 */
	public double cosSimilarity(WordVector vA, WordVector vB) {
		double weightA[] = vA.weight;
		double weightB[] = vB.weight;
		double result = 0;
		for (int i = 0; i < weightA.length; i++) {
			result += weightA[i] * weightB[i];
		}
		result = result / (vA.mod * vB.mod);
		return result;
	}

	/**
	 * 
	 * @author MJ
	 * @description �������ڲ���
	 */
	public class WordVector {
		String term = "";// ��������
		double weight[] = {};// Ȩ��
		double mod = 0;// Ȩ�ص�ģ

		public WordVector(String term, double weight[]) {
			this.term = term;
			this.weight = weight;
			for (int i = 0; i < weight.length; i++) {
				this.mod += weight[i] * weight[i];
			}
			this.mod = Math.sqrt(this.mod);
		}
	}
}
