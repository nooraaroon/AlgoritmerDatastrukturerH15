package algoritmer;

import java.util.Arrays;
import java.util.NoSuchElementException;

/*
*Av: Noora Aroon
*/

public class Oblig1 extends Test
{
	//oppg1

	public static int maks(int[] a)
	{
		int i=0;

		if (a.length < 1)
		      throw new java.util.NoSuchElementException("a er tom");
		for(i=1; i<a.length; i++)
		{
			if(a[i-1] > a[i])
			{
				int maks=a[i];
				a[i]=a[i-1];
				a[i-1]=maks;
			}
		}
		return a[i-1];
	}

	public static int ombyttinger(int[] a)
	{
		int ombyttinger=0;

		if (a.length < 1)
		      throw new java.util.NoSuchElementException("a er tom");
		for(int i=1; i<a.length; i++)
		{
			if(a[i-1] > a[i])
			{
				int maks=a[i];
				a[i]=a[i-1];
				a[i-1]=maks;
				ombyttinger++;
			}
		}
		return ombyttinger;
	}
	
	//oppg 2
	
	public static int[] sortering(int[] a)
	{
		int lengde=a.length;
		for(int i=1; i<lengde; i++)
		{
			int n=0;
			while(i-n-1 != -1 && a[i-n-1] > a[i-n])
			{
				int hjelpevariabel=a[i-n-1];
				a[i-n-1]=a[i-n];
				a[i-n]=hjelpevariabel;
				n++;
			}
		}
		return a;
	}

	/*
	 * Det gjennomsnittlige tilfellet: Hn = 1 + 1/2 + 1/3 + . . . + 1/n
	 * Det verste tilfellet: n(n-1)/2=2n-1n/2=n/2
	 * Det beste tilfellet: n-1
	 */
	
	//oppg 3
	
	public static int medlemsnummer()
	{
		  int antall=0;
		  for(int i=10000; i<55555; i++)
		  {
			  char[] arr=Integer.toString(i).toCharArray();
			  if(checkNumber(arr))
				  antall++;
		  }
		  return antall;
	  }

	  public static boolean checkNumber(char[] arr)
	  {
		  int count=0;
		  for(int j=0; j<arr.length; j++)
		  {
			  int checkNumber=Character.getNumericValue(arr[j]);
			  if(checkNumber>5)
			  {
				  return false;
			  }

		  	  for(int k = j+1; k < arr.length; k++)
		  	  {
		  		if(checkNumber==Character.getNumericValue(arr[k]))
		  			count++;
			  }
		  }
		  if(count>2) 
			  return false; 
		  else 
			  return true;
	  }
	  
	//oppg 4
	
	  public static int antallUlikeUsortert(int[] a)
	{
		if(a.length<1)
			return 0;
		int antallUlike=0;
		for(int i=0; i<a.length; i++)
		{
			boolean funnet=false;
			int verdi=a[i];

			for(int j=i+1; j<a.length; j++)
			{
				if(verdi==a[j])
				{
					funnet=true;
					break;
				}
			}
			if(funnet==false)
			antallUlike++;
		}
		return antallUlike;
	}
	
	//oppg 5
	
	public static void rotasjon(char[] a)
	{
		if(a.length<1)
			return;
		int siste=a.length-1;
		char hjelpevariabel=a[siste];
		for(int i=siste; i>0; i--)
		{
			a[i]=a[i-1];
		}
		a[0]=hjelpevariabel;
	}
	
	//oppg 6
	
	public static void rotasjon(char[] a, int k)
	{
		int n = a.length;
		if(n < 2)
			return;
	    if((k %= n) < 0)
	    	k += n;

	    char[] b = Arrays.copyOfRange(a, n - k, n);
	    for (int i = n - 1; i >= k; i--)
	    	a[i] = a[i - k];
	    System.arraycopy(b, 0, a, 0, k);
	}
	
	//oppg 7a
	
	public static String flett(String s, String t)
	{
		int k = Math.min(s.length(), t.length());
	    StringBuilder sb = new StringBuilder();

	    for (int i = 0; i < k; i++)
	    {
	      sb.append(s.charAt(i)).append(t.charAt(i));
	    }

	    sb.append(s.substring(k)).append(t.substring(k));
	    return sb.toString();
	 }

	//oppg 7b
	
	public static String flett(String... s)
	{
		int highestString = 0;
		for(int i = 0; i < s.length; i++)
		{
			if(s[i].length() > highestString) highestString = s[i].length();
		}
		StringBuilder sb=new StringBuilder();
		for(int j = 0; j < highestString; j++)
		{
			for(int i = 0; i < s.length; i++) // gÂr igjennom alle strengene en gang
			{
				String streng = s[i];
				if(streng.length() > j)
				{
					sb.append(streng.charAt(j));
				}
			}
		}
		return sb.toString();
	}
	
	//oppg 8a
	
	public static int[] indeks(int[] a)
	{
		int[] indeks = new int[]{0,1,2};
		int en = a[0];
		int to = a[1];
		int tre = a[2];

		if(en > tre)
		{
			int h = indeks[0];

			indeks[0] = indeks[2];
			indeks[2] = h;

			h = en;
			en = tre;
			tre = h;
		}
		if(en > to)
		{
			int h = indeks[0];
			indeks[0] = indeks[1];
			indeks[1] = h;

			h = en;
			en = to;
			to = en;
		}
		if(to > tre)
		{
			int h = indeks[1];
			indeks[1] = indeks[2];
			indeks[2] = h;

			h = to;
			to = tre;
			tre = h;
		}
		return indeks;
	}
	
	//oppg 8b
	
	public static int[] tredjeMin(int[] a)
	{
		int n=a.length;
  		if (n<3) throw new java.util.NoSuchElementException("a.length(" + n + ")<2");
  		int m=0;
  		int nm=1;
  		int nnm=2;

  		if(a[nnm] < a[nm])
  		{
			int nmt = nm;
			nm = nnm;
			nnm = nmt;
		}
		if(a[nm] < a[m])
		{
			int mt = m;
			m = nm;
			nm = mt;
		}
  		if(a[nnm]<a[nm])
  		{
			int mt = nm;
			nm = nnm;
			nnm = mt;
  		}
  		int minsteverdi=a[m];
  		int nestminsteverdi=a[nm];
  		int nestnestminsteverdi=a[nnm];

  		for (int i=3; i<n; i++)
  		{
			if(a[i]<nestnestminsteverdi)
	  		{
				if(a[i]<nestminsteverdi)
		  		{
					if(a[i]<minsteverdi)
			  		{
						nnm=nm;
						nestnestminsteverdi=nestminsteverdi;

				  		nm=m;
				  		nestminsteverdi=minsteverdi;

				  		m=i;
				  		minsteverdi=a[m];
			  		}
			  		else
			  		{
						nestnestminsteverdi = nestminsteverdi;
						nnm = nm;

						nm=i;
				  		nestminsteverdi=a[nm];
			  		}
		  		}
		  		else
		  		{
					nnm=i;
			  		nestnestminsteverdi=a[nnm];
			  	}
      		}
  		}
  		return new int[] {m, nm, nnm};
	}
	
	//oppg 9
	
	public static int[] kMinst(int[] a, int k)
	{
		if(k<1) 
			throw new NoSuchElementException("finner ikke element");
		else if(k>a.length) 
			throw new IllegalArgumentException("illegal argument");

		int[] minste = new int[k];
		int i = 0;
		for(; i < k; i++)
		{
			minste[i] = a[i];
		}
		Arrays.sort(minste);

		for(; i < a.length; i++)
		{
			if(a[i] < minste[k - 1])
			{
				int n = k - 1;
				while(n - 1 >= 0 && a[i] < minste[n - 1])
				{
					minste[n] = minste[n - 1];
					n--;
				}
				minste[n] = a[i];
			}
		}
		return minste;
  	}

	//oppg 10
	
	public static boolean inneholdt(String a, String b)
	{
		int[] tabell = new int[10000];
		if(a.length() > b.length()) return false;
		for(int i = 0; i < b.length(); i++)
		{
			char verdi = b.charAt(i);
			tabell[verdi] = tabell[verdi] + 1;
		}
		for(int i = 0; i < a.length(); i++)
		{
			char verdi = a.charAt(i);
			if(tabell[verdi] > 0)
			{
				tabell[verdi]--;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
}
