package oblig3;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.*; 

/*
 * Av: Noora Aroon
 */

public class ObligSBinTre<T> implements Beholder<T> 
{
	private static final class Node<T>
	{
		private T verdi; 
		private Node<T> venstre, hoyre; 
		private Node<T> forelder;
		
		private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
		{
			this.verdi=verdi; 
			venstre=v; hoyre=h; 
			this.forelder=forelder; 
		}
		
		private Node(T verdi, Node<T> forelder)
		{
			this(verdi, null, null, forelder);
		}
		
		@Override
		public String toString()
		{
			return "" + verdi; 
		}
	} 
	
	private T verdi; 
	private Node<T> rot; 
	private int antall; 
	private int endringer; 
	private int hoyde; 
	private int antallIngenBarn; 
	private int antallEttBarn; 
	private int antallToBarn; 
	private int antall_f=0;
	
	private final Comparator<? super T> comp;
	
	public ObligSBinTre(Comparator<?super T> c)
	{
		rot=null; 
		antall=0; 
		endringer=0; 
		hoyde=-1; 
		antallIngenBarn=0; 
		antallEttBarn=0; 
		antallToBarn=0; 
		comp=c; 
	}
		
	public T forelder()
	{
		Node<T> p=rot;
		while(p.hoyre != null) 
			p = p.hoyre;
		return p.forelder.verdi;
	}
	
	public final boolean leggInn(T verdi)
	{
		Objects.requireNonNull(verdi, "Ulovlig med nullverdier");
	    Node<T> p=rot, q=null;
	    int cmp=0, hoyde=-1;
	    
	    while(p != null)
	    {
	    	q=p;
	    	cmp=comp.compare(verdi,p.verdi);
	    	p=cmp < 0 ? p.venstre : p.hoyre;
	    	hoyde++;
	    }

	    p=new Node<T>(verdi, q);

	    if(q==null)
	    {
	    	rot=p;
	    	antallIngenBarn++;
	    }
	    else if(cmp < 0)
	    {
	    	q.venstre=p;
	    	if(q.hoyre != null)
	    	{
	    		antallEttBarn--;
	    		antallToBarn++;
	    		antallIngenBarn++;
	    	}
	    	else antallEttBarn++;
	    }
	    else
	    {
			q.hoyre=p;
			if(q.venstre != null)
			{
				antallEttBarn--;
				antallToBarn++;
				antallIngenBarn++;
			}
			else antallEttBarn++;
		}
	    endringer++;
	    antall++;
	    return true;                  
	}
		
	public int antall(T verdi)
	{ 
		int antall=0;
		Node<T> runner=rot;
		while(runner != null)
		{
			int cmp=comp.compare(verdi, runner.verdi);
			if(cmp==0) 
				antall++;

			if(cmp < 0) 
				runner=runner.venstre;
		    else runner=runner.hoyre;
		}
		return antall;
	}
		
	@Override
	public boolean inneholder(T verdi) 
	{
		if(verdi==null)
			return false; 
		
		Node<T> p=rot; 
		
		while(p != null)
		{
			int cmp=comp.compare(verdi, p.verdi);
			if(cmp < 0)
				p=p.venstre;
			else if(cmp > 0)
				p=p.hoyre; 
			else
				return true; 
		}
		return false;
	}
		
	@Override
	public boolean fjern(T verdi) 
	{
		if(verdi==null) 
			return false;
        
        Node<T> p=rot;
       
        while(p!=null)
        {
        	int cmp=comp.compare(verdi,p.verdi);
           
            if(cmp < 0) 
            	p=p.venstre;
            else if(cmp > 0) 
            	p=p.hoyre;
            else break;
        }
       
        if(p==null) 
        	return false;
       
        if(p.venstre==null || p.hoyre==null) 
        { 
            Node<T> b=(p.venstre!=null) ? p.venstre : p.hoyre;
           
            if(p==rot) 
            {
                rot= b;
                if(b!=null) 
                	b.forelder=null;
            }
            else if(p==p.forelder.venstre) 
            {
                if(b!=null)
                	b.forelder=p.forelder;
                p.forelder.venstre=b;
            } 
            else 
            {  
                if(b!=null)
                	b.forelder=p.forelder;
                p.forelder.hoyre=b;
            }
        }
        else 
        {   
            Node<T> r=p.hoyre;
            while(r.venstre != null) 
            	r=r.venstre;
            p.verdi=r.verdi;
           
            if(r.forelder!=p) 
            {
                Node<T> q=r.forelder;
                q.venstre=r.hoyre;
                if(q.venstre!=null)
                	q.venstre.forelder=q;
            }
            else
            {    
                p.hoyre=r.hoyre;              
                if(p.hoyre !=null) 
                	p.hoyre.forelder=p;
            }
        }
        antall--;
        return true;      
	}
	  
	public int fjernAlle(T verdi)
	{
		int i=0;
	    boolean fjernet=true;
	    
	    while(fjernet!=false)
	    {
	    	if(fjern(verdi))
	    		i++;
	        else 
	            fjernet=false;
	    }
	    return i;
	}
	
	@Override
	public int antall()
	{
		return antall;
	}
	  
	@Override
	public boolean tom()
	{
	    return antall==0;
	}
	
	public void nullstill()
	{
		if(!tom()) 
	    	nullstill(rot);  
	    rot=null; 
	    antall=0;      
	}

	private void nullstill(Node<T> p)
	{
		if(p.venstre != null)
	    {
			nullstill(p.venstre);      
			p.venstre = null;          
	    }
	    if(p.hoyre != null)
	    {
	    	nullstill(p.hoyre);        
	    	p.hoyre = null;           
	    }
	    p.verdi = null;            
	}
	  
	private static <T> Node<T> nesteInorden(Node<T> p)
	{
		if(p.hoyre != null)
		{
			p=p.hoyre;
			
			while(p.venstre != null) 
			{
				p=p.venstre;  
			}
			return p;
		 }
		 
		 while(p != null)
		 {
			if(p.forelder != null && p.forelder.venstre == p)
			{
				 return p.forelder;
			}
			p=p.forelder;
		 }
		 return p;
	}

	public String toString()
	{
		if(antall<1) 
			return "[]";
		StringBuilder sb=new StringBuilder(10);
		sb.append("[");
		Node<T> p=rot;
		
		while(p.venstre != null)
		{
			p=p.venstre;
		}
		
		for(int i=0; i<(antall); i++)
		{
			sb.append(p.verdi);
			if(i != antall-1) 
				sb.append(", ");
			else sb.append("]");
				p=nesteInorden(p);
		}
		String s=sb.toString();
		return s;
	}	
	
	public String omvendtString()
	{
		 Stakk<T> s=new TabellStakk<>();
		 
	        if(rot != null) 
	        {
	            Node<T> p=rot;
	            while(p.venstre != null)
	                p=p.venstre;
	 
	            for(; p != null; p=nesteInorden(p))
	                s.leggInn(p.verdi);
	        }
	        return s.toString();
	}
	  
	public String høyreGren()
	{
		 StringBuilder s=new StringBuilder();
	     s.append("[");
	     
	     if(rot != null)
	     {
	        Node p = rot;
	        s.append(p);
	        while(p.hoyre != null || p.venstre != null)
	        {
	        	if(p.hoyre != null)
	                p=p.hoyre;
	            else p=p.venstre;
	            s.append(",").append(" ").append(p);            
	        }      
	      }
	      s.append("]");
	      return s.toString();
	}
	  
	private static class BladNode<T>
	{
	    private int nivå=0;
	    private T verdi=null;
	}
	
	private static <T> void lengstGren(Node<T> p, int nivå, BladNode<T> blad)
	{
	    if(p.venstre==null && p.hoyre==null)
	    {
	    	if(nivå >= blad.nivå)
	    	{
	    		blad.nivå=nivå;       
	    		blad.verdi=p.verdi; 
	    	}
	    }
	    if (p.venstre != null) 
	    	lengstGren(p.venstre, nivå + 1, blad);
	    if (p.hoyre != null) 
	    	lengstGren(p.hoyre, nivå + 1, blad);
	}
	
	public String lengstGren()
	{
		if (tom()) 
			return "[]";
	    BladNode<T> blad=new BladNode();
	    lengstGren(rot, 0, blad);
	    return gren(blad.verdi);
	}
	
	private String gren(T bladnodeverdi)
	{
		Node<T> p=rot;
	    StringJoiner s=new StringJoiner(", ", "[", "]");

	    while(p != null)
	    {
	    	s.add(p.verdi.toString());
	    	p=comp.compare(bladnodeverdi, p.verdi) < 0 ? p.venstre : p.hoyre;
	    }
	    return s.toString();
	}
	
	public String[] grener()
	{
		if(tom())return new String[0];
        String[] stringTabell=new String[1];
        StringJoiner s;
        ArrayDeque<Node<T>> que=new ArrayDeque();
        ArrayDeque<Node<T>> nodegrenque=new ArrayDeque();

        boolean tomListe=false;
        
        Node<T> p=rot;
        int i=0;
        
        while(!tomListe)
        {
            s=new StringJoiner(", ","[","]");
    
            while(p.venstre != null || p.hoyre != null) 
            {
                if(p.venstre != null) 
                {
                    if(p.hoyre != null) 
                    	que.add(p.hoyre);
                    p=p.venstre;
                }
                else if(p.hoyre != null)
                {
                    p=p.hoyre; 
                }
            }

            while(p!=null) 
            {
            	nodegrenque.add(p);
            	p=p.forelder;
            }
            
            while(!nodegrenque.isEmpty()) 
                s.add(nodegrenque.pollLast().toString());
            
            if(stringTabell[stringTabell.length-1]!=null) 
                stringTabell=Arrays.copyOf(stringTabell, stringTabell.length+1);
            stringTabell[i++]=s.toString();

            if(!que.isEmpty()) 
            	p=que.pollLast();
            else tomListe=true;            
        }
        return stringTabell;
	}
	
	public String bladnodeverdier()
	{
		StringBuilder sb=new StringBuilder();
        sb.append("[");
        Node p=rot;
        finnBladnode(p, sb);
        sb.append("]");
        return sb.toString();
	}
	
	private void finnBladnode(Node p, StringBuilder sb) 
	{
        if(p==null) 
        {
        	return;
        }
        
        if(p.venstre==null && p.hoyre==null)
        {
            if(!sb.toString().equals("["))
            {
                sb.append(",").append(" ").append(p);
            } 
            else 
            {
                sb.append(p);
            }
        }        
        finnBladnode(p.venstre, sb);
        finnBladnode(p.hoyre, sb);
    }
	  
	@Override
	public Iterator<T> iterator()
	{
	    return new BladnodeIterator();
	}
	  
	private class BladnodeIterator implements Iterator<T>
	{
		private Node<T> p=rot, q=rot;
	    private boolean removeOK=false;
	    private int iteratorendringer;
	    private Stakk<Node<T>> s=new TabellStakk<>();
	    private Node<T> blad;
		private int Iendringer=endringer;

	    private BladnodeIterator()
	    {
			if(p != null)
			{
				while(p.venstre != null || p.hoyre != null)
				{
					while(p.venstre != null)
					{
						p=p.venstre;
					}
					if(p.hoyre != null)
					{
						p=p.hoyre;
					}
				}
			}
	    }
	    
	    @Override
	    public boolean hasNext()
	    {
	    	return p != null;
	    }

	    @Override
	    public T next()
	    {
			blad=p;
	    	if(endringer != Iendringer) 
	    		throw new ConcurrentModificationException("Treet er endret");
		    if(blad==null) 
		    	throw new NoSuchElementException("Ingen elementer i treet");

		   T verdi=p.verdi;
		   T bladet=p.verdi;
		   p=p.forelder;
		   removeOK=true;

		   if(p==null) 
			   return blad.verdi;
		   while((p != null && p.hoyre==null) || (p != null && comp.compare(verdi, p.hoyre.verdi)==0))
		   {
			   verdi=p.verdi;
			   p=p.forelder;
		   }
		   if(p==null) 
			   return blad.verdi;

		   if(p.hoyre != null)
		   {
			   p=p.hoyre;
	       	   
			   while(p.venstre != null || p.hoyre != null)
	       	   {
				   while(p.venstre != null) 
					   p=p.venstre;
				   if(p.hoyre != null) 
					   p=p.hoyre;
			   }
		   }
		   return blad.verdi;
	    }

	    @Override
	    public void remove()
	    {
			if(blad==null || q==null || removeOK==false) 
				throw new IllegalStateException("Kan ikke fjernes");
			removeOK=false;

			antall--;

			if(blad==q)
			{
				blad=q=rot=null;
				return;
			}
		    else if(blad.forelder != null)
			{
				if(blad.forelder.venstre != null && blad.forelder.venstre==blad) 
					blad.forelder.venstre=null;
			    else if(blad.forelder.hoyre != null && blad.forelder.hoyre==blad) 
			    	blad.forelder.hoyre=null;
		    }
	    }
  
	}
}
