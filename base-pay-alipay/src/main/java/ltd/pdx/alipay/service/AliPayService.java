package ltd.pdx.alipay.service;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface AliPayService {
	/**
	 * APP支付下单签名
	 */
	String unifiedOrder(String totalFee, String outTradeNo, String subject);
	
	/**
	 * 支付宝支付结果回调
	 */
	String notifyOrder(HttpServletRequest request);
	
	/**
	 * 交易查询
	 */
	boolean queryOrder(String outTradeNo);
	
	/**
	 * 交易退款
	 */
	boolean refund(String orderNum, String outRefundNo,BigDecimal refundAmount);
	
	/**
	 * 交易退款查询
	 * @param orderNo 订单号
	 * @param refundOrderNo 退款订单号
	 */
	boolean queryRefund(String orderNo, String refundOrderNo);
}
