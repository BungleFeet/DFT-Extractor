package facetGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import function.linkAnalysis.NavboxExtractor;
import function.linkAnalysis.URLtoExcel07;
import function.linkAnalysis.redirectProcess.RedirectProcess;
import function.util.SetUtil;

public class LinkFeaturePanel extends JPanel {

	private static final long serialVersionUID = 7584977725711945646L;
	
	private JTabbedPane funPane = new JTabbedPane(JTabbedPane.TOP);// �������
	private JPanel getLinkPanel = new JPanel();//���ӳ�ȡ���
	private JPanel redirectAnalysisPanel = new JPanel();//�ض���������
	private JPanel boxGetPanel = new JPanel();//NavBox��ȡ���
	
	private JLabel excelLabel = new JLabel("HTML�ļ���·����");
	private JTextField excelTf = new JTextField();
	private JButton excelChooseBtn = new JButton("���...");
	private JButton excelGetLinkBtn = new JButton("���ӳ�ȡ");
	private JLabel redirectLabel = new JLabel("���Ｏ�ļ�·����");
	private JTextField redirectTf = new JTextField();
	private JButton redirectChooseBtn = new JButton("���...");
	//private String[] redirectTag = { "Web","����" };
	//private JComboBox redirectJcb = new JComboBox(redirectTag);
	private JButton redirectAnalysisBtn = new JButton("�ض������");
	private JLabel boxLabel = new JLabel("���Ｏ�ļ�·����");
	private JTextField boxTf = new JTextField();
	private JButton boxChooseBtn = new JButton("���...");
	private JButton boxGetTextBtn = new JButton("NavBox��ȡ");
	private Object[] colNames = { "����", "��������" };
	private DefaultTableModel tableModel = new DefaultTableModel(colNames, 50);
	private JTable table = new JTable(tableModel);
	private Border border = BorderFactory.createEtchedBorder(Color.white,
			Color.gray);
	private Border resultTitle = BorderFactory.createTitledBorder(border,
			"���չʾ", TitledBorder.LEFT, TitledBorder.TOP);
	private JScrollPane resultSp = new JScrollPane(table);

	/**
	 * @param args
	 */
	public LinkFeaturePanel() {
		setLayout(null);
		this.setBackground(Color.white);
		funPane.setBounds(0, 10, 1000, 150);
		resultSp.setBounds(0, 180, 1000, 420);
		
		//������岼������
		this.add(funPane);
		funPane.addTab("���ӳ�ȡ",getLinkPanel);
		funPane.add("�ض������",redirectAnalysisPanel);
		funPane.add("NavBox��ȡ",boxGetPanel);
		
		// URLtoExcel��������
		getLinkPanel.setLayout(null);
		getLinkPanel.add(excelLabel);
		getLinkPanel.add(excelTf);
		getLinkPanel.add(excelChooseBtn);
		getLinkPanel.add(excelGetLinkBtn);
		getLinkPanel.setBackground(new Color(230,239,248));
		
		excelLabel.setBounds(30, 20, 150, 25);
		excelTf.setBounds(155, 20, 400, 25);
		excelChooseBtn.setBounds(570, 20, 60, 25);
		excelGetLinkBtn.setBounds(545, 65, 85, 25);
		
		excelChooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/Data");
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ����ѡ���ļ���
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ���
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					excelTf.setText(path);
				}
			}
		});
		excelGetLinkBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String dir = excelTf.getText();
				final String desPath = dir + "-xlsx\\result.xlsx";
				if (dir.length() == 0) {
					JOptionPane.showMessageDialog(LinkFeaturePanel.this,
							"��ѡ��HTML�ļ���·����");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							URLtoExcel07 ue = new URLtoExcel07();
							ue.extractUrlToExcel(dir, desPath);
							JOptionPane.showMessageDialog(
									LinkFeaturePanel.this, "����ļ��ѱ��浽" + desPath
											+ "�");
						}
					});
					t.start();
				}
			}
		});
		// �ض��������������
		redirectAnalysisPanel.setLayout(null);
		redirectAnalysisPanel.add(redirectLabel);
		redirectAnalysisPanel.add(redirectTf);
		redirectAnalysisPanel.add(redirectChooseBtn);
		//this.add(redirectJcb);
		redirectAnalysisPanel.add(redirectAnalysisBtn);
		redirectAnalysisPanel.setBackground(new Color(230,239,248));
		
		redirectLabel.setBounds(30, 20, 150, 25);
		redirectTf.setBounds(155, 20, 400, 25);
		redirectChooseBtn.setBounds(570, 20, 60, 25);
		//redirectJcb.setBounds(655, 65, 40, 25);
		redirectAnalysisBtn.setBounds(545, 65, 85, 25);
		redirectChooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/Data");
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ�
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					redirectTf.setText(path);
				}
			}
		});
		redirectAnalysisBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String fileName = redirectTf.getText();
				final String desPath = fileName
						.replace(".txt", "-redirect.xls");
				if (fileName.length() == 0) {
					JOptionPane.showMessageDialog(LinkFeaturePanel.this,
							"��ѡ�����Ｏ�ļ�·����");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							RedirectProcess ue = new RedirectProcess(fileName,false);
							ue.run(ue);
							JOptionPane.showMessageDialog(
									LinkFeaturePanel.this, "����ļ��ѱ��浽" + desPath
											+ "�");
						}
					});
					t.start();
				}
			}
		});
		// NavBox��ȡ��������
		boxGetPanel.add(boxLabel);
		boxGetPanel.add(boxTf);
		boxGetPanel.add(boxChooseBtn);
		boxGetPanel.add(boxGetTextBtn);
		boxGetPanel.setLayout(null);
		boxGetPanel.setBackground(new Color(230,239,248));
		
		boxLabel.setBounds(30, 20, 150, 25);
		boxTf.setBounds(155, 20, 400, 25);
		boxChooseBtn.setBounds(570, 20, 60, 25);
		boxGetTextBtn.setBounds(545, 65, 90, 25);
		boxChooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/Data");
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ�
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					boxTf.setText(path);
				}
			}
		});
		boxGetTextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String fileName = boxTf.getText();
				final Vector<String> vWikiTerm = SetUtil
						.readSetFromFile(fileName);
				final String desPath = fileName.substring(0,
						fileName.lastIndexOf("\\") + 1)
						+ "navbox";
				if (fileName.length() == 0) {
					JOptionPane.showMessageDialog(LinkFeaturePanel.this,
							"��ѡ�����Ｏ�ļ�·����");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							NavboxExtractor.extractNavBox(vWikiTerm, desPath);
							showmatchRelation(desPath); //��ʾ���
							JOptionPane.showMessageDialog(
									LinkFeaturePanel.this, "����ļ��ѱ��浽" + desPath
											+ "�");
						}
					});
					t.start();
				}
			}
		});
		// �����������
		this.add(resultSp);
		resultSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultSp.setBorder(resultTitle);
		
	}
	
	//��matchRelation������ʾ�������
	public void showmatchRelation(String desPath) {
		String matchRelationFile=desPath+"/matchRelation.csv";
		
		for (int index = tableModel.getRowCount() - 1; index >= 0; index--) {
			tableModel.removeRow(index);
		}
		
		try {
			FileReader fr = new FileReader(matchRelationFile);
			BufferedReader br = new BufferedReader(fr);
			String line= br.readLine();     //�����ȡ��һ��
			String term[] = new String[2];  //������е���������
			
			while(line != null) {
				term = line.split(",");
				tableModel.addRow(term);
				line = br.readLine();
				repaint();
			}
			br.close();
			fr.close();
		}catch(IOException e)  {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
