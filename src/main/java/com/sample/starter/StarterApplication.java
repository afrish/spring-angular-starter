package com.sample.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@SpringBootApplication
public class StarterApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }


    /**
     * Redefine the default static resource mapping to allow loading gzipped static resources via
     * {@link EncodedResourceResolver}. To make it work, the corresponding resources in the /static folder need to be
     * pre-gzipped with extension *.gz. The other resource resolver {@link PathResourceResolver} is added as a fallback
     * for the resources the are not pre-gzipped.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new EncodedResourceResolver())
                .addResolver(new PathResourceResolver());
    }

    /**
     * Define the default template resolver for the Thymeleaf templates. This resolver is used for all templates except
     * the index.html file.
     */
    @Bean
    public ClassLoaderTemplateResolver defaultTemplateResolver() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setOrder(1);
        resolver.setCheckExistence(true);
        return resolver;
    }

    /**
     * Define an additional template resolver for the index.html file. The main purpose of this resolver is to
     * allow the index.html file to work as a template, but to be served from the /static folder. This is required
     * because the index.html file is built by Angular, but also needs to be able to use Thymeleaf expressions.
     */
    @Bean
    ClassLoaderTemplateResolver indexTemplateResolver() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("static/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setOrder(2);
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    SpringTemplateEngine templateEngine() {
        var engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolvers(Set.of(defaultTemplateResolver(), indexTemplateResolver()));
        return engine;
    }

    @Bean
    ThymeleafViewResolver viewResolver() {
        var resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setOrder(1);
        return resolver;
    }

}
