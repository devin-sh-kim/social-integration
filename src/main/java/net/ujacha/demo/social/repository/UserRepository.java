package net.ujacha.demo.social.repository;

import net.ujacha.demo.social.vo.RepositoryParam;
import net.ujacha.demo.social.vo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository {

    @Select("SELECT COUNT(id) FROM user WHERE email = #{email}" )
    int countByEmail(RepositoryParam param);

    @Select("SELECT * FROM user")
    List<User> selectAllUsers(RepositoryParam param);

    @Select("SELECT * FROM user WHERE email = #{email} LIMIT 1")
    User selectUserByEmail(RepositoryParam param);

    @Select("SELECT * FROM user WHERE id = #{id} LIMIT 1")
    User selectUserById(RepositoryParam param);

    @Insert(value = "INSERT INTO USER VALUES(NEXT VALUE FOR SEQ, " +
            "#{payload.email}," +
            "#{encPassword}," +
            "#{payload.name}," +
            "#{payload.gender}," +
            "#{payload.age}," +
            "#{payload.mobile}," +
            "CURRENT_TIMESTAMP," +
            "0," +
            "CURRENT_TIMESTAMP," +
            "0" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(RepositoryParam param);
}
