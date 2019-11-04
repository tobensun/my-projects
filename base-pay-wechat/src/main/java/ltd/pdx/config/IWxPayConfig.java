package ltd.pdx.config;

import lombok.Data;
import ltd.pdx.wxpay.IWXPayDomain;
import ltd.pdx.wxpay.WXPayConfig;
import ltd.pdx.wxpay.WXPayConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class IWxPayConfig implements WXPayConfig { // 继承sdk WXPayConfig 实现sdk中部分抽象方法

    private byte[] certData;

    @Value("${wechatpay.appId}")
    private String appId;

    @Value("${wechatpay.mchKey}")
    private String wxKey;

    @Value("${wechatpay.mchId}")
    private String wxPaychId;

    @Value("${wechatpay.secret}")
    private String secret;

    /**
     * 微信支付回调url-H5
     */
    @Value("${wechatpay.notifyUrl}")
    private String payCallbackUrl;

    /**
     * 微信支付回调url-APP
     */
    @Value("${wechatpay.app.notifyUrl}")
    private String appPayCallbackUrl;

    /**
     * 统一下单url
     */
    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public String getUnifiedOrderUrl() {
        return UNIFIED_ORDER_URL;
    }


    public IWxPayConfig() throws Exception { // 构造方法读取证书, 通过getCertStream 可以使sdk获取到证书
        String certPath = "/root/wxcert/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return wxPaychId;
    }

    @Override
    public String getKey() {
        return wxKey;
    }

    public String getPayCallbackUrl() {
        return payCallbackUrl;
    }

    public String getAppPayCallbackUrl() {
        return appPayCallbackUrl;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public IWXPayDomain getWXPayDomain() { // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
}
