abstract class A{
	abstract void circle();
	abstract void reactangle();
	abstract void square();
	
}
class B extends A{
	void circle(){
		int r=10;
		double p;
		p=(3.14*r*r);
		System.out.println("Circle is"+p);
	}
	void reactangle()
	{
		int L=5;
		int B=7;
		int c;
		c=L*B;
		System.out.println("Rectangle is"+c);
		
	}
	void square()
	{
		int S=5;
		int s;
		s=S*S;
		System.out.println("Square is"+s);
		
	}
}
public class abstractdemo1{
	public static void main(String args[])
	{
		B b1=new B();
		b1.circle();
		b1.reactangle();
		b1.square();
	}
}