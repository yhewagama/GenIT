package org.getit.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.getit.model.TestCase;
import org.getit.model.TestSuite;

public class TestGenerator {

    static String packageName = "hackathon";

    public String generateTests (TestSuite testSuite)
        throws IOException {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("vtemplates/class.vm");

        VelocityContext context = new VelocityContext();

        String className = testSuite.getPath().replaceAll("[^a-zA-Z0-9]", "");

        context.put("className", className);
        context.put("packageName", packageName);
        context.put("path", testSuite.getPath().replaceAll("[^a-zA-Z0-9]", ""));
        context.put("testCases", testSuite.getTestCases());

        String outputFile = "src/test/java/hackathon/" + className + ".java";

        Files.createDirectories(Paths.get("src/test/java/hackathon"));

        Writer fileWriter = new FileWriter(new File(outputFile));
        Velocity.mergeTemplate("vtemplates/class.vm", "UTF-8", context, fileWriter);
        fileWriter.flush();
        fileWriter.close();

        return className;
    }
}
