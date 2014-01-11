package ixcode.platform.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ixcode.platform.io.IoStreamHandling;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

import static ixcode.platform.io.IoStreamHandling.closeQuietly;

public class XmlParser {

    private static final Logger log = LoggerFactory.getLogger(XmlParser.class);
    private String xml;

    public XmlParser parse(String xml) {
        this.xml = xml;
        return this;
    }

    public void using(DefaultHandler handler) {
        SAXParser parser = newSaxParser();
        InputStream in = null;
        try {
            in = IoStreamHandling.toInputStream(xml, "UTF-8");
            long startParse = System.currentTimeMillis();

            parser.parse(in, handler);

            long stopParse = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug("[timing] saxparser.parse() " + (stopParse - startParse));
            }
        } catch (SAXParseException e) {
            throw new RuntimeException("Could not parse : " + xml, e);
        } catch (Throwable t) {
            throw new RuntimeException("Could not parse input\n" + xml, t);
        } finally {
            closeQuietly(in);
        }
    }

    private static SAXParser newSaxParser() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);

        try {
            return factory.newSAXParser();
        } catch (Throwable t) {
            throw new RuntimeException("Could not create sax parser", t);
        }

    }
}

