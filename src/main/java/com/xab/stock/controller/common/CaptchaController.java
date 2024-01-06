package com.xab.stock.controller.common;

import com.google.code.kaptcha.Producer;
import com.xab.stock.config.StockConfig;
import com.xab.stock.domain.AjaxResult;
import com.xab.stock.domain.constant.Constants;
import com.xab.stock.service.ISysConfigService;
import com.xab.stock.utils.RedisCache;
import com.xab.stock.utils.sign.Base64;
import com.xab.stock.utils.uuid.IdUtils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author template
 */
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysConfigService configService;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        //去数据库中查询验证码是否开启
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        ajax.put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = StockConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            //1+1=?@2
            System.out.println("capText = " + capText);
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
