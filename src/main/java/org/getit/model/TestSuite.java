package org.getit.model;

import java.util.ArrayList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class TestSuite {
    private String path;
    private ArrayList<TestCase> TestCases;
}
