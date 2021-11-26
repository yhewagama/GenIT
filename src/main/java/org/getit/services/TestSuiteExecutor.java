package org.getit.services;

import org.getit.services.mock.petfindByStatusMock;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TestSuiteExecutor implements ITestSuiteExecutor{
    private String packageName = "org.getit.services.mock.";

    @Override
    public void textExecutor(ArrayList<String> classList) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();

        ArrayList<String> classListArr = new ArrayList<String>();
        classListArr.add("petfindByStatusMock");
        classListArr.add("storeinventoryMock");

        for (String clsName : classListArr
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
