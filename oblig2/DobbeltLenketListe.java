package oblig2;

import java.util.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
*Av: Noora Aroon
*/

public class DobbeltLenketListe<T> implements Liste<T>
{
	private static final class Node<T>
	{
		private T verdi;
		private Node<T> forrige, neste;

		private Node(T verdi, Node<T> forrige, Node<T> neste)
		{
			this.verdi = verdi;
			this.forrige = forrige;
			this.neste = neste;
		}
	}

	private Node<T> hode;
	private Node<T> hale;
	private int antall;
	private int antallEndringer;
	
	private void indeksKontroll(int indeks)
	{
		if (indeks < 0)
		{
			throw new IndexOutOfBoundsException("indeksen er negativ");
		}
		else if (indeks >= antall)
		{
			throw new IndexOutOfBoundsException("indeks er større enn antall");
		}
	}
	
	private Node<T> finnNode(int indeks)
	{
		if(indeks >= antall || indeks < 0) return null;

		Node<T> returnNode;

		if (indeks < antall / 2)
		{
			returnNode = hode;
			for (int i = 0; i < indeks; i++)
				returnNode = returnNode.neste;
		}
		else
		{
			returnNode = hale;
			for (int i = antall - 1; i > indeks; i--)
				returnNode = returnNode.forrige;
		}
		return returnNode;
	}
	
	public DobbeltLenketListe()
	{
		hode = hale = null;
		antall = 0;
		antallEndringer = 0;
	}
	
	public DobbeltLenketListe(T[] a)
	{
		this();
		Objects.<T[]>requireNonNull(a, "Tabellen a er null!");
		
		if(a.length == 0)
		{
			return;
		}
		int i = 0;
		
		while(i < a.length  && a[i] == null) 
			i++;

		Node n;
		
		if(i >= a.length) 
			return;

		if(a[i] != null)
		{
			n = hale = hode = new Node(a[i],null,null);
		    antall++;
		    i++;
		}
		else return;

		for( ; i < a.length; i++)
		{
			if(a[i] != null)
		    {
				n.neste = new Node(a[i],n,null);
				hale = n = n.neste;
				antall++;
		    }
		 }
	}

	@Override
	public int antall()
	{
		return antall;
	}

	@Override
	public boolean tom()
	{
		if(hode == null) 
			return true; 
		return false;
	}

	@Override
	public boolean leggInn(T verdi)
	{
		Objects.requireNonNull(verdi, "verdien kan ikke være et null-objekt");

		if (tom())
		{
			hode = hale = new Node<T>(verdi, null, null);
		}
		else
		{
			hale = hale.neste = new Node<T>(verdi, hale, null);
		}
		antallEndringer++;
		antall++;

		return true;
	}

	@Override
	public void leggInn(int indeks, T verdi)
	{
		Objects.requireNonNull(verdi, "Verdien kan ikke være et null-objekt");
		
		if(indeks < 0)
			throw new IndexOutOfBoundsException("indeks kan ikke være negativ");
		else if (indeks > antall)
			throw new IndexOutOfBoundsException("indeks kan ikke være større enn antall");

		if(indeks == 0 && antall == 0)
		{
			Node<T> nyNode = new Node<>(verdi,null,null);
			hale = hode = nyNode;
		}
		else if(indeks == 0)
		{
			Node<T> nodeFlyttes = finnNode(indeks);
			Node<T> nyNode = new Node<>(verdi,null,nodeFlyttes);
			nodeFlyttes.forrige = nyNode;
			hode = nyNode;
		}
		else if(indeks == antall)
		{
			Node<T> nodeFlyttes = finnNode(indeks-1);
			Node<T> nyNode = new Node<>(verdi,nodeFlyttes,null);
			hale = nyNode;
			nodeFlyttes.neste = nyNode;
		}
		else
		{
			Node<T> nodeFlyttes = finnNode(indeks);
			Node<T> forrigeNode = nodeFlyttes.forrige;
			Node<T> nyNode = new Node<>(verdi,forrigeNode,nodeFlyttes);
			forrigeNode.neste = nyNode;
			nodeFlyttes.forrige = nyNode;
		}
		antall++;
		antallEndringer++;
	}
	
	@Override
	public boolean inneholder(T verdi)
	{
		return indeksTil(verdi) != -1;
	}

	@Override
	public T hent(int indeks)
	{
		indeksKontroll(indeks);
		return finnNode(indeks).verdi;
	}

	@Override
	public int indeksTil(T verdi)
	{
		Node<T> node = hode;

		boolean funnet = false;
		int indeks = 0;

		while (node != null)
		{
			if (node.verdi.equals(verdi))
			{
				return indeks;
			}
			node = node.neste;
			indeks++;
		}
		return -1;
	}

	@Override
	public T oppdater(int indeks, T nyverdi)
	{
		indeksKontroll(indeks);
		Objects.requireNonNull(nyverdi, "verdien kan ikke være et null-objekt");
		Node<T> node = finnNode(indeks);
		T gammelVerdi = node.verdi;
		node.verdi = nyverdi;
		antallEndringer++;
		return gammelVerdi;
	}

	private void removeNode(int indeks, Node<T> forrigeNod, Node<T> nesteNod)
	{
		if(indeks == 0 && antall == 1)
		{
			hode = hale = null;
		}
		else if(indeks == 0)
		{
			hode = nesteNod;
			nesteNod.forrige = null;
		}
		else if(indeks == (antall-1))
		{
			hale = forrigeNod;
			forrigeNod.neste = null;
		}
		else
		{
			forrigeNod.neste = nesteNod;
			nesteNod.forrige = forrigeNod;
		}
		antall--;
		antallEndringer++;
	}
	
	@Override
	public boolean fjern(T verdi)
	{
		Node<T> node = hode;
		boolean funnet = false;
		int indeks = 0;
	
		while (node != null)
		{
			if (node.verdi.equals(verdi))
			{
				Node<T> forrigeNod = node.forrige;
				Node<T> nesteNod = node.neste;
				removeNode(indeks, forrigeNod, nesteNod);
				return true;
			}
			node = node.neste;
			indeks++;
		}
		return false;
	}

	@Override
	public T fjern(int indeks)
	{
		if(indeks >= antall || indeks < 0) 
			throw new IndexOutOfBoundsException("Indeksen er for stor");

		Node<T> noden = finnNode(indeks);
		Node<T> forrigeNod = noden.forrige;
		Node<T> nesteNod = noden.neste;
		removeNode(indeks, forrigeNod, nesteNod);
		return noden.verdi;
	}

	@Override
	public void nullstill()
	{
		hale = hode = null;
		antall = 0;
		antallEndringer++;
	}

	@Override
	public String toString()
	{
		StringJoiner sj = new StringJoiner(", " , "[", "]");
		
		if(hode == null) 
			return "[]";
		sj.add(hode.verdi.toString());
	    Node<T> runer = hode.neste;
		
	    while(runer != null)
		{
			sj.add(runer.verdi.toString());
			runer = runer.neste;
		}
		return sj.toString();
	}

	public String omvendtString()
	{
		if (tom()) 
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');

		Node<T> node = hale;

		sb.append(node.verdi);
		node = node.forrige;

		while (node != null) 
		{
			sb.append(',').append(' ').append(node.verdi);
			node = node.forrige;
		}

		sb.append(']');
		return sb.toString();
	}

	@Override
	public Iterator<T> iterator()
	{
		return new DobbeltLenketListeIterator();
	}

	public Iterator<T> iterator(int indeks)
	{
		indeksKontroll(indeks);
		return new DobbeltLenketListeIterator(indeks);
	}
	
	private class DobbeltLenketListeIterator implements Iterator<T>
	{
		private Node<T> removeValue;
		private Node<T> denne;
		private boolean fjernOK;
		private int forventetAntallEndringer;
		
		private DobbeltLenketListeIterator()
		{
			removeValue = null;
			denne = hode;
			removeValue = null;
			fjernOK = false;
			forventetAntallEndringer = antallEndringer;
		}
	  	
		public DobbeltLenketListeIterator(int indeks)
	  	{
	    	removeValue = null;
	  	    fjernOK = false;
	  	  	forventetAntallEndringer = antallEndringer;

	  	 	if(antall == 0 && indeks == 0) 
	  	 		denne = null; 
	  	 	else denne = finnNode(indeks);
	  	}
		
		@Override
		public boolean hasNext()
		{
			return denne != null;
		}
		
		@Override
		public T next()
		{
			if(forventetAntallEndringer != antallEndringer) 
				throw new ConcurrentModificationException("Listen er blitt endret på");
			if(denne == null) 
				throw new NoSuchElementException("Ingen elementer igjen");

			Node<T> verdi = denne;
			removeValue = denne;
			fjernOK = true;
			denne = denne.neste;

			return verdi.verdi;
		}

		@Override
		public void remove()
		{
			if (!fjernOK)
				throw new IllegalStateException("kan ikke kalle på denne tilstanden");
			else if (antallEndringer != forventetAntallEndringer)
			{
				throw new ConcurrentModificationException("antall endringer og forventet antall endringer er forskjellige");
			}
			fjernOK = false;
			
			if (antall == 1)
			{
				hode = hale = null;
			}
			else if (denne == null)
			{
				hale = hale.forrige;
				hale.neste = null;
			}
			else if (denne.forrige == hode)
			{
				hode = hode.neste;
				hode.forrige = null;
			}
			else
			{
				denne.forrige = denne.forrige.forrige;
				denne.forrige.neste = denne;
			}
			antall--;
			antallEndringer++;
			forventetAntallEndringer++;
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action)
		{
			Objects.requireNonNull(action, "handlingen kan ikke være null");
			Node<T> node = denne;

			while (node != null)
			{
				action.accept(node.verdi);
				node = node.neste;
			}
		}
	}
	
	@Override
	public void forEach(Consumer<? super T> action)
	{
		Objects.requireNonNull(action, "handlingen kan ikke være null");
		Node<T> node = hode;

		while (node != null)
		{
			action.accept(node.verdi);
			node = node.neste;
		}
	}

	public static <T> void traverser(T[] a, Predicate<? super T> p, Consumer<? super T> c)
	{
		//for (T t : a) Beholder.fjernHvis(navn->navn.charAt(hale) == 'R');
	}
}
