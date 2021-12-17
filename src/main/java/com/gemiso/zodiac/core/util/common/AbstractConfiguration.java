package com.gemiso.zodiac.core.util.common;

import java.util.Properties;

public abstract class AbstractConfiguration implements Config{
    protected Properties props = null;

    public Properties getProperties(){

        return this.props;

    }

    public String getString(String key){

        String value = null;
        value = this.props.getProperty(key);

        if (value == null) throw new IllegalArgumentException("Illegal String key : " + key);

        return value;
    }

    public int getInt(String key){

        int value = 0;
        try {
            value = Integer.parseInt(this.props.getProperty(key));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Illegal int key : " + key);
        }
        return value;
    }

    public double getDouble(String key){
        double value = 0.0D;
        try {
            value = Double.valueOf(this.props.getProperty(key)).doubleValue();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Illegal double key : " + key);
        }
        return value;
    }

    public boolean getBoolean(String key){
        boolean value = false;
        try {
            value = Boolean.valueOf(this.props.getProperty(key)).booleanValue();
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Illegal boolean key : " + key);
        }
        return value;
    }
}