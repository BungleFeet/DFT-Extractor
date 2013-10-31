package function.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public abstract class FileBase {

	public static String basePath = "";
	// �������ļ�����
	private FileReader fr = null;
	private BufferedReader br = null;
	private Vector<String> rv = new Vector<String>();
	private HashMap<String, Integer> titleh = new HashMap<String, Integer>();
	// д�����ļ�����
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	private Vector<String> wv = new Vector<String>();
	// ������ļ�����
	private Vector<FileReader> frv = new Vector<FileReader>();
	private Vector<BufferedReader> brv = new Vector<BufferedReader>();
	private Vector<Vector<String>> drv = new Vector<Vector<String>>();
	private Vector<HashMap<String, Integer>> dtitleh = new Vector<HashMap<String, Integer>>();
	private HashMap<String, Integer> rFileNameh = new HashMap<String, Integer>();
	// д����ļ�����
	private Vector<FileWriter> fwv = new Vector<FileWriter>();
	private Vector<BufferedWriter> bwv = new Vector<BufferedWriter>();
	private Vector<Vector<String>> dwv = new Vector<Vector<String>>();
	private HashMap<String, Integer> wFileNameh = new HashMap<String, Integer>();

	public abstract void process() throws IOException;
	public abstract void run(FileBase fb);
	
	public void ropen(String FileName) {
		System.out.println("���ڽ��ļ�" + FileName + "�����ڴ桭��");
		try {
			fr = new FileReader(basePath + FileName);
			br = new BufferedReader(fr);
			String temp = br.readLine();
			String title[] = temp.split(",");
			for (int i = 0; i < title.length; i++) {
				titleh.put(title[i], i);
			}
			while (temp != null) {
				rv.add(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dropenWithClose(String[] FileName) {
		try {
			for (int i = 0; i < FileName.length; i++) {
				System.out.println("���ڽ���" + (i+1) + "���ļ�" + FileName[i] + "�����ڴ桭��");
				rFileNameh.put(FileName[i], i);
				frv.add(new FileReader(basePath + FileName[i]));
				brv.add(new BufferedReader(frv.elementAt(i)));
				Vector<String> tempV = new Vector<String>();
				String temp = brv.elementAt(i).readLine();
				HashMap<String, Integer> temptitleh = new HashMap<String, Integer>();
				String title[] = temp.split(",");
				for (int n = 0; n < title.length; n++) {
					temptitleh.put(title[n], n);
				}
				dtitleh.add(temptitleh);
				while (temp != null) {
					tempV.add(temp);
					temp = brv.elementAt(i).readLine();
				}
				drv.add(tempV);
				System.out.println("�رյ�" + (i+1) + "���ļ�" + FileName[i] + "���ͷ���Դ����");
				brv.elementAt(i).close();
				frv.elementAt(i).close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rclose() {
		System.out.println("�ر��ļ����ͷ���Դ����");
		try {
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void wopen(String FileName) {
		System.out.println("����д���ļ�" + FileName + "����");
		try {
			fw = new FileWriter(basePath + FileName);
			bw = new BufferedWriter(fw);
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dwopen(String[] FileName) {
		try {
			for (int i = 0; i < FileName.length; i++) {
				wFileNameh.put(FileName[i], i);
				System.out.println("����д���ļ�" + FileName[i] + "����");
				fwv.add(new FileWriter(basePath + FileName[i]));
				bwv.add(new BufferedWriter(fwv.elementAt(i)));
				bwv.elementAt(i).flush();
				dwv.add(new Vector<String>());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void wclose() {
		System.out.println("����д���ļ��С���");
		try {
			for (int i = 0; i < wv.size(); i++) {
				bw.write(wv.elementAt(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
			System.out.println("д����ϣ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void dwclose() {
		try {
			for (int i = 0; i < bwv.size(); i++) {
				String temp;
				System.out.println("����д���" + (i+1)+ "���ļ��С���");
				for (int k = 0; k < dwv.elementAt(i).size(); k++) {
					temp = dwv.elementAt(i).elementAt(k);
					bwv.elementAt(i).write(temp);
					bwv.elementAt(i).newLine();
				}
				System.out.println("д���" + (i+1)+ "���ļ���ϣ��رյ�" + (i+1) + "���ļ�����");
				bwv.elementAt(i).flush();
				bwv.elementAt(i).close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void go(String FileName, char rw) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		if (rw == 'r') {
			ropen(FileName);
			rclose();
			try {
				System.out
						.println("-------------------����������---------------------");
				process();
				System.out
						.println("--------------------�������----------------------");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (rw == 'w') {
			try {
				System.out
						.println("-------------------����������---------------------");
				process();
				System.out
						.println("--------------------�������----------------------");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wopen(FileName);
			wclose();
		}
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}

	public void go(String[] FileName, char rw) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		if (rw == 'r') {
			dropenWithClose(FileName);
			try {
				System.out
						.println("-------------------����������---------------------");
				process();
				System.out
						.println("--------------------�������----------------------");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (rw == 'w') {
			dwopen(FileName);
			try {
				System.out
						.println("-------------------����������---------------------");
				process();
				System.out
						.println("--------------------�������----------------------");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dwclose();
		}
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}

	public void go(String[] drFileName, String[] dwFileName) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		dropenWithClose(drFileName);
		dwopen(dwFileName);
		try {
			System.out.println("-------------------����������---------------------");
			process();
			System.out
					.println("--------------------�������----------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dwclose();
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}
	public void go(String[] drFileName, String wFileName) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		dropenWithClose(drFileName);
		wopen(wFileName);
		try {
			System.out.println("-------------------����������---------------------");
			process();
			System.out
					.println("--------------------�������----------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wclose();
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}
	public void go(String rFileName, String[] dwFileName) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		ropen(rFileName);
		rclose();
		dwopen(dwFileName);
		try {
			System.out.println("-------------------����������---------------------");
			process();
			System.out
					.println("--------------------�������----------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dwclose();
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}
	public void go(String rFileName, String wFileName) {
		System.out.println("��ʼִ�С���");
		long  start=System.currentTimeMillis();
		ropen(rFileName);
		rclose();
		try {
			System.out.println("-------------------����������---------------------");
			process();
			System.out
					.println("--------------------�������----------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wopen(wFileName);
		wclose();
		System.out.println("ִ����ϣ�");
		long end=System.currentTimeMillis();
		System.out.println("���ѣ�" + (end - start) + "����");
	}

	public String getStringValue(String ColumnName, int Row) {
		String temp = rv.elementAt(Row);
		String s[] = temp.split(",");
		return s[titleh.get(ColumnName)];
	}

	public String getStringValue(String FileName, String ColumnName, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		String s[] = temp.split(",");
		return s[dtitleh.elementAt(ID).get(ColumnName)];
	}
	public String getStringValue(int Column, int Row) {
		String temp = rv.elementAt(Row);
		String s[] = temp.split(",");
		return s[Column];
	}
	
	public String getStringValue(String FileName, int Column, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		String s[] = temp.split(",");
		return s[Column];
	}
	
	public int getIntegerValue(String ColumnName, int Row) {
		String temp = rv.elementAt(Row);
		String s[] = temp.split(",");
		return Integer.parseInt(s[titleh.get(ColumnName)]);
	}

	public int getIntegerValue(String FileName, String ColumnName, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		String s[] = temp.split(",");
		return Integer.parseInt(s[dtitleh.elementAt(ID).get(ColumnName)]);
	}

	public double getDoubleValue(String ColumnName, int Row) {
		String temp = rv.elementAt(Row);
		String s[] = temp.split(",");
		return Double.parseDouble(s[titleh.get(ColumnName)]);
	}

	public int getIntegerValue(String FileName, int Column, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		String s[] = temp.split(",");
		return Integer.parseInt(s[Column]);
	}
	
	public int getIntegerValue(int Column, int Row) {
		String temp = rv.elementAt(Row);
		String s[] = temp.split(",");
		return Integer.parseInt(s[Column]);
	}
	
	public double getDoubleValue(String FileName, String ColumnName, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		String s[] = temp.split(",");
		return Double.parseDouble(s[dtitleh.elementAt(ID).get(ColumnName)]);
	}

	public String getLineValue(int Row) {
		String temp = rv.elementAt(Row);
		return temp;
	}

	public String getLineValue(String FileName, int Row) {
		int ID = rFileNameh.get(FileName);
		String temp = drv.elementAt(ID).elementAt(Row);
		return temp;
	}
	
	public Vector<String> getAllLine(){
		return rv;
	}
	
	public Vector<String> getAllLine(String FileName){
		int ID = rFileNameh.get(FileName);
		Vector<String> v=drv.elementAt(ID);
		return v;
	}

	public void write(String FileName, String s) {
		int ID = wFileNameh.get(FileName);
		dwv.elementAt(ID).add(s);
	}

	public void write(String s) {
		wv.add(s);
	}

	public int getRows() {
		return rv.size();
	}

	public int getRows(String FileName) {
		int ID = rFileNameh.get(FileName);
		return drv.elementAt(ID).size();
	}
}
