package facetGUI;

import java.awt.Color;
import java.awt.Container;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainFrame_liu extends JFrame {
	private static final long serialVersionUID = -4209263941008740114L;
	public MainFrame_liu(){
		Container c = this.getContentPane();
		// frame����
		this.setVisible(true);
		this.setLocation(200, 10);
		this.setSize(1030, 810);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Hyponym Relation Extraction From Wikipedia Hyperlinks");
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
		//JTabbedPane funPane = new JTabbedPane();// �������
		//funPane.setBackground(new Color(67,142,228));
		//funPane.setForeground(new Color(240,245,247));
		//DTExtractionPanel jpDTExtraction = new DTExtractionPanel();// ���������ȡ���
		HypRelationPanel jpHyponymy = new HypRelationPanel();// ����λ��ϵʶ�����
		//FacetedTreePanel jpTaxonomy = new FacetedTreePanel();// ������ϵ�������
		//funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// ������岼��
		//c.add(funPane, BorderLayout.CENTER);
		//funPane.addTab("Focused Crawling and Filtering", jpDTExtraction);
		//funPane.addTab("Hyponym Relation Extraction", jpHyponymy);
		//funPane.addTab("Faceted Taxonomy Generation", jpTaxonomy);
		this.add(jpHyponymy);
		jpHyponymy.setBounds(0, 0, 1030, 810);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame_liu();
	}

}
