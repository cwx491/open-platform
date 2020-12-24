package com.alliance.radish.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1095036083289522032L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "note")
    private String note;

//    @ManyToMany
//    @JoinTable(name = "t_role_resource", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"))
//    private List<Resource> resources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

//    public List<Resource> getResources() {
//        return resources;
//    }
//
//    public void setResources(List<Resource> resources) {
//        this.resources = resources;
//    }
}
