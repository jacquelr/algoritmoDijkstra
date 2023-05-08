/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ventanas;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author jacqueline
 */
public class VtnDijkstra extends javax.swing.JFrame {
    VtnInicio objVtnInicio = null;
    
    int filas = 10;
    int columnas = 10;
    Nodo matrizLogica[][];
    JButton celdas[][];
    Nodo inicioLogico;
    
    //algoritmo
    boolean busquedaActiva = true; 
    int abiertos = 0;


    //MAQUINA DE ESTADOS
    private static int nodoVacio = 0;
    private static int nodoInicio = 1;
    private static int nodoFinal = 2;
    private static int pared = 3;
    private static int camino = 4;
    private static int nodoVisitado = 5;
    
    /**
     * Creates new form VtnDijkstra
     */
    public VtnDijkstra() {
        initComponents();
        iniciarPrograma();
    }
    
    public VtnDijkstra(VtnInicio objVtnInicio, int filas, int columnas) {
        initComponents();
        this.objVtnInicio = objVtnInicio;
        this.filas = filas;
        this.columnas = columnas;
        
        iniciarPrograma();
    }
    
    public void iniciarPrograma(){
        crearMatrices();
    }
    
    public void crearMatrices(){
        pnlCuadricula.setLayout(new GridLayout (filas, columnas));
        matrizLogica = new Nodo [filas][columnas];
        celdas = new JButton[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizLogica[i][j] = new Nodo();
                celdas[i][j] = new JButton();
                pnlCuadricula.add(celdas[i][j]);
                celdas[i][j].addActionListener(new Acciones(matrizLogica, celdas, filas, columnas, i, j));
                celdas [i][j].setBackground(Color.white);
            
                iniciarMatriz(matrizLogica[i][j], i, j);
            }
        }
    }
    
    public void iniciarMatriz(Nodo nodoActual, int fila, int columna){
        nodoActual.setFila(fila);
        nodoActual.setColumna(columna);
        nodoActual.setEstado(nodoVacio);
        nodoActual.setSaltos(-1);
    }
     
    
    public void Dijkstra(){
        int startx = inicioLogico.getFila();
        int starty = inicioLogico.getColumna();
        ArrayList<Nodo> prioridad = new ArrayList<>();
	prioridad.add(matrizLogica[startx][starty]);
        
	while(busquedaActiva) {
            if(prioridad.size() <= 0) {
                busquedaActiva = false;
                JOptionPane.showMessageDialog(null, "No hay ningún camino");
                break;
            }
            
            int saltos = prioridad.get(0).saltos + 1;
            ArrayList<Nodo> visitados = buscarVecinos(prioridad.get(0), saltos);
            
            if(visitados.size() > 0) {
                prioridad.remove(0);
		prioridad.addAll(visitados);
		MyRepaint();
                //delay(30);
            } else {
		prioridad.remove(0);
            }
	}
    }
    
    public ArrayList<Nodo> buscarVecinos(Nodo actual, int saltos) {
	ArrayList<Nodo> visitados = new ArrayList<>();
	for(int a = -1; a <= 1; a++) {
            for(int b = -1; b <= 1; b++) {
		int xVecino = actual.fila + a;
		int yVecino = actual.columna + b;
		if((xVecino > -1 && xVecino < filas) && (yVecino > -1 && yVecino < columnas)) {
                    Nodo vecino = matrizLogica[xVecino][yVecino];
                    if((vecino.saltos==-1 || vecino.saltos > saltos) && vecino.estado!= pared) {
			revisarVecino(vecino, actual.fila, actual.columna, saltos);	
			visitados.add(vecino);
                    }
		}
            }
	}
        
	return visitados;
    }
    
    public void revisarVecino(Nodo actual, int xAnterior, int yAnterior, int saltos) {
	if(actual.estado!=1 && actual.estado != 2)
            actual.setEstado(nodoVisitado);
	actual.setNodoAnterior(xAnterior, yAnterior);
	actual.setSaltos(saltos);
	abiertos++;
                        
	if(actual.estado == 2) {
            camino(actual.getFilaAnterior(), actual.getColumnaAnterior(),saltos);
	}
    }
    
    public void camino(int xA, int yA, int saltos) {
	while(saltos > 0) {
            Nodo actual = matrizLogica[xA][yA];
            actual.setEstado(camino);
            xA = actual.getFilaAnterior();
            yA = actual.getColumnaAnterior();
            saltos--;
	}
        nodosAbiertos.setText("Nodos abiertos: "+ abiertos);
	busquedaActiva = false;
    }
    
    public void pruebaenconsola(){
        //prueba en consola
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.println("FUNCIONPRUEBAENCONSOLA");
                System.out.println(matrizLogica[i][j].fila + "." + matrizLogica[i][j].columna);
                System.out.println(matrizLogica[i][j].estado);
            }
        }
    } 
    

    public void delay() {
	try {
            Thread.sleep(30);
	} catch(Exception e) {}
    }
    
    public void MyRepaint (){
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                switch (matrizLogica[i][j].estado){
                    case 0: 
                        celdas[i][j].setBackground(Color.WHITE);
                        break;
                    case 1: 
                        celdas[i][j].setBackground(Color.PINK);
                        break;
                    case 2:
                        celdas[i][j].setBackground(Color.MAGENTA);
                        break;
                    case 3:
                        celdas[i][j].setBackground(Color.CYAN);
                        break;
                    case 4:
                        celdas[i][j].setBackground(Color.RED);
                        break;
                    case 5:
                        celdas[i][j].setBackground(Color.GREEN);
                        break;
                }
            }
        }
    }
    
    public class Nodo{
        private int estado;
        private int fila;
        private int columna;
        private int filaAnterior;
        private int columnaAnterior;
        private int saltos;

        
        public int getEstado() {
            return estado;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }

        public int getFila() {
            return fila;
        }

        public void setFila(int fila) {
            this.fila = fila;
        }

        public int getColumna() {
            return columna;
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }

        public int getFilaAnterior() {
            return filaAnterior;
        }

        public void setFilaAnterior(int filaAnterior) {
            this.filaAnterior = filaAnterior;
        }

        public int getColumnaAnterior() {
            return columnaAnterior;
        }

        public void setColumnaAnterior(int columnaAnterior) {
            this.columnaAnterior = columnaAnterior;
        }

        public int getSaltos() {
            return saltos;
        }

        public void setSaltos(int saltos) {
            this.saltos = saltos;
        }
        
        public void setNodoAnterior(int x, int y) {filaAnterior = x; columnaAnterior = y;}
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoSeleccion = new javax.swing.ButtonGroup();
        pnlFondo = new javax.swing.JPanel();
        pnlCuadricula = new javax.swing.JPanel();
        lblAlgoritmo = new javax.swing.JLabel();
        lblDijkstra = new javax.swing.JLabel();
        rdInicio = new javax.swing.JRadioButton();
        rdFinal = new javax.swing.JRadioButton();
        rdPared = new javax.swing.JRadioButton();
        btnDijkstra = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        colorInicio = new javax.swing.JPanel();
        colorFinal = new javax.swing.JPanel();
        colorPared = new javax.swing.JPanel();
        colorCamino = new javax.swing.JPanel();
        colorVisitados = new javax.swing.JPanel();
        lblVisitados = new javax.swing.JLabel();
        lblInicio1 = new javax.swing.JLabel();
        lblCamino = new javax.swing.JLabel();
        lblPared = new javax.swing.JLabel();
        lblFinal = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        nodosAbiertos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pnlFondo.setBackground(new java.awt.Color(254, 243, 255));

        pnlCuadricula.setBackground(new java.awt.Color(254, 231, 255));
        pnlCuadricula.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout pnlCuadriculaLayout = new javax.swing.GroupLayout(pnlCuadricula);
        pnlCuadricula.setLayout(pnlCuadriculaLayout);
        pnlCuadriculaLayout.setHorizontalGroup(
            pnlCuadriculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        pnlCuadriculaLayout.setVerticalGroup(
            pnlCuadriculaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        lblAlgoritmo.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        lblAlgoritmo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlgoritmo.setText("ALGORITMO DE ");
        lblAlgoritmo.setToolTipText("");

        lblDijkstra.setFont(new java.awt.Font("Kefa", 0, 48)); // NOI18N
        lblDijkstra.setText("DIJKSTRA");

        tipoSeleccion.add(rdInicio);
        rdInicio.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        rdInicio.setText("Inicio");

        tipoSeleccion.add(rdFinal);
        rdFinal.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        rdFinal.setText("Final");

        tipoSeleccion.add(rdPared);
        rdPared.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        rdPared.setText("Pared");

        btnDijkstra.setBackground(new java.awt.Color(0, 0, 0));
        btnDijkstra.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnDijkstra.setForeground(new java.awt.Color(255, 255, 255));
        btnDijkstra.setText("Aplicar Dijkstra");
        btnDijkstra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDijkstraActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiar.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        colorInicio.setBackground(new java.awt.Color(255, 175, 175));
        colorInicio.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout colorInicioLayout = new javax.swing.GroupLayout(colorInicio);
        colorInicio.setLayout(colorInicioLayout);
        colorInicioLayout.setHorizontalGroup(
            colorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        colorInicioLayout.setVerticalGroup(
            colorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        colorFinal.setBackground(new java.awt.Color(255, 0, 255));
        colorFinal.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout colorFinalLayout = new javax.swing.GroupLayout(colorFinal);
        colorFinal.setLayout(colorFinalLayout);
        colorFinalLayout.setHorizontalGroup(
            colorFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        colorFinalLayout.setVerticalGroup(
            colorFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        colorPared.setBackground(new java.awt.Color(0, 255, 255));
        colorPared.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout colorParedLayout = new javax.swing.GroupLayout(colorPared);
        colorPared.setLayout(colorParedLayout);
        colorParedLayout.setHorizontalGroup(
            colorParedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        colorParedLayout.setVerticalGroup(
            colorParedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        colorCamino.setBackground(new java.awt.Color(255, 0, 0));
        colorCamino.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout colorCaminoLayout = new javax.swing.GroupLayout(colorCamino);
        colorCamino.setLayout(colorCaminoLayout);
        colorCaminoLayout.setHorizontalGroup(
            colorCaminoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        colorCaminoLayout.setVerticalGroup(
            colorCaminoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        colorVisitados.setBackground(new java.awt.Color(0, 255, 0));
        colorVisitados.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout colorVisitadosLayout = new javax.swing.GroupLayout(colorVisitados);
        colorVisitados.setLayout(colorVisitadosLayout);
        colorVisitadosLayout.setHorizontalGroup(
            colorVisitadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        colorVisitadosLayout.setVerticalGroup(
            colorVisitadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        lblVisitados.setText("VISITADOS");

        lblInicio1.setText("INICIO");

        lblCamino.setText("CAMINO");

        lblPared.setText("PARED");

        lblFinal.setText("FINAL");

        btnVolver.setText("VOLVER");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        nodosAbiertos.setFont(new java.awt.Font("Hiragino Mincho ProN", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAlgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDijkstra)
                                    .addComponent(rdInicio)
                                    .addComponent(rdFinal)
                                    .addComponent(rdPared)))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(btnDijkstra))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(btnLimpiar)))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFondoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(colorInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblInicio1))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(colorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFinal))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(colorPared, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPared))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(colorCamino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCamino)))
                        .addGap(181, 181, 181)))
                .addComponent(pnlCuadricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorVisitados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVisitados)
                .addGap(195, 195, 195)
                .addComponent(nodosAbiertos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolver)
                .addGap(27, 27, 27))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(pnlCuadricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVolver)
                            .addComponent(nodosAbiertos)))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(lblAlgoritmo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDijkstra)
                        .addGap(39, 39, 39)
                        .addComponent(rdInicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdFinal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdPared)
                        .addGap(27, 27, 27)
                        .addComponent(btnDijkstra)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(72, 72, 72)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblInicio1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(colorInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(colorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(colorPared, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPared, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(colorCamino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorVisitados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(lblCamino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblVisitados, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDijkstraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDijkstraActionPerformed
        rdInicio.setEnabled(false);
        rdFinal.setEnabled(false);
        rdPared.setEnabled(false);
        btnDijkstra.setEnabled(false);
        try{
            Dijkstra();
        }catch(Exception e){
            System.out.println("Error en el algortimo");
        }
    }//GEN-LAST:event_btnDijkstraActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        inicioLogico = null;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
               matrizLogica[i][j] = new Nodo();
               matrizLogica[i][j].estado = 0;
               iniciarMatriz(matrizLogica[i][j], i, j);
            }
        }
        busquedaActiva = true;
	abiertos = 0;
        rdInicio.setEnabled(true);
        rdFinal.setEnabled(true);
        rdPared.setEnabled(true);
        btnDijkstra.setEnabled(true);
        MyRepaint();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        objVtnInicio.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    public class Acciones implements ActionListener{
    private int x;
    private int y;
    private int filas;
    private int columnas;
    Nodo matrizLogica[][];
    JButton celdas[][];
    
    public Acciones(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Acciones(Nodo matrizLogica[][] , JButton celdas[][], int filas, int columnas, int x, int y) {
        this.x = x;
        this.y = y;
        this.filas = filas;
        this.columnas = columnas;
        this.matrizLogica = matrizLogica;
        this.celdas = celdas;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        if(rdInicio.isSelected()){
            validarUnico(nodoInicio);
            if(matrizLogica[x][y].estado == 0){
                matrizLogica [x][y].estado = nodoInicio;
                celdas[x][y].setBackground(Color.PINK);
                inicioLogico = matrizLogica[x][y];
            }else{
                System.out.println("Celda ocupada");
            }
        }else if(rdFinal.isSelected()){
            validarUnico(nodoFinal);
            if(matrizLogica[x][y].estado == 0){
                matrizLogica [x][y].estado = nodoFinal;
                celdas[x][y].setBackground(Color.MAGENTA);
            }else{
                System.out.println("Celda ocupada");
            }
        }else if(rdPared.isSelected()){
            if(matrizLogica[x][y].estado == 0){
                matrizLogica [x][y].estado = pared;
                celdas[x][y].setBackground(Color.CYAN);
            }else if (matrizLogica[x][y].estado == 3){
                matrizLogica [x][y].estado = nodoVacio;
                celdas[x][y].setBackground(Color.WHITE);
            }
        }else{
            System.out.println("error en el tipo de seleccion");
        }
    }
    
    public void validarUnico(int a){
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matrizLogica[i][j].estado  == a) {
                    
                    matrizLogica[i][j].estado  = 0;
                    celdas[i][j].setBackground(Color.white);
                  
                }
            }
        }
    }
    
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VtnDijkstra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VtnDijkstra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VtnDijkstra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VtnDijkstra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VtnDijkstra().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDijkstra;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JPanel colorCamino;
    private javax.swing.JPanel colorFinal;
    private javax.swing.JPanel colorInicio;
    private javax.swing.JPanel colorPared;
    private javax.swing.JPanel colorVisitados;
    private javax.swing.JLabel lblAlgoritmo;
    private javax.swing.JLabel lblCamino;
    private javax.swing.JLabel lblDijkstra;
    private javax.swing.JLabel lblFinal;
    private javax.swing.JLabel lblInicio1;
    private javax.swing.JLabel lblPared;
    private javax.swing.JLabel lblVisitados;
    private javax.swing.JLabel nodosAbiertos;
    private javax.swing.JPanel pnlCuadricula;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JRadioButton rdFinal;
    private javax.swing.JRadioButton rdInicio;
    private javax.swing.JRadioButton rdPared;
    private javax.swing.ButtonGroup tipoSeleccion;
    // End of variables declaration//GEN-END:variables
}
