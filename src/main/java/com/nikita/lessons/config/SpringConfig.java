package com.nikita.lessons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.nikita.lessons")
@EnableWebMvc
@PropertySource("classpath:database.properties")
public class SpringConfig implements WebMvcConfigurer { /** replacement for applicationContext.xml */

    private final ApplicationContext applicationContext;
    private final Environment environment;

    public static final String DRIVER_PATH = "driver_path";
    public static final String URL = "url";
    public static final String USERNAME_PR = "username_pr";
    public static final String PASSWORD = "password";

    /*public static final String DRIVER_PATH = "org.postgresql.Driver";
    public static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "1111";*/

    /*driver_path = org.postgresql.Driver
            url = jdbc:postgresql://localhost:5432/first_db
    username = postgres
            password = 1111*/

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty(DRIVER_PATH)));
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME_PR));
        dataSource.setPassword(environment.getProperty(PASSWORD));

        /*dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty(DRIVER_PATH)));
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));*/

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
