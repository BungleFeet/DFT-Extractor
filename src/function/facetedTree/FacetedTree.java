package function.facetedTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import function.util.FileUtil;
import function.util.SetUtil;

/**
 * 
 * @author MJ
 * @description ���ĳ�����ݼ�������״�ṹ
 */
public class FacetedTree {

	/**
	 * @param args
	 */
	private HashMap<String, MyNode> hm = new HashMap<String, MyNode>();
	private HashSet<String> edgeHs =null;//������λ����λ�Ĺ�ϵ

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath="f:/Computer_Network.csv";
		FacetedTree ft=new FacetedTree();
		ft.constructTrees(filePath);
	}
	
	/**
	 * ����ָ��������λ��ϵ�߼���
	 * @param edgeHs
	 * @return ���ļ���
	 */
	public Vector<MyNode> constructTrees(HashSet<String> edgeHs){
		this.edgeHs=edgeHs;
		Vector<MyNode> vMyNode=new Vector<MyNode>();
		Vector<String> vRoot=findRoot(edgeHs);//���ָ�
		for(String root:vRoot){
			System.out.println("����"+root);
			hm.clear();
			MyNode rootNode = new MyNode(root, new Vector<MyNode>(),null,0);
			hm.put(root, rootNode);
			constructTree(rootNode);
			vMyNode.add(rootNode);
			printNode(rootNode);
		}
		return vMyNode;
	}
	
	/**
	 * ����ָ���Ĺ�ϵ�ļ�������
	 * @param filePath
	 * @return ���ļ���
	 */
	public Vector<MyNode> constructTrees(String filePath){
		Vector<MyNode> vMyNode=new Vector<MyNode>();
		Vector<String> vRecord=SetUtil.readSetFromFile(filePath);
		edgeHs=generateHypEdge(vRecord);//��������λ��ϵ��
		Vector<String> vRoot=findRoot(edgeHs);//���ָ�
		for(String root:vRoot){
			System.out.println("����"+root);
			hm.clear();
			MyNode rootNode = new MyNode(root, new Vector<MyNode>(),null,0);
			hm.put(root, rootNode);
			constructTree(rootNode);
			vMyNode.add(rootNode);
			printNode(rootNode);
		}
		return vMyNode;
	}

	/**
	 * 
	 * @return ��������λ��ϵ��
	 */
	public HashSet<String> generateHypEdge(Vector<String> vRecord) {
		HashSet<String> edgeHs = new HashSet<String>();
		for (int i = 1; i < vRecord.size(); i++) {
			String tempStr[] = vRecord.get(i).split(",");
			String edge = "";
			String src = tempStr[0];
			String to = tempStr[1];
			String relation = tempStr[2];
			if (relation.equals("A is a B")) {
				edge = to + "->" + src;
				edgeHs.add(edge);
			} else if (relation.equals("B is a A")) {
				edge = src + "->" + to;
				edgeHs.add(edge);
			}
		}
		return edgeHs;
	}
	
	/**
	 * 
	 * @param edgeHs
	 * @return ���ݸ���������λ��ϵ�Զ����ָ�
	 */
	public Vector<String> findRoot(HashSet<String> edgeHs) {
		Vector<String> vRoot = new Vector<String>();
		Iterator<String> it = edgeHs.iterator();
		while (it.hasNext()) {
			String edge = it.next();
			String fNode = edge.split("->")[0];
			if(!vRoot.contains(fNode)){
				Iterator<String> itTemp=edgeHs.iterator();
				int number=0;
				while(itTemp.hasNext()){
					String edgeTemp = itTemp.next();
					String cNodeTemp = edgeTemp.split("->")[1];
					if(cNodeTemp.equals(fNode))
						break;
					else 
						number++;
				}
				if(number>=edgeHs.size())
					vRoot.add(fNode);
			}
		}
		return vRoot;
	}
	
	/**
	 * ������
	 * 
	 * @param root
	 * @return
	 */
	public MyNode constructTree(MyNode root) {
		root.child = getChild(root);
		hm.put(root.nodeName, root);
		if (root.child.size() == 0) {
			return root;
		} else {
			for (int i = 0; i < root.child.size(); i++) {
				constructTree(root.child.elementAt(i));
			}
			return root;
		}
	}

	/**
	 * ��ӡĳ�����ڵ��µ����нڵ�
	 * 
	 * @param root
	 */
	public void printNode(MyNode root) {
		int spaceNum = root.layer * 3;
		String space = "";
		while (spaceNum > 0) {
			space = space + " ";
			spaceNum--;
		}
		if (root.child.size() == 0 && !root.nodeName.equals("noUse"))
			System.out.println(space + "+" + root.nodeName + "--------"
					+ root.layer);
		else if (!root.nodeName.equals("noUse")) {
			System.out.println(space + "-" + root.nodeName + "--------"
					+ root.layer);
			for (int i = 0; i < root.child.size(); i++) {
				printNode(root.child.elementAt(i));
			}
		}
	}
	
	/**
	 * ��ȡĳ���ڵ�ĺ��ӽڵ㼯��
	 * 
	 * @param father
	 * @return
	 */
	public Vector<MyNode> getChild(MyNode father) {
		Vector<MyNode> v = new Vector<MyNode>();
		Iterator<String> i = edgeHs.iterator();
		// System.out.println(father.nodeName);
		while (i.hasNext()) {
			String edge = i.next();
			String fNode = edge.split("->")[0];
			String cNode = edge.split("->")[1];
			if (fNode.equals(father.nodeName)) {
				if (hm.containsKey(cNode)
						&& hm.get(cNode).layer <= father.layer) {
					MyNode removeChildFather = hm.get(cNode).father;
					hm.get(cNode).layer = father.layer + 1;
					hm.get(cNode).father = father;
					MyNode n = hm.get(cNode);
					v.add(n);
					removeChildFather.removeChild(cNode);
					hm.put(removeChildFather.nodeName, removeChildFather);
				} else {
					MyNode n = new MyNode(cNode, new Vector<MyNode>(), father,
							father.layer + 1);
					v.add(n);
					hm.put(cNode, n);
				}
			}
		}
		return v;
	}
	
	/**
	 * ������nodeΪ����xml�ļ�
	 * @param node
	 * @param desXmlFile
	 */
	public void generateTreeXml(MyNode node,String desXmlFile){
		String s="";
		s=s+"<tree>\n";
		s=s+"<declarations>\n";
		s=s+"<attributeDecl name=\"name\" type=\"String\"/>\n";
		s=s+"</declarations>\n";
		s=generateBranchLeaf(node,s);
		s=s+"</tree>";
		FileUtil.writeStringFile(s, desXmlFile);
	}
	
	/**
	 * ����xml��branch��leaf����
	 * @param node
	 * @param s
	 * @return
	 */
	public String generateBranchLeaf(MyNode node,String s){
		if (node.child.size() != 0 && !node.nodeName.equals("noUse")) {
			s=s+"<branch>\n";
			s=s+"<attribute name=\"name\" value=\""+node.nodeName.replace("�C", "-")+"*\"/>\n";
			for (int i = 0; i < node.child.size(); i++) {
				s=generateBranchLeaf(node.child.elementAt(i),s);
			}
			s=s+"</branch>\n";
		} else if (node.child.size() == 0 && !node.nodeName.equals("noUse")) {
			s=s+"<leaf>\n";
			s=s+"<attribute name=\"name\" value=\""+node.nodeName.replace("�C", "-")+"\"/>\n";
			s=s+"</leaf>\n";
		}
		return s;
	}
}
