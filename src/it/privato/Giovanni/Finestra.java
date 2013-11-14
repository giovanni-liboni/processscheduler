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
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

public class Finestra extends JFrame{

	JFrame finestra;

	Object[] fcfsArray;
	DefaultTableModel tableModel;
	Table table;
	static int indexTable, maxRow=0;
	Processi processi;
	JTextField fieldAdd, fieldBurst,fieldT, fieldQuanto;
	int quanto = 1;
	JButton setQuanto;

	JPanel panelHrrn,panelSjf,panelFcfs,panelSrtf,panelRr;
	
	public Finestra(){
		finestra = new JFrame("Sistemi Operativi Teoria");
		finestra.setLayout(new GridLayout(6,1));
		finestra.setVisible(true);
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra.setLocationRelativeTo(null);
		finestra.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
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
					quanto = Integer.parseInt(fieldQuanto.getText());
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
		
		tableModel = new DefaultTableModel(new Object[] { "Nome Processo", "Tempo di Burst", "Tempo di arrivo" }, 0);
		
		tableModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int column = e.getColumn();
				int row = e.getFirstRow();
				TableModel model = (TableModel)e.getSource();
		        String columnName = model.getColumnName(column);
		        Object data = model.getValueAt(row, column);
		        
		        if(columnName.compareTo("Nome Processo") == 0){
		        	processi.add(new Processo((String) data,0,0));
		        }
		        if(columnName.compareTo("Tempo di Burst") == 0){
		        	
		        }
		        if(columnName.compareTo("Tempo di arrivo") == 0){
		        	
		        }
				
			}
		});
		
		table = new Table();
		
		table.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(), "Inserire i processi"));
		
		panel.add(table, BorderLayout.CENTER);
		
		JButton add = new JButton("Aggiungi");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!fieldAdd.getText().isEmpty() && !fieldBurst.getText().isEmpty() && !fieldT.getText().isEmpty())
				{
					String a = fieldAdd.getText();
					int b = Integer.parseInt(fieldBurst.getText());
					int c = Integer.parseInt(fieldT.getText());
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
				

				aggiornaPanel(panelFcfs, processi.logicaFcfs());
				aggiornaPanel(panelHrrn, processi.logicaHrrn());
				aggiornaPanel(panelSjf, processi.logicaSjf());
				aggiornaPanel(panelSrtf, processi.logicaSrtf());
				aggiornaPanel(panelRr, processi.logicaRr(quanto));
			}
		});
		JPanel right = new JPanel(new BorderLayout());
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,
				BoxLayout.LINE_AXIS));
		buttonPane.add(add);
		buttonPane.add(Box.createHorizontalStrut(2));
		buttonPane.add(remove);
		buttonPane.add(Box.createHorizontalStrut(2));
		buttonPane.add(clean);
		buttonPane.add(Box.createHorizontalStrut(2));
		buttonPane.add(calcola);
		buttonPane.add(Box.createHorizontalStrut(2));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		
		right.add(panelInput, BorderLayout.CENTER);
		right.add(buttonPane, BorderLayout.SOUTH);
		
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
}
