package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        var stringFlux = service.namesFlux();

        StepVerifier.create(stringFlux)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_Immutability() {
        var stringFlux = service.namesFlux_immutability()
                .log();
        StepVerifier.create(stringFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesMono() {
        var stringMono = service.namesMono();
        StepVerifier.create(stringMono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter() {
        int stringLength = 3;
        var stringMono = service.namesMono_map_filter(stringLength);
        StepVerifier.create(stringMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void namesMono_map_empty() {
        int stringLength = 4;
        var stringMono = service.namesMono_map_filter(stringLength);

        StepVerifier.create(stringMono)
                .expectNext("default")
                .verifyComplete();
    }


    @Test
    void namesFlux_map() {
        int stringLength = 3;

        var namesFlux = service.namesFlux_map(stringLength).log();

        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_flatmap(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_async() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_flatmap_async(stringLength).log();

        StepVerifier.create(namesFlux)
                /*.expectNext("0-A", "1-L", "2-E", "3-X")
                .expectNextCount(5)*/
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_concatmap(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                //expectNext("0-A", "1-L", "2-E", "3-X")
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatmap_withVirtualTime() {
        VirtualTimeScheduler.getOrSet();
        int stringLength = 3;
        var namesFlux = service.namesFlux_concatmap(stringLength);

        StepVerifier.withVirtualTime(() -> namesFlux)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatmap() {
        int stringLength = 3;
        var namesFlux = service.namesMono_flatmap(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatmapMany() {
        int stringLength = 3;
        var namesFlux = service.namesMono_flatmapMany(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }


    @Test
    void namesFlux_transform() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_transform(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        int stringLength = 6;
        var namesFlux = service.namesFlux_transform(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("default")
                //.expectNextCount(5)
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        int stringLength = 6;
        var namesFlux = service.namesFlux_transform_switchIfEmpty(stringLength).log();

        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                //.expectNextCount(5)
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_concatwith() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_transform_concatwith(stringLength).log();

        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX", "5-CHLOE", "4-ANNA")
                .verifyComplete();

    }

    @Test
    void name_defaultIfEmpty() {
        var value = service.name_defaultIfEmpty();
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    void name_switchIfEmpty() {
        var value = service.name_switchIfEmpty();
        StepVerifier.create(value)
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var value = service.explore_concat();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith() {
        var value = service.explore_concatWith();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concat_mono() {
        var value = service.explore_concatWith_mono();
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var value = service.explore_merge();
        StepVerifier.create(value)
                // .expectNext("A", "B", "C", "D", "E", "F")
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        var value = service.explore_mergeWith();
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith_mono() {
        var value = service.explore_mergeWith_mono();
        StepVerifier.create(value)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        var value = service.explore_mergeSequential();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var value = service.explore_zip().log();
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        var value = service.explore_zip_1().log();
        StepVerifier.create(value)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void explore_zip_2() {
        var value = service.explore_zip_2().log();
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        var value = service.explore_zipWith().log();
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {
        var value = service.explore_zipWith_mono().log();
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono_delay() {
        var value = service.explore_zipWith_mono_delay().log();
        StepVerifier.create(value)
                //.expectNext("AB")
                .expectError()
                .verify();
    }

    @Test
    void exception_flux() {
        var flux = service.exception_flux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify(); // you cannot do a verify complete in here
    }

    @Test
    void exception_flux_1() {
        var flux = service.exception_flux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError()
                .verify(); // you cannot do a verify complete in here
    }

    @Test
    void exception_flux_2() {
        var flux = service.exception_flux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectErrorMessage("Exception Occurred")
                .verify(); // you cannot do a verify complete in here
    }

    @Test
    void explore_OnErrorReturn() {
        var flux = service.explore_OnErrorReturn().log();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {
        var e = new IllegalStateException("Not a valid state");
        var flux = service.explore_OnErrorResume(e).log();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume_1() {
        var e = new RuntimeException("Not a valid state");
        var flux = service.explore_OnErrorResume(e).log();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorMap() {
        var e = new RuntimeException("Not a valid state");
        var flux = service.explore_OnErrorMap(e)
                .log();
        StepVerifier.create(flux)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_OnErrorMap_checkpoint() {

        /*Error has been observed at the following site(s):
	        |_ checkpoint ⇢ errorspot*/
        //given
        // In production, it could the code or the data thats executing in the run time that might throw the exception
        var e = new RuntimeException("Not a valid state");

        //when
        var flux = service.explore_OnErrorMap_checkpoint(e).log();

        //then
        StepVerifier.create(flux)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }


    @Test
    /**
     * This gives the visibility of which operator caused the problem
     * This operator gives the "Assembly trace" which is not available when you use chekpoint
     * This also gives you the line that caused the problem
     */
    void explore_OnErrorMap_onOperatorDebug() {

        //You will see the below in the code
/*
        Error has been observed at the following site(s):
	|_      Flux.error ⇢ at com.learnreactiveprogramming.service.FluxAndMonoGeneratorService.explore_OnErrorMap_checkpoint(FluxAndMonoGeneratorService.java:336)
	|_ Flux.concatWith ⇢ at com.learnreactiveprogramming.service.FluxAndMonoGeneratorService.explore_OnErrorMap_checkpoint(FluxAndMonoGeneratorService.java:336)
	|_      checkpoint ⇢ errorspot
*/

        //given
        Hooks.onOperatorDebug();
        var e = new RuntimeException("Not a valid state");

        //when
        var flux = service.explore_OnErrorMap_checkpoint(e).log();

        //then
        StepVerifier.create(flux)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }


    @Test
    /**
     * This gives the visibility of which operator caused the problem without any performance overhead
     */
    void explore_OnErrorMap_reactorDebugAgent() {

        //given
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        var e = new RuntimeException("Not a valid state");

        //when
        var flux = service.explore_OnErrorMap_checkpoint(e).log();

        //then
        StepVerifier.create(flux)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void doOnError() {

        //given
        var e = new RuntimeException("Not a valid state");

        //when
        var flux = service.explore_doOnError(e);

        //then
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();


    }

    @Test
    void explore_OnErrorContinue() {

        //given

        //when
        var flux = service.explore_OnErrorContinue().log();

        //then
        StepVerifier.create(flux)
                .expectNext("A", "C", "D")
                .verifyComplete();

    }


    @Test
    void exception_mono() {

        //given

        //when
        var mono = service.exception_mono_exception();

        //then
        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    void exception_mono_1() {

        //given

        //when
        var mono = service.exception_mono_exception();

        //then
        StepVerifier.create(mono)
                .expectErrorMessage("Exception Occurred")
                .verify();

    }

    @Test
    void exception_mono_onErrorResume() {

        //given
        var e = new IllegalStateException("Not a valid state");


        //when
        var mono = service.exception_mono_onErrorResume(e);

        //then
        StepVerifier.create(mono)
                .expectNext("abc")
                .verifyComplete();
    }

    @Test
    void exception_mono_onErrorReturn() {

        //given


        //when
        var mono = service.exception_mono_onErrorReturn();

        //then
        StepVerifier.create(mono)
                .expectNext("abc")
                .verifyComplete();
    }

    @Test
    void exception_mono_onErrorMap() {

        //given
        var e = new IllegalStateException("Not a valid state");


        //when
        var mono = service.exception_mono_onErrorMap(e);

        //then
        StepVerifier.create(mono)
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void exception_mono_onErrorContinue() {

        //given
        var input = "abc";

        //when
        var mono = service.exception_mono_onErrorContinue(input);

        //then
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    void exception_mono_onErrorContinue_1() {

        //given
        var input = "reactor";

        //when
        var mono = service.exception_mono_onErrorContinue(input);

        //then
        StepVerifier.create(mono)
                .expectNext(input)
                .verifyComplete();
    }

    @Test
    void explore_generate() {

        //given
        //Demonstrate multiple emissions per round is not supported

        //when
        var flux = service.explore_generate().log();

        //then
        StepVerifier.create(flux)
                .expectNext(2, 4)
                .expectNextCount(8)
                .verifyComplete();

    }


    @Test
    void explore_create() {

        //given

        //when
        var flux = service.explore_create().log();

        //then
        StepVerifier.create(flux)
                //.expectNext("alex", "ben", "chloe")
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    void explore_create_mono() {

        //given

        //when
        var mono = service.explore_create_mono().log();

        //then
        StepVerifier.create(mono)
                //.expectNext("alex", "ben", "chloe")
                .expectNext("alex")
                .verifyComplete();

    }

    @Test
    void explore_push() {

        //given

        //when
        var flux = service.explore_push().log();

        //then
        StepVerifier.create(flux)
                //.expectNext("alex", "ben", "chloe")
                .expectNextCount(3)
                // .thenConsumeWhile(Objects::nonNull)
                .verifyComplete();

    }

    @Test
    void explore_handle() {

        //given

        //when
        var flux = service.explore_handle().log();

        //then
        StepVerifier.create(flux)
                //.expectNext("alex", "ben", "chloe")
                .expectNextCount(2)
                .verifyComplete();

    }


    @Test
    void explore_mono_create() {

        //given

        //when
        var mono = service.explore_mono_create();

        //then
        StepVerifier.create(mono)
                .expectNext("abc")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_sequential() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_flatmap_sequential(stringLength).log();

        StepVerifier.create(namesFlux)
                //.expectNext("A", "L", "E", "X")
                .expectNextCount(9)
                .verifyComplete();

    }


    @Test
    void namesFlux_delay() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_delay(stringLength).log();

        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void range() {
        var rangeFlux = service.range(5).log();
        StepVerifier.create(rangeFlux)
                //.expectNext(0,1,2,3,4)
                .expectNextCount(5)
                .verifyComplete();
    }
}