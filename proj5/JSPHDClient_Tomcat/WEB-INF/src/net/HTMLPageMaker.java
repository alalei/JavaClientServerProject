package net;

public class HTMLPageMaker {
	public static String ErrorHTMLPage(String title, String content){
		StringBuilder page = new StringBuilder();
		page.append("<HTML>");
		page.append("<HEAD> <TITLE>");
		page.append(title);
		page.append("</TITLE> </HEAD>");
		page.append("<BODY>");
		page.append(content);
		page.append("</BODY>");
		page.append("</HTML>");
		
		return page.toString();
	}

}
