package org.someth2say.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.someth2say.JacksException;

import java.io.*;
import java.util.stream.Collectors;

public class Txt extends AbstractFormatMapper<ObjectMapper> implements FormatMapper {

    public Txt() {
        super(null, Format.TXT);
    }

    public final InputStream objectToInputStream(final Object obj) throws JacksException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        String stringArray;
        if (obj instanceof String) {
            stringArray = (String) obj;
        } else if (obj instanceof JSONArray) {
            stringArray = ((JSONArray) obj).stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new JacksException("Only Array objects can be serialized to plain format (type is " + obj.getClass().getSimpleName() + ")");
        }

        out.writeBytes(stringArray.getBytes());
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T inputStreamToObject(final InputStream plain, final Class<T> valueType) throws JacksException {

        if (valueType.isAssignableFrom(JSONArray.class)) {
            JSONArray result = new JSONArray();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(plain))) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    result.add(line);
                }
            } catch (final IOException e) {
                throw new JacksException("Cannot read plan input", e);
            }

            return (T) result;

        } else {
            throw new JacksException("Only Array objects can be deserialized to plain format.");
        }
    }

    @Override
    public <T> T stringToObject(final String plain, final Class<T> valueType) throws JacksException {
        return inputStreamToObject(new ByteArrayInputStream(plain.getBytes()), valueType);
    }

}