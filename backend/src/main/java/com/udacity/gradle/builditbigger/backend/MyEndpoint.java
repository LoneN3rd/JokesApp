package com.udacity.gradle.builditbigger.backend;

import com.example.android.jokesprovider.JokesProvider;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a joke from the java library and shows it */
    @ApiMethod(name = "getJoke")
    public MyBean getJoke(){

        MyBean response = new MyBean();

        //get joke from jokes provider
        response.setData(new JokesProvider().provideJokes());

        return response;
    }

}
