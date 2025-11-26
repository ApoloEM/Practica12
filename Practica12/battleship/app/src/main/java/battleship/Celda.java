package battleship;

public class Celda {
    private boolean hayBarco;
    private boolean impactada;
    private char simboloBarco;

    public Celda() {
        this.hayBarco = false;
        this.impactada = false;
        this.simboloBarco = ' ';
    }

    public void colocarBarco(char simbolo) {
        this.hayBarco = true;
        this.simboloBarco = simbolo;
    }

    public void recibirImpacto() {
        this.impactada = true;
    }

    public boolean estaImpactada() { return impactada; }
    public boolean tieneBarco() { return hayBarco; }
    public char getSimboloBarco() { return simboloBarco; }

    public char getRepresentacion(boolean ocultarBarcos) {
        if (impactada) {
            return hayBarco ? 'X' : 'O';
        }
        
        if (hayBarco && !ocultarBarcos) {
            return simboloBarco;
        }
        return '~';
    }
}