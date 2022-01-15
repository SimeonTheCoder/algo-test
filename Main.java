using java.util.Scanner;

public class Main {
	public static void main() {
		Scanner scanner = new Scanner(System.in);

		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		int r = 0;
		
		String s = args[2];

		switch(s) {
			case "+" :
				r = a+b;
				break;
			case "-" :
				r = a-b;
				break;
			case "*" :
				r = a*b;
				break;
			case "/" :
				r = a/b;
				break;
		}

		System.out.println(r);
	}
}
