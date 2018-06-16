package nl.idemi.mulesoft;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName = "MuleSoft SOAP WebService")
public interface ISoapWebService
{
	@WebMethod(operationName = "ServiceMethodA")
	String serviceMethodA(String requestMessage);
}
