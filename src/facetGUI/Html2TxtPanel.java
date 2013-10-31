package facetGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import function.jsoup.WikiHtml2Txt;

public class Html2TxtPanel extends JPanel {

	private static final long serialVersionUID = 1770238087081107574L;
	
	private JTabbedPane funPane = new JTabbedPane(JTabbedPane.TOP);// �������
	private JPanel urlPanel = new JPanel();//url��ȡ���
	private JPanel filePanel = new JPanel();//�ļ���ȡ���
	private JPanel dirPanel = new JPanel();//�ļ�����ȡ���
	
	private JLabel urlLabel = new JLabel("   ������HTMLԴURL��");
	private JTextField urlTf = new JTextField();
	private JButton urlGetTextBtn = new JButton("��ȡ����");
	private JLabel fileLabel = new JLabel(" ��ѡ��HTML�ļ���");
	private JTextField fileTf = new JTextField();
	private JButton filechooseBtn = new JButton("���...");
	private JButton fileGetTextBtn = new JButton("��ȡ����");
	private JLabel dirLabel = new JLabel("��ѡ��HTML�ļ��У�");
	private JTextField dirTf = new JTextField();
	private JButton dirchooseBtn = new JButton("���...");
	private JButton dirGetTextBtn = new JButton("��ȡ����");
	private JTextArea htmlTa = new JTextArea();
	private JTextArea textTa = new JTextArea();
	private JScrollPane htmlSp = new JScrollPane(htmlTa);
	private JScrollPane textSp = new JScrollPane(textTa);
	private Border border = BorderFactory.createEtchedBorder(Color.white,
			Color.gray);
	private Border htmlTitle = BorderFactory.createTitledBorder(border,
			"��ҳԴ�ļ�", TitledBorder.LEFT, TitledBorder.TOP);
	private Border textTitle = BorderFactory.createTitledBorder(border, "����",
			TitledBorder.LEFT, TitledBorder.TOP);
	private JButton textAreaGetTextBtn = new JButton("��ȡ����");
	private String htmlContent = "";
	private String textContent = "";

	/**
	 * @param args
	 */
	public Html2TxtPanel() {
		setLayout(null);
		final WikiHtml2Txt wht = new WikiHtml2Txt();
		this.setBackground(Color.white);
		funPane.setBounds(0, 10, 1000, 150);
		htmlSp.setBounds(0, 170, 1000, 200);
		textSp.setBounds(0, 400, 1000, 200);
		textAreaGetTextBtn.setBounds(880, 375, 85, 25);
		
		//����ѡ�����
		this.add(funPane);
		funPane.addTab("����Դurl��ȡ",urlPanel);
		funPane.addTab("���ݵ����ļ���ȡ",filePanel);
		funPane.addTab("���ݶ���ļ���ȡ",dirPanel);
		
		// URL������������
		urlPanel.add(urlLabel);
		urlPanel.add(urlTf);
		urlPanel.add(urlGetTextBtn);
		urlPanel.setLayout(null);
		urlPanel.setBackground(new Color(230,239,248));
		
		urlLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		urlLabel.setBounds(10, 20, 150, 25);
		urlTf.setBounds(155, 20, 400, 25);
		urlGetTextBtn.setBounds(470, 65, 85, 25);
		urlGetTextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String url = urlTf.getText();
				if (url.length() == 0) {
					JOptionPane.showMessageDialog(Html2TxtPanel.this,
							"������HTMLԴURL��ַ��");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							htmlContent = wht.parseURLHtml(url);
							textContent = wht.parseURLText(url);
							htmlTa.setText(htmlContent);
							textTa.setText(textContent);
						}
					});
					t.start();
				}
			}
		});
		// �ļ�������������
		filePanel.add(fileLabel);
		filePanel.add(fileTf);
		filePanel.add(filechooseBtn);
		filePanel.add(fileGetTextBtn);
		filePanel.setLayout(null);
		filePanel.setBackground(new Color(230,239,248));
		
		fileLabel.setBounds(10, 20, 150, 25);
		fileTf.setBounds(155, 20, 400, 25);
		filechooseBtn.setBounds(570, 20, 60, 25);
		fileGetTextBtn.setBounds(470, 65, 85, 25);
		filechooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/Data");
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ�
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					fileTf.setText(path);
				}
			}
		});
		fileGetTextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String path = fileTf.getText();
				if (path.length() == 0) {
					JOptionPane.showMessageDialog(Html2TxtPanel.this,
							"��ѡ��HTML�ļ�·����");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								FileReader fr = new FileReader(path);
								BufferedReader br = new BufferedReader(fr);
								String temp = br.readLine();
								htmlTa.setText("");
								while (temp != null) {
									htmlTa.append(temp);
									temp = br.readLine();
								}
								br.close();
								fr.close();
								textContent = wht.parsePathText(path);
								textTa.setText(textContent);
								if (JOptionPane.showConfirmDialog(
										Html2TxtPanel.this,
										"�Ƿ�Ҫ�������ͬ���ļ����±�����ı��ļ���", "������ʾ",
										JOptionPane.YES_NO_OPTION) == 0) {
									String txtPath = path.substring(0,
											path.length() - 5)
											+ ".txt";
									FileWriter fw = new FileWriter(txtPath);
									BufferedWriter bw = new BufferedWriter(fw);
									bw.write(textContent);
									bw.flush();
									bw.close();
									fw.close();
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					t.start();
					textTa.setText(textContent);
				}

			}
		});
		// �ļ��з�����������
		dirPanel.add(dirLabel);
		dirPanel.add(dirTf);
		dirPanel.add(dirchooseBtn);
		dirPanel.add(dirGetTextBtn);
		dirPanel.setLayout(null);
		dirPanel.setBackground(new Color(230,239,248));
		
		dirLabel.setBounds(10, 20, 150, 25);
		dirTf.setBounds(155, 20, 400, 25);
		dirchooseBtn.setBounds(570, 20, 60, 25);
		dirGetTextBtn.setBounds(470, 65, 85, 25);
		dirchooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser file = new JFileChooser("f:/Data");
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ����ѡ���ļ���
				int result = file.showOpenDialog(null);
				// JFileChooser.APPROVE_OPTION��0�������Ѿ�ѡ�����ļ�
				if (result == JFileChooser.APPROVE_OPTION) {
					// �����ѡ����ļ�����·�������������Ȼ�����ǻ�����·���󻹿������ܶ���¡�
					String path = file.getSelectedFile().getAbsolutePath();
					dirTf.setText(path);
				}
			}
		});
		dirGetTextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final String dir = dirTf.getText();
				if (dir.length() == 0) {
					JOptionPane.showMessageDialog(Html2TxtPanel.this,
							"��ѡ��HTML�ļ���·����");
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							wht.parseDirText(dir);
							JOptionPane.showMessageDialog(Html2TxtPanel.this,
									"����ļ��ѱ��浽" + dir + "-txt�£�");
						}

					});
					t.start();
				}
			}
		});
		// ��ҳԴ�ļ���������
		this.add(htmlSp);
		this.add(textAreaGetTextBtn);
		htmlSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		htmlSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		htmlTa.setLineWrap(true);
		htmlSp.setBorder(htmlTitle);
		
		
		textAreaGetTextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				htmlContent = htmlTa.getText();
				if (htmlContent.length() == 0) {
					JOptionPane.showMessageDialog(Html2TxtPanel.this,
							"��������ҳ���ݣ�");
				} else {
					if (htmlContent.toLowerCase().contains("<html>"))
						textContent = wht.parseHtmlString(htmlContent);
					else
						textContent = wht.parseBodyString(htmlContent);
					textTa.setText(textContent);
				}
			}
		});
		// ���Ĳ�������
		this.add(textSp);
		textSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textTa.setLineWrap(true);// �Զ�����
		textSp.setBorder(textTitle);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
