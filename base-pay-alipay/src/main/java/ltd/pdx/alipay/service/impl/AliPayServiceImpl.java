package ltd.pdx.alipay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.pdx.alipay.util.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import ltd.pdx.alipay.service.AliPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {

    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.alipayPrivateKey}")
    private String alipayPrivateKey;
    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;
    @Value("${alipay.app.notifyUrl}")
    private String notifyUrl;

    @Override
    public String unifiedOrder(String totalFee, String outTradeNo, String subject) {
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId,
                alipayPrivateKey, "json", "utf-8", alipayPublicKey, "RSA2");
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//		model.setBody("我是测试数据");
        model.setSubject(subject);
        // 采用雪花算法
//        long outTradeNo = SnowflakeIdUtil.getID();
        log.info("商户订单号：" + outTradeNo);
        // 商户唯一订单号
        model.setOutTradeNo(String.valueOf(outTradeNo));
//		model.setTimeoutExpress("30m");
        model.setTotalAmount(totalFee);
//		model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        // 商户外网可以访问的异步地址，异步回调地址

        log.info("aliPayNotifyUrl:[{}]", notifyUrl);
        request.setNotifyUrl(notifyUrl);
        try {
            // 这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            log.info(response.getBody());// 就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String notifyOrder(HttpServletRequest request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>(16);
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        // boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
        // publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "utf-8", "RSA2");
            if (flag) {
                if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
                    // 支付成功 修改订单状态

                    // 处理完业务逻辑之后返回支付宝
                    return "success";// fail
                } else {
                    return "fail";
                }
                // 处理业务逻辑
                // 获取钉钉相关信息，解析params，入库操作等，此接口需要加锁，已经处理的不再处理，此接口可能会重复调用
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean queryOrder(String outTradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId,
                alipayPrivateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        /**
         * 参数传入out_trade_no或trade_no即可，一般传入支付宝的订单号
         */
        bizContent.put("out_trade_no", outTradeNo);
//        bizContent.put("trade_no", "2014112611001004680");
        // bizContent.put("org_pid", "2088101117952222");
        request.setBizContent(JSONObject.toJSONString(bizContent));

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                /**
                 * 解析返回参数，处理业务逻辑
                 */
                log.info("调用成功");
                if ("TRADE_SUCCESS".equals(response.getTradeStatus())) {
                    return true;
                } else {
                    return false;
                }

            } else {
                log.info("调用失败");
                return false;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean refund(String outTradeNo, String outRefundNo,BigDecimal refundAmount) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId,
                alipayPrivateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        /**
         * out_trade_no String 特殊可选 64 订单支付时传入的商户订单号,不能和 trade_no同时为空。 20150320010101001
         * trade_no String 特殊可选 64 支付宝交易号，和商户订单号不能同时为空 2014112611001004680073956707
         * refund_amount
         */
        JSONObject bizContent = new JSONObject();
        /**
         * 参数传入out_trade_no或trade_no即可，一般传入支付宝的订单号,以下为必填字段
         */
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("out_request_no", outRefundNo);
        bizContent.put("refund_amount", refundAmount);
        request.setBizContent(JSONObject.toJSONString(bizContent));
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("{}退款{}成功",outTradeNo,refundAmount);
                return true;
            } else {
                log.info("{}退款{}失败",outTradeNo,refundAmount);
                return false;
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean queryRefund(String orderNo, String refundOrderNo) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId,
                alipayPrivateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

        JSONObject bizContent = new JSONObject();
        /**
         * 参数传入out_trade_no或trade_no即可，一般传入支付宝的订单号,以下为必填字段
         */
        bizContent.put("out_trade_no", orderNo);
        /**
         * 退款交易号
         */
        bizContent.put("out_request_no", refundOrderNo);
        request.setBizContent(JSONObject.toJSONString(bizContent));

        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("订单号:{},退款单号:{},退款成功");
                return true;
            } else {
                log.info("订单号:{},退款单号:{},退款失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
