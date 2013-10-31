package facetGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class MainFrame_old extends JFrame {

	private static final long serialVersionUID = -4209263941008740114L;
	/**
	 * @param args
	 */

	public MainFrame_old() {
		// ��ʼ��һ������
		Container c = this.getContentPane();
		// frame����
		setTitle("���������ϵ����ƽ̨");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300, 200);
		setSize(1020, 780);
		c.setBackground(Color.WHITE);
		try {
			Properties props = new Properties();
            props.put("logoString", "my company");
            
            com.jtattoo.plaf.mcwin.McWinLookAndFeel.setCurrentTheme(props);
			//com.jtattoo.plaf.mcwin.McWinLookAndFeel.setTheme("Pink", "", "my company");
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");

            // Start the application
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        JTabbedPane funPane = new JTabbedPane();// �������
    	CrawlerPanel jpCrawler = new CrawlerPanel();// �����������
    	Html2TxtPanel jpHtml2Txt = new Html2TxtPanel();// HTML������ȡ���
    	LinkFeaturePanel jpLinkFeature = new LinkFeaturePanel();// ���������������
    	TextFeaturePanel jpTextFeature = new TextFeaturePanel();// �ı������������
    	NetFeaturePanel jpNetFeature = new NetFeaturePanel();// ���������������
    	DTExtractionPanel jpDTExtraction = new DTExtractionPanel();// ���������ȡ���
    	HypRelationPanel_old jpHyponymy = new HypRelationPanel_old();// ����λ��ϵʶ�����
    	FacetedTreePanel jpTaxonomy = new FacetedTreePanel();// ������ϵ�������
		// �����������
		funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// ������岼��
		c.add(funPane, BorderLayout.CENTER);
		funPane.addTab("��������", jpCrawler);
		funPane.addTab("HTML������ȡ", jpHtml2Txt);
		funPane.addTab("������������", jpLinkFeature);
		funPane.addTab("�ı���������", jpTextFeature);
		funPane.addTab("������������", jpNetFeature);
		funPane.addTab("���������ȡ", jpDTExtraction);
		funPane.addTab("����λ��ϵʶ��", jpHyponymy);
		funPane.addTab("������ϵ����", jpTaxonomy);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame_old();
	}

}
