package com.exer.gulimall.product.service.impl;

import com.exer.gulimall.product.service.CategoryBrandRelationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.product.dao.CategoryDao;
import com.exer.gulimall.product.entity.CategoryEntity;
import com.exer.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    // @Autowired
    // CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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
        // 1.查出所有分类数据
        List<CategoryEntity> list = baseMapper.selectList(null);
        // 2.组装成父子的树形结构
        List<CategoryEntity> level1Menus = list.stream().filter((categoryEntity ->
                categoryEntity.getParentCid() == 0
        )).map((menu) -> {
            menu.setChildren(getChildrens(menu, list));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null?0:menu1.getSort()) - (menu2.getSort() == null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCategoryPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> pathList = findParentPath(catelogId,paths);
        Collections.reverse(pathList);



        return pathList.toArray(new Long[pathList.size()]);
    }

    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        if(StringUtils.hasText(category.getName())){
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }

    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths){
        //1、收集当前节点id的信息
        paths.add(catelogId);
        CategoryEntity category = this.getById(catelogId);
        if(category.getParentCid() != 0){
            findParentPath(category.getParentCid(),paths);
        }
        return paths;
    }

    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
        return children;
    };

}