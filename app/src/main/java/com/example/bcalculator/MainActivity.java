package com.example.bcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private TextView historyBar;
    private int FlagForTrigParen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        historyBar = findViewById(R.id.HistoryBar);
        result = findViewById(R.id.output);

        result.setShowSoftInputOnFocus(false);//removes the keyboard pop on selecting EditText

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.output).equals(result.getText().toString()))//if string in edittext is enter Expression,
                    result.setText("");//make it empty on clicking it
            }
        });
    }

    private void updateText(String numbertoadd) {
        //if(numbertoadd.equals("")) {
        // result.setText("");
        // return;}
        //above if statement works fine for Clear button
        int SymbolErrorFlag = 1;//to check if the user is clicking wrong first symbol
        String oldStr = result.getText().toString();
        int cursorPosition = result.getSelectionStart();//gets the cursor position
        String leftpartstr = oldStr.substring(0, cursorPosition);
        String rightpartstr = oldStr.substring(cursorPosition);

        //displays the string expression on the TextView
        if (getString(R.string.output).equals(result.getText().toString()) || getString(R.string.exception).equals(result.getText().toString())) {
            if ("sincostan".contains(numbertoadd)) {
                result.setText(String.format("%s%s", numbertoadd, "("));
                FlagForTrigParen = 1;
            }
            //first character of expression should not be the following operators
            else if("×+^/".contains(numbertoadd)){
                if(getString(R.string.output).equals(result.getText().toString()))
                    result.setText(getString(R.string.output));
                else
                    result.setText(getString(R.string.exception));
                SymbolErrorFlag = 0;
            }
            else{
                result.setText(numbertoadd);
            }
        } else {
            if ("sincostan".contains(numbertoadd)) {
                result.setText(String.format("%s%s(%s", leftpartstr, numbertoadd, rightpartstr));
                FlagForTrigParen = 1;
            } else
                result.setText(String.format("%s%s%s", leftpartstr, numbertoadd, rightpartstr));
        }

        //for updating cursor position for different strings received
        if ("sincostan".contains(numbertoadd))// can also use "sincostan".indexOf(numbertoadd)!=-1
            result.setSelection(cursorPosition + 4);//if trigo is selected, cursor should advance by 4 after adding '('
        else if ("00".equals(numbertoadd))
            result.setSelection(cursorPosition + 2);
        else{
            if(SymbolErrorFlag!=0)//when wrong first operator is pressed, the cursor should not update
            result.setSelection(cursorPosition + 1);//keeps the cursor at the end
        }
    }

    public void zerobtn(View view) {
        updateText("0");
    }

    public void onebtn(View view) {
        updateText("1");
    }

    public void twobtn(View view) {
        updateText("2");
    }

    public void threebtn(View view) {
        updateText("3");
    }

    public void fourbtn(View view) {
        updateText("4");
    }

    public void fivebtn(View view) {
        updateText("5");
    }

    public void sixbtn(View view) {
        updateText("6");
    }

    public void sevenbtn(View view) {
        updateText("7");
    }

    public void eightbtn(View view) {
        updateText("8");
    }

    public void ninebtn(View view) {
        updateText("9");
    }

    public void Cbtn(View view) {
        //updateText("");
        //above statement works fine when "" is passed to updateText method
        result.setText("");
    }

    public void backspacebtn(View view) {
        /*the entire function code works only for removing numbers from the end of the expression*/
        int cursorpos = result.getSelectionStart();
        int length = result.getText().length();
        if (cursorpos != 0 && length != 0) {
            String st;
            if (length >= 3 && "sincostan".contains(result.getText().toString().substring(length - 3))) {
                st = result.getText().toString().substring(0, length - 3);
            } else {
                st = result.getText().toString().substring(0, length - 1);
            }
            /*since the cursor keeps shifting to the beginning after the removal of last position
            in the previous code which was "result.setSelection(st.length()); result.setText(st);"
            * you just need to grab the new cursor position and add the st string length to it*/

            result.setText(st);//the new text needs to be set before updating the cursor position
            result.setSelection(result.getSelectionStart() + st.length());
        }
    }

    public void sinbtn(View view) {
        updateText("sin");
    }

    public void cosbtn(View view) {
        updateText("cos");
    }

    public void tanbtn(View view) {
        updateText("tan");
    }

    public void bracketbtn(View view) {
        int cursorpos = result.getSelectionStart(), length = result.getText().length();
        int openbrkt = 0, closebrkt = 0;

        if (FlagForTrigParen == 1)
            openbrkt = 1;

        for (int i = 0; i < cursorpos; i++) {
            if (result.getText().toString().charAt(i) == '(')
                openbrkt++;
            if (result.getText().toString().charAt(i) == ')')
                closebrkt++;
        }

        if (openbrkt == closebrkt || result.getText().toString().charAt(length - 1) == '(')
            updateText("(");
        else if (openbrkt < closebrkt || !(result.getText().toString().charAt(length - 1) == '('))
            updateText(")");

        result.setSelection(cursorpos + 1);
        FlagForTrigParen = 0;
    }

    public void powerbtn(View view) {
        updateText("^");
    }

    public void dividebtn(View view) {
        updateText("/");
    }

    public void multiplybtn(View view) {
        updateText("×");
    }

    public void minusbtn(View view) {
        updateText("-");
    }

    public void plusbtn(View view) {
        updateText("+");
    }

    public void equalsbtn(View view) {
        //if(getString(R.string.output).equals(result.getText().toString())){
        //    result.setText(getString(R.string.output));
        //    result.setSelection(result.getText().toString().length());
        //}

        if(!(getString(R.string.output).equals(result.getText().toString()))){
            try {
                String stri = result.getText().toString();
                stri = stri.replaceAll("×", "*");
                String str = negative(stri);

                Stack<Double> operands = new Stack<>();
                Stack<Character> operator = new Stack<>();

                for (int i = 0; i < str.length(); i++) {

                    System.out.println("start i= " + i + " char= " + str.charAt(i));
                    int count = 0;
                    char chr = str.charAt(i);
                    StringBuilder s = new StringBuilder();
                    int j;

                    // if the first number is negative, we need to push n in operator stack
                    if (i == 0 && ("nsct".contains(Character.toString(chr)))) {
                        operator.push(chr);
                        i++;
                    }// has to be written before j loop

                    chr = str.charAt(i);//if upper if executes for trigo functions, chr must change wrt i

                    for (j = i; j < str.length() && !("()+-*/^nstc".contains(Character.toString(str.charAt(j)))); j++) {
                        s.append(str.charAt(j));
                        count = 1;
                    }
                    if (count == 1)
                        i = j - 1;

                    if (chr == '(') {
                        operator.push(chr);
                    } else if (chr == ')') {
                        while (operator.peek() != '(') {
                            char c = operator.pop();
                            System.out.print(c + " popped for calculating in ) cond |");
                            double a, b, ans;
                            a = operands.pop();
                            // when special operation, only pop one value from operands and operate with it
                            if (c == 'n' || c == 's' || c == 'c' || c == 't') {
                                ans = special(a, c);
                            } else {
                                b = operands.pop();
                                ans = operate(b, a, c);
                            }
                            operands.push(ans);
                        }
                        operator.pop();
                    }
                    //if the value in s is parsable to double value, it will push the parsed s in stack
                    else if (s.toString().matches("-?\\d+(\\.\\d+)?")) {
                        operands.push(Double.parseDouble(s.toString()));
                    } else if ("+-*/^nsct".contains(Character.toString(chr))) {
                        while (operator.size() > 0 && operator.peek() != '(' && precede(chr) <= precede(operator.peek())) {
                            char c = operator.pop();
                            double a, b, ans;
                            a = operands.pop();
                            if (c == 'n' || c == 's' || c == 'c' || c == 't') {
                                ans = special(a, c);
                            } else {
                                b = operands.pop();
                                ans = operate(b, a, c);
                            }
                            operands.push(ans);
                        }
                        operator.push(chr);
                    }
                }
                while (operator.size() != 0) {
                    char c = operator.pop();
                    double a, b, ans;
                    a = operands.pop();
                    if (c == 'n' || c == 's' || c == 'c' || c == 't') {
                        ans = special(a, c);
                    } else {
                        b = operands.pop();
                        ans = operate(b, a, c);
                    }
                    operands.push(ans);
                }
                historyBar.setText(String.format("%s=%s", result.getText().toString(), operands.peek()));
                //result.setText(Double.toString(operands.peek()));
                result.setText(String.format("%s", operands.peek()));
                result.setSelection(Double.toString(operands.peek()).length());
            }
            catch (EmptyStackException e){
                result.setText(R.string.exception);
            }//end of catch
        }//end of else
    }//end of equalsBTN

    public void dotbtn(View view) {
        updateText(".");
    }

    public void doublezro(View view) {
        updateText("00");
    }

    public static String negative(String s) {

        for(int i=1; i<s.length()-1; i++){
            String st = "";
            int flag = 0;
            char ch = s.charAt(i);
            if("(sct".contains(Character.toString(ch)) && "0123456789)".contains(Character.toString(s.charAt(i-1)))){
                st = s.substring(0, i) + "*" + s.substring(i);
                flag = 1;
            }
            if(flag == 1)
                s = st;

            if(ch == ')' && ".0123456789".contains(Character.toString(s.charAt(i+1)))){
                st = s.substring(0, i+1) + "*" + s.substring(i+1);
                flag = 2;
            }
            if(flag == 2)
                s = st;

            if(ch == '.' && "())+-*/^".contains(Character.toString(s.charAt(i-1)))){
                st = s.substring(0, i) + "0" + s.substring(i);
                flag = 3;
            }
            if(flag == 3)
                s = st;
        }

        for (int i = 0; i < s.length() - 1; i++) {
            String st = "";
            int flag = 0;
            char ch = s.charAt(i);
            if (ch == '-' && (i == 0 || "+-*/^(".contains(Character.toString(s.charAt(i - 1))))//
                    && (Character.isDigit(s.charAt(i + 1)) || "(sct".contains(Character.toString(s.charAt(i + 1))))) {
                st = s.substring(0, i) + "n" + s.substring(i + 1);
                flag = 1;
            }
            if (flag == 1)
                s = st;

            if ("sct".contains(Character.toString(ch)) && (i == 0 || "+-*/^(n".contains(Character.toString(s.charAt(i - 1))))//
                    && s.charAt(i + 3) == '(') {
                st = s.substring(0, i + 1) + s.substring(i + 3);
                flag = 2;
            }
            if (flag == 2)
                s = st;
        }
        return s;
    }

    public static int precede(char ch) {
        if (ch == '+' || ch == '-')
            return 1;
        else if (ch == '*' || ch == '/')
            return 2;
        else if (ch == '^')
            return 3;
        else if (ch == 'n')
            return 4;// for 'n'
        else
            return 5;
    }

    public static double operate(double a, double b, char ch) {
        if (ch == '+')
            return b + a;
        else if (ch == '-')
            return a - b;
        else if (ch == '*')
            return a * b;
        else if (ch == '/')
            return a / b;
        else
            return (int) (Math.pow(a, b));
    }

    //for special operations
    public static double special(double a, char ch) {
        if (ch == 'n')
            return -a;
        else if (ch == 's')
            return Math.sin(Math.toRadians(a));
        else if (ch == 'c')
            return Math.cos(Math.toRadians(a));
        else
            return Math.tan(Math.toRadians(a));
    }
}