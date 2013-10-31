package facetGUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;







public class MainApplet_old extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param args
	 */
	
	public void init() {
		// ��ʼ��һ������
		Container c = this.getContentPane();
		// frame����
		setVisible(true);
		this.setLocation(300, 200);
		this.setSize(1020, 650);
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
		
		// �����������
		JTabbedPane funPane = new JTabbedPane();// �������
		funPane.setBackground(new Color(67,142,228));
		funPane.setForeground(new Color(240,245,247));
		CrawlerPanel jpCrawler = new CrawlerPanel();// �����������
		Html2TxtPanel jpHtml2Txt = new Html2TxtPanel();// HTML������ȡ���
		LinkFeaturePanel jpLinkFeature = new LinkFeaturePanel();// ���������������
		TextFeaturePanel jpTextFeature = new TextFeaturePanel();// �ı������������
		NetFeaturePanel jpNetFeature = new NetFeaturePanel();// ���������������
		DTExtractionPanel jpDTExtraction = new DTExtractionPanel();// ���������ȡ���
		HypRelationPanel_old jpHyponymy = new HypRelationPanel_old();// ����λ��ϵʶ�����
		JPanel jpFacetedTerm = new JPanel();// ���������ȡ���
		FacetedTreePanel jpTaxonomy = new FacetedTreePanel();// ������ϵ�������
		funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// ������岼��
		c.add(funPane, BorderLayout.CENTER);
		funPane.addTab("������ϵ����", jpTaxonomy);
		funPane.addTab("��������", jpCrawler);
		funPane.addTab("HTML������ȡ", jpHtml2Txt);
		funPane.addTab("������������", jpLinkFeature);
		funPane.addTab("�ı���������", jpTextFeature);
		funPane.addTab("������������", jpNetFeature);
		funPane.addTab("���������ȡ", jpDTExtraction);
		funPane.addTab("����λ��ϵʶ��", jpHyponymy);
		funPane.addTab("���������ȡ", jpFacetedTerm);
	}
}
