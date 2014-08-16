package org.kasource.commons.guice.injection.listener;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;

public class InjectionListenerModule extends AbstractModule {

    private InjectionListener<Object>[] listeners;
    
    public InjectionListenerModule(InjectionListener<Object>... listeners) {
        this.listeners = listeners;
    }
    
    @Override
    protected void configure() {
       InjectionListenerRegister register = new InjectionListenerRegister();
       for (InjectionListener<Object> listener : listeners) {
           register.addListener(listener);
       }
       InjectionTypeListener typeListener = new InjectionTypeListener(register);
       bindListener(Matchers.any(), typeListener); 
        
    }

}
