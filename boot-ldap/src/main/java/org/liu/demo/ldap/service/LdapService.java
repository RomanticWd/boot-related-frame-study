package org.liu.demo.ldap.service;

import org.liu.demo.ldap.entity.User;

import java.util.List;

public interface LdapService {

    boolean ldapAuth(String username, String passWord);

    List<User> ldapSearch(String username);

}
