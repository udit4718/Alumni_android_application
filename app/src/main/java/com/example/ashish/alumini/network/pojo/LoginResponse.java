package com.example.ashish.alumini.network.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashish on 7/9/16.
 */

public class LoginResponse {

    private String _id;
    private String name;
    private String work;

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The _id
     */
    public String get_id() {
        return _id;
    }

    /**
     *
     * @param _id
     *     The _id
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
