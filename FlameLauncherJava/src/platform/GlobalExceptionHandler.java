package platform;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Alec on 1/29/15.
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static void errorOut(Throwable aThrowable) {
        String stackTrace = getStackTrace(aThrowable);
        JOptionPane.showMessageDialog(
                new JFrame(), "Flame threw a handled exception. If you are unable to resolve this issue, please send a screenshot of this to Alec@Thilenius.com\n" + aThrowable.toString() + ".\n== Stack Trace ==\n" + stackTrace,
                "Flame Error", JOptionPane.ERROR_MESSAGE
        );
        System.exit(0);
    }

    @Override
    public void uncaughtException(Thread aThread, Throwable aThrowable) {
        String stackTrace = getStackTrace(aThrowable);
        JOptionPane.showMessageDialog(
                new JFrame(), "Flame threw an unhandled exception. Please send this to Alec@Thilenius.com\n" + aThrowable.toString() + ".\n== Stack Trace ==\n" + stackTrace,
                "Flame Error", JOptionPane.ERROR_MESSAGE
        );
        System.exit(0);
    }

    private static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
