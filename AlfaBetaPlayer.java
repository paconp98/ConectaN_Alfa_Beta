/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conectan;

/**
 *
 * @author Francisco Nieves Pastor
 * Trabajado sobre la versión base proporcionada por:
 * @version 1.3 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase AlfaBetaPlayer para representar al jugador CPU que usa la poda Alfa
 * Beta
 *
 * Esta clase es la que tenemos que implementar y completar
 *
 */
public class AlfaBetaPlayer extends Player {
    
    private final int nivelProfundidaMax = 6;

    private int alto;
    private int ancho;
    private int conecta;

    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int jugada(Grid tablero, int conecta) {

        this.alto = tablero.getFilas();
        this.ancho = tablero.getColumnas();
        
        this.conecta = conecta;

        int valorAlgoritmo;
        int valorMaximo = Integer.MIN_VALUE;
        int columnaFinal = -1;
        int filaFinal = -1;

        // Para cada columna comprobamos que puntuación obtenemos, y nos quedamos con la más alta
        for (int columna = 0; columna < ancho; columna++) {
            int fila = alto - 1;
            
            // Comprobamos que disponemos de una fila libre donde colocar la ficha
            while (fila >= 0 && tablero.toArray()[fila][columna] != 0) {
                fila--;
            }
            
            // Si no hay hueco disponible, no hacemos nada y pasamos a la siguiente columna
            if (fila != -1) {
                
                // Realizamos una copia del tablero, una por cada columna, de manera que permita ejecutar cada uno de los movimientos posibles
                int aux[][] = new int[alto][ancho];
                for (int i = 0; i < alto; i++) {
                    for (int j = 0; j < ancho; j++) {
                        aux[i][j] = tablero.toArray()[i][j];
                    }
                }
                
                // Asignamos el primer movimiento al jugador
                aux[fila][columna] = 1;

                // Ejecutamos el algoritmo de Poda a partir de la copia de la matriz
                valorAlgoritmo = MaxValor(aux, Integer.MIN_VALUE, Integer.MAX_VALUE, ConectaN.JUGADOR2, 0);

                // Si el valor obtenido en el algoritmo es superior al hasta entonces almacenado, lo actualizamos
                if (valorAlgoritmo > valorMaximo) {
                    valorMaximo = valorAlgoritmo;
                    columnaFinal = columna;
                    filaFinal = fila;
                }
            }
        }
        
        // Finalmente asignamos donde vamos a colocar la ficha
        tablero.setButton(columnaFinal, ConectaN.JUGADOR2);
        
        // Y comprobamos si con el movimiento realizado se ha ganado la partida
        return tablero.checkWin(filaFinal, columnaFinal, conecta);
    }
    
    private void copiaTablero(int[][] tablero, int[][] aux, int alto, int ancho){
        for (int i = 0; i < alto; i++) {
            System.arraycopy(tablero[i], 0, aux[i], 0, ancho);
                }
    }

    public int MaxValor(int[][] tablero, int alfa, int beta, int jugador, int profundidad) {
        
        // Si el tablero está lleno y no hay ganador, no realizamos nada
        if ((tableroLleno(tablero)) && (ganador(tablero) == 0)) {
            return 0;
        }

        // Si es nodo final, devolvemos el valor de la función de Utilidad
        if (testFinal(tablero)) {
            return FuncionUtilidad(tablero);
        }

        // Si hemos alcanzado un nodo hoja, devolvemos la heurística
        if (profundidad == nivelProfundidaMax) {
            return Heuristica(tablero, jugador);
        }

        // Aumentamos en 1 la profundidad del árbol que estamos generando
        profundidad++;

        // Para cada columna vamos actualizando el valor de alfa con el más elevado, lo que nos dirá la mejor jugada posible
        for (int columna = 0; columna < ancho; columna++) {
            int fila = alto - 1;
            
            // Comprobamos que disponemos de una fila libre donde colocar la ficha
            while (fila >= 0 && tablero[fila][columna] != 0) {
                fila--;
            }
            
            // Si no la hay, no hacemos nada
            if (fila != -1) {
                
                // Realizamos una copia del tablero, una por cada columna, de manera que permita ejecutar cada uno de los movimientos posibles
                int aux[][] = new int[alto][ancho];
                copiaTablero(tablero,aux,alto,ancho);
                
                // Y asignamos el jugador actual en las posiciones encontradas
                aux[fila][columna] = jugador;

                // Calculamos el valor MIN para el jugador opuesto al acutal
                int sucesor = MinValor(aux, alfa, beta, rival(jugador), profundidad);

                // Y escogemos el valor más elevado entre alfa y el valor MIN del otro jugador, es decir, escogemos el mejor movimiento posible para nosotros
                alfa = Math.max(alfa, sucesor);

                // Si alfa es mayor que beta, podamos
                if (alfa >= beta) {
                    return alfa;
                }
            }
        }
        return alfa;
    }

    private int MinValor(int[][] tablero, int alfa, int beta, int jugador, int profundidad) {
        
        // Si el tablero está lleno y no hay ganador, no realizamos nada
        if ((tableroLleno(tablero)) && (ganador(tablero) == 0)) {
            return 0;
        }

        // Si es nodo final, devolvemos el valor de la función de Utilidad
        if (testFinal(tablero)) {
            return FuncionUtilidad(tablero);
        }

        // Si hemos alcanzado un nodo hoja, devolvemos la heurística
        if (profundidad == nivelProfundidaMax) {
            return Heuristica(tablero, jugador);
        }

        // Aumentamos en 1 la profundidad del árbol que estamos generando
        profundidad++;

        
        // Para cada columna vamos actualizando el valor de beta por el valor mas reducido, de manera que podemos saber que jugada es la que más perjudica a nuestro rival
        for (int columna = 0; columna < ancho; columna++) {
            int fila = alto - 1;
            
            // Comprobamos que disponemos de una fila libre donde colocar la ficha
            while (fila >= 0 && tablero[fila][columna] != 0) {
                fila--;
            }
            
            // Si no la hay, no hacemos nada
            if (fila != -1) {

                // Realizamos una copia del tablero, una por cada columna, de manera que permita ejecutar cada uno de los movimientos posibles
                int aux[][] = new int[alto][ancho];
                copiaTablero(tablero,aux,alto,ancho);
                
                // Y asignamos el jugador actual en las posiciones encontradas
                aux[fila][columna] = jugador;

                // Calculamos el valor MAX para el jugador opuesto al acutal
                int sucesor = MaxValor(aux, alfa, beta, rival(jugador), profundidad);

                // Y escogemos el valor más reducido entre beta y el valor MAX del otro jugador, es decir, escogemos el peor movimiento para el rival
                beta = Math.min(beta, sucesor);
            }
        }
        return beta;
    }

    // Función que se encarga de calcular la puntuación obtenida para cada movimiento en base al estado del tablero.
    private int Heuristica(int[][] tablero, int jugador) {
        int H;
        if (jugador == ConectaN.JUGADOR2) {
            H = valorFichas(tablero, rival(jugador)) - 3 * valorFichas(tablero, jugador);
        } else {
            H = valorFichas(tablero, jugador) - 3 * valorFichas(tablero, rival(jugador));
        }
        return H;
    }
    
    // Función que devuelve un valor muy elevado en caso de que gane el jugador actual, y un valor muy malo en caso de que gane el oponente
    private int FuncionUtilidad(int[][] tablero) {
        if (ganador(tablero) == 1)
            return 9999999;
        else if (ganador(tablero) == -1)
            return -9999999;
        else 
            return 0;
    }

    // Función que comprueba si existe un ganador en todo el tablero
    private int ganador(int[][] tablero) {
        boolean arribaPuede, abajoPuede, derechaPuede, izquierdaPuede;
        int ganador = 0, jugador, cpu;
        
        // Comprobamos en todo el tablero si ya ha habido un ganador
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                
                // Miramos únicamente las casillas en las que hay fichas, de manera que, nos ahorramos tiempo de procesamiento
                if (tablero[i][j] != 0) {
                    
                    // Comprobamos hacía qué sentidos es posible realizar un N en raya
                    arribaPuede = i - (conecta - 1) >= 0;
                    abajoPuede = i + (conecta - 1) < alto;
                    derechaPuede = j + (conecta - 1) < ancho;
                    izquierdaPuede = j - (conecta - 1) >= 0;

                    // Y si es posible, vamos comprobando si existen fichas que hayan conseguido el N en raya, y por tanto hayan vencido, en las distintas direcciones: 
                    if (arribaPuede) // Derecha
                    {
                        jugador = 0;
                        cpu = 0;
                        
                        // Nos desplazamos hacia arriba N posiciones comprobando que fichas están en esa línea
                        for (int k = 0; k < conecta; k++) {
                            
                            // Si son del jugador, incrementamos el contador del jugador
                            if (tablero[i - k][j] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            
                            // Si son de la máquina, incrementamos el contador de la máquina
                            if (tablero[i - k][j] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        
                        // Si el número de fichas coincide con N, el jugador ha hecho N en raya 
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        // Si el número de fichas coincide con N, la máquina ha hecho N en raya
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }

                    
                    if (abajoPuede) // Abajo
                    {
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i + k][j] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }

                    if (derechaPuede) // Derecha
                    {
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i][j + k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i][j + k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }

                    if (izquierdaPuede) // Izquierda
                    {
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[k][j - k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[k][j - k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }
                    
                    if (abajoPuede && derechaPuede) { // AbajoDerecha
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j + k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i + k][j + k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }
                    
                    if (arribaPuede && derechaPuede) { // ArribaDerecha
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i - k][j + k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i - k][j + k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }
                    
                    if (arribaPuede && izquierdaPuede) { // ArribaIzquierda
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i - k][j - k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i - k][j - k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }

                    if (abajoPuede && izquierdaPuede) { // AbajoIzquierda
                        jugador = 0;
                        cpu = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j - k] == ConectaN.JUGADOR1) {
                                jugador++;
                            }
                            if (tablero[i + k][j - k] == ConectaN.JUGADOR2) {
                                cpu++;
                            }
                        }
                        if (jugador == conecta) {
                            return ConectaN.JUGADOR1;
                        }
                        if (cpu == conecta) {
                            return ConectaN.JUGADOR2;
                        }
                    }
                }
            }
        }
        return ganador;
    }
    
    // Devuelve el jugador contrario al que se le pasa
    private int rival(int jugador) {
        if(jugador == ConectaN.JUGADOR2)
            return 1;
        else
            return ConectaN.JUGADOR2;
    }

    // Función que comprueba si ha acabado la partida, ya sea porque hay un ganador o porque el tablero está lleno
    private boolean testFinal(int[][] tablero) {
        return ganador(tablero) != 0 || tableroLleno(tablero);
    }

    // Función que comprueba si el tablero está lleno
    private boolean tableroLleno(int[][] tablero) {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (tablero[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Función encargada de atribuir un valor a las fichas en línea, y es utilizada por la heurística.
    private int valorFichas(int[][] tablero, int jugador) {
        boolean arribaPuede, abajoPuede, derechaPuede, izquierdaPuede;
        int fichas = 0;
        int contador;
        
        // Recorremos todo el tablero
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                
                // Miramos los sitios posibles hacia los que se puede hacer un N en raya
                arribaPuede = i - (conecta - 1) >= 0;
                abajoPuede = i + (conecta - 1) < alto;
                derechaPuede = j + (conecta - 1) < ancho;
                izquierdaPuede = j - (conecta - 1) >= 0;

                // Miramos únicamente las casillas en las que hay fichas, de manera que, nos ahorramos tiempo de procesamiento
                if (tablero[i][j] != 0) {
                    
                    // Comprobamos si se puede, cuantas fichas son de nuestro rival, y si no tiene, cuantas son nuestras
                    if (arribaPuede)// Arriba
                    {
                        contador = 0;
                        
                        // Contamos cuantas fichas, en la línea de N fichas hacia arriba, son de nuestro oponente
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i - k][j] == rival(jugador)) {
                                contador++;
                            }
                        }
                        
                        // Si el oponente no tiene fichas, comprobamos cuantas fichas son nuestras
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i - k][j] == jugador) {
                                    contador++;
                                }
                            }
                            
                            // Asignamos una puntuación proporcional al número de fichas que son nuestras
                            fichas += contador * 120;
                        }
                    }
                    
                    // Realizamos lo mismo para todas las direcciones restantes
                    if (abajoPuede)// Abajo
                    {
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i + k][j] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }

                    if (derechaPuede)// Derecha
                    {
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i][j + k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i][j + k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }

                    if (izquierdaPuede)// Izquierda
                    {
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i][j - k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i][j - k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }
                    
                    if (arribaPuede && derechaPuede) { // ArribaDerecha
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i - k][j + k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i - k][j + k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }
                    
                    if (abajoPuede && derechaPuede) { // AbajoDerecha
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j + k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i + k][j + k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }
                    
                    if (arribaPuede && izquierdaPuede) { // ArribaIzquierda
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i - k][j - k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i - k][j - k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }

                    if (abajoPuede && izquierdaPuede) { // Abajo Izquierda
                        contador = 0;
                        for (int k = 0; k < conecta; k++) {
                            if (tablero[i + k][j - k] == rival(jugador)) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            for (int k = 0; k < conecta; k++) {
                                if (tablero[i + k][j - k] == jugador) {
                                    contador++;
                                }
                            }
                            fichas += contador * 120;
                        }
                    }
                }
            }
        }
        return fichas;
    }

} // AlfaBetaPlayer
