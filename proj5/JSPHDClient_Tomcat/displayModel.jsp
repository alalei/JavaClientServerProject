<HTML>
  <HEAD>
    <TITLE>Model information</TITLE>
    <%@ page import="model.*" %>
  </HEAD>

    <BODY>
        <H2><% out.println("Model Updated Successfully"); %></H2>
        <H3><% out.println("Below is what you selected:"); %></H3>
        <% 
        	HttpSession currentReqSession = request.getSession(false);
        	if (currentReqSession == null) {
		%>		<p>The session is timed out</p> <%
        		return;
         	}
        	Automobile automobileModel = (Automobile) currentReqSession.getAttribute("automobileModel");
        	if (automobileModel == null){
    	%>		<p>Failure to display Model details</p> <%
    			return;
    		}
        %>
        
        <Form>
        <TABLE border="1">
        <TR><TD> <% out.println(automobileModel.getName()); %></TD>
        	<TD>Basic Price</TD>
        	<TD> <% out.println((int)automobileModel.getBasePrice()); %></TD>
        </TR>
        <TR><TD>Model</TD>
        	<TD><% out.println(automobileModel.getModel()); %></TD>
        	<TD></TD>
		</TR>
		<TR><TD>Make</TD>
			<TD><% out.println(automobileModel.getMake()); %></TD>
			<TD></TD>
		</TR>
		<%
			String[] optSetsNames = automobileModel.getOptionSetNames();
			for (int i=0; i < optSetsNames.length; i++){
	        	out.println("<TR><TD>" + optSetsNames[i] +"</TD>");
	        	out.println("<TD>" + automobileModel.getOptionChoice(optSetsNames[i])+ "</TD>");
	        	out.println("<TD>" + automobileModel.getOptionChoicePrice(optSetsNames[i])+ "</TD>");
	        	out.println("</TR>");
	        }
		%>
		
		<TR><TD>Total Price</TD>
			<TD></TD>
			<TD><% out.println(automobileModel.calculateTotalPrice());%></TD>
		</TR>
        </TABLE>
        </Form>
        
   </BODY>
</HTML>