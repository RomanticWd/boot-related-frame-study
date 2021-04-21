package org.liu.demo.ldap.service.impl;

import org.liu.demo.ldap.entity.User;
import org.liu.demo.ldap.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LdapServiceImpl implements LdapService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public boolean ldapAuth(String username, String passWord) {
        ldapTemplate.setIgnorePartialResultException(true);
        EqualsFilter filter = new EqualsFilter("sAMAccountName", username);
        return ldapTemplate.authenticate("", filter.toString(), passWord);
    }

    @Override
    public List<User> ldapSearch(String username) {
        // 忽略PartialResultException, 保证ad服务器在遇到引用问题时候正常返回
        ldapTemplate.setIgnorePartialResultException(true);
        EqualsFilter filter = new EqualsFilter("sAMAccountName", username);
        List<User> userList = ldapTemplate.search("", filter.toString(), (AttributesMapper<User>) attributes -> {
            User user = new User();
            user.setUsername(attributes.get("cn").get().toString());
            user.setEmail(attributes.get("mail").get().toString());
            user.setRealName(attributes.get("description").get().toString());
            return user;
        });
        return userList;
    }
}
