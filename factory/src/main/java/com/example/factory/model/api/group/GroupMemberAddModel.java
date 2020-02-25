package com.example.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class GroupMemberAddModel {
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
