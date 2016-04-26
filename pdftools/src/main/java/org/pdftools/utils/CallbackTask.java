package org.pdftools.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallbackTask<V> extends FutureTask<V> {

    private final Callable<Void> callback;

    public CallbackTask(Callable<V> method, Callable<Void> callback) {
      super(method);
      this.callback = callback;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            this.callback.call();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

  }