package org.someth2say.format;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.someth2say.JacksException;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Xml extends AbstractFormatMapper<XmlMapper> implements FormatMapper {

    private String rootName = "xml";

    public Xml(final XmlMapper xmlMapper) {
        super(xmlMapper, Format.XML);
    }

    public Xml() {
        this(XmlMapper.builder().addModule(
                new SimpleModule().addDeserializer(Object.class, new ArrayInferringUntypedObjectDeserializer())
        ).build());
    }

    protected ObjectWriter getObjectWriter() {
        return mapper.writerWithDefaultPrettyPrinter().withRootName(rootName);
    }

    @Override
    public <T> T inputStreamToObject(final InputStream xml, final Class<T> valueType) throws JacksException {
        try {
            final JsonParser parser = mapper.createParser(xml);
            return parseValue(valueType, parser);
        } catch (final JsonParseException e) {
            throw new JacksException("Can not parse XML", e);
        } catch (final JsonMappingException e) {
            throw new JacksException("Can not decode XML", e);
        } catch (final IOException e) {
            throw new JacksException("Can not read XML", e);
        }
    }

    @Override
    public <T> T stringToObject(final String xml, final Class<T> valueType) throws JacksException {
        try {
            final JsonParser parser = mapper.createParser(xml);
            return parseValue(valueType, parser);
        } catch (final JsonParseException e) {
            throw new JacksException("Can not parse XML", e);
        } catch (final JsonMappingException e) {
            throw new JacksException("Can not decode XML", e);
        } catch (final IOException e) {
            throw new JacksException("Can not read XML", e);
        }
    }

    private <T> T parseValue(final Class<T> valueType, final JsonParser parser) throws IOException {
        final T value = parser.readValueAs(valueType);

        // Store the root node name in the local writer.
        final XMLStreamReader xmlStreamReader = ((FromXmlParser) parser).getStaxReader();
        this.rootName = xmlStreamReader.getLocalName();

        return value;
    }

}

class ArrayInferringUntypedObjectDeserializer extends UntypedObjectDeserializer {

    @Override
    protected Map<String, Object> mapObject(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        if (t == JsonToken.END_OBJECT) { // empty map, eg {}
            // empty LinkedHashMap might work; but caller may want to modify... so better just give small modifiable.
            return new LinkedHashMap<String, Object>(2);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        do {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            result.put(fieldName, handleMultipleValue(result, fieldName, deserialize(jp, ctxt)));
        } while (jp.nextToken() != JsonToken.END_OBJECT);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Object handleMultipleValue(final Map<String, Object> map, final String key, final Object value) {
        if (!map.containsKey(key)) {
            return value;
        }

        Object originalValue = map.get(key);
        if (originalValue instanceof List) {
            ((List) originalValue).add(value);
            return originalValue;
        } else {
            ArrayList newValue = new ArrayList();
            newValue.add(originalValue);
            newValue.add(value);
            return newValue;
        }
    }

}