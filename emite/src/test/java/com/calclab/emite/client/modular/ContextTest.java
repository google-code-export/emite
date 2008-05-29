package com.calclab.emite.client.modular;

import org.junit.Before;
import org.junit.Test;

public class ContextTest {

    private Context<Object> context;
    private Container container;

    @Before
    public void beforeTests() {
	container = new HashContainer();
	context = new Context<Object>(Object.class);
    }

    @Test
    public void test1() {
    }
}
