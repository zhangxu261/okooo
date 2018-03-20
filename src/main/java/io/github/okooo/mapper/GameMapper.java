package io.github.okooo.mapper;

import io.github.okooo.domain.Game;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface GameMapper extends Mapper<Game> {

    @Select("select * from game where serial = #{serial} and match_time = #{matchTime}")
    Game findOneBySerialAndMatchTime(@Param("serial") String serial,
                                     @Param("matchTime") String matchTime);

}
