package com.luv2code.springbootlibrary.config;

import com.luv2code.springbootlibrary.entity.Book;
//import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

//RepositoryRestConfigurer is an interface that allows you to customize the configuration of the REST exporter. It provides methods that can be used to customize the way resources are exported.
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private String theAllowedOrigins = "https://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.PUT};

        //By default, Spring Data REST does not expose the IDs of the entities in its responses for security reasons. Exposing IDs could potentially lead to information leakage or security vulnerabilities. However, there might be cases where you want to expose the IDs intentionally, and Spring Data REST provides a way to achieve that.
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
      //  config.exposeIdsFor(Message.class);

        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class, config, theUnsupportedActions);
        //disableHttpMethods(Message.class, config, theUnsupportedActions);

        /* Configure CORS Mapping */

        /* CORS is a security feature implemented by web browsers that prevents web pages from making requests
         to a different domain than the one that served the web page. CORS allows servers to specify which origins (domains)
          are allowed to access their resources.*/

        //getBasePath()=/api/** means anything like home,.....
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    //Disable Http methods
    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                //single item
                .withItemExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions))
                //collection of items
                .withCollectionExposure((metdata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions));
    }
}

//withItemExposure: Disables the support for HTTP PUT, POST, DELETE on all item resources.
//withCollectionExposure: Disables the support for HTTP PUT, POST, DELETE on all collection resources.
//For CORS
    // We need to tell the Spring Boot app ... which URLs are allowed to call the REST APIs.
    //In the code snippet below, we configure CORS by adding a mapping for the base path with "/**". The ** means all sub-paths recursively.
    // Then we add the allowed origins that can call the REST APIs. This list of allowed origins are injected from the properties file.