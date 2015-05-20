package expression;

/**
 *  This class is created to fix the precision bug existing in double format.
 *  @author Zhenge Jia 2015
 */
import java.math.BigDecimal;

public class Arith{
	
	private static final int DEF_DIV_SCALE = 10;
	
	private Arith(){
	}

	/**
	 * Provide the way for the correct precision of addition.
	 * @param v1 
	 * @param v2 
	 * @return sum
	 */
	public static double add(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	/**
	 * Provide the way for the correct precision of subtraction.
	 * @param v1 
	 * @param v2 
	 * @return difference
	 */
	public static double sub(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	/**
	 * Provide the way for the correct precision of multiplication.
	 * @param v1 
	 * @param v2 
	 * @return product
	 */
	public static double mul(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * Provide the way for the correct precision of division.
	 * @param v1 
	 * @param v2 
	 * @return quotient
	 */
	public static double div(double v1,double v2){
		return div(v1,v2,DEF_DIV_SCALE);
	}

	/**
	 * Provide the way for the correct precision of division.
	 * @param v1 
	 * @param v2 
	 * @param scale (precise to number of point)
	 * @return quotient
	 */
	public static double div(double v1,double v2,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * Round double number
	 * @param v rounded number
	 * @param scale 
	 * @return result
	 */
	public static double round(double v,int scale){

		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
};
