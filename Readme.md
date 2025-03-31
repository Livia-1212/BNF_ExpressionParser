# Expression Parser

A recursive descent parser and evaluator for arithmetic expressions written in Java. This parser follows a customized BNF grammar where `+` and `-` have higher precedence than `*` and `/`, reversing typical mathematical precedence.

## 📋 BNF Grammar

```bnf
<expression> ::= <factor> * <expression>
              |  <factor> / <expression>
              |  <factor>

<factor>     ::= <term> + <factor>
              |  <term> - <factor>
              |  <term>

<term>       ::= { <expression> }
              |  <literal>

<literal>    ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
```




## Features
Parses and evaluates numeric expressions

Supports {} for nested expressions

Correctly applies reversed operator precedence

Constructs an expression tree

Outputs the tree structure in DOT format (Graphviz compatible)

Supports float evaluation for accurate results


## ▶️ How to Run
1. Compile the program:
    ```bash
    javac src/ExpressionParser.java
    ```
2. Run the program:
    ```bash
    java -cp src ExpressionParser
    ```
3. Input an expression when prompted.
4. Visualization of the expression tree:\
   This project generates a Graphviz Dot file named `tree.dot` in the current directory. \
   You can visualize it using Graphviz tools by converting the expression tree to an image:
    ```bash
    dot -Tpng tree.dot -o tree.png
    ```
5.open tree.png


## 🧪 Example
```bash
Input: 4/2/2+8*2*3+1
Expression Tree: ((4.0 / 2.0) / 2.0 + 8.0) * (2.0 * (3.0 + 1.0))
Evaluated Result: 1.6
DOT file written to tree.dot
```



---

## License
MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

...

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND...

