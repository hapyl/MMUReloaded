package me.hapyl.mmu3.feature;

import me.hapyl.mmu3.Main;
import me.hapyl.mmu3.message.Message;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.chat.LazyClickEvent;
import me.hapyl.spigotutils.module.chat.LazyHoverEvent;
import org.bukkit.entity.Player;

public class Calculate extends Feature {

    private final char[] validArguments = {
            '-', // minus
            '+', // plus
            '*', // multiply
            '/', // divide
            '^', // power
            '(', // left parenthesis
            ')', // right parenthesis
            '%'  // modulo
    };

    private final String argumentColor = "&a";
    private final String numberColor = "&b";

    public Calculate(Main mmu3plugin) {
        super(mmu3plugin);
    }

    public void buildStringAndEvaluate(Player player, String[] args) {
        if (args.length > 50) {
            Message.TOO_MANY_ARGUMENTS_EXPECTED_NOT_MORE_THAN.send(player, 50);
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(literalToInteger(arg));
            builder.append(" ");
        }

        String expression = builder.toString().trim();
        double answer;

        try {
            answer = evaluate(expression);
        } catch (RuntimeException e) {
            Message.error(player, "Unable to evaluate! %s.", e.getMessage());
            return;
        }

        // Color expression
        for (char validArgument : validArguments) {
            expression = expression.replace(validArgument + "", argumentColor + validArgument + numberColor);
        }

        Chat.sendMessage(player, Message.PREFIX + "&a→ &b" + expression.replace("%", "%%")); // I have java sometimes

        Chat.sendClickableHoverableMessage(
                player,
                LazyClickEvent.SUGGEST_COMMAND.of(String.valueOf(answer)),
                LazyHoverEvent.SHOW_TEXT.of("&7Click to copy!"),
                Message.PREFIX + "&2← " + answer
        );
    }

    private String literalToInteger(String literal) {
        return switch (literal.toLowerCase()) {
            case "pi" -> Math.PI + "";
            case "e" -> Math.E + "";
            default -> literal;
        };
    }

    private double evaluate(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

            // Grammar:
            // expression = term
            // expression `+` term
            // expression `-` term
            // term = factor
            // term `*` factor
            // term `/` factor
            // factor = `+` factor |
            // `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) {
                        x += parseTerm(); // addition
                    }
                    else if (eat('-')) {
                        x -= parseTerm(); // subtraction
                    }
                    else if (eat('%')) {
                        x %= parseTerm(); // modulo
                    }
                    else {
                        return x;
                    }
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) {
                        x *= parseFactor(); // multiplication
                    }
                    else if (eat('/')) {
                        x /= parseFactor(); // division
                    }
                    else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) {
                    return +parseFactor(); // unary plus
                }
                if (eat('-')) {
                    return -parseFactor(); // unary minus
                }

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) {
                        throw new RuntimeException("Missing ')'");
                    }
                }
                else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z')
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) {
                            throw new RuntimeException("Missing ')' after argument to " + func);
                        }
                    }
                    else {
                        x = parseFactor();
                    }
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                }
                else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) {
                    x = Math.pow(x, parseFactor()); // exponentiation
                }

                return x;
            }
        }.parse();
    }

}
