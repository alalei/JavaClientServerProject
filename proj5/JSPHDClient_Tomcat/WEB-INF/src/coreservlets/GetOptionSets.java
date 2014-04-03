package coreservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Automobile;
import client.SelectCarOption;

public class GetOptionSets extends HttpServlet {
	private static final long serialVersionUID = 4074743145389307633L;
	private static final boolean DEBUG = true;
	private static final boolean DEBUG2 = true;
	String strLocalHost;
	int port = 8050;
	String modelName;
	SelectCarOption testClient;
	boolean isNewSession = true;
	public Automobile automobileModel;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		if (DEBUG) System.out.println("\nReceived request:") ;
		if (DEBUG) System.out.println(request.getRequestURL());
		
		strLocalHost = InetAddress.getLocalHost().getHostName();
		
		/* check the session */
		HttpSession currentReqSession = request.getSession(false);
		modelName = request.getParameter("selectedModel");
		
		if ( currentReqSession == null){
			isNewSession = true;/* if this is a new session */
        	currentReqSession = request.getSession(true);
			if (DEBUG) System.out.println("Create a new session: " + currentReqSession.getId());
			/* create client class to interact with remote server */
			testClient = new SelectCarOption(strLocalHost, port);
			if (DEBUG) System.out.println("\n--- Created a SelectCarOption Thread \n");	
		}
		else{
			isNewSession = false;
			/* find an existing session */
			if (DEBUG) System.out.println("Find an existing session: " + currentReqSession.getId());
			testClient = (SelectCarOption) currentReqSession.getAttribute("testClient");
			if (DEBUG2) System.out.println("Find testClient in session: " + testClient);
			if( modelName == null){
				modelName = (String) currentReqSession.getAttribute("modelName");
			}
			if (DEBUG2) System.out.println("Find modelName: "+modelName);
		}

		if(testClient == null || modelName == null) {
			if (DEBUG) System.out.println("Error: no valid client or modelName");
			response.sendRedirect("GetModels");
			return;
		}
		
		/* get object from server */
		automobileModel = testClient.getModelfromServer(modelName);
		if (DEBUG) System.out.println("\nSelected Model from the Server: " + strLocalHost);
		if (DEBUG2) testClient.printModel();
		
		/* display models from remote server via HTML */
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// modelName = request.getParameter("selectedModel");
		if (modelName == null || modelName.equals("")){
			response.sendRedirect("GetModels");
			if (DEBUG) System.out.println("Error: failed to select a model");
			return;
		}
		if (DEBUG2) out.println("<p> RequestURL(): " + request.getRequestURL() + "</p>");
		out.println("<p> Selected Model</p>");
		
		/* Display model list */
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>");
        out.println("Choose Model");
        out.println("</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<form method=\"post\" action=\"UpdateModel\">");
        out.println("<TABLE border=\"1\">");
        /* Forms of models */
        out.println("<TR><TD>Name</TD><TD>" + automobileModel.getName() + "\n"+ "</TD></TR>");
        out.println("<TR><TD>Model</TD><TD>" + automobileModel.getModel() + "\n"+ "</TD></TR>");
        out.println("<TR><TD>Make</TD><TD>" + automobileModel.getMake() + "\n"+ "</TD></TR>");
        String[] optSetsNames = automobileModel.getOptionSetNames();
        for (int i=0; i < optSetsNames.length; i++){
        	out.println("<TR><TD>" + optSetsNames[i] + "\n" +"</TD>");
        	String[] optionNames = automobileModel.getOptionNames(optSetsNames[i]);
        	out.println("<TD><select name=\"" + optSetsNames[i] + "\" value=\"" + optionNames[0]+ "\">");
        	for(String optionName: optionNames){
        		out.println("<option value=\"" + optionName + "\">" + optionName +"</option>");
        	}
        	out.println("</select></TD></TR>");
        }
        
        
        out.println("</TABLE>");
        out.println("<br/>Update the model: <input type=\"submit\" value=\"Done\">");  
        out.println("</form>");
        
        /* Client configuration information */
        out.println("<p>Host Address: " + strLocalHost + "</p>");
        out.println("<p>Port: " + port + "</p>");
        out.println("</BODY>");
        out.println("</HTML>");
        
        /*bind resources to the session */
		currentReqSession.setAttribute("modelName", modelName);
		currentReqSession.setAttribute("testClient", testClient);
		if (DEBUG) System.out.println("Set testClient in session: " + currentReqSession.getAttribute("testClient"));
		currentReqSession.setAttribute("automobileModel",automobileModel);
		currentReqSession.setMaxInactiveInterval(30); // the session will be timed out in 0.5 min
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

}
