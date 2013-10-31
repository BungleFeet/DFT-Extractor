package function.DTExtraction;

import java.io.File;

import function.util.FileUtil;

public class DTExtractor {
	/**
	 * @param args
	 */
	private String basePath="f:/FacetedTaxonomy/";//�����ȡ�ĳ�ʼλ��
	private String DTPath="";//����������·��
	private String DomainTerm="";//��������
	private double processId = 0;// ���̿��Ʊ�ǩ
	private String processIdFile = "";//���̿����ļ�·��
	private String catPath="";//����Ŀ¼�ļ���
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public DTExtractor(String DomainTerm){
		this.DomainTerm=DomainTerm;
		this.DTPath=basePath+DomainTerm+"/html";
		this.catPath=basePath+DomainTerm+"/category";
		File f=new File(DTPath);
		f.mkdirs();
		File fCat=new File(catPath);
		fCat.mkdirs();
		this.processIdFile = DTPath + "/processId.txt";
		File fProcess = new File(processIdFile);
		if (!fProcess.exists())
			FileUtil.writeStringFile("0", processIdFile);
	}
	
	public double getProcessId() {
		double id = Double.valueOf(FileUtil.readFile(processIdFile));
		processId = id;
		return processId;
	}

	public void setProcessId(double processId) {
		this.processId = processId;
		String id = String.valueOf(processId);
		FileUtil.writeStringFile(id, processIdFile);
	}
	
	public void extract(){
		//layer1
		Layer1Extractor extractor1=new Layer1Extractor(basePath,DomainTerm);
		extractor1.extract();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DTExtractor dte=new DTExtractor("Computer_network");
		dte.extract();
	}

}
