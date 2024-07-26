import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class SimpleCalculatorTest {
	static SimpleCalculator calc = new SimpleCalculator();
	
	@RunWith(Parameterized.class)
	public static class SumNormalTest {
		public int num1;
		public int num2;
		public int expected;
		
		public SumNormalTest(int num1, int num2, int expected) {
			this.num1 = num1;
			this.num2 = num2;
			this.expected = expected;
		}
		
		@Parameterized.Parameters(name = "Тест{index} : {0} + {1} = {2}")
		public static Iterable<Object[]> dataForTest() {
			return Arrays.asList(new Object[][]{
							{0, 0, 0},
							{5, 0, 5},
							{-5, -5, -10},
							{34, 55, 89},
							{-34, -55, -89}
			});
		}
		
		@Test
		public void testWithParams() {
			assertEquals(expected, calc.sum(num1, num2));
		}
	}
	
	public static class SumExceptionTest {
		@Test(expected = ArithmeticException.class)
		public void sumWithException() {
			calc.sum(Integer.MAX_VALUE, 1);
		}
	}
	
	@RunWith(Parameterized.class)
	public static class DiffNormalTest {
		public int num1;
		public int num2;
		public int expected;
		
		public DiffNormalTest(int num1, int num2, int expected) {
			this.num1 = num1;
			this.num2 = num2;
			this.expected = expected;
		}
		
		@Parameterized.Parameters(name = "Тест{index} : {0} - {1} = {2}")
		public static Iterable<Object[]> dataForTest() {
			return Arrays.asList(new Object[][]{
							{-1, 0, -1},
							{0, -1, 1},
							{-5, 5, -10},
							{-34, -55, 21},
							{55, -34, 89}
			});
		}
		
		@Test
		public void testWithParams() {
			assertEquals(expected, calc.diff(num1, num2));
		}
	}
	
	public static class DiffExceptionTest {
		@Test(expected = ArithmeticException.class)
		public void diffWithException() {
			calc.diff(Integer.MAX_VALUE, -1);
		}
	}
	
	@RunWith(Parameterized.class)
	public static class MultNormalTest {
		public int num1;
		public int num2;
		public int expected;
		
		public MultNormalTest(int num1, int num2, int expected) {
			this.num1 = num1;
			this.num2 = num2;
			this.expected = expected;
		}
		
		@Parameterized.Parameters(name = "Тест{index} : {0} * {1} = {2}")
		public static Iterable<Object[]> dataForTest() {
			return Arrays.asList(new Object[][]{
							{-1, 0, 0},
							{-1, -1, 1},
							{-5, 5, -25},
							{Integer.MIN_VALUE, 1, Integer.MIN_VALUE},
							{Integer.MAX_VALUE, -1, Integer.MIN_VALUE + 1}
			});
		}
		
		@Test
		public void testWithParams() {
			assertEquals(expected, calc.mult(num1, num2));
		}
	}
	
	public static class MultExceptionTest {
		@Test(expected = ArithmeticException.class)
		public void multWithException() {
			calc.mult(Integer.MAX_VALUE, 2);
		}
	}
	
	@RunWith(Parameterized.class)
	public static class DivNormalTest {
		public int num1;
		public int num2;
		public int expected;
		
		public DivNormalTest(int num1, int num2, int expected) {
			this.num1 = num1;
			this.num2 = num2;
			this.expected = expected;
		}
		
		@Parameterized.Parameters(name = "Тест{index} : {0} / {1} = {2}")
		public static Iterable<Object[]> dataForTest() {
			return Arrays.asList(new Object[][]{
							{-1, 1, -1},
							{-1, -1, 1},
							{-1, 1000, 0},
							{0, 1, 0}
			});
		}
		
		@Test
		public void testWithParams() {
			assertEquals(expected, calc.div(num1, num2));
		}
	}
	
	public static class DivExceptionTest {
		@Test(expected = ArithmeticException.class)
		public void divWithOverflow() {
			calc.div(Integer.MIN_VALUE, -1);
		}
		
		@Test(expected = ArithmeticException.class)
		public void divWithZero() {
			calc.div(1, 0);
		}
	}
	
	@AfterClass
	public static void finish() {
		calc = null;
	}
}

