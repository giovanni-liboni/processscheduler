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

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;

public class Processi {

	ArrayList<Processo> processi;

	public Processi(){
		processi = new ArrayList<Processo>();
	}
	public void add(Processo p){
		processi.add(p);
	}
	public void remove(Processo p){
		processi.remove(p);
	}
	public void clear(){
		processi.clear();
	}
	// da fare se il processo arriva dopo la fine di quello precedente
	public Object[] logicaFcfs(){

		Collections.sort(processi, Processo.COMPARE_BY_ARRIVO);
		int tempoTotale=0;
		for (int i = 0; i < processi.size();i++)
		{
			Processo p = processi.get(i);
			processi.get(i).burstResiduo = processi.get(i).burst;
			p.inQueue = false;
			p.justCome = false;
			p.finish=false;
			tempoTotale+=p.burst;
			tempoTotale+=p.tempoArrivo;
		}
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Processo> coda = new ArrayList<Processo>();
		Processo p=processi.get(0);
		boolean processoInCpu = false;

		for(int t = 0; t < tempoTotale ; t++)
		{
			// se non è presente un processo in CPU allora lo devo scegliere
			if(! processoInCpu )
			{
				// controllo se sono arrivati nuovi processi
				// e li metto in coda
				coda.clear();
				for (int i = 0; i < processi.size();i++)			
					if( processi.get(i).tempoArrivo <= t && processi.get(i).burstResiduo > 0)
						coda.add(processi.get(i));
				// se la coda è vuota allora devo far passare il tempo e nessun processo verrà
				// eseguito
				
				if(!coda.isEmpty())
				{
					// scelgo il processo con il burst più basso
					p = coda.get(0);
					for (int i = 0; i < coda.size();i++)
						if ( coda.get(i).tempoArrivo < p.tempoArrivo)
							p = coda.get(i);
					
					processoInCpu=true;
				}
				else
					processoInCpu=false;
				
				// a questo punto: 1. Ho scelto il processo da eseguire
				//					2. Non è presente alcun processo da eseguire
			}
			
			if(processoInCpu)
			{
				list.add(p.nome);
				p.burstResiduo--;
				if(p.burstResiduo <= 0)
					processoInCpu=false;
			}
			else
				list.add("");

		}
		return list.toArray();
	}
	public Object[] logicaSrtf(){
		Collections.sort(processi, Processo.COMPARE_BY_ARRIVO);
		int tempoTotale=0;
		int tempoArrivoMassimo=0;
		for (int i = 0; i < processi.size();i++)
		{
			Processo p = processi.get(i);
			processi.get(i).burstResiduo = processi.get(i).burst;
			p.inQueue = false;
			p.justCome = false;
			p.finish=false;
			tempoTotale+=p.burst;
			if (tempoArrivoMassimo < p.tempoArrivo)
				tempoArrivoMassimo = p.tempoArrivo;
		}
		tempoTotale+=tempoArrivoMassimo;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Processo> coda = new ArrayList<Processo>();
		Processo p=processi.get(0);
		boolean processoInCpu = false;
		boolean nuovoProcesso = false;

		for(int t = 0; t < tempoTotale ; t++)
		{
			// controllo se è arrivato un nuovo processo
			for (int i = 0; i < processi.size();i++)
				if( processi.get(i).tempoArrivo <= t )
					nuovoProcesso=true;
			
			// se non è presente un processo in CPU allora lo devo scegliere
			// oppure è arrivato un nuovo processo
			if(! processoInCpu || nuovoProcesso)
			{
				// controllo se sono arrivati nuovi processi
				// e li metto in coda
				// se hanno tempo di burst uguale a 0 non entrano nella coda
				coda.clear();
				for (int i = 0; i < processi.size();i++)			
					if( processi.get(i).tempoArrivo <= t && processi.get(i).burstResiduo > 0)
						coda.add(processi.get(i));
				// se la coda è vuota allora devo far passare il tempo e nessun processo verrà
				// eseguito
				
				if(!coda.isEmpty())
				{
					// scelgo il processo con il burst più basso
					p = coda.get(0);
					for (int i = 0; i < coda.size();i++)
						if ( coda.get(i).burstResiduo < p.burstResiduo)
							p = coda.get(i);
					
					processoInCpu=true;
				}
				else
					processoInCpu=false;
				
				// a questo punto: 1. Ho scelto il processo da eseguire
				//					2. Non è presente alcun processo da eseguire
			}
			
			if(processoInCpu)
			{
				list.add(p.nome);
				p.burstResiduo--;
				if(p.burstResiduo <= 0)
					processoInCpu=false;
			}
			else
				list.add("");

		}
		return list.toArray();
	}
	public void ordina(){
		Collections.sort(processi, Processo.COMPARE_BY_NOME);
	}
	public Object[] logicaHrrn(){

		Collections.sort(processi, Processo.COMPARE_BY_ARRIVO);
		int tempoTotale=0;
		int tempoArrivoMassimo=0;
		for (int i = 0; i < processi.size();i++)
		{
			Processo p = processi.get(i);
			processi.get(i).burstResiduo = processi.get(i).burst;
			p.inQueue = false;
			p.justCome = false;
			p.finish=false;
			tempoTotale+=p.burst;
			if (tempoArrivoMassimo < p.tempoArrivo)
				tempoArrivoMassimo = p.tempoArrivo;
		}
		tempoTotale+=tempoArrivoMassimo;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Processo> coda = new ArrayList<Processo>();
		Processo p=processi.get(0);
		boolean processoInCpu = false;

		for(int t = 0; t < tempoTotale ; t++)
		{

			// se non è presente un processo in CPU allora lo devo scegliere
			// questo vuol dire che o siamo all'inizio o è terminato un processo
			if(! processoInCpu )
			{
				// controllo se sono arrivati nuovi processi
				// e li metto in coda
				// se hanno tempo di burst uguale a 0 non entrano nella coda
				coda.clear();
				for (int i = 0; i < processi.size();i++)			
					if( processi.get(i).tempoArrivo <= t && processi.get(i).burstResiduo > 0)
						coda.add(processi.get(i));
				
				// se la coda è vuota allora devo far passare il tempo e nessun processo verrà
				// eseguito
				
				if(!coda.isEmpty())
				{
					// calcolo R e scelgo il processo con l'R più piccolo
					p = coda.get(0);
					
					// aggiorno i tempi di attesa
					p.tempoAttesa=t-p.tempoArrivo;
					
					// calcolo R
					p.R=1+(p.tempoAttesa/p.burst);
					
					for (int i = 0; i < coda.size();i++)
					{
						// aggiorno i tempi di attesa
						coda.get(i).tempoAttesa=t-coda.get(i).tempoArrivo;
						
						// calcolo R
						coda.get(i).R=1+(coda.get(i).tempoAttesa/coda.get(i).burst);
						
						if ( coda.get(i).R > p.R)
							p = coda.get(i);
					}
							
					processoInCpu=true;
				}
				else
					processoInCpu=false;
				
				// a questo punto: 1.  Ho scelto il processo da eseguire
				//					2. Non è presente alcun processo da eseguire
			}
			
			if(processoInCpu)
			{
				list.add(p.nome);
				p.burstResiduo--;
				if(p.burstResiduo <= 0)
					processoInCpu=false;
			}
			else
				list.add("");

		}
		return list.toArray();
	}
	/*
	 * SJF senza prelazione
	 */
	public Object[] logicaSjf(){
		Collections.sort(processi, Processo.COMPARE_BY_ARRIVO);
		int tempoTotale=0;
		int tempoArrivoMassimo=0;
		for (int i = 0; i < processi.size();i++)
		{
			Processo p = processi.get(i);
			processi.get(i).burstResiduo = processi.get(i).burst;
			p.inQueue = false;
			p.justCome = false;
			p.finish=false;
			tempoTotale+=p.burst;
			if (tempoArrivoMassimo < p.tempoArrivo)
				tempoArrivoMassimo = p.tempoArrivo;
		}
		tempoTotale+=tempoArrivoMassimo;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Processo> coda = new ArrayList<Processo>();
		Processo p=processi.get(0);
		boolean processoInCpu = false;

		for(int t = 0; t < tempoTotale ; t++)
		{
			// se non è presente un processo in CPU allora lo devo scegliere
			if(! processoInCpu )
			{
				// controllo se sono arrivati nuovi processi
				// e li metto in coda
				// se hanno tempo di burst uguale a 0 non entrano nella coda
				coda.clear();
				for (int i = 0; i < processi.size();i++)			
					if( processi.get(i).tempoArrivo <= t && processi.get(i).burstResiduo > 0)
						coda.add(processi.get(i));
				// se la coda è vuota allora devo far passare il tempo e nessun processo verrà
				// eseguito
				
				if(!coda.isEmpty())
				{
					// scelgo il processo con il burst più basso
					p = coda.get(0);
					for (int i = 0; i < coda.size();i++)
						if ( coda.get(i).burst < p.burst)
							p = coda.get(i);
					
					processoInCpu=true;
				}
				else
					processoInCpu=false;
				
				// a questo punto: 1. Ho scelto il processo da eseguire
				//					2. Non è presente alcun processo da eseguire
			}
			
			if(processoInCpu)
			{
				list.add(p.nome);
				p.burstResiduo--;
				if(p.burstResiduo <= 0)
					processoInCpu=false;
			}
			else
				list.add("");

		}
		return list.toArray();
	}
	public Object[] logicaRr(int quanto){
		
		Collections.sort(processi, Processo.COMPARE_BY_ARRIVO);
		int tempoTotale=0;
		int tempoArrivoMassimo=0;
		for (int i = 0; i < processi.size();i++)
		{
			Processo p = processi.get(i);
			processi.get(i).burstResiduo = processi.get(i).burst;
			p.inQueue = false;
			p.justCome = false;
			p.finish=false;
			tempoTotale+=p.burst;
			if (tempoArrivoMassimo < p.tempoArrivo)
				tempoArrivoMassimo = p.tempoArrivo;
		}
		tempoTotale+=tempoArrivoMassimo;
		ArrayList<String> list = new ArrayList<String>();
		ArrayBlockingQueue<Processo> coda = new ArrayBlockingQueue(processi.size());
		ArrayBlockingQueue<Processo> codaPrecedente = new ArrayBlockingQueue(processi.size());
		
		Processo p=processi.get(0);
		boolean processoInCpu = false;
		int quantoRimasto=0;

		for(int t = 0; t < tempoTotale ; t++)
		{
			// devo far eseguire il processo per il tempo definito dal quanto
			// se è uguale a 0 allora devo scegliere un nuovo processo
			if(quantoRimasto <= 0)
			{
				// aggiorno il quanto
				quantoRimasto=quanto;
				System.out.println("Aggiorno il quanto");
				
				// copio i processi nella coda precedente
				if(!coda.isEmpty())
					codaPrecedente.addAll(coda);
				
				// azzero la coda
				coda.clear();

				// controllo se è arrivato un nuovo processo
				// se è arrivato lo pongo all'inizio della coda
				for (int i = 0; i < processi.size();i++)
					if( processi.get(i).tempoArrivo <= t && !processi.get(i).inQueue)
						{
							coda.add(processi.get(i));
							processi.get(i).inQueue=true;
						}
				
				if (!codaPrecedente.isEmpty())
				{
					System.out.println("Coda non vuota...aggiorno la coda");
					// salvo il primo elemento ( quello appena uscito )
					p = codaPrecedente.poll();
					
					while(!codaPrecedente.isEmpty())
					{
						if (codaPrecedente.peek().burstResiduo > 0)
							coda.add(codaPrecedente.poll());
					}
					if( p.burstResiduo > 0)
						coda.add(p);
						
					codaPrecedente.clear();
				}
				// se la coda è vuota allora devo far passare il tempo e nessun processo verrà
				// eseguito
				if(!coda.isEmpty())
					processoInCpu=true;
				else
					processoInCpu=false;

				// a questo punto: 1. Ho scelto il processo da eseguire
				//					2. Non è presente alcun processo da eseguire
			}
			if(processoInCpu)
			{
				p = coda.peek();
				list.add(p.nome);
				p.burstResiduo--;
				quantoRimasto--;
				if(p.burstResiduo <= 0)
					processoInCpu=false;
			}
			else
				{
					list.add("");
					quantoRimasto--;
				}
		}
		return list.toArray();
	}
}
