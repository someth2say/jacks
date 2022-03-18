package org.someth2say;

import org.someth2say.formats.Format;

import java.io.InputStream;

public class ConvertUtils {
    public static InputStream convertJsonToYaml(final InputStream json) throws JacksException {
        Object obj = Format.JSON.getMapper().inputStreamToObject(json);
        return Format.YAML.getMapper().objectToInputStream(obj);
    }

    public static InputStream convertYamlToJson(final InputStream yaml) throws JacksException {
        Object obj = Format.YAML.getMapper().inputStreamToObject(yaml);
        return Format.JSON.getMapper().objectToInputStream(obj);
    }

}