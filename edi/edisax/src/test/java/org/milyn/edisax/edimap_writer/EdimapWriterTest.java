package org.milyn.edisax.edimap_writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.milyn.edisax.model.EDIConfigDigester;
import org.milyn.edisax.model.internal.Edimap;
import org.milyn.edisax.model.internal.Segment;
import org.milyn.edisax.model.internal.SegmentGroup;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class EdimapWriterTest {

    @Test
    public void test() throws IOException, SAXException {
        test("edimap-01.xml");
    }
    @Test
    public void testWithDefaultValue() throws IOException, SAXException {
        Edimap edimap = EDIConfigDigester.digestConfig(getClass().getResourceAsStream("edimap-01-withDefaultValue.xml"));
        Segment segment = (Segment) edimap.getSegments().getSegments().get(0);
        assertEquals("\"01\"", segment.getFields().get(0).getDefaultValue());
        assertFalse(segment.getFields().get(0).isModifiable());
        assertEquals("\"E1\"", segment.getFields().get(1).getComponents().get(0).getDefaultValue());
        assertFalse(segment.getFields().get(1).getComponents().get(0).isModifiable());
    }

    @Test
    public void testWithDefaultSegmentValue() throws IOException, SAXException {
        Edimap edimap = EDIConfigDigester.digestConfig(getClass().getResourceAsStream("edimap-01-withDefaultSegmentValue.xml"));
        SegmentGroup segmentGroup = edimap.getSegments().getSegments().get(0);
	Segment segment = (Segment) segmentGroup.getSegments().get(0);
        assertEquals("new SegmentGroupHeaderXRH1()", segment.getDefaultValue());
        assertFalse(segment.isModifiable());
    }

    @Test
    public void testWriteDefaultSegment() throws IOException, SAXException {
        test("edimap-01-withDefaultSegmentValue.xml");
    }

    public void test(String edimapfile) throws IOException, SAXException {
        Edimap edimap = EDIConfigDigester.digestConfig(getClass().getResourceAsStream(edimapfile));
        StringWriter result = new StringWriter();

        edimap.write(result);

        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream(edimapfile)), new StringReader(result.toString()));
    }
}
