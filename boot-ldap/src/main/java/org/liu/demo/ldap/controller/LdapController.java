package org.liu.demo.ldap.controller;

import org.liu.demo.ldap.entity.User;
import org.liu.demo.ldap.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LdapController {

    @Autowired
    private LdapService ldapService;

    @GetMapping("/ap")
    public Boolean ap() {
        return ldapService.ldapAuth("admin", "admin");
    }

    @GetMapping("/search")
    public List<User> search() {
        return ldapService.ldapSearch("admin");
    }

}
