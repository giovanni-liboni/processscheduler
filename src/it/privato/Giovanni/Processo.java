/*
 * This file is a part of processscheduler.
 * Copyright (C) 2013  Giovanni Liboni

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

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
