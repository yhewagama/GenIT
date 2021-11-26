package org.getit.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.getit.model.TestCase;

public class TestGenerator {
    public boolean generateTests (String baseUrl, String path, String method, String responseJsonFile, String requestBody) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("vtemplates/class.vm");

        VelocityContext context = new VelocityContext();

        String className = path.replaceAll("[^a-zA-Z0-9]", "");

        TestCase testCase = new TestCase();
        testCase.setMethod(method);
        testCase.setUrl(baseUrl + path);
        testCase.setResponse_path(responseJsonFile);
        testCase.setRequest_body(requestBody);

        context.put("className", className);

        context.put("path", path);
        context.put("testCases", testCase);

        return true;
    }
}
