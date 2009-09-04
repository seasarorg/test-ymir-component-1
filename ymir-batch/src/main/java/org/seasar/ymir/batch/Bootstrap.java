package org.seasar.ymir.batch;

import java.util.ArrayList;
import java.util.List;

public class Bootstrap {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String className = null;
        boolean raw = false;
        List<String> argList = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--raw")) {
                raw = true;
            } else if (className == null) {
                className = arg;
            } else {
                argList.add(arg);
            }
        }
        if (className == null) {
            System.out.println("Usage: Bootstrap [ --raw ] className");
            System.exit(1);
            return;
        }

        Class<? extends Batch> batchClass;
        try {
            batchClass = (Class<? extends Batch>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found: " + className);
            System.exit(1);
            return;
        } catch (ClassCastException ex) {
            System.out.println("Specified class is not an sub-class of " + Batch.class.getName() + " class: "
                    + className);
            System.exit(1);
            return;
        }

        final Batch batch;
        if (raw) {
            batch = batchClass.newInstance();
        } else {
            batch = new BatchLauncher(batchClass);
        }

        if (!batch.init(argList.toArray(new String[0]))) {
            System.exit(1);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    batch.destroy();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        System.exit(batch.execute());
    }
}
