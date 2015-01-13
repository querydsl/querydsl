package com.querydsl.core.types;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TermplateInfiniteLoop {

    static String templates[] = createTemplates();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(new Runner());
        executorService.execute(new Runner());
        executorService.execute(new Runner());
        executorService.shutdown();
    }

    private static class Runner implements Runnable {
        public void run() {
            for (int i = 0; i < 100000; i++) {
                TemplateFactory.DEFAULT.create(templates[i % templates.length]);
            }
        }
    }

    /**
     * Generates array of strings: "\0a", "\0\0a", "\0\0\0a" etc. all with the
     * same hashCode
     * 
     * @return
     */
    private static String[] createTemplates() {
        String tab[] = new String[10000];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tab.length; i++) {
            builder.append('\0');
            tab[i] = builder.toString() + 'a';
        }
        return tab;
    }
}
