package com.sample.starter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller works as a bridge between the frontend and the backend. Angular will handle all the routing and this
 * controller will just forward all requests to the index.html file, which is created by the Angular build. The
 * index.html file is defined as a template in the Spring Boot configuration. Any specific routes that need to be
 * handled by the backend should be added to the backend controllers. Since index.html is a template, it can be
 * modified to include any data that is needed by the frontend. This can be done by adding the data to the model
 * object.
 */
@Slf4j
@Controller
public class IndexPageController {

    /**
     * Handle routes that fullfill all of the following criteria:
     * <ul>
     *     <li>do not match "assets" - the default Angular folder for static files</li>
     *     <li>do not contain a dot - a marker of a file extension</li>
     * </ul>
     */
    @SuppressWarnings("unused")
    @RequestMapping("/{path:(?!.*assets)[^.]+}/**")
    public String pages(@PathVariable String path, Model model, HttpServletRequest request) {
        return getIndexPage(model, request);
    }

    /**
     * Handle the root route
     */
    @RequestMapping("/")
    public String root(Model model, HttpServletRequest request) {
        return getIndexPage(model, request);
    }

    private String getIndexPage(Model model, HttpServletRequest request) {
        var lang = request.getLocale().toLanguageTag();
        model.addAttribute("lang", lang != null ? lang : "en");
        return "index";
    }

}

