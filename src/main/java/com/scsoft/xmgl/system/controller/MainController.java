package com.scsoft.xmgl.system.controller;

import com.scsoft.xmgl.common.handler.SystemCommonHandler;
import com.scsoft.xmgl.common.redis.RedisUtil;
import com.scsoft.xmgl.syslog.entity.SysLoginRecord;
import com.scsoft.xmgl.syslog.service.ISysLoginRecordService;
import com.scsoft.xmgl.system.entity.Authority;
import com.scsoft.xmgl.system.entity.Depart;
import com.scsoft.xmgl.system.entity.Role;
import com.scsoft.xmgl.system.entity.User;
import com.scsoft.xmgl.system.service.IAuthorityService;
import com.scsoft.xmgl.system.service.IDepartService;
import com.scsoft.xmgl.system.service.IRoleService;
import com.scsoft.scpt.base.controller.BaseController;
import com.scsoft.scpt.common.JsonResult;
import com.scsoft.scpt.constant.ShiroConstants;
import com.scsoft.scpt.constant.UserConstants;
import com.scsoft.scpt.utils.StringUtils;
import com.scsoft.scpt.utils.UserAgentGetter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author: Zhaopengfei
 * @copyright: 雪城软件有限公司
 * @CreatedDate: 2019年03月15日 15:22
 * @Copyright: All rights Reserved，Designed By Scsoft
 */
@Controller
public class MainController extends BaseController implements ErrorController {
    @Autowired
    private IAuthorityService authoritiesService;
    @Autowired
    private ISysLoginRecordService loginRecordService;
    @Autowired
    private IDepartService departService;
    @Autowired
    private IRoleService roleService;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 主页
     */
    @RequestMapping({"/", "/index"})
    public String index(Model model, HttpServletRequest request,HttpSession session) {
        if(!JudgeIsMoblie(request)){
            List<Authority> authorities = authoritiesService.listByUserId(SystemCommonHandler.getLoginUserId());
            List<Map<String, Object>> menuTree = getMenuTree(authorities, -1);
            model.addAttribute("menus", menuTree);
            model.addAttribute("loginUser", SystemCommonHandler.getLoginUser());
            request.getSession().setAttribute("loginUser", SystemCommonHandler.getLoginUser());
        }else {
            return "redirect:/yqsb/detail/detailadd";
        }

        return "index";
    }

    /**
     * 登录页
     */
    @RequestMapping("/login")
    public String login() {
        if (SystemCommonHandler.getLoginUser() != null) {
            return "redirect:index";
        }
        return "login";
    }

    /**
     * 登录
     */
    @ResponseBody
    @PostMapping("/login")
    public JsonResult doLogin(String username, String password,String rememe, HttpServletRequest request) {
        if ("LOCK".equals(redisUtil.get(UserConstants.SHIRO_IS_LOCK+username))){
            return JsonResult.error("该用户尝试次数过多，"+ redisUtil.getExpire(UserConstants.SHIRO_IS_LOCK+username)+"分钟后再试");
        }
        if (StringUtils.isBlank(username, password)) {
            return JsonResult.error("账号密码不能为空");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            if (rememe!=null&&rememe.equals("on")) {
                token.setRememberMe(true);
            }
            SecurityUtils.getSubject().login(token);
            addLoginRecord(SystemCommonHandler.getLoginUser(), request);

            addRedisCache();
            return JsonResult.ok("登录成功");
        } catch (IncorrectCredentialsException ice) {
            String count=redisUtil.get(UserConstants.SHIRO_LOGIN_COUNT+username).toString();
            return JsonResult.error("密码错误！您还有"+(5-Integer.parseInt(count))+"次机会");
        } catch (UnknownAccountException uae) {
            return JsonResult.error("账号不存在");
        } catch (LockedAccountException e) {
            return JsonResult.error("账号被锁定");
        } catch (DisabledAccountException e){
            return JsonResult.error("用户被禁用");
        }catch (ExcessiveAttemptsException eae) {
                return JsonResult.error("操作频繁，请稍后再试"+eae.getMessage());
            }
    }


    /**
     * 登录页
     */
    @RequestMapping("/logout")
    public String logout( HttpServletRequest request,  HttpServletResponse response) throws IOException {
        SecurityUtils.getSubject().logout();
        response.sendRedirect(request.getContextPath() + "login");
        return "login";
    }
    /**
     * iframe页
     */
    @RequestMapping("/iframe")
    public String iframe(String url, Model model) {
        model.addAttribute("url", url);
        return "tpl/iframe";
    }
  /**
     * 错误页
     */
    @RequestMapping("/error")
    public String runerror(String code) {
        if ("403".equals(code)) {
            return "error/403";
        }
        if ("404".equals(code)) {
            return "error/404";
        }
        return "error/500";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * 递归转化树形菜单
     */
    private List<Map<String, Object>> getMenuTree(List<Authority> authorities, int parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < authorities.size(); i++) {
            Authority temp = authorities.get(i);
            if (temp.getType().equals(0) && parentId == temp.getParentId()) {
                Map<String, Object> map = new HashMap<>();
                map.put("menuName", temp.getName());
                map.put("menuIcon", temp.getMenuIcon());
                map.put("menuUrl", StringUtils.isBlank(temp.getUrl()) ? "javascript:;" : temp.getUrl());
                map.put("subMenus", getMenuTree(authorities, authorities.get(i).getId()));
                list.add(map);
            }
        }
        return list;
    }
    private void addRedisCache() {
        User user=SystemCommonHandler.getLoginUser();
        getSession().setAttribute(ShiroConstants.CURRENT_USER,user);
        List<Role> roleList=roleService.getByUserId(user.getId());
        List<Depart> departList=departService.getByUserId(user.getId());
        redisUtil.set("currentUser:"+user.getId()+":user", user);
        redisUtil.set("currentUser:"+user.getId()+":role", roleList);
        redisUtil.set("currentUser:"+user.getId()+":depart", departList);
    }
    /**
     * 添加登录日志
     */
    private void addLoginRecord(User user, HttpServletRequest request) {
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        // 添加到登录日志
        SysLoginRecord loginRecord = new SysLoginRecord();
        loginRecord.setUserId(user.getId());
        loginRecord.setOsName(agentGetter.getOS());
        loginRecord.setDevice(agentGetter.getDevice());
        loginRecord.setBrowserType(agentGetter.getBrowser());
        loginRecord.setIpAddress(agentGetter.getIpAddr());
        loginRecord.setCreateTime(new Date());
        loginRecord.setUserName(user.getUserName());
        loginRecord.setNickName(user.getRealName());
        loginRecordService.save(loginRecord);
    }


    /**
     * 控制台
     */
    @RequestMapping("/console")
    public String console() {
        return "tpl/console";
    }

    /**
     * 消息弹窗
     */
    @RequestMapping("/tpl/message")
    public String message() {
        return "tpl/message";
    }

    /**
     * 修改密码弹窗
     */
    @RequestMapping("/tpl/password")
    public String password() {
        return "tpl/password";
    }

    /**
     * 主题设置弹窗
     */
    @RequestMapping("/tpl/theme")
    public String theme() {
        return "tpl/theme";
    }



    public boolean JudgeIsMoblie(HttpServletRequest request) {
        boolean isMoblie = false;
        String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
                "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
                "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
                "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
                "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
                "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
                "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
                "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
                "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
                "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile" };
        if (request.getHeader("User-Agent") != null) {
            for (String mobileAgent : mobileAgents) {
                if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
                    isMoblie = true;
                    break;
                }
            }
        }
        return isMoblie;
    }

}
