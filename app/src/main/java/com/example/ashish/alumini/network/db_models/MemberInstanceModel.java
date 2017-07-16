package com.example.ashish.alumini.network.db_models;

/**
 * Created by ashish on 19/9/16.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ashish on 3/9/16.
 */

@Table(name = "members")
public class MemberInstanceModel extends Model {

    @Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String _id;

    @Column(name = "name")
    private String name;

//    @Column(name = "email")
    private String email;

//    @Column(name = "bio")
    private String bio;

    @Column(name = "isNerd")
    private Boolean isNerd;

//    @Column(name = "phone")
    private String phone;

//    @Column(name = "weblink")
    private String weblink;

//    @Column(name = "branch")
    private String branch;

    @Column(name = "year")
    private String year;

//    @Column(name = "home")
    private String home;

    @Column(name = "work")
    private String work;

    @Column(name = "designation")
    private String designation;

//    @Column(name = "company")
    private String company;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

}
