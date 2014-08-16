package org.kasource.guice.injection.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.google.inject.Singleton;
import com.google.inject.spi.InjectionListener;

/**
 * Register of InjectionListeners, that will be invoked when the register is invoked. 
 * 
 * Invokes all registered InjectionListeners.
 * 
 * @author rikardwi
 **/
public class InjectionListenerRegister implements InjectionListener<Object>{

    private List<InjectionListener<Object>> listeners = new ArrayList<InjectionListener<Object>>();
    private boolean oncePerSingleton = true;
    private Set<Class<?>> vistedClasses = new HashSet<Class<?>>();
    public InjectionListenerRegister(){
       
    }
    
    
    public void addListener(InjectionListener<Object> listener) {
        listeners.add(listener);
    }
    
    
    /**
     * Invokes all registered InjectionListeners.
     * 
     * @param injectee  The object that has been injected.
     */
    @Override
    public void afterInjection(Object injectee) {
       
       for (InjectionListener<Object> listener : listeners) {
           // Only invoke singletons once, if oncePerSingleton is true
           if (oncePerSingleton && injectee.getClass().isAnnotationPresent(Singleton.class)) {
               if (vistedClasses.contains(injectee.getClass())) {          
                  continue;
               } else {
                   vistedClasses.add(injectee.getClass());
               }
               
           }
           listener.afterInjection(injectee);
           
       }
        
    }


    /**
     * Returns the once per singleton.
     * 
     * @return the oncePerSingleton
     */
    protected boolean isOncePerSingleton() {
        return oncePerSingleton;
    }


    /**
     * Set the once per singleton.
     * 
     * @param oncePerSingleton the oncePerSingleton to set
     */
    protected void setOncePerSingleton(boolean oncePerSingleton) {
        this.oncePerSingleton = oncePerSingleton;
    }

}
