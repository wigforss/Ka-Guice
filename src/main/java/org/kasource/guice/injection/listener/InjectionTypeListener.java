package org.kasource.guice.injection.listener;


import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Registers a general purpose InjectionListener for all
 * Types, @See InjectionListenerRegister.
 * 
 * @author rikardwi
 **/
public class InjectionTypeListener implements TypeListener {
    
    
   
    @Inject
    private InjectionListenerRegister register;
    
    public InjectionTypeListener(InjectionListenerRegister register) {
        this.register = register;
    }
    
    /**
     * Register the InjectionListenerRegister as the InjectionListener for
     * all types.
     * 
     * @param type Type found-
     * @param encounter The context the type has been encountered in.
     **/
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {

        encounter.register(register);
        
    }

}
