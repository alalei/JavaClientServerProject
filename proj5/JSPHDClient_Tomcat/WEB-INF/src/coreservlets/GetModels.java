package coreservlets;

import java.io.*;
import java.net.InetAddress;

import javax.servlet.*;
import javax.servlet.http.*;

import net.HTMLPageMaker;
import client.SelectCarOption;
 
public class GetModels extends HttpServlet {
	private static final long serialVersionUID = -5364275593456816101L;
	private static final boolean DEBUG = true;
	String strLocalHost;
	int port = 8050;
	        
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		strLocalHost = InetAddress.getLocalHost().getHostName();
		if (DEBUG) System.out.println("\n--- Created a CarModelOptionsIO Thread \n");		
		
		/* Query models on server via client */
		if (DEBUG) System.out.println("\n--- Query models on server via client\n");
		SelectCarOption testClient2 = new SelectCarOption(strLocalHost, port);
		if (DEBUG) System.out.println("Client connected to " + strLocalHost +":" + port + " (default port)");
		if (DEBUG) System.out.println("Get Models from Server");
		String ModelList = testClient2.queryModelsfromServer();
		if (DEBUG) System.out.println( "Received message: " + ModelList );
		

		/* display models from remote server via HTML */
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		/* display error page */
		if (ModelList == null) {
			out.println(HTMLPageMaker.ErrorHTMLPage("Choose Model","Warning: no model returned from server"));
			return;
		}
		
		String[] models = ModelList.split("\n");
		if ( !models[0].contains("Model names")){
			out.println(HTMLPageMaker.ErrorHTMLPage("Choose Model","Warning: server returned invalid information"));
			return;
		}
		if ( models.length < 2){
			out.println(HTMLPageMaker.ErrorHTMLPage("Choose Model","Warning: server returned no model"));
			return;
		}
		
		/* Display model list */
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>");
        out.println("Choose Model");
        out.println("</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<p> Please choose a model to proceed.</p>");
        out.println("<form method=\"post\" action=\"GetOptionSets\">");
        /* Forms of models */
        out.println(models[0]);
        out.println("<select name=selectedModel>");// onChange=this.form.submit()>"
        for(int i = 1; i<models.length; i++){
        	out.println("<option value=\"" + models[i] + "\">" + models[i] +"</option>");
        }
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"Get model\">");
        out.println("</form>");
        /* Client configuration information */
        out.println("<p>Host Address: " + strLocalHost + "</p>");
        out.println("<p>Port: " + port + "</p>");
        out.println("</BODY>");
        out.println("</HTML>");
		}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

}
