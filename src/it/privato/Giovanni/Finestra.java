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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.table.*;

public class Finestra extends JFrame{

	JFrame finestra;

	Object[] fcfsArray;
	Table table;
	static int indexTable, maxRow=0;
	Processi processi;
	JTextField fieldAdd, fieldBurst,fieldT, fieldQuanto;
	int quanto = 1;
	JButton setQuanto;

	JPanel panelHrrn,panelSjf,panelFcfs,panelSrtf,panelRr;
	JPanel panelLog;
	JTextArea logArea;
	private PrintStream standardOut;
	
	public Finestra(){
		finestra = new JFrame("Sistemi Operativi Teoria");
		finestra.setLayout(new GridLayout(6,1));
		finestra.setVisible(true);
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra.setLocationRelativeTo(null);
		finestra.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// LOG PANEL
		panelLog = new JPanel();
		
		logArea = new JTextArea("Programma per verificare gli esercizi del corso di Sistemi Operativi\n",7,45);
		logArea.setVisible(true);
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelLog.add(new JScrollPane(scrollPane));
		logArea.setEditable(false);
        PrintStream printStream = new PrintStream(new CustomOutputStream(logArea));
         
        // keeps reference of standard output stream
        standardOut = System.out;
         
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
		
		
		processi = new Processi();
		
		finestra.add(input());
		finestra.add(sjf());
		finestra.add(sjfp());
		finestra.add(fcfs());
		finestra.add(hrrn());
		finestra.add(rr());
		
	}
	public JPanel input(){
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panelInput = new JPanel(new BorderLayout());
		GroupLayout groupLayout = new GroupLayout(panelInput);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		
		JLabel labelAdd = new JLabel("Nome ");
		JLabel labelBurst = new JLabel("Burst ");
		JLabel labelT = new JLabel("T. arrivo ");
		JLabel labelQ = new JLabel("Quanto");
		
		fieldAdd = new JTextField(8);
		fieldBurst = new JTextField(8);
		fieldT = new JTextField(8);
		fieldQuanto = new JTextField(8);
		setQuanto = new JButton("Set quanto");
		setQuanto.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!fieldQuanto.getText().isEmpty()){
					String qString = fieldQuanto.getText().replaceAll(" ", "");
					if(isInteger(qString))
					{
						quanto = Integer.parseInt(qString);
						System.out.println("Quanto impostato a : " + quanto);
						fieldQuanto.setText("");
					}
					else
					{
						System.out.println("Immettere un numero intero");
						fieldQuanto.setText("");
					}
				}

			}
		});
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(labelAdd)
						.addComponent(labelBurst)
						.addComponent(labelT)
						)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(fieldAdd)
						.addComponent(fieldBurst)
						.addComponent(fieldT)
						)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(labelQ)
						)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(fieldQuanto)
						.addComponent(setQuanto)
						)

		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelAdd)
						.addComponent(fieldAdd)
						.addComponent(labelQ)
						.addComponent(fieldQuanto)
						)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelBurst)
						.addComponent(fieldBurst)
						.addComponent(setQuanto)
						)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelT)
						.addComponent(fieldT)
						)
		);
		panelInput.setLayout(groupLayout);
	
		table = new Table();
		
		table.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Inserire i processi"));
		
		
		
		JButton add = new JButton("Aggiungi");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				

				if(!fieldAdd.getText().isEmpty() && 
						!fieldBurst.getText().isEmpty() && 
						!fieldT.getText().isEmpty()
						)
				{
					String burstString = fieldBurst.getText().replaceAll(" ", "");
					String tString = fieldT.getText().replaceAll(" ", "");
					if(isInteger(burstString) && 
							isInteger(tString))
					{
						String a = fieldAdd.getText();
						int b = Integer.parseInt(burstString);
						int c = Integer.parseInt(tString);
						processi.add(new Processo(a, c,b));
						processi.ordina();
						table.updateModel(processi.processi);
						maxRow++;
						fieldAdd.setText("");
						fieldBurst.setText("");
						fieldT.setText("");
						indexTable=processi.processi.size()-1;
						fieldAdd.requestFocus();
						fieldAdd.requestFocusInWindow();
						System.out.println("Nuovo processo : " + a );
					}
				}
			}
		});
		JButton remove = new JButton("Rimuovi");
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(indexTable >= 0 && processi.processi.size() > 0)
				{
					processi.processi.remove(indexTable);
					processi.ordina();
					table.updateModel(processi.processi);
					indexTable=processi.processi.size()-1;
				}

			}
		});
		JButton clean = new JButton("Pulisci");
		clean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				processi.clear();
				table.updateModel(processi.processi);
				panelFcfs.removeAll();
				panelHrrn.removeAll();
				panelRr.removeAll();
				panelSjf.removeAll();
				panelSrtf.removeAll();
				
				panelFcfs.validate();
				panelHrrn.validate();
				panelRr.validate();
				panelSjf.validate();
				panelSrtf.validate();
				
				panelFcfs.updateUI();
				panelHrrn.updateUI();
				panelRr.updateUI();
				panelSjf.updateUI();
				panelSrtf.updateUI();
				
				indexTable=-1;
			}
		});
		JButton calcola = new JButton("Calcola");
		calcola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panelFcfs.removeAll();
				panelHrrn.removeAll();
				panelRr.removeAll();
				panelSjf.removeAll();
				panelSrtf.removeAll();
				
				if(!processi.processi.isEmpty())
				{
					aggiornaPanel(panelFcfs, processi.logicaFcfs());
					aggiornaPanel(panelHrrn, processi.logicaHrrn());
					aggiornaPanel(panelSjf, processi.logicaSjf());
					aggiornaPanel(panelSrtf, processi.logicaSrtf());
					aggiornaPanel(panelRr, processi.logicaRr(quanto));
				}
			}
		});
		JPanel right = new JPanel(new BorderLayout());
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,
				BoxLayout.LINE_AXIS));
		buttonPane.add(add);
		buttonPane.add(Box.createHorizontalStrut(1));
		buttonPane.add(remove);
		buttonPane.add(Box.createHorizontalStrut(1));
		buttonPane.add(clean);
		buttonPane.add(Box.createHorizontalStrut(1));
		buttonPane.add(calcola);
		buttonPane.add(Box.createHorizontalStrut(1));
		
		right.add(panelInput, BorderLayout.CENTER);
		right.add(buttonPane, BorderLayout.SOUTH);
		
		panel.add(table, BorderLayout.WEST);
		panel.add(panelLog, BorderLayout.CENTER);
		panel.add(right, BorderLayout.EAST);
		return panel;
		
	}
	public JPanel sjfp(){

		panelSrtf = new JPanel(new GridLayout());
		panelSrtf.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduling SRTF"));
		return panelSrtf;
		
	}
	public JPanel sjf(){
		panelSjf = new JPanel();
		panelSjf.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduling SJF"));
		return panelSjf;
		
	}
	public JPanel fcfs(){
		panelFcfs = new JPanel(new GridLayout());
		panelFcfs.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduling FCFS"));
		return panelFcfs;
		
	}
	public JPanel hrrn(){
		panelHrrn = new JPanel(new GridLayout());
		panelHrrn.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduling HRRN"));
		return panelHrrn;
	}
	public JPanel rr(){
		panelRr = new JPanel();
		panelRr.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduling Round-Robin"));
		return panelRr;
	}
	public void aggiornaPanel(JPanel panel, Object[] array){
		
		
		panel.setLayout(new GridLayout(2, array.length + 1));
		panel.add(new JLabel("T   "));
		for(int i = 0; i < array.length; i++){
			panel.add(new JLabel(String.valueOf(i)));
		}
		panel.add(new JLabel("P   "));
		for(int i = 0; i < array.length; i++)
		{
			panel.add(new JLabel((String)array[i]));
		}
		panel.validate();
		panel.updateUI();
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
}