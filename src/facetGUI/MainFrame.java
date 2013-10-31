package facetGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -4209263941008740114L;
	private String tempPath="";
	private String tempDN="";
	public MainFrame(){
		Container c = this.getContentPane();
		// frame����
		this.setVisible(true);
		this.setLocation(200, 10);
		this.setSize(1030, 850);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DFT-Extractor: Extracting Domain-specific Faceted Taxonomies from Wikipedia");
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
		final DTExtractionPanel jpDTExtraction = new DTExtractionPanel();// ���������ȡ���
		final HypRelationPanel jpHyponymy = new HypRelationPanel();// ����λ��ϵʶ�����
		final FacetedTreePanel_new jpTaxonomy = new FacetedTreePanel_new();// ������ϵ�������
		funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// ������岼��
		c.add(funPane, BorderLayout.CENTER);
		funPane.addTab("Focused Crawling and Filtering", jpDTExtraction);
		funPane.addTab("Hyponym Relation Extraction", jpHyponymy);
		funPane.addTab("Faceted Taxonomy Generation", jpTaxonomy);
		
		//·�������¼�
		funPane.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				String domainName=jpDTExtraction.getDomainName();
				String savePath=jpDTExtraction.getSavePath();
				if(!tempPath.equals(savePath)||!tempDN.equals(domainName)){
					tempPath=savePath;
					tempDN=domainName;
					String htmlPath=savePath+"\\hyperExtraction\\"+domainName+"\\html";
					jpHyponymy.setHtmlDirPath(htmlPath);
					String relationPath=savePath+"\\facetedTree\\"+domainName+"\\relation.csv";
					jpTaxonomy.setRelationFilePath(relationPath);
				}//�����仯�Ÿı�������������������
				else{
					File fHtml=new File(jpHyponymy.getHtmlDirPath());
					String hypoDomainName=fHtml.getParentFile().getName();//�ڶ���������������
					String hypoSavePath=fHtml.getParentFile().getParentFile().getParentFile().getAbsolutePath();//�ڶ������ı���·��
					String relationPath=hypoSavePath+"\\facetedTree\\"+hypoDomainName+"\\relation.csv";
					jpTaxonomy.setRelationFilePath(relationPath);
				}//�������ı�ֻ���ݵڶ�������html·���ı������·��
			}
			
		});
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame();
	}

}
