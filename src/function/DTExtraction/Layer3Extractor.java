package function.DTExtraction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import function.crawler.WebCrawler;
import function.crawler.WikiCategoryCrawler;
import function.hypRelation.ExtractCategory;
import function.util.FileUtil;
import function.util.SetUtil;

public class Layer3Extractor {

	private String DTPath = "";// ����������·��
	private String domainName = "";// ��������
	// html
	private String htmlPath = "";// html���·��
	private String layer1SelectHtmlPath = "";// ��1��ѡ���ҳ��·��
	private String layer2SelectHtmlPath = "";// �ڶ���ѡ���ҳ��·��
	public String layer12SelectHtmlPath = "";// 1,2��ѡ���ҳ��·��
	public String layer3CatCrawlHtmlPath = "";// ��3�����ѡ���Ŀ¼��ȡ��ҳ��·��

	// process
	private String processPath = "";// ��������ļ����·��
	private double processId = 0;// ���̿��Ʊ�ǩ
	private String processIdFile = "";// ���̿����ļ�·��
	private String processFile = "";// �Ѿ�ִ�е������ļ�
	private Vector<String> vProcess = new Vector<String>();// �Ѿ�ִ�е�����
	private String layer1SelectListPath = "";// ��һ������ѡ�������б�·��
	private String layer2SelectListPath = "";// ��2�������������ѡ����б�·��
	private String layer2HrefPath = "";// �ڶ���ѡ���ҳ��ĳ����Ӽ���·��
	private String layer3CatCrawlListPath = "";// ��3�����ѡ���Ŀ¼��ȡ��ҳ���б�·��
	private String layer12SelectListPath = "";// ��һ����ѡ���ҳ���б�·��
	private String layer12SelectCatFeatureFile = "";// ��һ����ѡ���ҳ��Ŀ¼����
	private String layer0ListPath = "";// ��0���б�·��
	private String selectCategoryPath = "";// ѡ�����Ŀ¼����
	private String categoryPath = "";// ��ȡ������Ŀ¼·��
	private String layer1ListPath="";//��һ���б��ļ�
	private String layer2ListPath="";//�ڶ����б��ļ�

	// other
	int layer2HrefNumber = 0;// �ڶ��㳬��������
	private int sum = 0;// ����
	private int currentSize = 0;// ɨ�����ȡ�ļ�����

	// gui
	public String guiPath = "";// ǰ̨��ʾ�ļ���
	public String layer3CatCrawlHtmlConf = "";// ��3�����ѡ���Ŀ¼��ȡ��ҳ������·��
	public String layer3SelectConf="";//������ɸѡ�Ľ������·��
	public String chartConf = "";// chart����·��
	public String btnConf = "";// btn����·��

	public static void main(String[] args) {
		Layer3Extractor extractor = new Layer3Extractor("Computer_network",
				"F:\\DOFT-data\\DTExtraction");
		extractor.extract();
	}

	public Layer3Extractor(String domainName, String savePath) {
		this.domainName = domainName;
		this.DTPath = savePath + "/" + this.domainName;
		init();
	}

	public void init() {
		// html
		htmlPath = DTPath + "/html";
		// process
		processPath = DTPath + "/process";
		// ���̳�ʼ��
		processIdFile = processPath + "/processId.txt";
		processFile = processPath + "/process.txt";
		File fProcessId = new File(processIdFile);
		if (!fProcessId.exists())
			FileUtil.writeStringFile("1", processIdFile);
		File fProcessTxt = new File(processFile);
		if (!fProcessTxt.exists())
			try {
				fProcessTxt.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// gui
		guiPath = DTPath + "/gui";
		layer3CatCrawlHtmlConf = guiPath + "/layer3CatCrawlHtmlConf.txt";
		layer3SelectConf = guiPath + "/layer3SelectConf.txt";
		chartConf = guiPath + "/chartConf.txt";
		btnConf = guiPath + "/btnConf.txt";
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

	public boolean isFinishedProcess(String process) {
		vProcess = SetUtil.readSetFromFile(processFile);
		if (vProcess.contains(process))
			return true;
		else
			return false;
	}

	public void addProcess(String process) {
		vProcess = SetUtil.readSetFromFile(processFile);
		if (!vProcess.contains(process)) {
			vProcess.add(process);
			SetUtil.writeSetToFile(vProcess, processFile);
		}
		String id = String.valueOf(processId);
		FileUtil.writeStringFile(id, processIdFile);
	}

	Timer timer;

	/**
	 * ��ȡ����
	 */
	public void extract() {
		final Layer3ExtractorThread let = new Layer3ExtractorThread(100, "");
		// ������һ���̵߳ķ�ʽ��ִ��һ����ʱ������
		final Thread extractThread = new Thread(let);
		extractThread.start();
		// �������ȶԻ���
		final ProgressMonitor pm = new ProgressMonitor(null, "",
				let.getCurrentTask(), 0, let.getAmount());
		// ����һ����ʱ��
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ������ĵ�ǰ��������ý��ȶԻ������ɱ���
				pm.setProgress(let.getCurrent());
				pm.setNote(let.getCurrentTask());
				// ����û������˽��ȶԻ���ġ�ȡ������ť
				if (pm.isCanceled()) {
					// ֹͣ��ʱ��
					timer.stop();
					// �ж������ִ���߳�
					extractThread.interrupt();
				}
				if (pm.getNote().equals("success")) {
					try {
						Thread.sleep(1000);
						timer.stop();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		timer.start();
	}

	// ��ȡ���߳���
	class Layer3ExtractorThread implements Runnable {
		// ����ĵ�ǰ�����
		public volatile int current;
		// ����ĵ�ǰ��ʾ
		public volatile String currentTask;
		// ��������
		public int amount;

		public Layer3ExtractorThread(int amount, String currentTask) {
			current = 0;
			this.amount = amount;
			this.currentTask = currentTask;
		}

		public int getAmount() {
			return amount;
		}

		public int getCurrent() {
			return current;
		}

		public String getCurrentTask() {
			return currentTask;
		}

		public void setCurrent(int current, String currentTask) {
			this.current = current;
			this.currentTask = currentTask;
			System.out.println(currentTask);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// ��ȡ����
		public void run() {
			WebCrawler wc = new WebCrawler();
			WikiHrefProcess whp = new WikiHrefProcess();
			setProcessId(2);
			// 1.��ȡlayer2-select������
			layer2SelectHtmlPath = htmlPath + "/layer2-select";
			layer2HrefPath = processPath + "/layer2-href.txt";
			layer0ListPath = processPath + "/layer0.txt";
			layer1SelectListPath = processPath + "/layer1-select.txt";
			layer2SelectListPath = processPath + "/layer2-select.txt";
			if (getProcessId() < 2.1) {
				setCurrent(1, "Analysis hyperLink of layer2����");
				if (!isFinishedProcess("layer2-select->analysisHref")) {
					Vector<String> vLayer2Href = new Vector<String>();
					vLayer2Href.addAll(whp
							.getWikiTermFromDir(layer2SelectHtmlPath));
					Vector<String> vLayer0Term = SetUtil
							.readSetFromFile(layer0ListPath);
					Vector<String> vLayer1Term = SetUtil
							.readSetFromFile(layer1SelectListPath);
					Vector<String> vLayer2Term = SetUtil
							.readSetFromFile(layer2SelectListPath);
					vLayer2Href = SetUtil.getNoRepeatVectorIgnoreCase(SetUtil
							.getSubSet(vLayer2Href, SetUtil.getUnionSet(SetUtil
									.getUnionSet(vLayer0Term, vLayer1Term),
									vLayer2Term)));
					layer2HrefNumber = vLayer2Href.size();
					SetUtil.writeSetToFile(vLayer2Href, layer2HrefPath);
					addProcess("layer2-select->analysisHref");
				}
				setProcessId(2.1);
				layer2HrefNumber = SetUtil.readSetFromFile(layer2HrefPath)
						.size();
			}
			// 2.ѡ��category
			selectCategoryPath = processPath + "/select-category.txt";
			layer1SelectHtmlPath = htmlPath + "/layer1-select";
			layer12SelectHtmlPath = htmlPath + "/layer12-select";
			layer12SelectListPath = processPath + "/layer12-select.txt";
			layer12SelectCatFeatureFile = processPath
					+ "/layer12-select-cat.xls";
			if (getProcessId() < 2.2) {
				setCurrent(4, "Combine layer1-select and layer2-select����");
				try {
					FileUtil.copyDirectiory(layer1SelectHtmlPath,
							layer12SelectHtmlPath);
					FileUtil.copyDirectiory(layer2SelectHtmlPath,
							layer12SelectHtmlPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SetUtil.writeSetToFile(
						FileUtil.getDirFileSet(layer12SelectHtmlPath),
						layer12SelectListPath);
				setCurrent(6, "Extracting layer1,2-select category����");
				ExtractCategory ec = new ExtractCategory(layer12SelectHtmlPath,
						layer12SelectCatFeatureFile);
				ec.run(ec);
				setCurrent(8, "Select category root of layer2����");
				File f = new File(selectCategoryPath);
				if (!f.exists()) {
					// ѡ��
					SelectCategory sc = new SelectCategory(
							layer12SelectCatFeatureFile, 2, 6,
							selectCategoryPath);
					sc.run(sc);
				}
				setProcessId(2.2);
			}
			// 3.��ȡcategory
			categoryPath = processPath + "/category";
			if (getProcessId() < 2.3) {
				setCurrent(10, "Crawing the select category root����");
				if (!isFinishedProcess("select-category->crawl")) {
					WikiCategoryCrawler wcc = new WikiCategoryCrawler();
					Vector<String> vCat = SetUtil
							.readSetFromFile(selectCategoryPath);
					wcc.buildCategoryTree(vCat, categoryPath, 1);
					addProcess("select-category->crawl");
				}
				setProcessId(2.3);
			}
			// 4.��ȡcategory�е�����
			layer3CatCrawlListPath = processPath + "/layer3CatCrawl.txt";
			layer3CatCrawlHtmlPath=htmlPath+"/layer3CatCrawl";
			layer1ListPath= processPath + "/layer1.txt";
			layer2ListPath= processPath + "/layer2.txt";
			if (getProcessId() < 2.4) {
				setCurrent(12, "Analysis term in select category����");
				File f=new File(layer3CatCrawlListPath);
				if(!f.exists()){
					CategoryAnalysis ca = new CategoryAnalysis(
							layer12SelectListPath, categoryPath,
							layer3CatCrawlListPath);
					ca.getCategoryTerm();
				}
				sum=SetUtil.readSetFromFile(layer3CatCrawlListPath).size();
				FileUtil.writeStringFile(layer3CatCrawlHtmlPath + "," + sum,
						layer3CatCrawlHtmlConf);// ����ǰ̨��ʾ�����ļ�
				File fLayer3CatRawHtml = new File(layer3CatCrawlHtmlPath);
				fLayer3CatRawHtml.mkdirs();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// ��ֹ�ļ���ȡ����
				setCurrent(14, "Crawling the term in select category����");
				Thread tDetect = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ExtractorUtil.scanDirPage(layer3CatCrawlHtmlPath).size() >= sum) {
							for (int i = 1; i <= sum; i++) {
								setCurrent(14 + i * 76 / sum, "Crawling page(" + i
										+ "/" + sum + ")����");
							}
						}// �Ѿ���ȡ���
						else {
							Vector<String> vExistPage = new Vector<String>();
							while (currentSize < sum) {
								HashMap<String, String> hm = ExtractorUtil
										.scanDirPage(layer3CatCrawlHtmlPath);
								Vector<String> vNewAddPage = ExtractorUtil
										.getNewAddPage(vExistPage, hm);
								while (vNewAddPage.size() == 0) {
									hm = ExtractorUtil
											.scanDirPage(layer3CatCrawlHtmlPath);
									vNewAddPage = ExtractorUtil.getNewAddPage(
											vExistPage, hm);
								}
								vExistPage.addAll(vNewAddPage);
								currentSize = vExistPage.size();
								setCurrent(4 + currentSize * 76 / sum,
										"Crawling page(" + currentSize + "/" + sum
												+ ")����");
							}
						}// δ��ȡ���
					}
				});
				tDetect.start();
				wc.crawlPageByList(layer3CatCrawlListPath,layer3CatCrawlHtmlPath, 10);
				while (tDetect.isAlive()) {
				}// ִ�����ټ�������
				Vector<String> vConf=new Vector<String>();
				vConf.add(layer1SelectHtmlPath + "," + SetUtil.readSetFromFile(layer1SelectListPath).size());
				vConf.add(layer2SelectHtmlPath + "," + SetUtil.readSetFromFile(layer2SelectListPath).size());
				vConf.add(layer3CatCrawlHtmlPath + "," + SetUtil.readSetFromFile(layer3CatCrawlListPath).size());
				setCurrent(95, "Generating the GUI configuration����");
				SetUtil.writeSetToFile(vConf, layer3SelectConf);// ����ǰ̨��ʾ�����ļ�
				int crawl1Number=SetUtil.readSetFromFile(layer1ListPath).size();
				int filter1Number=SetUtil.readSetFromFile(layer1SelectListPath).size();
				int crawl2Number=SetUtil.readSetFromFile(layer2ListPath).size();
				int filter2Number=SetUtil.readSetFromFile(layer2SelectListPath).size();
				int layer3CatCrawl=SetUtil.readSetFromFile(layer3CatCrawlListPath).size();
				Vector<String> vChartData=new Vector<String>();
				vChartData.add(crawl1Number+"@@@"+filter1Number);
				vChartData.add(crawl2Number+"@@@"+filter2Number);
				vChartData.add(layer2HrefNumber+"@@@"+layer3CatCrawl);
				SetUtil.writeSetToFile(vChartData, chartConf);
				FileUtil.writeStringFile("First Layer", btnConf);
				setProcessId(2.4);
			}
			setProcessId(2.8);
			setCurrent(100, "success");
		}
	}
}
