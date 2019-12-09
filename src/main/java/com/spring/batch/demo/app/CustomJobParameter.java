package com.spring.batch.demo.app;

import org.springframework.batch.core.JobParameter;

import java.io.Serializable;


public class CustomJobParameter<T extends Serializable> extends JobParameter {
    private T customParam;
    public CustomJobParameter(T customParam){
        super("");//This is to avoid duplicate JobInstance error
        this.customParam = customParam;
    }
    public T getValue(){
        return customParam;
    }
}