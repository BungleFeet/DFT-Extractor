package function.hypRelation;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import function.util.SetUtil;

public class WekaTrainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String trainFile="D:\\Program Files (x86)\\Weka-3-7\\data\\contact-lenses.arff";
		String testFile="D:\\Program Files (x86)\\Weka-3-7\\data\\contact-lenses.arff";
		String methods="weka.classifiers.trees.RandomForest";
		new WekaTrainTest().trainTest(trainFile,testFile,methods);

	}

	public HashMap<Integer,String> trainTest(String trainFile, String testFile, String method) {
		HashMap<Integer,String> hm=new HashMap<Integer,String>();//id-predictvalue
		Instances TrainIns = null;
		Instances TestIns = null;
		Classifier cfs = null;
		try {
			// (1)����ѵ��������csv��ʽ
			File trainF = new File(trainFile);
			CSVLoader trainLoader = new CSVLoader();
			trainLoader.setFile(trainF);
			TrainIns = trainLoader.getDataSet();
			// �������������csv��ʽ
			File testF = new File(testFile);
			CSVLoader testLoader = new CSVLoader();
			testLoader.setFile(testF);
			TestIns = testLoader.getDataSet();

			// ��ʹ������֮ǰһ��Ҫ��������instances��classIndex��������ʹ��instances�����ǻ��׳��쳣
			TrainIns.setClassIndex(TrainIns.numAttributes() - 1);
			TestIns.setClassIndex(TestIns.numAttributes() - 1);

			// ��ʼ�������� ���뽫�ض���������class���Ʒ���forName����
			cfs = (Classifier) Class.forName(method).newInstance();

			// (2)ʹ��ѵ������ѵ��������
			cfs.buildClassifier(TrainIns);

			// (3)ʹ�ò����������Է�������ѧϰЧ��
			Instance testInst;

			//�������Լ����ǩ��ӳ�䣬������trainFile
			HashMap<Double,String> hmClass=buildClassMap(trainFile);
			// Evaluation���������ڼ�����ģ�͵���
			Evaluation testingEvaluation = new Evaluation(TestIns);
			int length = TestIns.numInstances();
			for (int i = 0; i < length; i++) {
				testInst = TestIns.instance(i);
				// ͨ�������������ÿ�������������Է�������Ч��
				testingEvaluation.evaluateModelOnceAndRecordPrediction(cfs,
						testInst);
				int id=(int)testInst.value(testInst.attribute(0));
				String classLabel=testInst.stringValue(testInst.classIndex());//����˳������û��Ҫ����
				String predictClassLabel=hmClass.get(cfs.classifyInstance(testInst));
				System.out.println(id+","+classLabel+","+predictClassLabel);
				hm.put(id, predictClassLabel);
			}

			// (4)��ӡ������ ���������Ǵ�ӡ�˷���������ȷ�� ������һЩ��Ϣ���ǿ���ͨ��Evaluation��������������õ�
			System.out
					.println("����������ȷ�ʣ�" + (1 - testingEvaluation.errorRate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hm;
	}
	
	/**
	 * ����֪�ļ��й������ǩ��ӳ��
	 * @param file
	 * @return
	 */
	public HashMap<Double,String> buildClassMap(String file){
		Vector<String> v=SetUtil.readSetFromFile(file);
		HashMap<Double,String> hmResult=new HashMap<Double, String>();
		HashMap<String,Double> hm=new HashMap<String, Double>();
		double classNumber=0.0;
		for(int i=1;i<v.size();i++){
			String s=v.get(i);
			String att[]=s.split(",");
			String classLabel=att[att.length-1];
			if(!hm.containsKey(classLabel)){
				hm.put(classLabel, classNumber);
				hmResult.put(classNumber, classLabel);
				classNumber+=1.0;
			}
		}
		return hmResult;
	}

}
