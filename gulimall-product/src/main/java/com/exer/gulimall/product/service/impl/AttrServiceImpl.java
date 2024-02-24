package com.exer.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.exer.common.constant.ProductConstant;
import com.exer.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.exer.gulimall.product.dao.AttrGroupDao;
import com.exer.gulimall.product.dao.CategoryDao;
import com.exer.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.exer.gulimall.product.entity.AttrGroupEntity;
import com.exer.gulimall.product.entity.CategoryEntity;
import com.exer.gulimall.product.service.CategoryService;
import com.exer.gulimall.product.vo.AttrGroupRelationVo;
import com.exer.gulimall.product.vo.request.AttrVo;
import com.exer.gulimall.product.vo.respons.AttrResponsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.product.dao.AttrDao;
import com.exer.gulimall.product.entity.AttrEntity;
import com.exer.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        //复制保存基本数据
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                && attr.getAttrGroupId() != null) {
            //保存关联关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());//attrEntity保存之后会返回主键
            relationDao.insert(relationEntity);
        }

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> param, Long cateLogId, String type) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrEntity::getAttrType, "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        String key = (String) param.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(item -> wrapper.eq(AttrEntity::getAttrId, key)
                    .or().like(AttrEntity::getAttrName, key));
        }

        if (cateLogId != 0) {
            wrapper.eq(AttrEntity::getCatelogId, cateLogId);
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(param),
                wrapper
        );

        List<AttrEntity> records = page.getRecords();
        List<AttrResponsVo> responsVoList = records.stream().map(attrEntity -> {
            AttrResponsVo attrResponsVo = new AttrResponsVo();
            BeanUtils.copyProperties(attrEntity, attrResponsVo);

            if ("base".equalsIgnoreCase(type)) {
                //两表关联关系： attrEntity&(attrId)&AttrAttrgroupRelationEntity
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                //relationEntity可能为null
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    Long attrGroupId = relationEntity.getAttrGroupId();
                    AttrGroupEntity attrGroup = attrGroupDao.selectById(attrGroupId);
                    if (attrGroup != null)
                        attrResponsVo.setGroupName(attrGroup.getAttrGroupName());
                }
            }

            CategoryEntity category = categoryDao.selectById(attrEntity.getCatelogId());
            attrResponsVo.setCatelogName(category.getName());

            return attrResponsVo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(responsVoList);
        return pageUtils;
    }

    @Override
    public AttrResponsVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrResponsVo responsVo = new AttrResponsVo();
        BeanUtils.copyProperties(attrEntity, responsVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //1、设置分组信息
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
            if (relationEntity != null) {
                responsVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null)
                    responsVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        //1、设置分组信息
        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
        if (relationEntity != null) {
            responsVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null)
                responsVo.setGroupName(attrGroupEntity.getAttrGroupName());
        }

        //2、设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] categoryPath = categoryService.findCategoryPath(catelogId);
        responsVo.setCatelogPath(categoryPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null)
            responsVo.setCatelogName(categoryEntity.getName());

        return responsVo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        //基本修改
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        //修改分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            //先查出该属性有无关联数据
            Long count = relationDao.selectCount(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));

            if (count > 0) {
                //1、修改分组
                relationDao.update(relationEntity, new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrId, relationEntity.getAttrId()));
            } else {
                relationDao.insert(relationEntity);
            }
        }


    }

    /**
     * 根据分组id查找关联的所有属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId));

        List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        if(attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    @Transactional
    public void deleteRelation(AttrGroupRelationVo[] relationVo) {
        List<AttrGroupRelationVo> relationVoList = Arrays.asList(relationVo);
        List<AttrAttrgroupRelationEntity> collect = relationVoList.stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBachRelation(collect);
    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param attrgroupId
     * @param params
     * @return
     */
    @Override
    public PageUtils getNoRealtion(Long attrgroupId, Map<String, Object> params) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1当前分类下的其他分组
        List<AttrGroupEntity> groupList = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId));
        List<Long> attrGroupIds = groupList.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());

        //这些其他分组关联的属性
        List<AttrAttrgroupRelationEntity> otherGroupEntity = relationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds));

        List<Long> attrIds = otherGroupEntity.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        //查询自己分类关联的属性，并且这些属性没有被其他分类关联
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                .eq(AttrEntity::getCatelogId, catelogId)
                .eq(AttrEntity::getAttrType,ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds != null && attrIds.size() > 0)
            wrapper.notIn(AttrEntity::getAttrId, attrIds);

        String key = (String) params.get("key");
        //附带搜索参数
        if(StringUtils.hasText(key)){
            wrapper.and(w ->{
                w.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}