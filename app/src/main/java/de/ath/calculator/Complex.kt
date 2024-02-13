package de.ath.calculator

import java.util.Locale
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Complex (realTeil:Double, imagTeil:Double, isPolar:Boolean, precision:Boolean=false){

    private var rT = realTeil
    private var iT = imagTeil
    private var iP = isPolar
    private var prec = precision

    private fun divNenner(num1: Double, num2: Double): Double {
        return num1*num1 + num2*num2
    }
    private fun arg(): Double {
        if (this.iT != 0.0 || this.rT > 0.0) {
            return 2 * atan2(this.iT, sqrt(this.rT*this.rT + this.iT*this.iT) + this.rT)
        }
        else if (this.iT == 0.0 && this.rT < 0.0) {
            return PI
        }
        else
            return 0.0
    }
    fun real(): Double {
        return(this.rT)
    }
    fun imag(): Double {
        return(this.iT)
    }
    fun YX(betrag: Double, phase: Double): Complex {
        val rx: Double = ln(betrag) * this.rT - phase * this.iT
        val ry: Double = phase * this.rT + ln(betrag) * this.iT
        var realT: Double = E.pow(rx) *  cos(ry)
        if (abs(cos(ry)) < 1E-15) // cos(90) ist nicht Null !!
            realT = 0.0
        var imagT: Double = E.pow(rx) * sin(ry)
        if (abs(sin(ry)) < 1E-15) // sin(180) ist nicht Null !!
            imagT = 0.0
//        Log.d("conv", realT.toString())
//        Log.d("conv", imagT.toString())
        return Complex(realT, imagT, false)
    }
    fun Ln(betrag: Double): Complex {
        return Complex(ln(betrag), this.arg(), false)
    }
    operator fun plus(c:Complex): Complex {
        return Complex( this.rT + c.rT, this.iT + c.iT, false)
    }
    operator fun minus(c:Complex): Complex {
        return Complex( this.rT - c.rT, this.iT - c.iT, false)
    }
    operator fun times(c:Complex): Complex {
        return Complex( this.rT * c.rT - this.iT * c.iT, this.rT * c.iT + this.iT * c.rT, false)
    }
    operator fun div(c:Complex): Complex {
        return Complex( (this.rT * c.rT + this.iT * c.iT)/divNenner(c.rT, c.iT ), (this.iT * c.rT - this.rT * c.iT)/divNenner(c.rT, c.iT ) , false)
    }
    override fun toString(): String {
        Locale.setDefault(Locale.US) // "2.0000 und nicht 2,0000"
        var formatE: String = if(abs(this.rT)  < 1e-6 || abs(this.iT) < 1e-6 || abs(this.rT)  > 1e3 || abs(this.iT) > 1e3 ) "%.2e" else "%.4e"
        var formatF: String = "%.4f"
        if (this.iT == 0.0)
            formatE = if(abs(this.rT)  < 1e-9 || abs(this.iT) < 1e-9 || abs(this.rT)  > 1e9 || abs(this.iT) > 1e9 ) "%.4e" else "%.4e"
        if (prec) {
            formatF = "%.15f"
            formatE = "%.12e"
        }
        var strFormatReal: String = if ((abs(this.rT) > 1E8 || abs(this.rT) < 1E-4) && this.rT != 0.0) formatE else formatF
        var strFormatImag: String = if ((abs(this.iT) > 1E8 || abs(this.iT) < 1E-4) && this.iT != 0.0) formatE else formatF

        if (this.iT != 0.0) {
            strFormatReal = if ((abs(this.rT) > 1E3 || abs(this.rT) < 1E-4) && this.rT != 0.0) formatE else formatF
            strFormatImag = if ((abs(this.iT) > 1E3 || abs(this.iT) < 1E-4) && this.iT != 0.0) formatE else formatF

        }


        if (this.iT == 0.0) {
            strFormatReal = if ((abs(this.rT) > 1E11 || abs(this.rT) < 1E-4) && this.rT != 0.0) formatE else formatF
            return String.format(strFormatReal, this.rT)
        }
        else {
            if (this.iP == false) {
                return String.format(strFormatReal +" i"+strFormatImag,this.rT, this.iT)
            }
            else {
                return String.format("$strFormatReal \u2220$strFormatImag",this.rT, this.iT)
            }
        }
    }

}

