package function.facetedTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import function.util.MapUtil;
import function.util.SetUtil;

public class Cluster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * ��ȡĳ�����г�ȥ�������Ҫɾ���ı�
	 * 
	 * @param edgeHs
	 * @param maxTreeNode
	 * @param vClusterNode
	 * @return
	 */
	public Vector<String[]> getClusterDelEdge(HashSet<String> edgeHs,
			Vector<String> maxTreeNode, Vector<String> vClusterNode) {
		Vector<String[]> vDelEdge = new Vector<String[]>();
		Iterator<String> it = edgeHs.iterator();
		while (it.hasNext()) {
			String edge = it.next();
			String nodeName[] = edge.split("->");
			if ((maxTreeNode.contains(nodeName[0]) && !maxTreeNode
					.contains(nodeName[1]))
					|| (maxTreeNode.contains(nodeName[1]) && !maxTreeNode
							.contains(nodeName[0])))
				vDelEdge.add(nodeName);
			else if ((vClusterNode.contains(nodeName[0]) && !maxTreeNode
					.contains(nodeName[0]))
					|| (vClusterNode.contains(nodeName[1]) && !maxTreeNode
							.contains(nodeName[1])))
				vDelEdge.add(nodeName);
		}
		return vDelEdge;
	}

	/**
	 * ��ȡĳ�����г�ȥ�������Ҫɾ���Ľڵ�
	 * 
	 * @param vClusterNode
	 * @param maxTreeNode
	 * @return
	 */
	public Vector<String> getClusterDelNode(Vector<String> vClusterNode,
			Vector<String> maxTreeNode) {
		return SetUtil.getSubSet(vClusterNode, maxTreeNode);
	}

	/**
	 * �ҳ�������
	 * 
	 * @param vRoot
	 * @return
	 */
	public MyNode getMaxTree(Vector<MyNode> vRoot) {
		int maxNodeNumber = 0;
		MyNode maxRoot = null;
		for (int i = 0; i < vRoot.size(); i++) {
			MyNode root = vRoot.get(i);
			if (root.getNodeNumber() > maxNodeNumber) {
				maxNodeNumber = root.getNodeNumber();
				maxRoot = root;
			}
		}
		return maxRoot;
	}

	/**
	 * �Ӱ���term�����ĶȵĴ�����ȡ���ڵ���
	 * 
	 * @param vClusters
	 * @return
	 */
	public Vector<String> getClusterNode(Vector<String[]> vCluster) {
		Vector<String> vNode = new Vector<String>();
		for (String[] node : vCluster) {
			vNode.add(node[0]);
		}
		return vNode;
	}

	/**
	 * ��ȡָ����vClusterNode�е�����λ��
	 * 
	 * @param edgeHs
	 * @param vClusterNode
	 * @return
	 */
	public HashSet<String> getClusterHypeEdge(HashSet<String> edgeHs,
			Vector<String> vClusterNode) {
		HashSet<String> clusterEdgeHs = new HashSet<String>();
		Iterator<String> it = edgeHs.iterator();
		while (it.hasNext()) {
			String edge = it.next();
			String nodeName[] = edge.split("->");
			if (vClusterNode.contains(nodeName[0])
					&& vClusterNode.contains(nodeName[1]))
				clusterEdgeHs.add(edge);
		}
		return clusterEdgeHs;
	}

	/**
	 * ���ݴؽڵ�Ĵ�С�Դؽ��к��ʵ�λ������
	 * 
	 * @param vClusters
	 * @return
	 */
	public Vector<Vector<String[]>> getSuitableCluster(
			Vector<Vector<String[]>> vClusters) {
		Vector<Vector<String[]>> vSuitableClusters = new Vector<Vector<String[]>>();
		int clusterNumber = vClusters.size();
		HashMap<String, Integer> hmClusterSize = new HashMap<String, Integer>();// ���ÿ���ؽڵ�ĸ���
		HashMap<String, Double> hmAngle = new HashMap<String, Double>();// ��ŽǶ�
		for (int i = 0; i < clusterNumber; i++) {
			hmClusterSize.put(String.valueOf(i), vClusters.get(i).size());
			double angle = 360 * 1.0 * i / clusterNumber;
			if (angle >= 90 && angle < 180)
				angle = 180 - angle;
			else if (angle >= 180 && angle < 270)
				angle = angle - 180;
			else if (angle >= 270)
				angle = 360 - angle;
			hmAngle.put(String.valueOf(i), angle);
		}
		Vector<String[]> vAngle = MapUtil.sortMapDoubleValueAsc(hmAngle);// ���վ���ˮƽ�ߵĽǶ���������
		Vector<String[]> vClusterSize = MapUtil.sortMapValueDes(hmClusterSize);// ���սڵ������������
		HashMap<Integer, Integer> hmAngleCluster = new HashMap<Integer, Integer>();// �ǶȺ�cluster��idӳ��
		for (int i = 0; i < clusterNumber; i++) {
			hmAngleCluster.put(Integer.valueOf(vAngle.get(i)[0]),
					Integer.valueOf(vClusterSize.get(i)[0]));
		}
		for (int i = 0; i < clusterNumber; i++) {
			vSuitableClusters.add(vClusters.get(hmAngleCluster.get(i)));
		}
		return vSuitableClusters;
	}

	/**
	 * ��csv�ļ���ȡ�ؽ��������term centrality clusterId
	 * 
	 * @param fileName
	 * @return
	 */
	public Vector<Vector<String[]>> readCluster(String fileName) {
		Vector<Vector<String[]>> vClusters = new Vector<Vector<String[]>>();
		HashMap<Integer, HashMap<String, Double>> hm = new HashMap<Integer, HashMap<String, Double>>();
		Vector<String> vRecord = SetUtil.readSetFromFile(fileName);
		for (int i = 1; i < vRecord.size(); i++) {
			String record[] = vRecord.get(i).split(",");
			String term = record[0];
			Double centrality = Double.valueOf(record[1]);
			int clusterId = Integer.valueOf(record[2]);
			if (!hm.containsKey(clusterId)) {
				HashMap<String, Double> hmCluster = new HashMap<String, Double>();
				hmCluster.put(term, centrality);
				hm.put(clusterId, hmCluster);
			} else {
				HashMap<String, Double> hmCluster = hm.get(clusterId);
				hmCluster.put(term, centrality);
				hm.put(clusterId, hmCluster);
			}
		}// end for
		Iterator<Integer> it = hm.keySet().iterator();
		while (it.hasNext()) {
			int id = it.next();
			HashMap<String, Double> hmCluster = hm.get(id);
			Vector<String[]> vCluster = MapUtil
					.sortMapDoubleValueDes(hmCluster);
			vClusters.add(vCluster);
		}
		return vClusters;
	}

	/**
	 * ����������λ����ɵ�����ͼ�Ķ�
	 * 
	 * @param edgeHs
	 * @return
	 */
	public HashMap<String, Integer> getDegree(HashSet<String> edgeHs) {
		HashMap<String, Integer> hmDegree = new HashMap<String, Integer>();
		Iterator<String> it = edgeHs.iterator();
		while (it.hasNext()) {
			String record[] = it.next().split("->");
			if (!hmDegree.containsKey(record[0]))
				hmDegree.put(record[0], 1);
			else {
				int degree = hmDegree.get(record[0]) + 1;
				hmDegree.put(record[0], degree);
			}
			if (!hmDegree.containsKey(record[1]))
				hmDegree.put(record[1], 1);
			else {
				int degree = hmDegree.get(record[1]) + 1;
				hmDegree.put(record[1], degree);
			}
		}
		return hmDegree;
	}
}
