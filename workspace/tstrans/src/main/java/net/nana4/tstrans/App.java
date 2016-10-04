package net.nana4.tstrans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.nana4.tstrans.bean.Translate;
import net.nana4.tstrans.bean.TranslateProperty;
import net.nana4.tstrans.reader.TransTsvReader;
import net.nana4.tstrans.util.Util;
import net.nana4.tstrans.writer.TransJsonWriter;

public class App {
    public static void main(String[] args) throws Exception
    {
        new App().execute(Util.getPropertyAsArray("tsv.languages"), Util.getPropertyAsArray("tsv.types"));
    }

    private static Map<String, Predicate<TranslateProperty>> filters;

    private static Predicate<TranslateProperty> nullFilter = new Predicate<TranslateProperty>() {
        public boolean test(TranslateProperty prop) {
            return true;
        }
    };

    static {
        filters = new HashMap<>();

        // item絞り込み
        filters.put("item", new Predicate<TranslateProperty>() {
            public boolean test(TranslateProperty prop) {
                String t = prop.getText();
                return !t.contains("。") && !t.contains("、");
            }
        });
    }

    private static Predicate<TranslateProperty> getFilter(String type) {
        return filters.containsKey(type) ? filters.get(type) : nullFilter;
    }

    public void execute(String[] languages, String[] types) throws IOException {
        for (String type : types) {
            try (TransJsonWriter writer = newTransJsonWriter(type)) {
                for (String lang : languages) {
                    writer.addTranslate(getTranslate(lang, type));
                }
                writer.write("callback." + type);
            }
        }
    }

    private TransJsonWriter newTransJsonWriter(String type) throws IOException {
        return new TransJsonWriter(Files.newOutputStream(Paths.get("json", type.toLowerCase() + ".json")));
    }

    private Translate getTranslate(String lang, String type) throws IOException {
        try (TransTsvReader reader = Util.newTransTsvReader(lang, type)) {
            Predicate<TranslateProperty> filter = getFilter(type);
            return Util.createTranslate(lang, reader.read().stream()
                    .filter(filter).collect(Collectors.toList()));
        }
    }
}
