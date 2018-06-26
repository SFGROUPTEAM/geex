package com.hy.common;

import java.math.BigDecimal;

/**
 * @company 申孚网络科技有限公司
 * @project SF_new_system
 * @author	lijianguo
 * @Date	2015年6月5日 
 * @version V1.0   
 * @Copyright 2015申孚公司-版权所有
 * 
 * */
public class AmountUtil {

	/** 金额为分的格式 */
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	/**
	 * 将分为单位的转换为元并返回金额格式的字符串 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(Long amount) throws Exception {
		if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}

		int flag = 0;
		String amString = amount.toString();
		if (amString.charAt(0) == '-') {
			flag = 1;
			amString = amString.substring(1);
		}
		StringBuffer result = new StringBuffer();
		if (amString.length() == 1) {
			result.append("0.0").append(amString);
		} else if (amString.length() == 2) {
			result.append("0.").append(amString);
		} else {
			String intString = amString.substring(0, amString.length() - 2);
			for (int i = 1; i <= intString.length(); i++) {
				if ((i - 1) % 3 == 0 && i != 1) {
					result.append(",");
				}
				result.append(intString.substring(intString.length() - i,
						intString.length() - i + 1));
			}
			result.reverse().append(".")
					.append(amString.substring(amString.length() - 2));
		}
		if (flag == 1) {
			return "-" + result.toString();
		} else {
			return result.toString();
		}
	}

	/**
	 * 将分为单位的转换为元 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(String amount) throws Exception {
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}
		return BigDecimal.valueOf(Long.valueOf(amount))
				.divide(new BigDecimal(100)).toString();
	}

	/**
	 * 将元为单位的转换为分 （乘100）
	 * 
	 * @param amount
	 * @return
	 */
	public static String changeY2F(Long amount) {
		return BigDecimal.valueOf(amount).multiply(new BigDecimal(100))
				.toString();
	}

	/**
	 * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
	 * 
	 * @param amount
	 * @return
	 */
	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}
	
			
      public static String add(String s1, String s2)
		{        // 进行加法运算
		         BigDecimal b1 = new BigDecimal(s1);
		         BigDecimal b2 = new BigDecimal(s2);
		        return b1.add(b2).toString();
		     }
      public static String sub(String s1, String s2)
		{        // 进行减法运算
		         BigDecimal b1 = new BigDecimal(s1);
		         BigDecimal b2 = new BigDecimal(s2);
		        return b1.subtract(b2).toString();
		     }
	  public static double mul(double d1, double d2)
		{  // 进行乘法运算
		         BigDecimal b1 = new BigDecimal(d1);
		         BigDecimal b2 = new BigDecimal(d2);
		        return b1.multiply(b2).doubleValue();
		     }
		    public static double div(double d1,
		double d2,int len) {// 进行除法运算
		         BigDecimal b1 = new BigDecimal(d1);
		         BigDecimal b2 = new BigDecimal(d2);
		        return b1.divide(b2,len,BigDecimal.
		   ROUND_HALF_UP).doubleValue();
  }

}
