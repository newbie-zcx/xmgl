package com.scsoft.xmgl.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
* 台账-台账内容
**/
@TableName("work_acount_content")
@Data
public class AcountContent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * id
    **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
    * 台账主键id
    **/
    @TableField("acount_id")
    private Integer acoungId;
    /**
    * 台账内容主键id
    **/
    @TableField("content_id")
    private Integer contentId;
}

