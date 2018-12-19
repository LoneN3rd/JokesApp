package com.example.android.jokesprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokesProvider {

    private List<String> jokes = new ArrayList<>();

    public String provideJokes(){

        jokes.add("Why did the programmer use the entire bottle of shampoo during one shower?\n"+
                "Because the bottle said \"Lather, Rinse, Repeat.\"");

        // add jokes here
        jokes.add("We’ll we’ll we’ll…if it isn’t auto-correct.");

        jokes.add("What does a proud computer call his little son?\n"+
            "A microchip off the old block.");

        // get a random number
        Random random = new Random();
        int i = random.nextInt(jokes.size());

        return jokes.get(i);
    }
}
