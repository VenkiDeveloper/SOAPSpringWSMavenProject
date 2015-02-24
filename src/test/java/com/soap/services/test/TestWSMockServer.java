package com.soap.services.test;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;

import com.soap.services.SoapWSClient;

public class TestWSMockServer {

	SoapWSClient wsClient;
	private MockWebServiceClient mockClient;
	
	
	
	private MockWebServiceServer mockWebServiceServer;
	private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
	String uri = "http://www.webservicex.com/CurrencyConvertor.asmx";
	final String soapRequest = "<web:ConversionRate xmlns:web=\"http://www.webserviceX.NET/\">"
			+ "<web:FromCurrency>USD</web:FromCurrency>"
			+ "<web:ToCurrency>INR</web:ToCurrency>" + "</web:ConversionRate>";

	@Before
	public void createServer() throws Exception {
		wsClient = new SoapWSClient();
		mockWebServiceServer = MockWebServiceServer
				.createServer(webServiceTemplate);
	}

	@Test
	public void testWebService() {

		Source requestPayload = new StringSource(
				"<web:ConversionRate xmlns:web=\"http://www.webserviceX.NET/\">"
						+ "<web:FromCurrency>USD</web:FromCurrency>"
						+ "<web:ToCurrency>INR</web:ToCurrency>"
						+ "</web:ConversionRate>");

		Source responsePayload = new StringSource(
				"<ConversionRateResponse xmlns=\"http://www.webserviceX.NET/\">"
						+ "  <ConversionRateResult>?</ConversionRateResult>"
						+ "  </ConversionRateResponse>");

		mockWebServiceServer.expect(payload(requestPayload)).andRespond(
				withPayload(responsePayload));

		String result = wsClient.customSendAndReceive(uri, soapRequest);

		assertTrue(result.contains("62"));

	}

}
