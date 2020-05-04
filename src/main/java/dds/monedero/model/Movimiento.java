package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
    private LocalDate fecha;
    //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
    //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
    private double monto;
    private boolean esDeposito;

    public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
        this.fecha = fecha;
        this.monto = monto;
        this.esDeposito = esDeposito;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean fueDepositado(LocalDate fecha) {
        return isDeposito() && esDeLaFecha(fecha);
    }

    public boolean fueExtraido(LocalDate fecha) {
        return isExtraccion() && esDeLaFecha(fecha);
    }

    public boolean esDeLaFecha(LocalDate fecha) {
        return this.fecha.equals(fecha);
    }

    public boolean isDeposito() {
        return esDeposito;
    }

    public boolean isExtraccion() {
        return !esDeposito;
    }

    //7. No es responsabilidad de Movimiento modificar el saldo y agregarse a la coleccion
    public void agregateA(Cuenta cuenta) {
        cuenta.setSaldo(calcularValor(cuenta));
        cuenta.agregarMovimiento(fecha, monto, esDeposito);
    }

    //8. No se entiende para que sirve (podria darle un mejor nombre)
    //Ademas, no parece que le pertenezca a Movimiento

    //De todas formas esta atado al metodo anterior
    public double calcularValor(Cuenta cuenta) {
        if (esDeposito) {
            return cuenta.getSaldo() + getMonto();
        } else {
            return cuenta.getSaldo() - getMonto();
        }
    }
}
