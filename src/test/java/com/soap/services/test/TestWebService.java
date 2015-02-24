package com.soap.services.test;

import com.soap.services.SoapWSClient;

public class TestWebService {

	public static void main(String[] args) throws Exception {

		SoapWSClient client = new SoapWSClient();
		final String uri = "http://www.webservicex.com/CurrencyConvertor.asmx";

		final String soapRequest = "<web:ConversionRate xmlns:web=\"http://www.webserviceX.NET/\">"
				+ "<web:FromCurrency>USD</web:FromCurrency>"
				+ "<web:ToCurrency>INR</web:ToCurrency>"
				+ "</web:ConversionRate>";

		String xmlFile = "src\\main\\resources\\request.xml";
		// String response = client.customSendAndReceive(uri, soapRequest);
		/*
		 * String response = client.customSendAndReceiveXML(uri, xmlPath);
		 * System.out.println("Response" + response);
		 */
		

		/*client.responseExtractor1(uri, xmlPath);
		
		System.out.println("response ");
*/
		double response = client.responseExtractorXML(uri, xmlFile);
		System.out.println("response "+response);
		
	}
}
