package com.gulimall.member.dao;

import com.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author Oblivion
 * @email 934752370@qq.com
 * @date 2022-10-01 09:26:02
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
