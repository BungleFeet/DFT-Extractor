package function.txtAnalysis.util;

import java.util.Vector;

public class ReplaceChars {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 
	 * @param vTerm
	 * @return �滻�����е�",_"Ϊ"#_"
	 */
	public static Vector<String> replaceDouGang(Vector<String> vTerm){
		Vector<String> v=new Vector<String>();
		for(String s:vTerm){
			s=s.replace(",_", "#_");
			v.add(s);
		}
		return v;
	}
	/**
	 * 
	 * @param vTerm
	 * @return �滻�����е�"#_"Ϊ",_"
	 */
	public static Vector<String> replaceIDouGang(Vector<String> vTerm){
		Vector<String> v=new Vector<String>();
		for(String s:vTerm){
			s=s.replace("#_", ",_");
			v.add(s);
		}
		return v;
	}

}
