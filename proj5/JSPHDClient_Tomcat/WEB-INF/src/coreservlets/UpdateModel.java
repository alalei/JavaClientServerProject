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
import net.HTMLPageMaker;
import client.SelectCarOption;

public class UpdateModel extends HttpServlet {
	private static final long serialVersionUID = 3196362946617189238L;
	private static final boolean DEBUG = true;
	private static final boolean DEBUG2 = true;
	String strLocalHost;
	int port = 8050;
	SelectCarOption testClient;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		strLocalHost = InetAddress.getLocalHost().getHostName();
		
		/* display models from remote server via HTML */
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession currentReqSession = request.getSession(false);
		if (currentReqSession == null){
			out.println(HTMLPageMaker.ErrorHTMLPage("Update Model Failure","Error: failed to get a valid session, please try again"));
			return;
		}
		if (DEBUG) System.out.println("\nCaptured a session based on client's request: " + currentReqSession);
		
		SelectCarOption testClient = (SelectCarOption) currentReqSession.getAttribute("testClient");
		if (DEBUG) System.out.println("Get client in session: " + testClient);
		if (testClient == null){
			out.println(HTMLPageMaker.ErrorHTMLPage("Update Model Failure","Update failure: session is timed-out, please try again"));
			return;
		}
		//testClient.printModel();
		
		/* retrieve automobile from session */
		Automobile automobileModel = (Automobile) currentReqSession.getAttribute("automobileModel");
		//Automobile automobileModel = testClient.getModelfromLocal();
		if (automobileModel == null){
			out.println(HTMLPageMaker.ErrorHTMLPage("Update Model Failure","Error: fail to read model, please try again"));
			return;
		}
		if (DEBUG2) automobileModel.print();
		
		/* update attributes */
		String[] optSetsNames = automobileModel.getOptionSetNames();
        for (int i=0; i < optSetsNames.length; i++){
        	String optionName = request.getParameter(optSetsNames[i]);
        	if (DEBUG2) System.out.println("OptionSet: " + optSetsNames[i] + ", option: " + optionName);
        	automobileModel.setOptionChoice(optSetsNames[i], optionName);
        	if (DEBUG2) System.out.println("automobileModel.setOptionChoice(optSetsNames[i], optionName): " + optSetsNames[i] + optionName);
        }
        if (! testClient.sendModelsToServer (automobileModel)){
        	out.println(HTMLPageMaker.ErrorHTMLPage("Update Model failure",
        			"Error: failed to update model on remote server, please try again"));
        	return;
        }
        else{
        	/* update session */
        	currentReqSession.setAttribute("automobileModel",automobileModel);
        	if (DEBUG2) automobileModel.print();
        	response.sendRedirect("displayModel.jsp");
        	return;
        }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

}
