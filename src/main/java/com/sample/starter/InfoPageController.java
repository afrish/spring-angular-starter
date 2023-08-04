package com.sample.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
public class InfoPageController {

    private Map<String, String> props;

    @GetMapping("/info")
    public String getInfo(Model model) {
        model.addAttribute("commit", getGitProperties().getOrDefault("git.commit.id.describe", "unknown"));
        model.addAttribute("version", getGitProperties().getOrDefault("git.build.version", "unknown"));
        return "info";
    }

    private synchronized Map<String, String> getGitProperties() {
        if (props != null) {
            return props;
        }

        props = new HashMap<>();
        try {
            var properties = new Properties();
            properties.load(Resources.getStream("classpath:git.properties"));
            for (var entry : properties.entrySet()) {
                props.put((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (Exception e) {
            log.error("Failed to load git.properties", e);
        }

        return props;
    }
}
