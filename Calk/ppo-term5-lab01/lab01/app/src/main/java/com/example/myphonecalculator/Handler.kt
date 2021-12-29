package com.example.myphonecalculator

import android.graphics.Color
import android.view.View
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.operator.Operator
import kotlin.math.sqrt



var factorial: Operator = object : Operator("!", 1, true, PRECEDENCE_POWER + 1) {
    override fun apply(vararg args: Double): Double {
        val arg = args[0].toInt()
        require(args[0] - arg.toDouble() == 0.0) { "Not integer" }
        require(arg >= 0) { "Error" }
        var result = 1.0
        for (i in 1..arg) {
            result *= i.toDouble()
        }
        return result
    }
}

var root: Operator = object : Operator("#", 1, false, PRECEDENCE_POWER + 1) {
    override fun apply(vararg args: Double): Double {
        val arg = args[0].toDouble()
        require(arg >= 0) { "Error" }
        return sqrt(arg)
    }
}

var degree: Operator = object : Operator("~", 1, true, PRECEDENCE_POWER + 1) {
    override fun apply(vararg args: Double): Double {
        val arg = args[0].toDouble()
        return arg*Math.PI/180
    }
}

object Handler {
    private var currentAnswer = ""
    var angleConf = "rad"
    var trigonometryConf = ""
    var orientation = "port"
    var textForInputLine = ""

    fun handler(v: View?, str:String, inputLine:TextView, resultLine: TextView){
        minTextSize(v,inputLine, resultLine)
        if(inputLine.text.last() == '°') this.angleConf = "deg"
        if((inputLine.text.last() == '°'|| inputLine.text.endsWith("sin(")
            || inputLine.text.endsWith("cos(")
            || inputLine.text.endsWith("tan("))
            && angleConf == "deg"
        ){
            if(str.isDigitsOnly() || str == ".") {
                if(inputLine.text.last() == '('){
                    inputLine.append(str + "°")
                }
                else{
                    inputLine.text = inputLine.text.dropLast(1)
                    inputLine.append(str + "°")
                }
                updateResultLine(v,inputLine, resultLine)
                return
            }
            else  {
                if(inputLine.text.last() == '(') inputLine.append("0")
                inputLine.append(")")
                this.angleConf = v!!.findViewById<TextView>(R.id.btn_dr).text.toString()
            }
        }
        if(this.currentAnswer != "") {
            if(str.last().isDigit()) inputLine.text = "0"
            else inputLine.text = this.currentAnswer
            this.currentAnswer = ""
        }
        if(inputLine.text.last() == '.' && !str.isDigitsOnly()){
            inputLine.append("0")
        }
        if(str.isDigitsOnly()) {
            if(getFirstDigit(inputLine.text.toString()) == "0") inputLine.text = inputLine.text.dropLast(1)
            if(!inputLine.text.isEmpty() && inputLine.text.last() in "eπ!") inputLine.append("×")
            inputLine.append(str)
        }
        else if (str == "."){
            if(inputLine.text.last().isDigit() && getFirstDigit(inputLine.text.toString()) != ".")
                inputLine.append(str)
        }
        else if (str == "e" || str == "π"){
            if(getFirstDigit(inputLine.text.toString()) == "0"){
                inputLine.text = inputLine.text.dropLast(1)
                inputLine.append(str)
            }
            else if(inputLine.text.last() in arrayOf(')','π','e') || inputLine.text.last().isDigit())
                inputLine.append("×" + str)
            else inputLine.append(str)
        }
        else if (str in arrayOf("+","-","×","÷","^","^(-1)","!","%")){
            if(inputLine.text.last() == '0'&& inputLine.text.length == 1 && str == "-")
                inputLine.text = "-"
            else if (inputLine.text.last() in arrayOf('+','-','×','÷','^')){
                inputLine.text = inputLine.text.dropLast(1)
                inputLine.append(str)
            }
            else if (inputLine.text.last() == '%'){
                if(str == "-") inputLine.append("(" + str)
                else {
                    inputLine.text = inputLine.text.dropLast(1)
                    inputLine.append(str)
                }
            }
            else if (inputLine.text.last() == '√'){
                if(str == "-") inputLine.append("(" + str)
                else {
                    inputLine.text = inputLine.text.dropLast(1)
                    inputLine.append("0" + str)
                }
            }
            else if (inputLine.text.last() == '(' && str != "-") {}
            else inputLine.append(str)
        }
        else if (str == "√"){
            if(getFirstDigit(inputLine.text.toString()) == "0"){
                inputLine.text = inputLine.text.dropLast(1)
            }
            else if (inputLine.text.last() in arrayOf('π','e','!',')') || inputLine.text.last().isDigit())
                inputLine.append("×")
            inputLine.append(str)
        }
        else if (str in arrayOf("(",")")){
            if(str == "("){
                if(getFirstDigit(inputLine.text.toString()) == "0"){
                    inputLine.text = inputLine.text.dropLast(1)
                }
                else if (inputLine.text.last() in arrayOf('π','e','!',')') || inputLine.text.last().isDigit())
                    inputLine.append("×")
                inputLine.append(str)
            }
            else{
                if(inputLine.text.last() in "+-×÷^√%"){

                }
                else if(getBracketsBalans(inputLine.text.toString())> 0){
                    if(inputLine.text.last() == '(')
                        inputLine.append("0)")
                    else
                        inputLine.append(")")
                }
            }
        }
        else if (str in arrayOf("sin", "cos", "tan")){
            if(getFirstDigit(inputLine.text.toString()) == "0"){
                inputLine.text = inputLine.text.dropLast(1)
            }
            else if (inputLine.text.last() in arrayOf('π','e','!',')') || inputLine.text.last().isDigit())
                inputLine.append("×")
            inputLine.append(this.trigonometryConf + str + "(")
        }
        else if (str in arrayOf("lg", "ln")){
            if(getFirstDigit(inputLine.text.toString()) == "0"){
                inputLine.text = inputLine.text.dropLast(1)
            }
            else if (inputLine.text.last() in arrayOf('π','e','!',')') || inputLine.text.last().isDigit())
                inputLine.append("×")
            inputLine.append(str + "(")
        }
        updateResultLine(v,inputLine, resultLine)
    }

    private fun getTempNum(sym:String):String{
        if(sym in arrayOf("+", "-")) return "0"
        else if(sym in arrayOf("×","÷")) return "1"
        return "0"
    }

    private fun updateResultLine(v: View?, inputLine:TextView, resultLine: TextView){
        var tempLine:String = inputLine.text.toString()
        if (tempLine.last() in arrayOf('+', '-', '.')) tempLine += "0"
        if (tempLine.last() in arrayOf('×','÷','^','√')) tempLine += "1"
        if (tempLine.last() == '%') tempLine += "1"
        if (tempLine.last() == '('){
            if(tempLine.length > 1 && tempLine.reversed()[1] in arrayOf('×','÷','+','-')){
                tempLine += getTempNum(tempLine.reversed()[1].toString())
            }
            else {
                tempLine += 0
            }
        }
        if (getBracketsBalans(tempLine) != 0){
            tempLine += ")".repeat(getBracketsBalans(tempLine))
        }
        tempLine = tempLine.replace('×', '*')
        tempLine = tempLine.replace('÷', '/')
        tempLine = tempLine.replace("lg", "log10")
        tempLine = tempLine.replace("ln", "log")
        tempLine = tempLine.replace("√", "#")
        tempLine = tempLine.replace("°", "~")
        tempLine = tempLine.replace(".~", ".0~")
        try {
            val expression = ExpressionBuilder(tempLine).operator(factorial,root,degree).build()
            val numResult = expression.evaluate()

            //Leave only 6 digits after dot
            //var finalAnswer = String.format("%.9f", numResult).toDouble()
            var finalAnswer = Math.round(numResult * 1000000000.0) / 1000000000.0

            var result = "= " + finalAnswer.toString()

            for (sym in result.reversed()) {
                if (sym == '0') {
                    result = result.dropLast(1)
                    continue
                }
                if (sym == '.') {
                    result = result.dropLast(1)
                    break
                }
                break
            }
            if(result == "= -0") result = "= 0"
            resultLine.text = result

        }
        catch (e: Exception){
            resultLine.text = e.message
        }
    }

    fun clean(v: View?, inputLine:TextView, resultLine: TextView){
        minTextSize(v,inputLine, resultLine)
        inputLine.text = "0"
        resultLine.text = ""
        this.currentAnswer = ""
    }

    fun stepBack(v: View?, inputLine:TextView, resultLine: TextView){
        if(this.currentAnswer != "") return
        for (literal in arrayOf("asin(", "acos(", "atan(","sin(", "cos(", "tan(","lg(", "ln(")) {
            if (inputLine.text.endsWith(literal)) {
                inputLine.text = inputLine.text.dropLast(literal.length)
                if(inputLine.text.isEmpty()) {
                    inputLine.text = "0"
                    resultLine.text = ""
                    return
                }
                updateResultLine(v,inputLine, resultLine)
                return
            }
        }
        if(inputLine.text.last() == '°'){
            if(inputLine.text.reversed()[2] == '(')
                inputLine.text = inputLine.text.dropLast(2)
            else{
                inputLine.text = inputLine.text.dropLast(2)
                inputLine.append("°")
            }
            updateResultLine(v,inputLine, resultLine)
            return
        }
        inputLine.text = inputLine.text.dropLast(1)
        if(inputLine.text.isEmpty()) {
            inputLine.text = "0"
            resultLine.text = ""
            return
        }
        updateResultLine(v,inputLine, resultLine)
    }

    //Reduces inputLine text size and increases resultLine
    private fun maxTextSize(v: View?, inputLine:TextView, resultLine: TextView){
        if(resultLine.text.isEmpty()) return
        if(orientation == "port"){
            inputLine.textSize = 40.toFloat()
            resultLine.textSize = 70.toFloat() - getTextSizeBig(resultLine)
        }
        else{
            inputLine.textSize = 30.toFloat()
            resultLine.textSize = 40.toFloat()
        }
        inputLine.setTextColor(Color.parseColor("#AAAAAA"))
        resultLine.setTextColor(Color.parseColor("#000000"))
    }

    //Increases inputLine text size and reduces resultLine
    private fun minTextSize(v: View?, inputLine:TextView, resultLine: TextView){
        if(orientation == "port"){
            inputLine.textSize = 70.toFloat()
            resultLine.textSize = 50.toFloat() - getTextSize(resultLine)
        }
        else{
            inputLine.textSize = 40.toFloat()
            resultLine.textSize = 30.toFloat()
        }
        inputLine.setTextColor(Color.parseColor("#000000"))
        resultLine.setTextColor(Color.parseColor("#AAAAAA"))
    }

    fun printResult(v: View? ,inputLine:TextView, resultLine: TextView){
        if(inputLine.text.length == 0 && inputLine.text.last() == '0') return
        if(resultLine.text.drop(2).toString() != "Infinity"
            && resultLine.text.drop(2).toString() != "-Infinity"
            && resultLine.text.drop(2).toString() != "NaN"
            && resultLine.text.drop(2).toString() != "-NaN"
            && "token" !in resultLine.text
            && "Error" !in resultLine.text
            && "operator" !in resultLine.text
                )
            this.currentAnswer = resultLine.text.drop(2).toString()
        else currentAnswer = "0"
        maxTextSize(v,inputLine, resultLine)
    }

    private fun getFirstDigit(str:String):String{
        var last = '+'
        for(dig in str.reversed()){
            if(!dig.isDigit()){
                if(dig == '.') last = dig
                break
            }
            last = dig
        }
        return last.toString()
    }

    private fun getBracketsBalans(str:String):Int{
        var ans = 0
        for(s in str){
            if(s == '(') ans++
            if(s == ')') ans--
        }
        return ans
    }

    fun isRadAvailable():Boolean{
        if (trigonometryConf == "") return true
        return false
    }

    fun isArcAvailable():Boolean{
        if (angleConf == "rad") return true
        return false
    }

    private fun getTextSize(resultLine: TextView):Float{
        val step:Float = 1.5.toFloat()
        if(resultLine.text.length < 9) return 0.0.toFloat()
        return  step * (resultLine.text.length - 9)
    }

    private fun getTextSizeBig(resultLine: TextView):Float{
        val step:Float = 3.4.toFloat()
        if(resultLine.text.length < 9) return 0.0.toFloat()
        for (i in 9..19){
            if(resultLine.text.length == i) return step*(i-9)
        }
        return step*12
    }
}
