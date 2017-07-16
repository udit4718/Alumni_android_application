package com.example.ashish.alumini.network.db_models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ashish on 20/9/16.
 */
@Table(name = "job")
public class JobInstanceModel extends Model {
    @Column(name = "_id" , unique = true , onUniqueConflict = Column.ConflictAction.REPLACE)
    String _id;

    @Column(name = "name")
    String name;

    @Column(name = "designation")
    String designation;

    @Column(name = "location")
    String location;



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
