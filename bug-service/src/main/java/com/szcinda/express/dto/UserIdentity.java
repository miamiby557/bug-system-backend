package com.szcinda.express.dto;

import com.szcinda.express.persistence.UserType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserIdentity implements Serializable {
    private String id;
    private String account;
    private String password;
    private String token;
    private String name;
    private String userType;
    private List<String> permissions = new ArrayList<>();

    public UserIdentity(String id, String account, String password,String name,UserType userType) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        if(UserType.ADMIN.equals(userType)){
            this.userType = "管理员";
        }else if(UserType.DEVELOPER.equals(userType)){
            this.userType = "研发人员";
        }else if(UserType.MANAGER.equals(userType)){
            this.userType = "研发主管";
        }else if(UserType.DEMAND_MANAGER.equals(userType)){
            this.userType = "需求主管";
        }else if(UserType.DEMANDER.equals(userType)){
            this.userType = "需求人员";
        }else if(UserType.CONFIGUE_MANAGER.equals(userType)){
            this.userType = "交付主管";
        }else if(UserType.CONFIGUER.equals(userType)){
            this.userType = "交付人员";
        }else if(UserType.PROGRAM_MANAGER.equals(userType)){
            this.userType = "项目经理";
        }
    }
}
