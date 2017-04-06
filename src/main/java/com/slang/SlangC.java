package com.slang;

import java.util.Arrays;

/**
 * Created by sarath on 16/3/17.
 */
public class SlangC {

    public static void main(String[] args) {
        Arrays.stream(args).forEach(s -> System.out.println(s));
    }
}
