package service;


import com.fasterxml.jackson.databind.ObjectMapper;
import model.RichRequest;
import model.SimpleRequest;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class RequestFactory {

private static ObjectMapper mapper= new ObjectMapper();

    private static List<Request> createRequests(List<RichRequest> requests){
        LinkedList<Request> results= new LinkedList<>();

        for(RichRequest rr: requests){
            Request parsed;
            SimpleRequest re= rr.getRequest();
            if(re.getMethod().equals("GET")){
                parsed= new Request.Builder()
                        .headers(Headers.of(re.getHeaders()))
                        .url(re.getUrl())
                        .get()
                        .build();
            }else {
                parsed = new Request.Builder()
                        .method(re.getMethod(), RequestBody.create(MediaType.parse("application/json"), re.body.toString()))
                        .headers(Headers.of(re.getHeaders()))
                        .url(re.getUrl())
                        .build();
            }
            results.add(parsed);
        }
        return results;
    }

    public static List<Request> getRequest(String filename){
        String raw = readToString(filename);
        List<RichRequest> requests= new LinkedList<>();
        try {
            RichRequest simpleRequest = mapper.readValue(raw, RichRequest.class);
            requests.add(simpleRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createRequests(requests);

    }

    private static String readToString(String filename) {
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(RequestFactory.class.getResourceAsStream(filename)))) {
            reader.lines().forEach(s -> sb.append(s));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
