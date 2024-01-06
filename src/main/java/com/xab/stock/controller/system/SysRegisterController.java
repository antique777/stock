package com.xab.stock.controller.system;

import com.xab.stock.config.security.SysRegisterService;
import com.xab.stock.controller.BaseController;
import com.xab.stock.domain.AjaxResult;
import com.xab.stock.domain.model.RegisterBody;
import com.xab.stock.service.ISysConfigService;
import com.xab.stock.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 注册验证
 *
 * @author template
 */
@RestController
public class SysRegisterController extends BaseController {
    @Resource
    private SysRegisterService registerService;

    @Resource
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
