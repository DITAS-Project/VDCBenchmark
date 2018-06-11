package de.tub;

import de.tub.model.RichRequest;
import de.tub.model.SimpleResult;
import org.apache.log4j.Logger;
import de.tub.service.BenchmarkRunner;
import de.tub.service.LogUtil;
import de.tub.service.RequestFactory;

import java.io.IOException;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class Benchmark {

    private static Logger log = LogUtil.getLogger();

    public static void main(String... args) throws IOException {
        BenchmarkRunner runner = new BenchmarkRunner();
        /*Request request = new Request.Builder()
                .url("https://" +"google.com")
                        //"190afbef-5c10-4fd9-bb11-515f57b93eab.mock.pstmn.io/test/*")
                .get()
                .build();*/
        List<RichRequest> request = RequestFactory.getRichRequests("/request.json");
        int warmup= 5;
        int benchmark=20;
        List<SimpleResult> results = runner.multithreadBenchmark(request.get(0), warmup, benchmark);
        LongSummaryStatistics collect = results.stream().map(SimpleResult::getTime).collect(LongSummaryStatistics::new,
                LongSummaryStatistics::accept,
                LongSummaryStatistics::combine);
        log.info("warmup iterations: "+warmup);
        log.info("benchmark iterations: "+ benchmark);
        log.info("Max: " + (double) collect.getMax() / 1000 + " s");
        log.info("Min: " + (double) collect.getMin() / 1000 + " s");
        log.info("Avg: " + collect.getAverage() / 1000 + " s");

        int not200 = results.size() - results.stream().filter((SimpleResult s) -> s.getStatus() == 200).collect(Collectors.toList()).size();
        log.info("Percentage of not 200 codes: " + (not200 / results.size()) * 100);

    }
}
