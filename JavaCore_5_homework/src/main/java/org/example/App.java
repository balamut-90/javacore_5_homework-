package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.models.Employee;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class App {

    public static void main( String[] args ) {

        String[] columnMapping = {"id", "firstName", "lastName","country", "age"};
        String filename = "data.csv";
        String jsonFilename = "data.json";

        List<Employee> employeeList = parseCVS(columnMapping, filename);
        String json = listToJson(employeeList);
        writeString(json, jsonFilename);
    }

    private static void writeString(String json, String jsonFilename) {
        try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
            fileWriter.write(json);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> employeeList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(employeeList, listType);
    }

    private static List<Employee> parseCVS(String[] columnMapping, String filename) {

        List<Employee> employeeList = null;

        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            employeeList = csv.parse();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return employeeList;
    }
}
