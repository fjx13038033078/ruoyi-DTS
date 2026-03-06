package com.ruoyi.ticket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import lombok.Data;

/**
 * 支付宝沙箱配置
 * 从 application.yml 中 alipay.* 读取配置
 *
 * @author ruoyi
 */
@Configuration
public class AlipayConfig {

    @Bean
    @ConfigurationProperties(prefix = "alipay")
    public AlipayProperties alipayProperties() {
        return new AlipayProperties();
    }

    @Bean
    public AlipayClient alipayClient(AlipayProperties props) {
        return new DefaultAlipayClient(
            props.getGatewayUrl(),
            props.getAppId(),
            props.getAppPrivateKey(),
            "json",
            props.getCharset(),
            props.getAlipayPublicKey(),
            props.getSignType()
        );
    }

    /**
     * 支付宝配置属性
     */
    @Data
    public static class AlipayProperties {
        /** 应用ID */
        private String appId;
        /** 应用私钥（RSA2格式） */
        private String appPrivateKey;
        /** 支付宝公钥 */
        private String alipayPublicKey;
        /** 网关地址 */
        private String gatewayUrl;
        /** 异步通知地址 */
        private String notifyUrl;
        /** 同步跳转地址 */
        private String returnUrl;
        /** 签名类型 */
        private String signType = "RSA2";
        /** 字符编码 */
        private String charset = "utf-8";
    }
}
