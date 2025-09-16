package com.tacocloud;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;

public class ReactorLearningTests {
    @Test
    public void createAFlux() {
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Banana", "Kiwi", "Strawberry");
        fruitFlux.subscribe(
                fruit -> System.out.println("Here's some fruit: " + fruit)
        );

        var fruitArray = new String[] {
                "Apple",
                "Orange",
                "Banana",
                "Kiwi",
                "Strawberry"
        };
        var fruitFluxFromArray = Flux.fromArray(fruitArray);
        StepVerifier.create(fruitFluxFromArray)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Banana")
                .expectNext("Kiwi")
                .expectNext("Strawberry")
                .verifyComplete();

        var fruitList = Arrays.asList(fruitArray);
        var fruitFluxFromList = Flux.fromIterable(fruitList);
        StepVerifier.create(fruitFluxFromList)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Banana")
                .expectNext("Kiwi")
                .expectNext("Strawberry")
                .verifyComplete();

        var fruitStream = fruitList.stream();
        var fruitFluxFromStream = Flux.fromStream(fruitStream);
        StepVerifier.create(fruitFluxFromStream)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Banana")
                .expectNext("Kiwi")
                .expectNext("Strawberry")
                .verifyComplete();

        var rangeFlux = Flux.range(1, 5);
        StepVerifier.create(rangeFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();

        var intervalFlux = Flux.interval(Duration.ofSeconds(1))
                .take(5);
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void testMergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();
    }

    @Test
    public void testZipFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<Tuple2<String, String>> zipFlux = Flux.zip(characterFlux, foodFlux);
        StepVerifier.create(zipFlux)
                .expectNextMatches(p ->
                        p.getT1().equals("Garfield") && p.getT2().equals("Lasagna")
                )
                .expectNextMatches(p ->
                        p.getT1().equals("Kojak") && p.getT2().equals("Lollipops")
                )
                .expectNextMatches(p ->
                        p.getT1().equals("Barbossa") && p.getT2().equals("Apples")
                )
                .verifyComplete();
    }

    @Test
    public void testZipFluxesToObject() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> zipFlux = Flux.zip(
                characterFlux,
                foodFlux,
                (character, food) -> String.format("%s eats %s", character, food)
        );
        StepVerifier.create(zipFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();
    }

    @Test
    public void firstWithSignalFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));
        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.firstWithSignal(slowFlux, fastFlux);
        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }
}
