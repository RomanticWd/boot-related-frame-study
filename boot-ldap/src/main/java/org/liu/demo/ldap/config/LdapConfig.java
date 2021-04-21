package org.liu.demo.ldap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapConfig {

    @Value("${spring.ldap.urls}")
    private String ldapUrl;
    @Value("${spring.ldap.username}")
    private String userName;
    @Value("${spring.ldap.password}")
    private String passWord;
    @Value("${spring.ldap.base}")
    private String base;

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setBase(base);
        source.setUrl(ldapUrl);
        source.setPassword(passWord);
        source.setUserDn(userName);
        return source;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }

}
