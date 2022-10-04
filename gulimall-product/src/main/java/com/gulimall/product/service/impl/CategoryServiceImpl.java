package com.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.gulimall.product.dao.CategoryDao;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        // 1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 2、组装成树形结构
        // 1）、找到所有一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildren(menu,entities));
            return menu;
        }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());

        return level1Menus;
    }

    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildren(CategoryEntity parent,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(parent.getCatId());
        }).map(categoryEntity -> {
            // 1、找到子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
            // 2、菜单的排序
        }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return children;
    }

}