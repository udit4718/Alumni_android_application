package com.example.ashish.alumini.network.pojo;

/**
 * Created by ashish on 8/8/16.
 */

public class JobListInstance {

    private String _id;
    private String name;
    private String location;
    private String role;




    private String postedbyid;


    /**
     *
     * @return
     * The _id
     */
    public String get_id() {
        return _id;
    }

    /**
     *
     * @param _id
     * The _id
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The role
     */
    public String getRole() {
        return role;
    }

    /**
     *
     * @param role
     * The Role
     */
    public void setRole(String role) {
        this.role = role;
    }


    public String getPostedbyid() {
        return postedbyid;
    }

    public void setPostedbyid(String postedbyid) {
        this.postedbyid = postedbyid;
    }
}