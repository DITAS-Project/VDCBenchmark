package de.tub.service;

import de.tub.model.RichRequest;
import de.tub.model.SimpleResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class BenchmarkRunner {
    private final OkHttpClient client;

    private final Logger log = LogUtil.getLogger();

    public BenchmarkRunner() {
        client = new OkHttpClient();
    }

    private SimpleResult runRequest(Request request, int delay, int varianz) throws IOException, InterruptedException {
        SimpleResult result = new SimpleResult();

        //randomize waiting time
        long waiting = Math.round(delay + Math.random() * varianz);
        sleep(waiting);

        //actual servercall
        Response response = client.newCall(request).execute();

        result.setStatus(response.code());
        result.setTime(response.receivedResponseAtMillis() - response.sentRequestAtMillis());
        return result;


    }

    public List<SimpleResult> benchmarkRequestSingle(Request request, int warmup, int iterations) throws IOException {
        warmup(request, warmup);
        log.info("starting benchmark...");
        return benchmarkRequest(request, iterations, 50, 30);


    }

    private List<SimpleResult> benchmarkRequest(Request request, int iterations, int delay, int varianz) throws IOException {
        LinkedList<SimpleResult> results = new LinkedList<SimpleResult>();


        try {
            for (int i = 0; i < iterations; i++) {
                log.info("benchmark iteration " + (i + 1));
                SimpleResult result = runRequest(request, delay, varianz);
                log.info("http statuscode " + result.getStatus());
                results.add(result);

            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return results;
    }

    private void warmup(Request request, int warmup) throws IOException {
        log.info("Starting warmup...");
        for (int i = 0; i < warmup; i++) {
            log.info("warmup iteration " + (i + 1));
            try {
                runRequest(request, 50, 30);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    //4000 pro s
    public List<SimpleResult> multithreadBenchmark(RichRequest request, int warmup, int iterations) throws IOException {
        int threads = request.getMetaInfo().getThreads();
        Request toExecute = RequestFactory.getRequest(request.getRequest());
        warmup(toExecute, warmup);
        Queue<SimpleResult> results = new ConcurrentLinkedQueue();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.execute(new RequestJob(toExecute, results, iterations, request.getMetaInfo().getDelay(), request.getMetaInfo().getVarianz()));
        }
       pool.shutdown();
        try {
            pool.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new LinkedList<>(results);
    }



    private class RequestJob implements Runnable {
        private Request request;
        private Queue<SimpleResult> results;
        private int iterations;
        private int varianz;
        private int delay;

        public RequestJob(Request request, Queue<SimpleResult> results, int iterations, int delay, int varianz) {
            this.request = request;
            this.results = results;
            this.iterations = iterations;
            this.delay = delay;
            this.varianz = varianz;
        }

        @Override
        public void run() {
            try {

                results.addAll(benchmarkRequest(request, iterations, delay, varianz));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
