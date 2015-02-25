package com.soap.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Node;

public class SoapWSClient {

	private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	// send to an explicit URI
	public String customSendAndReceive(String uri, String soapRequest) {
		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(new StringReader(soapRequest));
		StringResult result = new StringResult();
		webServiceTemplate.sendSourceAndReceiveToResult(uri, source, result);
		return result.toString();

	}

	public String customSendAndReceiveXML(String uri, String xmlFile) {
		File file = new File(xmlFile);
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(in);
		StringResult result = new StringResult();
		webServiceTemplate.sendSourceAndReceiveToResult(uri, source,/*
																	 * new
																	 * WebServiceMessageCallback
																	 * () {
																	 * 
																	 * public
																	 * void
																	 * doWithMessage
																	 * (
																	 * WebServiceMessage
																	 * message)
																	 * throws
																	 * IOException
																	 * ,
																	 * TransformerException
																	 * {
																	 * ((SoapMessage
																	 * )
																	 * message).
																	 * setSoapAction
																	 * (
																	 * "http://www.webserviceX.NET/ConversionRate"
																	 * );
																	 * 
																	 * } }
																	 */result);
		return result.toString();
	}

	public double responseExtractorXML(String uri, String xmlFile)
			throws Exception {

		webServiceTemplate.setDefaultUri(uri);
		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(new FileInputStream(new File(
				xmlFile)));
		DOMSource domSource = webServiceTemplate.sendSourceAndReceive(source,
				new SourceExtractor<DOMSource>() {

					public DOMSource extractData(Source source)
							throws IOException, TransformerException {
						if (source instanceof DOMSource) {
							return (DOMSource) source;
						}
						DOMResult result = new DOMResult();
						TransformerFactory.newInstance().newTransformer()
								.transform(source, result);
						return new DOMSource(result.getNode());
					}

				});

		return getResponse(domSource);

	}

	public double responseExtractor(String uri, String soapRequest)
			throws Exception {

		webServiceTemplate.setDefaultUri(uri);
		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(new StringReader(soapRequest));
		DOMSource domSource = webServiceTemplate.sendSourceAndReceive(source,
				new SourceExtractor<DOMSource>() {

					public DOMSource extractData(Source source)
							throws IOException, TransformerException {
						if (source instanceof DOMSource) {
							return (DOMSource) source;
						}
						DOMResult result = new DOMResult();
						TransformerFactory.newInstance().newTransformer()
								.transform(source, result);
						return new DOMSource(result.getNode());
					}

				});
		return getResponse(domSource);

	}

	public void responseExtractor1(String uri, String xmlFile) throws Exception {

		webServiceTemplate.setDefaultUri(uri);
		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(new FileInputStream(new File(
				xmlFile)));
		DOMSource domSource = webServiceTemplate.sendSourceAndReceive(source,
				new SourceExtractor<DOMSource>() {

					public DOMSource extractData(Source source)
							throws IOException, TransformerException {
						if (source instanceof DOMSource) {
							return (DOMSource) source;
						}
						DOMResult result = new DOMResult();
						TransformerFactory.newInstance().newTransformer()
								.transform(source, result);
						return new DOMSource(result.getNode());
					}

				});
		String path = "C:\\Venky\\ATMECS Projects\\SOAPSpringWSProject\\src\\main\\resources\\resposne.xml";
		writeToFile(domSource, path);

	}

	/*
	 * <SOAP-ENV:Header> <wsa:MessageID>urn:abcdef1234</wsa:MessageID>
	 * </SOAP-ENV:Header>
	 */

	public void addRequestHeader(String uri, String xmlFile) throws Exception {

		webServiceTemplate.setDefaultUri(uri);
		try {
			webServiceTemplate
					.setMessageFactory(new SaajSoapMessageFactory(
							MessageFactory
									.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		StreamSource source = new StreamSource(new FileInputStream(new File(
				xmlFile)));
		DOMSource domSource = webServiceTemplate.sendSourceAndReceive(source,
				new WebServiceMessageCallback() {

					public void doWithMessage(WebServiceMessage message)
							throws IOException, TransformerException {

						SaajSoapMessage soapMessage = (SaajSoapMessage) message;
						SoapHeaderElement messageId = soapMessage
								.getSoapHeader()
								.addHeaderElement(
										new QName(
												"http://www.w3.org/2005/08/addressing",
												"MessageID", "wsa"));
						messageId.setText("urn:abcdef1234");

					}
				}, new SourceExtractor<DOMSource>() {

					public DOMSource extractData(Source source)
							throws IOException, TransformerException {
						if (source instanceof DOMSource) {
							return (DOMSource) source;
						}
						DOMResult result = new DOMResult();
						TransformerFactory.newInstance().newTransformer()
								.transform(source, result);
						return new DOMSource(result.getNode());
					}

				});
		String path = "C:\\Venky\\ATMECS Projects\\SOAPSpringWSProject\\src\\main\\resources\\resposne.xml";
		writeToFile(domSource, path);

	}

	public double getResponse(DOMSource domSource) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(ConversionRateResponse.class);
		Unmarshaller u = jc.createUnmarshaller();
		Node node = (Node) domSource.getNode();
		JAXBElement<ConversionRateResponse> element = (JAXBElement<ConversionRateResponse>) u
				.unmarshal(node, ConversionRateResponse.class);
		ConversionRateResponse st = element.getValue();
		return st.getConversionRateResult();
	}

	public void writeToFile(DOMSource domSource, String fileNameToWrite)
			throws Exception {
		// DOMSource domSource = new DOMSource(doc);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				fileNameToWrite)));
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, streamResult);
	}
}
