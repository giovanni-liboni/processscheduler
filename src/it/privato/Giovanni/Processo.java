package it.privato.Giovanni;

import java.util.Comparator;

public class Processo {

	String nome;
	int tempoArrivo;
	int burst;
	int burstResiduo;
	int tempoAttesa;
	boolean inQueue;
	boolean justCome;
	boolean finish;
	boolean firstInQueue;
	float R;
	
	public Processo(String nome, int tempoArrivo, int burst){
		this.nome = nome;
		this.tempoArrivo = tempoArrivo;
		this.burst = burst;
	}
	public static Comparator<Processo> COMPARE_BY_ARRIVO = new Comparator<Processo>() {
		public int compare(Processo one, Processo other) {
			return one.tempoArrivo < other.tempoArrivo? 1:0;
		}
	};
	public static Comparator<Processo> COMPARE_BY_BURST = new Comparator<Processo>() {
		public int compare(Processo one, Processo other) {
			return one.burst < other.burst? 1:0;
		}
	};
	public static Comparator<Processo> COMPARE_BY_NOME = new Comparator<Processo>() {
		public int compare(Processo one, Processo other) {
			return one.nome.compareTo(other.nome);
		}
	};
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Processo))return false;
	    Processo p = (Processo) other;
	    if(p.nome.compareTo(this.nome) == 0 ) return true;
	    else return false;
	    	
	}
}
