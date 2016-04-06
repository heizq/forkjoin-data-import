package cn.lamppa.edu.platform.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liupd on 16-2-26.
 **/
public class ThreadPool {

    private static ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(10);

    public static void execute(Runnable command){
        executor.execute(command);
    }

    public static void execute(Runnable command,long delayseconds){
        executor.schedule(command, delayseconds, TimeUnit.SECONDS);
    }

    public static void shutdown(){
        executor.shutdown();
    }

    public static void remove(Runnable Task){
        executor.remove(Task);
    }


}
