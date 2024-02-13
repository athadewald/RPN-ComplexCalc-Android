package de.ath.calculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.E
import kotlin.math.abs
import kotlin.math.floor


class MainActivity : ComponentActivity() {

    private var numOk = true
    private var overrideFlag = false
    private var numError = false
    private var preciseFlag = true

    private var regX:Double = 0.0
    private var numX = Complex(0.0,0.0, false)
    private var numY = Complex(0.0,0.0, false)
    private var numU = Complex(0.0,0.0, false)
    private var numT = Complex(0.0,0.0, false)
    private var bufferX = Complex(0.0,0.0, false)
    private var bufferY = Complex(0.0,0.0, false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView1 = findViewById<TextView>(R.id.textView1)
        val textView2 = findViewById<TextView>(R.id.textView2)

        val button0 = findViewById<Button>(R.id.button0)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val buttonDot = findViewById<Button>(R.id.buttonDot)
		val buttonE = findViewById<Button>(R.id.buttonE)
        val buttonClear = findViewById<Button>(R.id.buttonClear)

        val buttonEnter = findViewById<Button>(R.id.buttonEnter)
        val buttonPrec = findViewById<Button>(R.id.buttonPrec)
		val buttonPlusMinus = findViewById<Button>(R.id.buttonPlusMinus)
		val buttonChangeXY = findViewById<Button>(R.id.buttonChangeXY)
		val buttonCplx = findViewById<Button>(R.id.buttonCplx)
		val buttonSquareRoot = findViewById<Button>(R.id.buttonSquareRoot)
		val buttonYX = findViewById<Button>(R.id.buttonYX)
		val buttonInvert = findViewById<Button>(R.id.buttonInvert)
		val buttonDivide = findViewById<Button>(R.id.buttonDivide)
		val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
		val buttonSubtract = findViewById<Button>(R.id.buttonSubtract)
		val buttonAdd = findViewById<Button>(R.id.buttonAdd)
		val buttonPi = findViewById<Button>(R.id.buttonPi)
		val buttonLn = findViewById<Button>(R.id.buttonLn)
		val buttonExp = findViewById<Button>(R.id.buttonExp)
        val radiobuttonDeg  = findViewById<RadioButton>(R.id.radiobuttonDeg)
        val radiobuttonRad  = findViewById<RadioButton>(R.id.radiobuttonRad)
        val radiobuttonRect  = findViewById<RadioButton>(R.id.radiobuttonRect)
        val radiobuttonPolar  = findViewById<RadioButton>(R.id.radiobuttonPol)

        fun betrag(num: Complex) : Double {
            return sqrt(num.real() * num.real() + num.imag() * num.imag())
        }
        fun phase(num: Complex) : Double {
            return atan2(num.imag(), num.real())
        }

        fun toPol(num: Complex, conFac: Double): Complex {
            if (num.imag() != 0.0) {
                Log.d("conv", phase(num).toString())
                return Complex(betrag(num), phase(num) * conFac, true)
            }
            else
                return num

        }
        fun toRect(num: Complex, conFac: Double) : Complex  {
//            Log.d("conv", cos(num.imag() * conFac).toString())
            if (num.imag() != 0.0) {
                var realT: Double = num.real() *  cos(num.imag() * conFac)
                if (abs(cos(num.imag() * conFac)) < 1E-15)
                    realT = 0.0
                var imagT: Double = num.real() * sin(num.imag() * conFac)
                if (abs(sin(num.imag() * conFac)) < 1E-15)
                    imagT = 0.0
                return Complex(realT, imagT, isPolar = false)
            }
            else
                return num
        }
        fun angleConv(num: Complex, conFac: Double) : Complex  {
//            Log.d("conv", numX.real().toString())
            if (num.imag() != 0.0) {
                if(!radiobuttonRect.isChecked)
                    return Complex(num.real(), num.imag()* conFac, isPolar = true)
                else
                    return Complex(num.real(), num.imag()* conFac, isPolar = false)
            }
            else
                return num
        }
        fun showDisplay() {
            preciseFlag = true // if not prec is pressed the flag must be true
            textView1.text = numX.toString()
            if (!numError)
                textView2.text = numY.toString()
            else
                textView2.text = "Num Error"
        }
        fun plusMinus() = if (numOk == true) {
            numError = false
            if(!radiobuttonRect.isChecked) {
                numX = toRect(numX, PI / 180.0)
                numX = when(numX.real()) {
                    0.0 -> Complex(numX.real(), numX.imag() * -1.0, false)
                    else -> Complex(-1.0, 0.0, false) * numX
                }
                numX = toPol(numX,180.0 / PI)
            }
            else {
                numX = when(numX.real()) {
                    0.0 -> Complex(numX.real(), numX.imag() * -1.0, false)
                    else -> Complex(-1.0, 0.0, false) * numX
                }
            }
            showDisplay()
        } // if end
        else {
            var dispVal = textView1.text.toString()
//            Log.d("conv", dispVal)
            if (dispVal.contains("E")) {
                var exponent = dispVal.split("E").toMutableList()
                if ("${exponent[1]}" != "") {
                    exponent[1] = when {
                        exponent[1].toDouble() > 0 -> "-" + exponent[1]
                        else -> exponent[1].replace("-", "")
                    }
                    dispVal = exponent[0] + "E" + exponent[1]
                }
            }
            else {
                if (dispVal.toDouble() > 0)
                    dispVal = "-"+dispVal
                else {
                    dispVal = dispVal.replace("-", "")
//                    Log.d("conv", dispVal)
                }
            }

            textView1.text = dispVal
        }
        fun shiftStack() {
            numT = numU
            numU = numY
            numY = numX
        }
        fun dropStack() {
            numY = numU
            numU = numT
        }

        fun checkDisplayInput(displayValue: String){
            numError = false
            if (numOk == false) {
                val expStr = """\d+.?\d*E$""".toRegex()
                if (displayValue.matches(expStr)) {
                    var displayValueBuffer = displayValue + "0"
                    regX = displayValueBuffer.toDouble()
                }
                else
                    regX = displayValue.toDouble()
                var bufferX: Complex = numX
                numX = Complex(regX, 0.0, false)
                if (numX.real().isNaN() || numX.real().isInfinite()) {
                    numError = true
                    numX = bufferX
                    overrideFlag = false
                }
            }
            numOk = true

        }
        fun displayPrec() {
            checkDisplayInput(textView1.text.toString())

            if (preciseFlag) {
                bufferX = numX
                bufferY = numY
                if (numX.imag() == 0.0) {
                    numX = Complex(numX.real(), 0.0, false, precision = true)
                    if (numY.imag() == 0.0)
                        numY = Complex(numY.real(), 0.0, false, precision = true)
                } else {
                    numY = Complex(numX.real(), 0.0, false, precision = true)
                    numX = Complex(bufferX.imag(), 0.0, false, precision = true)
                }
                showDisplay()
                numX = bufferX
                numY = bufferY
                preciseFlag = false
            }
            else {
                showDisplay()
                preciseFlag = true
            }

        }
        fun calcDeg() {
            numError = false
            if(!radiobuttonRect.isChecked) {
                checkDisplayInput(textView1.text.toString())
                numX = angleConv(numX, 180.0 / PI)
                numY = angleConv(numY, 180.0 / PI)
                numU = angleConv(numU, 180.0 / PI)
                numT = angleConv(numT, 180.0 / PI)
                showDisplay()
            }
        }
        fun calcRad() {
            numError = false
            if(!radiobuttonRect.isChecked) {
                checkDisplayInput(textView1.text.toString())
                numX = angleConv(numX, PI / 180.0)
                numY = angleConv(numY, PI / 180.0)
                numU = angleConv(numU, PI / 180.0)
                numT = angleConv(numT, PI / 180.0)
                showDisplay()
            }
        }
        fun calcRect() {
            numError = false
            checkDisplayInput(textView1.text.toString())
            var conFactor: Double = if(radiobuttonDeg.isChecked) PI / 180.0 else 1.0
            numX = toRect(numX, conFactor)
            numY = toRect(numY, conFactor)
            numU = toRect(numU, conFactor)
            numT = toRect(numT, conFactor)
            showDisplay()
        }

        fun calcPolar() {
            numError = false
            checkDisplayInput(textView1.text.toString())
            val conFactor: Double = if(radiobuttonDeg.isChecked) 180.0 / PI else 1.0
            numX = toPol(numX,conFactor)
            numY = toPol(numY,conFactor)
            numU = toPol(numU,conFactor)
            numT = toPol(numT,conFactor)
            showDisplay()
        }

        fun appendVal(value: String) {
            numError = false
            if (numOk) {
                if (overrideFlag == false)
                    shiftStack()
                showDisplay()
                numOk = false
                overrideFlag = false
                textView1.text = ""
            }
            if (textView1.text.toString() == "")
                if (value == "E")
                    textView1.text = "1E"
                else if (value == ".")
                    textView1.text = "0."
            if (textView1.text.toString().contains(".")  && value == ".")
                textView1.text = textView1.text.toString()
            else if (textView1.text.toString().contains("E") && (value == "E" || value == "."))
                textView1.text = textView1.text.toString()
            else
                textView1.append(value)
        }

         fun enterNum() {
            checkDisplayInput(textView1.text.toString())
            shiftStack()
            showDisplay()
            overrideFlag = true
        }
        fun pressComplex() {
            checkDisplayInput(textView1.text.toString())
            if (numX.imag() == 0.0 && numY.imag() == 0.0) {
                if (radiobuttonRect.isChecked)
                    numX = Complex(numY.real(), numX.real(), false)
                else {
                    if (radiobuttonDeg.isChecked) {
                        if (abs(numX.real()) >= 360.0) {
                            val diffRest: Double = numX.real() / 360.0 - floor(numX.real() / 360)
                            Log.d("conv", diffRest.toString())
                            numX = Complex(diffRest * 360, 0.0, true)
                        }
                        if (abs(numX.real()) > 180.0 && abs(numX.real()) < 360.0) {
                            if (numX.real() > 0) {
                                numX = numX - Complex(360.0, 0.0, true)
                            }
                            else
                                numX = numX + Complex(360.0, 0.0, true)
                        }
                    }
                    else {
                        if (abs(numX.real()) >= 2 * PI) {
                            val diffRest: Double =
                                numX.real() / (2 * PI) - floor(numX.real() / (2 * PI))
                            numX = Complex(diffRest * 2 * PI, 0.0, true)
                        }
                        if (abs(numX.real()) > PI && abs(numX.real()) < 2 * PI) {
                            if (numX.real() > 0) {
                                numX = numX - Complex(2 * PI, 0.0, true)
                            } else
                                numX = numX + Complex(2 * PI, 0.0, true)
                        }
                    }
                    numX = Complex(numY.real(), numX.real(), true)
                }
                dropStack()
            } else if (numX.imag() != 0.0) {
                var bufferX: Complex = numX
                numX = Complex(numX.real(), 0.0, false)
                shiftStack()
                numX = Complex(bufferX.imag(), 0.0, false)
                }
                showDisplay()
                overrideFlag = false
        }

        fun calculate(op: String) {
            checkDisplayInput(textView1.text.toString())
            var bufferNumX: Complex = numX
            if(!radiobuttonRect.isChecked) {
                numX = toRect(numX, PI / 180.0)
                numY = toRect(numY, PI / 180.0)
                numU = toRect(numU, PI / 180.0)
                numT = toRect(numT, PI / 180.0)
            }
//            Log.d("conv", numX.toString())
//            Log.d("conv", numY.toString())
             if (op == "+") {
                 numX = numY + numX
             }
            else if (op == "-") {
                numX = numY - numX
            }
             else if (op == "*") {
                numX = numY * numX
            }
             else if (op == "/") {
                 numX = numY / numX
            }
             else if (op == "y^x") {
                 if (betrag(numY) == 0.0) { // because of ln(betrag) in fun YX
                     if (betrag(numX) == 0.0) // zero to the power of zero = 1
                        numX =  Complex(1.0,0.0, false)
                     else
                         numX =  Complex(0.0,0.0, false)
                 }
                 else
                    numX = numX.YX(betrag(numY), phase(numY))
             }
             else if (op == "sqrt") {
                 var hochzahl: Complex = Complex(0.5,0.0, false)
                 if (numX.real() != 0.0) // because of ln(betrag) in fun YX
                    numX = hochzahl.YX(betrag(numX), phase(numX))
             }
             else if (op == "e^x") {
                 var basis: Complex = Complex(E,0.0, false)
                 numX = numX.YX(betrag(basis), phase(basis))
             }
             else if (op == "ln") {
                 numX = numX.Ln(betrag(numX))
             }
             else if (op == "1/x") {
                 if(radiobuttonRect.isChecked)
                     numX = Complex(1.0,0.0, false) / numX
                 else
                     numX = Complex(1.0,0.0, true) / numX
             }
             else if (op == "x<>y") {
                 if (!numX.real().isNaN() && !numX.real().isInfinite()) {
                     var bufferX: Complex = numX
                     numX = numY
                     numY = bufferX
                 }
            }
             else if (op == "pi") {
                 shiftStack()
                 numX = Complex(PI, 0.0, false)
             }
            else if (op == "delX") {
                numX = Complex(0.0, 0.0, false)
            }
            if(!radiobuttonRect.isChecked) {
                numX = toPol(numX,180.0 / PI)
                numY = toPol(numY,180.0 / PI)
                numU = toPol(numU,180.0 / PI)
                numT = toPol(numT,180.0 / PI)
            }
            if (op in arrayOf("+", "-","*","/","y^x")) {
                if (numX.real().isNaN() || numX.real().isInfinite()) { // error-handling mit try catch geht nicht!
                    overrideFlag = false
                    numError = true
                    numX = bufferNumX
                }
                else {
                    dropStack()
                    overrideFlag = false
                }
            }
            else if (op in arrayOf("sqrt", "e^x", "ln", "1/x", "x<>y", "pi")) {
                if (numX.real().isNaN() || numX.real().isInfinite()) { // error-handling mit try catch geht nicht!
                    overrideFlag = false
                    numError = true
                    numX = bufferNumX
                }
                else {
                    overrideFlag = false
                }
            }
            else if (op == "delX")
                overrideFlag = true

            showDisplay()
            numOk = true

        }

        //numbers
        button0.setOnClickListener { appendVal("0" ) }
        button1.setOnClickListener { appendVal("1" ) }
        button2.setOnClickListener { appendVal("2" ) }
        button3.setOnClickListener { appendVal("3" ) }
        button4.setOnClickListener { appendVal("4" ) }
        button5.setOnClickListener { appendVal("5" ) }
        button6.setOnClickListener { appendVal("6" ) }
        button7.setOnClickListener { appendVal("7" ) }
        button8.setOnClickListener { appendVal("8" ) }
        button9.setOnClickListener { appendVal("9" ) }
        buttonDot.setOnClickListener { appendVal("." ) }
		buttonE.setOnClickListener { appendVal("E" ) }

        buttonEnter.setOnClickListener { enterNum() }
        buttonCplx.setOnClickListener { pressComplex() }
        buttonPrec.setOnClickListener { displayPrec() }
        buttonClear.setOnClickListener { calculate("delX") }
        buttonChangeXY.setOnClickListener { calculate("x<>y") }
        buttonPlusMinus.setOnClickListener { plusMinus() }
        buttonAdd.setOnClickListener { calculate("+") }
        buttonSubtract.setOnClickListener { calculate("-") }
        buttonMultiply.setOnClickListener { calculate("*") }
        buttonDivide.setOnClickListener { calculate("/") }
        buttonInvert.setOnClickListener { calculate("1/x") }
        buttonYX.setOnClickListener { calculate("y^x") }
        buttonSquareRoot.setOnClickListener { calculate("sqrt") }
        buttonLn.setOnClickListener { calculate("ln") }
        buttonExp.setOnClickListener { calculate("e^x") }
        buttonPi.setOnClickListener { calculate("pi") }
        radiobuttonRect.setOnClickListener { calcRect()}
        radiobuttonPolar.setOnClickListener { calcPolar()}
        radiobuttonDeg.setOnClickListener { calcDeg()}
        radiobuttonRad.setOnClickListener { calcRad()}

		//
    }
}
