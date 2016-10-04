package net.nana4.tstrans.bean;

import java.util.Map;

public class Translate {
    private String language;

    private Map<String, String> properties;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String id) {
        return properties.get(id);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
