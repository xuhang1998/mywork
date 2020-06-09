package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.File;
import org.apache.ibatis.annotations.Param;

public interface FileDao extends BaseMapper<File> {
     int saveFile(@Param("info") File file);
}
