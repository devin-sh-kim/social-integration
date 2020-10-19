package net.ujacha.demo.social.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ujacha.demo.social.enums.Social;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {

    public SocialAccount(Social social){
        this.social = social;
    }

    private Social social;
    private String id;
    private String email;
    private String name;
    private String mobile;
    private String age;
    private String gender;


}
