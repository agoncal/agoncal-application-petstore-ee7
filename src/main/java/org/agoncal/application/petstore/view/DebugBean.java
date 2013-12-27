package org.agoncal.application.petstore.view;

import org.agoncal.application.petstore.util.Loggable;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@RequestScoped
@Loggable
@CatchException
public class DebugBean extends AbstractBean {

    // ======================================
    // =              Public Methods        =
    // ======================================

    public List<String> getThreadStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<String> elements = new ArrayList<>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            elements.add(stackTraceElement.getClassName() + "." +
                    stackTraceElement.getMethodName() +
                    "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")");
        }
        return elements;
    }

    public String getWorkingDirectory() {
        return new java.io.File(".").getAbsolutePath();
    }

    public String getTotalMemory() {
        return String.valueOf(Runtime.getRuntime().totalMemory());
    }

    public String getFreeMemory() {
        return String.valueOf(Runtime.getRuntime().freeMemory());
    }
}