package logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws IOException {
        System.out.println("Insira o endereço do diretório: ");

        // Lê o diretório do arquivo que o usuario informa
        File diretorio = getFileDirectory();

        // Valida os arquivos de log e deleta os arquivos com mais de 30 dias
        validateLogFiles(diretorio);

    }

    
    //Método para ler o diretório do usuário
    private static File getFileDirectory() {
        Scanner ler = new Scanner(System.in);
        File diretorio;
        try {
            diretorio = new File(ler.nextLine());
        } finally {
            ler.close();
        }

        return diretorio;
    }

    
    //Método de exclusão do arquivo .log
    private static void validateLogFiles(File diretorio) throws IOException {
        FileWriter writer = new FileWriter(new File("error.log"));
        LocalDate hoje = LocalDate.now();

        try {
            for (File file1 : Objects.requireNonNull(diretorio.listFiles())) {
                LocalDate dataArquivo = new Date(file1.lastModified()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long meses = dataArquivo.until(hoje, ChronoUnit.MONTHS);
                String sufixo = getFileExtension(file1.getAbsolutePath());

                if (meses >= 1 && sufixo.equals("log")) {
                    System.out.println("Deletando...");
                    file1.delete();
                }
            }
        } catch (Exception exception) {
            writer.write(exception.getMessage());
            System.out.println("Não foi possível completar a ação, acesse 'error.log' para mais detalhes.");
        } finally {
            writer.close();
        }

    }

    // Método pra pegar o sufixo do arquivo
    private static String getFileExtension(String arquivo) {
        if (arquivo.contains(".")) {
            return arquivo.substring(arquivo.lastIndexOf(".") + 1);
        } else {
            return "Não foi possível obter a extensão do arquivo " + arquivo;
        }

    }
}