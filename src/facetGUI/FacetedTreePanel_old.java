package facetGUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import twaver.Node;
import twaver.TDataBox;
import twaver.tree.TTree;
import function.facetedTree.FacetedTree;
import function.facetedTree.MyNode;

public class FacetedTreePanel_old extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4034136806675760042L;
	private JLabel domainLabel = new JLabel("    ά������");
	private JTextField domainTf = new JTextField();
	private JButton domainRelationBtn = new JButton("һ������");
	private JLabel relationFileLabel = new JLabel("�����ϵ�ļ���");
	private JTextField relationFileTf = new JTextField();
	private JButton relationFileChooseBtn = new JButton("���...");
	private JButton relationFileBtn = new JButton("����չʾ");
	JPanel jp=new JPanel();
	private JScrollPane jsp = new JScrollPane(jp);
	
	private JPanel jpTop = new JPanel();

	/**
	 * @param args
	 */
	
	public FacetedTreePanel_old() {
		setLayout(null);
		this.setBackground(Color.white);
		
		this.add(jpTop);
		jpTop.setLayout(null);
		jpTop.setBackground(new Color(230,239,248));
		jpTop.setBounds(0, 10, 1000, 150);
		jsp.setBounds(0, 180, 1000, 420);
		
		//�����������������
		jpTop.add(domainLabel);
		jpTop.add(domainTf);
		jpTop.add(domainRelationBtn);
		domainLabel.setBounds(50, 25, 110, 25);
		domainTf.setBounds(145, 25, 510, 25);
		domainRelationBtn.setBounds(740, 25, 85, 25);
		
		domainRelationBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String domain = domainTf.getText(); //��������
				if (domain.length() == 0) {
					JOptionPane.showMessageDialog(FacetedTreePanel_old.this,
							"������������ƣ�");
				} else {
					
					
					//����ʵ��
					
					
					
				}
			}
		});
		
		//�����ϵ�ļ���������������
		jpTop.add(relationFileLabel);
		jpTop.add(relationFileTf);
		jpTop.add(relationFileChooseBtn);
		jpTop.add(relationFileBtn);
		
		relationFileLabel.setBounds(50, 70, 110, 25);
		relationFileTf.setBounds(145, 70, 510, 25);
		relationFileChooseBtn.setBounds(665, 70, 60, 25);
		relationFileBtn.setBounds(740, 70, 85, 25);
		
		relationFileChooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/");
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ���
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					relationFileTf.setText(path);
				}
			}
		});
		
		relationFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String relationPath = relationFileTf.getText();   //�����ϵ�ļ�����·��
				if (relationPath.length() == 0) {
					JOptionPane.showMessageDialog(FacetedTreePanel_old.this,
							"��ѡ�������ϵ�ļ�·����");
				} else {
					final Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							FacetedTree ft = new FacetedTree();
							Vector<MyNode> vMyNode = ft.constructTrees(relationPath);
							int rootNumber=vMyNode.size();
							int row=rootNumber/4+1;
							jp.setLayout(new GridLayout(row,4,30,30));
							for (int i = 0; i < vMyNode.size(); i++) {
								MyNode root = vMyNode.get(i);
								TTree tree=generateTree(root, new TDataBox(root.nodeName), null);
								jp.add(tree);
							}
						}
					});
					t.start();
					
					//����λ��������ʵ��
					
					
				}
			}
		});
		
		//���˽��չʾ��������
		this.add(jsp);
		
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jp.setBackground(Color.white);
		jp.setLayout(new GridLayout(5,3,80,100));
		
	
	}
	
	/**
	 * �������ڵ������
	 * @param root
	 * @param box
	 * @param parent
	 * @return ������
	 */
	public TTree generateTree(MyNode root, TDataBox box, Node parent) {
		if (root.child.size() != 0 && !root.nodeName.equals("noUse")) {
			if (root.layer == 0) {
				TTree tree = new TTree(box);
				tree.setName(root.nodeName);
				for (int i = 0; i < root.child.size(); i++) {
					generateTree(root.child.elementAt(i), box, parent);
				}
				return tree;
			} else if (root.layer == 1) {
				Node node = new Node();
				node.setName(root.nodeName);
				box.addElement(node);
				for (int i = 0; i < root.child.size(); i++) {
					generateTree(root.child.elementAt(i), box, node);
				}
				return null;
			} else {
				Node node = new Node();
				node.setName(root.nodeName);
				node.setParent(parent);
				box.addElement(node);
				for (int i = 0; i < root.child.size(); i++) {
					generateTree(root.child.elementAt(i), box, node);
				}
				return null;
			}
		} else if (root.child.size() == 0 && !root.nodeName.equals("noUse")) {
			if (root.layer == 0) {
				TTree tree = new TTree(box);
				tree.setName(root.nodeName);
				return tree;
			} else if (root.layer == 1) {
				Node node = new Node();
				node.setName(root.nodeName);
				box.addElement(node);
				return null;
			} else {
				Node node = new Node();
				node.setName(root.nodeName);
				node.setParent(parent);
				box.addElement(node);
				return null;
			}
		}
		else return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
