package edu.kh.semi.common;

public class XSS {
	
	public static String replaceParameter(String parameter) {
		
		if(parameter != null) {
			parameter = parameter.replaceAll("&", "&amp;");
			parameter = parameter.replaceAll("<", "&lt;");
			parameter = parameter.replaceAll(">", "&gt;");
			parameter = parameter.replaceAll("\"", "&quot;");
		}
		
		return parameter;
	}
	
}
