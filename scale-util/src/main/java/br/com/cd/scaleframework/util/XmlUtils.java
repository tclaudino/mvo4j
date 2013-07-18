package br.com.cd.scaleframework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;

public class XmlUtils {

	public static Object fromXml(String url) {
		return fromXml(url, false);
	}

	public static Object fromXml(String url, boolean isRemoteFile) {
		if (isRemoteFile) {
			XStream xstream = new XStream();
			try {
				return (Object) xstream.fromXML(FileUtils.readFromURL(url));
			} catch (MalformedURLException ex) {
				return null;
			} catch (IOException ex) {
				return null;
			}
		} else {
			try {
				return fromXml(FileUtils.getString(url));
			} catch (FileNotFoundException ex) {
				return null;
			} catch (IOException ex) {
				return null;
			}
		}
	}

	public static Document toXml(Object object)
			throws ParserConfigurationException, SAXException, IOException {
		XStream xstream = new XStream();
		String xml = xstream.toXML(object);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);

		DocumentBuilder docBuilder;
		docBuilder = dbf.newDocumentBuilder();
		System.out.println("xml: " + xml);

		FileUtils.save(object.toString() + ".xml", xml);

		return docBuilder.parse(xml);
	}
}
