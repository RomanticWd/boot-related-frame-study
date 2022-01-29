package site.lgong.config.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用户接口
 *
 * @author: liudayue
 * @date: 2022-01-29 14:58
 **/
@Service
public class UserService {

    @Value("${person.name}")
    String name;

    @Value("${person.age}")
    Integer age;

    public String getName(){
        return name;
    }

    public Integer getAge(){
        return age;
    }
}
