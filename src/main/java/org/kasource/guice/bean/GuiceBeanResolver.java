package org.kasource.guice.bean;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.di.bean.BeanNotFoundException;
import org.kasource.di.bean.BeanResolver;


import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Bean Resolver for Guice.
 * 
 * Will locate beans using the Guice Injector.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceBeanResolver implements BeanResolver {

    @Inject
    private Injector injector;
    
    /**
     * Returns bean from Guice.
     * 
     * @param <T> Class of the bean
     * @param beanName Name of the bean to return.
     * @param ofType Class of the bean.
     * 
     * @return Bean from the Guice Injector.
     * 
     * @throws CouldNotResolveBeanException if no bean named beanName can be found.
     * @throws ClassCastException if the bean found could not be cast to T.
     **/
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanName, Class<T> ofType) throws BeanNotFoundException {
        Key<T> bindKey = Key.get(ofType, Names.named(beanName));
        try {
            return  injector.getInstance(bindKey);
        } catch (RuntimeException e) {
            Key<?> beanKey = getNamedBeanKey(beanName);
            if (beanKey == null) {
                throw new BeanNotFoundException("Could not find any bean named " + beanName);
            }
            return (T) injector.getInstance(beanKey);
        }
    }

    
    /**
     * Returns the key for the bean with name beanName.
     * 
     * @param beanName Name of bean to find Key for.
     * 
     * @return Key for named bean or null if no Key found.
     **/
    private Key<?> getNamedBeanKey(String beanName) {
        Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Key<?> key : bindings.keySet()) {
            if (key.getAnnotationType() != null && key.getAnnotationType().equals(Named.class)) {
                Named named = (Named) key.getAnnotation();
                if (named.value().equals(beanName)) {
                    return key;
                }
            }
        }
        return null;
    }
    
    
    @Override
    public <T> Set<T> getBeans(Class<T> ofType) {
        Set<T> beans = new HashSet<T>();
        TypeLiteral<T> type = TypeLiteral.get(ofType);
        List<Binding<T>> bindings = injector.findBindingsByType(type);
        for (Binding<T> binding : bindings) {
            try {
                beans.add((T)injector.getInstance(binding.getKey()));
            } catch (Exception e) {
                continue;
            }
        }
        return beans;
    }
    
    /**
     * Returns all beans from which is bound with any of the supplied qualifier annotations.
     * 
     * @param <T> Class of the bean
     * @param ofType Class of the bean.
     * @param qualifiers Qualifier to look for.
     * 
     * @return all beans from which is bound with any of the supplied qualifier annotations.
     **/
    @SuppressWarnings("unchecked")
    @Override
    public <T> Set<T> getBeansByQualifier(Class<T> ofType, Class<? extends Annotation>... qualifiers) {
        Set<T> beans = new HashSet<T>();
        Set<Key<?>> keys = getBeanAnnotationTypeKey(qualifiers);
        for (Key<?> key : keys) {
            Object instance = injector.getInstance(key);
            if (ofType.isAssignableFrom(instance.getClass())) {
                try {
                    beans.add((T) instance);
                } catch (RuntimeException re) {
                    continue;
                }
            }
        }
        return beans;
    }
    
    /**
     * Returns all keys bound to any of the supplied qualifiers.
     * 
     * @param qualifiers Qualifiers to look for.
     * 
     * @return all keys bound to any of the supplied qualifiers.
     **/
    private Set<Key<?>> getBeanAnnotationTypeKey(Class<? extends Annotation>... qualifiers) {
        Set<Key<?>> keys = new HashSet<Key<?>>();
        Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Key<?> key : bindings.keySet()) {
            for (Class<? extends Annotation> qualifier : qualifiers) {
                if (key.getAnnotationType() != null && key.getAnnotationType().equals(qualifier)) {
                   keys.add(key);
                }
            }
        }
        return keys;
        
    }

    /**
     * Returns all beans from which is bound with any of the supplied qualifier annotations.
     * 
     * @param <T> Class of the bean
     * @param ofType Class of the bean.
     * @param qualifiers Qualifier to look for.
     * 
     * @return all beans from which is bound with any of the supplied qualifier annotations.
     **/
    @SuppressWarnings("unchecked")
    @Override
    public <T> Set<T> getBeansByQualifier(Class<T> ofType, Annotation... qualifiers) {
        Set<T> beans = new HashSet<T>();
        Set<Key<?>> keys = getBeanAnnotationKey(qualifiers);
        for (Key<?> key : keys) {
            Object instance = injector.getInstance(key);
            if (ofType.isAssignableFrom(instance.getClass())) {
                try {
                    beans.add((T) instance);
                } catch (RuntimeException re) {
                    continue;
                }
            }
        }
        return beans;
    }
    
    /**
     * Returns all keys bound to any of the supplied qualifiers.
     * 
     * @param qualifiers Qualifiers to look for.
     * 
     * @return all keys bound to any of the supplied qualifiers.
     **/
    private Set<Key<?>> getBeanAnnotationKey(Annotation... qualifiers) {
        Set<Key<?>> keys = new HashSet<Key<?>>();
        Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Key<?> key : bindings.keySet()) {
            for (Annotation qualifier : qualifiers) {             
                if (qualifier.equals(key.getAnnotation())) {
                    keys.add(key);
                } 
            }
        }
        return keys;
        
    }


  



}
