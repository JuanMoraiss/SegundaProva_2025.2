import java.util.Scanner;

class Conta {
    int id;
    String cliente;
    double saldo;
    double limite;
}

class Operacao {
    int id;
    int idConta;
    char tipo; 
    double valor; 
}
public class Prova {
public static final int TAM_EXTRATO = 5;
public static final int TAM_CONTAS = 50;
public static final int TAM_OP = 1000;
public static Scanner sc = new Scanner(System.in);

// 1)
public static int cadastrarConta(Conta[] v, int tam) {
    if (tam == TAM_CONTAS) {
        System.out.println("Vetor de contas cheio!");
        return tam;
    }
    Conta c = new Conta();
    c.id = lerIdNovo(v, tam);
    System.out.println("Digite o nome do cliente: ");
    c.cliente = sc.nextLine();
    c.saldo = lerNaoNegativo("Digite o saldo: ");
    c.limite = lerNaoNegativo("Digite o limite: ");
    v[tam] = c;
    return tam + 1;
}

public static int lerIdNovo(Conta[] v, int tam) {
    int id;
    boolean duplicado;
    do {
        System.out.println("Digite o id: ");
        id = sc.nextInt();
        sc.nextLine();
        duplicado = buscaId(v, tam, id) != -1;
        if (duplicado) {
            System.out.println("ID já cadastrado!");
        }
    } while (duplicado);
    return id;
}

public static double lerNaoNegativo(String mensagem) {
    double valor;
    do {
        System.out.println(mensagem);
        valor = sc.nextDouble();
        sc.nextLine();
        if (valor < 0) {
            System.out.println("Valor não pode ser negativo!");
        }
    } while (valor < 0);
    return valor;
}

// 2)
public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
    int inicio = 0, fim = tam - 1, meio, comp;
    while (inicio <= fim) {
        meio = (inicio + fim) / 2;
        comp = v[meio].cliente.compareToIgnoreCase(x);
        if (comp == 0) {
            return meio;
        } else if (comp < 0) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }
    }
    return -1;
}

// 3)
public static void insertionSortCliente(Conta[] v, int tam) {
    Conta chave;
    int j;
    for (int i = 1; i <= tam - 1; i += 1) {
        chave = v[i];
        j = i - 1;
        while (j >= 0 && chave.cliente.compareToIgnoreCase(v[j].cliente) < 0) {
            v[j + 1] = v[j];
            j -= 1;
        }
        v[j + 1] = chave;
    }
}

// 4)
public static int filtraOperacoes(Operacao[] vOp, int tamOp, Operacao[] vOpFiltrado, int x) {
    int tamFiltrado = 0;
    for (int i = 0; i < tamOp; i += 1) {
        if (vOp[i].idConta == x) {
            vOpFiltrado[tamFiltrado] = vOp[i];
            tamFiltrado += 1;
        }
    }
    return tamFiltrado;
}

// 5)
public static void extrato(Conta[] vContas, int tamConta, Operacao[] vOp, int tamOp) {
    System.out.println("Digite o id da conta para o extrato: ");
    int idConta = sc.nextInt();
    sc.nextLine();
    int pos = buscaId(vContas, tamConta, idConta);
    if (pos == -1) {
        System.out.println("Conta não encontrada!");
        return;
    }
    Operacao[] vOpFiltrado = new Operacao[TAM_OP];
    int tamFiltrado = filtraOperacoes(vOp, tamOp, vOpFiltrado, idConta);
    int inicio = 0;
    if (tamFiltrado > TAM_EXTRATO) {
        inicio = tamFiltrado - TAM_EXTRATO;
    }
    double saldo = somarValores(vOpFiltrado, inicio);
    imprimirCabecalhoExtrato(vContas[pos]);
    imprimirSaldoAnterior(saldo);
    for (int i = inicio; i < tamFiltrado; i += 1) {
        saldo += vOpFiltrado[i].valor;
        imprimirLinhaOperacao(vOpFiltrado[i], saldo);
    }
    imprimirRodapeExtrato(saldo);
}

public static double somarValores(Operacao[] v, int n) {
    double soma = 0;
    for (int i = 0; i < n; i += 1) {
        soma += v[i].valor;
    }
    return soma;
}

public static char creditoOuDebito(double valor) {
    if (valor < 0) {
        return 'D';
    }
    return 'C';
}

public static String nomeOperacao(char tipo) {
    if (tipo == 'D') {
        return "Depósito";
    } else if (tipo == 'S') {
        return "Saque";
    }
    return "Transferência";
}

public static void imprimirCabecalhoExtrato(Conta c) {
    System.out.println("+----------------------------------------+");
    System.out.printf("| C O N T A%25sN.%03d|\n", "", c.id);
    System.out.println("+----------------------------------------+");
    System.out.printf("|CLIENTE: %-31s|\n", c.cliente);
    System.out.println("+----------------------------------------+");
    System.out.println("| ID | O P E R A Ç Ã O | VALOR | SALDO |");
    System.out.println("+----+-----------------+--------+--------+");
}

public static void imprimirSaldoAnterior(double saldo) {
    System.out.printf("|    |%-17s|        |%7.2f%c|\n", "Saldo Anterior",
            Math.abs(saldo), creditoOuDebito(saldo));
}

public static void imprimirLinhaOperacao(Operacao op, double saldo) {
    System.out.printf("|%04d|%-17s|%7.2f%c|%7.2f%c|\n", op.id, nomeOperacao(op.tipo),
            Math.abs(op.valor), creditoOuDebito(op.valor),
            Math.abs(saldo), creditoOuDebito(saldo));
}

public static void imprimirRodapeExtrato(double saldo) {
    System.out.println("+----+-----------------+--------+--------+");
    System.out.printf("%-32s|%7.2f%c|\n", "Saldo Atual", Math.abs(saldo), creditoOuDebito(saldo));
    System.out.printf("%32s+--------+\n", "");
}
}