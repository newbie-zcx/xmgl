package com.scsoft.xgsb.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.scsoft.scpt.base.enitity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.util.Date;

/**
 * <p>
 * 附件管理
 * </p>
 *
 * @author zhaopengfei
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_file")
@ApiModel(value="File对象", description="附件管理")
public class FileInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文件用途更具具体用户定义code，如第三方app包插件:thirdparty_apk(第三方apk包)，thirdparty_ipa(第三方ios的包)")
    @TableField("file_purpose")
    private String filePurpose;

    @ApiModelProperty(value = "文件名称")
    @TableField("filename")
    private String filename;

    @ApiModelProperty(value = "文件扩展名 如png apk等不带点")
    @TableField("extname")
    private String extname;

    @ApiModelProperty(value = "文件的相对路径(不含文件名)")
    @TableField("file_path")
    private String filePath;
    @ApiModelProperty(value = "文件的全路径")
    @TableField("full_path")
    private String fullPath;
    @ApiModelProperty(value = "默认是上传文件的文件名(),可自定义 ")
    @TableField("source_name")
    private String sourceName;

    @ApiModelProperty(value = "文件大小")
    @TableField("file_size")
    private String fileSize;

    @ApiModelProperty(value = "文件实际生成的日期")
    @TableField("file_date")
    private Date fileDate;

    @ApiModelProperty(value = "附件描述")
    @TableField("file_desc")
    private String fileDesc;

    @ApiModelProperty(value = "缩略小图文件路径,当文件为图片或视频,保存对应缩略图.此字段缩略小图大小宽或高建议不超过100px.")
    @TableField("thumb_small_file")
    private String thumbSmallFile;

    @ApiModelProperty(value = "缩略中图文件路径,当文件为图片或视频,保存对应缩略图.此字段缩略中图大小宽或高建议不超过400px.")
    @TableField("thumb_medium_file")
    private String thumbMediumFile;

    @ApiModelProperty(value = "媒体文件时长(秒),视频或音频播放的时长（秒）")
    @TableField("media_duration")
    private String mediaDuration;
    @TableField(exist = false)
    private InputStream content;

}
