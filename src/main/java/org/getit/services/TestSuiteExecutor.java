package org.getit.services;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.ArrayList;

public class TestSuiteExecutor implements ITestSuiteExecutor{
    private String packageName = "test.";

    @Override
    public void textExecutor(ArrayList<String> classList) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();

        for (String clsName : classList
        ) {
            try {
                classes.add(Class.forName(packageName + clsName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(classes.stream().toArray(Class<?>[]::new)
        );
        resultReport(result);
    }

    private void resultReport(Result result) {
        System.out.println("Finished. Result: Failures: " +
                result.getFailureCount() + ". Ignored: " +
                result.getIgnoreCount() + ". Tests run: " +
                result.getRunCount() + ". Time: " +
                result.getRunTime() + "ms.");
    }
}
