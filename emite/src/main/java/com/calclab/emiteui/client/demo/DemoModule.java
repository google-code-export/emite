package com.calclab.emiteui.client.demo;

import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emiteui.client.DemoParameters;

public class DemoModule implements Module {

    public void onLoad(final Container container) {
	final DemoParameters params = container.registerSingletonInstance(DemoParameters.class, new DemoParameters());
	container.registerSingletonInstance(EmiteDemoUI.class, new EmiteDemoUI(params));
    }

}
