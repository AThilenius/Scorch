package platform;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Alec on 1/29/15.
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread aThread, Throwable aThrowable) {
        String stackTrace = getStackTrace(aThrowable);
        JOptionPane.showMessageDialog(
                new JFrame(), "Flame threw an unhandled exception. Please send this to Alec@Thilenius.com\n" + aThrowable.toString() + ".\n== Stack Trace ==\n" + stackTrace,
                "Flame Error", JOptionPane.ERROR_MESSAGE
        );
    }

    private String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
