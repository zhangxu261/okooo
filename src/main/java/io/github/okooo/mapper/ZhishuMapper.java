package io.github.okooo.mapper;

import io.github.okooo.domain.Zhishu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ZhishuMapper extends Mapper<Zhishu> {

    Zhishu findBySerialAndMatchTime(@Param("serial") String serial,
                                    @Param("matchTime") String matchTime);

}