package tw.jdi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SharedUtils {

	public String currentAccount() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * 工具 -> 轉成[Json]格式列印
	 * 
	 * @param T object
	 * @author KYUU
	 */
	public static <T> void printToJson(T object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			System.out.println(jsonStr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 工具 -> 拆解[浮點數]
	 * 
	 * @param Float f
	 * @return Integer[]
	 * @author KYUU
	 */
	public static Integer[] unpackFloat(Float f) {
		if (f == 0) {
			return new Integer[] { 0, 0 };
		}
		String valueStr = String.format("%03.2f", f);
		String[] valueStrArray = valueStr.split("\\.");
		Integer valueIntX = Integer.valueOf(valueStrArray[0]);
		Integer valueIntY = Integer.valueOf(valueIntX < 0 ? "-" + valueStrArray[1] : valueStrArray[1]);
		return new Integer[] { valueIntX, valueIntY };
	}

	/**
	 * 工具 -> 判斷[數值]
	 * 
	 * @param Float valueU
	 * @param Float valueL
	 * @param Float value
	 * @return Boolean
	 * @author KYUU
	 */
	public static Boolean judgeValue(Float valueU, Float valueL, Float value) {
		if (value >= valueU || value <= valueL) {
			return true;
		}
		return false;
	}

	/**
	 * 工具 -> [四則]運算 -> [四捨五入]到[小數點後兩位]
	 * 
	 * @param BigDecimal readValue
	 * @param String     calSign
	 * @param Double     convertMerge
	 * @return Double
	 */
	public static Double convertValue(BigDecimal readValue, String calSign, Double convertMerge) {
		switch (calSign) {
		case "+":
			return readValue.add(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		case "-":
			return readValue.subtract(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		case "*":
			return readValue.multiply(new BigDecimal(convertMerge)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		case "/":
			return readValue.divide(new BigDecimal(convertMerge), 2, RoundingMode.HALF_UP).doubleValue();
		}
		return readValue.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 工具 -> Map[比較器]
	 * 
	 * @return Comparator<String>
	 * @author KYUU
	 */
	public static Comparator<String> mapComparator() {
		return (s1, s2) -> {
			String[] split1 = s1.split(",");
			String[] split2 = s2.split(",");
			int num1 = Integer.parseInt(split1[0]);
			int num2 = Integer.parseInt(split2[0]);
			int numCompare = Integer.compare(num1, num2);
			if (numCompare != 0) {
				return numCompare;
			}
			int nameCompare = split1[1].compareTo(split2[1]);
			if (nameCompare != 0) {
				return nameCompare;
			}
			if (split1.length > 2 && split2.length > 2) {
				return split1[2].compareTo(split2[2]);
			}
			return 0;
		};
	}

	/**
	 * 工具 -> [Set]轉[String]
	 * 
	 * @param Set<?> set
	 * @return String
	 * @author KYUU
	 */
	public static String setToString(Set<?> set) {
		return set.stream().map(Object::toString).collect(Collectors.joining(","));
	}
	
	/**
	 * 獲取 -> Logger
	 * 
	 * @param Class<?> c
	 * @return Logger
	 * @author KYUU
	 */
	public static Logger getLogger(Class<?> c) {
		return LoggerFactory.getLogger(c);
	}

	/**
	 * 工具 -> 取得系統時間
	 * 
	 * @return String system time
	 */
	public static String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

}
