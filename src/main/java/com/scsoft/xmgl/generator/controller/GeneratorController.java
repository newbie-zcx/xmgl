package com.scsoft.xmgl.generator.controller;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.scsoft.xmgl.generator.engine.base.GeneratorConfig;
import com.scsoft.xmgl.generator.entity.TableInfo;
import com.scsoft.xmgl.generator.modle.GenConfig;
import com.scsoft.xmgl.generator.service.IGeneratorService;
import com.scsoft.xmgl.generator.utils.Query;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 * @author zhaopengfei
 * @CreateDate 2019-04-26
 * @copyright: 雪城软件有限公司
 * All rights Reserved，Designed By Scsoft
 * @Version: 1.0
 */
@Controller
@RequestMapping("/genCode")
public class GeneratorController extends BaseController {
    private String PREFIX = "module/generator/";
    @Autowired
    private IGeneratorService generatorService;
    /**
     * 跳转到代码生成首页
     */
    @RequestMapping("")
    public String generator(Model model, HttpServletRequest request) {
        System.out.println("进入代码生成页面");
        model.addAttribute("projectPath",System.getProperty("user.dir"));

        return PREFIX + "gen";
    }
    /**
     * 获取所有表信息
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public PageResult<TableInfo> list(@RequestParam Map<String, Object> params) {
        return generatorService.queryList(new Query(params));
    }
    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        byte[] data = generatorService.generatorCode(tables.split(","));

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"renren.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

//        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 生成代码
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult generate(GenConfig genConfig) {
        GeneratorConfig webGeneratorConfig = new GeneratorConfig(genConfig);
        try {
            webGeneratorConfig.doMpGeneration();
        }catch (Exception e) {
            return JsonResult.error("代码生成失败");
        }
        return JsonResult.ok("代码生成成功");
    }


    /**
     * 获取当前数据库所有的表信息
     */
    public List<Map<String, Object>> getAllTables(String dbName) {
        String sql = "select TABLE_NAME as tableName,TABLE_COMMENT as tableComment from information_schema.`TABLES` where TABLE_SCHEMA = '" + dbName + "'";
        return SqlRunner.db().selectList(sql);
    }
}
