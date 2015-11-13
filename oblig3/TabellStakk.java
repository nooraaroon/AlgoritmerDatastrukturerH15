package oblig3;

import java.util.*;

public class TabellStakk<T> implements Stakk<T>
{
	private T[] a;                    
	private int antall;             

	public TabellStakk()              
	{
		this(8);
	}

	@SuppressWarnings("unchecked")
	public TabellStakk(int lengde) 
	{
		if(lengde < 0)
			throw new IllegalArgumentException("Negativ tabellengde!");

		a = (T[])new Object[lengde];
		antall = 0;                  
	}

	public void leggInn(T t)
	{
		if(antall == a.length)
			a = Arrays.copyOf(a, antall == 0 ? 1 : 2*antall);

		a[antall++] = t;
	}

	public T kikk()
	{
		if(antall == 0)
			throw new NoSuchElementException("Stakken er tom!");

		return a[antall-1];
	}

	public T taUt()
	{
		if(antall == 0)
			throw new NoSuchElementException("Stakken er tom!");

		antall--;

		T temp = a[antall];
		a[antall] = null;     

		return temp;      
	}

	public boolean tom() {  return antall == 0; }

	public int antall() {  return antall; }

	public void nullstill()
	{
		for(int i = 0; i < antall; i++) 
			a[i] = null;
		antall = 0;
	}

	public String toString()
	{
		if (antall == 0) 
			return "[]";

		StringBuilder s = new StringBuilder();
		s.append('[').append(a[antall-1]);

		for(int i = antall-2; i >= 0; i--)
		{
			s.append(',').append(' ').append(a[i]);
		}
		s.append(']');

		return s.toString();
	}
}
