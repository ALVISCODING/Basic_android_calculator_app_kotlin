package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {
    private var resultOutput: TextView? = null
    var lastInputIsDigit : Boolean = false;
    var lastInputIsDot : Boolean = false;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultOutput= findViewById(R.id.resultOutput)
    }

    /**
     * it extract the text from the button clicked
     */
    fun onDigit(view: View){
        resultOutput?.append((view as Button).text)
        lastInputIsDigit = true
        lastInputIsDot = false



    }

    /**
     * we set the onclick in button to onClear
     */
    fun onClear(view: View){
        resultOutput?.text=""
        lastInputIsDigit = false;
        lastInputIsDot = false;

    }

    fun onDecimalPoint(view: View){
        if(lastInputIsDigit && !lastInputIsDot){
            resultOutput?.append(".")
            lastInputIsDot = true
            lastInputIsDigit = false
        }
    }

    /**
     * for opeation only
     * first check if the view is empty
     * and if the previous is a operator
     */
    fun onOperator(view: View){
        resultOutput?.text.let {

            //check if the last inout is digit and if is contains operator
            if (lastInputIsDigit && !isOperatorAdded(it.toString())){
                //display and add the operator char to the resultOutput
                resultOutput?.append((view as Button).text)

                //set the lastInputIsDigit is false as we added a operator on the text
                lastInputIsDigit = false;
                lastInputIsDot = false;
            }
        }
    }


    /**
     * To calculate the result based on the operator
     */
    fun onEqual(view: View) {
        if (lastInputIsDigit) {

            var result = resultOutput?.text.toString()
            var prefix = ""

            try {
                if (result.startsWith("-")){
                    prefix = "-"
                    //the negative sign of the number is remove and save to the var prefix
                    result = result.substring(1)

                }
                //spilt the 2 value
                val splitValue = result.split("-", "+", "*", "/")
                if (splitValue.size == 2) {
                    val first = splitValue[0].toDouble()
                    val second = splitValue[1].toDouble()


                    // is the same of the java switch statement
                    //the temp resultis save on teh operation variable
                    val operation = when {
                        result.contains("+") -> first + second
                        result.contains("-") -> first - second
                        result.contains("*") -> first * second
                        result.contains("/") -> {
                            //if 0 / any  thrown exception
                            if (second != 0.0) first / second else Double.NaN
                        }
                        else -> throw ArithmeticException("Invalid operation")
                    }
                    //set the operation to BigDecimal to limit to 6 decimal only
                    val resultDecimal = BigDecimal(operation).setScale(6, BigDecimal.ROUND_HALF_UP)

                    //check if there is a prefix and put it back accordingly
                    val resultText = if (prefix.isNotEmpty()) {
                        "$prefix${resultDecimal.stripTrailingZeros().toPlainString()}"
                    } else {
                        resultDecimal.stripTrailingZeros().toPlainString()
                    }

                    //set the text of the result
                    resultOutput?.text = resultText
                    return

                }
                }catch (e: java.lang.ArithmeticException){
                e.printStackTrace()
            }
        }
    }



    /**
     * to check the prefix of a string
     * return false if starting with - else true
     */
    private fun isOperatorAdded(value : String):Boolean{
        return if (value.startsWith("-")){
            false
        } else {
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }

}