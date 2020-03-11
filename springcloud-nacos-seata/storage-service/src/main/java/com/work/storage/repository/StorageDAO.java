package com.work.storage.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.storage.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StorageDAO extends BaseMapper<Storage> {

}
