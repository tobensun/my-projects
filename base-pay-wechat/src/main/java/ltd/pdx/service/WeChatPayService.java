package ltd.pdx.service;

import java.util.Map;


public interface WeChatPayService {

    Map<String, String> unifiedOrder(String openId, Long totalFee, String orderNum);

    Map<String, String> appUnifiedOrder( Long totalFee, String orderNum, String message);

    /**
     * 退款
     * @param outTradeNo 订单号
     * @param outRefundNo 退款订单号
     * @param totalFee 总费用
     * @param refundFee 退款费用
     */
    boolean refund(String outTradeNo,String outRefundNo,Long totalFee,Long refundFee);

    boolean queryRefund(String outRefundNo);
}
