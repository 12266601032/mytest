package com.sample.service;

import groovy.lang.GroovyShell;
import org.junit.Test;

public class GroovyDemo {

    @Test
    public void name() {
        GroovyShell shell = new GroovyShell();
        Object evaluate = shell.evaluate("\"rs_${0..3}\"");

        shell.setProperty("name","hi");
        Object name = shell.evaluate("\" ${name} \"");

        System.out.println(evaluate);
        System.out.println(name);
    }
}
