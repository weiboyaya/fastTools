package com.free.fasttools.dao.custom;

import com.free.fasttools.po.custom.SuggestPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/6 15:15
 * @Description: 咨询与建议DAO
 */
@Mapper
@Repository
public interface SuggestDAO {

    @Select("SELECT id ,email ,remark ,status ,created  FROM ft_suggest WHERE email=#{email}AND remark=#{remark}")
    public List<SuggestPO> querySuggest(@Param(value="email")String email,
                                        @Param(value="remark")String remark);

    @Insert("INSERT INTO  ft_suggest(email,remark,created) values ( #{email}, #{remark}, #{created}  )")
    public int save(@Param(value="email")String email,
                    @Param(value="remark")String remark,
                    @Param(value="created")String created);
}
