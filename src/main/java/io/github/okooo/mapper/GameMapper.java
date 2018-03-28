package io.github.okooo.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.github.okooo.domain.Game;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface GameMapper extends BaseMapper<Game> {

    @Select("select * from game where serial = #{serial} and match_time = #{matchTime}")
    Game findOneBySerialAndMatchTime(@Param("serial") String serial,
                                     @Param("matchTime") String matchTime);

}
