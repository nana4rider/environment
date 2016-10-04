package net.nana4.tstrans.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.nana4.tstrans.bean.Translate;
import net.nana4.tstrans.bean.TranslateProperty;
import net.nana4.tstrans.reader.TransTsvReader;

public class Util {
    public static Translate createTranslate(String lang, List<TranslateProperty> properties) {
        Translate trans = new Translate();
        trans.setLanguage(lang);
        Map<String, String> map = new LinkedHashMap<>();
        properties.stream().forEach(prop -> map.put(prop.getId(), prop.getText()));
        trans.setProperties(map);
        return trans;
    }

    public static TransTsvReader newTransTsvReader(String lang, String type) throws IOException {
        Path path = Paths.get("tsv", lang, type.toUpperCase() + ".tsv");
        InputStream stream = Files.newInputStream(path);

        return new TransTsvReader(stream);
    }

    public static String getProperty(String key) {
        return ResourceBundle.getBundle("system").getString(key);
    }

    public static String[] getPropertyAsArray(String key) {
        return getProperty(key).split(",");
    }
}
