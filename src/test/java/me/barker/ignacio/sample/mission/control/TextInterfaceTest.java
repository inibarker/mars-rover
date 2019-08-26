package me.barker.ignacio.sample.mission.control;

import java.util.Collections;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.ApplicationArguments;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TextInterfaceTest extends TestCase {

    private static final String TEST_APP_ARGUMENT = "test_text_arg";

    @Mock
    private TextInterfaceConfigurationProperties configurationProperties;

    @Mock
    private ApplicationArguments applicationArguments;

    private TextInterface underTest;

    @Before
    public void setUp() {
        when(configurationProperties.getAppArgument())
            .thenReturn(TEST_APP_ARGUMENT);

        underTest = new TextInterface(configurationProperties);
    }

    @Test
    public void testRunWithText() {
        when(applicationArguments.getNonOptionArgs())
            .thenReturn(Collections.singletonList(TEST_APP_ARGUMENT));

        StepVerifier.create(Mono
            .fromRunnable(() -> underTest
                .run(applicationArguments)))
            .verifyComplete();
    }

    @Test
    public void testRunNoText() {
        StepVerifier.create(Mono
            .fromRunnable(() -> underTest
                .run(applicationArguments)))
            .verifyComplete();
    }

    @Test
    public void testReport() {
        underTest.report(new MissionStatusStub());
    }

}