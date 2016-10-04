package net.nana4.tstrans.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultJson {
    private String id;

    private Map<String, String> text;

    public ResultJson(String id) {
        this.id = id;
        this.text = new LinkedHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getText() {
        return text;
    }

    public void addText(String lang, String text) {
        this.text.put(lang, text);
    }
}
