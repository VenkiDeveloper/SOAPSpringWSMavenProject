package com.soap.services.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.soap.services.ConversionRateResponse;

public class UnMExample {
	public static void main(String[] args) throws FileNotFoundException {

		try {

			JAXBContext jaxbContext = JAXBContext
					.newInstance(ConversionRateResponse.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ConversionRateResponse item = (ConversionRateResponse) unmarshaller
					.unmarshal(new FileInputStream(new File(
							"src\\main\\resources\\resposne.xml")));
			System.out.println("asdasd" + item.getConversionRateResult());

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
