package dds.monedero.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.time.Month;

public class MonederoTest {
    private Cuenta cuenta;

    @Before
    public void init() {
        cuenta = new Cuenta();
    }

    @Test
    public void Poner() {
        cuenta.poner(1500);
        Assert.assertEquals(1500, cuenta.getSaldo(), 0);
    }

    @Test(expected = MontoNegativoException.class)
    public void PonerMontoNegativo() {
        cuenta.poner(-1500);
    }

    @Test
    public void TresDepositos() {
        cuenta.poner(1500);
        cuenta.poner(456);
        cuenta.poner(1900);

        Assert.assertEquals(1500 + 456 + 1900, cuenta.getSaldo(), 0);
    }

    @Test(expected = MaximaCantidadDepositosException.class)
    public void MasDeTresDepositos() {
        cuenta.poner(1500);
        cuenta.poner(456);
        cuenta.poner(1900);
        cuenta.poner(245);
    }

    @Test
    public void Sacar(){
        cuenta.setSaldo(1500);
        cuenta.sacar(1000);

        Assert.assertEquals(500, cuenta.getSaldo(), 0);
    }

    @Test(expected = SaldoMenorException.class)
    public void ExtraerMasQueElSaldo() {
        cuenta.setSaldo(90);
        cuenta.sacar(1001);
    }

    @Test(expected = MaximoExtraccionDiarioException.class)
    public void ExtraerMasDe1000() {
        cuenta.setSaldo(5000);
        cuenta.sacar(1001);
    }

    @Test(expected = MontoNegativoException.class)
    public void ExtraerMontoNegativo() {
        cuenta.sacar(-500);
    }
    
    @Test
    public void NuevoMovimiento(){
        cuenta.agregarMovimiento(LocalDate.now(), 1000, true);

        Assert.assertFalse(cuenta.getMovimientos().isEmpty());
    }

    @Test
    public void MontoExtraidoUnaDeterminadaFecha(){
        LocalDate hoy = LocalDate.now();

        cuenta.agregarMovimiento(hoy, 1000, false);
        cuenta.agregarMovimiento(hoy, 500, false);
        cuenta.agregarMovimiento(LocalDate.of(2015, Month.APRIL, 10), 2300, false);

        Assert.assertEquals(1500, cuenta.getMontoExtraidoA(hoy), 0);
    }
}