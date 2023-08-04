package com.sample.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Execute all actions that should happen right after the app start.
 * There are some things that cannot be executed e.g. in Spring @PostConstruct, because it is too early in the lifecycle
 */
@Slf4j
@Component
public class AppStartListener {

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent(ContextRefreshedEvent event) {
        printAllProperties(event);
    }

    private void printAllProperties(ContextRefreshedEvent event) {
        final var env = event.getApplicationContext().getEnvironment();

        var shouldPrint = env.getProperty("app.printEnv", Boolean.class, Boolean.FALSE);
        if (!shouldPrint) {
            return;
        }

        final var sources = ((AbstractEnvironment) env).getPropertySources();
        var props = StreamSupport.stream(sources.spliterator(), false)
                                 .filter(ps -> ps instanceof EnumerablePropertySource)
                                 .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
                                 .flatMap(Arrays::stream)
                                 .distinct()
                                 .sorted()
                                 .map(prop -> safePropertyValue(env, prop))
                                 .collect(Collectors.joining("\n"));
        log.info("\n\n===================== Environment and configuration ====================="
                 + "\n\nActive profiles: " + Arrays.toString(env.getActiveProfiles())
                 + "\n\n" + props
                 + "\n\n=========================================================================\n");
    }

    private String safePropertyValue(Environment env, String prop) {
        if (prop.toLowerCase().contains("password") || prop.toLowerCase().contains("secret")) {
            return prop + ": *****";
        }
        var value = env.getProperty(prop);
        if (value == null) {
            value = "null";
        } else if (value.length() > 100) {
            value = value.substring(0, 97) + "...";
        }
        return prop + ": " + value;
    }
}
