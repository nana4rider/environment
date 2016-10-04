package net.nana4.tstrans.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.arnx.jsonic.JSON;
import net.nana4.tstrans.bean.Translate;

public class TransJsonWriter implements AutoCloseable {
    private OutputStream out;
    private Translate baseTranslate;
    List<Translate> extendTranslates;

    public TransJsonWriter(OutputStream out) {
        this.out = out;
        this.baseTranslate = null;
        this.extendTranslates = new ArrayList<>();
    }

    public void addTranslate(Translate trans) {
        if (baseTranslate == null) {
            baseTranslate = trans;
        } else {
            extendTranslates.add(trans);
        }
    }

    public void write(String callback) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        String baseLang = baseTranslate.getLanguage();

        baseTranslate.getProperties().entrySet().stream().forEach(new Consumer<Map.Entry<String, String>>() {
            public void accept(Map.Entry<String, String> property) {
                Map<String, Object> data = new LinkedHashMap<>();
                String id = property.getKey();
                data.put("id", id);

                Map<String, String> textMap = new LinkedHashMap<>();
                textMap.put(baseLang, property.getValue());
                data.put("text", textMap);

                for (Translate extrans : extendTranslates) {
                    String text = extrans.getProperty(id);
                    textMap.put(extrans.getLanguage(), text);
                }

                result.add(data);
            }
        });

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.write(callback + "(");
            writer.write(JSON.encode(result, true));
            writer.write(")");
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
