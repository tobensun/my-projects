package ltd.pdx.service.impl;

import lombok.extern.slf4j.Slf4j;
import ltd.pdx.config.IWxPayConfig;
import ltd.pdx.service.WeChatPayService;
import ltd.pdx.util.HttpUtils;
import ltd.pdx.wxpay.WXPay;
import ltd.pdx.wxpay.WXPayConstants;
import ltd.pdx.wxpay.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
@Slf4j
public class WeChatPayServiceImpl implements WeChatPayService {
    @Autowired
    IWxPayConfig wxPayConfig;

    @Value("${wechatpay.notifyUrl}")
    private static String notifyUrl;

    /**
     * 微信下单接口-H5
     *
     * @param openId   微信openid
     * @param totalFee 费用,单位分
     * @param orderNum 订单号
     * @return
     */
    @Override
    public Map<String, String> unifiedOrder(String openId, Long totalFee, String orderNum) {
        /**
         * openId=oV6Tj6V0JxxJPD7vO75YQkyjRXFs
         */
        try {
            SortedMap<String, String> params = new TreeMap<>();
            params.put("appid", wxPayConfig.getAppID());
            params.put("mch_id", wxPayConfig.getMchID());
            //随机字符串
            params.put("nonce_str", WXPayUtil.generateNonceStr());
            //商品描述
            String message = "会员购买";
            params.put("body", message);
            //商户订单号,商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
            params.put("out_trade_no", orderNum);
            //总金额,单位分
            params.put("total_fee", totalFee.toString());
            //通知地址
            params.put("notify_url", wxPayConfig.getPayCallbackUrl());
            //native方式 params.put("trade_type", "NATIVE");
            params.put("trade_type", "JSAPI");
            params.put("openid", openId);


            //4.2、sign签名 具体规则:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_3
            String sign = WXPayUtil.generateSignature(params, wxPayConfig.getKey());
            params.put("sign", sign);

            //4.3、map转xml （ WXPayUtil工具类）
            String payXml = WXPayUtil.mapToXml(params);

            //4.4、回调微信的统一下单接口(HttpUtil工具类）
            String orderStr = HttpUtils.doPost(wxPayConfig.getUnifiedOrderUrl(), payXml, 4000);

            //4.5、xml转map （WXPayUtil工具类）
            Map<String, String> unifiedOrderMap = WXPayUtil.xmlToMap(orderStr);
            return unifiedOrderMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信下单接口-APP
     *
     * @param totalFee 费用,单位分
     * @param orderNum 订单号
     * @param message  商品描述 会员开通 房间预定
     * @return
     */
    @Override
    public Map<String, String> appUnifiedOrder(Long totalFee, String orderNum, String message) {
        /**
         * openId=oV6Tj6V0JxxJPD7vO75YQkyjRXFs
         */
        try {
            SortedMap<String, String> params = new TreeMap<>();
            params.put("appid", wxPayConfig.getAppID());
            params.put("mch_id", wxPayConfig.getMchID());

            String nonceStr = WXPayUtil.generateNonceStr();

            //随机字符串
            params.put("nonce_str", nonceStr);
            //商品描述
            //String message = "会员购买";
            params.put("body", message);
            //商户订单号,商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
            params.put("out_trade_no", orderNum);
            //总金额,单位分
            params.put("total_fee", totalFee.toString());
            //通知地址
            params.put("notify_url", wxPayConfig.getAppPayCallbackUrl());
            //native方式 params.put("trade_type", "NATIVE");
            params.put("trade_type", "APP");

            WXPay wxPay = new WXPay(wxPayConfig, notifyUrl);
//
            Map<String, String> unifiedOrderMap = wxPay.unifiedOrder(params);
            log.info(unifiedOrderMap.toString());
            String prepayId = unifiedOrderMap.get("prepay_id");
            String appId = wxPayConfig.getAppID();
            String key = wxPayConfig.getKey();
            String packages = "Sign=WXPay";
            long timestamp = WXPayUtil.getCurrentTimestampMs() / 1000;
            Map<String, String> map = new HashMap<String, String>(16);
            //SortedMap<String, String> map = new TreeMap<>();
            map.put("appid", appId);
            map.put("partnerid", wxPayConfig.getMchID());
            map.put("prepayid", prepayId);
            map.put("package", packages);
            map.put("noncestr", nonceStr);
            map.put("timestamp", String.valueOf(timestamp));

            String sign1 = WXPayUtil.generateSignature(map, key, WXPayConstants.SignType.HMACSHA256);
            log.info(nonceStr);
            log.info("prepayId:" + prepayId);
            log.info("sign:" + sign1);
            log.info("timestamp:" + timestamp);
            map.put("sign", sign1);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean refund(String outTradeNo,String outRefundNo,Long totalFee,Long refundFee) {
        try {
            WXPay wxPay = new WXPay(wxPayConfig);
            Map<String, String> params = new HashMap();
            //商户订单号,商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
            params.put("out_trade_no", outTradeNo);
            //退款单号-商户自己生成的，系统唯一
            log.info("商户退款单号："+outRefundNo);
            params.put("out_refund_no", outRefundNo);
            //订单金额
            params.put("total_fee", totalFee.toString());
            //退款金额
            params.put("refund_fee", refundFee.toString());
            Map<String, String> unifiedOrderMap = wxPay.refund(params);
            log.info(unifiedOrderMap.toString());
            //请求成功，相当于http请求返回200
            if("SUCCESS".equals(unifiedOrderMap.get("return_code"))) {
                //SUCCESS 退款申请接收成功
                if("SUCCESS".equals(unifiedOrderMap.get("result_code"))) {
                    log.info("退款申请成功");
                    //解析字符串，获取微信退款单号 refund_id
                    String refundId = unifiedOrderMap.get("refund_id");
                    log.info("微信退款单号，refundId:"+refundId);
                    return true;
                }
            }else {
                return false;
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public boolean queryRefund(String outRefundNo) {
        try {
            WXPay wxPay = new WXPay(wxPayConfig);
            Map<String, String> params = new HashMap();
            //退款单号-商户自己生成的，系统唯一
            log.info("商户退款单号："+outRefundNo);
            params.put("out_refund_no", outRefundNo);
            Map<String, String> resultMap = wxPay.refundQuery(params);
            log.info(resultMap.toString());
            //请求成功，相当于http请求返回200
            if("SUCCESS".equals(resultMap.get("return_code"))) {
                //SUCCESS 退款申请接收成功
                if("SUCCESS".equals(resultMap.get("result_code"))) {
                    log.info("退款申请成功");
                    //解析字符串，获取微信退款单号 refund_id
                    String refundId = resultMap.get("refund_id");
                    log.info("微信退款单号，refundId:"+refundId);
                    return true;
                }
            }else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
