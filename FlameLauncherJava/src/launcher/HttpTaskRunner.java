package launcher;

import platform.GlobalExceptionHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 12/27/14.
 */
public class HttpTaskRunner implements Runnable {

    private List<HttpFileDownloadTask> m_tasks;
    private JProgressBar m_progressBar;
    private int m_compleatedCount;
    private int m_nextIndex;

    public HttpTaskRunner(List<HttpFileDownloadTask> tasks, JProgressBar progressBar) {
        m_tasks = tasks;
        m_progressBar = progressBar;
    }

    public void run() {
        if (m_tasks == null || m_tasks.size() == 0) {
            System.out.println("All files up to date.");
            return;
        }

        System.out.println("Downloading " + m_tasks.size() + " files...");
        List<Thread> threads = new ArrayList<Thread>();
        print();

        for (int i = 0; i < 32; i++) {
            Thread thread = new Thread() {
                public void run() {
                    while (true) {
                        HttpFileDownloadTask task = getNextTask();
                        if (task == null) {
                            return;
                        }
                        task.run();
                        print();
                        if (m_progressBar != null) {
                            m_progressBar.setValue((int) ((float) m_compleatedCount / (float) m_tasks.size() * 100.0f));
                        }
                    }
                }
            };
            threads.add(thread);
            thread.start();
        }

        try {
            for (final Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        System.out.println();
        System.out.println("All files up to date.");
    }

    private synchronized HttpFileDownloadTask getNextTask() {
        if (m_nextIndex >= m_tasks.size()) {
            return null;
        }
        HttpFileDownloadTask task = m_tasks.get(m_nextIndex);
        m_nextIndex++;
        return task;
    }

    private synchronized void print() {
        float fraction = (float)m_compleatedCount / (float)m_tasks.size();
        int hashCount = (int)(fraction * 50.0f);
        int spaceCount = 50 - hashCount;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r[");

        for (int i = 0; i < hashCount; i++) {
            stringBuilder.append('#');
        }
        for (int i = 0; i < spaceCount; i++) {
            stringBuilder.append(' ');
        }

        stringBuilder.append("] " + m_compleatedCount + " / " + m_tasks.size());
        System.out.print(stringBuilder.toString());

        m_compleatedCount++;
    }
}
