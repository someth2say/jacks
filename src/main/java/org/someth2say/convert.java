package org.someth2say;

import java.io.InputStream;
import org.someth2say.formats.*;

public class convert {
	public static final InputStream convertJsonToYaml(final InputStream json) throws yqException {
		Object obj = Format.JSON.mapper.inputStreamToObject(json);
			return Format.YAML.mapper.objectToInputStream(obj);

	}

	public static final InputStream convertYamlToJson(final InputStream yaml) throws yqException {
		Object obj = Format.YAML.mapper.inputStreamToObject(yaml);
		return Format.JSON.mapper.objectToInputStream(obj);
	}

}