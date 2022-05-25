package com.example.electricbillsystem.validation;

import com.example.electricbillsystem.model.Employee;
import org.springframework.core.convert.converter.Converter;

public class CustomConverter implements Converter<String, Employee> {
    @Override
    public Employee convert(String source) {
        String[] data = source.split(",");
        return new Employee(Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                data[4],
                Double.parseDouble(data[5]));
    }
}
