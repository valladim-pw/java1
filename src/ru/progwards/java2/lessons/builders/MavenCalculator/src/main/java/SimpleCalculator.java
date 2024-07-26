
public class SimpleCalculator {
	public int sum(int a, int b) {
		long longRes = (long) a + b;
		if (longRes > Integer.MAX_VALUE || longRes < Integer.MIN_VALUE)
			throw new ArithmeticException("Переполнение диапазона int при сложении " + longRes);
		return a + b;
	}
	
	public int diff(int a, int b) {
		long longRes = (long) a - b;
		if (longRes > Integer.MAX_VALUE || longRes < Integer.MIN_VALUE)
			throw new ArithmeticException("Переполнение диапазона int при вычитании " + longRes);
		return a - b;
	}
	
	public int mult(int a, int b) {
		long longRes = (long) a * b;
		if (longRes > Integer.MAX_VALUE || longRes < Integer.MIN_VALUE)
			throw new ArithmeticException("Переполнение диапазона int при умножении " + longRes);
		return a * b;
	}
	
	public int div(int a, int b) {
		long longRes = (long) a * b;
		if (longRes > Integer.MAX_VALUE || longRes < Integer.MIN_VALUE)
			throw new ArithmeticException("Переполнение диапазона int при делени " + longRes);
		if (b == 0)
			throw new ArithmeticException("Произошла недопустимая арифметическая операция " + a + "/" + b);
		return a / b;
	}
	
	public static void main(String[] args) {
		SimpleCalculator calc = new SimpleCalculator();
		System.out.println(calc.sum(12, 13));
		System.out.println(calc.diff(75, 50));
		System.out.println(calc.mult(5, 5));
		System.out.println(calc.div(100, 4));
	}
}