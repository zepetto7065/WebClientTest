package com.example.resttemplatewebclienttest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RestClient implements ApplicationRunner {

    private final RestTemplateBuilder restTemplateBuilder;
    private final WebClient.Builder webClientBuilder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        restTemplateRunner();
        System.out.println("restTemplateRunner is Done, webClientRunner will start");
        webClientRunner();
    }

    public void restTemplateRunner(){
        RestTemplate restTemplate = restTemplateBuilder.build();

        long mainThreadStart = System.currentTimeMillis();

        long helloStart = System.currentTimeMillis();
        String helloReturn = restTemplate.getForObject("http://localhost:8080/hello", String.class);
        long helloEnd = System.currentTimeMillis();
        System.out.println("====================================");
        System.out.println(helloReturn);
        System.out.println(helloEnd-helloStart);

        long worldStart = System.currentTimeMillis();
        String worldReturn = restTemplate.getForObject("http://localhost:8080/world", String.class);
        long worldEnd = System.currentTimeMillis();
        System.out.println("====================================");
        System.out.println(worldReturn);
        System.out.println(worldEnd-worldStart);

        long mainThreadEnd = System.currentTimeMillis();
        System.out.println("====================================");
        System.out.println("RestTemplate = " + (mainThreadEnd-mainThreadStart));
    }


    public void webClientRunner(){
        WebClient webClient = webClientBuilder.build();

        long mainThreadStart = System.currentTimeMillis();

        Mono<String> helloMono = webClient.get()
                .uri("http://localhost:8080/hello")
                .retrieve()
                .bodyToMono(String.class);

        long helloStart = System.currentTimeMillis();
        helloMono.subscribe(s -> {
            System.out.println("====================================");
            System.out.println(s);
            long helloEnd = System.currentTimeMillis();
            System.out.println(helloEnd-helloStart);
            System.out.println("====================================");
        });

        Mono<String> worldMono = webClient.get()
                .uri("http://localhost:8080/world")
                .retrieve()
                .bodyToMono(String.class);

        long worldStart = System.currentTimeMillis();
        worldMono.subscribe(s -> {
            System.out.println("====================================");
            System.out.println(s);
            long worldEnd = System.currentTimeMillis();
            System.out.println(worldEnd-worldStart);
            System.out.println("====================================");
        });

        long mainThreadEnd = System.currentTimeMillis();
        System.out.println("====================================");
        System.out.println("WebClient = " + (mainThreadEnd-mainThreadStart));
    }
}
