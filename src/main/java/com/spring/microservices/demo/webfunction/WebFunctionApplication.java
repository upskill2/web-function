package com.spring.microservices.demo.webfunction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class WebFunctionApplication {

    public static void main (String[] args) {
        SpringApplication.run (WebFunctionApplication.class, args);
    }

    List<TollStation> tollStationList;

    public WebFunctionApplication () {
        tollStationList = new ArrayList<> ();
        tollStationList.add (new TollStation ("100A", 112.5f, 2));
        tollStationList.add (new TollStation ("111C", 124.8f, 4));
        tollStationList.add (new TollStation ("115D", 22.31f, 3));
    }

    @Bean
    public Function<String, TollStation> retrieveStation () {
        return value -> {
            log.info ("received request for station: {}", value);
            return tollStationList.stream ()
                    .filter (toll -> value.equals (toll.getStationId ()))
                    .findAny ()
                    .orElse (null);
        };
    }

   @Bean
    public Consumer<TollRecord> processTollRecord () {
        return value ->{
            log.info ("received toll for car with license plate: {}", value.licensePlate ());
        };
    }

    @Bean
    public Function <TollRecord, Mono<Void>> processTollRecordReactive(){
        return value->{
            log.info ("received reactive toll for car with license plate: {}", value.stationId ());
            return Mono.empty ();
        };
    }

    @Bean
    public Consumer<Flux<TollRecord>> processListOfTollRecordsReactive(){
        return value ->{
          value.subscribe (toll->log.info (toll.licensePlate ()));
        };
    }

    @Bean
    public Supplier<Flux<TollStation>> getTollStations(){
        return ()->{
            log.info ("received reactive toll for tool stations");
            return Flux.fromIterable (tollStationList);
        };
    }
}
