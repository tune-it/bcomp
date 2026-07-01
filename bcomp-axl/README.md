# axl — a C-like language for the Basic Computer (v2-ng)

`axl` is a small C-like language that compiles to **v2-ng assembler** for the Basic
Computer (БЭВМ). The compiler front-end is an ANTLR4 grammar
([`Axl.g4`](src/main/antlr4/ru.ifmo.cs.bcomp.axl/Axl.g4)); the code generator
([`Gen.java`](src/main/java/ru/ifmo/cs/bcomp/axl/Gen.java)) emits assembler text that is then
assembled by the `bcomp-assembler` module.

This document describes the syntax that is currently supported. Runnable programs live in
[`examples/`](examples).

## Running

### GUI tab

The **AXL Compiler** tab is optional and is shown only when the emulator is launched with the
`axl` mode key:

```
java -Dmode=axl -jar bcomp-ng-app-2.45.jar
```

Without `-Dmode=axl` (e.g. the default `-Dmode=gui`) the tab is hidden. Add `-Dlocale=ru` or
`-Dlocale=en` to switch the interface language.

In the tab: edit source in the top pane, press **Compile** to see the generated assembler in the
bottom pane, or **Load into computer** to compile and load the program into the machine. All
status/error lines in the output pane are prefixed with the assembler comment marker `;`, so the
whole pane can be copied verbatim into the **Assembler** tab and still assembles.

### Command line

```
java -cp bcomp-ng-app-2.45.jar ru.ifmo.cs.bcomp.axl.Cli program.axl        # assemble + dump binary
java -cp bcomp-ng-app-2.45.jar ru.ifmo.cs.bcomp.axl.Cli program.axl --asm  # print generated assembler
```

## Program structure

A program is a sequence of top-level declarations. A `main` function is required and is the entry
point.

```c
int result;                 // global variable

int square(int x) {         // function
    return x * x;
}

void main() {
    result = square(7);
    halt();                 // stop the machine
}
```

## Types

| Type   | Meaning                          |
|--------|----------------------------------|
| `int`  | 16-bit signed word               |
| `uint` | 16-bit unsigned word             |
| `char` | byte-sized value                 |
| `long` | 32-bit value (see *Limitations*) |
| `void` | no value (function return only)  |

Pointers are written with `*` (`int *p`, `char **pp`). Arrays are declared with a size
(`int data[5]`).

## Declarations

### Variables

Variables may have initializers. Brace initializers for arrays work on **global** arrays only
(see *Limitations*):

```c
int counter = 0;
int table[3] = {10, 20, 30};   // global array initializer

void main() {
    int i;              // local
    int *p;             // pointer local
    p = &counter;       // address-of
    *p = 42;            // dereference
    halt();
}
```

### Directives: `org` and `word`

`org N;` sets the origin (load address) of the generated code (default `0x10`).
`word ADDR : v1, v2, ...;` places raw words at a fixed address; each value is an integer or the
address of a label written as `&name`. This is typically used for the interrupt vector:

```c
org 0x100;

word 0x00 : &timer_handler, 0x00;   // interrupt vector -> handler

int ticks;

void timer_handler() {
    ticks = ticks + 1;
    iret();
}

void main() {
    ticks = 0;
    ei();               // enable interrupts
    halt();
}
```

## Statements

`if` / `else`, `while`, `do { ... } while (...)`, `for (init; cond; update)`, `return`, `break`,
`continue`, `goto label;` with `label:` targets, block statements `{ ... }`, and the empty
statement `;`.

```c
void main() {
    int i, acc;
    acc = 0;
    for (i = 0; i < 10; i = i + 1) {
        if (i == 5) continue;
        if (i > 7) break;
        acc = acc + i;
    }
    halt();
}
```

## Operators

From highest to lowest precedence:

| Group            | Operators                                             |
|------------------|-------------------------------------------------------|
| Postfix / call   | `f(...)`  `a[i]`  `x++`  `x--`                         |
| Prefix (unary)   | `-`  `+`  `~`  `!`  `*` (deref)  `&` (address)  `++`  `--` |
| Multiplicative   | `*`  `/`  `%`                                          |
| Additive         | `+`  `-`                                               |
| Shift            | `<<`  `>>`                                             |
| Relational       | `<`  `<=`  `>`  `>=`                                   |
| Equality         | `==`  `!=`                                             |
| Bitwise          | `&`  then `^`  then `\|`                               |
| Logical          | `&&`  then `\|\|`                                      |
| Assignment       | `=`  `+=`  `-=`  `*=`  `/=`  `%=`  `&=`  `\|=`  `^=`  `<<=`  `>>=` |

## Literals and comments

- Integers: decimal (`42`) or hexadecimal (`0xABCD`).
- Character literals: `'a'`, with escapes such as `'\n'`.
- Comments: `// line` and `/* block */`.

## Intrinsics

Intrinsics are built-in functions that map directly to machine instructions
([`Intrinsics.java`](src/main/java/ru/ifmo/cs/bcomp/axl/Intrinsics.java)). Their names are
reserved.

**Accumulator operations** — evaluate the argument, apply the op, leave the result in the
accumulator: `inc`, `dec`, `neg`, `not_`, `sxtb`, `swab`, `rol`, `ror`, `asl`, `asr`.

**Control / flags** (no arguments): `cla`, `clc`, `cmc`, `nop`, `halt`, `ei`, `di`, `iret`,
`ret`, `swap_top`.

**Stack**: `push(x)`, `pop()`, `pushf()`, `popf()`.

**I/O** (device number is a constant): `in(dev)`, `out(dev, value)`, `softint(n)`.

**Memory** (operand is a global variable): `adc(var)`, `swam(var)`, `loop_dec(var)`.

**Loads with addressing modes**:

| Intrinsic          | Assembler   | Mode                        |
|--------------------|-------------|-----------------------------|
| `ld_abs(var)`      | `LD $var`   | absolute                    |
| `ld_rel(var)`      | `LD var`    | relative / direct           |
| `ld_ind(var)`      | `LD (var)`  | indirect                    |
| `ld_postinc(var)`  | `LD (var)+` | indirect, post-increment    |
| `ld_predec(var)`   | `LD -(var)` | indirect, pre-decrement     |
| `ld_sp(n)`         | `LD &n`     | SP-relative (`n` constant)  |
| `ld_imm(n)`        | `LD #n`     | immediate (`n` constant)    |

**Flag predicates** — return `1` if the flag is set, otherwise `0`: `zero`, `notzero`,
`negative`, `positive`, `carry`, `nocarry`, `overflow`, `nooverflow`.

```c
int value, doubled;

void main() {
    value = in(4);          // read device 4
    doubled = asl(value);   // arithmetic shift left
    out(2, doubled);        // write device 2
    halt();
}
```

## Limitations

- A `main` function is required and must take no arguments.
- `long` cannot be used as a function return type or as a function parameter type
  (`long` globals, locals, and `long` arithmetic are supported).
- Local arrays, `long` locals, and locals whose address is taken (`&x`) are placed in
  **static** storage (one cell per variable, labelled `gl_<func>_<var>`), not on the stack.
  Consequently a function that uses any of them is **not re-entrant**: recursion — or
  re-entry from an interrupt — overwrites those variables. Recursion is safe only for plain
  scalar locals, which live on the stack.
- Local arrays cannot have an initializer (a code-generator restriction, not a storage one —
  they are stored statically like globals); declare the array, then fill it element by element
  (`int a[3];` then `a[0] = ...;`). Global arrays may use a brace initializer.
- Arrays are single-dimensional only — `a[i][j]` is not supported.
- There is no conditional (ternary) operator `?:`.
- There are no string literals; use character literals (`'a'`) or a `word` data block.
- Intrinsic names are reserved and cannot be used as function names.

## Examples

See [`examples/`](examples):

| File                   | Shows                                             |
|------------------------|--------------------------------------------------|
| `arithmetic.axl`       | basic arithmetic on globals                      |
| `factorial.axl`        | functions and iteration                          |
| `recursion_fib.axl`    | recursion                                         |
| `control_flow.axl`     | `for`, `if`, `break`, `continue`                  |
| `arrays_pointers.axl`  | arrays, pointers, pointer arithmetic             |
| `bitops.axl`           | bitwise operators and `uint`                     |
| `long_math.axl`        | `long` arithmetic                                 |
| `io.axl` / `intrinsics_io.axl` | `in` / `out` and intrinsics              |
| `org_placement.axl`    | `org` / `word` directives, interrupt handler     |

```c
// factorial.axl
int result;

int fact(int n) {
    if (n <= 1) return 1;
    return n * fact(n - 1);
}

void main() {
    result = fact(5);
    halt();
}
```
