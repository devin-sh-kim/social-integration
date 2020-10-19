package net.ujacha.demo.social.repository;

import net.ujacha.demo.social.vo.RepositoryParam;
import net.ujacha.demo.social.vo.UserSocial;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserSocialRepository {
    @Select("SELECT * FROM user_social WHERE social = #{social} AND social_account_id = #{socialAccountId} LIMIT 1")
    UserSocial selectUserSocial(RepositoryParam param);

    @Insert(value = "INSERT INTO user_social VALUES(" +
            "#{userId}," +
            "#{social}," +
            "#{socialAccountId}," +
            "#{accessToken}," +
            "#{refreshToken}," +
            "CURRENT_TIMESTAMP," +
            "0," +
            "CURRENT_TIMESTAMP," +
            "0" +
            ")")
    int insertUserSocial(RepositoryParam param);
}
