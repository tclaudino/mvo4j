/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springview.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import br.com.cd.scaleframework.util.ParserUtils;



import junit.framework.TestCase;

public class ParserUtilsTest extends TestCase {

	public ParserUtilsTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test of parseString method, of class ParserUtils.
	 */
	public void testParseString_Object() {
		System.out.println("parseString");

		assertEquals("123456", ParserUtils.parseString("123456"));

		assertEquals("", ParserUtils.parseString(null));

		assertEquals("34.0", ParserUtils.parseString(34f));

		assertEquals("467.32", ParserUtils.parseString(467.32d));

	}

	/**
	 * Test of parseString method, of class ParserUtils.
	 */
	public void testParseString_Object_String() {
		System.out.println("parseString");

		assertEquals("AbcF09lkX�~pooasn@3445",
				ParserUtils.parseString(null, "AbcF09lkX�~pooasn@3445"));

		assertEquals("AbcF09lkX�~pooasn@3445",
				ParserUtils.parseString("", "AbcF09lkX�~pooasn@3445"));
	}

	/**
	 * Test of parseString method, of class ParserUtils.
	 */
	public void testParseString_Date_DateFormat() {
		System.out.println("parseString");

		Date now = new Date();
		DateFormat dateFormat = DateFormat.getDateInstance(
				DateFormat.DATE_FIELD, new Locale("pt_BR"));

		assertEquals(dateFormat.format(now),
				ParserUtils.parseString(now, dateFormat));

		assertEquals(DateFormat.getInstance().format(now),
				ParserUtils.parseString(now));
	}

	/**
	 * Test of parseInt method, of class ParserUtils.
	 */
	public void testParseInt_Object() {
		System.out.println("parseInt");

		assertEquals(0, ParserUtils.parseInt("AbcF09lkX�~pooasn@3445"));

		assertEquals(0, ParserUtils.parseInt(null));

		assertEquals(32654, ParserUtils.parseInt("32654"));
	}

	/**
	 * Test of parseInt method, of class ParserUtils.
	 */
	public void testParseInt_Object_int() {
		System.out.println("parseInt");

		assertEquals(6524,
				ParserUtils.parseInt("AbcF09lkX�~pooasn@3445", 6524));

		assertEquals(8215, ParserUtils.parseInt(null, 8215));

		assertEquals(2165, ParserUtils.parseInt("0002165"));

		assertEquals(62, ParserUtils.parseInt(new Double(62d)));

		assertEquals(60, ParserUtils.parseInt(new Float(60f)));

		assertEquals(325, ParserUtils.parseInt(325f));

		assertEquals(2347, ParserUtils.parseInt(2347d));
	}

	/**
	 * Test of parseBoolean method, of class ParserUtils.
	 */
	public void testParseBoolean_Object() {
		System.out.println("parseBoolean");

		assertEquals(false, ParserUtils.parseBoolean("adfv"));

		assertEquals(false, ParserUtils.parseBoolean(null));

		assertEquals(true, ParserUtils.parseBoolean("True"));

		assertEquals(true, ParserUtils.parseBoolean("1"));

		assertEquals(false, ParserUtils.parseBoolean("0"));
	}

	/**
	 * Test of parseBoolean method, of class ParserUtils.
	 */
	public void testParseBoolean_Object_boolean() {
		System.out.println("parseBoolean");

		assertEquals(true, ParserUtils.parseBoolean("adfv", true));

		assertEquals(false, ParserUtils.parseBoolean(null, false));

		assertEquals(true, ParserUtils.parseBoolean("True", false));

		assertEquals(true, ParserUtils.parseBoolean("1", false));

		assertEquals(true, ParserUtils.parseBoolean("d0", true));
	}

	/**
	 * Test of parseDouble method, of class ParserUtils.
	 */
	public void testParseDouble_Object() {
		System.out.println("parseDouble");

		assertEquals(0D, ParserUtils.parseDouble("efg"));

		assertEquals(0D, ParserUtils.parseDouble(null));

		assertEquals(325.23d, ParserUtils.parseDouble(325.23f));

		assertEquals(13.0d, ParserUtils.parseDouble(13));

		assertEquals(62d, ParserUtils.parseDouble(new Integer(62)));

		assertEquals(60d, ParserUtils.parseDouble(new Float(60f)));
	}

	/**
	 * Test of parseDouble method, of class ParserUtils.
	 */
	public void testParseDouble_Object_double() {
		System.out.println("parseDouble");

		assertEquals(6524d,
				ParserUtils.parseDouble("AbcF09lkX�~pooasn@3445", 6524d));

		assertEquals(8215d, ParserUtils.parseDouble(null, 8215d));

	}

	/**
	 * Test of parseFloat method, of class ParserUtils.
	 */
	public void testParseFloat() {
		System.out.println("parseFloat");

		assertEquals(0f, ParserUtils.parseFloat("efg"));

		assertEquals(0f, ParserUtils.parseFloat(null));

		assertEquals(325.23f, ParserUtils.parseFloat(325.23d));

		assertEquals(13.0f, ParserUtils.parseFloat(13));

		assertEquals(62f, ParserUtils.parseFloat(new Integer(62)));

		assertEquals(60f, ParserUtils.parseFloat(new Double(60d)));
	}

	/**
	 * Test of parseFloat method, of class ParserUtils.
	 */
	public void testParseFloat_Object_float() {
		System.out.println("parseFloat");

		assertEquals(6524f,
				ParserUtils.parseFloat("AbcF09lkX�~pooasn@3445", 6524f));

		assertEquals(8215f, ParserUtils.parseFloat(null, 8215f));
	}

	/**
	 * Test of parseDate method, of class ParserUtils.
	 */
	public void testParseDate_Object() throws ParseException {
		System.out.println("parseDate");

		DateFormat dateFormat = DateFormat.getDateInstance(
				DateFormat.DATE_FIELD, new Locale("pt_BR"));
		Date now = dateFormat.parse(dateFormat.format(new Date()));

		assertEquals(now,
				ParserUtils.parseDate(dateFormat.format(now), dateFormat));

		assertEquals(dateFormat.parse(dateFormat.format(now)),
				ParserUtils.parseDate(dateFormat.format(now), dateFormat));
	}

	/**
	 * Test of parseDate method, of class ParserUtils.
	 */
	public void testParseDate_Object_DateFormat() {
		System.out.println("parseDate");

		Date now = new Date();
		assertEquals(now, ParserUtils.parseDate(326, now));
	}

	/**
	 * Test of parseDate method, of class ParserUtils.
	 */
	public void testParseDate_Object_Date() {
		System.out.println("parseDate");

		Date now = new Date();

		assertEquals(now, ParserUtils.parseDate(326, now));
	}

	/**
	 * Test of parseDate method, of class ParserUtils.
	 */
	public void testParseDate_3args() {
		System.out.println("parseDate");

		Date now = new Date();
		DateFormat dateFormat = DateFormat.getDateInstance(
				DateFormat.DATE_FIELD, new Locale("pt_BR"));

		assertEquals(now, ParserUtils.parseDate(326, dateFormat, now));
	}

	/**
	 * Test of arrayToList method, of class ParserUtils.
	 */
	public void testArrayToList() {
		System.out.println("arrayToList");

		// TODO review the generated test code and remove the default call to
		// fail.
	}

	/**
	 * Test of arrayToSet method, of class ParserUtils.
	 */
	public void testArrayToSet() {
		System.out.println("arrayToSet");

		// TODO review the generated test code and remove the default call to
		// fail.
	}

	/**
	 * Test of setToList method, of class ParserUtils.
	 */
	public void testSetToList() {
		System.out.println("setToList");

		// TODO review the generated test code and remove the default call to
		// fail.
	}
}
