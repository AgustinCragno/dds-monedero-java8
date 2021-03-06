package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

    private static final double LIMITE_DE_EXTRACCION_DIARIO = 1000;

    private double saldo;
    private List<Movimiento> movimientos = new ArrayList<>();

    public Cuenta() {
        saldo = 0;
    }

    public Cuenta(double montoInicial) {
        saldo = montoInicial;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void poner(double cuanto) {
        validarMontoNegativo(cuanto);

        if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
        }

        agregarMovimiento(LocalDate.now(), cuanto, true);
    }

    public void sacar(double cuanto) {
        validarMontoNegativo(cuanto);

        if (getSaldo() - cuanto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
        }

        double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
        double limite = LIMITE_DE_EXTRACCION_DIARIO - montoExtraidoHoy;

        if (cuanto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
                    + " diarios, límite: " + limite);
        }

        agregarMovimiento(LocalDate.now(), cuanto, false);
    }

    public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
        saldo += cuanto * (esDeposito ? 1 : -1);

        Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
        movimientos.add(movimiento);
    }

    public double getMontoExtraidoA(LocalDate fecha) {
        return getMovimientos().stream()
                .filter(movimiento -> movimiento.fueExtraido(fecha))
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    private void validarMontoNegativo(double monto){
        if (monto <= 0) throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

}
