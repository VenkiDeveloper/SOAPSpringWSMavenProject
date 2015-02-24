package com.soap.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.integration.ws.SimpleWebServiceOutboundGateway;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

	public void responseExtractor1(String uri, String xmlFile)
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
