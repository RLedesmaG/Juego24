/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego24;
import java.util.*;

/**
 *
 * @author roberto
 */
public class Juego24 {
    
    final String[] patterns = {"nnonnoo", "nnonono", "nnnoono", "nnnonoo",
        "nnnnooo"};
    final String ops = "+-*/^";
 
    String solution;
    List<Integer> digits;

    
    public static void main(String[] args) {
        new Juego24().play();
   
    }
    
    void play() {
        
        System.out.print("Introduzca los números que desea evaluar: ");
        digits = getSolvableDigits();
 
        Scanner in = new Scanner(System.in);
        while (true) {
            
            System.out.println("Si se puede, presione s para mostrar solución o l para salir.");
            System.out.print("> ");
 
            String line = in.nextLine();
            if (line.equalsIgnoreCase("l")) {
                System.out.println("\nLol");
                return;
            }
 
            if (line.equalsIgnoreCase("s")) {
                System.out.println(solution);
                digits = getSolvableDigits();
                continue;
            }
        }   
    }
    boolean evaluate(char[] line) throws Exception {
        Stack<Float> s = new Stack<>();
        try {
            for (char c : line) {
                if(c == 'T')
                    s.push((float) 10 - '0');
                if(c == 'J')
                    s.push((float) 11 - '0');
                if(c == 'Q')
                    s.push((float) 12 - '0');
                if(c == 'K')
                    s.push((float) 13 - '0');
                if ('1' <= c && c <= '9')
                    s.push((float) c - '0');
                else
                    s.push(applyOperator(s.pop(), s.pop(), c));
            }
        } catch (EmptyStackException e) {
            throw new Exception("Invalid entry.");
        }
        
        return (Math.abs(24 - s.peek()) < 0.001F);
    }
    float applyOperator(float a, float b, char c) {
        switch (c) {
            case '+':
                return a + b;
            case '-':
                return b - a;
            case '*':
                return a * b;
            case '/':
                return b / a;
            default:
                return Float.NaN;
        }
    }
    public static int getValue(String numero){
        if("K".equals(numero))
            return 13;
        if("Q".equals(numero))
            return 12;
        if("J".equals(numero))
            return 11;
        if("T".equals(numero))
            return 10;
        if(Integer.parseInt(numero) > 0 && Integer.parseInt(numero) <= 9)
            return Integer.parseInt(numero);
        return 0;
        }
    List<Integer> randomDigits() {
        Scanner in = new Scanner(System.in);
        String uno = in.next();
        String dos = in.next();
        String tres = in.next();
        String cuatro = in.next();
        List<Integer> result = new ArrayList<>(4);
        
            result.add(getValue(uno));
            result.add(getValue(dos));
            result.add(getValue(tres));
            result.add(getValue(cuatro));
        return result;
    }
    List<Integer> getSolvableDigits() {
        List<Integer> result;
        do {
            result = randomDigits();
        } while (!isSolvable(result));
        return result;
    }
    
   
    
    boolean isSolvable(List<Integer> digits) {
        Set<List<Integer>> dPerms = new HashSet<>(4 * 3 * 2);
        permute(digits, dPerms, 0);
 
        int total = 4 * 4 * 4;
        List<List<Integer>> oPerms = new ArrayList<>(total);
        permuteOperators(oPerms, 4, total);
 
        StringBuilder sb = new StringBuilder(4 + 3);
 
        for (String pattern : patterns) {
            char[] patternChars = pattern.toCharArray();
            System.out.print(patternChars);
 
            for (List<Integer> dig : dPerms) {
                for (List<Integer> opr : oPerms) {
 
                    int i = 0, j = 0;
                    for (char c : patternChars) {
                        if (c == 'n')
                            
                            sb.append(dig.get(i++));
                        else
                            sb.append(ops.charAt(opr.get(j++)));
                        
                    }
 
                    String candidate = sb.toString();
                    try {
                        System.out.println(candidate);
                        if (evaluate(candidate.toCharArray())) {
                            
                            solution = postfixToInfix(candidate);
                            return true;
                        }
                    } catch (Exception ignored) {
                    }
                    sb.setLength(0);
                }
            }
        }
        System.out.println("No se puede.");
        return false;
    }
     String postfixToInfix(String postfix) {
        class Expression {
            String op, ex;
            int prec = 3;
 
            Expression(String e) {
                ex = e;
            }
 
            Expression(String e1, String e2, String o) {
                ex = String.format("%s %s %s", e1, o, e2);
                op = o;
                prec = ops.indexOf(o) / 2;
            }
        }
 
        Stack<Expression> expr = new Stack<>();
 
        for (char c : postfix.toCharArray()) {
            int idx = ops.indexOf(c);
            if (idx != -1) {
 
                Expression r = expr.pop();
                Expression l = expr.pop();
 
                int opPrec = idx / 2;
 
                if (l.prec < opPrec)
                    l.ex = '(' + l.ex + ')';
 
                if (r.prec <= opPrec)
                    r.ex = '(' + r.ex + ')';
 
                expr.push(new Expression(l.ex, r.ex, "" + c));
            } else {
                expr.push(new Expression("" + c));
            }
        }
        return expr.peek().ex;
    }
     void permute(List<Integer> lst, Set<List<Integer>> res, int k) {
        for (int i = k; i < lst.size(); i++) {
            Collections.swap(lst, i, k);
            permute(lst, res, k + 1);
            Collections.swap(lst, k, i);
        }
        if (k == lst.size())
            res.add(new ArrayList<>(lst));
    }
     void permuteOperators(List<List<Integer>> res, int n, int total) {
        for (int i = 0, npow = n * n; i < total; i++)
            res.add(Arrays.asList((i / npow), (i % npow) / n, i % n));
    }
}
