package net.nana4.tstrans.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import net.nana4.tstrans.bean.TranslateProperty;

public class TransTsvReader implements AutoCloseable {
    private InputStreamReader reader;

    public TransTsvReader(InputStream in) {
        this.reader = new InputStreamReader(in, StandardCharsets.UTF_8);
    }

    public List<TranslateProperty> read() throws IOException {
        CSVReader csvReader = new CSVReader(reader, '\t');

        ColumnPositionMappingStrategy<TranslateProperty> mapper = new ColumnPositionMappingStrategy<TranslateProperty>();
        mapper.setType(TranslateProperty.class);
        mapper.setColumnMapping(new String[] {"id", "text"});

        return new CsvToBean<TranslateProperty>().parse(mapper, csvReader);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
