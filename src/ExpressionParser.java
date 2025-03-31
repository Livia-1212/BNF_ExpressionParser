import java.util.Scanner;

abstract class Node {
    public abstract float eval();
    public abstract String toString();
    public abstract void toDot(StringBuilder sb, int id, DotHelper helper);
}

class DotHelper {
    int counter = 0;
    public int getNextId() {
        return counter++;
    }
}

class LiteralNode extends Node {
    private float value;

    public LiteralNode(float value) {
        this.value = value;
    }

    public float eval() {
        return value;
    }

    public String toString() {
        return Float.toString(value);
    }

    public void toDot(StringBuilder sb, int id, DotHelper helper) {
        sb.append("  node" + id + " [label=\"" + value + "\"];");
    }
}

class BinOpNode extends Node {
    private char op;
    private Node left, right;

    public BinOpNode(char op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public float eval() {
        switch (op) {
            case '+': return left.eval() + right.eval();
            case '-': return left.eval() - right.eval();
            case '*': return left.eval() * right.eval();
            case '/': return left.eval() / right.eval();
            default: throw new RuntimeException("Unknown operator: " + op);
        }
    }

    public String toString() {
        return "(" + left.toString() + " " + op + " " + right.toString() + ")";
    }

    public void toDot(StringBuilder sb, int id, DotHelper helper) {
        sb.append("  node" + id + " [label=\"" + op + "\"];");

        int leftId = helper.getNextId();
        left.toDot(sb, leftId, helper);
        sb.append("  node" + id + " -> node" + leftId + ";");

        int rightId = helper.getNextId();
        right.toDot(sb, rightId, helper);
        sb.append("  node" + id + " -> node" + rightId + ";");
    }
}

class Tokenizer {
    private String input;
    private int pos;

    public Tokenizer(String input) {
        this.input = input.replaceAll("\\s+", "");
        this.pos = 0;
    }

    public char peek() {
        return (pos < input.length()) ? input.charAt(pos) : '\0';
    }

    public char next() {
        return (pos < input.length()) ? input.charAt(pos++) : '\0';
    }

    public boolean match(char expected) {
        if (peek() == expected) {
            next();
            return true;
        }
        return false;
    }
}

class Parser {
    private Tokenizer tokenizer;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Node parseExpression() {
        Node left = parseFactor();
        while (true) {
            char op = tokenizer.peek();
            if (op == '*' || op == '/') {
                tokenizer.next();
                Node right = parseFactor();
                left = new BinOpNode(op, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    public Node parseFactor() {
        Node left = parseTerm();
        while (true) {
            char op = tokenizer.peek();
            if (op == '+' || op == '-') {
                tokenizer.next();
                Node right = parseTerm();
                left = new BinOpNode(op, left, right);
            } else {
                break;
            }
        }
        return left;
    }

    public Node parseTerm() {
        if (tokenizer.match('{')) {
            Node node = parseExpression();
            if (!tokenizer.match('}')) {
                throw new RuntimeException("Expected '}'");
            }
            return node;
        } else {
            char digit = tokenizer.next();
            if (Character.isDigit(digit)) {
                return new LiteralNode((float) Character.getNumericValue(digit));
            } else {
                throw new RuntimeException("Expected digit but found: " + digit);
            }
        }
    }
}

public class ExpressionParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expression: ");
        String input = scanner.nextLine();
        scanner.close();

        try {
            Tokenizer tokenizer = new Tokenizer(input);
            Parser parser = new Parser(tokenizer);
            Node tree = parser.parseExpression();

            System.out.println("Expression Tree: " + tree.toString());
            System.out.println("Evaluated Result: " + tree.eval());
            writeDotFile(tree, "tree.dot");
            System.out.println("DOT file written to tree.dot");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void writeDotFile(Node tree, String filename) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ExpressionTree {\n");

        DotHelper helper = new DotHelper();
        int rootId = helper.getNextId();
        tree.toDot(sb, rootId, helper);

        sb.append("}\n");

        java.nio.file.Files.write(java.nio.file.Paths.get(filename), sb.toString().getBytes());
    }
}
