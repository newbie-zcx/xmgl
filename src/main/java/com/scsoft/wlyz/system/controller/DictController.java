package com.scsoft.wlyz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scsoft.wlyz.system.entity.Dict;
import com.scsoft.wlyz.system.service.IDictService;
import com.scsoft.scpt.annotation.SysLog;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.common.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
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
@RequestMapping("/system/dict")
public class DictController extends BaseController {
    private String PREFIX = "module/system/dict/";

    @Autowired
    private IDictService dictService;


    /**
     * 跳转到数据字典首页
     */

    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) {
        return PREFIX + "dict";
    }

    /**
     * 跳转到添加数据字典
     */
    @RequestMapping("/dict_add")

    public String dictAdd(Model model,Integer gid,HttpServletRequest request) {
        model.addAttribute("gid", gid);
        return PREFIX + "dict_add";
    }

    /**
     * 跳转到修改数据字典
     */
    @RequestMapping("/dict_update/{dictId}")

    public String dictUpdate(@PathVariable Integer dictId, Model model, HttpServletRequest request) {
        Dict dict = dictService.getById(dictId);
        model.addAttribute("dict",dict);
        //LogObjectHolder.me().set(dict);
        return PREFIX + "dict";
    }

    /* *
     *
     * 跳转到添加数据字典
     * 通过gid查询
     **/
    @RequestMapping(value = "/dictlist")
    @ResponseBody
    public  PageResult  edit(Integer gid, Integer page,String code, String condition,Integer limit,Model model,HttpServletRequest request) {
        if (page==null){
            page=1;
            limit=10;
        }
        Page<Dict> mypage = new Page<Dict>(page, limit);
        PageResult result = dictService.findbyGidPage(mypage, gid,code,condition);
        return result;
    }
    /**
     * 跳转到添加/修改数据字典
     * 通过gid查询
     */
    @RequestMapping(value = "/dict_edit")
    public String  dictedit(Integer gid, Model model,HttpServletRequest request) {
        model.addAttribute("gid", gid);
        return PREFIX + "dict";
    }

    /*
     * 数据字典详情
     * 通过id查询
     */
    @RequestMapping(value = "/detail/{dictId}")
    @ResponseBody
    public Object detail(@PathVariable("dictId") Integer dictId, Model model,HttpServletRequest request) {
        return dictService.getById(dictId);
    }


    /**
     * 分页获取数据字典列表
     */
    @RequestMapping(value = "/list")

    @ResponseBody
    public PageResult<Dict> list(Integer page, Integer limit, Dict dict,String condition, Model model,HttpServletRequest request) {
        Map<String,Object> map=new HashMap<String,Object>();
        if (page==null){
            page=1;
            limit=10;
        }
        if (StringUtils.isBlank(condition)){
            condition=null;
        }
        Page<Dict> mypage = new Page<Dict>(page, limit);
        PageResult all = dictService.findAll(mypage, condition);

        return all;
    }


    /**
     * 新增数据字典
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @SysLog(operationType="add操作:",operationName="添加数据字典")
    public JsonResult add(Dict dict,Integer gid) {
        if (gid!=null){
        dict.setGid(gid);
        }
        if (dictService.save(dict)) {
            return JsonResult.ok("添加成功");
        } else {
            return JsonResult.error("添加失败");
        }

    }

    /**
     * 删除数据字典
     */
    @RequestMapping(value = "/delete")
    @ResponseBody

    @SysLog(operationType="delete操作:",operationName="删除数据字典")
    public JsonResult delete(@RequestParam int dictid) {

        if (dictService.removeById(dictid)) {
            return JsonResult.ok("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }
    }


    /**
     * 修改数据字典
     */
    @RequestMapping(value = "/update")
    @ResponseBody

    @SysLog(operationType="update操作:",operationName="修改数据字典")
    public JsonResult update(Dict dict) {
        if (dictService.updateById(dict)) {
            return JsonResult.ok("修改成功");
        } else {
            return JsonResult.error("修改失败");
        }
    }


    @RequestMapping("/batchDelete")
    @ResponseBody

    @SysLog(operationType="delete操作:",operationName="批量删除数据字典")
    public JsonResult batchDelete(String departIds) {
        System.out.println(departIds);
        List<String> lis = Arrays.asList(departIds.split(","));
//        if (departService.removeByIds(lis)) {
//            return JsonResult.ok("删除成功");
//        }
        return JsonResult.error("删除失败");
    }

}
