package function.txtAnalysis.basicTxtFeatures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import function.base.LinkBase;
import function.base.SheetBase;

/*
 * ���룺���Ｏ������html�ļ���Ŀ¼
 * ���������HTML�ļ��Ĺ�ϵ������������
 * 
 * ��Ҫ�����ӹ������أ�
 * 0�����Ｏ���ˡ����ＯҪô��Դ��һ���ļ���Ҫô���ض�Ŀ¼�µ��ļ���
 * 1������Box����
 * 2���߼�λ�ù���
 * 3���ظ����ӹ���
 * 
 * ʹ�÷�����
 * 0)׼��MS excel�ļ�:ID	sourceURLName	toURLName	DeltaText	posInHtml	AnchorText	linkSameAhchor	htmlLen	posInHtml2	LogicPos	repeatNum	linkMode	LinkSequence
 * 1) ����html·����termset���·��
 * 2)����updateTermSet
 * 3)����processing��Ĭ��ʹ��TermSet
 * 4)�����������ö�Ӧ��test������
 */
public class BasicFeatures extends SheetBase {
	MOREConfig mc = null;
	LocalCorpus localCorpus =null;
	int[] boxRange = null;
	int[] logicPosRange = null;

	// Ĭ��ʹ��TermSet,
	public BasicFeatures(MOREConfig mc) {
		this.mc=mc;
		localCorpus=new LocalCorpus(mc);
	}

	@Override
	public void processing() throws RowsExceededException, WriteException,
			FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		localCorpus.getTermSetFromFile(mc.termSetPath);
		int recordIndex = 1;
		for (String term : localCorpus.TermSet) {
			StringBuffer htmlBuffer = localCorpus.getHtmlFileBuffer(term);
			boxRange = getInfoBoxRange(htmlBuffer);
			logicPosRange = getLogicPosRange(htmlBuffer);
			Vector<LinkBase> links = getHtmlFeatures(term, htmlBuffer);
			Vector<LinkBase> links2 = filterHtmlFeatures(links);
			Vector<LinkBase> links3 = getDistinctLinks(links2);

			for (LinkBase link : links3) {
				System.err.print(recordIndex + ":");
				System.err.println(link);
				setNumberValue("ID", recordIndex, recordIndex);
				setStringValue("sourceURLName", recordIndex, link.srcName);
				setStringValue("toURLName", recordIndex, link.desName);
				setStringValue("DeltaText", recordIndex, link.deltaText);
				setNumberValue("posInHtml", recordIndex, link.Pos);
				setStringValue("AnchorText", recordIndex, link.achorText);
				setStringValue("linkSameAhchor", recordIndex,
						link.isAchorEDesName);
				setNumberValue("htmlLen", recordIndex, htmlBuffer.length());
				setNumberValue("posInHtml2", recordIndex, (double) link.Pos
						/ htmlBuffer.length());
				setNumberValue("LogicPos", recordIndex, link.logicPos);
				setNumberValue("repeatNum", recordIndex, link.repeatNum);
				setNumberValue("linkMode", recordIndex, link.linkMode);
				setNumberValue("LinkSequence", recordIndex, link.sequence2);
				recordIndex++;
			}
		}
	}

	public void createTermSet() {
		BasicFeatures bf = new BasicFeatures(mc);
		bf.updateTermSet();
	}

	// ����һ�������ȡ��������ļ�����ȡ��html�ļ������Ӳ���ӡ������
	public void testSingleFile(String term) {

		localCorpus.getTermSetFromFile(mc.termSetPath);
		// StringBuffer sb =
		// DataMining.getFileBuffer(DataMining.htmlFileRoot+"/"+term+".html");
		StringBuffer htmlBuffer = localCorpus.getHtmlFileBuffer(term);
		Vector<LinkBase> links = getHtmlFeatures(term, htmlBuffer);
		for (LinkBase link : links)
			System.out.println(link);

		boxRange = getInfoBoxRange(htmlBuffer);
		logicPosRange = getLogicPosRange(htmlBuffer);
		Vector<LinkBase> links2 = filterHtmlFeatures(links);
		for (LinkBase link : links2)
			System.err.println(link);

		Vector<LinkBase> links3 = getDistinctLinks(links2);
		for (LinkBase link : links3) {
			System.out.println(link);
			System.out.println((double) link.Pos / htmlBuffer.length());
		}
	}

	// ����Ŀ¼���ļ����ָ������Ｏ
	public void updateTermSet() {
		localCorpus.getFileNamesFromDirectory(mc.htmlPath);
		localCorpus.storeTermSet(mc.termSetPath);
	}

	// ����һ�������ȡ�߼�λ�õķ�Χ��
	public void testLogicPos(String term) {

		StringBuffer Buffer = localCorpus.getHtmlFileBuffer(term);
		int[] posArray = getLogicPosRange(Buffer);
		System.out.println(posArray[0]);
		System.out.println(posArray[1]);
		System.out.println(posArray[2]);
		Integer logic = getLogicPos(8311, posArray);
		log.info(logic.toString());
	}

	// ����һ�������ȡBox�ķ�Χ��
	public void testInfoBox(String term) {
		StringBuffer Buffer = localCorpus.getHtmlFileBuffer(term);
		int[] range = getInfoBoxRange(Buffer);
		System.out.println(range[0]);
		System.out.println(range[1]);

		String temp = Buffer.substring(range[0] - 20, range[0] + 30);
		System.out.println(temp);
		temp = Buffer.substring(range[1] - 20, range[1] + 40);
		System.out.println(temp);

	}

	/*
	 * ���룺�ļ������ļ�����������˼·������href="/wiki/��Ȼ����������������Ӷ����Vector
	 */
	public static Vector<LinkBase> getHtmlFeatures(String sourceURLName,
			StringBuffer sb) {
		System.out.println("begin processing: " + sourceURLName);
		int termEndPos = 0;
		int sbSize = sb.length();
		Vector<LinkBase> links = new Vector<LinkBase>();
		String flagStr = "href=\"/wiki/";
		for (int pos = 0; pos < sbSize; pos += 4) {
			/*
			 * short for Adaptive <a href="/wiki/Boosting"
			 * title="Boosting">Boosting</a> <li id="ca-talk"><span><a
			 * href="/wiki/Talk:AdaBoost"
			 * title="Discussion about the content page [t]" is a <a
			 * href="/wiki/Machine_learning" title="Machine learning">machine
			 * learning</a> algorithm, formulated by
			 * 
			 * �����ַ�������<a href="/wiki/Distance_matrix"
			 * title="Distance matrix">distance matrix</a></b> has in position
			 * (<i>i</i>,<i>j</i>) the distance between verticesê�ı�������<a
			 * href="/wiki/K-medoids" title="K-medoids"><span class="texhtml"
			 * style="white-space: nowrap;"><var>k</var></span>-medoids</a><a
			 * href="/wiki/Greedy_algorithm" title="View the content page [c]"
			 * accesskey="c">Article</a></span></li>ê�ı��������ַ���������<a
			 * href="/wiki/Branch_and_bound" title="Branch and bound">Branch
			 * &amp; bound</a> <a href="/wiki/Branch_and_cut"
			 * title="Branch and cut">
			 */
			LinkBase link = new LinkBase();
			link.srcName = sourceURLName;

			pos = sb.indexOf(flagStr, pos);
			if (pos == -1)
				break;// ���������ĩβ������
			termEndPos = sb.indexOf("\"", pos + flagStr.length());
			if (termEndPos == -1)
				break;// ���������ĩβ������
			// ����Ŀ���ַ����
			String toURLName = sb.substring(pos + flagStr.length(), termEndPos);// ����ȡ�ı�
			// System.out.println("wiki text=" + LinkedUrlName);
			// if
			// (!LinkedUrlName.matches("^((\\()?[a-z0-9A-Z]?(-)?(_)?(\\))?)*$"))
			// {
			// continue;
			// }
			link.desName = toURLName;
			link.Pos = pos + flagStr.length();

			// ê�ı�
			int rightIndex = sb.indexOf(">", termEndPos);// �����Ҽ�����λ��
			int leftIndex = sb.indexOf("<", rightIndex);// �����������λ��
			if (leftIndex == -1 || rightIndex == -1)
				break;
			// link.achorText = specificReplace(sb.substring(rightIndex + 1,
			// leftIndex));
			link.achorText = sb.substring(rightIndex + 1, leftIndex);

			// ���������ַ�������text�Ĵ���
			int nextRightIndex = sb.indexOf(">", leftIndex);// ������һ���Ҽ�����λ��
			int nextLeftIndex = sb.indexOf("<", nextRightIndex);// ������һ���������λ��
			if (nextRightIndex == -1 || nextLeftIndex == -1)
				break;

			// ���������ַ���
			if (nextLeftIndex - nextRightIndex <= 20)
				link.deltaText = sb
						.substring(nextRightIndex + 1, nextLeftIndex);// �����ַ���
			else
				link.deltaText = sb.substring(nextRightIndex + 1,
						nextRightIndex + 21);
			// link.deltaText=specificReplace(link.deltaText);

			link.sequence = links.size() + 1;
			links.add(link);

		}
		// �����ڲ�ͬλ�ý��д���
		for (int i = 0; i < links.size(); i++) {
			links.get(i).sequence2 = (double) (links.get(i).sequence)
					/ links.size();
		}
		System.out.println("links num:" + links.size());
		return links;
	}

	public Vector<LinkBase> filterHtmlFeatures(Vector<LinkBase> links) {
		Vector<LinkBase> newLinks = new Vector<LinkBase>();
		for (LinkBase link : links) {
			link.isESrcDes = link.srcName.equalsIgnoreCase(link.desName);
			link.isTermSet = localCorpus.isTerm(link.desName);
			if (link.isTermSet == true) {
				link.desName = localCorpus.getTrueTerm(link.desName);
			}
			String compareURL = link.desName.replace('_', ' ');
			link.isAchorEDesName = compareURL.equalsIgnoreCase(link.achorText);
			link.isInBox = isInBox(link.Pos, boxRange);
			link.logicPos = getLogicPos(link.Pos, logicPosRange);
			// System.out.println(link);
			if (link.isTermSet && !link.isInBox && !link.isESrcDes
					&& link.logicPos < 4) {
				// System.err.println(link);
				// link.sequence=newLinks.size()+1;
				newLinks.add(link);
			}
		}
		return newLinks;
	}

	public Vector<LinkBase> getDistinctLinks(Vector<LinkBase> links) {
		Vector<LinkBase> newLinks = new Vector<LinkBase>();
		Vector<String> desNames = new Vector<String>();
		for (int i = 0; i < links.size(); i++) {
			//
			LinkBase link = links.get(i);
			String desName = link.desName;
			if (!desNames.contains(desName.toLowerCase())) {
				desNames.add(desName.toLowerCase());
				link.isFirstOfDes = true;
				link.linkMode = (int) Math.pow(2, link.logicPos);
				link.repeatNum = 1;
				// link.sequence=newLinks.size()+1;
				newLinks.add(link);
			} else {
				int index = desNames.indexOf(desName.toLowerCase());
				newLinks.elementAt(index).linkMode += (int) Math.pow(2,
						link.logicPos);
				newLinks.elementAt(index).repeatNum++;
			}
		}
		return newLinks;
	}

	/*
	 * nonlinear_conjugate_gradient_method Data collection û��2,3��ֻ��1��4 ����˼·��
	 */
	public int[] getLogicPosRange(StringBuffer htmlBuffer) {

		int[] posArray = { 0, 0, Integer.MAX_VALUE };

		// ��������
		String baseFeature = "</span></h2>";
		posArray[1] = htmlBuffer.indexOf(baseFeature);
		if (-1 == posArray[1])
			posArray[1] = Integer.MAX_VALUE;

		int endPos = Integer.MAX_VALUE;

		// ���õ��͵�4�����
		Vector<String> endFeatures = new Vector<String>();
		endFeatures.add("Journals and conferences</span></h2>");
		// ���ٹ���
		// endFeatures.add("See also</span></h2>");

		endFeatures.add("References</span></h2>");
		endFeatures.add("Notes</span></h2>");
		endFeatures.add("Notable Tools</span></h2>");
		endFeatures.add("Further reading</span></h2>");
		endFeatures.add("Works cited</span></h2>");
		endFeatures.add("External links</span></h2>");
		// ������������⣬����С��λ�ñ�������
		for (String feature : endFeatures) {
			endPos = htmlBuffer.indexOf(feature);
			if (-1 == endPos)
				continue;
			if (endPos < posArray[2])
				posArray[2] = endPos;
		}
		// ���û��3����ô3��λ�ú�4��ͬ��
		if (posArray[1] > posArray[2]) {
			posArray[1] = posArray[2];
			// posArray[2]=Integer.MAX_VALUE;
		}

		return posArray;
	}

	public int getLogicPos(int pos, int[] posArray) {
		if (pos >= posArray[2])
			return 4;
		else if (pos >= posArray[1] && pos < posArray[2])
			return 3;
		else if (pos < posArray[1])
			return 1;
		else
			return 4;
	}

	/*
	 * Web_Ontology_Language.html <table class="infobox hproduct"
	 * cellspacing="5" style="width:22em;"> Tree_traversal.html <table
	 * class="toccolours" style=
	 * "padding: 0px; width: 170px; margin: 0 0 1em 1em; float:right; clear:right"
	 * > <table class="wikitable"> Tree_data_structure.html <table
	 * class="navbox" cellspacing="0" style=";">
	 * F:\����\Datamining\layer\Depth-first_search.html <table class="infobox"
	 * cellspacing="5" style="width:22em;"> </table> <table class="toccolours"
	 * style
	 * ="padding: 0px; width: 170px; margin: 0 0 1em 1em; float:right; clear:right"
	 * > F:\����\Datamining\layer\Marketing_analysis.html <table
	 * class="vertical-navbox nowraplinks" cellspacing="5" st
	 */
	// һ��html�ļ��п����ж��info box������Web_Ontology_Language.html
	// һ��html�ļ��п����ж���box��info box + nav box
	// һ��html�ļ��п����ж���box���ֲ���ͷ�����в�����β����
	// 1���ٶ� box�������ģ�ͬһ�ֲ��ظ����Ҳ�Ƕ�ס�ͨ��UE��������Ŀ¼����table class=��
	// 2��ֻ����info box��nav box��vertical-navbox��toccolours
	// 3������ֻ����ͷ����box��β�������߼�λ�ô���
	public int[] getInfoBoxRange(StringBuffer htmlBuffer) {
		// trick:ֻ����ͷ��box
		int limit = (htmlBuffer.length() / 2);
		// limit=Integer.MAX_VALUE;
		Vector<String> features = new Vector<String>();
		features.add("<table class=\"infobox");
		features.add("<table class=\"navbox");
		features.add("<table class=\"toccolours");
		features.add("<table class=\"vertical-navbox");

		int[] range = { 0, 0 };

		int firstTablePos = Integer.MAX_VALUE;
		int lastTablePos = -1;
		int endTablePos = Integer.MAX_VALUE;
		int pos = 0;
		for (String feature : features) {
			pos = htmlBuffer.indexOf(feature);
			if ((pos > lastTablePos) && (pos < limit))
				lastTablePos = pos; // ���ܵ���˳��
			if (-1 == pos) {
				pos = 0;
			} else if (pos < firstTablePos) {
				firstTablePos = pos;
			}
		}
		if (-1 != lastTablePos) {
			range[0] = firstTablePos;
			range[1] = Integer.MAX_VALUE;
			endTablePos = htmlBuffer.indexOf("</table>", lastTablePos);
			if (-1 != endTablePos) {
				range[1] = endTablePos + 8;
			}
		}
		return range;
	}

	public boolean isInBox(int pos, int[] range) {
		if (pos >= range[0] && pos <= range[1])
			return true;
		else
			return false;
	}

}
