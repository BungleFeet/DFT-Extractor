package function.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * @author MJ
 * @description 1).�����ļ��л��ļ���    2).��ȡ�ļ����ַ���    3).ɾ���ļ�
 */
public class FileUtil {

    public static void main(String args[]) throws IOException {
        // Դ�ļ���
        String url1 = "F:/layer3.txt";
        // Ŀ���ļ���
        String url2 = "F:/layer3-copy.txt";
        // ����Ŀ���ļ���
        copyFile(new File(url1),new File(url2));
    }

    /**
	 * ��ȡdirĿ¼�µ��ļ����Ƽ��ϣ�ȥ���˺�׺��
	 * @param dir
	 * @return
	 */
	public static Vector<String> getDirFileSet(String dir){
		Vector<String> vFile=new Vector<String>();
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName();
			String fileNamePrefix=fileName;
			if(fileName.contains("."))
			 fileNamePrefix=fileName.substring(0, fileName.lastIndexOf("."));
			vFile.add(fileNamePrefix);
		}
		return vFile;
	}
    
    // �����ļ�
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // �½��ļ����������������л���
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // �½��ļ���������������л���
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // ��������
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // ˢ�´˻���������
            outBuff.flush();
        } finally {
            // �ر���
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // �����ļ���
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // �½�Ŀ��Ŀ¼
        (new File(targetDir)).mkdirs();
        // ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // Դ�ļ�
                File sourceFile = file[i];
                // Ŀ���ļ�
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                System.out.println("copy:"+(i+1)+"/"+file.length);
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // ׼�����Ƶ�Դ�ļ���
                String dir1 = sourceDir + "/" + file[i].getName();
                // ׼�����Ƶ�Ŀ���ļ���
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {// ���ļ�ת��ΪGBK�ļ�
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    /**
     * 
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// �����ļ�·��
        if (f.exists() && f.isDirectory()) {// �ж����ļ�����Ŀ¼
            if (f.listFiles().length == 0) {// ��Ŀ¼��û���ļ���ֱ��ɾ��
                f.delete();
            } else {// ��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// �ݹ����del������ȡ����Ŀ¼·��
                    }
                    delFile[j].delete();// ɾ���ļ�
                }
            }
        }
        else f.delete();
    }
    
    /**
     * 
     * @param filePath Ҫ��ȡ���ļ�·��
     * @return �ļ��ַ���
     */
    public static String readFile(String filePath){
    	FileReader fr;
    	StringBuffer sb=new StringBuffer();
    	File f=new File(filePath);
    	if(f.exists()){
    		try {
    			fr = new FileReader(filePath);
    			BufferedReader br=new BufferedReader(fr);
    			String temp=br.readLine();
    			while(temp!=null){
    				sb=sb.append(temp);
    				temp=br.readLine();
    			}
    			br.close();
    			fr.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
    
    /**
     * ��sд��filePath����
     * @param s
     * @param filePath
     */
    public static void writeStringFile(String s,String filePath){
    	try{
    		FileWriter fw=new FileWriter(filePath);
        	BufferedWriter bw=new BufferedWriter(fw);
        	bw.write(s);
        	bw.close();
        	fw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void appendStringFile(String s,String filePath){
    	try{
    		FileWriter fw=new FileWriter(filePath,true);
        	BufferedWriter bw=new BufferedWriter(fw);
        	bw.newLine();
        	bw.write(s);
        	bw.close();
        	fw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}

