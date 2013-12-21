package org.agoncal.application.petstore.web;

import org.agoncal.application.petstore.util.Loggable;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@RequestScoped
@Loggable
@CatchException
public class DebugController extends Controller {

    // ======================================
    // =             Attributes             =
    // ======================================


    // ======================================
    // =              Public Methods        =
    // ======================================

    public String formatValue(String name, Object rawValue) {
        String value;
        try {
            if (name.indexOf("classpath") >= 0 ||
                    name.indexOf("java.library.path") >= 0 ||
                    name.indexOf("ws.ext.dirs") >= 0 ||
                    name.indexOf("java.class.path") >= 0 ||
                    name.indexOf("sun.boot.class.path") >= 0) {
                value = "<pre>";

                String classpath = rawValue == null ? "" : rawValue.toString();
                String[] arrClasspath = classpath.split(System.getProperty("path.separator"));
                for (int i = 0; i < arrClasspath.length; i++) {
                    value += arrClasspath[i] + System.getProperty("path.separator") + "\n";
                }
                value += "</pre>";
            } else if (
                    (name.toLowerCase().indexOf("password") >= 0) ||
                            (name.toLowerCase().indexOf("secret") >= 0)) {
                value = "***";
            } else {
                value = rawValue == null ? "" : rawValue.toString();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            value = "Exception formatting <code>" + rawValue.getClass() + "</code><br/><pre>" + sw + "</pre>";
        }
        return value;
    }

//    public void getHeaders(){
//        Enumeration<?> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()) {
//            String header = (String) headers.nextElement();
//            out.println("<tr><td>" + header + "</td><td>" + request.getHeader(header) + "</td></tr>");
//        }
//
//    }
//
// public void getCookies(){
//     Cookie[] cookies = request.getCookies();
//     if (null != cookies) {
//         for (Cookie cookie : cookies) {
//             out.println("<tr><td>" + cookie.getName() + "</td><td>" + cookie.getValue() + "</td></tr>");
//         }
//     }

    // }
//    public void threadStack() {
//        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : stackTraceElements) {
//            out.println(stackTraceElement.getClassName() + "." +
//                    stackTraceElement.getMethodName() +
//                    "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")");
//        }
//    }

//    public void attributes(){
//        for (String attribute : Collections.list((Enumeration<String>) session.getAttributeNames())) {
//            out.println("<tr><td>" + attribute + "</td><td>" + session.getAttribute(attribute) + "</td></tr>");
//        }
//    }

//    public void getattr(){
//        List<String> applicationAttributeNames = Collections.list((Enumeration<String>) application.getAttributeNames());
//        Collections.sort(applicationAttributeNames);
//        for (String attribute : applicationAttributeNames) {
//            Object rawValue = application.getAttribute(attribute);
//            String value = formatValue(attribute, rawValue);
//            out.println("<tr><td valign='top'>" + attribute + "</td><td>" + value + "</td></tr>");
//        }
//    }
//    public void initParam(){
//        List<String> applicationInitParameterNames = Collections.list((Enumeration<String>) application.getInitParameterNames());
//        Collections.sort(applicationInitParameterNames);
//        for (String parameter : applicationInitParameterNames) {
//            out.println("<tr><td>" + parameter + "</td><td>" + application.getInitParameter(parameter) + "</td></tr>");
//        }
//    }

//    public void initContext(){
//        try {
//            InitialContext initialContext = new InitialContext(new Properties());
//            initialContext.getNameInNamespace()
//        } catch (Throwable e) {
//            out.write("<pre>");
//            PrintWriter printWriter = new PrintWriter(out);
//            e.printStackTrace(printWriter);
//            out.write("</pre>");
//            printWriter.flush();
//        }
//        }
//    public void system(){
//        List<String> systemPropertyNames = new ArrayList<String>(System.getProperties().stringPropertyNames());
//        Collections.sort(systemPropertyNames);
//        for (String propertyName : systemPropertyNames) {
//
//            String propertyValue = System.getProperty(propertyName);
//            String value = formatValue(propertyName, propertyValue);
//            out.println("<tr><td valign='top'>" + propertyName + "</td><td>" + value + "</td></tr>");
//        }

    //    public void envVar(){
//        List<String> environmentVariableNames = new ArrayList<String>(System.getenv().keySet());
//        Collections.sort(environmentVariableNames);
//        for (String environmentVariableName : environmentVariableNames) {
//
//            String environmentVariableValue = System.getenv(environmentVariableName);
//            String value = formatValue(environmentVariableName, environmentVariableValue);
//            out.println("<tr><td valign='top'>" + environmentVariableName + "</td><td>" + value + "</td></tr>");
//        }
    public String getWorkingDirectory() {
        return new java.io.File(".").getAbsolutePath();
    }

//    public void inetaddress() throws UnknownHostException {
//        InetAddress localhost = InetAddress.getLocalHost();
//        localhost.getHostName() + ", " + localhost.getCanonicalHostName() + ", " + localhost.getHostAddress();
//        for (InetAddress address : InetAddress.getAllByName(null)) {
//            out.println(address.getHostName() + ", " + address.getCanonicalHostName() + ", " + address.getHostAddress() + "<br/>");
//        }

    public String getTotalMemory() {
        return String.valueOf(Runtime.getRuntime().totalMemory());
    }

    public String getFreeMemory(){
        return String.valueOf(Runtime.getRuntime().freeMemory());
    }
}