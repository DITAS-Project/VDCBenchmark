package service;

import model.SimpleResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class BenchmarkRunner {
   private final OkHttpClient client;

   private final Logger log= LogUtil.getLogger();

    public BenchmarkRunner(){
        client= new OkHttpClient();
    }

    private SimpleResult runRequest(Request request) throws  IOException {
        SimpleResult result = new SimpleResult();

        //long start = System.nanoTime();
        Response response = client.newCall(request).execute();
        //long finished = System.nanoTime();

        result.setStatus(response.code());
        result.setTime(response.receivedResponseAtMillis()-response.sentRequestAtMillis());
        return result;


    }

    public List<SimpleResult> benchmarkRequest(Request request, int warmup, int iterations) throws IOException {
        log.info("Starting warmup...");
        for(int i=0; i<warmup;i++){
            log.info("warmup iteration "+ (i+1));
            runRequest(request);
        }
        log.info("starting benchmark...");
        LinkedList<SimpleResult> results= new LinkedList<SimpleResult>();
        for(int i=0;i<iterations; i++){
            log.info("benchmark iteration " + (i+1));
            SimpleResult result=runRequest(request);
            log.info("http statuscode "+result.getStatus());
            try {
                sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            results.add(result);

        }
        return results;



    }
}
