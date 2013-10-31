package function.util;

import java.util.ArrayList; 
import java.util.BitSet; 
/** 
   * java����㷨��ʵ�� 
   * 
   */
public class Combination { 
  
    private ArrayList<String> combList= new ArrayList<String>(); 
/** 
   *mn������ʵ�� 
   * 
   */
    public void mn(String[] array, int n) { 
        int m = array.length; 
        if (m < n) 
            throw new IllegalArgumentException("Error   m   <   n"); 
        BitSet bs = new BitSet(m); 
        for (int i = 0; i < n; i++) { 
            bs.set(i, true); 
        } 
        do { 
            printAll(array, bs); 
        } while (moveNext(bs, m)); 
  
    } 
    /**˵�� ���������̫������⣬Ҳ����ϵĺ����㷨,������м򵥵Ľ���. 
     * 1��start�Ǵӵ�һ��trueƬ����Ϊ��ʼλ��end��Ϊ��ֹλ 
     * 2����һ��trueƬ�ζ����ó�false 
     * 3�������0�±���ʼ���Ե�һ��trueƬ��Ԫ��������һΪ�±��λ�ö���true 
     * 4���ѵ�һ��trueƬ��end��ֹλ��true 
     * 
     * @param bs �����Ƿ���ʾ�ı�־λ 
     * @param m ���鳤�� 
     * @return boolean �Ƿ���������� 
     */
    private boolean moveNext(BitSet bs, int m) { 
        int start = -1; 
        while (start < m) 
            if (bs.get(++start)) 
                break; 
        if (start >= m) 
            return false; 
  
        int end = start; 
        while (end < m) 
            if (!bs.get(++end)) 
                break; 
        if (end >= m) 
            return false; 
        for (int i = start; i < end; i++) 
            bs.set(i, false); 
        for (int i = 0; i < end - start - 1; i++) 
            bs.set(i); 
        bs.set(end); 
        return true; 
    } 
  
    /** 
     * �����ӡ��� 
     * 
     * @param array ���� 
     * @param bs ����Ԫ���Ƿ���ʾ�ı�־λ���� 
     */
    private void printAll(String[] array, BitSet bs) { 
        StringBuffer sb = new StringBuffer(); 
                //ƴ��StringBuffer������ط�һ��Ҫ��StringBuffer����Ҫֱ��ʹ��String ��+ 
        for (int i = 0; i < array.length; i++) 
            if (bs.get(i)) { 
                sb.append(array[i]).append(','); 
            } 
        sb.setLength(sb.length() - 1); 
        combList.add(sb.toString()); 
    } 
  
    public ArrayList<String> getCombList() { 
        return combList; 
    } 
      /** 
     * ���������� 
     * 
     */
    public static void main(String[] args) throws Exception { 
        Combination comb = new Combination(); 
        comb.mn(new String[]{"1","2","3","4","5","6"}, 2); 
        for (int i = 0; i < comb.getCombList().size(); i++) { 
            System.out.println(comb.getCombList().get(i)); 
        } 
    } 
  
}
