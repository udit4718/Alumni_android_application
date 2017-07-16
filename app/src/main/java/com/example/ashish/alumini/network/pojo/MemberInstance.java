package com.example.ashish.alumini.network.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashish on 3/9/16.
 */

public class MemberInstance {

    private String _id;
    private String name;
    private String email;
    private String bio;
    private Boolean isNerd;
    private String phone;
    private String weblink;
    private String branch;
    private String year;
    private String home;
    private String work;
    private String designation;
    private String company;
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

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The bio
     */
    public String getBio() {
        return bio;
    }

    /**
     *
     * @param bio
     *     The bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     *
     * @return
     *     The isNerd
     */
    public Boolean getIsNerd() {
        return isNerd;
    }

    /**
     *
     * @param isNerd
     *     The isNerd
     */
    public void setIsNerd(Boolean isNerd) {
        this.isNerd = isNerd;
    }

    /**
     *
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     *     The weblink
     */
    public String getWeblink() {
        return weblink;
    }

    /**
     *
     * @param weblink
     *     The weblink
     */
    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    /**
     *
     * @return
     *     The branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     *
     * @param branch
     *     The branch
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     *
     * @return
     *     The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     *     The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     *     The home
     */
    public String getHome() {
        return home;
    }

    /**
     *
     * @param home
     *     The home
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     *
     * @return
     *     The work
     */
    public String getWork() {
        return work;
    }

    /**
     *
     * @param work
     *     The work
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     *
     * @return
     *     The designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     *
     * @param designation
     *     The designation
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     *
     * @return
     *     The company
     */
    public String getCompany() {
        return company;
    }

    /**
     *
     * @param company
     *     The company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}