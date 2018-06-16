package nl.idemi.mulesoft;

import javax.jws.WebService;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.session.DefaultMuleSession;

@WebService
public class SoapWebService implements ISoapWebService, MuleContextAware
{	
	 MuleContext muleContext;
	 
	 public void setMuleContext(MuleContext context)
	 {
		 muleContext = context;
	 }
	 
	public String serviceMethodA(String requestMessage)
	{
		log("serviceMethodA - 1 - muleMessage");
		MuleMessage muleMessage = new DefaultMuleMessage(requestMessage, muleContext);
		muleMessage.setEncoding("UTF-8");
		
		try
		{
			log("serviceMethodA - 2 - muleEvent");
			MuleEvent muleEvent = invokeMuleFlow(muleMessage, muleContext, "serviceMethodA");	
			log("serviceMethodA - 3 - muleFlowResponseMessage");
			MuleMessage muleFlowResponseMessage = muleEvent.getMessage();
			log("serviceMethodA - 4 - Return muleFlowResponseMessage payload as string");
			return muleFlowResponseMessage.getPayload(java.lang.String.class);
		}
		catch (Exception ex)
		{
			return "An error occurred while executing ServiceMethodA: " + ex.getMessage();
		}
		
		/*
		StringBuilder sb = new StringBuilder();
		sb.append("ServiceMethodA called with requestMessage=");
		if (requestMessage == null)
			sb.append("null-reference");
		else
		{
			sb.append("\"");
			sb.append(requestMessage);
			sb.append("\"");
		}
		sb.append(".");

		return sb.toString();
		*/
	}
	

	 @SuppressWarnings("deprecation")
	 public static MuleEvent invokeMuleFlow(MuleMessage muleMessage, MuleContext muleContext, String flowName) throws Exception
	 {
		 log("invokeMuleFlow - 0 - Executing flow \"" + flowName + "\"");

		 log("invokeMuleFlow - 1 - subFlow");
		 MessageProcessor subFlow = muleContext.getRegistry().lookupObject(flowName);
		 if (subFlow != null) // The flow is a sub-flow.
		 {
			 log("invokeMuleFlow - 2 - subFlow muleEvent");
			 MuleEvent muleEvent = new DefaultMuleEvent(muleMessage, MessageExchangePattern.REQUEST_RESPONSE, new DefaultMuleSession());
			 log("invokeMuleFlow - 3 - subFlow muleEvent process");
			 return subFlow.process(muleEvent);			 
			 
		 }
		 else // The flow is a flow.
		 {
			 log("invokeMuleFlow - 4 - flow");
			 Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(flowName);
			 log("invokeMuleFlow - 5 - flow muleEvent");
			 MuleEvent muleEvent = new DefaultMuleEvent(muleMessage, MessageExchangePattern.REQUEST_RESPONSE, flow);
			 log("invokeMuleFlow - 6 - flow muleEvent process");
			 return flow.process(muleEvent);			 
		 }
	 }
	 
	 private static void log(String message)
	 {
		 System.out.println("LOG - " + message);
	 }
}
